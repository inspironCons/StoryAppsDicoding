package bpai.dicoding.storyapss.presentation.auth.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import bpai.dicoding.storyapss.R
import bpai.dicoding.storyapss.databinding.ActivityLoginBinding
import bpai.dicoding.storyapss.presentation.auth.register.RegisterActivity
import bpai.dicoding.storyapss.presentation.homepage.HomeActivity
import bpai.dicoding.storyapss.utils.ConstantName
import bpai.dicoding.storyapss.utils.Session
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        whenInputForm()
        whenSignIn()
        whenSignUp()
        loginState()
        playAnimation()
    }

    private fun whenInputForm() {
        binding.etEmail.addTextChangedListener { text-> viewModel.setEmail(text.toString())}
        binding.etPassword.addTextChangedListener { text->viewModel.setPassword(text.toString()) }
    }

    private fun whenSignIn() {
        binding.btnSignIn.setOnClickListener {
            viewModel.login()
        }
    }

    private fun loginState(){
        lifecycleScope.launchWhenCreated {
            viewModel.loginState.collect{ state->
                when(state){
                    is LoginViewModel.LoginState.Success ->{
                        val session = state.sessions
                        startServiceSession(session?.name,session?.token)
                        val mIntent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(mIntent)
                        finish()
                        binding.btnSignIn.isEnabled = true
                        binding.btnSignIn.text = getString(R.string.sign_in)
                    }
                    is LoginViewModel.LoginState.Error ->{
                        Toast.makeText(this@LoginActivity,state.message,Toast.LENGTH_LONG).show()
                        binding.btnSignIn.isEnabled = true
                        binding.btnSignIn.text = getString(R.string.sign_in)
                    }
                    is LoginViewModel.LoginState.Loading ->{
                        binding.btnSignIn.isEnabled = false
                        binding.btnSignIn.text = getString(R.string.loading)

                    }
                    else -> Unit
                }
            }
        }
    }

    private fun whenSignUp() {
        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }
    }

    private fun startServiceSession(name:String?,token:String?) {
        if(!Session.getIsActive()){
            val mSession = Intent(this, Session::class.java)
            mSession.action = Session.ACTION_START_SESSION
            mSession.putExtra(ConstantName.PREFS_username,name)
            mSession.putExtra(ConstantName.PREFS_token,token)

            startService(mSession)
        }
    }

    private fun playAnimation(){
        val title = ObjectAnimator.ofFloat(binding.headerTitle,View.ALPHA,1f).setDuration(800)
        val subtitle = ObjectAnimator.ofFloat(binding.subHeaderTitle,View.ALPHA,1f).setDuration(800)
        val formEmail = ObjectAnimator.ofFloat(binding.etEmail,View.ALPHA,1f).setDuration(500)
        val formPassword = ObjectAnimator.ofFloat(binding.etPassword,View.ALPHA,1f).setDuration(500)
        val buttonSignIn = ObjectAnimator.ofFloat(binding.btnSignIn,View.ALPHA,1f).setDuration(500)
        val buttonSignUp = ObjectAnimator.ofFloat(binding.btnSignUp,View.ALPHA,1f).setDuration(500)
        val textOr = ObjectAnimator.ofFloat(binding.tvOr,View.ALPHA,1f).setDuration(500)
        val together = AnimatorSet().apply {
            playTogether(formEmail,formPassword,buttonSignUp,buttonSignIn,textOr)
        }
        AnimatorSet().apply{
            playSequentially(title,subtitle,together)
            start()
        }


    }
}