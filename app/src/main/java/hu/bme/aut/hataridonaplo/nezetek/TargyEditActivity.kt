package hu.bme.aut.hataridonaplo.nezetek

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.hataridonaplo.R
import hu.bme.aut.hataridonaplo.adat.Adatbazis
import hu.bme.aut.hataridonaplo.adat.Felev
import hu.bme.aut.hataridonaplo.adat.Targy
import hu.bme.aut.hataridonaplo.databinding.ActivityTargyEditBinding
import kotlin.concurrent.thread


class TargyEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTargyEditBinding
    private lateinit var database: Adatbazis

    private fun isValid() = binding.etNevTeljes.text.isNotEmpty()

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.kicsuszas, R.anim.becsuszas)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTargyEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = Adatbazis.getInstance(applicationContext)
        var id = getIntent().getStringExtra("ID")?.toLong()
        var t = Targy(nevTeljes ="Dummy targy -> Lekerdezesi hiba", nevRovid = "", megjegyzes = "", felev = "", kredit = 0)
        var felevek = listOf<Felev>()
        var felevNevLista = mutableListOf<String>()
        thread {
            if(id == null) Snackbar.make(binding.root,getString(R.string.not_found), Snackbar.LENGTH_LONG).show()
            else t = database.targyDAO().getByID(id)
            felevek = database.felevDAO().getAll()

            runOnUiThread {

                felevek.forEach{felevNevLista.add(it.nev)}
                var felevArray = felevNevLista.toTypedArray()
                var felevSelPos : Int
                felevSelPos = 0
                val hossz = felevek.size-1
                for( i in 0..hossz)
                {
                    if(felevek.elementAt(i).nev.equals(t.felev)) felevSelPos = i
                }
                binding.spFelev.adapter = ArrayAdapter(applicationContext, R.layout.support_simple_spinner_dropdown_item, felevArray)
                binding.spFelev.setSelection(felevSelPos)
                binding.etNevTeljes.setText(t.nevTeljes)
                binding.etNevRovid.setText(t.nevRovid)
                binding.etKredit.setText(t.kredit.toString())
                binding.etMegj.setText(t.megjegyzes)


            }
        }
        binding.btnDelete.setOnClickListener {
            thread {
                database.targyDAO().deleteItem(t)
                runOnUiThread {
                    finish()
                    overridePendingTransition(R.anim.kicsuszas, R.anim.becsuszas)
                }
            }
        }
        binding.btnSave.setOnClickListener{
            if(isValid())
            {
                var Kredit: Int
                var Felev = getString(R.string.NA)
                var Rovid = getString(R.string.NA)
                var Megj = getString(R.string.NA)

                if(binding.etNevRovid.text.isNotEmpty()) Rovid = binding.etNevRovid.text.toString()
                if(binding.etMegj.text.isNotEmpty()) Megj = binding.etMegj.text.toString()

                if(binding.etKredit.text.toString() == "") Kredit = 0
                else Kredit = binding.etKredit.text.toString().toInt()

                if(binding.spFelev.selectedItem != null)Felev = binding.spFelev.selectedItem.toString()
                t.nevTeljes = binding.etNevTeljes.text.toString()
                t.nevRovid = Rovid
                t.felev = Felev
                t.megjegyzes = Megj
                t.kredit = Kredit

                thread {
                    database.targyDAO().update(t)
                    runOnUiThread {
                        finish()
                        overridePendingTransition(R.anim.kicsuszas, R.anim.becsuszas)
                    }
                }
            }
            else Snackbar.make(binding.root,getString(R.string.name_missing),Snackbar.LENGTH_LONG).show()

        }
    }
}