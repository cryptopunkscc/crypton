package cc.cryptopunks.crypton.repo.test.room

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    RoomAccountRepoTest::class,
    RoomDeviceRepoTest::class,
    RoomChatRepoTest::class,
    RoomMessageRepoTest::class
)
class RoomRepoTest
