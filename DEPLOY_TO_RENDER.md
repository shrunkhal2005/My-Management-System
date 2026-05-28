Render deployment: quick fix (Postgres) and MySQL notes
=====================================================

Goal
----
Get `auth-service` and `user-service` running on Render by provisioning a reachable database.

Recommended (fastest)
---------------------
1. Create a Postgres database on Render (Dashboard → New → PostgreSQL).
2. Copy the connection details (host, port, database, user, password).
3. In each Web Service (auth-service, user-service) on Render, set these environment variables:

- `SPRING_DATASOURCE_URL=jdbc:postgresql://<HOST>:5432/<DBNAME>`
- `SPRING_DATASOURCE_USERNAME=<USER>`
- `SPRING_DATASOURCE_PASSWORD=<PASSWORD>`
- `SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect`

Notes:
- The services already include a Postgres JDBC driver in `pom.xml`, so no code change is required.
- Redeploy the service after setting env vars.

MySQL options (if you must use MySQL)
-----------------------------------
- Preferred: use a managed MySQL (cloud provider, RDS) and set `SPRING_DATASOURCE_URL` to:

  `jdbc:mysql://<HOST>:3306/auth_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`

  And set `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD` accordingly.

- Running MySQL as a separate Render Web Service is problematic unless you:
  - Use Render Private Services (paid) so services can privately communicate, OR
  - Run MySQL inside the same container that runs your app (single Dockerfile that starts `mysqld` before the jar) — fragile and not recommended.

Troubleshooting
---------------
- If logs show `Communications link failure` or `Connection refused`, the app cannot reach the DB host. Confirm the DB host and port are reachable from the app container.
- Check the Render service logs (Deploy → View Logs) for the exact `SPRING_DATASOURCE_URL` used and connection attempts.

Deploy steps (summary)
----------------------
1. Create the DB on Render or obtain an external DB endpoint.
2. In Render, open the Web Service for `auth-service` → Environment → Add the env vars above.
3. Repeat for `user-service`.
4. Redeploy the services or push a new commit to trigger rebuild.

Commands (git)
--------------
```bash
git add -A
git commit -m "docs: add Render deploy guide"
git push
```

If you want, I can:
- (A) Patch `application.properties` to set a Postgres-first default and commit it, or
- (B) Implement a single-container startup (start MySQL then the app), or
- (C) Walk you through creating the Render Postgres and setting env vars interactively.
