package hu.bme.aut.hataridonaplo.adat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "targy")
data class Targy(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "felev") var felev: String,
    @ColumnInfo(name = "nevTeljes") var nevTeljes: String,
    @ColumnInfo(name = "nevRovid") var nevRovid: String,
    @ColumnInfo(name = "kredit") var kredit: Int,
    @ColumnInfo(name = "megjegyzes") var megjegyzes: String
)