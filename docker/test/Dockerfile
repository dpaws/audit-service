FROM dockerproductionaws/microtrader-dev
MAINTAINER Justin Menga <justin.menga@gmail.com>
LABEL application=audit-service

# Copy just the POM first
# Git plugins are deactivated if APP_VERSION is set
ENV APP_VERSION=snapshot
COPY pom.xml /app/
WORKDIR /app

# Install dependencies 
RUN mvn clean install

# Set the app version and copy the application source
ARG app_version
ENV APP_VERSION=${app_version}
COPY src /app/src