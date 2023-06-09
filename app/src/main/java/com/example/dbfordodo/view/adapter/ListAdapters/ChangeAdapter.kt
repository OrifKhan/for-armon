package com.example.dbfordodo.view.adapter.ListAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dbfordodo.R
import com.example.dbfordodo.databinding.ComboDezignBinding
import com.example.dbfordodo.view.diffUtils.ChangDiffutils
import islom.din.dodo_ilmhona_proskills.db.data.Pizza

class ChangeAdapter:ListAdapter<Pizza, ChangeAdapter.ChangeViewHolder>(ChangDiffutils()) {
    var onClickItem:((Pizza, Int)->Unit)? = null


    inner class ChangeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ComboDezignBinding.bind(itemView)

        fun bind(pizza: Pizza){
            binding.imageComboDezign.setImageResource(pizza.image)
            binding.nameComboDezign.text = pizza.name
            binding.descriptionComboDezig.text = pizza.name

            binding.root.setOnClickListener {
                onClickItem?.invoke(pizza,adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChangeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.combo_dezign,parent,false)
        return ChangeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChangeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

