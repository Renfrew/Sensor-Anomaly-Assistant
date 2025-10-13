#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
OUT="$ROOT_DIR/openapi/types.ts"
API_URL="${API_URL:-http://localhost:8080}"
npx --yes openapi-typescript "$API_URL/v3/api-docs" -o "$OUT"
echo "[gen-types] Wrote $OUT"
