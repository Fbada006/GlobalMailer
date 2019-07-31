package com.droidafricana.globalmail.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.droidafricana.globalmail.databinding.NewsArticleItemBinding
import com.droidafricana.globalmail.domain.Article
import com.like.LikeButton
import com.like.OnLikeListener

class MyArticleAdapter(private val articleClickListenerInterface: ArticleClickListenerInterface) :
        ListAdapter<Article, MyArticleAdapter.MyNewsHolder>(ArticleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyNewsHolder {
        return MyNewsHolder.from(parent)
    }

    override fun onBindViewHolder(myViewHolder: MyNewsHolder, position: Int) {
        myViewHolder.bind(getItem(position), articleClickListenerInterface)
    }

    class MyNewsHolder private constructor(private val binding: NewsArticleItemBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(currentArticle: Article, articleClickListenerInterface: ArticleClickListenerInterface) {
            binding.article = currentArticle
            binding.articleClickListener = articleClickListenerInterface

            binding.starButton.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton?) {
                    articleClickListenerInterface.liked(likeButton, currentArticle)
                }

                override fun unLiked(likeButton: LikeButton?) {
                    articleClickListenerInterface.unLiked(likeButton, currentArticle)
                }
            })
            binding.executePendingBindings()
        }

        //For inflating the view
        companion object {
            fun from(parent: ViewGroup): MyNewsHolder {
                val binding = NewsArticleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return MyNewsHolder(binding)
            }
        }
    }
}

class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.articleUrl == newItem.articleUrl
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}

interface ArticleClickListenerInterface {
    fun onArticleClick(article: Article)
    fun liked(likeButton: LikeButton?, article: Article)
    fun unLiked(likeButton: LikeButton?, article: Article)
}
