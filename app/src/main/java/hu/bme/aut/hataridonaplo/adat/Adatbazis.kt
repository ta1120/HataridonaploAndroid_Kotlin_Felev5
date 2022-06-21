package hu.bme.aut.hataridonaplo.adat

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Felev::class, Targy::class, Hatarido::class], version = 1)
@TypeConverters(value = [Hatarido.Tipus::class])
abstract class Adatbazis : RoomDatabase() {
    abstract fun hataridoDAO(): HataridoDAO
    abstract fun targyDAO(): TargyDAO
    abstract fun felevDAO(): FelevDAO


    companion object {
        fun getInstance(applicationContext: Context): Adatbazis {
            return Room.databaseBuilder(
                applicationContext,
                Adatbazis::class.java,
                "hataridoAdatbazis"
            ).build();
        }
    }
}
