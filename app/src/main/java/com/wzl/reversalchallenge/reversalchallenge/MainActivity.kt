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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.wzl.reversalchallenge.utils.common.CacheUtil

class MainActivity : AppCompatActivity() {

    private val permissions_record: Array<String> = arrayOf(
        Manifest.permission.RECORD_AUDIO
    )
    private val permissions_storage: Array<String> = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private val REQUEST_RECORD_AUDIO_PERMISSION = 1
    private val REQUEST_STORAGE_PERMISSION = 2
    private var adapter: MyFragmentPagerAdapter? = null
    private var mList: MutableList<Fragment>? = null
    private var viewPager: ViewPager? = null
    private var radioGroup: RadioGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()

        checkPermissions()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_RECORD_AUDIO_PERMISSION -> {
                //对于录音
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //权限授予成功
                    Toast.makeText(this, "录音权限授予成功！", Toast.LENGTH_SHORT).show()
                } else {
                    //权限授予失败
                    Toast.makeText(this, "录音权限授予失败，请重新授权！", Toast.LENGTH_SHORT).show()
                }
            }
            REQUEST_STORAGE_PERMISSION->{
                //对于存储
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        //权限授予成功
                        Toast.makeText(this, "存储权限授予成功！", Toast.LENGTH_SHORT).show()
                    } else {
                        //权限授予失败
                        Toast.makeText(this, "存储权限授予失败，请重新授权！", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                checkPermissionRecord()
            }
        }
    }

    private fun checkPermissions() {
        val readPermission =
            ContextCompat.checkSelfPermission(this, permissions_storage[0])
        val writePermission =
            ContextCompat.checkSelfPermission(this, permissions_storage[1])
        if (readPermission != PackageManager.PERMISSION_GRANTED || writePermission != PackageManager.PERMISSION_GRANTED) {
            //没有权限时
            ActivityCompat.requestPermissions(
                this,
                permissions_storage,
                REQUEST_STORAGE_PERMISSION
            )
        } else{
            //在有存储权限时，在这里申请录音权限。假如没有存储权限，则在onRequestPermissionsResult()里申请录音权限，防止因为异步问题，吞掉一个权限的申请。
            //不一下子把2个申请同时进行的原因是，这样在只同意了一个权限，而没有同意另一个权限时，仍会弹出2个toast，即申请成功的权限的toast也会弹出来，比较烦人。
            checkPermissionRecord()
        }
    }

    private fun checkPermissionRecord(){
        val recordPermission =
            ContextCompat.checkSelfPermission(this, permissions_record[0])
        if (recordPermission != PackageManager.PERMISSION_GRANTED) {
            //没有权限时
            ActivityCompat.requestPermissions(
                this,
                permissions_record,
                REQUEST_RECORD_AUDIO_PERMISSION
            )
        }
    }

    private fun initView() {

        viewPager = findViewById(R.id.viewPager)
        radioGroup = findViewById(R.id.radioGroup)

        mList = mutableListOf(RecordFragment.newInstance(), RepeatFragment.newInstance())
        adapter = MyFragmentPagerAdapter(supportFragmentManager, mList!!)
        viewPager!!.adapter = adapter
        //viewPager!!.addOnPageChangeListener(mPageChangeListener)
        radioGroup!!.setOnCheckedChangeListener(mCheckedChangeListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        //viewPager!!.removeOnPageChangeListener(mPageChangeListener)
        val recordFragment = supportFragmentManager.findFragmentById(R.id.recordFragmnet)
        recordFragment?.apply {
            (this as RecordFragment).recorder?.release()
            this.mediaPlayer.release()
        }
        val repeatFragment = supportFragmentManager.findFragmentById(R.id.recordFragmnet)
        repeatFragment?.apply {
            (this as RepeatFragment).recorder?.release()
            this.mediaPlayer.release()
        }
    }

//    private val mPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
//
//        override fun onPageScrollStateChanged(state: Int) {
//        }
//
//        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//        }
//
//        override fun onPageSelected(position: Int) {
//            //val radioButton: RadioButton = radioGroup!!.getChildAt(position) as RadioButton
//            //radioButton.isChecked = true
//        }
//    }

    private val mCheckedChangeListener: RadioGroup.OnCheckedChangeListener =
        object : RadioGroup.OnCheckedChangeListener {

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
                dialog.setPositiveButton(
                    R.string.dialog_ok,
                    object : DialogInterface.OnClickListener {
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