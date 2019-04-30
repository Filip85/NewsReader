package com.filip.newsreader.Recylcer

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.filip.newsreader.DatabaseTable.Articles
import com.filip.newsreader.DatabaseTable.News
import com.filip.newsreader.MyApplication

//import com.filip.newsreader.DatabaseTable.News
import com.filip.newsreader.R
import com.filip.newsreader.SingleActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.news_look.view.*


class RecylcerAdapter(): RecyclerView.Adapter<NewsHolder>(){

    val news: MutableList<Articles> = mutableListOf()

    fun show(newsList: List<Articles>){
        news.clear()
        news.addAll(newsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val newsView = LayoutInflater.from(parent.context).inflate(R.layout.news_look, parent, false)
        val newsHolder = NewsHolder(newsView)
        return newsHolder
    }

    override fun getItemCount(): Int {
        return news.size

    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val news1 = news[position]
        holder.bind(news1)
    }

}

class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(news1: Articles) {
        itemView.newsTitle.text = news1.title//articles.title
        itemView.description.text = news1.description//articles.description

        val pictureUrl = news1.urlToImage
        val singleNewsUrl = news1.url

        Picasso.get()
                .load(pictureUrl)
                .error(android.R.drawable.stat_notify_error)
                .into(itemView.newsPicture)

        itemView.setOnClickListener{

            //Log.d("TAG", pictureUrl)

            val navigate = Intent(MyApplication.ApplicationContext, SingleActivity::class.java)
            navigate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            navigate.putExtra(SingleActivity.TITLE, news1.title)
                    .putExtra(SingleActivity.PICTURE, pictureUrl)
                    .putExtra(SingleActivity.TEXT, singleNewsUrl)

            MyApplication.ApplicationContext.startActivity(navigate)
        }
    }


}
