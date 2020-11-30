package cc.cryptopunks.crypton.cli

import cc.cryptopunks.crypton.util.mutableMapProperty


typealias Config = MutableMap<String, Any?>

var Config.account: String? by mutableMapProperty { null }
var Config.chat: String? by mutableMapProperty { null }

