package com.example.canvasclock.config

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<B : ViewBinding> (@LayoutRes val layoutRes : Int) : AppCompatActivity() {
    protected lateinit var binding : B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preLoad()

        binding = DataBindingUtil.setContentView(this, layoutRes)
    }

    open fun setButton(){}

    open fun setRecyclerView(){}

    open fun preLoad() {}

    fun showSimpleToast(message : String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}