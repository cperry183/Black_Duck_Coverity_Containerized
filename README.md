## Coverity SAST as a Docker container

#### Running the container

```bash 
docker run --rm \
  -v "$PWD":/src \
  -v /opt/coverity:/opt/coverity:ro \
  -e PATH=/opt/coverity/analysis/202x.y/bin:$PATH \
  my-build-image \
  bash -lc 'cov-build --dir /tmp/idir make -j && cov-analyze --dir /tmp/idir --all'
```

#### Commands to run after the container is built

```bash 
cov-build --dir /tmp/idir mvn -B clean verify
cov-analyze --dir /tmp/idir --all
cov-commit-defects --dir /tmp/idir \
  --url https://coverity.example.com \
  --stream my-project-main \
  --user "$COV_USER" \
  --password "$COV_PASS"
```


# Black_Duck_Coverity_Containerized
