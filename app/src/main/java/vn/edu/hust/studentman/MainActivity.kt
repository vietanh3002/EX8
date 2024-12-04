package vn.edu.hust.studentman

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var studentAdapter: StudentAdapter
    private lateinit var addStudentLauncher: ActivityResultLauncher<Intent>
    private lateinit var editStudentLauncher: ActivityResultLauncher<Intent>

    private val students = mutableListOf(
        StudentModel("Nguyễn Văn An", "SV001"),
        StudentModel("Trần Thị Bảo", "SV002"),
        StudentModel("Lê Hoàng Cường", "SV003"),
        StudentModel("Phạm Thị Dung", "SV004"),
        StudentModel("Đỗ Minh Đức", "SV005"),
        StudentModel("Vũ Thị Hoa", "SV006"),
        StudentModel("Hoàng Văn Hải", "SV007"),
        StudentModel("Bùi Thị Hạnh", "SV008"),
        StudentModel("Đinh Văn Hùng", "SV009"),
        StudentModel("Nguyễn Thị Linh", "SV010"),
        StudentModel("Phạm Văn Long", "SV011"),
        StudentModel("Trần Thị Mai", "SV012"),
        StudentModel("Lê Thị Ngọc", "SV013"),
        StudentModel("Vũ Văn Nam", "SV014"),
        StudentModel("Hoàng Thị Phương", "SV015"),
        StudentModel("Đỗ Văn Quân", "SV016"),
        StudentModel("Nguyễn Thị Thu", "SV017"),
        StudentModel("Trần Văn Tài", "SV018"),
        StudentModel("Phạm Thị Tuyết", "SV019"),
        StudentModel("Lê Văn Vũ", "SV020")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addStudentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val studentName = result.data?.getStringExtra("studentName") ?: return@registerForActivityResult
                val studentId = result.data?.getStringExtra("studentId") ?: return@registerForActivityResult

                val student = StudentModel(studentName, studentId)
                students.add(student)
                studentAdapter.notifyItemInserted(students.size - 1)
            }
        }

        editStudentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val updatedName = result.data?.getStringExtra("updatedName") ?: return@registerForActivityResult
                val updatedMssv = result.data?.getStringExtra("updatedMssv") ?: return@registerForActivityResult
                val position = result.data?.getIntExtra("position", -1) ?: return@registerForActivityResult
                students[position] = StudentModel(updatedName, updatedMssv)
                studentAdapter.notifyItemChanged(position)
            }
        }

        // Cập nhật adapter để sử dụng launcher cho chỉnh sửa
        studentAdapter = StudentAdapter(students, object : StudentAdapter.OnItemClickListener {
            override fun onEditClick(position: Int) {
                editStudent(position) // Gọi hàm chỉnh sửa với launcher
            }

            override fun onDeleteClick(position: Int) {
                deleteStudent(position)
            }
        })

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_students)
        recyclerView.adapter = studentAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btn_add_new).setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            addStudentLauncher.launch(intent)
        }

        // Đăng ký context menu cho RecyclerView
        registerForContextMenu(recyclerView)
    }

    // Xử lý menu ngữ cảnh
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val info = menuInfo as AdapterContextMenuInfo
        val position = info.position
        menu?.setHeaderTitle("Chọn hành động")
        menu?.add(0, v?.id ?: 0, 0, "Chỉnh sửa")
        menu?.add(0, v?.id ?: 0, 1, "Xóa")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterContextMenuInfo
        val position = info.position

        return when (item.itemId) {
            0 -> {
                editStudent(position) // Chỉnh sửa
                true
            }
            1 -> {
                deleteStudent(position) // Xóa
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    // Cập nhật hàm editStudent để sử dụng launcher
    private fun editStudent(position: Int) {
        if (position !in students.indices) {
            return
        }

        val student = students[position]
        val intent = Intent(this, EditStudentActivity::class.java).apply {
            putExtra("studentName", student.studentName)
            putExtra("studentId", student.studentId)
            putExtra("position", position)
        }
        editStudentLauncher.launch(intent)
    }

    companion object {
        private const val EDIT_STUDENT_REQUEST_CODE = 1
    }

    private fun deleteStudent(position: Int) {
        if (position !in students.indices) {
            return
        }

        // Lưu sinh viên bị xóa để có thể hoàn tác
        val deletedStudent = students[position]
        
        // Xóa sinh viên khỏi danh sách
        students.removeAt(position)
        studentAdapter.notifyItemRemoved(position)

        // Hiển thị Snackbar với nút hoàn tác
        Snackbar.make(
            findViewById(R.id.recycler_view_students),
            "Đã xóa sinh viên ${deletedStudent.studentName}",
            Snackbar.LENGTH_LONG
        ).setAction("Hoàn tác") {
            // Thêm lại sinh viên vào vị trí cũ
            students.add(position, deletedStudent)
            studentAdapter.notifyItemInserted(position)
        }.show()
    }
}