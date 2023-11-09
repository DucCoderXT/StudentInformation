package net.braniumacademy.exercises515

import android.app.Application
import net.braniumacademy.exercises515.data.repository.StudentRepository
import net.braniumacademy.exercises515.data.repository.StudentRepositoryImpl
import net.braniumacademy.exercises515.data.source.local.LocalDataSource

/**
 * Lớp này chứa đối tượng repository dùng chung trong HomeFragment và UpdatingFragment.
 * Bạn có thể tạo đối tượng dùng chung qua mẫu thiết kế singleton.
 */
class Exercises515Application : Application() {
    lateinit var repository: StudentRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val localDataSource = LocalDataSource(applicationContext)
        repository = StudentRepositoryImpl(localDataSource)
    }
}