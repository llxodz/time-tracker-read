package ru.llxodz.timetracker.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_settings.*
import ru.llxodz.timetracker.R
import ru.llxodz.timetracker.viewmodel.TaskViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var mTaskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        button_delete_marks.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setPositiveButton(getString(R.string.text_yes_alert)) { _, _ ->
                mTaskViewModel.deleteAllTasks()
                Toast.makeText(this, getString(R.string.text_toast_delete_marks), Toast.LENGTH_LONG).show()
            }
            builder.setNegativeButton(getString(R.string.text_no_alert)) { _, _ ->

            }
            builder.setTitle(getString(R.string.text_title_alert))
            builder.setMessage(getString(R.string.text_message_alert))
            builder.create().show()
        }

        button_back_settings.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        button_about.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setNeutralButton("ОК") {_, _ ->

            }
            builder.setTitle(getString(R.string.text_title_about_alert))
            builder.setMessage(getString(R.string.text_message_about_alert))
            builder.create().show()
        }
    }

}