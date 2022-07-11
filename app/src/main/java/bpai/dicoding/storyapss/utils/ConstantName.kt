package bpai.dicoding.storyapss.utils

import bpai.dicoding.storyapss.BuildConfig


object ConstantName {
    const val BASE_URL = BuildConfig.BASE_URL
    const val PREFS_NAME = "StoryApps"

    //prefs session
    const val PREFS_username = "username"
    const val PREFS_token = "token"
    const val PREFS_has_login = "hasLogin"

    //page
    const val FIRST_PAGE_HISTORY = 1
    const val PAGE_SIZE_HISTORY = 10

    //database
    const val DATABASE_NAME = "StoryApps"
    const val DATABASE_VERSION = 1

}