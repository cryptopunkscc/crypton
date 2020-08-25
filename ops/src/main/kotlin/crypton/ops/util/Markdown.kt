package crypton.ops.util


fun String.markdownBold() = "**$this**"

fun markdownLink(description: String, url: String) = "[$description]($url)"

fun String.markdownH(size: Int) = "${formatH(size)}$this"

private fun formatH(size: Int) = (0 until size)
    .joinToString("") { "#" }
    .let { if (size > 0) "$it " else it }
