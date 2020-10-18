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
    @PrimaryKey val fingerprint: String,
    @DatabaseField val deviceId: Int,
    @DatabaseField val address: AddressData,
    @DatabaseField val state: String
) {
    @androidx.room.Dao
    interface Dao {

        @Insert
        fun insert(fingerprintData: FingerprintData)

        @Query("select * from device where fingerprint == :fingerprint")
        fun get(fingerprint: String): FingerprintData?

        @Query("delete from device where fingerprint == fingerprint")
        fun clear()
    }
}
