Microservices scaffold

This repository now contains two simple Spring Boot services for initial microservices work:

- `auth-service` (port 8081): uses its own `auth_db` H2 database and exposes:
	- `POST /auth/login`
	- `GET /auth/validate-token`
	- `GET /auth/users/{id}` to call `user-service`
- `user-service` (port 8082): uses its own `user_db` H2 database and exposes:
	- `GET /users`
	- `GET /users/{id}`
	- `GET /users/by-username/{username}`
	- `GET /users/validate-token` to call `auth-service`

Run (from repository root) with Maven:

```powershell
cd d:\jfk(microservices)\auth-service
mvn spring-boot:run

# in another terminal
cd d:\jfk(microservices)\user-service
mvn spring-boot:run
```

Notes:
- These services now persist to separate H2 file databases under `./data/auth_db` and `./data/user_db`.
- The current auth token is still a simple scaffold token; replace it with JWT/OAuth once the service split is stable.
- Next steps: add an API gateway, move from H2 to MySQL/PostgreSQL, and add service discovery plus centralized config.
