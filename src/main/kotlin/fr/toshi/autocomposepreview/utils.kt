package fr.toshi.autocomposepreview

fun String.capitalizeFirstLetter(): String = if (isNotEmpty()) { this[0].toUpperCase() + this.drop(1) } else { "" }