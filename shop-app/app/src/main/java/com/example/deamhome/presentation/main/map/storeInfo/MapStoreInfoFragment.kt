package com.example.deamhome.presentation.main.map.storeInfo

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.deamhome.databinding.FragmentMapStoreInfoBinding
import com.example.deamhome.domain.model.GeoPoint
import com.example.deamhome.presentation.main.map.MapViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MapStoreInfoFragment : BottomSheetDialogFragment() {
    private val parentViewModel: MapViewModel by viewModels({ requireParentFragment() })
    private val viewModel: MapStoreInfoViewModel by viewModels()

    private var _binding: FragmentMapStoreInfoBinding? = null
    private val binding: FragmentMapStoreInfoBinding
        get() = _binding!!

    private val adapter: MapStoreInfoAdapter by lazy {
        MapStoreInfoAdapter(viewModel::onStoreCall, viewModel::onNavigatorStart)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMapStoreInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        setupViewModel()
        setupStoreAdapter()
    }

    private fun setupViewModel() {
        viewModel.event.observe(viewLifecycleOwner) { handleEvent(it) }
    }

    private fun setupStoreAdapter() {
        binding.rvStoreInfo.addItemDecoration(MapStoreAdapterItemDecoration(convertDpToPx(15f)))
        binding.rvStoreInfo.adapter = adapter
        adapter.setStores(parentViewModel.searchedStores)
    }

    private fun convertDpToPx(dp: Float): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (dp * density + 0.5f).toInt()
    }

    private fun handleEvent(event: MapStoreInfoViewModel.Event) {
        when (event) {
            is MapStoreInfoViewModel.Event.Dismiss -> {
                dismiss()
            }

            is MapStoreInfoViewModel.Event.NavigateToCall -> {
                navigateToCall(event.phoneNumber)
            }

            is MapStoreInfoViewModel.Event.NavigateToDestination -> {
                parentViewModel.lastUserPoint.value?.let {
                    navigateToDestination(it, event.destination)
                }
            }
        }
    }

    private fun navigateToCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        startActivity(intent)
    }

    private fun navigateToDestination(start: GeoPoint, destination: GeoPoint) {
        val url: String = MAP_URI.format(
            start.latitude,
            start.longitude,
            destination.latitude,
            destination.longitude,
        )
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        val list = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().packageManager.queryIntentActivities(
                Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER),
                PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong()),
            )
        } else {
            @Suppress("DEPRECATION")
            requireActivity().packageManager.queryIntentActivities(
                Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER),
                PackageManager.GET_META_DATA,
            )
        }

        if (list.isEmpty()) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(MAP_URI_IN_PLAY_STORE)))
        } else {
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val MAP_URI = "kakaomap://route?sp=%f,%f&ep=%f,%f&by=FOOT"
        private const val MAP_URI_IN_PLAY_STORE = "market://details?id=net.daum.android.map"
    }
}
