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

class CustomAdapter (private val dataSet: ArrayList<ArticlesObject>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val txtTitle: TextView
        val txtAuthor: TextView

        init {
            v.setOnClickListener { Log.d("RecycleView","Element $adapterPosition clicked") }
            txtTitle = v.findViewById(R.id.txtTitle)
            txtAuthor = v.findViewById(R.id.txtAuthor)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_article, viewGroup, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d(TAG, "Element $position set")

        viewHolder.txtAuthor.text = dataSet[position].author
        viewHolder.txtTitle.text  = dataSet[position].title
    }

    override fun getItemCount() = dataSet.size

    companion object {
        private val TAG = "CustomAdapter"
    }
}