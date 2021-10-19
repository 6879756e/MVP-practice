package com.raywenderlich.wewatch.add

import com.raywenderlich.wewatch.BaseViewInterface
import com.raywenderlich.wewatch.model.Movie

interface AddContract {

    interface ViewInterface: BaseViewInterface {
        fun onMovieAdded()

        fun onValidSearchRequest()
    }

    interface PresenterInterface {
        fun onAddMovieClicked(movie: Movie)

        fun onSearchMovieClicked(toString: String)
    }

}