package net.braniumacademy.exercises515.ui.addition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import net.braniumacademy.exercises515.R
import net.braniumacademy.exercises515.data.repository.StudentRepositoryImpl
import net.braniumacademy.exercises515.data.source.local.LocalDataSource
import net.braniumacademy.exercises515.ui.home.viewmodel.StudentViewModel
import net.braniumacademy.exercises515.ui.home.viewmodel.StudentViewModelFactory

class NewStudentFragment : Fragment() {
    private lateinit var newStudentViewModel: NewStudentViewModel
    private lateinit var studentViewModel: StudentViewModel

    private lateinit var spinnerAddress: Spinner
    private lateinit var spinnerMajor: Spinner
    private lateinit var spinnerYear: Spinner
    private lateinit var editStudentId: EditText
    private lateinit var editFullName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editGpa: EditText
    private lateinit var editBirthDate: EditText
    private lateinit var rbtnMale: RadioButton
    private lateinit var rbtnFemale: RadioButton
    private lateinit var rbtnOther: RadioButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_student, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = getString(R.string.title_new_student)
        setupViews(view)
        setupSpinners(view)
        setupViewModel()
    }

    private fun setupViews(view: View) {
        val btnCompleted = view.findViewById<Button>(R.id.btn_submit)
        btnCompleted.setOnClickListener { createStudent() }
        // liên kết các view khác vào các biến
        rbtnMale = view.findViewById(R.id.radioMale)
        rbtnFemale = view.findViewById(R.id.radioFemale)
        rbtnOther = view.findViewById(R.id.radioOther)
        editBirthDate = view.findViewById(R.id.edit_birth_date)
        editStudentId = view.findViewById(R.id.edit_student_id)
        editFullName = view.findViewById(R.id.edit_full_name)
        editEmail = view.findViewById(R.id.edit_email)
        editGpa = view.findViewById(R.id.edit_gpa)
        editBirthDate.setOnClickListener { showViewNormal(it) }
        editGpa.setOnClickListener { showViewNormal(it) }
    }

    private fun setupSpinners(view: View) {
        spinnerAddress = view.findViewById(R.id.spinner_address)
        spinnerMajor = view.findViewById(R.id.spinner_major)
        spinnerYear = view.findViewById(R.id.spinner_year)
        // tạo các adapter cho spinner
        val addressAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.city_array, android.R.layout.simple_spinner_item
        )
        val majorAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.major_array, android.R.layout.simple_spinner_item
        )
        val yearAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.year_array, android.R.layout.simple_spinner_item
        )
        // tạo các drop down item view cho adapter
        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // gắn adapter cho spinner
        spinnerAddress.adapter = addressAdapter
        spinnerMajor.adapter = majorAdapter
        spinnerYear.adapter = yearAdapter
    }

    // tạo lập đối tượng viewmodel thêm mới sinh viên
    private fun setupViewModel() {
        newStudentViewModel = ViewModelProvider(this)[NewStudentViewModel::class.java]
        val repository = StudentRepositoryImpl(LocalDataSource(requireContext()))
        studentViewModel = ViewModelProvider(
            requireActivity(),
            StudentViewModelFactory(repository)
        )[StudentViewModel::class.java]
        newStudentViewModel.studentLiveData.observe(viewLifecycleOwner) {
            studentViewModel.addStudent(it)
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private val gender: String
        get() {
            if (rbtnFemale.isChecked) {
                return "Nữ"
            } else if (rbtnMale.isChecked) {
                return "Nam"
            } else if (rbtnOther.isChecked) {
                return "Khác"
            }
            return ""
        }

    private fun createStudent() {
        var isDataClear = true
        val studentId = editStudentId.text.toString()
        val fullName = editFullName.text.toString()
        val gender = gender
        val email = editEmail.text.toString()
        val gpa = editGpa.text.toString()
        val birthDate = editBirthDate.text.toString()
        val address = spinnerAddress.selectedItem.toString()
        val year = spinnerYear.selectedItem.toString()
        val major = spinnerMajor.selectedItem.toString()
        if (newStudentViewModel.checkStringIsEmpty(studentId)) {
            showErrorHint(editStudentId, R.string.hint_id_error)
        }
        if (newStudentViewModel.checkStringIsEmpty(fullName)) {
            showErrorHint(editFullName, R.string.err_full_name)
            isDataClear = false
        }
        if (newStudentViewModel.checkStringIsEmpty(birthDate)) {
            showErrorHint(editBirthDate, R.string.hint_birth_date_empty)
            isDataClear = false
        } else if (!newStudentViewModel.checkBirthDate(birthDate)) {
            showErrorView(editBirthDate)
            isDataClear = false
        }
        if (newStudentViewModel.checkStringIsEmpty(email)) {
            showErrorHint(editEmail, R.string.hint_email_empty)
            isDataClear = false
        }
        if (newStudentViewModel.checkStringIsEmpty(gpa)) {
            showErrorHint(editGpa, R.string.hint_gpa_empty)
            isDataClear = false
        } else if (!newStudentViewModel.checkGpa(gpa)) {
            showErrorView(editGpa)
            isDataClear = false
        }
        if (!isDataClear) {
            return
        }
        if (!newStudentViewModel.checkStudentExisted(studentId, studentViewModel.student.value!!)) {
            newStudentViewModel.addNewStudent(
                studentId, fullName, address,
                email, major, gpa, gender, year, birthDate
            )
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.msg_student_existed),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showErrorView(view: EditText) {
        view.setBackgroundResource(R.drawable.invalid_text)
    }

    private fun showViewNormal(view: View) {
        view.setBackgroundResource(R.drawable.normal_text)
    }

    private fun showErrorHint(view: EditText, stringId: Int) {
        view.hint = getString(stringId)
        view.setHintTextColor(requireContext().getColor(R.color.text_color_red))
    }
}