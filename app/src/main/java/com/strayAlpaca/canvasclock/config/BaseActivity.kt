package com.strayAlpaca.canvasclock.config

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

abstract class BaseActivity<B : ViewBinding> (@LayoutRes val layoutRes : Int) : AppCompatActivity() {
    protected lateinit var binding : B
    protected lateinit var firebaseAnalytics : FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAnalytics = Firebase.analytics

        preLoad()

        binding = DataBindingUtil.setContentView(this, layoutRes)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        currentFocus?.let { view ->
            val rect = Rect()
            view.getGlobalVisibleRect(rect)
            ev?.let { motionEvent ->
                if (!rect.contains(motionEvent.x.toInt(), motionEvent.y.toInt())) {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                    view.clearFocus()
                }
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    open fun setButton(){}

    open fun setRecyclerView(){}

    open fun preLoad() {}

    fun showSimpleToast(message : String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}