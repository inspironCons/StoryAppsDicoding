package bpai.dicoding.storyapss.database

import androidx.room.Database
import androidx.room.RoomDatabase
import bpai.dicoding.storyapss.data.remote.local.stories.StoriesDao
import bpai.dicoding.storyapss.data.remote.local.stories.StoriesEntity
import bpai.dicoding.storyapss.utils.ConstantName

@Database([
    StoriesEntity::class
], version = ConstantName.DATABASE_VERSION, exportSchema = true)
abstract class StoryDb:RoomDatabase() {
    abstract fun storiesDao(): StoriesDao
}