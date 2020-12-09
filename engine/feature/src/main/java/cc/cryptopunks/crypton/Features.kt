package cc.cryptopunks.crypton

import kotlinx.coroutines.CoroutineScope

val Scope.features: Features by dep()

data class Features(val list: FeatureList) : FeatureList by list

private typealias FeatureList = List<Feature<Scoped<CoroutineScope>, CoroutineScope>>

@Suppress("UNCHECKED_CAST")
fun features(vararg features: Feature<out Scoped<*>, out CoroutineScope>): Features =
    Features(features.toList() as FeatureList)

operator fun Features.plus(features: Features) = Features(list + features)

fun Features.createHandlers(): HandlerRegistry = createHandlers {
    filter { it.handle != NonHandle }.forEach { feature ->
        unsafePlus(
            type = feature.type.kotlin,
            handle = feature.handle
        )
    }
}
