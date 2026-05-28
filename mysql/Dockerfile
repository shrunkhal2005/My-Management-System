FROM mysql:8.0

# Copy initialization SQL if present
COPY init.sql /docker-entrypoint-initdb.d/

# Install Python for the tiny health server
RUN apt-get update && apt-get install -y python3 \
    && rm -rf /var/lib/apt/lists/*

# Copy start script
COPY start.sh /start.sh
RUN chmod +x /start.sh

# Expose port used by the lightweight HTTP health check
EXPOSE 8080

CMD ["/start.sh"]
