package com.example.simplepomodoroapp

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

class TimerActivity : AppCompatActivity() {

    private var studyMinute: Int? = null
    private var breakMinute: Int? = null
    private var roundCount: Int? = null

    private var restTimer: CountDownTimer? = null
    private var studyTimer: CountDownTimer? = null
    private var breakTimer: CountDownTimer? = null

    private lateinit var progressBar: ProgressBar

    private var mRound = 1

    private var isStudy = true

    private var isStop = false

    private lateinit var ivstop: ImageView

    private var mp: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        // Receive Extras
        studyMinute = intent.getIntExtra("trip", 0) * 60 * 1000
        breakMinute = intent.getIntExtra("break", 0) * 60 * 1000
        roundCount = intent.getIntExtra("round", 0)
        progressBar = findViewById(R.id.progress_bar)
        ivstop = findViewById(R.id.iv_stop)
        // Set Rounds Text
        findViewById<TextView>(R.id.tv_round).text = "$mRound/$roundCount"
        //Start Timer
        setRestTimer()
        // Reset Button
        ivstop.setOnClickListener {
            resetOrStart()
        }
    }

    // Set Rest Timer
    private fun setRestTimer(){
        playSound()
        findViewById<TextView>(R.id.tv_status).text = "Get ready"
        progressBar.progress = 0
        progressBar.max = 10025
        restTimer = object : CountDownTimer(10500,1000) {
            override fun onTick(p0: Long) {
                progressBar.progress = (p0 / 1000).toInt()
                findViewById<TextView>(R.id.tv_timer).text = (p0 / 1000).toString()
            }
            override fun onFinish() {
                mp?.reset()
                if (isStudy){
                    setupStudyView()
                }else{
                    setupBreakView()
                }
            }
        }.start()
    }

    // Set Study Timer
    private fun setStudyTimer(){

        studyTimer = object : CountDownTimer(studyMinute!!.toLong() + 500,1000) {
            override fun onTick(p0: Long) {
                progressBar.progress = (p0 /1000).toInt()
                findViewById<TextView>(R.id.tv_timer).text = createTimeLabels((p0 / 1000).toInt())
            }
            override fun onFinish() {
                if(mRound < roundCount!!){
                    isStudy = false
                    setRestTimer()
                    mRound++
                }else{
                    clearAttribute()
                    findViewById<TextView>(R.id.tv_status).text = "You have finish your rounds :)"
                }
            }
        }.start()
    }

    // Set Break Timer
    private fun setBreakTimer() {
        breakTimer = object : CountDownTimer(breakMinute!!.toLong()+500, 1000 ) {
            override fun onTick(p0: Long) {
                progressBar.progress = (p0 / 1000).toInt()
                findViewById<TextView>(R.id.tv_timer).text = createTimeLabels((p0 / 1000).toInt())
            }

            override fun onFinish() {
                isStudy = true
                setRestTimer()
            }

        }.start()
    }

    // Prepare Screen for Study Timer
    private fun setupStudyView() {
        findViewById<TextView>(R.id.tv_round).text = "$mRound/$roundCount"
        findViewById<TextView>(R.id.tv_status).text = "Focus Time"
        progressBar.max = studyMinute!!/1000

        if (studyTimer != null)
            studyTimer = null

        setStudyTimer()
    }

    // Prepare Screen for Break Timer
    private fun setupBreakView() {
        findViewById<TextView>(R.id.tv_status).text = "Break Time"
        progressBar.max = breakMinute!!/1000

        if (breakTimer != null)
            breakTimer = null

        setBreakTimer()
    }

    // Initialize sound file to MediaPlayer
    private fun playSound() {

        try {
            val soundUrl = Uri.parse("android.resource://com.exo.pomodoro/" + R.raw.count_down)
            mp = MediaPlayer.create(this,soundUrl)
            mp?.isLooping = false
            mp?.start()
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    // Rest Whole Attributes in FeedActivity
    private fun clearAttribute() {
        findViewById<TextView>(R.id.tv_status).text = "Press Play Button to Restart"
        ivstop.setImageResource(R.drawable.ic_play)
        progressBar.progress = 0
        findViewById<TextView>(R.id.tv_timer).text = "0"
        mRound = 1
        findViewById<TextView>(R.id.tv_round).text = "$mRound/$roundCount"
        restTimer?.cancel()
        studyTimer?.cancel()
        breakTimer?.cancel()
        mp?.reset()
        isStop = true
    }

    // Convert Received Numbers to Minutes and Seconds
    private fun createTimeLabels(time : Int): String {
        var timeLabel = ""
        val minutes = time / 60
        val secends = time % 60

        if (minutes < 10) timeLabel += "0"
        timeLabel += "$minutes:"

        if (secends < 10) timeLabel += "0"
        timeLabel += secends

        return timeLabel
    }

    // For Reset or Restart Pomodoro
    private fun resetOrStart() {
        if (isStop){
            ivstop.setImageResource(R.drawable.ic_stop)
            setRestTimer()
            isStop = false
        }else
            clearAttribute()
    }

    // Clear Everything When App Destroyed
    override fun onDestroy() {
        super.onDestroy()
        restTimer?.cancel()
        studyTimer?.cancel()
        breakTimer?.cancel()
        mp?.reset()
    }
}