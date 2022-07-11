package bpai.dicoding.storyapss.di

import bpai.dicoding.storyapss.data.remote.network.login.ILoginApi
import bpai.dicoding.storyapss.data.remote.network.register.IRegisterApi
import bpai.dicoding.storyapss.data.remote.network.stories.IStoriesApi
import bpai.dicoding.storyapss.utils.ConstantName
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.pixplicity.easyprefs.library.Prefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    private val networkFlipperPlugin = NetworkFlipperPlugin()
    @Provides
    fun provideNetworkModule():NetworkFlipperPlugin = networkFlipperPlugin

    private val client = OkHttpClient.Builder()
        .addNetworkInterceptor(FlipperOkhttpInterceptor(networkFlipperPlugin))
        .addInterceptor{ chain->
            val original = chain.request()
            val token = Prefs.getString(ConstantName.PREFS_token)
            if(token.isNotEmpty()){
                val request = original.newBuilder()
                    .header(
                        "Authorization",
                        "Bearer $token"
                    )
                    .build()
                chain.proceed(request)
            }else{
                chain.proceed(original)
            }
        }
        .build()

    @Provides
    fun provideRetrofit():Retrofit = Retrofit.Builder()
        .baseUrl(ConstantName.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T>buildService(service: Class<T>):T = provideRetrofit().create(service)

    @Provides
    fun provideLoginNetwork(retrofit: Retrofit):ILoginApi = retrofit.create(ILoginApi::class.java)

    @Provides
    fun provideRegisterNetwork(retrofit: Retrofit):IRegisterApi = retrofit.create(IRegisterApi::class.java)

    @Provides
    fun provideStoriesNetwork(retrofit: Retrofit): IStoriesApi = retrofit.create(IStoriesApi::class.java)
}