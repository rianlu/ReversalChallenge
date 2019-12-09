package com.wzl.reversalchallenge


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.wzl.reversalchallenge.databinding.FragmentRepeatBinding

/**
 * A simple [Fragment] subclass.
 */
class RepeatFragment : Fragment(), View.OnClickListener {

    private var mediaUtil: MediaUtil? = null
    private var binding: FragmentRepeatBinding? = null

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
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mediaUtil = MediaUtil(requireActivity())
        initView()
    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.start_play -> {
                Toast.makeText(requireActivity(), "点击 start_play", Toast.LENGTH_SHORT).show()
                mediaUtil!!.startPlay()
            }
            R.id.stop_play -> {
                Toast.makeText(requireActivity(), "点击 stop_play", Toast.LENGTH_SHORT).show()
                mediaUtil!!.stopPlay()
            }
        }
    }

    fun initView() {
        binding!!.startPlay.setOnClickListener(this)
        binding!!.stopPlay.setOnClickListener(this)
    }

}
