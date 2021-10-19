/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.wewatch.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.raywenderlich.wewatch.R
import com.raywenderlich.wewatch.model.TmdbResponse
import com.raywenderlich.wewatch.util.display
import com.raywenderlich.wewatch.util.displayError

class SearchActivity : AppCompatActivity(), SearchContract.ViewInterface {

    private lateinit var searchPresenter: SearchPresenter

    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter
    private lateinit var noMoviesTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var query: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_movie)

        setupPresenter()
        setupViews()
    }

    private fun setupPresenter() {
        searchPresenter = SearchPresenter(this)
    }

    override fun onStart() {
        super.onStart()
        searchPresenter.getSearchResults(query)
    }

    override fun onStop() {
        super.onStop()
        searchPresenter.onStop()
    }

    private fun setupViews() {
        searchResultsRecyclerView = findViewById(R.id.search_results_recyclerview)
        adapter = SearchAdapter(arrayListOf(), this@SearchActivity, itemListener)
        searchResultsRecyclerView.adapter = adapter
        noMoviesTextView = findViewById(R.id.no_movies_textview)
        progressBar = findViewById(R.id.progress_bar)

        query = intent.getStringExtra(SEARCH_QUERY).toString()

        searchResultsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    /**
     * Listener for clicks on tasks in the ListView.
     */
    private var itemListener: RecyclerItemListener = object : RecyclerItemListener {
        override fun onItemClick(v: View, position: Int) {
            val movie = adapter.getItemAtPosition(position)

            Intent().run {
                putExtra(EXTRA_TITLE, movie.title)
                putExtra(EXTRA_RELEASE_DATE, movie.releaseDate)
                putExtra(EXTRA_POSTER_PATH, movie.posterPath)

                setResult(RESULT_OK, this)
            }

            finish()
        }
    }

    interface RecyclerItemListener {
        fun onItemClick(v: View, position: Int)
    }

    override fun displayResults(tmdbResponse: TmdbResponse) {
        progressBar.visibility = INVISIBLE

        adapter.movieList = tmdbResponse.results!!
        adapter.notifyDataSetChanged()

        searchResultsRecyclerView.visibility = VISIBLE
        noMoviesTextView.visibility = INVISIBLE
    }

    override fun displayNoResults() {
        progressBar.visibility = INVISIBLE

        searchResultsRecyclerView.visibility = INVISIBLE
        noMoviesTextView.visibility = VISIBLE
    }

    override fun displayMessage(message: String) {
        display(message)
    }

    override fun displayErrorMessage(errorMessage: String) {
        displayError(errorMessage)
    }

    companion object {
        const val SEARCH_QUERY = "searchQuery"
        const val EXTRA_TITLE = "SearchActivity.TITLE_REPLY"
        const val EXTRA_RELEASE_DATE = "SearchActivity.RELEASE_DATE_REPLY"
        const val EXTRA_POSTER_PATH = "SearchActivity.POSTER_PATH_REPLY"
    }

}

