package net.braniumacademy.exercises515.ui.home.domain

import android.icu.text.RuleBasedCollator
import net.braniumacademy.exercises515.data.model.Student
import java.util.Locale

class SortingImpl : Sorting {
    override fun sortByNameASC(students: MutableList<Student>) {
        // sắp xếp tên tiếng việt có dấu.
        // lưu ý cho kí tự & đầu chuỗi quy tắc so sánh.
        // trong java thuần ta thay & bằng <
        val rules = "&a<á<à<ả<ã<ạ<ă<ắ<ằ<ẳ<ẵ<ặ<â<ấ<ầ<ẩ<ẫ<ậ" +
                "<b<c<d<đ<e<é<è<ẻ<ẽ<ẹ<ê<ế<ề<ể<ễ<ệ<f<g<h" +
                "<i<í<ì<ỉ<ĩ<ị<j<k<l<m<n<o<ó<ò<ỏ<õ<ọ<ô<ố<ồ<ỗ" +
                "<ộ<ơ<ớ<ờ<ở<ỡ<ợ<p<q<r<s<t" +
                "<u<ú<ù<ủ<ũ<ụ<ư<ứ<ừ<ử<ữ" +
                "<v<w<x<y<ý<ỳ<ỷ<ỹ<ỵ<z"
        try {
            val collator = RuleBasedCollator(rules)
            students.sortWith { s1, s2 ->
                val nameCompare = collator.compare(
                    s1?.fullName?.firstName?.lowercase(
                        Locale.getDefault()
                    ),
                    s2?.fullName?.firstName?.lowercase(Locale.getDefault())
                )
                if (nameCompare != 0) {
                    nameCompare
                } else {
                    collator.compare(
                        s1?.fullName?.lastName?.lowercase(Locale.getDefault()),
                        s2?.fullName?.lastName?.lowercase(Locale.getDefault())
                    )
                }
            }
        } catch (_: Exception) {
        }
    }

    override fun sortByNameDESC(students: MutableList<Student>) {
        sortByNameASC(students)
        students.reverse()
    }

    override fun sortByGpaASC(students: MutableList<Student>) {
        students.sortWith { s1: Student, s2: Student ->
            s1.gpa.compareTo(s2.gpa)
        }
    }

    override fun sortByGpaDESC(students: MutableList<Student>) {
        students.sortWith { s1: Student, s2: Student ->
            s2.gpa.compareTo(s1.gpa)
        }
    }

    override fun sortByIdASC(students: MutableList<Student>) {
        students.sortWith { s1: Student, s2: Student ->
            val s1IdStr = s1.studentId
            val s2IdStr = s2.studentId
            val s1IdNumber = s1IdStr.substring(s1IdStr.length - 3)
            val s2IdNumber = s2IdStr.substring(s2IdStr.length - 3)
            s1IdNumber.toInt() - s2IdNumber.toInt()
        }
    }

    override fun sortByIdDESC(students: MutableList<Student>) {
        sortByIdASC(students)
        students.reverse()
    }
}