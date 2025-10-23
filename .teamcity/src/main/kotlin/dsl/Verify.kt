package dsl

import jetbrains.buildServer.configs.kotlin.AbsoluteId
import jetbrains.buildServer.configs.kotlin.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.triggers.vcs

class Verify(private val dir: String, private val desc: String) : AbstractCheck({
    id = AbsoluteId("VerifyGradleBuildTestDistributionAgent$desc")
    uuid = "VerifyGradleBuildTestDistributionAgent$desc"
    name = "Verify Gradle Build Test Distribution Agent $desc"
    description = "Verify Gradle Build Test Distribution Agent ($desc)"

    triggers {
        vcs {
            branchFilter = """
    +:refs/heads/*
""".trimIndent()
        }
    }

    steps {
//        provisionDependencies(dir)
//
//        dockerLoginStep()

        script {
            name = "Build and verify test distribution docker image"
            scriptContent = """
                cd $dir
                cp -r ../shared/* .
                docker build . 
            """.trimIndent()
        }
    }

    features {
        commitStatusPublisher {
            vcsRootExtId = "Gradle_GradleBuildTestDistributionAgent"
            publisher = github {
                githubUrl = "https://api.github.com"
                authType = personalToken {
                    token = "%github.bot-teamcity.token%"
                }
            }
        }
    }
})
