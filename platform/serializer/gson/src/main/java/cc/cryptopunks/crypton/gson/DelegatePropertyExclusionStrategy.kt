package cc.cryptopunks.crypton.gson

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes

object DelegatePropertyExclusionStrategy : ExclusionStrategy {
    override fun shouldSkipClass(clazz: Class<*>): Boolean = false
    override fun shouldSkipField(f: FieldAttributes): Boolean = f.name.endsWith("\$delegate")
}
