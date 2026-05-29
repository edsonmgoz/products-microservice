pipeline {
    agent none
    triggers {
        githubPush()
    }
    environment {
        MAVEN_OPTS      = "-Dmaven.repo.local=${WORKSPACE}/.m2"
        SONAR_USER_HOME = "${WORKSPACE}/.sonar"
    }
    stages {
        stage('Build') {
            agent {
                docker {
                    image 'maven:3.9-eclipse-temurin-25'
                }
            }
            steps {
                sh 'mvn clean compile -B -ntp'
            }
        }
        stage('Test') {
            agent {
                docker {
                    image 'maven:3.9-eclipse-temurin-25'
                }
            }
            options { skipDefaultCheckout() }
            steps {
                sh 'mvn test -B -ntp'
            }
            post {
                success {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Coverage') {
            agent {
                docker {
                    image 'maven:3.9-eclipse-temurin-25'
                }
            }
            options { skipDefaultCheckout() }
            steps {
                sh 'mvn jacoco:report -B -ntp'
            }
            post {
                success {
                    recordCoverage(tools: [[parser: 'JACOCO']])
                }
            }
        }
        stage('Package') {
            agent {
                docker {
                    image 'maven:3.9-eclipse-temurin-25'
                }
            }
            options { skipDefaultCheckout() }
            steps {
                sh 'mvn package -DskipTests -B -ntp'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }
        stage('Sonar') {
            agent {
                docker {
                    image 'maven:3.9-eclipse-temurin-25'
                }
            }
            options { skipDefaultCheckout() }
            steps {
                withSonarQubeEnv('sonarqube') {
                    script {
                        if (env.CHANGE_ID) {
                            sh """
                                mvn sonar:sonar -B -ntp \
                                -Dsonar.pullrequest.key=${env.CHANGE_ID} \
                                -Dsonar.pullrequest.branch=${env.CHANGE_BRANCH} \
                                -Dsonar.pullrequest.base=${env.CHANGE_TARGET}
                            """
                        } else {
                            def branchName = GIT_BRANCH.replaceFirst('^origin/', '')
                            println "Branch name: ${branchName}"
                            sh "mvn sonar:sonar -B -ntp -Dsonar.branch.name=${branchName}"
                        }
                    }
                }
            }
        }
        stage('Docker') {
            agent {
                docker {
                    image 'docker:29.4.0-cli'
                    args '--group-add 988 -v /var/run/docker.sock:/var/run/docker.sock'
                }
            }
            environment {
                DOCKER_CONFIG         = "${WORKSPACE}/.docker"
                DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')
            }
            options { skipDefaultCheckout() }
            steps {
                sh 'docker --version'
                sh 'docker images'
                script {
                    def pom = readMavenPom file: 'pom.xml'
                    def image = "edsonmgoz/${pom.artifactId}"

                    def app = docker.build("${image}:${pom.version}")
                    docker.withRegistry('https://registry.hub.docker.com/', 'dockerhub-credentials') {
                        app.push()
                        app.push('latest')
                    }
                }
            }
        }
    }
    post {
        cleanup {
            cleanWs()
        }
    }
}
