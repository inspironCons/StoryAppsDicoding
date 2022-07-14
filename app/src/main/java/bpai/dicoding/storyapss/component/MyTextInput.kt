package bpai.dicoding.storyapss.component

import android.content.Context
import android.graphics.Canvas
import android.text.InputType
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import bpai.dicoding.storyapss.R

class MyTextInput: AppCompatEditText {
    private var type:Int = 0

    constructor(context: Context):super(context){
        init()
    }

    constructor(context: Context,attrs:AttributeSet):super(context,attrs){
        init()

    }

    constructor(context: Context,attrs: AttributeSet,defStyles:Int):super(context,attrs,defStyles){
        init()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        type = inputType
        val tf = ResourcesCompat.getFont(context,R.font.roboto_regular)
        typeface = tf

    }

    private fun init(){
        addTextChangedListener { s->
            val text = s.toString()
            if(type == InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT) validatePassword(text)
            if(type == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS or InputType.TYPE_CLASS_TEXT) validateEmail(text)
        }
    }

    private fun validateEmail(text: String) {
        val pattern = Patterns.EMAIL_ADDRESS
        if(!pattern.matcher(text).matches()){
            background =  resources.getDrawable(R.drawable.bg_form_error,null)
            error = context.getString(R.string.validation_email)
        }else{
            background =  resources.getDrawable(R.drawable.bg_form,null)
            error = null
        }
    }

    private fun validatePassword(text:String){
        if(text.length < 6){
            background =  resources.getDrawable(R.drawable.bg_form_error,null)
            error = context.getString(R.string.validation_password)
        }else{
            background = resources.getDrawable(R.drawable.bg_form,null)
            error = null

        }
    }
}