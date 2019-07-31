package com.droidafricana.globalmail.utils

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.droidafricana.globalmail.R
import com.droidafricana.globalmail.domain.Article
import com.squareup.picasso.Picasso

object PicassoTags {
    const val ARTICLE_TAG = "mailerArticlesTag"
}

@BindingAdapter("imageArticle")
fun ImageView.setArticleImage(currentArticle: Article) {
    val articleUrlToImage = currentArticle.articleUrlToImage
    if (articleUrlToImage != null && articleUrlToImage.trim { it <= ' ' }.isNotEmpty()) {
        Picasso.get()
                .load(articleUrlToImage)
                .tag(PicassoTags.ARTICLE_TAG) //To pause loading of images while scrolling for good data use
                .fit()
                .centerCrop()
                .placeholder(R.drawable.article_loading_animation)
                .error(R.drawable.ic_broken_image)
                .into(this)

    } else {
        Picasso.get()
                .load(R.drawable.ic_broken_image)
                .fit()
                .centerCrop()
                .into(this)
    }
}

@BindingAdapter("descriptionArticle")
fun TextView.setArticleDescription(currentArticle: Article) {
    val articleDesc = currentArticle.articleDescription
    val articleCont = currentArticle.articleContent
    text = when {
        (articleDesc != null && articleDesc.isNotEmpty()) -> {
            articleDesc
        }
        (articleCont != null && articleCont.isNotEmpty()) -> {
            articleCont
        }
        else -> currentArticle.articleTitle
    }
}

/**
 * Binding adapter used to hide the loading indicator once data becomes available
 */
@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(viewGroup: ViewGroup, it: LiveData<List<Article>>?) {
    if (it?.value?.size == 0 || it == null) {
        viewGroup.visibility = View.VISIBLE
    } else {
        viewGroup.visibility = View.GONE
    }
}

