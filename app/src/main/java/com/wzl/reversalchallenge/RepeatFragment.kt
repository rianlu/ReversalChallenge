package com.wzl.reversalchallenge


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.wzl.reversalchallenge.databinding.FragmentRepeatBinding
import com.wzl.reversalchallenge.utils.MediaUtil

/**
 * A simple [Fragment] subclass.
 */
class RepeatFragment : Fragment(), View.OnClickListener {

    private lateinit var mediaUtil: MediaUtil
    private lateinit var binding: FragmentRepeatBinding
    private var isRecording: Boolean = false;

    companion object {
        private var fragment: RepeatFragment? = null
        fun newInstance() : RepeatFragment {
            if (fragment == null) {
                fragment = RepeatFragment()
            }
            return fragment as RepeatFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_repeat, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mediaUtil = MediaUtil(requireActivity())
        initView()
    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.start_stop_record -> {
                if (!isRecording) {
                    Toast.makeText(requireActivity(), "开始录音", Toast.LENGTH_SHORT).show()
                    mediaUtil.startRecord()
                    isRecording = true
                    binding.commonView.startStopRecord.background = resources.getDrawable(R.drawable.stop_record_icon, null)
                } else {
                    Toast.makeText(requireActivity(), "停止录音", Toast.LENGTH_SHORT).show()
                    mediaUtil.stopRecord()
                    isRecording = false
                    binding.commonView.startStopRecord.background = resources.getDrawable(R.drawable.start_record_icon, null)
                    binding.commonView.startStopRecord.visibility = View.GONE
                    binding.commonView.playOriginVoice.visibility = View.VISIBLE
                    binding.commonView.playReverseVoice.visibility = View.VISIBLE
                    binding.commonView.backToRecord.visibility = View.VISIBLE
                }
            }
            R.id.play_origin_voice -> {
                Toast.makeText(requireActivity(), "开始播放原音", Toast.LENGTH_SHORT).show()
                mediaUtil.playOriginVoice()
            }
            R.id.play_reverse_voice -> {
                Toast.makeText(requireActivity(), "开始播放", Toast.LENGTH_SHORT).show()
                mediaUtil.playReverseVoice()
            }
            R.id.back_to_record -> {
                binding.commonView.startStopRecord.visibility = View.VISIBLE
                binding.commonView.playOriginVoice.visibility = View.GONE
                binding.commonView.playReverseVoice.visibility = View.GONE
                binding.commonView.backToRecord.visibility = View.GONE
            }
        }
    }

    fun initView() {

        binding.commonView.startStopRecord.setOnClickListener(this)
        binding.commonView.playOriginVoice.setOnClickListener(this)
        binding.commonView.playReverseVoice.setOnClickListener(this)
        binding.commonView.backToRecord.setOnClickListener(this)
    }
}
