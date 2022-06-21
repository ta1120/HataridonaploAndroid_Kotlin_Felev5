package hu.bme.aut.hataridonaplo.adat

import androidx.room.*

@Dao
interface TargyDAO {
    @Query("SELECT * FROM targy")
    fun getAll(): List<Targy>

    @Query("SELECT * FROM targy WHERE id = :tID")
    fun getByID(tID : Long) : Targy

    @Query("SELECT * FROM targy WHERE felev LIKE :felev")
    fun filterByFelev(felev : String) : List<Targy>

    @Insert
    fun insert(targy: Targy): Long

    @Update
    fun update(targy: Targy)

    @Delete
    fun deleteItem(targy: Targy)
}
