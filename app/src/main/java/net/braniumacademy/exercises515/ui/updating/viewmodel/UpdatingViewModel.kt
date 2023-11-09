package net.braniumacademy.exercises515.ui.updating

import net.braniumacademy.exercises515.data.model.Student
import net.braniumacademy.exercises515.ui.base.BaseViewModel
import net.braniumacademy.exercises515.ui.detail.SharedStudentViewModel
import net.braniumacademy.exercises515.ui.home.viewmodel.StudentViewModel
import net.braniumacademy.exercises515.utils.StudentUtil

class UpdatingViewModel(
    private val studentViewModel: StudentViewModel,
    private val sharedStudentViewModel: SharedStudentViewModel
) : BaseViewModel() {
    fun updateStudentInfo(
        id: String, fullName: String, address: String,
        email: String, major: String, gpa: String,
        gender: String, year: String, birthDate: String
    ) {
        val student = Student(id, address, gender, email, major)
        student.gpa = gpa.toFloat()
        student.year = year.toInt()
        student.birthDate = StudentUtil.stringToDate(birthDate)
        student.setFullName(fullName)
        studentViewModel.updateStudent(student)
        sharedStudentViewModel.updateSelectedStudent(student)
    }
}