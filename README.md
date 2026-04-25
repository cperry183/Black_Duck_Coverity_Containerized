<div align="center">
  <img src="https://img.shields.io/badge/Synopsys-Coverity_SAST-red?style=for-the-badge&logo=synopsys" alt="Coverity SAST" />
  <img src="https://img.shields.io/badge/Container-Docker-2496ED?style=for-the-badge&logo=docker" alt="Docker" />
  <img src="https://img.shields.io/badge/CI%2FCD-Jenkins-blue?style=for-the-badge&logo=jenkins" alt="Jenkins CI/CD" />
  <img src="https://img.shields.io/badge/Language-Groovy-4298B8?style=for-the-badge&logo=groovy" alt="Groovy" />
  <img src="https://img.shields.io/badge/License-MIT-green.svg?style=for-the-badge" alt="License" />
</div>

<h1 align="center">🛡️ Black Duck Coverity — Containerized SAST for Jenkins</h1>

<p align="center">
  <strong>A robust solution for containerizing Synopsys Coverity Static Application Security Testing (SAST) and integrating it seamlessly into Jenkins CI/CD pipelines.</strong>
</p>

---

## 📖 Overview

This project provides a comprehensive approach to running **Synopsys Coverity Static Application Security Testing (SAST)** within a Docker container, enabling flexible and consistent integration into Jenkins CI/CD pipelines. By containerizing Coverity, development teams can achieve **shift-left security** without the complexities of managing host-level installations, ensuring reproducible and scalable security analysis across diverse build environments.

### ✨ Key Capabilities

| Feature | Description |
| :--- | :--- |
| 🐳 **Containerized Coverity** | Encapsulates the Coverity SAST analysis engine within a Docker image, simplifying deployment and ensuring environment consistency. |
| ⚙️ **Jenkins CI/CD Integration** | Provides a `coverity.groovy` pipeline script for seamless integration into Jenkins, automating the SAST process within your build workflows. |
| 🚀 **Reproducible Scans** | Guarantees consistent scan results by isolating the Coverity environment and its dependencies within a Docker container. |
| 📦 **Simplified Management** | Eliminates the need for manual Coverity toolchain installations on Jenkins agents, reducing operational overhead. |
| 📈 **Scalable Security Analysis** | Easily scales SAST capabilities across multiple projects and teams by leveraging Docker and Jenkins orchestration. |

---

## 📂 Repository Structure

```text
Black_Duck_Coverity_Containerized/
├── Docker/                         # Contains the Dockerfile for building the Coverity SAST image
│   └── Dockerfile                  # Defines the Docker image for Coverity SAST
├── Jenkins_Files/                  # Contains Jenkins-specific integration files
│   └── coverity.groovy             # Jenkins Pipeline script for Coverity SAST integration
└── README.md                       # This README file
```

---

## 🚀 Quick Start Guide

To get started with containerized Coverity SAST in your Jenkins pipeline, follow these steps:

### 1. Build the Coverity Docker Image

Navigate to the `Docker` directory and build the Docker image. You will need to provide your Coverity analysis installer (`cov-analysis-linux64-<VERSION>.sh`) and license file (`license.dat`).

```bash
cd Docker
# Place cov-analysis-linux64-<VERSION>.sh and license.dat in this directory
docker build -t coverity-sast:latest --build-arg VERSION=2020.12 .
```

*Note: Adjust `VERSION` to match your Coverity analysis version.*

### 2. Run the Container (Local Test)

You can test the container locally by mounting your source code and Coverity installation directory.

```bash
docker run --rm \
  -v "$PWD":/src \
  -v /opt/coverity:/opt/coverity:ro \
  -e PATH="/opt/coverity/analysis/202x.y/bin:$PATH" \
  my-build-image \
  bash -lc 'cov-build --dir /tmp/idir make -j && cov-analyze --dir /tmp/idir --all'
```

### 3. Integrate with Jenkins

Use the provided `Jenkins_Files/coverity.groovy` script to integrate Coverity SAST into your Jenkins Pipeline. You will need to configure Jenkins credentials for Coverity and set up environment variables.

**Example Jenkinsfile snippet:**

```groovy
pipeline {
    agent any
    environment {
        COVERITY_HOME = '/opt/coverity/analysis/2024.12' // Adjust to your Coverity version
        COVERITY_URL = 'https://coverity.example.com' // Your Coverity Connect URL
        COVERITY_STREAM = 'my-project-main' // Your Coverity project stream
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
                    image 'maven:3.9.9-eclipse-temurin-21' // Or your build image
                    reuseNode true
                    args '-v /opt/coverity:/opt/coverity:ro -v $HOME/.m2:/root/.m2'
                }
            }
            environment {
                PATH = "${env.COVERITY_HOME}/bin:${env.PATH}"
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'coverity-creds', usernameVariable: 'COV_USER', passwordVariable: 'COV_PASS')]) {
                    sh '''
                        set -eux
                        cov-configure --java || true
                        cov-configure --javascript || true
                        cov-build --dir /tmp/idir mvn -B clean verify
                        cov-analyze --dir /tmp/idir --all
                        cov-commit-defects \
                            --url ${COVERITY_URL} \
                            --stream ${COVERITY_STREAM} \
                            --user ${COV_USER} \
                            --password ${COV_PASS}
                    '''
                }
            }
        }
    }
}
```

*Note: Ensure `coverity-creds` is configured in Jenkins with your Coverity username and password.*

---

## 🤝 Contributing

Contributions are welcome! If you have suggestions for improvements or new features, please open an issue or submit a pull request.

---

## 📜 License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for complete details.
