package com.example.myapplication

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeviceRepository(private val deviceService: DeviceService) {

    suspend fun getDevices(): List<DeviceData> = withContext(Dispatchers.IO) {
        try {
            val response = deviceService.getDevices()
            response.devices ?: emptyList()
        } catch (e: Exception) {
            // In a professional app, you might want to wrap this in a Result object
            // or throw a custom exception. For now, returning empty list on error
            // to maintain consistency with existing ViewModel logic.
            throw e
        }
    }
}
