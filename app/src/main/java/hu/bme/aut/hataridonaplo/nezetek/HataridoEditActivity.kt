package hu.bme.aut.hataridonaplo.nezetek

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.hataridonaplo.R
import hu.bme.aut.hataridonaplo.adat.Adatbazis
import hu.bme.aut.hataridonaplo.adat.Hatarido
import hu.bme.aut.hataridonaplo.adat.Targy
import hu.bme.aut.hataridonaplo.databinding.ActivityHataridoBinding
import hu.bme.aut.hataridonaplo.databinding.ActivityHataridoEditBinding
import java.util.*
import kotlin.concurrent.thread

class HataridoEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHataridoEditBinding
    private lateinit var database: Adatbazis

    private fun isValid() :Boolean
    {
        if(!binding.etNev.text.isNotEmpty()) return false
        if(binding.etHet.text.toString() != "")
        {
            if(binding.etHet.text.toString().toInt() > 15 || binding.etHet.text.toString().toInt() < 0) return false
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.kicsuszas, R.anim.becsuszas)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHataridoEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = Adatbazis.getInstance(applicationContext)
        var id = getIntent().getStringExtra("ID")?.toLong()
        var h = Hatarido(nev="Dummy hatarido -> Lekerdezesi hiba",datum="2020-20-20",het=0, oraigeny=0,tipus= Hatarido.Tipus.ZH,targyNev="",megjegyzes="",teljesitett=false)
        var targyak = listOf<Targy>()
        var targyNevLista = mutableListOf<String>()
        thread {
            if(id == null) Snackbar.make(binding.root,getString(R.string.not_found), Snackbar.LENGTH_LONG).show()
            else h = database.hataridoDAO().getByID(id)
            targyak = database.targyDAO().getAll()

            runOnUiThread {

                targyak.forEach{targyNevLista.add(it.nevTeljes)}
                var targyArray = targyNevLista.toTypedArray()
                var targySelPos : Int
                targySelPos = 0
                val hossz = targyak.size-1
                for( i in 0..hossz)
                {
                    if(targyak.elementAt(i).nevTeljes.equals(h.targyNev)) targySelPos = i
                }
                binding.spTargy.adapter = ArrayAdapter(applicationContext, R.layout.support_simple_spinner_dropdown_item, targyArray)
                binding.spTargy.setSelection(targySelPos)
                binding.etNev.setText(h.nev)
                binding.cbTelj.isChecked = h.teljesitett
                var datum = h.datum.split('-')
                binding.date.updateDate(datum.elementAt(0).toInt(),datum.elementAt(1).toInt()-1,datum.elementAt(2).toInt())
                binding.spTipus.adapter = ArrayAdapter(applicationContext, R.layout.support_simple_spinner_dropdown_item, resources.getStringArray(R.array.tipusok))
                binding.spTipus.setSelection(h.tipus.ordinal)
                binding.etOra.setText(h.oraigeny.toString())
                binding.etHet.setText(h.het.toString())
                binding.etMegj.setText(h.megjegyzes)


            }
        }
        binding.btnDelete.setOnClickListener {
            thread {
                database.hataridoDAO().deleteItem(h)
                runOnUiThread {
                    finish()
                    overridePendingTransition(R.anim.kicsuszas, R.anim.becsuszas)
                }
            }
        }
        binding.btnSave.setOnClickListener{
            if(isValid())
            {
                var Het : Int
                var Ora : Int
                var Targy = getString(R.string.NA)

                if(binding.etHet.text.toString() == "") Het = 0
                else Het = binding.etHet.text.toString().toInt()

                if(binding.etOra.text.toString() == "") Ora = 0
                else Ora = binding.etOra.text.toString().toInt()

                if(binding.spTargy.selectedItem != null)Targy = binding.spTargy.selectedItem.toString()
                h.nev = binding.etNev.text.toString()
                h.teljesitett = binding.cbTelj.isChecked
                h.tipus = Hatarido.Tipus.ZH
                h.targyNev = Targy
                if(binding.spTipus.selectedItem.toString() == "HF")h.tipus = Hatarido.Tipus.HF
                else if(binding.spTipus.selectedItem.toString() == "Vizsga")h.tipus = Hatarido.Tipus.VIZSGA
                else if(binding.spTipus.selectedItem.toString() == "Konzultáció")h.tipus = Hatarido.Tipus.KONZULTACIO
                else if(binding.spTipus.selectedItem.toString() == "Egyéb")h.tipus = Hatarido.Tipus.EGYEB
                else if(binding.spTipus.selectedItem.toString() == "Labor")h.tipus = Hatarido.Tipus.LABOR
                h.datum = String.format(Locale.getDefault(), "%04d-%02d-%02d", binding.date.year, binding.date.month + 1, binding.date.dayOfMonth)
                h.het = Het
                h.oraigeny = Ora
                h.megjegyzes = binding.etMegj.text.toString()


                thread {
                    database.hataridoDAO().update(h)
                    runOnUiThread {
                        finish()
                        overridePendingTransition(R.anim.kicsuszas, R.anim.becsuszas)
                    }
                }
            }
            else Snackbar.make(binding.root,getString(R.string.invalid_hatarido),Snackbar.LENGTH_LONG).show()

        }
    }
}