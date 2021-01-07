package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.delegate.dep

val RootScope.applicationId: ApplicationId by dep()

data class ApplicationId(val value: String = "crypton")
