package bpai.dicoding.storyapss.presentation.auth.register

import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import bpai.dicoding.storyapss.R
import bpai.dicoding.storyapss.databinding.ActivityRegisterBinding
import bpai.dicoding.storyapss.presentation.auth.login.LoginActivity
import com.airbnb.lottie.LottieAnimationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    //dialog
    private lateinit var dialog: Dialog
    private lateinit var loaderState: LottieAnimationView
    private lateinit var descriptionDialog: TextView
    private lateinit var btnLoader: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //create dialog
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        whenInputForm()
        whenClickRegister()
        whenSignIn()
        observeRegisterState()
    }

    private fun whenInputForm() {
        binding.etUsername.addTextChangedListener { text-> viewModel.setName(text.toString())}
        binding.etEmail.addTextChangedListener { text-> viewModel.setEmail(text.toString())}
        binding.etPassword.addTextChangedListener { text->viewModel.setPassword(text.toString()) }
    }

    private fun whenClickRegister(){
        binding.btnRegister.setOnClickListener {
            showDialog()
            setLottie(R.raw.loading,true)
            viewModel.singUpAccount()


        }
    }

    private fun observeRegisterState(){
        lifecycleScope.launchWhenCreated {
            viewModel.registerState.collect{ state->
                when(state){
                    is RegisterViewModel.RegisterState.Success->{
                        setLottie(R.raw.success,false)
                        descriptionDialog.text = state.msg
                        btnLoader.setOnClickListener {
                            dialog.dismiss()
                            goesToSignIn()
                        }
                    }
                    is RegisterViewModel.RegisterState.Loading->{
                        descriptionDialog.text = state.msg
                    }

                    is RegisterViewModel.RegisterState.Error->{
                        setLottie(R.raw.failure,false)
                        descriptionDialog.text = state.msg
                        btnLoader.setOnClickListener {
                            dialog.dismiss()
                        }
                    }
                    else->Unit
                }
            }
        }
    }

    private fun whenSignIn() {
        binding.btnSingIn.setOnClickListener {
            goesToSignIn()
        }
    }

    private fun goesToSignIn() {
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        finish()
    }

    /**
     * show dialog loader
     */
    private fun showDialog(){
        dialog.setContentView(R.layout.dialog_view_loader)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        descriptionDialog = dialog.findViewById(R.id.tv_deskripsi)
        loaderState = dialog.findViewById(R.id.loader_state)
        btnLoader = dialog.findViewById(R.id.btn_back)



        dialog.show()
    }

    private fun setLottie(raw:Int,loop:Boolean){
        loaderState.setAnimation(raw)
        loaderState.playAnimation()
        loaderState.repeatCount = if(loop) ValueAnimator.INFINITE else 0
        btnLoader.isEnabled = !loop
    }
}