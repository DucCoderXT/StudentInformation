package net.braniumacademy.exercises515.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.braniumacademy.exercises515.data.model.Student

class SharedStudentViewModel : ViewModel() {
    private val _selectedStudentLiveData = MutableLiveData<Student>()
    val selectedStudentLiveData: LiveData<Student> = _selectedStudentLiveData

    fun updateSelectedStudent(student: Student) {
        _selectedStudentLiveData.value = student
    }
}