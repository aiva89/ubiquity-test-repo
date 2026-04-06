package com.example.myapplication.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.DeviceData
import com.example.myapplication.R
import kotlin.collections.contains
import kotlin.collections.set
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

@Composable
fun DeviceDashboard(
    modifier: Modifier = Modifier,
    devices: List<DeviceData>,
    isLoading: Boolean,
    onDeviceClick: (DeviceData) -> Unit
) {
    var selectedTab by remember { mutableStateOf("All") }

    // Popup state
    var showFilterMenu by remember { mutableStateOf(false) }
    val filterOptions = listOf("UniFi", "UniFi LTE", "UniFi Protect", "UniFi Access")
    val selectedFilters = remember { mutableStateMapOf<String, Boolean>().apply {
        filterOptions.forEach { put(it, false) }
    } }


    val filteredDevices = remember(devices, selectedTab, selectedFilters.toMap()) {
        val tabFiltered = when (selectedTab) {
            "Cameras" -> devices.filter {
                it.line?.id?.lowercase() == "unifi-protect"
            }
            "IoT" -> devices.filter {
                it.line?.id?.lowercase() in listOf("unifi-led", "unifi-connect", "unifi-access", "smart-power")
            }
            else -> devices
        }

        val activeFilters = selectedFilters.filter { it.value }.keys
        if (activeFilters.isEmpty()) {
            tabFiltered
        } else {
            tabFiltered.filter { it.line?.name in activeFilters }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Top row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Main Tab Pill container
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf("All", "Cameras", "IoT").forEach { tab ->
                        val isSelected = selectedTab == tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(
                                    color = if (isSelected) Color(0xFFEBEBEB) else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedTab = tab },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab,
                                color = if (isSelected) Color(0xFF007AFF) else Color(0xFF555555),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Filter button
            Box {
                Surface(
                    modifier = Modifier
                        .size(44.dp)
                        .shadow(elevation = 2.dp, shape = CircleShape)
                        .clickable { showFilterMenu = true },
                    shape = CircleShape,
                    color = Color.White
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.filter),
                            contentDescription = "Filter Button",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                DropdownMenu(
                    expanded = showFilterMenu,
                    onDismissRequest = { showFilterMenu = false },
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(24.dp))
                        .width(240.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    filterOptions.forEach { name ->
                        val isChecked = selectedFilters[name] ?: false
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = null, // Handled by onClick
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = Color(0xFF007AFF)
                                        )
                                    )
                                    Text(
                                        text = name,
                                        modifier = Modifier.padding(start = 8.dp),
                                        fontSize = 16.sp,
                                        color = Color(0xFF333333)
                                    )
                                }
                            },
                            onClick = {
                                selectedFilters[name] = !isChecked
                            },
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Device List
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(24.dp),
            color = Color.White
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (filteredDevices.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No devices found in this category", color = Color.Gray)
                }
            } else {
                val listState = rememberLazyListState()
                LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
                    itemsIndexed(filteredDevices) { index, device ->
                        DeviceItem(
                            device = device,
                            onClick = { onDeviceClick(device) }
                        )
                        if (index < filteredDevices.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(start = 16.dp),
                                thickness = 0.5.dp,
                                color = Color.LightGray.copy(alpha = 0.5f)
                            )
                        }
                    }
                }

                val showFade by remember {
                    derivedStateOf {
                        val layoutInfo = listState.layoutInfo
                        val visibleItems = layoutInfo.visibleItemsInfo

                        if (visibleItems.isEmpty()) {
                            false
                        } else {
                            val lastVisibleItem = visibleItems.last()
                            // Show fade if the last visible item index is less than the total item count - 1
                            lastVisibleItem.index < layoutInfo.totalItemsCount - 1
                        }
                    }
                }

                AnimatedVisibility(
                    visible = showFade,
                    enter = fadeIn(), // Default fade in transition
                    exit = fadeOut(), // Default fade out transition
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.White
                                    )
                                )
                            )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun DeviceItem(device: DeviceData, onClick: () -> Unit) {
    val id = device.id ?: ""
    val imageDefault = device.image?.default ?: ""
    val imageUrl = "https://images.svc.ui.com/?u=https%3A%2F%2Fstatic.ui.com%2Ffingerprint%2Fui%2Fimages%2F$id%2Fdefault%2F$imageDefault.png&w=128&q=75"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(28.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = device.product?.name ?: "N/A",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray,
            modifier = Modifier.weight(1f)
        )
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.ic_list),
                contentDescription = "List chevron",
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
