package com.droidafricana.globalmail.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
        private val queryParam: String?
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(BusinessViewModel::class.java) ->
                        BusinessViewModel(application, articleCategory)
                    isAssignableFrom(EntertainmentViewModel::class.java) ->
                        EntertainmentViewModel(application, articleCategory)
                    isAssignableFrom(HealthViewModel::class.java) ->
                        HealthViewModel(application, articleCategory)
                    isAssignableFrom(ScienceViewModel::class.java) ->
                        ScienceViewModel(application, articleCategory)
                    isAssignableFrom(GeneralViewModel::class.java) ->
                        GeneralViewModel(application, articleCategory)
                    isAssignableFrom(TechnologyViewModel::class.java) ->
                        TechnologyViewModel(application, articleCategory)
                    isAssignableFrom(SportsViewModel::class.java) ->
                        SportsViewModel(application, articleCategory)
                    isAssignableFrom(FavsViewModel::class.java) ->
                        FavsViewModel(application)
                    isAssignableFrom(SearchViewModel::class.java) ->
                        SearchViewModel(application, articleCategory, queryParam)
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}