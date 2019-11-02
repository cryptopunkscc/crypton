package crypton

import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

class ProcessContext(
    val roundEnv: RoundEnvironment,
    val processingEnv: ProcessingEnvironment
) {

    val TypeElement.members: List<ExecutableElement>
        get() = processingEnv
            .elementUtils
            .getAllMembers(this)
            .filter { it.kind == ElementKind.METHOD }
            .filterNot { it.modifiers.contains(Modifier.FINAL) }
            .filterNot { methodBlacklist.any(it.simpleName::contentEquals) }
            .map { it as ExecutableElement }


    fun warning(message: Any?) =
        processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, message.toString())

    private companion object {
        val methodBlacklist = listOf(
            "toString",
            "hashCode",
            "equals"
        )
    }
}


fun ProcessContext.run() {
    val injectedProps = injectProps() + depProps()
    val log: (Any?) -> Unit = this::warning

    roundEnv.getElementsAnnotatedWith(Dependency::class.java)
        .filter { it.kind == ElementKind.INTERFACE }
        .map { it as TypeElement }
        .forEach { element ->
            component(
                log = log,
                element = element,
                injections = injectedProps
            )
                .build()
                .also { warning(it) }
        }
}