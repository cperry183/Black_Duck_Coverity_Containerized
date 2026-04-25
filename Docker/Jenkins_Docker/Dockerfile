FROM maven:3.9.9-eclipse-temurin-21

ARG VERSION=2024.12
ARG COVERITY_INSTALL_DIR=/opt/coverity/analysis/${VERSION}

LABEL org.opencontainers.image.description="Coverity ${VERSION} on Maven 3.9 / Eclipse Temurin 21" \
      org.opencontainers.image.version="${VERSION}"

COPY cov-analysis-linux64-${VERSION}.sh license.dat /tmp/

RUN set -eux; \
    sh /tmp/cov-analysis-linux64-${VERSION}.sh -q \
        --installation.dir="${COVERITY_INSTALL_DIR}" \
        --license.agreement=agree \
        --license.region=0 \
        --license.type.choice=0 \
        --license.cov.path=/tmp/license.dat \
        --component.sdk=false \
        --component.cov-wizard=false \
        --component.skip.documentation=true; \
    rm -f /tmp/cov-analysis-linux64-${VERSION}.sh /tmp/license.dat; \
    "${COVERITY_INSTALL_DIR}/bin/cov-configure" --java; \
    "${COVERITY_INSTALL_DIR}/bin/cov-configure" --javascript; \
    # Strip docs/samples if the installer left any behind
    find "${COVERITY_INSTALL_DIR}" \
        \( -type d -name "doc" -o -type d -name "samples" \) \
        -exec rm -rf {} + 2>/dev/null || true

ENV PATH="${COVERITY_INSTALL_DIR}/bin:${PATH}"

# Non-root runtime user; /workspace owned so Maven can write .m2 cache
RUN groupadd --gid 1001 coverity && \
    useradd  --uid 1001 --gid coverity --shell /bin/bash --create-home coverity && \
    mkdir -p /workspace && chown coverity:coverity /workspace

USER coverity
WORKDIR /workspace
