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
        param("test", "!awssm://test")
        param("env.another", "%test%")
    }

    steps {
        script {
            name = "Test"
            scriptContent = """
                echo "${'$'}another"
            """.trimIndent()
        }
    }

})

object TestDistributionAgentProject : Project({
    buildType(Build)
})
