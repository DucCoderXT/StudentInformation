package net.braniumacademy.exercises515.ui.addition

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.braniumacademy.exercises515.data.model.Student
import net.braniumacademy.exercises515.ui.base.BaseViewModel
import net.braniumacademy.exercises515.utils.StudentUtil.stringToDate

class NewStudentViewModel : BaseViewModel() {
    private val _studentLiveData = MutableLiveData<Student>()
    val studentLiveData: LiveData<Student> = _studentLiveData

    fun addNewStudent(
        id: String, fullName: String, address: String,
        email: String, major: String, gpa: String,
        gender: String, year: String, birthDate: String
    ) {
        val student = Student(id)
        student.setFullName(fullName)
        student.address = address
        student.email = email
        student.gender = gender
        student.gpa = gpa.toFloat()
        student.major = major
        student.year = year.toInt()
        student.birthDate = stringToDate(birthDate)
        _studentLiveData.value = student
    }
}