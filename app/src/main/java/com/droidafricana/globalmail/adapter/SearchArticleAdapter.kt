package com.droidafricana.globalmail.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidafricana.globalmail.databinding.SearchNewsArticleItemBinding
import com.droidafricana.globalmail.domain.Article

class SearchArticleAdapter(private val articleClickListenerInterface: SearchArticleClickListenerInterface) :
        ListAdapter<Article, SearchArticleAdapter.MyNewsHolder>(SearchArticleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyNewsHolder {
        return MyNewsHolder.from(parent)
    }

    override fun onBindViewHolder(myViewHolder: MyNewsHolder, position: Int) {
        myViewHolder.bind(getItem(position), articleClickListenerInterface)
    }

    class MyNewsHolder private constructor(private val binding: SearchNewsArticleItemBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(currentArticle: Article, articleClickListenerInterface: SearchArticleClickListenerInterface) {
            binding.article = currentArticle
            binding.articleClickListener = articleClickListenerInterface

            binding.executePendingBindings()
        }

        //For inflating the view
        companion object {
            fun from(parent: ViewGroup): MyNewsHolder {
                val binding = SearchNewsArticleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return MyNewsHolder(binding)
            }
        }
    }
}

class SearchArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.articleUrl == newItem.articleUrl
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}

interface SearchArticleClickListenerInterface {
    fun onArticleClick(article: Article)
}
