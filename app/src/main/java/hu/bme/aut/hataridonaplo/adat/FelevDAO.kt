package hu.bme.aut.hataridonaplo.adat

import androidx.room.*

@Dao
interface FelevDAO {
    @Query("SELECT * FROM Felev")
    fun getAll(): List<Felev>

    @Insert
    fun insert(felev: Felev): Long

    @Update
    fun update(felev: Felev)

    @Delete
    fun deleteItem(felev: Felev)
}
