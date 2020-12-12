package fr.fanismichalakis.monactu

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
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





    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initDataset()
        getArticles()
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


        val apiKey = "d59958a4990048c896539cb17af6a6b7"
        val textViewSources = findViewById<TextView>(R.id.textViewSources)

        val queue = Volley.newRequestQueue(this)
        val url = "https://newsapi.org/v2/sources?apiKey=$apiKey&language=fr"

        // Request a string response from the provided URL.
        val stringRequest = object: JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    Log.d("TAG", response.toString())
                    textViewSources.text = response.toString()
                },
                { error ->
                    Log.d("TAG", "Something went wrong: $error")
                    dialog?.show()
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

    private fun getArticles() {
        dataset = ArrayList<ArticlesObject>()
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url: String = "https://newsapi.org/v2/everything?apiKey=d59958a4990048c896539cb17af6a6b7&language=fr&sources=google-news-fr"

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