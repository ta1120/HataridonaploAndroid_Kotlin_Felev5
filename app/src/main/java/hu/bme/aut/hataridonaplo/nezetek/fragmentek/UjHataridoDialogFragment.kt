package hu.bme.aut.hataridonaplo.nezetek.fragmentek

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.hataridonaplo.R
import hu.bme.aut.hataridonaplo.adat.Adatbazis
import hu.bme.aut.hataridonaplo.adat.Hatarido
import hu.bme.aut.hataridonaplo.adat.Targy
import hu.bme.aut.hataridonaplo.databinding.DialogUjHataridoBinding
import java.util.*
import kotlin.concurrent.thread

class UjHataridoDialogFragment : DialogFragment() {
    interface UjHataridoDialogListener {
        fun onUjHatarido(uj: Hatarido)
        fun onFailToMake()
    }

    private lateinit var listener: UjHataridoDialogListener

    private lateinit var binding: DialogUjHataridoBinding


    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? UjHataridoDialogListener
            ?: throw RuntimeException(getString(R.string.impl_mistake))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogUjHataridoBinding.inflate(LayoutInflater.from(context))
        binding.spTipus.adapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.tipusok)
        )
        var database = Adatbazis.getInstance(requireContext())
        var targyak = listOf<Targy>()
        var targyNevLista = mutableListOf<String>()
        thread {
            targyak = database.targyDAO().getAll()

            activity?.runOnUiThread {
                targyak.forEach{targyNevLista.add(it.nevTeljes)}
                var targyArray = targyNevLista.toTypedArray()
                binding.spTargy.adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, targyArray)
            }
        }
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.newHatarido))
            .setView(binding.root)
            .setPositiveButton(getString(R.string.ok)) { dialogInterface, i ->
                if (isValid()) {
                    listener.onUjHatarido(getHatarido())
                }
                else listener.onFailToMake()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .create()


    }
    private fun isValid() :Boolean
    {
        if(!binding.etNev.text.isNotEmpty()) return false
        if(binding.etHet.text.toString() != "")
        {
            if(binding.etHet.text.toString().toInt() > 15 || binding.etHet.text.toString().toInt() < 0) return false
        }
        return true
    }

    private fun getHatarido() : Hatarido
    {
        var Het : Int
        var Ora : Int
        var Targy = getString(R.string.NA)

        if(binding.etHet.text.toString() == "") Het = 0
        else Het = binding.etHet.text.toString().toInt()

        if(binding.etOra.text.toString() == "") Ora = 0
        else Ora = binding.etOra.text.toString().toInt()

        if(binding.spTargy.selectedItem != null)Targy = binding.spTargy.selectedItem.toString()


       return Hatarido(
            nev = binding.etNev.text.toString(),
            tipus = Hatarido.Tipus.getByOrdinal(binding.spTipus.selectedItemPosition)
                ?: Hatarido.Tipus.ZH,
            targyNev = Targy,
            datum = String.format(Locale.getDefault(), "%04d-%02d-%02d", binding.date.year, binding.date.month + 1, binding.date.dayOfMonth),
            het = Het,
            oraigeny = Ora,
            megjegyzes = binding.etMegj.text.toString(),
            teljesitett = false
        )
    }

    companion object {
        const val TAG = "UjTargyDialogFragment"
    }
}
