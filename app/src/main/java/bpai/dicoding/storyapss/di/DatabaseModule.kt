package bpai.dicoding.storyapss.di

import android.content.Context
import androidx.room.Room
import bpai.dicoding.storyapss.database.StoryDb
import bpai.dicoding.storyapss.utils.ConstantName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun database(@ApplicationContext app: Context) = Room.databaseBuilder(app,
        StoryDb::class.java,
        ConstantName.DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideStoriesDao(database: StoryDb) = database.storiesDao()

    @Singleton
    @Provides
    fun provideRemoteKeysDao(database: StoryDb) = database.remotesKeysDao()
}