# My Management System

This repository is now organized as a small microservices parent project instead of one monolith.

## Current structure

- `pom.xml` - parent Maven project
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
cd d:\jfk(microservices)\auth-service
mvn spring-boot:run
```

```powershell
cd d:\jfk(microservices)\user-service
mvn spring-boot:run
```

## Notes

- The old monolith code has been removed from the root to keep the repository focused on the two services.
- The services currently use H2 file databases so each service has its own local persistence layer.
- The token flow is still a scaffold and can be upgraded to JWT or OAuth next.

