package com.raywenderlich.wewatch.add

import com.raywenderlich.wewatch.model.LocalDataSource
import com.raywenderlich.wewatch.model.Movie

class AddPresenter(
    private var viewInterface: AddContract.ViewInterface,
    private var dataSource: LocalDataSource
) : AddContract.PresenterInterface {

    override fun onAddMovieClicked(movie: Movie) {
        if (movie.title.isNullOrBlank()) {
            viewInterface.displayErrorMessage("Movie title cannot be empty.")
        } else {
            dataSource.insert(movie)
            viewInterface.onMovieAdded()
        }
    }

    override fun onSearchMovieClicked(title: String) {
        if (title.isBlank()) {
            viewInterface.displayErrorMessage("Title cannot be empty!")
            return
        }

        viewInterface.onValidSearchRequest()
    }

}