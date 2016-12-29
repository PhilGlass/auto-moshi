package glass.phil.auto.moshi

fun String.removeAll(char: Char) = filterNot { it == char }
