Deployment notes

1) Local Docker build
   - Ensure Docker Desktop is running on Windows.
   - Run `build-and-run-docker.bat` to build and start the container (maps host 8080 -> container 8080).

2) Dockerfile
   - Uses `eclipse-temurin:21-jre` as the base image and expects the fat JAR at `target/jfx-app-1.0.jar`.

3) CI/CD (suggestions)
   - Create a GitHub Actions workflow to build the Maven project, build the Docker image and push to a registry.
   - Example steps: `actions/checkout`, `actions/setup-java`, `maven package`, `docker/build-push-action`.
