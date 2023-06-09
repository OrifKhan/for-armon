package com.example.dbfordodo.view.adapter.ListAdapters

// -------- NOT USED ------------ //
// ------------------------------//


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.dbfordodo.R
import com.example.dbfordodo.databinding.CategoryItemBinding
import islom.din.dodo_ilmhona_proskills.QA.diffUtils.CategoryDiffUtils
import islom.din.dodo_ilmhona_proskills.db.data.Category

class CategoryAdapter() :
    ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(CategoryDiffUtils()) {

    var onClickedSelectedItem : ((Int) -> (Unit))? = null

    var onFilter: ((String) -> (Unit))? = null

    //ViewHolder
    inner class CategoryViewHolder(itemView: View) : ViewHolder(itemView) {
        private val binding = CategoryItemBinding.bind(itemView)

        //Binding the data and setting background and text color
        fun bind(categoryData: Category) {
            binding.categoryText.text = categoryData.name
            if (categoryData.isChecked==1) {
                binding.categoryText.setBackgroundResource(R.drawable.shape_category_checked)
                binding.categoryText.setTextColor(ContextCompat.getColor(binding.root.context,R.color.secondaryColor))
            } else {
                binding.categoryText.setBackgroundResource(R.drawable.shape_category_unchecked)
                binding.categoryText.setTextColor(ContextCompat.getColor(binding.root.context,R.color.grey_darker))
            }

            //OnClick Listener for certain Item
            binding.categoryText.setOnClickListener {
                onClickedSelectedItem?.invoke(adapterPosition)
                onFilter?.invoke(categoryData.name)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}