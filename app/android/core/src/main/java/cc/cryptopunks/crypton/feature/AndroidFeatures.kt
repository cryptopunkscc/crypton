package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.Features
import cc.cryptopunks.crypton.features

fun androidFeatures(): Features = features(
    showFileChooser(),
    openFile()
)

fun androidResolvers() = listOf(
    execUploadResolver(),
    showFileChooserResolver()
)
