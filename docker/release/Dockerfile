FROM dockerproductionaws/microtrader-base
MAINTAINER Justin Menga <justin.menga@gmail.com>
LABEL application=audit-service

# Copy application artefacts
ARG app_version
LABEL application.version=${app_version}
COPY target/audit-service-${app_version}-fat.jar /app/app.jar
