import org.gradle.api.Project

val Project.gitDescribeBuild: String?
    get() {
        val output = providers.exec {
            commandLine("git", "describe", "--match", "build", "--dirty")
            isIgnoreExitValue = true
        }
        val result = output.result.get()

        return when (result.exitValue) {
            0 -> output.standardOutput.asText.orNull?.trim()
            128 -> {
                logger.warn("git describe ended with exit code ${result.exitValue}")
                null
            }

            else -> {
                result.assertNormalExitValue()
                null
            }
        }
    }

val Project.buildNumber: Int
    get() = gitDescribeBuild?.parseBuildNumber() ?: 1

private fun String.parseBuildNumber(): Int = split('-').get(1).toInt()