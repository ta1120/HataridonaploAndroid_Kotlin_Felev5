package hu.bme.aut.hataridonaplo.nezetek

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.hataridonaplo.R
import hu.bme.aut.hataridonaplo.adat.Adatbazis
import hu.bme.aut.hataridonaplo.adat.Targy
import hu.bme.aut.hataridonaplo.databinding.ActivityTargyBinding
import hu.bme.aut.hataridonaplo.nezetadapter.TargyAdapter
import hu.bme.aut.hataridonaplo.nezetek.fragmentek.UjTargyDialogFragment
import kotlin.concurrent.thread

class TargyActivity : AppCompatActivity(), TargyAdapter.TargyClickListener, UjTargyDialogFragment.UjTargyDialogListener {
    private lateinit var binding: ActivityTargyBinding

    private lateinit var database: Adatbazis
    private lateinit var adapter: TargyAdapter
    lateinit var felevNev :String

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.kicsuszas, R.anim.becsuszas)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTargyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        felevNev = intent.getStringExtra("FelevNev").toString()
        database = Adatbazis.getInstance(applicationContext)
        setTitle(getString(R.string.targyak) + " - " + felevNev)

        binding.uj.setOnClickListener {
            UjTargyDialogFragment().show(
                supportFragmentManager,
                UjTargyDialogFragment.TAG
            )
        }
        initRecyclerView()
    }



    private fun initRecyclerView() {
        adapter = TargyAdapter(this)
        binding.rvTargy.layoutManager = LinearLayoutManager(this)
        binding.rvTargy.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.targyDAO().filterByFelev(felevNev)
            runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onResume()
    {
        super.onResume()
        loadItemsInBackground()
    }

    override fun onItemClick(item: Targy) {
        val intent = Intent(this,TargyEditActivity::class.java)
        intent.putExtra("ID", item.id.toString())
        startActivity(intent)
        overridePendingTransition(R.anim.kicsuszas, R.anim.becsuszas);
    }

    override fun onUjTargy(uj: Targy) {
        thread {
            val insertId = database.targyDAO().insert(uj)
            uj.id = insertId
            runOnUiThread {
                adapter.addItem(uj)
            }
        }
        loadItemsInBackground()
    }
}