package com.raywenderlich.wewatch.add

import com.raywenderlich.wewatch.model.Movie

interface AddContract {

    interface ViewInterface {
        fun displayMessage(message: String)

        fun displayError(errorMessage: String)

        fun onMovieAdded()
    }

    interface PresenterInterface {
        fun onAddMovieClicked(movie: Movie)

    }

}