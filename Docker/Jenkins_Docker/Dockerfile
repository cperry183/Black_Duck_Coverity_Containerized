FROM maven:3.9.9-eclipse-temurin-21

ARG VERSION=2024.12
COPY cov-analysis-linux64-${VERSION}.sh /tmp/
COPY license.dat /tmp/

RUN sh /tmp/cov-analysis-linux64-${VERSION}.sh -q \
    --installation.dir=/opt/coverity/analysis/${VERSION} \
    --license.agreement=agree \
    --license.region=0 \
    --license.type.choice=0 \
    --license.cov.path=/tmp/license.dat \
    --component.sdk=false \
    --component.cov-wizard=false \
    --component.skip.documentation=true && \
    rm -f /tmp/cov-analysis-linux64-${VERSION}.sh /tmp/license.dat

ENV PATH="/opt/coverity/analysis/${VERSION}/bin:${PATH}"
WORKDIR /workspace

