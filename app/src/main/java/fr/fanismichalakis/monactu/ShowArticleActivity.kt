package fr.fanismichalakis.monactu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class ShowArticleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_article)

        findViewById<TextView>(R.id.txtTitle).text = intent.getStringExtra("title")
        findViewById<TextView>(R.id.txtAuthor).text = intent.getStringExtra("author")
        findViewById<TextView>(R.id.txtDate).text = intent.getStringExtra("date")
        findViewById<TextView>(R.id.txtSource).text = intent.getStringExtra("source")
        findViewById<TextView>(R.id.txtDescription).text = intent.getStringExtra("description")
        //findViewById<TextView>(R.id.articleThumbnail).text = intent.getStringExtra("imageUrl")
        findViewById<Button>(R.id.readArticle).text = intent.getStringExtra("articleUrl")

        val readArticleButton = findViewById<Button>(R.id.readArticle)
        readArticleButton.setOnClickListener {
            Toast.makeText(this, "Opening article", Toast.LENGTH_LONG).show()
            val webIntent = Intent(this, WebViewActivity::class.java)
            webIntent.putExtra("articleUrl", readArticleButton.text)
            startActivity(webIntent)
        }
    }
}