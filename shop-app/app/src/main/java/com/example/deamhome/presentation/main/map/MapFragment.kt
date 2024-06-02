package com.example.deamhome.presentation.main.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import com.example.deamhome.R
import com.example.deamhome.common.base.BindingFragment
import com.example.deamhome.common.view.Toaster
import com.example.deamhome.common.view.showSnackbar
import com.example.deamhome.databinding.FragmentMapBinding
import com.example.deamhome.domain.model.GeoPoint
import com.example.deamhome.domain.model.MapStoreInfo
import com.example.deamhome.domain.model.Trash
import com.example.deamhome.presentation.main.map.storeInfo.MapStoreInfoFragment
import com.example.deamhome.presentation.main.mypage.MyPageFragment
import com.example.deamhome.presentation.main.qr.QRActivity
import com.example.deamhome.presentation.main.qr.ScanActivity
import com.example.throw_fornt.util.common.ProgressDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import net.daum.mf.map.api.MapCircle
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapFragment : BindingFragment<FragmentMapBinding>(R.layout.fragment_map) {

    @SuppressLint("MissingPermission")
    private val locationResultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                requestLocationUpdateService()
                return@registerForActivityResult
            }
            if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                requestLocationUpdateService()
                showSnackBar(R.string.map_location_permission_upgrade_require_message) { navigateToPermissionSetting() }
            } else {
                showSnackBar(R.string.map_location_permission_require_message) { navigateToPermissionSetting() }
            }
        }

    private val viewModel: MapViewModel by viewModels { MapViewModel.Factory }

    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog(requireContext())
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var curPositionMarker: MapPOIItem? = null

    private var nearByStoreMarkers: Array<MapPOIItem> = arrayOf()

    private var mapCircle: MapCircle? = null

    private lateinit var markerEventListener: MarkerEventListener

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                Log.d(
                    "mendel",
                    "제공자: ${location.provider}, ${location.latitude}, ${location.longitude}",
                )
                viewModel.updateCurPosition(GeoPoint(location.latitude, location.longitude))
                break
            }
        }
    }

    private val locationRequest = LocationRequest.create().apply {
        interval = 2000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        markerEventListener = MarkerEventListener(requireContext(), viewModel::selectMapPoint)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.mapView.setPOIItemEventListener(markerEventListener)
        setupFilterSearchChips()
        checkLocationPermission()
        setupViewModel()
    }

    private fun setupFilterSearchChips() {
        Trash.values().forEach { type ->
            binding.cgTrashFilterChipGroup.addView(
                Chip(requireContext()).apply {
                    setChipDrawable(
                        ChipDrawable.createFromAttributes(
                            requireContext(),
                            null,
                            0,
                            R.style.CustomChipChoice,
                        ),
                    )
                    text = type.type
                    isChecked = viewModel.selectedTrashTypes.value?.contains(type) ?: false
                    setOnClickListener { viewModel.changeCheckedState(type, this.isChecked) }
                },
            )
        }
    }

    private fun setupViewModel() {
        viewModel.event.observe(viewLifecycleOwner) { handleEvent(it) }
        viewModel.curCameraCenterPoint.observe(viewLifecycleOwner) {
            val mapPoint = MapPoint.mapPointWithGeoCoord(it.latitude, it.longitude)
            binding.mapView.setMapCenterPoint(mapPoint, true)
            setMinDistance()
        }
        viewModel.lastUserPoint.observe(viewLifecycleOwner) {
            val mapPoint = MapPoint.mapPointWithGeoCoord(it.latitude, it.longitude)
            if (curPositionMarker == null) {
                curPositionMarker = MapPOIItem().apply {
                    itemName = "현재 위치"
                    markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.
                    selectedMarkerType =
                        MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                    this.mapPoint = mapPoint
                    markerType = MapPOIItem.MarkerType.CustomImage // 마커타입을 커스텀 마커로 지정.
                    customImageResourceId = R.drawable.ic_map_pin_user_24
                    isCustomImageAutoscale = false // 항상 같은 크기로 보이도록
                    setCustomImageAnchor(0.5f, 1.0f)
                }
                binding.mapView.addPOIItem(curPositionMarker)
                binding.mapView.setZoomLevel(1, true)
            } else {
                curPositionMarker?.mapPoint = mapPoint
            }
        }
        viewModel.nearByStores.observe(viewLifecycleOwner) {
            Log.d("mendel", "가게 정보들: $it")
            binding.mapView.removePOIItems(nearByStoreMarkers)
            nearByStoreMarkers = it.convertStoreInfoToMarkers()
            binding.mapView.addPOIItems(nearByStoreMarkers)
            changeMapCircle()
        }

        viewModel.selectedTrashTypes.observe(viewLifecycleOwner) { selectedTypes ->
            val typeNames = selectedTypes.map { it.type }
            binding.cgTrashFilterChipGroup.forEach { view ->
                val chip = view as Chip
                chip.isChecked = (chip.text.toString() in typeNames)
            }
            viewModel.refreshNearbyStores()
        }

        viewModel.refreshStoreLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                progressDialog.startWithAnimation(R.raw.loading)
            } else {
                if (progressDialog.isShowing) {
                    progressDialog.dismiss()
                }
            }
        }
    }

    private fun changeMapCircle() {
        if (mapCircle != null) binding.mapView.removeCircle(mapCircle)
        mapCircle = MapCircle(
            MapPoint.mapPointWithGeoCoord(
                viewModel.lastUserPoint.value!!.latitude,
                viewModel.lastUserPoint.value!!.longitude,
            ), // center
            viewModel.selectedSearchDistance.value!! * 1000, // radius

            Color.argb(128, 255, 0, 0), // strokeColor
            Color.argb(20, 0, 0, 0), // fillColor
        )
        binding.mapView.addCircle(mapCircle)
    }

    private fun handleEvent(event: MapViewModel.Event) {
        when (event) {
            is MapViewModel.Event.ShowMapStoreInfo -> {
                MapStoreInfoFragment().show(childFragmentManager, MAP_STORE_INFO_TAG)
            }

            is MapViewModel.Event.ToastMessage -> {
                Toaster.showShort(
                    requireContext(),
                    event.message,
                )
            }

            is MapViewModel.Event.NavigateToQR -> {
                startActivity(ScanActivity.getIntent(requireContext()))
            }
        }
    }

    private fun setMinDistance() {
        val bounds = binding.mapView.mapPointBounds
        val bl = bounds.bottomLeft.mapPointGeoCoord
        val tr = bounds.topRight.mapPointGeoCoord
        viewModel.setVisibleMapDistance(
            GeoPoint(bl.latitude, bl.longitude),
            GeoPoint(tr.latitude, tr.longitude),
        )
    }

    private fun Map<Int, List<MapStoreInfo>>.convertStoreInfoToMarkers(): Array<MapPOIItem> {
        return map { samePointStores ->
            val mapPoint = MapPoint.mapPointWithGeoCoord(
                samePointStores.value[0].geoPoint.latitude,
                samePointStores.value[0].geoPoint.longitude,
            )
            MapPOIItem().apply {
                tag = samePointStores.key
                itemName = samePointStores.value[0].storeName.let {
                    if (samePointStores.value.size > 1) {
                        it + "외 ${samePointStores.value.size - 1}개"
                    } else {
                        it
                    }
                }
                selectedMarkerType = MapPOIItem.MarkerType.RedPin
                this.mapPoint = mapPoint
                markerType = MapPOIItem.MarkerType.CustomImage
                customImageResourceId = R.drawable.ic_map_open_store_24
                isCustomImageAutoscale = false // 항상 같은 크기로 보이도록
                setCustomImageAnchor(0.5f, 1.0f)
            }
        }.toTypedArray()
    }

    override fun onStart() {
        super.onStart()
        requestLocationUpdateService()
    }

    override fun onStop() {
        super.onStop()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        curPositionMarker = null
        mapCircle = null
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdateService() {
        if (isAllowedLocationPermission().not()) return
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper(),
        )
    }

    // 위치 권한 관련 로직들
    private fun isAllowedLocationPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }

    private fun checkLocationPermission() {
        if (checkLocationService().not()) {
            showToast(R.string.map_location_service_turn_on_require_message)
        }

        // 둘 중에 한 개도 부여된 것이 없다면,,
        if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                showSnackBar(R.string.map_location_permission_require_message) { navigateToPermissionSetting() }
            } else {
                locationResultLauncher.launch(REQUIRE_LOCATION_PERMISSIONS)
            }
        } else {
            if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                showSnackBar(R.string.map_location_permission_upgrade_require_message) { navigateToPermissionSetting() }
            }
        }
    }

    private fun navigateToPermissionSetting() {
        Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", requireActivity().packageName, null)
            startActivity(this)
        }
    }

    // GPS가 켜져있는지 확인
    private fun checkLocationService(): Boolean {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun showSnackBar(@StringRes messageId: Int, action: () -> Unit) {
        binding.root.showSnackbar(messageId) { action() }
    }

    private fun showToast(@StringRes messageId: Int) {
        Toaster.showShort(requireContext(), requireContext().getString(messageId))
    }

    // 마커 클릭 이벤트 리스너
    class MarkerEventListener(val context: Context, private val onClick: (Int) -> Unit) :
        MapView.POIItemEventListener {
        override fun onPOIItemSelected(mapView: MapView?, poiItem: MapPOIItem?) {
            val groupTag = poiItem?.tag ?: return
            onClick(groupTag)
        }

        @Deprecated("Deprecated in Java")
        override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView?, poiItem: MapPOIItem?) {
        }

        override fun onCalloutBalloonOfPOIItemTouched(
            mapView: MapView?,
            poiItem: MapPOIItem?,
            buttonType: MapPOIItem.CalloutBalloonButtonType?,
        ) {
        }

        override fun onDraggablePOIItemMoved(
            mapView: MapView?,
            poiItem: MapPOIItem?,
            mapPoint: MapPoint?,
        ) {
        }
    }

    companion object {
        private const val MAP_STORE_INFO_TAG = "fragment_map_store_info_tag"
        private val REQUIRE_LOCATION_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
        @JvmStatic
        fun newInstance() = MapFragment()
    }
}
