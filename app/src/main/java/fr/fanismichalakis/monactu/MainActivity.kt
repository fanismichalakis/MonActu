package fr.fanismichalakis.monactu

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {

    lateinit var viewManager: LinearLayoutManager
    lateinit var viewAdapter: CustomAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var dataset: ArrayList<ArticlesObject>

    var sources: JSONArray = JSONArray()
    var SOURCE: String = "google-news-fr"
    var API_KEY: String = "d59958a4990048c896539cb17af6a6b7"
    var LANG: String = "fr"





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
                    setPositiveButton(R.string.ok_button
                    ) { dialog, id ->
                    //User clicked OK button
                    }
                    setNegativeButton(R.string.cancel_button
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
        getArticles(SOURCE)
        msgShow("Source switched to ${item.title}")


        return true
    }

    // actions on click menu items
    /*override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_source1 -> {
            msgShow("Source 1")
            true
        }
        R.id.action_source2 -> {
            msgShow("Source 2")
            true
        }
        R.id.action_source3 -> {
            msgShow("Source 3")
            true
        }
        R.id.action_source4 -> {
            msgShow("Source 4")
            true
        }
        R.id.action_source5 -> {
            msgShow("Source 5")
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }*/

    private fun msgShow(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun getSources() {

        val queue = Volley.newRequestQueue(this)
        val url = "https://newsapi.org/v2/sources?apiKey=$API_KEY&language=$LANG"

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

        var article = ArticlesObject("A la Une", "Etienne Klein")
        dataset.add(article)
        article = ArticlesObject("A la deux", "Max Weber")
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
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url: String = "https://newsapi.org/v2/everything?apiKey=$API_KEY&language=$LANG&sources=$source"

        val req = object : JsonObjectRequest(url, null,
        Response.Listener{ response ->
            val strResp = response.toString()
            val jsonObj: JSONObject = JSONObject(strResp)
            Log.d("getArticles", "JSON of articles : $jsonObj")
            Log.d("getArticles", "JSON.toString() of articles : ${jsonObj.toString()}")
            val jsonArray: JSONArray = jsonObj.getJSONArray("articles")
            var strArticleTitle: String = ""
            var strArticleAuthor: String = ""
            var article = ArticlesObject(strArticleTitle,strArticleTitle)
            for (i in 0 until jsonArray.length()) {
                val jsonInner: JSONObject = jsonArray.getJSONObject(i)
                strArticleTitle = jsonInner.get("title").toString()
                Log.d("getArticles", "title: $strArticleTitle")
                strArticleAuthor = jsonInner.get("author").toString()
                Log.d("getArticles", "author; $strArticleAuthor")
                article = ArticlesObject(strArticleTitle, strArticleAuthor)
                Log.d("article", "next article: ${article.toString()}")
                dataset.add(article)
                Log.d("dataset", "updated dataset: $dataset")
            }
            Log.d("dataset", "dataset après boucle for: $dataset")
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

    private fun triggerRecyclerView() {
        viewManager = LinearLayoutManager(this)
        viewAdapter = CustomAdapter(dataset)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewArticles).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}