package com.raywenderlich.wewatch.main

import android.util.Log

import com.raywenderlich.wewatch.model.LocalDataSource
import com.raywenderlich.wewatch.model.Movie

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

const val TAG = "MainPresenter"

class MainPresenter(
    private var viewInterface: MainContract.ViewInterface,
    private var dataSource: LocalDataSource
): MainContract.PresenterInterface {

    private val myMoviesObservable: Observable<List<Movie>>
        get() = dataSource.allMovies

    private val observer: DisposableObserver<List<Movie>>
        get() = object : DisposableObserver<List<Movie>>() {

            override fun onNext(movieList: List<Movie>) {
                if (movieList.isNullOrEmpty()) {
                    Log.d(TAG, "No movies to display.")
                    viewInterface.displayNoMovies()
                } else {
                    viewInterface.displayMovies(movieList)
                }
            }

            override fun onError(@NonNull e: Throwable) {
                Log.e(TAG, "Error fetching movie list", e)
                viewInterface.displayError("Error fetching movie list")
            }

            override fun onComplete() {
                Log.d(TAG, "Completed")
            }
        }

    private val compositeDisposable = CompositeDisposable()

    override fun getMyMoviesList() {
        val myMoviesDisposable = myMoviesObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(observer)

        compositeDisposable.add(myMoviesDisposable)
    }

    override fun stop() {
        compositeDisposable.clear()
    }

    override fun onDeleteMoviesClicked(selectedMovies: Set<Movie>) {
        when (selectedMovies.size) {
            0 -> {
                viewInterface.displayError("No movies have been selected for deletion!")
                return
            }

            1 -> viewInterface.displayMessage("Movie deleted.")

            else -> viewInterface.displayMessage("Movies deleted.")
        }

        selectedMovies.forEach { dataSource.delete(it) }
    }

}