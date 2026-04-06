package com.example.myapplication.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.myapplication.DeviceData
import com.example.myapplication.R

@Composable
fun DeviceDetailView(device: DeviceData, onBack: () -> Unit) {
    val id = device.id ?: ""
    val imageDefault = device.image?.default ?: ""
    val imageUrl = "https://images.svc.ui.com/?u=https%3A%2F%2Fstatic.ui.com%2Ffingerprint%2Fui%2Fimages%2F$id%2Fdefault%2F$imageDefault.png&w=640&q=75"

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFFBFBFB),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button matching the circular background image
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    modifier = Modifier
                        .size(44.dp)
                        .shadow(elevation = 2.dp, shape = CircleShape)
                        .clickable { onBack() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back",
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = device.product?.name ?: "Unknown",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Spacer(modifier = Modifier.weight(1.2f))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(256.dp)
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(24.dp))
                    .padding(16.dp),
                contentScale = ContentScale.Fit,
                loading = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    DetailRow("Product line", device.line?.name ?: "N/A", isGreen = true)
                    HorizontalDivider(modifier = Modifier.padding(start = 16.dp), thickness = 0.5.dp, color = Color(0xFFEEEEEE))
                    DetailRow("ID", device.line?.id ?: "N/A", isGreen = true)
                    HorizontalDivider(modifier = Modifier.padding(start = 16.dp), thickness = 0.5.dp, color = Color(0xFFEEEEEE))
                    DetailRow("Name", device.product?.name ?: "N/A")
                    HorizontalDivider(modifier = Modifier.padding(start = 16.dp), thickness = 0.5.dp, color = Color(0xFFEEEEEE))
                    DetailRow("Short name", device.shortnames?.joinToString(", ") ?: "N/A")
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, isGreen: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color(0xFF333333),
            modifier = Modifier.width(120.dp)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            color = if (isGreen) Color(0xFF2ECC71) else Color(0xFF999999),
            fontWeight = if (isGreen) FontWeight.Medium else FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
    }
}