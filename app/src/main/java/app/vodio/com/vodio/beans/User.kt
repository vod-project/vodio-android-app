package app.vodio.com.vodio.beans

data class User( var name : String? = "", var surname : String? ="", var login : String? ="",
                 var email : String? ="", var password : String? =""){

    fun isProvided() : Boolean {
        return !(name == null && surname == null && login == null && email == null)
    }

}