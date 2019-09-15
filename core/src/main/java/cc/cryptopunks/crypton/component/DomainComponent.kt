package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.util.Scopes

interface DomainComponent {
    val useCaseScope: Scopes.UseCase
}