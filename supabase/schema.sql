create table if not exists public.app_users (
    id bigserial primary key,
    username varchar(80) not null unique,
    password varchar(120) not null,
    full_name varchar(120) not null,
    role varchar(20) not null check (role in ('ADMIN', 'ENGINEER'))
);

create table if not exists public.daily_activities (
    id bigserial primary key,
    activity_date date not null,
    engineer_name varchar(120) not null,
    application_name varchar(140) not null,
    application_status varchar(20) not null check (application_status in ('WORKING', 'NOT_WORKING')),
    server_health_checked boolean not null,
    cpu_usage integer not null check (cpu_usage between 0 and 100),
    ram_usage integer not null check (ram_usage between 0 and 100),
    disk_space_status varchar(120) not null,
    services_checked boolean not null,
    database_connectivity_checked boolean not null,
    backup_status varchar(20) not null check (backup_status in ('SUCCESS', 'FAILED', 'NOT_CHECKED')),
    logs_checked boolean not null,
    errors_found varchar(1000),
    tickets_received integer not null default 0,
    tickets_resolved integer not null default 0,
    pending_tickets integer not null default 0,
    escalated_issues varchar(1000),
    remarks varchar(1200),
    updated_by varchar(120) not null,
    status varchar(20) not null check (status in ('COMPLETED', 'PENDING')),
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);

create index if not exists idx_daily_activities_date on public.daily_activities(activity_date);
create index if not exists idx_daily_activities_engineer on public.daily_activities(engineer_name);
create index if not exists idx_daily_activities_application on public.daily_activities(application_name);
create index if not exists idx_daily_activities_status on public.daily_activities(status);

alter table public.app_users enable row level security;
alter table public.daily_activities enable row level security;
