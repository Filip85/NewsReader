package com.filip.newsreader

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_single.*
import kotlinx.android.synthetic.main.news_look.*
import kotlinx.android.synthetic.main.news_look.view.*
import org.jetbrains.anko.doAsync
import org.jsoup.Jsoup

class SingleActivity : AppCompatActivity() {

    private var img: String = "null"
    private var news: String ="null"

    companion object {
        const val PICTURE = "picture"
        const val TITLE = "title"
        const val TEXT = "text"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)

        setUpUi()
    }

    private fun setUpUi() {
        img = intent.getStringExtra(PICTURE ?: "nothing")
        Picasso.get()
                .load(img)
                .error(android.R.drawable.stat_notify_error)
                .into(singleImage)

        newsTitleSingle.text = intent?.getStringExtra(TITLE ?: "nothing recieved")


        news = intent.getStringExtra(TEXT ?: "nothing")

        redText()
    }

    fun redText(){
        doAsync {
            val doc = Jsoup.connect(news).get()
            val show = doc.select("div[class=story-body__inner] p, div[id=story-body] p, div[class=vxp-media__summary] p")

            newsFromUrl.text = show.text()
        }
    }
}