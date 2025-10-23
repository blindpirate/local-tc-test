package dsl

import jetbrains.buildServer.configs.kotlin.AbsoluteId
import jetbrains.buildServer.configs.kotlin.BuildSteps
import jetbrains.buildServer.configs.kotlin.BuildType
import jetbrains.buildServer.configs.kotlin.CheckoutMode
import jetbrains.buildServer.configs.kotlin.buildSteps.script

abstract class AbstractCheck(init: BuildType.() -> Unit) : BuildType({
    init()

    vcs {
        root(AbsoluteId("Gradle_GradleBuildTestDistributionAgent"))

        checkoutMode = CheckoutMode.ON_AGENT
        cleanCheckout = true
    }

    requirements {
        contains("teamcity.agent.jvm.os.name", "Linux")
    }
})

fun BuildSteps.provisionDependencies(targetDir: String) {
    script {
        name = "Copy JDK distributions to working dir"
        scriptContent = """
            cp -r %linux.java7.oracle.64bit% $targetDir/oracle-jdk-7
            cp -r %linux.java8.oracle.64bit% $targetDir/oracle-jdk-8
            cp -r %linux.java11.openjdk.64bit% $targetDir/open-jdk-11
            cp -r %linux.java17.openjdk.64bit% $targetDir/open-jdk-17
            cp -r %linux.java19.openjdk.64bit% $targetDir/open-jdk-19
            cp -r %linux.java21.openjdk.64bit% $targetDir/open-jdk-21
            cp -r %linux.java23.openjdk.64bit% $targetDir/open-jdk-23
            cp -r %linux.java24.openjdk.64bit% $targetDir/open-jdk-24
            cp -r %linux.java25.openjdk.64bit% $targetDir/open-jdk-25
            """.trimIndent()
    }
}

fun BuildSteps.dockerLoginStep() {
    script {
        name = "Docker login"
        scriptContent = """
                docker login enterprise-releases.repo.grdev.net -u %gradle.enterprise.releases.docker.username% -p '%gradle.enterprise.releases.docker.password%'
                docker login enterprise-snapshots.repo.grdev.net -u %gradle.enterprise.releases.docker.username% -p '%gradle.enterprise.releases.docker.password%'
            """.trimIndent()
    }
}
