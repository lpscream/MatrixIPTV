package net.matrixhome.matrixiptv.ui.adapters.Channels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import net.matrixhome.matrixiptv.R

class CategoryAdapter(
    private val clickListener: OnCategoryItemClickListener)
    : RecyclerView.Adapter<CategoryAdapter.CategoriesViewHolder>() {

    private val categories = ArrayList<String>()

    fun update(new: Array<String>){
        categories.clear()
        categories.addAll(new)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when(viewType){
            0 -> CategoriesViewHolder.Base(R.layout.item_category.makeView(parent), clickListener)
            1 -> CategoriesViewHolder.ProgressCategory(R.layout.item_skeleton_category.makeView(parent))
            2 -> CategoriesViewHolder.FailCategory(R.layout.fail_category.makeView(parent))
            else -> CategoriesViewHolder.FailCategory(R.layout.fail_category.makeView(parent))
        }

    override fun getItemViewType(position: Int) =
        when(categories[position]){
            "progress" -> 1
            "fail" -> 2
            else -> 0
        }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) =
        holder.bind(categories[position])

    override fun getItemCount(): Int = categories.size

    abstract class CategoriesViewHolder(item: View): RecyclerView.ViewHolder(item){
        open fun bind(name: String){}

        class FailCategory(view: View):CategoriesViewHolder(view)

        class Base(view: View, private val clickListener: OnCategoryItemClickListener)
            : CategoriesViewHolder(view){
            private val rootLayout = itemView.findViewById<ConstraintLayout>(R.id.super_layout_category)
            private val categoryName: TextView = itemView.findViewById(R.id.category_name)
            private val selector = itemView.findViewById<ConstraintLayout>(R.id.selector_category)
            override fun bind(name: String) {
                categoryName.text = name
                rootLayout.setOnClickListener {
                    clickListener.onCategoryItemClick(name, absoluteAdapterPosition)
                }
                rootLayout.setOnFocusChangeListener { view, b ->
                    if (b){
                        selector.visibility = View.VISIBLE
                        view.animate().apply {
                            scaleX(1.1f)
                            scaleY(1.1f)
                            duration = 200
                        }.start()
                        view.elevation = 20f
                    }else{
                        selector.visibility = View.GONE
                        view.animate().apply {
                            scaleX(1.0f)
                            scaleY(1.0f)
                            duration = 200
                        }.start()
                        view.elevation = 0f
                    }
                }
            }
        }

        class ProgressCategory(view: View): CategoriesViewHolder(view)
    }

    interface OnCategoryItemClickListener{
        fun onCategoryItemClick(item: String, position: Int)
    }
}

private fun Int.makeView(parent: ViewGroup) =
    LayoutInflater.from(parent.context).inflate(this, parent, false)