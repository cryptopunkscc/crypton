package cc.cryptopunks.crypton.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert


@Entity(
    primaryKeys = [
        "accountId",
        "userId"
    ],
    foreignKeys = [
        ForeignKey(
            entity = Chat::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AccountUser(
    val accountId: Long,
    val userId: Long
) {

    constructor(account: Account, user: User) : this(
        accountId = account.id,
        userId = user.id
    )

    @androidx.room.Dao
    interface Dao:
        User.Dao,
        Account.Dao {

        @Insert
        fun insert(entity: AccountUser) : Long

        @Insert
        fun insert(list: List<AccountUser>) : List<Long>
    }
}