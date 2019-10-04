package cc.cryptopunks.crypton.smack

import javax.inject.Qualifier
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiScope

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiQualifier