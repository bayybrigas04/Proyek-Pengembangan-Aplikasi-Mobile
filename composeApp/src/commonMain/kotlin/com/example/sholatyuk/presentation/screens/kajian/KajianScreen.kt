package com.example.sholatyuk.presentation.screens.kajian

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sholatyuk.domain.model.KajianCategory
import com.example.sholatyuk.domain.model.KajianNote
import com.example.sholatyuk.presentation.theme.*
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KajianScreen(
    onNavigateBack: () -> Unit,
    viewModel: KajianViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var noteToEdit by remember { mutableStateOf<KajianNote?>(null) }
    var noteToDelete by remember { mutableStateOf<KajianNote?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catatan Kajian Islam", color = TextWhite, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = AccentYellow)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepBlue)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = AccentYellow,
                contentColor = DeepBlue,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Catatan")
            }
        },
        containerColor = DeepBlue
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(DarkTeal, DeepBlue)
                    )
                )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Search Bar
                SearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = viewModel::onSearchQueryChange
                )

                // Category Stats
                CategoryStatsRow(
                    notes = uiState.notes,
                    selectedCategory = uiState.selectedCategory,
                    onCategoryClick = viewModel::onCategorySelect
                )

                // Notes Grid
                if (uiState.notes.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Belum ada catatan kajian.", color = TextWhite.copy(alpha = 0.5f))
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.notes, key = { it.id }) { note ->
                            KajianNoteCard(
                                note = note,
                                onEdit = { noteToEdit = it },
                                onDelete = { noteToDelete = it }
                            )
                        }
                    }
                }
            }
        }
    }

    // Dialogs
    if (showAddDialog) {
        AddEditNoteDialog(
            onDismiss = { showAddDialog = false },
            onSave = { judul, ustadz, tanggal, kategori, isi ->
                viewModel.addNote(judul, ustadz, tanggal, kategori, isi)
                showAddDialog = false
            }
        )
    }

    noteToEdit?.let { note ->
        AddEditNoteDialog(
            note = note,
            onDismiss = { noteToEdit = null },
            onSave = { judul, ustadz, tanggal, kategori, isi ->
                viewModel.updateNote(note.copy(judul = judul, ustadz = ustadz, tanggal = tanggal, kategori = kategori, isi = isi))
                noteToEdit = null
            }
        )
    }

    noteToDelete?.let { note ->
        AlertDialog(
            onDismissRequest = { noteToDelete = null },
            title = { Text("Hapus Catatan") },
            text = { Text("Apakah Anda yakin ingin menghapus catatan '${note.judul}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteNote(note.id)
                        noteToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Hapus", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { noteToDelete = null }) {
                    Text("Batal", color = TextWhite.copy(alpha = 0.6f))
                }
            },
            containerColor = CardBackground,
            titleContentColor = TextWhite,
            textContentColor = TextWhite.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp)),
        placeholder = { Text("Cari judul atau ustadz...", color = TextWhite.copy(alpha = 0.5f)) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AccentYellow) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = CardBackground,
            unfocusedContainerColor = CardBackground,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = TextWhite,
            unfocusedTextColor = TextWhite
        ),
        singleLine = true
    )
}

@Composable
fun CategoryStatsRow(
    notes: List<KajianNote>,
    selectedCategory: KajianCategory?,
    onCategoryClick: (KajianCategory?) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            CategoryChip(
                name = "Semua",
                count = notes.size,
                isSelected = selectedCategory == null,
                onClick = { onCategoryClick(null) }
            )
        }
        items(KajianCategory.entries) { category ->
            val count = notes.count { it.kategori == category }
            CategoryChip(
                name = category.displayName,
                count = count,
                isSelected = selectedCategory == category,
                onClick = { onCategoryClick(category) }
            )
        }
    }
}

@Composable
fun CategoryChip(name: String, count: Int, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = if (isSelected) AccentYellow else CardBackground,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                color = if (isSelected) DeepBlue else TextWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isSelected) DeepBlue else AccentYellow.copy(alpha = 0.2f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = count.toString(),
                    color = AccentYellow,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
fun KajianNoteCard(
    note: KajianNote,
    onEdit: (KajianNote) -> Unit,
    onDelete: (KajianNote) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        border = BorderStroke(1.dp, AccentYellow.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(note.kategori.icon, fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = note.judul,
                        color = TextWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = note.ustadz,
                        color = AccentYellow,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.isi,
                color = TextWhite.copy(alpha = 0.7f),
                fontSize = 11.sp,
                maxLines = 2,
                lineHeight = 16.sp,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = note.tanggal,
                    color = TextWhite.copy(alpha = 0.4f),
                    fontSize = 10.sp
                )
                Row {
                    IconButton(onClick = { onEdit(note) }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = AccentYellow, modifier = Modifier.size(16.dp))
                    }
                    IconButton(onClick = { onDelete(note) }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color.Red.copy(alpha = 0.7f), modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteDialog(
    note: KajianNote? = null,
    onDismiss: () -> Unit,
    onSave: (String, String, String, KajianCategory, String) -> Unit
) {
    var judul by remember { mutableStateOf(note?.judul ?: "") }
    var ustadz by remember { mutableStateOf(note?.ustadz ?: "") }
    var tanggal by remember { mutableStateOf(note?.tanggal ?: "") }
    var kategori by remember { mutableStateOf(note?.kategori ?: KajianCategory.AQIDAH) }
    var isi by remember { mutableStateOf(note?.isi ?: "") }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = { if (judul.isNotBlank()) onSave(judul, ustadz, tanggal, kategori, isi) },
                colors = ButtonDefaults.buttonColors(containerColor = AccentYellow),
                enabled = judul.isNotBlank()
            ) {
                Text("Simpan", color = DeepBlue, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = TextWhite.copy(alpha = 0.6f))
            }
        },
        title = {
            Text(
                text = if (note == null) "Tambah Catatan" else "Edit Catatan",
                color = TextWhite,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NoteTextField(value = judul, onValueChange = { judul = it }, label = "Judul Kajian")
                NoteTextField(value = ustadz, onValueChange = { ustadz = it }, label = "Ustadz/Pemateri")
                NoteTextField(value = tanggal, onValueChange = { tanggal = it }, label = "Tanggal (DD/MM/YYYY)")
                
                // Category Dropdown
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedCard(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.outlinedCardColors(containerColor = DeepBlue),
                        border = BorderStroke(1.dp, AccentYellow.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${kategori.icon} ${kategori.displayName}", color = TextWhite)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = AccentYellow)
                        }
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(CardBackground).fillMaxWidth(0.7f)
                    ) {
                        KajianCategory.entries.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text("${cat.icon} ${cat.displayName}", color = TextWhite) },
                                onClick = {
                                    kategori = cat
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                NoteTextField(value = isi, onValueChange = { isi = it }, label = "Isi Catatan", singleLine = false, minLines = 3)
            }
        },
        containerColor = CardBackground
    )
}

@Composable
fun NoteTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = AccentYellow.copy(alpha = 0.6f)) },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextWhite,
            unfocusedTextColor = TextWhite,
            focusedBorderColor = AccentYellow,
            unfocusedBorderColor = AccentYellow.copy(alpha = 0.3f),
            cursorColor = AccentYellow
        ),
        singleLine = singleLine,
        minLines = minLines,
        shape = RoundedCornerShape(8.dp)
    )
}