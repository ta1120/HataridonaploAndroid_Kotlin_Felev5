package hu.bme.aut.hataridonaplo.nezetadapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.hataridonaplo.R
import hu.bme.aut.hataridonaplo.adat.Hatarido
import hu.bme.aut.hataridonaplo.databinding.HataridoListBinding

class HataridoAdapter(private val listener: HataridoClickListener) :
    RecyclerView.Adapter<HataridoAdapter.HataridoViewHolder>() {

    private var hataridok = mutableListOf<Hatarido>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HataridoViewHolder(
        HataridoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: HataridoViewHolder, position: Int) {
        val hatarido = hataridok[position]

        holder.binding.tvNev.text = hatarido.nev
        holder.binding.tvDatum.text = hatarido.datum
        holder.binding.tvTipus.text = holder.binding.tvTipus.context.resources.getStringArray(R.array.tipusok).elementAt(hatarido.tipus.ordinal)
        holder.binding.tvTargy.text = hatarido.targyNev
        if(hatarido.teljesitett)
        {
            holder.binding.tvTelj.text = holder.binding.tvTelj.context.getString(R.string.done)
            holder.binding.tvTelj.setTextColor(Color.parseColor("#006400"))
        }
        else
        {
            holder.binding.tvTelj.text = holder.binding.tvTelj.context.getString(R.string.not_yet_done)
            holder.binding.tvTelj.setTextColor(Color.parseColor("#8B0000"))
        }
        holder.itemView.setOnClickListener{listener.onClick(hatarido)}
    }

    fun addItem(item: Hatarido) {
        hataridok.add(item)

        notifyItemInserted(hataridok.size - 1)
        val lista = hataridok.toMutableList()
        update(lista)
    }

    fun update(hataridoLista: List<Hatarido>) {
        hataridok.clear()
        hataridok.addAll(hataridoLista)
        hataridok = hataridok.toList().sortedWith(compareBy<Hatarido>{it.datum}.thenBy{it.nev}).toMutableList()
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = hataridok.size

    interface HataridoClickListener {
        fun onClick(item: Hatarido)
    }

    inner class HataridoViewHolder(val binding: HataridoListBinding) : RecyclerView.ViewHolder(binding.root)
}
