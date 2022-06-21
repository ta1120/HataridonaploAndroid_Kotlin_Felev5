package hu.bme.aut.hataridonaplo.nezetadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.hataridonaplo.adat.Felev
import hu.bme.aut.hataridonaplo.adat.Hatarido
import hu.bme.aut.hataridonaplo.databinding.FelevListBinding

class FelevAdapter(private val listener: FelevClickListener) :
    RecyclerView.Adapter<FelevAdapter.FelevViewHolder>() {

    private var felevek = mutableListOf<Felev>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FelevViewHolder(
       FelevListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: FelevViewHolder, position: Int) {
        val felev = felevek[position]

        holder.binding.tvFelev.text = felev.nev
        holder.itemView.setOnClickListener{listener.onItemClick(felev)}
    }

    fun addItem(item: Felev) {
        felevek.add(item)
        notifyItemInserted(felevek.size - 1)
        val lista = felevek.toMutableList()
        update(lista)
    }

    fun update(felevLista: List<Felev>) {
        felevek.clear()
        felevek.addAll(felevLista)
        felevek = felevek.toList().sortedWith(compareBy<Felev>{it.nev}).toMutableList()
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = felevek.size

    interface FelevClickListener {
        fun onItemClick(item: Felev)
    }

    inner class FelevViewHolder(val binding: FelevListBinding) : RecyclerView.ViewHolder(binding.root)
}
