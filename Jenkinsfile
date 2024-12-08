pipeline {
    agent any

    environment {
        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-23'
        PATH = "${env.JAVA_HOME}\\bin:${env.PATH}"
    }

    stages {

        stage('Clonar Repositorio') {
            steps {
                git branch: 'main', url: 'https://github.com/NeriasSH/Rastreo_Paquetes.git'
            }
        }

        stage('Compilar') {
            steps {
                bat './mvnw.cmd clean package'
            }
        }

        stage('Pruebas') {
            steps {
                bat './mvnw.cmd test'
            }
        }

        stage('Empaquetar') {
            steps {
                bat './mvnw.cmd package'
            }
        }
    }
}

