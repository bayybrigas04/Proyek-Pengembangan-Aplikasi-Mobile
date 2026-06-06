package com.example.sholatyuk.presentation.screens.prayer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sholatyuk.domain.model.ManualPrayer
import com.example.sholatyuk.presentation.screens.home.BottomNavigationBar
import com.example.sholatyuk.presentation.theme.*
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToIslamAI: () -> Unit = {},
    onNavigateToDoa: () -> Unit = {},
    viewModel: ManualPrayerViewModel = koinViewModel()
) {
    val manualPrayers by viewModel.manualPrayers.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var prayerToEdit by remember { mutableStateOf<ManualPrayer?>(null) }
    var prayerToDelete by remember { mutableStateOf<ManualPrayer?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Jadwal Shalat", color = TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.Settings, contentDescription = "Settings", tint = TextWhite)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent),
                modifier = Modifier.background(brush = Brush.verticalGradient(colors = listOf(DarkTeal, Color.Transparent)))
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "shalat",
                onHomeClick = onNavigateToHome,
                onShalatClick = {},
                onIslamAIClick = onNavigateToIslamAI,
                onDoaClick = onNavigateToDoa
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = AccentYellow,
                contentColor = DeepBlue
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Jadwal")
            }
        },
        containerColor = DeepBlue
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item { PrayerHeaderCard() }
            item { DateNavigationSection() }
            item { PrayerTimeList() }

            // SEKSI CRUD: Jadwal Manual
            if (manualPrayers.isNotEmpty()) {
                item {
                    Text(
                        "Jadwal Kustom",
                        color = AccentYellow,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 8.dp)
                    )
                }
                items(manualPrayers) { prayer ->
                    ManualPrayerItem(
                        prayer = prayer,
                        onEdit = { prayerToEdit = prayer },
                        onDelete = { prayerToDelete = prayer }
                    )
                }
            }
        }

        // Dialog Create
        if (showAddDialog) {
            PrayerFormDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { name, time, loc, hijri ->
                    viewModel.addManualPrayer(name, time, loc, hijri)
                    showAddDialog = false
                }
            )
        }

        // Dialog Update
        prayerToEdit?.let { prayer ->
            PrayerFormDialog(
                initialPrayer = prayer,
                onDismiss = { prayerToEdit = null },
                onConfirm = { name, time, loc, hijri ->
                    viewModel.updateManualPrayer(prayer.id, name, time, loc, hijri)
                    prayerToEdit = null
                }
            )
        }

        // Dialog Delete (Confirmation)
        prayerToDelete?.let { prayer ->
            AlertDialog(
                onDismissRequest = { prayerToDelete = null },
                title = { Text("Hapus Jadwal", fontWeight = FontWeight.Bold) },
                text = { Text("Hapus jadwal '${prayer.namaSholat}'?") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteManualPrayer(prayer.id)
                            prayerToDelete = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) { Text("Hapus", color = Color.White) }
                },
                dismissButton = {
                    TextButton(onClick = { prayerToDelete = null }) { Text("Batal", color = DeepBlue) }
                },
                containerColor = Color.White,
                titleContentColor = DeepBlue,
                textContentColor = Color.DarkGray
            )
        }
    }
}

@Composable
fun ManualPrayerItem(prayer: ManualPrayer, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Timer, null, tint = LightTeal)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(prayer.namaSholat, color = TextWhite, fontWeight = FontWeight.Bold)
                Text("${prayer.lokasi} • ${prayer.tanggalHijriah}", color = TextWhite.copy(0.6f), fontSize = 12.sp)
            }
            Text(prayer.waktu, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            IconButton(onClick = onEdit) { Icon(Icons.Outlined.Edit, null, tint = TextWhite.copy(0.6f)) }
            IconButton(onClick = onDelete) { Icon(Icons.Outlined.Delete, null, tint = Color.Red.copy(0.6f)) }
        }
    }
}

@Composable
fun PrayerFormDialog(
    initialPrayer: ManualPrayer? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialPrayer?.namaSholat ?: "") }
    var time by remember { mutableStateOf(initialPrayer?.waktu ?: "") }
    var loc by remember { mutableStateOf(initialPrayer?.lokasi ?: "") }
    var hijri by remember { mutableStateOf(initialPrayer?.tanggalHijriah ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialPrayer == null) "Tambah Jadwal" else "Edit Jadwal", color = DeepBlue) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nama Sholat") })
                OutlinedTextField(value = time, onValueChange = { time = it }, label = { Text("Waktu (misal 04:30)") })
                OutlinedTextField(value = loc, onValueChange = { loc = it }, label = { Text("Lokasi") })
                OutlinedTextField(value = hijri, onValueChange = { hijri = it }, label = { Text("Tanggal Hijriah") })
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(name, time, loc, hijri) }, enabled = name.isNotBlank() && time.isNotBlank()) {
                Text("Simpan")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } },
        containerColor = Color.White
    )
}

// UI yang sudah ada sebelumnya
@Composable fun PrayerHeaderCard() { /* ... kode Anda ... */ }
@Composable fun DateNavigationSection() { /* ... kode Anda ... */ }
@Composable fun PrayerTimeList() { /* ... kode Anda ... */ }
@Composable fun PrayerListItem(name: String, time: String, icon: ImageVector, isAlarmOn: Boolean, isSpecial: Boolean = false, isRed: Boolean = false) { /* ... kode Anda ... */ }
