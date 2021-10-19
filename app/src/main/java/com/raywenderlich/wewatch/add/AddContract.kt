package com.raywenderlich.wewatch.add

import com.raywenderlich.wewatch.BaseViewInterface
import com.raywenderlich.wewatch.model.Movie

interface AddContract {

    interface ViewInterface: BaseViewInterface {
        fun onMovieAdded()
    }

    interface PresenterInterface {
        fun onAddMovieClicked(movie: Movie)
    }

}