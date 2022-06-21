package hu.bme.aut.hataridonaplo.nezetek

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.hataridonaplo.R
import hu.bme.aut.hataridonaplo.adat.Adatbazis
import hu.bme.aut.hataridonaplo.adat.Hatarido
import hu.bme.aut.hataridonaplo.databinding.ActivityHataridoBinding
import hu.bme.aut.hataridonaplo.nezetadapter.HataridoAdapter
import hu.bme.aut.hataridonaplo.nezetek.fragmentek.UjHataridoDialogFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class HataridoActivity : AppCompatActivity(), HataridoAdapter.HataridoClickListener, UjHataridoDialogFragment.UjHataridoDialogListener {
    private lateinit var binding: ActivityHataridoBinding

    private lateinit var database: Adatbazis
    private lateinit var adapter: HataridoAdapter
    private var showpast = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHataridoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        database = Adatbazis.getInstance(applicationContext)

        binding.spFilter.adapter = ArrayAdapter(
            applicationContext,
            R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.filterek)
        )

        binding.btnFilter.setOnClickListener{loadItemsFiltered(binding.spFilter.selectedItemPosition,binding.etFilter.text.toString())}

        binding.uj.setOnClickListener {
            var n : Int
            thread {
                n =  database.targyDAO().getAll().size
                runOnUiThread {
                    if(n != 0)UjHataridoDialogFragment().show(supportFragmentManager, UjHataridoDialogFragment.TAG)
                    else Snackbar.make(binding.root,getString(R.string.no_targy),Snackbar.LENGTH_LONG).show()
                }
            }
        }
        initRecyclerView()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.kicsuszas, R.anim.becsuszas)
    }

    override fun onResume()
    {
        super.onResume()
        loadItemsInBackground()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.hatarido_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.felevek)
        {
            startActivity(Intent(this, FelevActivity::class.java))
            overridePendingTransition(R.anim.kicsuszas, R.anim.becsuszas)
            return true
        }
        else if(item.itemId == R.id.timeToggle)
        {
            if(showpast)
            {
                showpast = false
                item.title = getString(R.string.showPast)
            }
            else
            {
                showpast = true
                item.title = getString(R.string.noShowPast)
            }
            loadItemsInBackground()
            return true
        }
        else return super.onOptionsItemSelected(item)
    }


    private fun initRecyclerView() {
        adapter = HataridoAdapter(this)
        binding.rvHatarido.layoutManager = LinearLayoutManager(this)
        binding.rvHatarido.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            var items : List<Hatarido>
            if(showpast) items = database.hataridoDAO().getAll()
            else
            {
                val df = SimpleDateFormat("yyyy-MM-dd")
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DATE, -2)
                val yesterday = df.format(calendar.getTime())

                items = database.hataridoDAO().getAllNoPast(yesterday)
            }
            runOnUiThread {
                adapter.update(items)
            }
        }
    }

    private fun loadItemsFiltered(attr:Int, filter:String) {
        var items: List<Hatarido>
        items = listOf(Hatarido(nev="Dummy hatarido -> Lekerdezesi hiba",datum="",het=0, oraigeny=0,tipus=Hatarido.Tipus.ZH,targyNev="",megjegyzes="",teljesitett=false))
        var upd = false
        thread {

            if (attr == 0) {
                if (filter == "0" || filter == "1" || filter == "2" || filter == "3" || filter == "4" || filter == "5" || filter == "6" || filter == "7" || filter == "8" || filter == "9" || filter == "10" || filter == "11" || filter == "12" || filter == "13" || filter == "14" || filter == "15" ) {
                    items = database.hataridoDAO().filterByHet(filter.toInt())
                    upd = true
                }
            } else if (attr == 1) {
                if (filter != "") {
                    items = database.hataridoDAO().filterByTargy((filter + "%"))
                    upd = true
                }

            } else if (attr == 2) {
                if (filter != "") {

                    if(filter == "ZH" || filter == "Zh" || filter == "zh")
                    {
                        items = database.hataridoDAO().filterByTip(0)
                        upd = true
                    }
                    else if(filter == "HF" || filter == "Hf" || filter == "hf")
                    {
                        items = database.hataridoDAO().filterByTip(1)
                        upd = true
                    }
                    else if(filter == "VIZSGA" || filter == "Vizsga" || filter == "vizsga")
                    {
                        items = database.hataridoDAO().filterByTip(2)
                        upd = true
                    }
                    else if(filter == "KONZULTÁCIÓ" || filter == "Konzultáció" || filter == "konzultáció"|| filter == "konzi")
                    {
                        items = database.hataridoDAO().filterByTip(3)
                        upd = true
                    }
                    else if(filter == "EGYÉB" || filter == "Egyéb" || filter == "egyéb"|| filter == "egyeb")
                    {
                        items = database.hataridoDAO().filterByTip(4)
                        upd = true
                    }
                    else if(filter == "LABOR" || filter == "Labor" || filter == "labor" || filter == "lab" )
                    {
                        items = database.hataridoDAO().filterByTip(5)
                        upd = true
                    }
                }

            } else if (attr == 3) {
                if ( filter == "0" || filter == "1") {
                    if(filter == "0") items = database.hataridoDAO().filterByTelj(false)
                    else items = database.hataridoDAO().filterByTelj(true)

                    upd = true
                }
            }

            if (upd) {
                runOnUiThread {
                    adapter.update(items)
                    Snackbar.make(binding.root,getString(R.string.filter_ok) + " " + items.size.toString() + " " + getString(
                                            R.string.nr_items_found),Snackbar.LENGTH_LONG).show()
                }
            }
            else
            {
                Snackbar.make(binding.root,getString(R.string.invalid_filter),Snackbar.LENGTH_LONG).show()
                loadItemsInBackground()
            }
        }
    }

    override fun onClick(item: Hatarido) {
        val intent = Intent(this,HataridoEditActivity::class.java)
        intent.putExtra("ID", item.id.toString())
        startActivity(intent)
        overridePendingTransition(R.anim.kicsuszas, R.anim.becsuszas);

    }

    override fun onUjHatarido(uj: Hatarido) {
        thread {
            val insertId = database.hataridoDAO().insert(uj)
            uj.id = insertId
            runOnUiThread {
                adapter.addItem(uj)
                loadItemsInBackground()
                Snackbar.make(binding.root,getString(R.string.hatarido_ok),Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onFailToMake() {
        Snackbar.make(binding.root,getString(R.string.invalid_hatarido),Snackbar.LENGTH_LONG).show()
    }
}