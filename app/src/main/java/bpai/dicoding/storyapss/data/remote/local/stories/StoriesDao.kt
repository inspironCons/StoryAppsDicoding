package bpai.dicoding.storyapss.data.remote.local.stories

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoriesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(stories:List<StoriesEntity>)

    @Query("SELECT * from stories")
    fun getAllStories():List<StoriesEntity>
}