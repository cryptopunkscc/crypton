package cc.cryptopunks.crypton.app.ui.component

import cc.cryptopunks.crypton.app.module.FragmentModule
import cc.cryptopunks.crypton.app.module.FragmentScope
import dagger.Component

@FragmentScope
@Component(modules = [FragmentModule::class])
interface FragmentComponent