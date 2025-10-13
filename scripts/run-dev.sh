#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
INFRA_DIR="$ROOT_DIR/infra"
API_DIR="$ROOT_DIR/api-java"
WEB_DIR="$ROOT_DIR/app"

API_PORT="${API_PORT:-8080}"
WEB_PORT="${WEB_PORT:-5173}"
DB_PORT="${DB_PORT:-5432}"

# ---- helpers ---------------------------------------------------------------
log() { printf "\033[1;36m[dev]\033[0m %s\n" "$*"; }
err() { printf "\033[1;31m[dev]\033[0m %s\n" "$*" >&2; }

need() {
  command -v "$1" >/dev/null 2>&1 || {
    err "Missing dependency: $1"; exit 1;
  }
}

wait_for_port() {
  local host="$1" port="$2" name="$3" tries=60
  log "Waiting for $name on $host:$port ..."
  for _ in $(seq 1 "$tries"); do
    (echo >/dev/tcp/$host/$port) >/dev/null 2>&1 && { log "$name is up"; return 0; }
    sleep 1
  done
  err "Timed out waiting for $name on $host:$port"
  return 1
}

cleanup() {
  local code=$?
  log "Shutting down (signal caught) ..."
  [[ -n "${API_PID:-}" ]] && kill "$API_PID" >/dev/null 2>&1 || true
  [[ -n "${WEB_PID:-}" ]] && kill "$WEB_PID" >/dev/null 2>&1 || true
  wait >/dev/null 2>&1 || true
  log "Done."
  exit "$code"
}

# ---- preflight -------------------------------------------------------------
need docker
need bash
need awk
need grep
need sed

# Prefer Maven wrapper if present; otherwise mvn
if [[ -x "$API_DIR/mvnw" ]]; then MVN_CMD="./mvnw"; else need mvn; MVN_CMD="mvn"; fi

# Node/yarn/npm check
if command -v pnpm >/dev/null 2>&1; then PKG="pnpm"; RUN_DEV="pnpm dev"
elif command -v yarn >/dev/null 2>&1; then PKG="yarn"; RUN_DEV="yarn dev"
else need npm; PKG="npm"; RUN_DEV="npm run dev"; fi

trap cleanup INT TERM

# ---- start services --------------------------------------------------------
log "Starting Postgres via docker compose ..."
docker compose -f "$INFRA_DIR/docker-compose.yml" up -d

wait_for_port "127.0.0.1" "$DB_PORT" "Postgres"

# API
log "Starting Spring Boot API on :$API_PORT ..."
(
  cd "$API_DIR"
  $MVN_CMD -q spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.port=$API_PORT"
) &
API_PID=$!
log "API PID = $API_PID"
wait_for_port "127.0.0.1" "$API_PORT" "API"

# Web
log "Ensuring app/.env exists ..."
[[ -f "$WEB_DIR/.env" ]] || { cp "$WEB_DIR/.env.example" "$WEB_DIR/.env" 2>/dev/null || true; }
# Ensure API base URL is set
if ! grep -q "^VITE_API_BASE_URL=" "$WEB_DIR/.env" 2>/dev/null; then
  echo "VITE_API_BASE_URL=http://localhost:$API_PORT" >> "$WEB_DIR/.env"
fi

log "Installing web deps and starting Vite on :$WEB_PORT ..."
(
  cd "$WEB_DIR"
  $PKG install
  $RUN_DEV -- --port "$WEB_PORT"
) &
WEB_PID=$!
log "Web PID = $WEB_PID"

wait_for_port "127.0.0.1" "$WEB_PORT" "Web (Vite dev server)"

log "All set!
- API: http://localhost:$API_PORT (Swagger UI at /swagger-ui)
- Web: http://localhost:$WEB_PORT

Press Ctrl-C to stop both."

# ---- wait on background jobs ----------------------------------------------
wait
