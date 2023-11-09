package net.braniumacademy.exercises515.data.source

import net.braniumacademy.exercises515.data.model.Student

interface DataSource {
    fun loadData(): List<Student>
}