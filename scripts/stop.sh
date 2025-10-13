#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
echo "[stop] Stopping docker compose services ..."
docker compose -f "$ROOT_DIR/infra/docker-compose.yml" down
echo "[stop] Done."
