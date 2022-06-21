package hu.bme.aut.hataridonaplo.adat

import androidx.room.*

@Dao
interface HataridoDAO {
    @Query("SELECT * FROM hatarido")
    fun getAll(): List<Hatarido>

    @Query("SELECT * FROM hatarido WHERE datum > :Today")
    fun getAllNoPast(Today : String): List<Hatarido>

    @Query("SELECT * FROM hatarido WHERE id = :hID")
    fun getByID(hID: Long): Hatarido

    @Query("SELECT * FROM hatarido WHERE het = :Het")
    fun filterByHet(Het: Int): List<Hatarido>

    @Query("SELECT * FROM hatarido INNER JOIN targy ON hatarido.targyNev = targy.nevTeljes WHERE nevTeljes LIKE :Targy OR nevRovid LIKE :Targy")
    fun filterByTargy(Targy: String): List<Hatarido>


    @Query("SELECT * FROM hatarido WHERE tipus = :Tip")
    fun filterByTip(Tip: Int): List<Hatarido>

    @Query("SELECT * FROM hatarido WHERE teljesitett = :Telj")
    fun filterByTelj(Telj: Boolean): List<Hatarido>

    @Insert
    fun insert(hatarido: Hatarido): Long

    @Update
    fun update(hatarido: Hatarido)

    @Delete
    fun deleteItem(hatarido: Hatarido)
}
