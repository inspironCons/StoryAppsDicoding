package bpai.dicoding.storyapss.presentation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import bpai.dicoding.storyapss.databinding.ActivitySplashBinding
import bpai.dicoding.storyapss.presentation.auth.login.LoginActivity
import bpai.dicoding.storyapss.presentation.homepage.HomeActivity
import bpai.dicoding.storyapss.utils.ConstantName
import com.pixplicity.easyprefs.library.Prefs

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                val intent = if(Prefs.getBoolean(ConstantName.PREFS_has_login)){
                    Intent(this@SplashActivity,HomeActivity::class.java)
                }else{
                    Intent(this@SplashActivity, LoginActivity::class.java)
                }

                startActivity(intent)
                finish()
            }, 3000)
        }
    }
}