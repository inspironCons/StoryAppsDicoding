package bpai.dicoding.storyapss.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import bpai.dicoding.storyapss.R
import bpai.dicoding.storyapss.presentation.auth.login.LoginActivity
import bpai.dicoding.storyapss.utils.ConstantName
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pixplicity.easyprefs.library.Prefs

open class BaseActivity:AppCompatActivity() {
    override fun onResume() {
        super.onResume()
        if(!isSessionActive()) showGotoLogout()
    }

    private fun isSessionActive():Boolean = Prefs.getBoolean(ConstantName.PREFS_has_login,false)

    private fun showGotoLogout(){
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.title_expired))
            .setMessage(resources.getString(R.string.supporting_text))
            .setPositiveButton(resources.getString(R.string.ok)){ _, _->
                startActivity(Intent(this@BaseActivity, LoginActivity::class.java))
            }
            .show()
    }
}