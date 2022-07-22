package com.example.canvasclock.ui.page.main

import android.os.Bundle
import androidx.activity.viewModels
import com.example.canvasclock.R
import com.example.canvasclock.config.BaseActivity
import com.example.canvasclock.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setButton()
        setRecyclerView()
    }

    override fun setButton() {

        binding.viewClock.setOnClickListener{
            viewModel.tryGetRecentWatch(1)
        }
    }

    override fun setRecyclerView() {

    }

    override fun preLoad() {

    }

}