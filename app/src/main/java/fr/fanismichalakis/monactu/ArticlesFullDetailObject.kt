package fr.fanismichalakis.monactu

class ArticlesFullDetailObject {
    var title:String = ""
    var author:String = ""
    var date:String = ""
    var source:String = ""
    var description:String =""
    var imageUrl: String = ""
    var articleUrl: String =""

    constructor() {}

    constructor(title: String,
                author: String,
                date: String,
                source: String,
                description: String,
                imageURL: String,
                articleUrl: String) {
        this.title = title
        this.author = author
        this.date = date
        this.source = source
        this.description = description
        this.imageUrl = imageURL
        this.articleUrl = articleUrl
    }
}