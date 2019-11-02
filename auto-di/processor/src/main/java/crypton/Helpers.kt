package crypton

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.buildCodeBlock

fun CodeBlock.lazy() = buildCodeBlock {
    add("lazy { ")
    add(this@lazy)
    add(" }")
}

fun CodeBlock.returns() = buildCodeBlock {
    add("return ")
    add(this@returns)
}