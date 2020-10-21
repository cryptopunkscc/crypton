package cc.cryptopunks.crypton.entity

import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "device")
@Entity(tableName = "device")
data class FingerprintData(
    @DatabaseField(id = true)
    @PrimaryKey val fingerprint: String = "",
    @DatabaseField val deviceId: Int = -1,
    @DatabaseField val address: AddressData = "",
    @DatabaseField val state: String = "",
) {
    @androidx.room.Dao
    interface Dao {

        @Insert
        suspend fun insert(entity: FingerprintData)

        @Query("select * from device where fingerprint == :id")
        suspend fun get(id: String): FingerprintData?

        @Query("delete from device where fingerprint == fingerprint")
        suspend fun deleteAll()
    }
}
