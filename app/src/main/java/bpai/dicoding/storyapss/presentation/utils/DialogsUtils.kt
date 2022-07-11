package bpai.dicoding.storyapss.presentation.utils

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogsUtils {
    fun simpleDialog(context:Context,title:String,subtitle:String?,buttonText:String,callbackButton: ()->Unit){
        val dialog = MaterialAlertDialogBuilder(context)
        dialog.setTitle(title)
        if(subtitle != null) dialog.setMessage(subtitle)
        dialog.setPositiveButton(buttonText){ _,_->
            callbackButton.invoke()
        }
        dialog.show()
    }
}