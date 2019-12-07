package com.wzl.reversalchallenge;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.wzl.reversalchallenge.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;
    private MediaUtil mediaUtil;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};
    private int REQUEST_RECORD_AUDIO_PERMISSION  = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        checkPermissions();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mediaUtil = new MediaUtil(this);
        initView();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.start_record:
                Toast.makeText(this, "点击 start_record", Toast.LENGTH_SHORT).show();
                if (mediaUtil != null) {
                    mediaUtil.startRecord();
                }
                break;
            case R.id.stop_record:
                Toast.makeText(this, "点击 stop_record", Toast.LENGTH_SHORT).show();
                if (mediaUtil != null) {
                    mediaUtil.stopRecord();
                }
                break;
            case R.id.start_play:
                Toast.makeText(this, "点击 start_play", Toast.LENGTH_SHORT).show();
                if (mediaUtil != null) {
                    mediaUtil.startPlay();
                }
                break;
            case R.id.stop_play:
                Toast.makeText(this, "点击 stop_play", Toast.LENGTH_SHORT).show();
                if (mediaUtil != null) {
                    mediaUtil.stopPlay();
                }
                break;
        }
    }

    private void initView() {

        binding.startRecord.setOnClickListener(this);
        binding.stopRecord.setOnClickListener(this);
        binding.startPlay.setOnClickListener(this);
        binding.stopPlay.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaUtil.closeAll();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "权限授权成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "权限授权失败，请重新授权！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }
}
