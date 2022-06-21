package hu.bme.aut.hataridonaplo.adat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.*

@Entity(tableName = "hatarido")
data class Hatarido(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "nev") var nev: String,
    @ColumnInfo(name = "tipus") var tipus: Tipus,
    @ColumnInfo(name = "targyNev") var targyNev: String,
    @ColumnInfo(name = "datum") var datum: String,
    @ColumnInfo(name = "het") var het: Int,
    @ColumnInfo(name = "oraigeny") var oraigeny: Int,
    @ColumnInfo(name = "megjegyzes") var megjegyzes: String,
    @ColumnInfo(name = "teljesitett") var teljesitett: Boolean
) {
    var selected: Boolean = false

    enum class Tipus {
        ZH, HF, VIZSGA,KONZULTACIO,EGYEB,LABOR;
        companion object {
            @JvmStatic
            @TypeConverter
            fun getByOrdinal(ordinal: Int): Tipus? {
                var vissza = ZH
                if(ordinal == 1) vissza = HF
                if(ordinal == 2) vissza = VIZSGA
                if(ordinal == 3) vissza = KONZULTACIO
                if(ordinal == 4) vissza = EGYEB
                if(ordinal == 5) vissza = LABOR
                return vissza
            }
            @JvmStatic
            @TypeConverter
            fun toInt(category: Tipus): Int {
                var vissza = 0
                if(category == HF) vissza = 1
                if(category == VIZSGA) vissza = 2
                if(category == KONZULTACIO) vissza = 3
                if(category == EGYEB) vissza = 4
                if(category == LABOR) vissza = 5
                return vissza
            }
        }
    }
}