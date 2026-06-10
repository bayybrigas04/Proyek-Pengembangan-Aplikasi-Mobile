package com.example.sholatyuk.presentation.screens.prayer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sholatyuk.presentation.screens.home.BottomNavigationBar
import com.example.sholatyuk.presentation.theme.*
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToDoa: () -> Unit = {},
    onNavigateToIslamAI: () -> Unit = {},
    viewModel: PrayerViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Jadwal Shalat", color = TextWhite, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DeepBlue)
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "shalat",
                onHomeClick = onNavigateToHome,
                onShalatClick = {},
                onDoaClick = onNavigateToDoa,
                onIslamAIClick = onNavigateToIslamAI
            )
        },
        containerColor = DeepBlue
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(DarkTeal, DeepBlue),
                        startY = 0f,
                        endY = 1200f
                    )
                )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                // Tampilan Loading atau Error
                if (uiState.isLoading) {
                    item {
                        Text(
                            text = "Mencari lokasi...",
                            color = AccentYellow,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                } else if (uiState.error != null) {
                    item {
                        Text(
                            text = uiState.error ?: "Terjadi kesalahan",
                            color = Color.Red.copy(alpha = 0.8f),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Tampilan Jadwal Sholat
                uiState.prayerTime?.let { time ->
                    item {
                        Column(modifier = Modifier.padding(bottom = 8.dp)) {
                            Text(
                                text = "Lokasi: ${time.cityName}",
                                color = TextWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Tanggal: ${time.date}",
                                color = TextWhite.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )
                        }
                    }

                    item { PrayerTimeRow("Imsak", time.imsak) }
                    item { PrayerTimeRow("Subuh", time.fajr) }
                    item { PrayerTimeRow("Dzuhur", time.dhuhr) }
                    item { PrayerTimeRow("Ashar", time.asr) }
                    item { PrayerTimeRow("Maghrib", time.maghrib) }
                    item { PrayerTimeRow("Isya", time.isha) }
                }
                
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
fun PrayerTimeRow(name: String, time: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.08f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                color = TextWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = time,
                color = AccentYellow,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}