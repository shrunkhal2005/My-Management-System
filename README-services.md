Microservices scaffold

This repository now contains two simple Spring Boot services for initial microservices work:

- `auth-service` (port 8081): provides a very small `/auth/login` endpoint that accepts `username` and `password` JSON and returns a dummy access token.
- `user-service` (port 8082): provides `/users` endpoints to list and fetch users.

Run (from repository root) with Maven:

```powershell
cd d:\jfk(microservices)\auth-service
mvn spring-boot:run

# in another terminal
cd d:\jfk(microservices)\user-service
mvn spring-boot:run
```

Notes:
- These are minimal scaffolds to begin splitting the monolith. They intentionally use an in-memory store and a dummy token for auth to keep the first iteration small.
- Next steps: wire real authentication (JWT / OAuth), move domain logic into services, add API gateway or service discovery, and add CI/CD.
