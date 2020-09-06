package com.example.photoalarm.ui.view

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
import com.example.photoalarm.data.models.Welcome
import com.example.photoalarm.databinding.FragmentWeatherBinding
import com.example.photoalarm.ui.view.customs.PhotoAlarmFragment
import com.example.photoalarm.ui.view.interfaces.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt

private const val MY_PERMISSIONS_REQUEST_LOCATION = 5254
private const val END_POINT = "https://api.openweathermap.org/data/2.5/"

class WeatherFragment : PhotoAlarmFragment() {

    private val apiId = "3cfc1d5c1a8a4e9709fd07398c77d1af"

    private lateinit var service: ApiService

    private lateinit var locationManager: LocationManager
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private val locationListener: LocationListener = object : LocationListener {
        @SuppressLint("SetTextI18n")
        override fun onLocationChanged(location: Location) {
            longitude = location.longitude
            latitude = location.latitude
            getWeather(latitude.toString(), longitude.toString())
        }

        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
        override fun onProviderEnabled(s: String) {}
        override fun onProviderDisabled(s: String) {}
    }

    override fun onBackPressFragment() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(END_POINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(ApiService::class.java)
    }

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
    }

    private fun getWeather(lat: String, lon: String){
        service.getWeather(lat, lon, apiId, "metric").enqueue(object : Callback<Welcome> {
            override fun onResponse(call: Call<Welcome>, response: Response<Welcome>) {
                if (response.isSuccessful) {
                    binding.zeeLoader.showView(false)
                    binding.txtWeather.text = (response.body()!!.main.temp.roundToInt()).toString()
                    binding.txtWeather.showView(true)
                    binding.txtMetric.showView(true)
                    println("body token: ${response.body()}")
                } else {
                    println("response token: $response")
                }
            }

            override fun onFailure(call: Call<Welcome>, t: Throwable) {
                println("el error es el siguiente ${t.message}")
            }
        })
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