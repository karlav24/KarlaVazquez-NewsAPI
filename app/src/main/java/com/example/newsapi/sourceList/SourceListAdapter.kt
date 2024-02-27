package com.example.newsapi.sourceList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.R
import com.example.newsapi.data.Source
import com.example.newsapi.bind
class SourceListAdapter(
    private val onItemClick: (Source) -> Unit
) : RecyclerView.Adapter<SourceListViewHolder>() {
    private val sources: MutableList<Source> = mutableListOf()

    fun setSources(list: List<Source>) {
        sources.clear()
        sources.addAll(list)
        notifyDataSetChanged()
        Log.d("Adapter", "Sources updated: ${sources.size}")
    }

    override fun getItemCount(): Int = sources.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceListViewHolder {
        return SourceListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: SourceListViewHolder, position: Int) {
        holder.bind(sources[position], onItemClick)
    }
}

class SourceListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvSourceAvatar by view.bind<TextView> { R.id.tvSourceAvatar }
    private val tvSourceTitle by view.bind<TextView> { R.id.tvSourceTitle }
    private val tvSourceCategory by view.bind<TextView> { R.id.tvSourceCategory }
    private val tvSourceDescription by view.bind<TextView> { R.id.tvSourceDescription }

    fun bind(source: Source, onItemClick: (Source) -> Unit) {
        with(itemView) {
            setOnClickListener {
                onItemClick(source)
            }

            tvSourceAvatar.text = getAvatarText(source)
            tvSourceTitle.text = source.name
            tvSourceCategory.text = source.category
            tvSourceDescription.text = source.description
        }
    }

    private fun getAvatarText(source: Source): String {
        return source.name.split(" ")
            .filterIndexed { index, _ -> index < 2 }
            .map { it[0].toString() }
            .reduce { acc, s -> acc + s }
            .uppercase()
    }

    companion object {
        fun create(parent: ViewGroup): SourceListViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.source_item, parent, false)
            return SourceListViewHolder(view)
        }
    }
}