package vn.edu.hust.studentman

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

class EditStudentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_student)

        val nameInput = findViewById<TextInputLayout>(R.id.name)
        val mssvInput = findViewById<TextInputLayout>(R.id.mssv)
        val btnCancel = findViewById<Button>(R.id.button_cancel)
        val btnOk = findViewById<Button>(R.id.button_ok)

        // Nhận dữ liệu từ Intent
        val studentName = intent.getStringExtra("studentName") ?: ""
        val studentId = intent.getStringExtra("studentId") ?: ""
        val position = intent.getIntExtra("position", -1)

        nameInput.editText?.setText(studentName)
        mssvInput.editText?.setText(studentId)

        btnCancel.setOnClickListener {
            finish()
        }

        btnOk.setOnClickListener {
            val updatedName = nameInput.editText?.text.toString()
            val updatedMssv = mssvInput.editText?.text.toString()


            val resultIntent = Intent()
            resultIntent.putExtra("updatedName", updatedName)
            resultIntent.putExtra("updatedMssv", updatedMssv)
            resultIntent.putExtra("position", position)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
} 