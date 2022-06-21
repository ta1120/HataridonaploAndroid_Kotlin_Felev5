package hu.bme.aut.hataridonaplo.nezetek.fragmentek

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.hataridonaplo.R
import hu.bme.aut.hataridonaplo.adat.Felev
import hu.bme.aut.hataridonaplo.databinding.DialogUjFelevBinding

class UjFelevDialogFragment : DialogFragment() {
    interface UjFelevDialogListener {
        fun onUjFelev(uj: Felev)
    }

    private lateinit var listener: UjFelevDialogListener

    private lateinit var binding: DialogUjFelevBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? UjFelevDialogListener
            ?: throw RuntimeException(getString(R.string.impl_mistake))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogUjFelevBinding.inflate(LayoutInflater.from(context))

        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.newFelev))
            .setView(binding.root)
            .setPositiveButton(getString(R.string.ok)) { dialogInterface, i ->
                if (isValid()) {
                    listener.onUjFelev(getFelev())
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .create()
    }
    private fun isValid() = binding.etNev.text.isNotEmpty()

    private fun getFelev() = Felev(
        nev = binding.etNev.text.toString(),
        szam = binding.etSzam.text.toString().toInt()
    )


    companion object {
        const val TAG = "UjFelevDialogFragment"
    }
}
