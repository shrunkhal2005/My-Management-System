#!/bin/bash
set -euo pipefail

# Allow override of the health HTTP port via the PORT env var (Render provides $PORT)
HTTP_PORT=${PORT:-8080}

echo "Starting MySQL via official entrypoint..."
# Start MySQL using the official entrypoint in background
/docker-entrypoint.sh mysqld &
MYSQL_PID=$!

echo "Waiting for MySQL to start (checking mysqladmin ping)..."
MAX_WAIT=60
while ! mysqladmin ping -h "127.0.0.1" --silent; do
  sleep 1
  MAX_WAIT=$((MAX_WAIT-1))
  if [ "$MAX_WAIT" -le 0 ]; then
    echo "MySQL did not start in time"
    ps aux
    echo "---- MySQL logs ----"
    tail -n 200 /var/log/mysql || true
    exit 1
  fi
done

echo "MySQL started. PID=$MYSQL_PID"

echo "Listening sockets before starting health server:"
ss -lntp || netstat -lntp || true

echo "Launching HTTP health server on :${HTTP_PORT}"
# Start a simple HTTP server on the configured port to satisfy Render's port scan
python3 -m http.server "${HTTP_PORT}" --bind 0.0.0.0

