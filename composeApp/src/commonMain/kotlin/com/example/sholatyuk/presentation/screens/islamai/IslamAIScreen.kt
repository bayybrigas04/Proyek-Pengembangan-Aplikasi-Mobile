package com.example.sholatyuk.presentation.screens.islamai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sholatyuk.presentation.screens.home.BottomNavigationBar
import com.example.sholatyuk.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IslamAIScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToShalat: () -> Unit = {},
    onNavigateToDoa: () -> Unit = {}
) {
    var messageText by remember { mutableStateOf("") }

    // Placeholder pesan sebelum disambung ke ViewModel
    val messages = remember {
        mutableStateListOf(
            ChatMessageUi("Assalamu'alaikum. Saya IslamAI, asisten virtual yang siap membantu Anda menjawab pertanyaan seputar agama Islam. Ada yang bisa saya bantu?", true)
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "IslamAI",
                        color = TextWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DeepBlue
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "islamAI",
                onHomeClick = onNavigateToHome,
                onShalatClick = onNavigateToShalat,
                onDoaClick = onNavigateToDoa,
                onIslamAIClick = {}
            )
        },
        containerColor = DeepBlue
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { msg ->
                    ChatBubble(message = msg)
                }
            }

            // Bagian Input Teks & Tombol Kirim
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DeepBlue)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Tanya seputar Islam...", color = TextWhite.copy(alpha = 0.5f)) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LightTeal,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        cursorColor = AccentYellow
                    ),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            messages.add(ChatMessageUi(messageText, false))
                            // TODO: Di Sprint berikutnya ini akan disambung ke ViewModel
                            messageText = ""
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(LightTeal)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Kirim",
                        tint = TextWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

// Data class sementara untuk UI
data class ChatMessageUi(val text: String, val isAi: Boolean)

// Komponen chat bubble (balon pesan)
@Composable
fun ChatBubble(message: ChatMessageUi) {
    val backgroundColor = if (message.isAi) Color.White.copy(alpha = 0.1f) else LightTeal
    val alignment = if (message.isAi) Alignment.CenterStart else Alignment.CenterEnd
    val shape = if (message.isAi) {
        RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp)
    } else {
        RoundedCornerShape(16.dp, 16.dp, 4.dp, 16.dp)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .clip(shape)
                .background(backgroundColor)
                .padding(16.dp)
        ) {
            Text(
                text = message.text,
                color = TextWhite,
                fontSize = 15.sp,
                lineHeight = 22.sp
            )
        }
    }
}