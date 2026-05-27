# My Management System

This repository is now organized as a small microservices parent project instead of one monolith.

## Current structure

- `pom.xml` - parent Maven project
- `eureka-server/` - Spring Cloud Eureka registry on port `8761`
- `api-gateway/` - Spring Cloud Gateway entry point on port `8080`
- `auth-service/` - authentication service on port `8081`
- `user-service/` - user profile service on port `8082`
- `legacy/` - archived scripts, old docs, and prior monolith support files

## Service responsibilities

- `auth-service`
	- Own database: `auth_db`
	- Login and token validation APIs
	- Can call `user-service` when needed
- `user-service`
	- Own database: `user_db`
	- User profile APIs
	- Can call `auth-service` to validate tokens

## Run

Run each service in its own terminal:

```powershell
cd d:\jfk(microservices)\eureka-server
mvn spring-boot:run
```

```powershell
cd d:\jfk(microservices)\api-gateway
mvn spring-boot:run
```

```powershell
cd d:\jfk(microservices)\auth-service
mvn spring-boot:run
```

```powershell
cd d:\jfk(microservices)\user-service
mvn spring-boot:run
```

The gateway routes `/auth/**` to auth-service and `/users/**` to user-service through Eureka.
The services still register with Eureka and call each other through discovery rather than hard-coded localhost URLs.

## UI

In local development, the browser UI is served from the gateway at `http://localhost:8080/`.

In Docker, the same UI is available at the mapped host port `http://localhost:8080/`, and the Eureka dashboard is available at `http://localhost:8761/`.

It lets you:

- log in through `/auth/login`
- validate tokens through `/auth/validate-token`
- browse auth users through `/auth/users`
- fetch user profiles through `/auth/users/{id}`

## Deployment variables

Set these on Render or any other host so the services can communicate outside localhost:

- `EUREKA_SERVER_URL` for both services, for example `http://your-eureka-host:8761/eureka`
- `AUTH_JWT_SECRET` for signing access tokens
- `AUTH_JWT_TTL_SECONDS` for access-token lifetime
- `SPRING_DATASOURCE_URL` or the `MYSQLHOST`/`MYSQLPORT`/`MYSQLDATABASE` trio for the auth and user services
- `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD` (or `MYSQLUSER` / `MYSQLPASSWORD`)

## Render deployment

This repo now includes `render.yaml` so Render can create all services as a Blueprint.

1. In Render, click **New +** -> **Blueprint**.
2. Select this GitHub repository.
3. Render will detect `render.yaml` and show 4 services:
	 - `eureka-server`
	 - `api-gateway`
	 - `auth-service`
	 - `user-service`
4. Fill required env vars (`sync: false`) before first deploy:
	 - For `auth-service`:
		 - `EUREKA_SERVER_URL` (for example `https://<eureka-host>/eureka`)
		 - `AUTH_JWT_SECRET`
		 - `SPRING_DATASOURCE_URL`
		 - `SPRING_DATASOURCE_USERNAME`
		 - `SPRING_DATASOURCE_PASSWORD`
	 - For `user-service`:
		 - `EUREKA_SERVER_URL`
		 - `SPRING_DATASOURCE_URL`
		 - `SPRING_DATASOURCE_USERNAME`
		 - `SPRING_DATASOURCE_PASSWORD`
	 - For `api-gateway`:
		 - `EUREKA_SERVER_URL`

Notes:
- Do not use `localhost` in datasource URLs on Render.
- `auth-service`, `user-service`, `api-gateway`, and `eureka-server` each have their own Dockerfile now under their module folders.

## Docker

The root [Dockerfile](Dockerfile) builds one service at a time using the `SERVICE_DIR` build argument.

The Docker stack also starts a MySQL server and creates the `auth_db` and `user_db` databases for the two services.

Build `auth-service`:

```powershell
docker build --build-arg SERVICE_DIR=auth-service -t my-management-auth .
```

Build `user-service`:

```powershell
docker build --build-arg SERVICE_DIR=user-service -t my-management-user .
```

To run the full stack with Eureka on a server or locally:

```powershell
docker compose up --build
```

That starts `eureka-server` on `8761`, `api-gateway` on `8080`, `auth-service` on `8081`, and `user-service` on `8082`.
It also starts MySQL on host port `3307`.

After startup, open:

- Eureka dashboard: `http://localhost:8761/`
- Gateway UI: `http://localhost:8080/`
- Auth UI: `http://localhost:8081/`
- User service API: `http://localhost:8082/`

## Notes

- The old monolith code has been removed from the root to keep the repository focused on the two services.
- The services now use MySQL databases so each service has its own persistent schema in the Docker stack.
- The token flow is still a scaffold and can be upgraded to JWT or OAuth next.

