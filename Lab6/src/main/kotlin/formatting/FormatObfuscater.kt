package formatting

import ContentObfuscater

class FormatObfuscater : ContentObfuscater {

    override fun obfuscate(content: String) = content.lineSequence()
        .removeComments()
        .memoryPrivates()
        .renamePrivates()
        .memoryLocals()
        .renameLocals()
        .trimIndents()
        .joinToString("\n")

    private fun Sequence<String>.trimIndents() = map { it.trim() }

    private val localVars = mutableMapOf<Regex, String>()
    private fun Sequence<String>.memoryLocals() = map {
        if (it.contains("var")) {
            varRegex.find(it)?.value?.let { name ->
                localVars[Regex("(?<=\\W)$name(?=\\W)")] = getVariableName()
            }
        }
        it
    }

    private fun Sequence<String>.renameLocals() = map {
        var res = it
        localVars.forEach { (k, v) ->
            res = res.replace(k, v)
        }
        res
    }


    private val privateVars = mutableMapOf<Regex, String>()

    private fun Sequence<String>.memoryPrivates() = map {
        if (it.contains("private")) {
            nameRegex.find(it.replace(" =", ";"))?.value?.let { name ->
                privateVars[Regex("(?<=\\W)$name(?=\\W)")] = getVariableName()
            }
        }
        it
    }

    private fun Sequence<String>.renamePrivates() = map {
        var res = it
        privateVars.forEach { (k, v) ->
            res = res.replace(k, v)
        }
        res
    }

    private var isInMultiline = false
    private fun Sequence<String>.removeComments() = map {
        if (isInMultiline) {
            if (it.contains("*/")) {
                isInMultiline = false
            }
            it.substringAfter("*/", "")
        } else {
            it
                .replace(singleLineRegex, "")
                .replace(multiLineInSingleLineRegex, "")
        }.run {
            if (contains(multilineStartRegex)) {
                isInMultiline = true
                replaceFirst(multilineStartRegex, "/*")
                    .substringBefore("/*")
            } else {
                this
            }
        }
    }
}

private val singleLineRegex = Regex("//.*$")
private val multiLineInSingleLineRegex = Regex("/\\*\\*?.*\\*/")
private val multilineStartRegex = Regex("/\\*\\*?")

private val nameRegex = Regex("\\b\\S+(?=;)")
private val varRegex = Regex("(?<=var\\s)\\w+(?=\\W)")