FROM maven:3-jdk-11 AS builder

ARG VERSION=2020.12
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

RUN cov-configure --java && \
    cov-configure --javascript

FROM maven:3-jdk-11

ARG VERSION=2020.12
ENV PATH="/opt/coverity/analysis/${VERSION}/bin:${PATH}"

COPY --from=builder /opt/coverity /opt/coverity
WORKDIR /src

