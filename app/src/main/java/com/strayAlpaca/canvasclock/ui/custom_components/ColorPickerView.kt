package com.strayAlpaca.canvasclock.ui.custom_components

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.strayAlpaca.canvasclock.databinding.ViewColorPickerBinding
import com.strayAlpaca.canvasclock.ui.recycler.adapter.ColorInUseAdapter
import com.strayAlpaca.domain.utils.*
import kotlin.math.roundToInt

class ColorPickerView(context : Context, attrs : AttributeSet) : ConstraintLayout(context, attrs) {
    private val binding = ViewColorPickerBinding.inflate(LayoutInflater.from(context), this, true)

    lateinit var changeCallback : (String) -> Unit
    private var isInitSetting = false

    var currentColor = ""

    fun setButtonCallback(cancelCallback : () -> Unit, selectCallback : () -> Unit) {
        binding.tvbtnCancel.setOnClickListener {
            cancelCallback()
        }
        binding.tvbtnSelect.setOnClickListener {
            selectCallback()
        }

        binding.root.setOnClickListener {
            /* 특정 화면에서 화면을 클릭하면 해당 view 가 종료되는 경우가 있는데, 그 경우를 막기 위함 */
        }
    }

    fun attachChangeCallbackFunction(callback : (String) -> Unit) {
        changeCallback = callback
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        setSeekbar()
        setEditText()
        setRecyclerView()
    }

    fun setColor(colorString : String) {
        currentColor = colorString

        val transparency = (Integer.parseInt(getTransparency(currentColor), 16) * 100 / 255f).roundToInt()
        val red = Integer.parseInt(getRed(currentColor), 16)
        val blue = Integer.parseInt(getBlue(currentColor), 16)
        val green = Integer.parseInt(getGreen(currentColor), 16)

        changeColorText(binding.etRgb, colorString)
        changeProgressText(binding.etRed, red)
        changeProgressText(binding.etBlue, blue)
        changeProgressText(binding.etGreen, green)
        changeProgressText(binding.etTransparency, transparency)

        changeProgress(binding.seekbarRed, red)
        changeProgress(binding.seekbarBlue, blue)
        changeProgress(binding.seekbarGreen, green)
        changeProgress(binding.seekbarTransparency, transparency)

        binding.viewColor.ivColor.setBackgroundColor(Color.parseColor(currentColor))
    }

    private fun setSeekbar(){
        binding.seekbarRed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (!isInitSetting){
                    val newColorString = changeRed(redInt = p1)
                    changeCallback(newColorString)
                }
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        binding.seekbarGreen.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (!isInitSetting) {
                    val newColorString = changeGreen(greenInt = p1)
                    changeCallback(newColorString)
                }
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })


        binding.seekbarBlue.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (!isInitSetting) {
                    val newColorString = changeBlue(blueInt = p1)
                    changeCallback(newColorString)
                }
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        binding.seekbarTransparency.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (!isInitSetting) {
                    val newColorString = changeTransparency(transparencyInt = p1)
                    changeCallback(newColorString)
                }
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    private fun setEditText() {
        binding.etRgb.setOnFocusChangeListener { view, b ->
            if (!b) {
                view as AppCompatEditText
                val newColorString = view.text.toString()
                if (isRgbFormat(newColorString)){
                    if (newColorString.length == 7){
                        changeCallback( "#FF" +  newColorString.substring(1))
                    } else {
                        changeCallback(newColorString)
                    }
                } else {
                    view.setText(currentColor)
                }
            }
        }

        binding.etRed.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val red = p0.toString().toIntOrNull()
                red?.let {
                    if (it in 0..255) {
                        val newColorString = changeRed(redInt = it)
                        changeCallback(newColorString)
                    } else {
                        binding.etRed.setText("255")
                    }
                }
            }
        })

        binding.etGreen.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val red = p0.toString().toIntOrNull()
                red?.let {
                    if (it in 0..255) {
                        val newColorString = changeGreen(greenInt = it)
                        changeCallback(newColorString)
                    } else {
                        binding.etGreen.setText("255")
                    }
                }
            }
        })

        binding.etBlue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val red = p0.toString().toIntOrNull()
                red?.let {
                    if (it in 0..255) {
                        val newColorString = changeBlue(blueInt = it)
                        changeCallback(newColorString)
                    } else {
                        binding.etBlue.setText("255")
                    }
                }
            }
        })

        binding.etTransparency.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val red = p0.toString().toIntOrNull()
                red?.let {
                    if (it in 0..100) {
                        val newColorString = changeTransparency(transparencyInt = it)
                        changeCallback(newColorString)
                    } else {
                        binding.etTransparency.setText("100")
                    }
                }
            }
        })
    }

    private fun setRecyclerView(){
        binding.rvColorInUse.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvColorInUse.adapter = ColorInUseAdapter(changeCallback)
    }

    private fun changeColorText(edittext : AppCompatEditText, colorText : String) {
        if (edittext.text.toString() != colorText) {
            edittext.setText(colorText)
        }
    }

    private fun changeProgress(seekbar : AppCompatSeekBar, newProgress : Int){
        if (seekbar.progress != newProgress) {
            seekbar.progress = newProgress
        }
    }

    private fun changeProgressText(editText : AppCompatEditText, newProgress : Int) {
        if (editText.text.toString().toIntOrNull() != newProgress) {
            editText.setText(newProgress.toString())
        }
    }

    // 사용중인 색상 목록을 갱신할 때 호출
    fun setColorInUse(colorInUseSet : MutableSet<String>) {
        (binding.rvColorInUse.adapter as ColorInUseAdapter).changeData(colorInUseSet)
    }

    fun setIsInit(){
        isInitSetting = true
    }

    fun removeIsInit(){
        isInitSetting = false
    }

    private fun changeRed(redInt : Int) : String {
        var red = redInt.toString(16)
        if (red.length == 1) red = "0$red"
        val newColorString = if (currentColor.length == 7){
            "#FF" + red + currentColor.substring(3)
        } else {
            "#" + currentColor.substring(1, 3) + red + currentColor.substring(5)
        }
        return newColorString
    }

    private fun changeGreen(greenInt : Int) : String {
        var green = greenInt.toString(16)
        if (green.length == 1) green = "0$green"
        val newColorString = if (currentColor.length == 7){
            "#FF" + currentColor.substring(1, 3) + green + currentColor.substring(5)
        } else {
            "#" + currentColor.substring(1, 5) + green + currentColor.substring(7)
        }
        return newColorString
    }

    private fun changeBlue(blueInt : Int) : String {
        var blue = blueInt.toString(16)
        if (blue.length == 1) blue = "0$blue"
        val newColorString = if (currentColor.length == 7){
            "#FF" + currentColor.substring(1, 5) + blue
        } else {
            "#" + currentColor.substring(1, 7) + blue
        }
        return newColorString
    }

    private fun changeTransparency(transparencyInt : Int) : String {
        var transparency = (transparencyInt * 255 / 100f).roundToInt().toString(16)
        if (transparency.length == 1) transparency = "0$transparency"
        val newColorString = if (currentColor.length == 7) {
            "#" + transparency + currentColor.substring(1)
        } else {
            "#" + transparency + currentColor.substring(3)
        }
        return newColorString
    }
}