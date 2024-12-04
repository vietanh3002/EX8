package vn.edu.hust.studentman

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

class AddStudentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        val nameInput = findViewById<TextInputLayout>(R.id.name)
        val mssvInput = findViewById<TextInputLayout>(R.id.mssv)
        val btnCancel = findViewById<Button>(R.id.button_cancel)
        val btnOk = findViewById<Button>(R.id.button_ok)

        btnCancel.setOnClickListener {
            finish()
        }

        btnOk.setOnClickListener {
            val studentName = nameInput.editText?.text.toString()
            val studentId = mssvInput.editText?.text.toString()

            val resultIntent = Intent()
            resultIntent.putExtra("studentName", studentName)
            resultIntent.putExtra("studentId", studentId)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}