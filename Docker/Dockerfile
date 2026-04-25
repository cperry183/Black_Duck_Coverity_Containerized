# ── Stage 1: Coverity installer ──────────────────────────────────────────────
FROM maven:3-jdk-11 AS builder

ARG VERSION=2020.12
ARG COVERITY_INSTALL_DIR=/opt/coverity/analysis/${VERSION}

# Copy installer and license together to keep layer count low
COPY cov-analysis-linux64-${VERSION}.sh license.dat /tmp/

# Install Coverity, configure analyzers, then scrub installer artifacts
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
    # Configure language analyzers in the same layer so intermediate state is not cached
    "${COVERITY_INSTALL_DIR}/bin/cov-configure" --java; \
    "${COVERITY_INSTALL_DIR}/bin/cov-configure" --javascript

# ── Stage 2: Runtime image ───────────────────────────────────────────────────
FROM maven:3-jdk-11

ARG VERSION=2020.12
ARG COVERITY_INSTALL_DIR=/opt/coverity/analysis/${VERSION}

LABEL org.opencontainers.image.description="Coverity ${VERSION} on Maven/JDK 11" \
      org.opencontainers.image.version="${VERSION}"

# Copy only the installed tree — no installer, no license file
COPY --from=builder /opt/coverity /opt/coverity

ENV PATH="${COVERITY_INSTALL_DIR}/bin:${PATH}"

# Run as non-root; Maven local repo lives under the user home
RUN groupadd --gid 1001 coverity && \
    useradd  --uid 1001 --gid coverity --shell /bin/bash --create-home coverity

USER coverity
WORKDIR /src
