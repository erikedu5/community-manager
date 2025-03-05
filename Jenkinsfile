pipeline {
  agent any

  tools {
    maven 'Maven Apache'
  }
  stages{
      stage('checkout') {
          steps {
            //clonar repo
            git url: 'https://github.com/erikedu5/community-manager.git', branch: 'main'
          }
      }
      stage ('build') {
        steps {
          script {
            if (!isUnix()) {
              bat 'mvn -v || echo Maven is not installed'
            } else {
              sh 'mvn -v || echo Maven is not installed'
            }
            sh 'mvn clean install'
          }
        }
      }
  }
  post {
    success {
      echo 'Build completed successfully!!'
    }
    failure {
      echo 'Build failed.'
    }
  }
}
