package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.create.cryptonContext

fun androidFeatures() = cryptonContext(
    showFileChooser(),
    openFile()
)

fun androidResolvers() = cryptonContext(
    execUploadResolver(),
    showFileChooserResolver()
)
