package cc.cryptopunks.crypton.xmpp

import javax.inject.Qualifier
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class XmppScope

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class XmppQualifier