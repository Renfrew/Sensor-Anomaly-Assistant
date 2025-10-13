create extension if not exists pgcrypto;

create table devices (
  id uuid primary key default gen_random_uuid(),
  name text not null,
  description text,
  created_at timestamptz not null default now()
);

create table readings (
  id bigserial primary key,
  device_id uuid not null references devices(id) on delete cascade,
  ts timestamptz not null,
  value double precision not null,
  severity int not null default 0,
  reason text,
  created_at timestamptz not null default now()
);
create index if not exists idx_readings_device_ts_desc on readings (device_id, ts desc);
create index if not exists idx_readings_device_sev_ts on readings (device_id, severity, ts desc);

create table alerts (
  id uuid primary key default gen_random_uuid(),
  device_id uuid not null references devices(id),
  first_ts timestamptz not null,
  last_ts timestamptz not null,
  severity int not null,
  rule_key text not null,
  sample_count int not null,
  explanation text not null,
  ai_summary text,
  created_at timestamptz not null default now()
);

create table channels (
  id uuid primary key default gen_random_uuid(),
  device_id uuid references devices(id) on delete cascade,
  type text not null check (type in ('email','slack')),
  target text not null,
  cooldown_minutes int not null default 60,
  last_sent_at timestamptz
);

-- simple users (replace with real IdP later)
create table users (
  id uuid primary key default gen_random_uuid(),
  email text unique not null,
  password_hash text not null,
  role text not null check (role in ('VIEWER','EDITOR','ADMIN')),
  created_at timestamptz not null default now()
);