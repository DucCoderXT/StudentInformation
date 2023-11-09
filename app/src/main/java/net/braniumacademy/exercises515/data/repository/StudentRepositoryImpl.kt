package net.braniumacademy.exercises515.data.repository

import net.braniumacademy.exercises515.data.model.Student
import net.braniumacademy.exercises515.data.source.DataSource

class StudentRepositoryImpl(
    private val dataSource: DataSource
) : StudentRepository {
    // trả về danh sách sinh viên đọc được
    override fun loadStudent(): List<Student> {
        return dataSource.loadData()
    }
}