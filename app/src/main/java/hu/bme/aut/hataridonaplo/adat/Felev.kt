package hu.bme.aut.hataridonaplo.adat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.io.Serializable

@Entity(tableName = "Felev")
data class Felev (
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "nev") var nev: String,
    @ColumnInfo(name = "szam") var szam: Int
)
