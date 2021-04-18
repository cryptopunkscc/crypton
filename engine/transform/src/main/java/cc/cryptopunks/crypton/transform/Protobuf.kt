package cc.cryptopunks.crypton.transform

import cc.cryptopunks.crypton.flip
import cc.cryptopunks.crypton.transformation

val protoToString = transformation<Any, String>("proto")
val stringToProto = jsonToString.flip()
