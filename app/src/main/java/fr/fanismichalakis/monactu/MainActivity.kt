package fr.fanismichalakis.monactu

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.service.autofill.Dataset
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley


class MainActivity : AppCompatActivity() {

    lateinit var viewManager: LinearLayoutManager
    lateinit var viewAdapter: CustomAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var dataset: ArrayList<ArticlesObject>





    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDataset()

        viewManager = LinearLayoutManager(this)
        viewAdapter = CustomAdapter(dataset)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewArticles).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }


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

    private fun initDataset() {
        dataset = ArrayList<ArticlesObject>()

        var article = ArticlesObject("A la Une", "Etienne Klein")
        dataset.add(article)
    }

    private fun getArticles(articleSource: String, apiKey: String) {
        val url = "https://newsapi.org/v2/everything?apiKey=$apiKey&language=fr&sources=$articleSource"
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object: JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    Log.d("TAG", response.toString())
                },
                { error ->
                    Log.d("TAG", "Something went wrong: $error")
                    //TODO("need to make show dialog a function outside of onCreate")
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
}

