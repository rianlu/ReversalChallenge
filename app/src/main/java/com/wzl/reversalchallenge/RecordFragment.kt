package com.wzl.reversalchallenge


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.wzl.reversalchallenge.databinding.FragmentRecordBinding

/**
 * A simple [Fragment] subclass.
 */
class RecordFragment : Fragment(), View.OnClickListener {

    private var binding: FragmentRecordBinding? = null
    private var mediaUtil: MediaUtil? = null

    companion object {
        private var fragment: RecordFragment? = null
        fun newInstance(): RecordFragment {
            if (fragment == null) {
                fragment = RecordFragment()
            }
            return fragment as RecordFragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_record, container, false)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record, container, false)
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mediaUtil = MediaUtil(requireActivity())
        initView()
    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.start_record -> {
                Toast.makeText(requireActivity(), "点击 start_record", Toast.LENGTH_SHORT).show()
                mediaUtil!!.startRecord()
            }
            R.id.stop_record -> {
                Toast.makeText(requireActivity(), "点击 stop_record", Toast.LENGTH_SHORT).show()
                mediaUtil!!.stopRecord()
            }
        }
    }

    fun initView() {
        binding!!.startRecord.setOnClickListener(this)
        binding!!.stopRecord.setOnClickListener(this)
    }
}
