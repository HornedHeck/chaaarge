package formatting

import java.util.*

private fun getName(prefix : String) = "$prefix${UUID.randomUUID()}".replace("-", "_")

fun getVariableName() = getName("v")