package net.braniumacademy.exercises515.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.braniumacademy.exercises515.R
import net.braniumacademy.exercises515.data.model.Student
import net.braniumacademy.exercises515.utils.StudentUtil.getDrawable

class StudentAdapter(
    private val students: MutableList<Student>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return ViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = students[position]
        holder.bind(student, listener)
    }

    override fun getItemCount(): Int {
        return students.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateStudents(students: List<Student>) {
        this.students.clear()
        this.students.addAll(students)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageAvatar: ImageView = itemView.findViewById(R.id.img_avatar)
        private val tvStudentId: TextView = itemView.findViewById(R.id.tv_student_id)
        private val tvFullName: TextView = itemView.findViewById(R.id.tv_full_name)
        private val tvGpa: TextView = itemView.findViewById(R.id.tv_gpa)
        private val imageButtonMore = itemView.findViewById<ImageButton>(R.id.img_btn_more)

        fun bind(student: Student, listener: OnItemClickListener) {
            tvStudentId.text = student.studentId
            tvGpa.text = student.gpa.toString()
            tvFullName.text = student.getFullNameString()
            imageAvatar.setImageDrawable(getDrawable(itemView.context, student.gender))
            itemView.setOnClickListener {
                listener.onItemClick(student)
            }
            imageButtonMore.setOnClickListener {
                listener.onMenuItemMoreClick(student)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(student: Student)
        fun onMenuItemMoreClick(student: Student)
    }
}