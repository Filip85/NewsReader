package com.filip.newsreader

import android.content.ComponentCallbacks2
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.filip.newsreader.Database.NewsDatabase
import com.filip.newsreader.DatabaseTable.Articles
import com.filip.newsreader.DatabaseTable.News
import com.filip.newsreader.DatabaseTable.Time
import com.filip.newsreader.Recylcer.RecylcerAdapter
import com.filip.newsreader.Retrofit.RetrofitFactory
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.LocalTime


class MainActivity : AppCompatActivity(), Callback<News> {

    private var code: Int = 0
    private var index: Int = 0
    private var flag: Boolean = false
    private var nisuPrviPodaci: Boolean = true

    private var min: Int = 0

    private val newsAdapter by lazy { RecylcerAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(NewsDatabase.newsDao.count() == 0){
            RetrofitFactory.showAllNews
                    .showNews("bbc-news", "top", "6946d0c07a1c4555a4186bfcade76398")
                    .enqueue(this)

            min = LocalTime.now().minute
            var time: Time = Time(1, min)
            NewsDatabase.newsDao.insertTime(time)

            nisuPrviPodaci == false

        }
        else if(NewsDatabase.newsDao.count() != 0 && nisuPrviPodaci == true){
            newsAdapter.show(NewsDatabase.newsDao.getAll())
            min = NewsDatabase.newsDao.getTime()
            Log.d("NisuPrviPodaci", "Nisu Prvi Podaci")

        }
        setUpUi()
        checkTime()
    }

    private fun setUpUi() {
        news.layoutManager = LinearLayoutManager(this)
        news.itemAnimator = DefaultItemAnimator()
        news.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))

        news.adapter = newsAdapter

    }

    private fun checkTime() {
        Log.d("TAG5", "check")
        Handler().postDelayed({
            var jeLiProsloPetMinuta = LocalTime.now().minute

            if((min - jeLiProsloPetMinuta) <=-5 || (min - jeLiProsloPetMinuta) >= 5){
                RetrofitFactory.showAllNews
                        .showNews("bbc-news", "top", "6946d0c07a1c4555a4186bfcade76398")
                        .enqueue(this)

                NewsDatabase.newsDao.updateTime(jeLiProsloPetMinuta)
                min = NewsDatabase.newsDao.getTime()

                flag = true
                Log.d("TAG1", "pokrenuo se retrofit")
            }
            checkTime()

        }, 10000)
    }

    override fun onFailure(call: Call<News>, t: Throwable) {
        Log.d("TAG", code.toString())

        dialog()
    }

    override fun onResponse(call: Call<News>, response: Response<News>) {
        loading.visibility = View.VISIBLE

        val results = response.body()

        //code = response.code()

        val articlesResults = results!!.articles

        if(NewsDatabase.newsDao.count() == 0){
            for(article in articlesResults){
                val articleRes = Articles(index, article.title, article.description, article.url, article.urlToImage)
                NewsDatabase.newsDao.insert(articleRes)
                index++
            }
            index = 0
            Log.d("TAG2", "Prvi podaci u bazi")
            newsAdapter.show(NewsDatabase.newsDao.getAll())
        }
        else if(flag == true && NewsDatabase.newsDao.count() != 0){
            for(article in articlesResults) {
                val articleRes = Articles(index, article.title, article.description, article.url, article.urlToImage)
                NewsDatabase.newsDao.deleteAllNews(articleRes)
                index++
            }
            index = 0

            for(article in articlesResults){
                val articleRes = Articles(index, article.title, article.description, article.url, article.urlToImage)
                NewsDatabase.newsDao.insert(articleRes)
                index++
            }
            index = 0
            Log.d("TAG3", "Novi podaci u bazi")
            newsAdapter.show(NewsDatabase.newsDao.getAll())
        }
        else{
            newsAdapter.show(NewsDatabase.newsDao.getAll())
        }


        loading.visibility = View.GONE
    }

    fun dialog(){
        AlertDialog.Builder(this)
                .setTitle(R.string.title)
                .setMessage(R.string.message)
                .setPositiveButton(R.string.positive_button) { dialog, which ->
                    Log.d("TAG10", "Pritisnut Uredu")
                }
                .create()
                .show()
    }
}


