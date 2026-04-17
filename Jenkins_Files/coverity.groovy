pipeline {
  agent any

  environment {
    COVERITY_HOME = '/opt/coverity/analysis/2024.12'
    COVERITY_URL  = 'https://coverity.example.com'
    COVERITY_STREAM = 'my-project-main'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build + Coverity Scan') {
      agent {
        docker {
          image 'maven:3.9.9-eclipse-temurin-21'
          reuseNode true
          args '-v /opt/coverity:/opt/coverity:ro -v $HOME/.m2:/root/.m2'
        }
      }
      environment {
        PATH = "${env.COVERITY_HOME}/bin:${env.PATH}"
      }
      steps {
        withCredentials([usernamePassword(
          credentialsId: 'coverity-creds',
          usernameVariable: 'COV_USER',
          passwordVariable: 'COV_PASS'
        )]) {
          sh '''
            set -eux

            cov-configure --java || true

            cov-build --dir idir mvn -B -DskipTests clean verify

            cov-analyze --dir idir --all

            cov-commit-defects --dir idir \
              --url "$COVERITY_URL" \
              --stream "$COVERITY_STREAM" \
              --user "$COV_USER" \
              --password "$COV_PASS"
          '''
        }
      }
    }
  }
}

