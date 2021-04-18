package cc.cryptopunks.crypton.agent

interface Story {
    val type: String
    val ver: Int
    val rel: List<String>
}

data class StoryHeader(
    override val ver: Int,
    override val type: String,
    override val rel: List<String>
) : Story
