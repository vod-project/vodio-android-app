package app.vodio.com.vodio.beans;

import java.security.Timestamp;
import java.util.List;

data class Salon(val idSalon : Integer, val users : kotlin.collections.List<String>, var salonName : String, var imagePath : String, var createTime : Timestamp)
