package cc.cryptopunks.crypton.presentation

import cc.cryptopunks.crypton.api.Api

class PresentationModule(
    private val api: Api
) :
    Api by api,
    PresentationComponent {

    override fun <T> api() = api as T

}