package net.braniumacademy.exercises515.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.braniumacademy.exercises515.data.model.Student
import net.braniumacademy.exercises515.data.repository.StudentRepository

class StudentViewModel(
    private val repository: StudentRepository
) : ViewModel() {
    private val _student = MutableLiveData<List<Student>>()
    private val students: MutableList<Student> = mutableListOf()
    val student: LiveData<List<Student>> = _student

    init {
        loadData()
    }

    private fun loadData() {
        students.addAll(repository.loadStudent())
        _student.value = students
    }

    fun addStudent(student: Student) {
        students.add(student)
        _student.value = students
    }

    fun updateStudent(student: Student) {
        val index = students.indexOf(student)
        students.remove(student)
        if (index < students.size) {
            students.add(index, student)
        } else {
            students.add(student)
        }
        _student.value = students
    }

    fun deleteStudent(student: Student) {
        students.remove(student)
        _student.value = students
    }
}