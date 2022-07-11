package bpai.dicoding.storyapss.database

import androidx.room.Database
import androidx.room.RoomDatabase
import bpai.dicoding.storyapss.data.remote.local.remote_keys.RemoteKeysDao
import bpai.dicoding.storyapss.data.remote.local.remote_keys.RemoteKeysEntity
import bpai.dicoding.storyapss.data.remote.local.stories.StoriesDao
import bpai.dicoding.storyapss.data.remote.local.stories.StoriesEntity
import bpai.dicoding.storyapss.utils.ConstantName

@Database(entities = [
    StoriesEntity::class,
    RemoteKeysEntity::class
], version = ConstantName.DATABASE_VERSION, exportSchema = false)
abstract class StoryDb:RoomDatabase() {
    abstract fun storiesDao(): StoriesDao
    abstract fun remotesKeysDao(): RemoteKeysDao

}