package com.droidafricana.globalmail.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.droidafricana.globalmail.repository.ArticleRepository
import com.droidafricana.globalmail.ui.business.BusinessViewModel
import com.droidafricana.globalmail.ui.entertainment.EntertainmentViewModel
import com.droidafricana.globalmail.ui.favs.FavsViewModel
import com.droidafricana.globalmail.ui.general.GeneralViewModel
import com.droidafricana.globalmail.ui.health.HealthViewModel
import com.droidafricana.globalmail.ui.science.ScienceViewModel
import com.droidafricana.globalmail.ui.search.SearchViewModel
import com.droidafricana.globalmail.ui.sports.SportsViewModel
import com.droidafricana.globalmail.ui.technology.TechnologyViewModel

/**
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class ArticleViewModelFactory constructor(
        private val application: Application,
        private val articleCategory: String?,
        private val queryParam: String?,
        private val articleRepository: ArticleRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(BusinessViewModel::class.java) ->
                        BusinessViewModel(application, articleRepository)
                    isAssignableFrom(EntertainmentViewModel::class.java) ->
                        EntertainmentViewModel(application, articleRepository)
                    isAssignableFrom(HealthViewModel::class.java) ->
                        HealthViewModel(application, articleRepository)
                    isAssignableFrom(ScienceViewModel::class.java) ->
                        ScienceViewModel(application, articleRepository)
                    isAssignableFrom(GeneralViewModel::class.java) ->
                        GeneralViewModel(application, articleRepository)
                    isAssignableFrom(TechnologyViewModel::class.java) ->
                        TechnologyViewModel(application, articleRepository)
                    isAssignableFrom(SportsViewModel::class.java) ->
                        SportsViewModel(application, articleRepository)
                    isAssignableFrom(FavsViewModel::class.java) ->
                        FavsViewModel(articleRepository)
                    isAssignableFrom(SearchViewModel::class.java) ->
                        SearchViewModel(application, articleCategory, queryParam)
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}