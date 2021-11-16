package com.codelog.secury.data

class LinkModel(id:Int, title: String, url: String) {

    var id : Int
        internal set
    var title: String
        internal set
    var url: String
        internal set

    init {
        this.id = id
        this.title = title
        this.url = url
    }

}