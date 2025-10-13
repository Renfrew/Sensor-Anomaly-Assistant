# Sensor Anomaly Assistant

Drop in time-series data, get clean charts, **explainable** anomaly alerts, and Slack/email notifications.
Backend: **Java 25 + Spring Boot 3 + PostgreSQL 16**.  
Frontend: **React 18 + TypeScript (Vite)**.  
Monorepo: `app/` (web) + `api-java/` (API) + `infra/` (Docker).

---

## Day-1 status

- ✅ Monorepo skeleton
- ✅ Postgres via Docker Compose
- ✅ Spring Boot app boots and connects to DB
- ✅ Flyway migration `V1__init.sql` with base tables + indexes
- ✅ Basic `/health` endpoint
- 🟨 React app boots; API base URL wired
- ⏭️ Next: `/devices` + `/readings` (GET/POST), CSV ingest, keyset pagination

---

## Quick start (local)

Prereqs: Node 20+, Java 21, Docker.

```bash
# 1) Clone and boot DB
git clone <your-repo-url> && cd sensor-anomaly-assistant
docker compose -f infra/docker-compose.yml up -d

# 2) Run API (port 8080)
cd api-java
./mvnw spring-boot:run   # or ./gradlew bootRun

# 3) Run Web (port 5173)
cd ../app
cp .env.example .env
npm i
npm run dev
