package hu.bme.aut.hataridonaplo.nezetek

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.hataridonaplo.R
import hu.bme.aut.hataridonaplo.adat.Adatbazis
import hu.bme.aut.hataridonaplo.adat.Felev
import hu.bme.aut.hataridonaplo.databinding.ActivityFelevBinding
import hu.bme.aut.hataridonaplo.nezetadapter.FelevAdapter
import hu.bme.aut.hataridonaplo.nezetek.fragmentek.UjFelevDialogFragment
import kotlin.concurrent.thread

class FelevActivity : AppCompatActivity(), FelevAdapter.FelevClickListener,UjFelevDialogFragment.UjFelevDialogListener {
    private lateinit var binding: ActivityFelevBinding

    private lateinit var database: Adatbazis
    private lateinit var adapter: FelevAdapter

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.kicsuszas, R.anim.becsuszas)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFelevBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        database = Adatbazis.getInstance(applicationContext)

        binding.uj.setOnClickListener {
            UjFelevDialogFragment().show(
                supportFragmentManager,
                UjFelevDialogFragment.TAG
            )
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = FelevAdapter(this)
        binding.rvFelev.layoutManager = LinearLayoutManager(this)
        binding.rvFelev.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.felevDAO().getAll()
            runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onItemClick(item: Felev) {
        val intent = Intent(this,TargyActivity::class.java)
        intent.putExtra("FelevNev",item.nev.toString())
        startActivity(intent)
        overridePendingTransition(R.anim.kicsuszas, R.anim.becsuszas);
        }

    override fun onUjFelev(uj: Felev) {
        thread {
            val insertId = database.felevDAO().insert(uj)
            uj.id = insertId
            runOnUiThread {
                adapter.addItem(uj)
            }
        }
    }
}