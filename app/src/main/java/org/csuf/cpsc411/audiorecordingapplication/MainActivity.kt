package org.csuf.cpsc411.audiorecordingapplication

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var result: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false
    private var recStopped: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mediaRecorder = MediaRecorder()
        result = Environment.getExternalStorageDirectory().absolutePath + "/myRec.mp3"

        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setOutputFile(output)

        fun checkPermissions() {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO), 0)
            }
        }

        fun startRecording() {
            try {
                //viewModel?.startRecording()
                mediaRecorder?.prepare()
                mediaRecorder?.start()
                state = true
                Toast.makeText(this, "Recording . . .", Toast.LENGTH_SHORT).show()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun stopRecording() {
            if(state){
                //viewModel?.stopRecording()
                mediaRecorder?.stop()
                mediaRecorder?.release()
                state = false
                Toast.makeText(this, "Recording Stopped", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "You are not recording right now!", Toast.LENGTH_SHORT).show()
            }
        }

        button_Record.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this, permissions,0)
            } else {
                startRecording()
            }
        }

        button_Stop.setOnClickListener{
            stopRecording()
        }
    }
}