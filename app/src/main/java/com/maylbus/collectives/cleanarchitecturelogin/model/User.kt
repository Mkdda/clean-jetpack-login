package com.maylbus.collectives.cleanarchitecturelogin.model

data class User(val name: String, val genre: Genre? = null, val age: Int? = null) {

    class Builder {

        private var name = ""
        private var genre: Genre = Genre.Builder().build()
        private var age = 0

        fun name(name: String) = this.apply { this.name = name }
        fun genre(genre: Genre) = this.apply { this.genre = genre }
        fun age(age: Int) = this.apply { this.age = age }

        fun build(): User = User(this.name, this.genre, this.age)
    }
}

data class Genre(val male: Boolean = false, val female: Boolean = false) {

    class Builder {

        private var male = true
        private var female = false

        fun setMaleGenre() = this.apply {

            this.female = false
            this.male = true
        }

        fun setFemaleGenre() = this.apply {

            this.male = false
            this.female = true
        }

        fun build(): Genre = Genre(this.male, this.female)
    }
}