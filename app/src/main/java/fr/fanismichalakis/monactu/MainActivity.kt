package fr.fanismichalakis.monactu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiKey = "d59958a4990048c896539cb17af6a6b7"
        val textViewSources = findViewById<TextView>(R.id.textViewSources)

        val queue = Volley.newRequestQueue(this)
        val url = "https://newsapi.org/v2/sources?apiKey=d59958a4990048c896539cb17af6a6b7&language=fr"

        // Request a string response from the provided URL.
        val stringRequest = object: JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("TAG", response.toString())
                textViewSources.text = response.toString()
            },
            { error ->
                Log.d("TAG", "Something went wrong: $error")
                textViewSources.text = "Something went wrong"})
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