package com.farmbiz.agrivistaar.files

data class Product(
    var name: String = " ",
    var category: String = " ",
    var price: Double = 0.0,
    var quantity: Int = 0,
    var description: String = " ",
    var imageUrl: String = " ",
    var seller: String = " ",
    var location: String = " ",
    var rating: Double = 0.0,
)