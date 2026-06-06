package com.example.sholatyuk.data.remote.api

import com.example.sholatyuk.core.network.ApiConfig
import com.example.sholatyuk.data.remote.dto.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class GeminiService(private val client: HttpClient) {

    suspend fun generateContent(prompt: String, systemPrompt: String? = null): Result<String> {
        val modelVersion = "gemini-1.5-flash"
        val apiKey = ApiConfig.geminiApiKey
        val url = "https://generativelanguage.googleapis.com/v1beta/models/$modelVersion:generateContent?key=$apiKey"

        val defaultSystemPrompt = """
            Anda adalah IslamAI, asisten virtual Islami yang dikembangkan untuk aplikasi SholatYuk. 
            Tugas Anda adalah membantu pengguna menjawab pertanyaan seputar agama Islam berdasarkan pemahaman Ahlussunnah wal Jama'ah.
        """.trimIndent()

        val requestBody = GeminiRequest(
            systemInstruction = GeminiSystemInstruction(
                parts = listOf(GeminiPart(text = systemPrompt ?: defaultSystemPrompt))
            ),
            contents = listOf(
                GeminiContent(
                    role = "user",
                    parts = listOf(GeminiPart(text = prompt))
                )
            )
        )

        return try {
            val response: GeminiResponse = client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }.body()

            if (response.error != null) {
                Result.failure(Exception(response.error.message))
            } else {
                val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: "Maaf, saya tidak dapat merangkai jawaban saat ini."
                Result.success(text)
            }
        } catch (e: Exception) {
            Result.failure(Exception("Gagal terhubung ke server: ${e.message}"))
        }
    }

    suspend fun generateResponse(prompt: String): String {
        return generateContent(prompt).getOrElse { "Maaf, terjadi kesalahan: ${it.message}" }
    }
}
