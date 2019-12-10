package com.wzl.reversalchallenge


import android.app.AlertDialog
import android.content.DialogInterface
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.wzl.reversalchallenge.databinding.FragmentRecordBinding
import com.wzl.reversalchallenge.utils.MediaUtil

/**
 * A simple [Fragment] subclass.
 */
class RecordFragment : Fragment(), View.OnClickListener {

    private lateinit var mediaUtil: MediaUtil
    private var isRecording: Boolean = false
    private var clickCount: Int = 0
    private var smallNum: Int = 0
    private var imageClickTime: Int = 0

    companion object {
        private lateinit var binding: FragmentRecordBinding
        private var fragment: RecordFragment? = null
        fun newInstance(): RecordFragment {
            if (fragment == null) {
                fragment = RecordFragment()
            }
            return fragment as RecordFragment
        }

        class HintAsyncTask : AsyncTask<Void, Void, Void>() {

            override fun doInBackground(vararg params: Void?): Void? {
                return null
            }

            override fun onPostExecute(result: Void?) {
                binding.commonView.textHintMessage.visibility = View.VISIBLE
                binding.commonView.imageView.visibility = View.GONE
                binding.commonView.textHintNum.visibility = View.GONE
            }

        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_record, container, false)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record, container, false)
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
                    Toast.makeText(requireActivity(), R.string.image_hint_start_record, Toast.LENGTH_SHORT).show()
                    mediaUtil.startRecord()
                    isRecording = true
                    binding.commonView.startStopRecord.background = resources.getDrawable(R.drawable.stop_record_icon, null)
                } else {
                    Toast.makeText(requireActivity(), R.string.image_hint_stop_record, Toast.LENGTH_SHORT).show()
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
                Toast.makeText(requireActivity(), R.string.image_hint_play_origin_voice, Toast.LENGTH_SHORT).show()
                mediaUtil.playOriginVoice()
            }
            R.id.play_reverse_voice -> {
                Toast.makeText(requireActivity(), R.string.image_hint_play_voice, Toast.LENGTH_SHORT).show()
                mediaUtil.playReverseVoice()
            }
            R.id.back_to_record -> {
                binding.commonView.startStopRecord.visibility = View.VISIBLE
                binding.commonView.playOriginVoice.visibility = View.GONE
                binding.commonView.playReverseVoice.visibility = View.GONE
                binding.commonView.backToRecord.visibility = View.GONE
            }
            R.id.text_hint_message -> {
                binding.commonView.textHintMessage.text = resources.getString(R.string.text_hint_message_clicked)
                clickCount = 0
                smallNum = 0
                imageClickTime = 0
                binding.commonView.textHintNum.visibility = View.VISIBLE
            }
            R.id.text_hint_num -> {
                if (clickCount < 9) {
                    clickCount ++
                    binding.commonView.textHintNum.text = clickCount.toString()
                } else if (smallNum < 9) {
                    smallNum ++
                    val text = clickCount.toString() + ".$smallNum"
                    binding.commonView.textHintNum.text = text
                } else {
                    Toast.makeText(requireActivity(), resources.getString(R.string.text_hint_message_error), Toast.LENGTH_SHORT).show()
                    binding.commonView.imageView.visibility = View.VISIBLE
                    binding.commonView.textHintMessage.visibility = View.GONE
                    binding.commonView.textHintNum.visibility = View.GONE
                }
            }
            R.id.imageView -> {
                if (imageClickTime < 5) {
                    imageClickTime++
                } else {
                    val dialog: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
                    dialog.setTitle("提示")
                    dialog.setMessage("别点了，下次，下次一定支持(没错我才是彩蛋)")
                    dialog.setPositiveButton("好的", null)
                    dialog.create().show()
                }
            }
        }
    }

    fun initView() {

        binding.commonView.startStopRecord.setOnClickListener(this)
        binding.commonView.playOriginVoice.setOnClickListener(this)
        binding.commonView.playReverseVoice.setOnClickListener(this)
        binding.commonView.backToRecord.setOnClickListener(this)

        binding.commonView.textHintMessage.setOnClickListener(this)
        binding.commonView.textHintNum.setOnClickListener(this)
        binding.commonView.imageView.setOnClickListener(this)
    }
}
