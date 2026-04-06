package com.example.myapplication

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DeviceViewModel(private val repository: DeviceRepository) : ViewModel() {
    var devices by mutableStateOf(emptyList<DeviceData>())
        private set
    var isLoading by mutableStateOf(true)
        private set

    init {
        fetchDevices()
    }

    private fun fetchDevices() {
        viewModelScope.launch {
            try {
                devices = repository.getDevices()
            } catch (e: Exception) {
                Log.e("DeviceAPI", "Error fetching devices", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun getDeviceById(id: String?): DeviceData? {
        return devices.find { it.id == id }
    }
}
