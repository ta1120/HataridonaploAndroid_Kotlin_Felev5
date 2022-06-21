package hu.bme.aut.hataridonaplo.nezetadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.hataridonaplo.R
import hu.bme.aut.hataridonaplo.adat.Hatarido
import hu.bme.aut.hataridonaplo.adat.Targy
import hu.bme.aut.hataridonaplo.databinding.TargyListBinding

class TargyAdapter(private val listener: TargyClickListener) :
    RecyclerView.Adapter<TargyAdapter.TargyViewHolder>() {

    private var targyak = mutableListOf<Targy>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TargyViewHolder(
        TargyListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: TargyViewHolder, position: Int) {
        val targy = targyak[position]

        holder.binding.tvTargy.text = targy.nevTeljes
        holder.itemView.setOnClickListener{listener.onItemClick(targy)}

    }

    fun addItem(item: Targy) {
        targyak.add(item)
        notifyItemInserted(targyak.size - 1)
        val lista = targyak.toMutableList()
        update(lista)
    }

    fun update(targyLista: List<Targy>) {
        targyak.clear()
        targyak.addAll(targyLista)
        targyak = targyak.toList().sortedWith(compareBy<Targy>{it.nevTeljes}).toMutableList()
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = targyak.size

    interface TargyClickListener {
        fun onItemClick(item: Targy)
    }

    inner class TargyViewHolder(val binding: TargyListBinding) : RecyclerView.ViewHolder(binding.root)
}
