package net.braniumacademy.exercises515.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.braniumacademy.exercises515.data.model.Student
import net.braniumacademy.exercises515.ui.home.domain.FilterImpl
import net.braniumacademy.exercises515.ui.home.domain.SortingImpl

class HomeDomainViewModel : ViewModel() {
    private var sortIdCounter = 0
    private var sortNameCounter = 0
    private var sortGpaCounter = 0

    private val filter = FilterImpl()
    private val sorting = SortingImpl()

    private val _searchedStudent = MutableLiveData<List<Student>>()
    val searchedStudent: LiveData<List<Student>> = _searchedStudent

    private val _sortedStudent = MutableLiveData<List<Student>>()
    val sortedStudent: LiveData<List<Student>> = _sortedStudent

    fun searchStudentByName(students: List<Student>, name: String) {
        val result = filter.findByName(students, name.trim())
        _searchedStudent.value = result
    }

    fun sortStudentById(students: List<Student>) {
        val mutableList = students.toMutableList()
        if (sortIdCounter % 2 == 0) {
            sorting.sortByIdASC(mutableList)
        } else {
            sorting.sortByIdDESC(mutableList)
        }
        _sortedStudent.value = mutableList
        sortIdCounter++
    }

    fun sortStudentByName(students: List<Student>) {
        val mutableList = students.toMutableList()
        if (sortNameCounter % 2 == 0) {
            sorting.sortByNameASC(mutableList)
        } else {
            sorting.sortByNameDESC(mutableList)
        }
        _sortedStudent.value = mutableList
        sortNameCounter++
    }

    fun sortStudentByGpa(students: List<Student>) {
        val mutableList = students.toMutableList()
        if (sortGpaCounter % 2 == 0) {
            sorting.sortByGpaASC(mutableList)
        } else {
            sorting.sortByGpaDESC(mutableList)
        }
        _sortedStudent.value = mutableList
        sortGpaCounter++
    }
}