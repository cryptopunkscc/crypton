package cc.cryptopunks.crypton

private const val START_UML = "@startuml"
private const val END_UML = "@enduml"
private const val RELATION = " --> "

fun Appendable.startUml() = appendln(START_UML)
fun Appendable.endUml() = appendln(END_UML)

fun Appendable.addRelation(pair: Pair<String, String>) = this
    .append(pair.first)
    .append(RELATION)
    .append(pair.second)
    .appendln()

fun component(any: Any) = "[$any]"

fun Appendable.packageOf(name: String, block: Appendable.() -> Unit) =
    append("package \"").append(name).append("\" {").appendln().apply(block).appendln("}")
