package com.example.simplepomodoroapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val startIcon= findViewById<TextView>(R.id.pomodoro_start)
        startIcon.setOnClickListener {
            val tripTime = findViewById<TextView>(R.id.et_study).text.toString()
            val breakTime = findViewById<TextView>(R.id.et_break).text.toString()
            val roundCount = findViewById<TextView>(R.id.r_count).text.toString()
            //Toast.makeText(this,studyTime,Toast.LENGTH_SHORT).show()
            if (tripTime.isNotEmpty() && breakTime.isNotEmpty() && roundCount.isNotEmpty()) {
                val intent = Intent(this, TimerActivity::class.java)
                intent.putExtra("trip",tripTime.toInt())
                intent.putExtra("break",breakTime.toInt())
                intent.putExtra("round",roundCount.toInt())
                startActivity(intent)
            }else{
                    Toast.makeText(this,"Fill fields above",Toast.LENGTH_SHORT).show()}
    }
}}