package cc.cryptopunks.crypton.core.component

import cc.cryptopunks.crypton.core.module.FragmentModule
import cc.cryptopunks.crypton.core.module.FragmentScope
import dagger.Component

@FragmentScope
@Component(modules = [FragmentModule::class])
interface FragmentComponent