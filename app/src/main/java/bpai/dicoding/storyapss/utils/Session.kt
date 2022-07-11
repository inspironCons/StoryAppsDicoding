package bpai.dicoding.storyapss.utils

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class Session:Service() {
    private val job = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main+job)

    private fun startSessionService(name:String?, token:String?){
        Prefs.putString(ConstantName.PREFS_username,name)
        Prefs.putString(ConstantName.PREFS_token,token)
        Prefs.putBoolean(ConstantName.PREFS_has_login,true)

        serviceScope.launch {
            //clear session 1 hours
            delay(3600000)
            stopSessionService()
        }
    }

    private fun stopSessionService(){
        Log.d("ServiceTTT","Service telah remove all session")
        Prefs.clear()
        stopSelf()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent != null){
            val action = intent.action
            if(action != null){
                val extra = intent.extras
                when(action){
                    ACTION_START_SESSION ->{
                        val name = extra?.getString(ConstantName.PREFS_username) as String
                        val token = extra.getString(ConstantName.PREFS_token) as String
                        startSessionService(name,token)
                    }
                    ACTION_STOP_SESSION ->{
                        stopSessionService()
                    }
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        Log.d("ServiceTTT","Service telah destroy")
    }

    companion object{
        private var isServiceActive =false
        fun getIsActive():Boolean = isServiceActive
        fun getUsername():String = Prefs.getString(ConstantName.PREFS_username)

        const val ACTION_START_SESSION = "ACTION_START_SESSION"
        const val ACTION_STOP_SESSION = "ACTION_STOP_SESSION"

    }


}