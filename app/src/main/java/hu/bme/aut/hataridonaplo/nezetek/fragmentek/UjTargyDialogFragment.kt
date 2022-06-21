package hu.bme.aut.hataridonaplo.nezetek.fragmentek

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.hataridonaplo.R
import hu.bme.aut.hataridonaplo.adat.Felev
import hu.bme.aut.hataridonaplo.adat.Targy
import hu.bme.aut.hataridonaplo.databinding.DialogUjTargyBinding
import hu.bme.aut.hataridonaplo.adat.Adatbazis
import hu.bme.aut.hataridonaplo.nezetek.TargyActivity
import kotlin.concurrent.thread

class UjTargyDialogFragment : DialogFragment() {
    interface UjTargyDialogListener {
        fun onUjTargy(uj: Targy)
    }

    private lateinit var listener: UjTargyDialogListener

    private lateinit var binding: DialogUjTargyBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? UjTargyDialogListener
            ?: throw RuntimeException(getString(R.string.impl_mistake))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        var act = activity
        binding = DialogUjTargyBinding.inflate(LayoutInflater.from(context))
        var database = Adatbazis.getInstance(requireContext())
        var felevek = listOf<Felev>()
        var felevNevLista = mutableListOf<String>()
        thread {
            felevek = database.felevDAO().getAll()

            activity?.runOnUiThread {
                felevek.forEach{felevNevLista.add(it.nev)}
                var felevArray = felevNevLista.toTypedArray()
                binding.spFelev.adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, felevArray)
                val felev = (requireActivity() as TargyActivity).felevNev
                val hossz = felevek.size-1
                var selPos = 0
                for(i in 0..hossz){if(felevek.elementAt(i).nev.equals(felev))selPos = i}
                binding.spFelev.setSelection(selPos)
            }
        }
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.newTargy))
            .setView(binding.root)
            .setPositiveButton(getString(R.string.ok)) { dialogInterface, i ->
                if (isValid()) {
                    listener.onUjTargy(getTargy())
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .create()
    }
    private fun isValid() = binding.etNevTeljes.text.isNotEmpty()

    private fun getTargy() : Targy {
        var Kredit : Int
        var Felev = getString(R.string.NA)
        var Rovid = getString(R.string.NA)
        var Megj = getString(R.string.NA)

        if(binding.etKredit.text.toString() == "") Kredit = 0
        else Kredit = binding.etKredit.text.toString().toInt()
        if(binding.etNevRovid.text.toString() != "") Rovid = binding.etNevRovid.text.toString()
        if(binding.etMegj.text.toString() != "") Megj = binding.etMegj.text.toString()



        if(binding.spFelev.selectedItem != null)Felev = binding.spFelev.selectedItem.toString()

        return Targy(
            felev = Felev,
            nevTeljes = binding.etNevTeljes.text.toString(),
            nevRovid = Rovid,
            megjegyzes = Megj,
            kredit = Kredit)
    }

    companion object {
        const val TAG = "UjTargyDialogFragment"
    }
}
