package com.wzl.reversalchallenge;

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil

import com.wzl.reversalchallenge.databinding.FragmentRecordBinding
import com.wzl.reversalchallenge.utils.media.AudioRecordUtil
import com.wzl.reversalchallenge.utils.media.IRecorder
import com.wzl.reversalchallenge.utils.media.MediaPlayerUtil
import com.wzl.reversalchallenge.utils.media.MediaRecorderUtil

class RecordFragment : Fragment(), View.OnClickListener {

    var recorder: IRecorder? = null
    lateinit var mediaPlayer: MediaPlayerUtil
    private var isRecording: Boolean = false
    private var FILE_PICKER_REQUEST_CODE = 2

    private val permissions: Array<String> = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var binding: FragmentRecordBinding
        private var fragment: RecordFragment? = null
        fun newInstance(): RecordFragment {
            if (fragment == null) {
                fragment = RecordFragment()
            }
            return fragment as RecordFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record, container, false)
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mediaPlayer = MediaPlayerUtil()
        initView()
    }

    @SuppressLint("ShowToast", "UseCompatLoadingForDrawables")
    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.start_stop_record -> {
                if (recorder == null) {
                    recorder = MediaRecorderUtil(requireActivity())
                }
                if (!isRecording) {
                    //防止授权失败
                    if (ContextCompat.checkSelfPermission(
                            requireActivity(),
                            permissions[0]
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        Toast.makeText(requireActivity(), "授权失败，请重新授权", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            R.string.image_hint_start_record,
                            Toast.LENGTH_SHORT
                        ).show()
                        recorder!!.startRecord()
                        isRecording = true
                        binding.commonView.startStopRecord.background =
                            resources.getDrawable(R.drawable.stop_record_icon, null)
                    }
                } else {
                    Toast.makeText(
                        requireActivity(),
                        R.string.image_hint_stop_record,
                        Toast.LENGTH_SHORT
                    ).show()
                    recorder!!.stopRecord()
                    isRecording = false
                    binding.commonView.startStopRecord.background =
                        resources.getDrawable(R.drawable.start_record_icon, null)
                    showOrHideUI()
                }
            }

            R.id.upload_record -> {
                if (isRecording)
                    Toast.makeText(requireActivity(), "正在录音中", Toast.LENGTH_SHORT).show()
                else if (ContextCompat.checkSelfPermission(
                        requireActivity(), permissions[1]
                    ) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        requireActivity(), permissions[2]
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(requireActivity(), "授权失败，请重新授权", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    //创建文件选择器 Intent
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "audio/*"
                    // 启动文件选择器
                    startActivityForResult(intent, FILE_PICKER_REQUEST_CODE)
                }
            }

            R.id.play_origin_voice -> {
                if (!mediaPlayer.checkPlaying()) {
                    if (mediaPlayer.playOrigin(recorder!!.getOriginPath()) == 1)
                        Toast.makeText(
                            requireActivity(),
                            "正在加载中，请耐心等待……",
                            Toast.LENGTH_SHORT
                        ).show()
                    else
                        Toast.makeText(
                            requireActivity(),
                            R.string.image_hint_play_origin_voice,
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }

            R.id.play_reverse_voice -> {
                if (!mediaPlayer.checkPlaying()) {
                    if (mediaPlayer.playReverse(recorder!!.getReversePath()) == 1)
                        Toast.makeText(
                            requireActivity(),
                            "正在加载中，请耐心等待……",
                            Toast.LENGTH_SHORT
                        ).show()
                    else
                        Toast.makeText(
                            requireActivity(),
                            R.string.image_hint_play_voice,
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }

            R.id.back_to_record -> {
                showOrHideUI()
                mediaPlayer.stopPlay()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedFileUri = data?.data ?: return

            Log.e("AudioPlay", "1")
            //处理uri，即复制文件然后音频反向
            if (recorder == null)
                recorder = MediaRecorderUtil(requireActivity())
            recorder!!.startCopy(requireActivity(), selectedFileUri)

            Log.e("AudioPlay", "2")

            //修改ui界面
            showOrHideUI()
            Log.e("AudioPlay", "3")
        }
    }

    private fun initView() {

        binding.commonView.startStopRecord.setOnClickListener(this)
        binding.commonView.uploadRecord.setOnClickListener(this)
        binding.commonView.playOriginVoice.setOnClickListener(this)
        binding.commonView.playReverseVoice.setOnClickListener(this)
        binding.commonView.backToRecord.setOnClickListener(this)
    }

    private fun showOrHideUI() {
        if (binding.commonView.startStopRecord.visibility == View.VISIBLE) {
            binding.commonView.startStopRecord.visibility = View.GONE
        } else {
            binding.commonView.startStopRecord.visibility = View.VISIBLE
        }
        if (binding.commonView.uploadRecord.visibility == View.VISIBLE) {
            binding.commonView.uploadRecord.visibility = View.GONE
        } else {
            binding.commonView.uploadRecord.visibility = View.VISIBLE
        }
        if (binding.commonView.playOriginVoice.visibility == View.VISIBLE) {
            binding.commonView.playOriginVoice.visibility = View.GONE
        } else {
            binding.commonView.playOriginVoice.visibility = View.VISIBLE
        }
        if (binding.commonView.playReverseVoice.visibility == View.VISIBLE) {
            binding.commonView.playReverseVoice.visibility = View.GONE
        } else {
            binding.commonView.playReverseVoice.visibility = View.VISIBLE
        }
        if (binding.commonView.backToRecord.visibility == View.VISIBLE) {
            binding.commonView.backToRecord.visibility = View.GONE
        } else {
            binding.commonView.backToRecord.visibility = View.VISIBLE
        }
    }
}