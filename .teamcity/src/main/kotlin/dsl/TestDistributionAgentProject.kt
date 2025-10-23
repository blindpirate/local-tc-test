package dsl

import jetbrains.buildServer.configs.kotlin.Project


object TestDistributionAgentProject : Project({
    buildType(Verify("ubuntu", "Default"))
    params {
        param("JdkProviderEnabled", "true")
        param(
            "AwsEnvironmentVariables", "-e AWS_DEFAULT_REGION=eu-central-1 " +
                "-e AWS_ACCESS_KEY_ID=%gradle.eng.tooling.aws.access.key.id% " +
                "-e AWS_SECRET_ACCESS_KEY=%gradle.eng.tooling.aws.secret.access.key%"
        )
    }
})
