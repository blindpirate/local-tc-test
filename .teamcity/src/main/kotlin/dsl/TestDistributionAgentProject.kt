package dsl

import jetbrains.buildServer.configs.kotlin.BuildType
import jetbrains.buildServer.configs.kotlin.Project
import jetbrains.buildServer.configs.kotlin.buildSteps.script


object Build : BuildType({
    name = "MyBuild"
    features {
        feature {
            type = "aws-secrets-build-feature"
        }
    }

    params {
        param("test", "!awssm://arn:aws:secretsmanager:eu-central-1:897729135250:secret:TestSecret-WKIj2z")
        param("env.another", "%test%")
    }

    steps {
        script {
            name = "Test"
            scriptContent = """
                echo -n "${'$'}another" > result.txt
            """.trimIndent()
        }
    }

})

object TestDistributionAgentProject : Project({
    buildType(Build)
})
