package bajeti.susac.co.ke.utils

import java.text.ParseException
import java.text.SimpleDateFormat

class DateConverter {
    fun fromMillis(num: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy")
        val date = java.util.Date(num)
        return sdf.format(date)
    }

    fun fromMillisFor7DayOverview(num: Long): String {
        val sdf = SimpleDateFormat("dd MMM")
        val date = java.util.Date(num)
        return sdf.format(date)
    }

    fun fromMillisForEdit(num: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val date = java.util.Date(num)
        return sdf.format(date)
    }

    fun toMillis(date: String): Long{
        //String date_ = date;
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        try {
            val mDate = sdf.parse(date)
            return mDate.time
        } catch (e: ParseException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return 0
    }
}