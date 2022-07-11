package bpai.dicoding.storyapss.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import bpai.dicoding.storyapss.R
import bpai.dicoding.storyapss.StoryStackWidget
import bpai.dicoding.storyapss.domain.repository.IStoriesRepository
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class StackRemoteViewsFactory(
    private val mContext:Context
):RemoteViewsService.RemoteViewsFactory {
    private val mWidgetItems = ArrayList<Bitmap>()
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    @Inject lateinit var repo:IStoriesRepository
    override fun onCreate() {

    }


    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()
        coroutineScope.launch {
            mWidgetItems.clear()
            try {
                repo.getListHistoryToWidget().collect{ _list->
                   _list.map {
                       val bitmap = try {
                           Glide.with(mContext)
                               .asBitmap()
                               .load(it.image)
                               .submit()
                               .get()
                       }catch (e:Exception){
                           BitmapFactory.decodeResource(mContext.resources,R.drawable.ic_broken_image)
                       }

                       mWidgetItems.add(bitmap)
                   }
                }

            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        Binder.restoreCallingIdentity(identityToken)

    }

    override fun onDestroy() {
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(p0: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.image_widget, mWidgetItems[p0])

        val extras = bundleOf(
            StoryStackWidget.EXTRA_ITEM to p0
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.image_widget, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}