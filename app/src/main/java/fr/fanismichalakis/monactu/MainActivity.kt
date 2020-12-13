package fr.fanismichalakis.monactu

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity(), CustomAdapter.OnArticleListener {

    lateinit var viewManager: LinearLayoutManager
    lateinit var viewAdapter: CustomAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var dataset: ArrayList<ArticlesObject>
    lateinit var datasetFull: ArrayList<ArticlesFullDetailObject>

    var sources: JSONArray = JSONArray()
    var BASE_URL: String = "https://newsapi.org/v2"
    var SOURCE: String = "google-news-fr"
    var API_KEY: String = "d59958a4990048c896539cb17af6a6b7"
    var LANG: String = "fr"
    var CURRENT_PAGE: Int = 1





    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getSources()
        Log.d("MainActivity", "sources: $sources")

        //initDataset()
        getArticles(SOURCE)
        //initMockDataset()

        /*viewManager = LinearLayoutManager(this)
        viewAdapter = CustomAdapter(dataset)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewArticles).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }*/


        val builder: AlertDialog.Builder? = MainActivity@ this?.let {
            AlertDialog.Builder(it)
        }
        builder?.setMessage(R.string.no_connection_message)
                ?.setTitle(R.string.no_connection_dialog_title)
                ?.apply {
                    setPositiveButton(
                        R.string.ok_button
                    ) { dialog, id ->
                    //User clicked OK button
                    }
                    setNegativeButton(
                        R.string.cancel_button
                    ) { dialog, id ->
                    //User cancelled the dialog
                    }
                }
        val dialog: AlertDialog? = builder?.create()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        Log.d("onPrepareOptionsMenu", "sources: ${sources.toString()}")
        super.onPrepareOptionsMenu(menu)
        if (sources.length() > 0) {
            menu?.clear()
        }
        for (index in 0 until sources.length()) {
            menu?.add(0, index, index, sources.getJSONObject(index).getString("name"))
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val sourceId = sources.getJSONObject(item.itemId).getString("id")
        SOURCE = sourceId
        CURRENT_PAGE = 1
        getArticles(SOURCE)
        msgShow("Source switched to ${item.title}")


        return true
    }

    override fun onArticleClick(position: Int) {
        Log.d("onArticleClick", "datasetFull: $datasetFull")
        val article = datasetFull[position]
        Log.d("onArticleClick", "selected article: $article")
        val articleIntent = Intent(this, ShowArticleActivity::class.java)
        articleIntent.putExtra("title", article.title)
        articleIntent.putExtra("author", article.author)
        articleIntent.putExtra("date", article.date)
        articleIntent.putExtra("source", article.source)
        articleIntent.putExtra("description", article.description)
        articleIntent.putExtra("imageUrl", article.imageUrl)
        articleIntent.putExtra("articleUrl", article.articleUrl)
        startActivity(articleIntent)
    }


    private fun msgShow(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun getSources() {

        val queue = Volley.newRequestQueue(this)
        val url = "$BASE_URL/sources?apiKey=$API_KEY&language=$LANG"

        // Request a string response from the provided URL.
        val stringRequest = object: JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                sources = response.getJSONArray("sources")
            },
            { error ->
                Log.d("TAG", "Something went wrong: $error")
                //dialog?.show()
            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }

        queue.add(stringRequest)
    }


    private fun initMockDataset() {
        dataset = ArrayList<ArticlesObject>()

        var article = ArticlesObject("A la Une", "Etienne Klein", "2020-12-09T05:00:00Z")
        dataset.add(article)
        article = ArticlesObject("A la deux", "Max Weber", "2020-12-09T05:00:00Z")
        dataset.add(article)
    }

    /*private fun initDataset() {
        dataset = ArrayList<ArticlesObject>()

        // add articles to dataset
        dataset = getArticles(dataset)
        Log.d("initDataset", "dataset = ${dataset.toString()}")
    }*/

    private fun getArticles(source: String) {
        dataset = ArrayList<ArticlesObject>()
        datasetFull = ArrayList<ArticlesFullDetailObject>()
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url: String = "$BASE_URL/everything?apiKey=$API_KEY&language=$LANG&sources=$source&page=$CURRENT_PAGE"

        val req = object : JsonObjectRequest(url, null,
            Response.Listener { response ->
                val strResp = response.toString()
                val jsonObj: JSONObject = JSONObject(strResp)
                Log.d("getArticles", "JSON of articles : $jsonObj")
                Log.d("getArticles", "JSON.toString() of articles : ${jsonObj.toString()}")
                val jsonArray: JSONArray = jsonObj.getJSONArray("articles")
                var strArticleTitle: String = ""
                var strArticleAuthor: String = ""
                var strArticleDate: String = ""
                var strArticleSource: String = ""
                var strArticleDescription: String = ""
                var strArticleImageUrl: String = ""
                var strArticleUrl: String = ""
                var article = ArticlesObject(strArticleTitle, strArticleTitle, strArticleDate)
                var articleFull = ArticlesFullDetailObject()
                for (i in 0 until jsonArray.length()) {
                    val jsonInner: JSONObject = jsonArray.getJSONObject(i)
                    strArticleTitle = jsonInner.get("title").toString()
                    strArticleAuthor = jsonInner.get("author").toString()
                    strArticleDate = jsonInner.get("publishedAt").toString()
                    article = ArticlesObject(strArticleTitle, strArticleAuthor, strArticleDate)
                    dataset.add(article)
                    Log.d("dataset", "updated dataset: $dataset")
                    val jsonInnerSource: JSONObject = jsonInner.getJSONObject("source")
                    strArticleSource = jsonInnerSource.get("name").toString()
                    Log.d("full article", "source: $strArticleSource")
                    strArticleDescription = jsonInner.get("description").toString()
                    strArticleImageUrl = jsonInner.get("urlToImage").toString()
                    strArticleUrl = jsonInner.get("url").toString()
                    Log.d("full article", strArticleUrl)
                    articleFull = ArticlesFullDetailObject(
                        strArticleTitle,
                        strArticleAuthor,
                        strArticleDate,
                        strArticleSource,
                        strArticleDescription,
                        strArticleImageUrl,
                        strArticleUrl
                    )
                    datasetFull.add(articleFull)
                    Log.d("datasetFull", "updated datasetFull: $datasetFull")

                }
                Log.d("dataset", "dataset après boucle for: $dataset")
                Log.d("datasetFull", "datasetFull après boucle for: $datasetFull")
                triggerRecyclerView()
            },
            Response.ErrorListener { error ->
                Log.e("getArticles", "Something went wrong $error")
            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }
        Log.d("dataset", "dataset in getArticles : $dataset")
        queue.add(req)

    }

    fun showArticle(v: View) {
        msgShow("Article clicked!")
        val monIntent = Intent(this, ShowArticleActivity::class.java)
        startActivity(monIntent)
    }

    private fun triggerRecyclerView() {
        viewManager = LinearLayoutManager(this)
        viewAdapter = CustomAdapter(dataset, this)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewArticles).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    //msgShow("Reached bottom")
                    CURRENT_PAGE++
                    getArticles(SOURCE)
                }
            }
        })
    }
}