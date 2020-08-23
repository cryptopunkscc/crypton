package cc.cryptopunks.crypton.entity

import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(
    tableName = "device"
)
internal data class FingerprintData(
    @PrimaryKey val fingerprint: String,
    val deviceId: Int,
    val address: AddressData,
    val state: String
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
