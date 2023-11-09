package net.braniumacademy.exercises515.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.SpinnerAdapter
import net.braniumacademy.exercises515.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object StudentUtil {
    const val EXTRA_STUDENT_ID = "EXTRA_STUDENT_INDEX"
    const val DATE_FORMAT = "dd/MM/yyyy"

    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat(DATE_FORMAT)

    /**
     * Phương thức trả về icon tương ứng với giới tính của sinh viên cho trước.
     * @param context context truyền vào
     * @param gender giới tính của sinh viên
     * @return Giá trị drawable tương ứng với giới tính.
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    fun getDrawable(context: Context, gender: String): Drawable? {
        return if (gender.compareTo("Nam") == 0) {
            context.resources.getDrawable(R.drawable.ic_man_24, null)
        } else {
            context.getDrawable(R.drawable.ic_woman_24)
        }
    }

    /**
     * Phương thức chuyển đổi từ string sang Date.
     * @param dateStr tham số chứa chuỗi ngày giờ cần chuyển đổi.
     * @return giá trị Date nếu chuyển đổi thành công và null nếu chuyển đổi thất bại.
     */
    fun stringToDate(dateStr: String): Date {
        return try {
            dateFormat.parse(dateStr)!!
        } catch (e: ParseException) {
            Date()
        }
    }

    /**
     * Phương thức chuyển đổi từ Date sang string tương ứng.
     * @param date giá trị date cần chuyển đổi.
     * @return String tương ứng của date cho trước theo định dạng ngày/tháng/năm.
     */
    fun dateToString(date: Date): String {
        return dateFormat.format(date)
    }

    fun concatString(str1: String, obj: Any): String {
        return "$str1 $obj"
    }

    fun getAdapterPosition(adapter: SpinnerAdapter, value: String): Int {
        for (i in 0 until adapter.count) {
            if (value.compareTo(adapter.getItem(i).toString()) == 0) {
                return i
            }
        }
        return 0
    }
}