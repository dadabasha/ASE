# Application Support Engineer Daily Activity Tracker

Spring Boot, Bootstrap, and Supabase Postgres application for recording daily support activities, viewing operational dashboard metrics, and exporting reports.

## Features

- Admin and support engineer login
- Daily activity entry form
- Dashboard metrics for today's activity, completed and pending work, failed backups, and ticket counts
- Searchable daily records table
- Daily, weekly, and monthly reports
- CSV export for Excel and PDF export
- Admin user creation and all-record visibility

## Supabase Setup

1. Create a Supabase project.
2. Open the SQL editor and run `supabase/schema.sql`.
3. Optionally run `supabase/sample-data.sql`.
4. Copy your Supabase Postgres JDBC connection details.
5. Start the app with environment variables:

```powershell
$env:SUPABASE_DB_URL="jdbc:postgresql://aws-1-ap-south-1.pooler.supabase.com:5432/postgres?sslmode=require"
$env:SUPABASE_DB_USER="postgres.lcumwxkwvtalrtxixmip"
$env:SUPABASE_DB_PASSWORD="your-database-password"
mvn spring-boot:run
```

The app also seeds demo users and records automatically when the connected database is empty.

## Demo Logins

- Admin: `admin` / `admin123`
- Engineer: `engineer` / `engineer123`

## API Summary

- `POST /api/auth/login`
- `GET /api/auth/me`
- `POST /api/auth/logout`
- `GET /api/activities`
- `POST /api/activities`
- `PUT /api/activities/{id}`
- `DELETE /api/activities/{id}`
- `GET /api/activities/dashboard`
- `GET /api/admin/users`
- `POST /api/admin/users`
- `GET /api/reports?period=DAILY|WEEKLY|MONTHLY`
- `GET /api/reports/export?period=DAILY&format=excel|pdf`
