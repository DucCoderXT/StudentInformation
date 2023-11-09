package net.braniumacademy.exercises515.data.source.local

import android.content.Context
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import net.braniumacademy.exercises515.R
import net.braniumacademy.exercises515.data.model.Student
import net.braniumacademy.exercises515.data.source.DataSource
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader
import java.io.StringWriter
import java.io.Writer
import java.nio.charset.StandardCharsets

class LocalDataSource(private val context: Context) : DataSource {
    /**
     * Phương thức trả về list các student từ dữ liệu string JSON.
     *
     * @return một List các Student. Trả về danh sách rỗng nếu chuyển đổi thất bại.
     */
    override fun loadData(): List<Student> {
        val mapper = ObjectMapper()
        val jsonString = loadJsonString()
        return try {
            mapper.readValue(
                jsonString,
                object : TypeReference<List<Student>>() {})
        } catch (_: JsonProcessingException) {
            listOf()
        }
    }

    /**
     * Phương thức đọc dữ liệu từ file json và trả về dữ liệu trong file vừa đọc
     * dưới dạng String.
     *
     * @return String chứa dữ liệu của file json cần đọc.
     */
    private fun loadJsonString(): String {
        val writer: Writer = StringWriter()
        val buffer = CharArray(1024)
        try {
            context.resources.openRawResource(R.raw.student).use { inputStream ->
                val reader: Reader = BufferedReader(
                    InputStreamReader(inputStream, StandardCharsets.UTF_8)
                )
                var size: Int
                while (reader.read(buffer).also { size = it } != -1) {
                    writer.write(buffer, 0, size)
                }
            }
        } catch (_: IOException) {
        }
        return writer.toString()
    }
}