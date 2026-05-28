#!/bin/bash
set -e

# Start MySQL using the official entrypoint in background
/docker-entrypoint.sh mysqld &

# Wait for MySQL to become available (mysqladmin ping)
echo "Waiting for MySQL to start..."
MAX_WAIT=60
while ! mysqladmin ping -h "127.0.0.1" --silent; do
  sleep 1
  MAX_WAIT=$((MAX_WAIT-1))
  if [ "$MAX_WAIT" -le 0 ]; then
    echo "MySQL did not start in time"
    exit 1
  fi
done

echo "MySQL started. Launching HTTP health server on :8080"
# Start a simple HTTP server on port 8080 to satisfy Render's port scan
python3 -m http.server 8080 --bind 0.0.0.0
