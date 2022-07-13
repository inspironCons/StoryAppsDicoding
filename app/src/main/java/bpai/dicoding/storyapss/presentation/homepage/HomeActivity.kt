package bpai.dicoding.storyapss.presentation.homepage

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bpai.dicoding.storyapss.R
import bpai.dicoding.storyapss.adapter.StoriesAdapter
import bpai.dicoding.storyapss.databinding.ActivityHomeBinding
import bpai.dicoding.storyapss.databinding.ListItemHitoryBinding
import bpai.dicoding.storyapss.model.Stories
import bpai.dicoding.storyapss.presentation.BaseActivity
import bpai.dicoding.storyapss.presentation.auth.login.LoginActivity
import bpai.dicoding.storyapss.presentation.camera.CameraStoryActivity
import bpai.dicoding.storyapss.presentation.story.base_on_maps.StoryBasedOnMap
import bpai.dicoding.storyapss.presentation.story.detail.StoryActivity
import bpai.dicoding.storyapss.utils.Session
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeActivity : BaseActivity() {
    private lateinit var binding:ActivityHomeBinding
    private val viewModel:HomeViewModel by viewModels()

    private val resultCreateStory = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        getStories()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        //toolbar override
        binding.toolbar.overflowIcon = ContextCompat.getDrawable(this,R.drawable.ic_menu)
        binding.toolbar.title = getString(R.string.greeting_name,Session.getUsername())

        setContentView(binding.root)
        getStories()
        goesToAddStory()
        animationFab()
        menusToolbar()
    }

    private fun getStories() {
        val adapter = StoriesAdapter()
        binding.rvHistories.layoutManager = LinearLayoutManager(this)
        binding.rvHistories.setHasFixedSize(true)
        binding.rvHistories.adapter = adapter

        lifecycleScope.launchWhenCreated {
            viewModel.fetchList().collectLatest {
                adapter.submitData(it)
            }
        }

        adapter.setOnClickListener(object :StoriesAdapter.OnClickListener{
            override fun onClick(data: Stories?, view: ListItemHitoryBinding) {
                val mIntent = Intent(this@HomeActivity, StoryActivity::class.java)
                mIntent.putExtra(StoryActivity.DATA_BUNDLE,data)

                val optionCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this@HomeActivity,
                    Pair(view.ivImage,"image_story"),
                    Pair(view.tvUsername,"username"),
                    Pair(view.tvLoc,"location")
                )
                startActivity(mIntent,optionCompat.toBundle())
            }
        })
    }

    private fun goesToAddStory(){
        binding.fabCreateHistory.setOnClickListener {
            try {
                val mIntent = Intent(this, CameraStoryActivity::class.java)
                resultCreateStory.launch(mIntent)
            }catch (e:ClassNotFoundException){
                e.printStackTrace()
            }
        }
    }

    private fun animationFab(){
        binding.rvHistories.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    binding.fabCreateHistory.extend()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && binding.fabCreateHistory.isExtended) {
                    binding.fabCreateHistory.shrink()
                }
            }
        })
    }

    private fun menusToolbar(){
        binding.toolbar.setOnMenuItemClickListener { menu->
            when(menu.itemId){
                R.id.change_language->{
                    val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                    startActivity(mIntent)
                    true
                }
                R.id.logout->{
                    actionLogout()
                    true
                }
                R.id.maps_location->{
                    val mIntent = Intent(this,StoryBasedOnMap::class.java)
                    startActivity(mIntent)
                    true
                }
                else->false
            }
        }
    }

    private fun actionLogout(){
        //stop session
        val mSession = Intent(this, Session::class.java)
        mSession.action = Session.ACTION_STOP_SESSION
        startService(mSession)

        //go to login
        val mIntent = Intent(this,LoginActivity::class.java)
        startActivity(mIntent)
        finish()
    }
}