package com.wzl.reversalchallenge;

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.wzl.reversalchallenge.databinding.ActivityMainBinding

public class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var binding: ActivityMainBinding? = null
    private var mediaUtil: MediaUtil? = null
    private val permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private val REQUEST_RECORD_AUDIO_PERMISSION  = 1

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main);

        checkPermissions()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mediaUtil = MediaUtil(this)
        initView()
    }

    public override fun onClick(v: View) {

        when (v.id) {
            R.id.start_record -> {
                Toast.makeText(this, "点击 start_record", Toast.LENGTH_SHORT).show()
                    mediaUtil!!.startRecord()
            }
            R.id.stop_record -> {
                Toast.makeText(this, "点击 stop_record", Toast.LENGTH_SHORT).show()
                    mediaUtil!!.stopRecord()
            }
            R.id.start_play -> {
                Toast.makeText(this, "点击 start_play", Toast.LENGTH_SHORT).show()
                    mediaUtil!!.startPlay()
            }
            R.id.stop_play -> {
                Toast.makeText(this, "点击 stop_play", Toast.LENGTH_SHORT).show()
                    mediaUtil!!.stopPlay()
            }
        }
    }

    private fun initView() {
        binding!!.startRecord.setOnClickListener(this)
        binding!!.stopRecord.setOnClickListener(this)
        binding!!.startPlay.setOnClickListener(this)
        binding!!.stopPlay.setOnClickListener(this)
    }

    protected override fun onStop() {
        super.onStop()
        mediaUtil!!.closeAll()
    }

    public override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "权限授权成功", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "权限授权失败，请重新授权！", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }
}
