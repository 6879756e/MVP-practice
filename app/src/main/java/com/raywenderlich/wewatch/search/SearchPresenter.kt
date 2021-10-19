package com.raywenderlich.wewatch.search

import android.util.Log

import com.raywenderlich.wewatch.model.RemoteDataSource
import com.raywenderlich.wewatch.model.TmdbResponse

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

private const val TAG = "SEARCH_PRESENTER"

class SearchPresenter(
    private val viewInterface: SearchContract.ViewInterface
) : SearchContract.PresenterInterface {

    private var dataSource = RemoteDataSource()

    private val compositeDisposable = CompositeDisposable()

    val searchResultsObservable: (String) -> Observable<TmdbResponse> =
        { query -> dataSource.searchResultsObservable(query) }

    private val observer: DisposableObserver<TmdbResponse>
        get() = object : DisposableObserver<TmdbResponse>() {
            override fun onNext(@NonNull tmdbResponse: TmdbResponse) {
                Log.d(TAG, "OnNext" + tmdbResponse.totalResults)
                if (tmdbResponse.totalResults == null || tmdbResponse.totalResults == 0) {
                    viewInterface.displayNoResults()
                    viewInterface.displayErrorMessage("Sorry, there were no results.")
                } else {
                    viewInterface.displayResults(tmdbResponse)
                }
            }

            override fun onError(@NonNull e: Throwable) {
                Log.e(TAG, "Error fetching movie data.", e)
                viewInterface.displayErrorMessage("Error fetching movie data.")
            }

            override fun onComplete() {
                Log.d(TAG, "Completed")
            }
        }

    override fun onStop() {
        compositeDisposable.clear()
    }

    override fun getSearchResults(query: String) {
        val searchResultsDisposable = searchResultsObservable(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(observer)

        compositeDisposable.add(searchResultsDisposable)
    }
}