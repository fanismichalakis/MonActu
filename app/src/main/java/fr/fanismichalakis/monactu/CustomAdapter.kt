package fr.fanismichalakis.monactu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter (private val dataSet: ArrayList<ArticlesObject>, private val article_listener: OnArticleListener) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

        class ViewHolder(v: View, onArticleListener: OnArticleListener) : RecyclerView.ViewHolder(v) {
        val txtTitle: TextView
        val txtAuthor: TextView
        val txtDate: TextView

        init {
            v.setOnClickListener {
                onArticleListener.onArticleClick(adapterPosition)
            }
            txtTitle = v.findViewById(R.id.txtTitle)
            txtAuthor = v.findViewById(R.id.txtAuthor)
            txtDate = v.findViewById(R.id.txtDate)
        }
    }

    interface OnArticleListener{
        fun onArticleClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
        return ViewHolder(view.inflate(R.layout.list_item_article, parent, false), article_listener)
    }

    /*override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_article, viewGroup, false)

        return ViewHolder(v)
    }*/

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d(TAG, "Element $position set")

        viewHolder.txtAuthor.text = dataSet[position].author
        viewHolder.txtTitle.text  = dataSet[position].title
        viewHolder.txtDate.text = dataSet[position].date
    }

    override fun getItemCount() = dataSet.size

    companion object {
        private val TAG = "CustomAdapter"
    }
}