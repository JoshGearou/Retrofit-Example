package com.example.retrofitexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val wikiApiServe by lazy {
        WikiApiService.create()
    }
    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_search.setOnClickListener {
            if (edit_search.text.toString().isNotEmpty()) {
                beginSearch(edit_search.text.toString())
            }
        }
    }

    private fun beginSearch(srsearch: String) {
        disposable =
            wikiApiServe.hitCountCheck("query", "json", "search", srsearch)
                .subscribeOn(Schedulers.io()) //Tell it to fetch the data on background by subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()) //Display data on main thread
                .subscribe( //use subscribe to define an action on the result
                    { result -> result_count.text = "${result.query.searchinfo.totalhits} results found" },
                    { error -> Toast.makeText(this, "error!", Toast.LENGTH_SHORT).show() }
                )
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}
