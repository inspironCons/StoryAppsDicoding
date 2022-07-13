package bpai.dicoding.storyapss.presentation.story.base_on_maps

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import bpai.dicoding.storyapss.R
import bpai.dicoding.storyapss.databinding.ActivityStoryBasedOnMapBinding
import bpai.dicoding.storyapss.model.Stories
import bpai.dicoding.storyapss.presentation.utils.DialogsUtils
import bpai.dicoding.storyapss.presentation.utils.ImageUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoryBasedOnMap : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStoryBasedOnMapBinding

    private val viewModel:StoryBasedOnMapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryBasedOnMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get list map
        viewModel.getStoriesByLocation()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        getDataLocation()

        //back action
        binding.actionBack.setOnClickListener { onBackPressed() }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mapStyles()
        getMyLocation()
        locationIndonesiaSet()
    }

    /**
     * get lan lot Indonesia view on Map
     */
    private fun locationIndonesiaSet() {
        val indonesiaLoc = LatLng( -6.200000, 106.816666)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(indonesiaLoc, 5f))
    }
    /**
     * setting enable my location
     */
    private fun getMyLocation() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }

                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if(p0?.isAnyPermissionPermanentlyDenied == true){
                        goesToSetting(p0.deniedPermissionResponses.joinToString(","))
                    }else if(p0?.areAllPermissionsGranted() == true){
                        mMap.isMyLocationEnabled = true
                    }
                }

            }).check()
    }
    /**
     * setting map Style google map
     */
    private fun mapStyles() {
        try {
            val sMaps = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.map_style))
            if(!sMaps){
                Log.e(StoryBasedOnMap::class.java.simpleName,"Style parsing failed.")
            }
        }catch (e:Exception){
            Log.e(StoryBasedOnMap::class.java.simpleName,e.localizedMessage?:"")
        }
    }

    /**
     * open dialog to navigate user open setting apps
     */
    private fun goesToSetting(permissionName:String?) {
        DialogsUtils.simpleDialog(
            this,
            resources.getString(R.string.need_permission),
            getString(R.string.need_permission_desc,permissionName),
            resources.getString(R.string.go_to_setting)
        ){
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null)
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    /**
     * get all marker from api
     */
    private fun getDataLocation(){
        lifecycleScope.launchWhenCreated {
            viewModel.mapState.collect{ state->
                when(state){
                    is StoryBasedOnMapViewModel.MarkState.Success->{
                        setMarker(state.stories)
                    }
                    is StoryBasedOnMapViewModel.MarkState.Error->{
                        Toast.makeText(this@StoryBasedOnMap,state.message,Toast.LENGTH_LONG).show()
                    }
                    else->Unit
                }
            }
        }
    }

    private fun setMarker(stories: List<Stories>) {
        stories.forEach { stories->
            val location = LatLng(stories.lat,stories.lon)
            mMap.addMarker(MarkerOptions()
                .position(location)
                .title(stories.name)
                .snippet(stories.description)
                .icon(ImageUtils.vectorToBitmap(this,R.drawable.ic_location,getColor(R.color.coral)))
            )
        }
    }
}