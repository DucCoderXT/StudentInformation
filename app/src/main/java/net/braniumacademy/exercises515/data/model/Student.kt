package net.braniumacademy.exercises515.data.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import net.braniumacademy.exercises515.utils.StudentUtil
import java.util.Date
import java.util.Objects

data class Student(
    @JsonProperty("id") var studentId: String = "",
    @JsonProperty("address") var address: String = "",
    @JsonProperty("gender") var gender: String = "",
    @JsonProperty("email") var email: String = "",
    @JsonProperty("major") var major: String = "",
    @JsonProperty("gpa") var gpa: Float = 0f,
    @JsonProperty("year") var year: Int = 0,
    @JsonProperty("birth_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = StudentUtil.DATE_FORMAT)
    var birthDate: Date = Date()
) {
    lateinit var fullName: FullName
        private set

    @Suppress("unused")
    @JsonProperty("full_name")
    private fun unpackFullName(fullName: Map<String, Any>) {
        this.fullName = FullName(
            Objects.requireNonNull(fullName["first"]).toString(),
            Objects.requireNonNull(fullName["last"]).toString(),
            Objects.requireNonNull(fullName["midd"]).toString()
        )
    }

    fun getFullNameString(): String {
        return (fullName.lastName + " " + fullName.midName
                + " " + fullName.firstName).trim()
    }

    fun setFullName(fullName: String) {
        val data = fullName.trim().split("\\s+")
        var firstName = ""
        var lastName = ""
        var midName = ""
        when (data.size) {
            1 -> firstName = data[0]

            2 -> {
                firstName = data[1]
                lastName = data[0]
            }

            3 -> {
                lastName = data[0]
                firstName = data[data.size - 1]
                for (i in 1 until data.size) {
                    midName += data[i] + " "
                }
            }
        }
        this.fullName = FullName(firstName, lastName, midName)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val student = other as Student
        return studentId == student.studentId
    }

    override fun hashCode(): Int {
        return Objects.hash(studentId)
    }

    data class FullName(
        @JsonProperty("first") var firstName: String = "",
        @JsonProperty("last") var lastName: String = "",
        @JsonProperty("mid") var midName: String = ""
    )
}