package net.braniumacademy.exercises515.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import net.braniumacademy.exercises515.Exercises515Application
import net.braniumacademy.exercises515.R
import net.braniumacademy.exercises515.data.model.Student
import net.braniumacademy.exercises515.ui.addition.NewStudentFragment
import net.braniumacademy.exercises515.ui.detail.SharedStudentViewModel
import net.braniumacademy.exercises515.ui.detail.StudentDetailFragment
import net.braniumacademy.exercises515.ui.home.adapter.StudentAdapter
import net.braniumacademy.exercises515.ui.home.viewmodel.HomeDomainViewModel
import net.braniumacademy.exercises515.ui.home.viewmodel.StudentViewModel
import net.braniumacademy.exercises515.ui.home.viewmodel.StudentViewModelFactory

class HomeFragment : Fragment(), View.OnClickListener {
    private lateinit var rvStudent: RecyclerView
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var sharedStudentViewModel: SharedStudentViewModel
    private lateinit var studentViewModel: StudentViewModel
    private lateinit var homeDomainViewModel: HomeDomainViewModel
    private lateinit var toolbar: Toolbar

    override fun onStart() {
        super.onStart()
        requireActivity().title = getString(R.string.title_student_list)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        setupSearchView()
        setupRecyclerView(view)
        setupViewModel()
    }

    private fun setupViews(view: View) {
        toolbar = view.findViewById(R.id.toolbar_home)
        toolbar.inflateMenu(R.menu.menu_home)
        toolbar.title = getString(R.string.title_student_list)
        toolbar.setOnMenuItemClickListener {
            onMenuItemClick(it)
        }
        val btnNewStudent =
            view.findViewById<FloatingActionButton>(R.id.fbtn_add_new_student)
        btnNewStudent.setOnClickListener(this)
    }

    private fun setupSearchView() {
        val searchView =
            toolbar.menu.findItem(R.id.action_search)?.actionView as SearchView
        searchView.queryHint = getString(R.string.hint_search)
        // thiết lập màu văn bản và nhắc lệnh
        val editTextId = androidx.appcompat.R.id.search_src_text
        val editText = searchView.findViewById<EditText>(editTextId)
        editText.setTextColor(Color.WHITE)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    studentViewModel.student.value?.let {
                        homeDomainViewModel.searchStudentByName(it, newText)
                    }
                } else {
                    studentViewModel.student.value?.let {
                        studentAdapter.updateStudents(it)
                    }
                }
                return true
            }
        })
    }

    private fun setupRecyclerView(view: View) {
        rvStudent = view.findViewById(R.id.recycler_student)
        studentAdapter = StudentAdapter(
            mutableListOf(),
            object : StudentAdapter.OnItemClickListener {
                override fun onItemClick(student: Student) {
                    sharedStudentViewModel.updateSelectedStudent(student)
                    showDetailFragment()
                }

                override fun onMenuItemMoreClick(student: Student) {
                    showPopupOptionMenu(student)
                }
            })
        rvStudent.adapter = studentAdapter
        val itemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        rvStudent.addItemDecoration(itemDecoration)
        rvStudent.setHasFixedSize(true)
    }

    private fun showPopupOptionMenu(student: Student) {
        val popupMenu = PopupMenu(requireContext(), toolbar, Gravity.END)
        val menuInflater = popupMenu.menuInflater
        menuInflater.inflate(R.menu.menu_option, popupMenu.menu)
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener {

            false
        }
    }

    private fun setupViewModel() {
        val repository =
            (requireActivity().application as Exercises515Application).repository
        studentViewModel = ViewModelProvider(
            requireActivity(),
            StudentViewModelFactory(repository)
        )[StudentViewModel::class.java]
        sharedStudentViewModel =
            ViewModelProvider(requireActivity())[SharedStudentViewModel::class.java]
        studentViewModel.student.observe(viewLifecycleOwner) {
            studentAdapter.updateStudents(it)
        }
        homeDomainViewModel = ViewModelProvider(requireActivity())[HomeDomainViewModel::class.java]
        // theo dõi thay đổi dữ liệu khi tìm kiếm
        homeDomainViewModel.searchedStudent.observe(viewLifecycleOwner) {
            studentAdapter.updateStudents(it)
        }
        // theo dõi thay đổi dữ liệu khi sắp xếp
        homeDomainViewModel.sortedStudent.observe(viewLifecycleOwner) {
            studentAdapter.updateStudents(it)
        }
    }

    private fun showDetailFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, StudentDetailFragment::class.java, null)
            .addToBackStack(null)
            .setReorderingAllowed(true)
            .commit()
    }

    private fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_name -> {
                studentViewModel.student.value?.let {
                    homeDomainViewModel.sortStudentByName(it)
                }
                true
            }

            R.id.action_sort_gpa -> {
                studentViewModel.student.value?.let {
                    homeDomainViewModel.sortStudentByGpa(it)
                }
                true
            }

            R.id.action_sort_id -> {
                studentViewModel.student.value?.let {
                    homeDomainViewModel.sortStudentById(it)
                }
                true
            }

            R.id.action_more -> {
                showPopup()
                true
            }

            else -> false
        }
    }

    private fun showPopup() {
        val popupMenu = PopupMenu(requireContext(), toolbar, Gravity.END)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.menu_sorting, popupMenu.menu)
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener {
            onMenuItemClick(it)
        }
    }

    override fun onClick(v: View) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, NewStudentFragment::class.java, null)
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