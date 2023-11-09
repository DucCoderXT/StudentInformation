package net.braniumacademy.exercises515.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import net.braniumacademy.exercises515.Exercises515Application
import net.braniumacademy.exercises515.R
import net.braniumacademy.exercises515.data.model.Student
import net.braniumacademy.exercises515.ui.detail.dialog.DeleteDialogFragment
import net.braniumacademy.exercises515.ui.home.viewmodel.StudentViewModel
import net.braniumacademy.exercises515.ui.home.viewmodel.StudentViewModelFactory
import net.braniumacademy.exercises515.ui.updating.UpdatingFragment
import net.braniumacademy.exercises515.utils.StudentUtil

class StudentDetailFragment : Fragment() {
    private lateinit var textStudentId: TextView
    private lateinit var textFullName: TextView
    private lateinit var textAddress: TextView
    private lateinit var textEmail: TextView
    private lateinit var textMajor: TextView
    private lateinit var textBirthDate: TextView
    private lateinit var textGpa: TextView
    private lateinit var textYear: TextView
    private lateinit var textGender: TextView
    private lateinit var imageAvatar: ImageView
    private lateinit var sharedStudentViewModel: SharedStudentViewModel
    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = getString(R.string.title_detail)
        setupViews(view)
        setupToolbar(view)
        setupViewModel()
    }

    private fun setupViews(view: View) {
        imageAvatar = view.findViewById(R.id.img_avatar_detail)
        textStudentId = view.findViewById(R.id.tv_id_detail)
        textFullName = view.findViewById(R.id.tv_full_name_detail)
        textBirthDate = view.findViewById(R.id.tv_birth_date_detail)
        textAddress = view.findViewById(R.id.tv_address_detail)
        textGpa = view.findViewById(R.id.tv_gpa_detail)
        textGender = view.findViewById(R.id.tv_gender_detail)
        textEmail = view.findViewById(R.id.tv_email_detail)
        textMajor = view.findViewById(R.id.tv_major_detail)
        textYear = view.findViewById(R.id.tv_year_detail)
    }

    private fun setupToolbar(view: View) {
        toolbar = view.findViewById(R.id.toolbar_detail)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            onBackPress()
        }
        toolbar.inflateMenu(R.menu.menu_editting)
        toolbar.setOnMenuItemClickListener {
            onMenuItemClick(it)
        }
    }

    private fun setupViewModel() {
        sharedStudentViewModel =
            ViewModelProvider(requireActivity())[SharedStudentViewModel::class.java]
        sharedStudentViewModel.selectedStudentLiveData.observe(viewLifecycleOwner) {
            toolbar.title = it.studentId
            showData(it)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showData(student: Student) {
        imageAvatar.setImageDrawable(
            StudentUtil.getDrawable(requireContext(), student.gender)
        )
        textStudentId.text = StudentUtil.concatString(
            getString(R.string.title_id),
            student.studentId
        )
        textFullName.text = StudentUtil.concatString(
            getString(R.string.title_full_name),
            student.getFullNameString()
        )
        textGender.text = StudentUtil.concatString(
            getString(R.string.title_gender),
            student.gender
        )
        textAddress.text = StudentUtil.concatString(
            getString(R.string.title_address),
            student.address
        )
        textEmail.text = StudentUtil.concatString(
            getString(R.string.title_email),
            student.email
        )
        textBirthDate.text = StudentUtil.concatString(
            getString(R.string.title_birth_date),
            StudentUtil.dateToString(student.birthDate)
        )
        textGpa.text = StudentUtil.concatString(
            getString(R.string.title_gpa),
            student.gpa
        )
        textYear.text = StudentUtil.concatString(
            getString(R.string.title_year),
            student.year
        )
        textMajor.text = StudentUtil.concatString(
            getString(R.string.title_major),
            student.major
        )
    }

    private fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                openEditingFragment()
                true
            }

            R.id.action_editing_delete -> {
                val dialog = DeleteDialogFragment(object : ActionDeleteListener() {
                    override fun onConfirm() {
                        val repository =
                            (requireActivity().application as Exercises515Application).repository
                        val factory = StudentViewModelFactory(repository)
                        val studentViewModel = ViewModelProvider(
                            requireActivity(),
                            factory
                        )[StudentViewModel::class.java]
                        val student = sharedStudentViewModel.selectedStudentLiveData.value!!
                        studentViewModel.deleteStudent(student)
                        onBackPress()
                    }

                    override fun onCancel() {
                        val message = getString(R.string.msg_delete_cancel)
                        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
                    }
                })
                dialog.show(parentFragmentManager, null)
                true
            }

            else -> false
        }
    }

    private fun onBackPress() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun openEditingFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, UpdatingFragment::class.java, null)
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            .addToBackStack(null)
            .setReorderingAllowed(true)
            .commit()
    }
}