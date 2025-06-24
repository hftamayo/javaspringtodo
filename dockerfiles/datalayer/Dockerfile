# Use the Alpine-based MySQL image as the base image
FROM mysql:8.0

# Set the working directory inside the container
WORKDIR /docker-entrypoint-initdb.d

# Copy the datalayerschema.sql script to the container
COPY datalayerschema.sql .

# Expose the MySQL default port
EXPOSE 3306

# Add a database initialization script
ADD datalayerschema.sql /docker-entrypoint-initdb.d

# Run the script during container startup
CMD ["mysqld"]
#how to run this file:
#docker build -t hftamayo/mysqlrestapi:0.0.1 .
#docker run --name mysqlrestapi -p 3307:3306 -d --restart=always --env-file .env hftamayo/mysqlrestapi:0.0.1
#mysql -h localhost -u $MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DATABASE
