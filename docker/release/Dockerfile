FROM dockerproductionaws/microtrader-base
MAINTAINER Justin Menga <justin.menga@gmail.com>
LABEL application=audit-service

# Copy application config
COPY src/conf/*.tmpl /etc/confd/templates/
COPY src/conf/*.toml /etc/confd/conf.d/

# Copy application artefacts
ARG app_version
ENV app_version $app_version
COPY target/audit-service-${app_version}-fat.jar /app/