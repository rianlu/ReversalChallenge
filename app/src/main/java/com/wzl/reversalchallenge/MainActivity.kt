package com.wzl.reversalchallenge;

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.wzl.reversalchallenge.utils.common.CacheUtil

public class MainActivity : AppCompatActivity() {

    private val permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private val REQUEST_RECORD_AUDIO_PERMISSION  = 1
    private var adapter: MyFragmentPagerAdapter? = null
    private var mList: MutableList<Fragment>? = null
    private var viewPager: ViewPager? = null
    private var radioGroup: RadioGroup? = null


    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);

        initView()

        checkPermissions()
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

    fun initView() {

        viewPager = findViewById(R.id.viewPager)
        radioGroup = findViewById(R.id.radioGroup)

        mList = mutableListOf(RecordFragment.newInstance(), RepeatFragment.newInstance())
        adapter = MyFragmentPagerAdapter(supportFragmentManager, mList!!)
        viewPager!!.adapter = adapter
        viewPager!!.addOnPageChangeListener(mPageChangeListener)
        radioGroup!!.setOnCheckedChangeListener(mCheckedChangeListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager!!.removeOnPageChangeListener(mPageChangeListener)
        val recordFragment = supportFragmentManager.findFragmentById(R.id.recordFragmnet)
        recordFragment?.apply {
            (this as RecordFragment).recorder.release()
            this.mediaPlayer.release()
        }
        val repeatFragment = supportFragmentManager.findFragmentById(R.id.recordFragmnet)
        repeatFragment?.apply {
            (this as RepeatFragment).recorder.release()
            this.mediaPlayer.release()
        }
    }

    private val mPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            val radioButton: RadioButton = radioGroup!!.getChildAt(position) as RadioButton
            radioButton.isChecked = true
        }
    }

    private val mCheckedChangeListener: RadioGroup.OnCheckedChangeListener = object : RadioGroup.OnCheckedChangeListener {

        override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
            for (i in 0 until group!!.childCount) {
                if (group.getChildAt(i).id == checkedId) {
                    viewPager!!.currentItem = i
                    return
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.clear_cache -> {
                val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
                dialog.setTitle(R.string.app_dialog_hint)
                dialog.setMessage("共有${CacheUtil.getExternalCacheSize(this)}缓存，是否清理？")
                dialog.setPositiveButton(R.string.dialog_ok, object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        CacheUtil.clearExternalCache(this@MainActivity)
                    }
                })
                dialog.create().show()
            }
            R.id.app_about -> {
                val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
                dialog.setTitle(R.string.app_dialog_about)
                dialog.setMessage(R.string.dialog_message)
                dialog.setPositiveButton(R.string.dialog_yes, null)
                dialog.create().show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
