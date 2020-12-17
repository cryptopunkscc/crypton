package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.Features
import cc.cryptopunks.crypton.features
import cc.cryptopunks.crypton.resolvers

fun androidFeatures(): Features = features(
    showFileChooser(),
    openFile()
)

fun androidResolvers() = resolvers(
    execUploadResolver(),
    showFileChooserResolver()
)
