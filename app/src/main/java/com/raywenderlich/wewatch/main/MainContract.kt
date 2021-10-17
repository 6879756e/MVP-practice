package com.raywenderlich.wewatch.main

import com.raywenderlich.wewatch.model.Movie

interface MainContract {

    interface ViewInterface {
        fun displayMessage(message: String)

        fun displayError(errorMessage: String)

        fun displayMovies(movieList: List<Movie>)

        fun displayNoMovies()
    }

    interface PresenterInterface {
        fun onDeleteMoviesClicked(selectedMovies: Set<Movie>)

        fun getMyMoviesList()

        fun stop()
    }

}