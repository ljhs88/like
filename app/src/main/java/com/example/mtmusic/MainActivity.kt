package com.example.mtmusic

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mtmusic.databinding.ActivityMainBinding
import com.example.mtmusic.ui.InforFragment
import com.example.mtmusic.ui.musicFragment
import com.example.mtmusic.ui.playFragment
import com.example.mtmusic.ui.vwFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private val REQUEST_CAMERA_AND_STORAGE_PERMISSION = 1

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 检查权限
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            // 请求权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CAMERA_AND_STORAGE_PERMISSION)
            }
        }

        // 设置viewpager
        setPager()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_AND_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.d("qx", "query")
            } else {
                finish()
            }
        }
    }

    private fun setPager() {
        val adapter = MyPagerAdapter(this)
        binding.pager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.pager) {tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.tab1)
                1 -> tab.text = getString(R.string.tab2)
                2 -> tab.text = getString(R.string.tab3)
                else -> tab.text = "Tab ${position + 1}"
            }
            // 忘写了。。。
        }.attach()

        // 设置初始tab为第二个
        binding.pager.setCurrentItem(1, false)
    }

    class MyPagerAdapter(activity: MainActivity) : FragmentStateAdapter(activity) {

        override fun getItemCount() = 3

        @Throws(IllegalAccessException::class)
        override fun createFragment(position: Int): Fragment {
            return when(position) {
                0 -> vwFragment()
                1 -> playFragment()
                2 -> InforFragment()
                else -> throw IllegalAccessException("Invalid position: $position")
            }
        }

    }
}