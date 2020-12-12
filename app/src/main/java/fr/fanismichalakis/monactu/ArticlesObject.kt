package fr.fanismichalakis.monactu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ArticlesObject {
    var title:String = ""
    var author:String = ""

    constructor() {}

    constructor(title: String, author: String) {
        this.title = title
        this.author = author
    }
}