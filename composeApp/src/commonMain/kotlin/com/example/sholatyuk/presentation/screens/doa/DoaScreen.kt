package com.example.sholatyuk.presentation.screens.doa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sholatyuk.presentation.screens.home.BottomNavigationBar
import com.example.sholatyuk.presentation.theme.*
import com.example.sholatyuk.presentation.screens.profile.ProfileViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoaScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToShalat: () -> Unit = {},
    onNavigateToIslamAI: () -> Unit = {},
    viewModel: DoaViewModel = koinViewModel(),
    profileViewModel: ProfileViewModel = koinViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val doaList by viewModel.filteredDoaList.collectAsState()
    val categories = viewModel.categories
    val isLightModeEnabled by profileViewModel.isLightModeEnabled.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "doa",
                onHomeClick = onNavigateToHome,
                onShalatClick = onNavigateToShalat,
                onDoaClick = {},
                onIslamAIClick = onNavigateToIslamAI,
                isLightMode = isLightModeEnabled
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header
            Text(
                text = "Kumpulan Doa",
                color = if (isLightModeEnabled) Color.Black else TextWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp, start = 20.dp, end = 20.dp, bottom = 16.dp)
            )

            // 1. SEARCH BAR
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                placeholder = { Text("Cari doa (misal: tidur, masjid)...", color = Color.Gray) },
                leadingIcon = { 
                    Icon(
                        Icons.Default.Search, 
                        contentDescription = "Search", 
                        tint = if (isLightModeEnabled) DeepBlue else AccentYellow
                    ) 
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isLightModeEnabled) DeepBlue else AccentYellow,
                    unfocusedBorderColor = (if (isLightModeEnabled) Color.Black else Color.White).copy(alpha = 0.3f),
                    focusedContainerColor = if (isLightModeEnabled) Color.White else CardBackground,
                    unfocusedContainerColor = if (isLightModeEnabled) Color.White else CardBackground,
                    focusedTextColor = if (isLightModeEnabled) Color.Black else TextWhite,
                    unfocusedTextColor = if (isLightModeEnabled) Color.Black else TextWhite
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. FILTER CATEGORIES
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val isSelected = category == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.onCategorySelected(category) },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = if (isLightModeEnabled) DeepBlue else AccentYellow,
                            selectedLabelColor = if (isLightModeEnabled) Color.White else DeepBlue,
                            containerColor = if (isLightModeEnabled) Color.White else CardBackground,
                            labelColor = if (isLightModeEnabled) Color.Black else TextWhite
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) {
                                if (isLightModeEnabled) DeepBlue else AccentYellow
                            } else {
                                (if (isLightModeEnabled) Color.Black else Color.White).copy(alpha = 0.2f)
                            }
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. DAFTAR DOA
            if (doaList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text(
                        "Doa tidak ditemukan.", 
                        color = (if (isLightModeEnabled) Color.Black else TextWhite).copy(alpha = 0.5f)
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(doaList) { doa ->
                        DoaCard(doa, isLightModeEnabled)
                    }
                }
            }
        }
    }
}

@Composable
fun DoaCard(doa: Doa, isLightMode: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isLightMode) Color.White else CardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isLightMode) 2.dp else 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = doa.title,
                color = if (isLightMode) DeepBlue else AccentYellow,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = doa.arabic,
                color = if (isLightMode) Color.Black else TextWhite,
                fontSize = 24.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = 36.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = doa.translation,
                color = (if (isLightMode) Color.Black else TextWhite).copy(alpha = 0.7f),
                fontSize = 13.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }
    }
}