package net.braniumacademy.exercises515.ui.updating

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import net.braniumacademy.exercises515.Exercises515Application
import net.braniumacademy.exercises515.R
import net.braniumacademy.exercises515.data.model.Student
import net.braniumacademy.exercises515.ui.detail.SharedStudentViewModel
import net.braniumacademy.exercises515.ui.home.viewmodel.StudentViewModel
import net.braniumacademy.exercises515.ui.home.viewmodel.StudentViewModelFactory
import net.braniumacademy.exercises515.ui.updating.dialog.ConfirmDialogFragment
import net.braniumacademy.exercises515.utils.StudentUtil
import java.util.Locale

class UpdatingFragment : Fragment(), View.OnFocusChangeListener,
    CompoundButton.OnCheckedChangeListener {
    private lateinit var toolbar: Toolbar
    private lateinit var spinnerAddress: Spinner
    private lateinit var spinnerMajor: Spinner
    private lateinit var spinnerYear: Spinner
    private lateinit var edtStudentId: EditText
    private lateinit var edtFullName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtGpa: EditText
    private lateinit var edtBirthDate: EditText
    private lateinit var radioBtnMale: RadioButton
    private lateinit var radioBtnFemale: RadioButton
    private lateinit var radioBtnOther: RadioButton
    private lateinit var updatingViewModel: UpdatingViewModel
    private lateinit var sharedStudentViewModel: SharedStudentViewModel
    private lateinit var studentViewModel: StudentViewModel
    private var checkChangeCounter = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_updating, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        setupToolbar(view)
        setupSpinners(view)
        setupFocusListener()
        setupViewModels()
        showMenuItemOnToolbar(false)
    }

    private fun setupViews(view: View) {
        edtStudentId = view.findViewById(R.id.edit_student_id)
        edtStudentId.isEnabled = false
        radioBtnMale = view.findViewById(R.id.radioMale)
        radioBtnFemale = view.findViewById(R.id.radioFemale)
        radioBtnOther = view.findViewById(R.id.radioOther)
        edtBirthDate = view.findViewById(R.id.edit_birth_date)
        edtFullName = view.findViewById(R.id.edit_full_name)
        edtEmail = view.findViewById(R.id.edit_email)
        edtGpa = view.findViewById(R.id.edit_gpa)
        edtBirthDate.setOnClickListener { v: View -> showViewNormal(v) }
        edtGpa.setOnClickListener { v: View -> showViewNormal(v) }
    }

    private fun setupToolbar(view: View) {
        toolbar = view.findViewById(R.id.toolbar_editing)
        toolbar.title = getString(R.string.text_edit_student)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.inflateMenu(R.menu.menu_done)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        toolbar.setOnMenuItemClickListener {
            onMenuItemClick(it)
        }
    }

    private fun onMenuItemClick(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_done) {
            val confirmDialog = ConfirmDialogFragment(object : ActionUpdateListener() {
                override fun onConfirm() {
                    val result = updateStudent()
                    if (result) {
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                }

                override fun onCancel() {
                    closeSoftKeyboard()
                    val message = getString(R.string.msg_cancel_update)
                    Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
                }
            })
            confirmDialog.show(parentFragmentManager, null)
            return true
        }
        return false
    }

    private fun closeSoftKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun setupSpinners(view: View) {
        spinnerAddress = view.findViewById(R.id.spinner_address)
        spinnerMajor = view.findViewById(R.id.spinner_major)
        spinnerYear = view.findViewById(R.id.spinner_year)
        // tạo các adapter cho spinner
        val addressAdapter = ArrayAdapter
            .createFromResource(requireContext(), R.array.city_array, R.layout.my_spinner)
        val majorAdapter = ArrayAdapter
            .createFromResource(requireContext(), R.array.major_array, R.layout.my_spinner)
        val yearAdapter = ArrayAdapter
            .createFromResource(requireContext(), R.array.year_array, R.layout.my_spinner)
        // tạo các drop down item view cho adapter
        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // gắn adapter cho spinner
        spinnerAddress.adapter = addressAdapter
        spinnerMajor.adapter = majorAdapter
        spinnerYear.adapter = yearAdapter
    }

    private fun setupViewModels() {
        sharedStudentViewModel =
            ViewModelProvider(requireActivity())[SharedStudentViewModel::class.java]
        sharedStudentViewModel.selectedStudentLiveData.observe(viewLifecycleOwner) {
            fillData(it)
        }
        val repository =
            (requireActivity().application as Exercises515Application).repository

        studentViewModel = ViewModelProvider(
            requireActivity(),
            StudentViewModelFactory(repository)
        )[StudentViewModel::class.java]
        updatingViewModel = UpdatingViewModel(studentViewModel, sharedStudentViewModel)
    }

    private fun setupFocusListener() {
        radioBtnMale.setOnCheckedChangeListener(this)
        radioBtnFemale.setOnCheckedChangeListener(this)
        radioBtnOther.setOnCheckedChangeListener(this)

        edtBirthDate.onFocusChangeListener = this
        edtFullName.onFocusChangeListener = this
        edtEmail.onFocusChangeListener = this
        edtGpa.onFocusChangeListener = this
        edtBirthDate.onFocusChangeListener = this
        edtGpa.onFocusChangeListener = this
    }

    private fun fillData(student: Student) {
        edtStudentId.setText(student.studentId.uppercase(Locale.getDefault()))
        edtFullName.setText(student.getFullNameString())
        edtBirthDate.setText(StudentUtil.dateToString(student.birthDate))
        edtEmail.setText(student.email)
        edtGpa.setText(student.gpa.toString())
        val addPos = StudentUtil.getAdapterPosition(
            spinnerAddress.adapter, student.address
        )
        spinnerAddress.setSelection(addPos)
        val majorPos = StudentUtil.getAdapterPosition(
            spinnerMajor.adapter, student.major
        )
        spinnerMajor.setSelection(majorPos)
        spinnerYear.setSelection(student.year - 1)
        if (student.gender.lowercase(Locale.getDefault()).compareTo("nam") == 0) {
            radioBtnMale.isChecked = true
        } else if (student.gender.lowercase(Locale.getDefault()).compareTo("nữ") == 0) {
            radioBtnFemale.isChecked = true
        } else {
            radioBtnOther.isChecked = true
        }
    }

    // Lấy thông tin về giới tính từ radio button check hay uncheck
    private fun getGender(): String {
        if (radioBtnFemale.isChecked) {
            return "Nữ"
        } else if (radioBtnMale.isChecked) {
            return "Nam"
        } else if (radioBtnOther.isChecked) {
            return "Khác"
        }
        return ""
    }

    private fun updateStudent(): Boolean {
        var isDataClear = true
        val studentId = edtStudentId.text.toString()
        val fullName = edtFullName.text.toString()
        val gender = getGender()
        val email = edtEmail.text.toString()
        val gpa = edtGpa.text.toString()
        val birthDate = edtBirthDate.text.toString()
        val address = spinnerAddress.selectedItem.toString()
        val year = spinnerYear.selectedItem.toString()
        val major = spinnerMajor.selectedItem.toString()
        if (updatingViewModel.checkStringIsEmpty(fullName)) {
            showErrorHint(edtFullName, R.string.err_full_name)
            isDataClear = false
        }
        if (updatingViewModel.checkStringIsEmpty(birthDate)) {
            showErrorHint(edtBirthDate, R.string.hint_birth_date_empty)
            isDataClear = false
        } else if (!updatingViewModel.checkBirthDate(birthDate)) {
            showErrorView(edtBirthDate)
            isDataClear = false
        }
        if (updatingViewModel.checkStringIsEmpty(email)) {
            showErrorHint(edtEmail, R.string.hint_email_empty)
            isDataClear = false
        }
        if (updatingViewModel.checkStringIsEmpty(gpa)) {
            showErrorHint(edtGpa, R.string.hint_gpa_empty)
            isDataClear = false
        } else if (!updatingViewModel.checkGpa(gpa)) {
            showErrorView(edtGpa)
            isDataClear = false
        }
        if (isDataClear) {
            updatingViewModel.updateStudentInfo(
                studentId, fullName, address,
                email, major, gpa, gender, year, birthDate
            )
            return true
        }
        return false
    }

    private fun showErrorView(view: EditText?) {
        view?.setBackgroundResource(R.drawable.invalid_text)
    }

    private fun showViewNormal(view: View) {
        view.setBackgroundResource(R.drawable.normal_text)
    }

    private fun showErrorHint(view: EditText?, stringId: Int) {
        view?.hint = getString(stringId)
        view?.setHintTextColor(requireActivity().getColor(R.color.text_color_red))
    }

    private fun showMenuItemOnToolbar(isVisible: Boolean) {
        val item = toolbar.menu.findItem(R.id.action_done)
        item.isVisible = isVisible
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (hasFocus) {
            showMenuItemOnToolbar(true)
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        checkChangeCounter++
        if(checkChangeCounter > 1) {
            showMenuItemOnToolbar(true)
        }
    }
}