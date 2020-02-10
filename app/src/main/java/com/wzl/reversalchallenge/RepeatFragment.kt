package com.wzl.reversalchallenge


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.wzl.reversalchallenge.databinding.FragmentRepeatBinding
import com.wzl.reversalchallenge.utils.media.AudioRecordUtil
import com.wzl.reversalchallenge.utils.media.IRecorder
import com.wzl.reversalchallenge.utils.media.MediaPlayerUtil

/**
 * A simple [Fragment] subclass.
 */
class RepeatFragment : Fragment(), View.OnClickListener {

    public lateinit var recorder: IRecorder
    public lateinit var mediaPlayer: MediaPlayerUtil
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
        recorder = AudioRecordUtil(requireActivity())
        mediaPlayer = MediaPlayerUtil()
        initView()
    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.start_stop_record -> {
                if (!isRecording) {
                    Toast.makeText(requireActivity(), R.string.image_hint_start_record, Toast.LENGTH_SHORT).show()
                    recorder.startRecord()
                    isRecording = true
                    binding.commonView.startStopRecord.background = resources.getDrawable(R.drawable.stop_record_icon, null)
                } else {
                    Toast.makeText(requireActivity(), R.string.image_hint_stop_record, Toast.LENGTH_SHORT).show()
                    recorder.stopRecord()
                    isRecording = false
                    binding.commonView.startStopRecord.background = resources.getDrawable(R.drawable.start_record_icon, null)
                    showOrHideUI()
                }
            }
            R.id.play_origin_voice -> {
                if (!mediaPlayer.checkPlaying()) {
                    Toast.makeText(requireActivity(), R.string.image_hint_play_origin_voice, Toast.LENGTH_SHORT).show()
                    mediaPlayer.playOrigin(recorder.getOriginPath())
                }
            }
            R.id.play_reverse_voice -> {
                if (!mediaPlayer.checkPlaying()) {
                    Toast.makeText(requireActivity(), R.string.image_hint_play_voice, Toast.LENGTH_SHORT).show()
                    mediaPlayer.playReverse(recorder.getReversePath())
                }
            }
            R.id.back_to_record -> {
                showOrHideUI()
                mediaPlayer.stopPlay()
            }
        }
    }

    private fun initView() {

        binding.commonView.startStopRecord.setOnClickListener(this)
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
