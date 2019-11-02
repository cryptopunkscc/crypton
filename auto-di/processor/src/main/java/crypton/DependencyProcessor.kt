package crypton

import com.google.auto.service.AutoService
import javax.annotation.processing.*
import javax.inject.Inject
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(KAPT_KOTLIN_GENERATED_OPTION_NAME)
class DependencyProcessor : AbstractProcessor() {

    override fun process(
        elements: Set<TypeElement>,
        env: RoundEnvironment
    ): Boolean {
        ProcessContext(
            roundEnv = env,
            processingEnv = processingEnv
        ).run()
        return false
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> = listOf(
        Dependency::class,
        Inject::class,
        AsInterface::class
    )
        .map { it.java.canonicalName }
        .toMutableSet()
}