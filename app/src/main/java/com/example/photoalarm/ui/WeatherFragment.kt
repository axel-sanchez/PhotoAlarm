package com.example.photoalarm.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.photoalarm.data.models.Result
import com.example.photoalarm.databinding.FragmentWeatherBinding
import com.example.photoalarm.domain.WeatherUseCase
import com.example.photoalarm.ui.customs.PhotoAlarmFragment
import com.example.photoalarm.viewmodel.WeatherViewModel
import com.example.photoalarm.viewmodel.WeatherViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val MY_PERMISSIONS_REQUEST_LOCATION = 5254

class WeatherFragment : PhotoAlarmFragment() {

    private val viewModel: WeatherViewModel by lazy { ViewModelProviders.of(requireActivity(), WeatherViewModelFactory(WeatherUseCase())).get(WeatherViewModel::class.java) }

    private lateinit var locationManager: LocationManager
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private val locationListener: LocationListener = object : LocationListener {
        @SuppressLint("SetTextI18n")
        override fun onLocationChanged(location: Location) {
            longitude = location.longitude
            latitude = location.latitude

            CoroutineScope(Main).launch {
                viewModel.getWeather(latitude.toString(), longitude.toString())
            }
        }

        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
        override fun onProviderEnabled(s: String) {}
        override fun onProviderDisabled(s: String) {}
    }

    override fun onBackPressFragment() = false

    private var fragmentWeatherBinding: FragmentWeatherBinding? = null
    private val binding get() = fragmentWeatherBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentWeatherBinding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentWeatherBinding = null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        toggleUpdates()

        setupViewModelAndObserve()
    }

    private fun setupViewModelAndObserve(){
        val observer = Observer<Result?> {
            binding.zeeLoader.showView(false)
            binding.txtWeather.text = it?.let{ it.main }?.let { main -> main.temp }?.let { temp -> temp.roundToInt().toString() }?: kotlin.run { "" }
            binding.txtWeather.showView(true)
            binding.txtMetric.showView(true)
        }

        viewModel.getWeatherLiveData().observe(viewLifecycleOwner, observer)
    }

    private fun checkLocation(): Boolean {
        if (!isLocationEnabled())
            showAlert()
        return isLocationEnabled()
    }

    private fun showAlert() {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(context!!)
        dialog.setTitle("Enable Location")
            .setMessage("Su ubicación esta desactivada.\npor favor active su ubicación usa esta app")
            .setPositiveButton("Configuración de ubicación") { _, _ ->
                var myIntent: Intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent) }
            .setNegativeButton("Cancelar") { _, _ -> }
        dialog.show()
    }

    private fun isLocationEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun toggleUpdates() {
        if (!checkLocation())
            return

        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) { }
            else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
            }
        } else {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0,
                10.0f,
                locationListener)
        }
    }
}