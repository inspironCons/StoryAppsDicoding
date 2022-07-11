package bpai.dicoding.storyapss

import android.app.Application
import android.content.Context
import bpai.dicoding.storyapss.utils.ConstantName
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App:Application() {
    @Inject lateinit var networkFlipperPlugin: NetworkFlipperPlugin
    override fun onCreate() {
        super.onCreate()
        Prefs.Builder()
            .setContext(this)
            .setMode(Context.MODE_PRIVATE)
            .setPrefsName(ConstantName.PREFS_NAME)
            .setUseDefaultSharedPreference(false)
            .build()

        if(BuildConfig.DEBUG){
            SoLoader.init(this,false)
            if(FlipperUtils.shouldEnableFlipper(this)){
                val client =AndroidFlipperClient.getInstance(this)
                client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
                client.addPlugin(DatabasesFlipperPlugin(this))
                client.addPlugin(networkFlipperPlugin)
                client.addPlugin(SharedPreferencesFlipperPlugin(this, ConstantName.PREFS_NAME))
                client.start()
            }
        }
    }
}