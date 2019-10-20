package cc.cryptopunks.crypton.presentation

import cc.cryptopunks.crypton.net.Net

class PresentationModule(
    private val net: Net
) :
    Net by net,
    PresentationComponent {

    override fun <T> net() = net as T

}