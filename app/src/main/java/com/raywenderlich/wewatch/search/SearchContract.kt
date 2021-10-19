package com.raywenderlich.wewatch.search

import com.raywenderlich.wewatch.BaseViewInterface
import com.raywenderlich.wewatch.model.TmdbResponse

interface SearchContract {

    interface ViewInterface: BaseViewInterface {
        fun displayResults(tmdbResponse: TmdbResponse)

        fun displayNoResults()
    }

    interface PresenterInterface {
        fun onStop()

        fun getSearchResults(query: String)
    }

}