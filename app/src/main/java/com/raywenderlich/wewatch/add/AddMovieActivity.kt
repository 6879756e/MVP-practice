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

package com.raywenderlich.wewatch.add

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView

import androidx.appcompat.app.AppCompatActivity

import com.raywenderlich.wewatch.R
import com.raywenderlich.wewatch.model.LocalDataSource
import com.raywenderlich.wewatch.model.Movie
import com.raywenderlich.wewatch.network.RetrofitClient.TMDB_IMAGEURL
import com.raywenderlich.wewatch.search.SearchActivity
import com.raywenderlich.wewatch.util.display
import com.raywenderlich.wewatch.util.displayError
import com.raywenderlich.wewatch.util.tag
import com.raywenderlich.wewatch.util.text

import com.squareup.picasso.Picasso

const val SEARCH_MOVIE_ACTIVITY_REQUEST_CODE = 2

open class AddMovieActivity : AppCompatActivity(), AddContract.ViewInterface {

    private lateinit var addPresenter: AddPresenter

    private lateinit var titleEditText: EditText
    private lateinit var releaseDateEditText: EditText
    private lateinit var movieImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie)

        setupPresenter()
        setupViews()
    }

    private fun setupPresenter() {
        addPresenter = AddPresenter(this, LocalDataSource(application))
    }

    private fun setupViews() {
        titleEditText = findViewById(R.id.movie_title)
        releaseDateEditText = findViewById(R.id.movie_release_date)
        movieImageView = findViewById(R.id.movie_imageview)
    }

    //search onClick
    fun onSearchMovieClicked(v: View) {
        addPresenter.onSearchMovieClicked(titleEditText.text())
    }

    override fun onValidSearchRequest() {
        val intent = Intent(this@AddMovieActivity, SearchActivity::class.java)
        intent.putExtra(SearchActivity.SEARCH_QUERY, titleEditText.text())
        startActivityForResult(intent, SEARCH_MOVIE_ACTIVITY_REQUEST_CODE)
    }

    //addMovie onClick
    fun onClickAddMovie(v: View) {
        addPresenter.onAddMovieClicked(
            Movie(
                titleEditText.text(),
                releaseDateEditText.text(),
                movieImageView.tag() ?: ""
            )
        )
    }

    override fun onMovieAdded() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        this@AddMovieActivity.runOnUiThread {
            titleEditText.setText(data?.getStringExtra(SearchActivity.EXTRA_TITLE))
            releaseDateEditText.setText(data?.getStringExtra(SearchActivity.EXTRA_RELEASE_DATE))
            movieImageView.tag = data?.getStringExtra(SearchActivity.EXTRA_POSTER_PATH)
            Picasso.get()
                .load(TMDB_IMAGEURL + data?.getStringExtra(SearchActivity.EXTRA_POSTER_PATH))
                .into(movieImageView)
        }
    }

    override fun displayMessage(message: String) {
        display(message)
    }

    override fun displayErrorMessage(errorMessage: String) {
        displayError(errorMessage)
    }

}