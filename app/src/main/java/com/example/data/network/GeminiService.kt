package com.example.data.network

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiService {
    private const val TAG = "GeminiService"
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    suspend fun generateContent(
        prompt: String,
        systemInstruction: String = ""
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.e(TAG, "Gemini API Key is empty or placeholder! Please enter your key in the Secrets panel.")
            return@withContext "Error: Gemini API Key is missing. Please add GEMINI_API_KEY to the Secrets panel in AI Studio to continue."
        }

        try {
            // Build request JSON
            val requestBodyJson = JSONObject().apply {
                // contents
                val contentsArray = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        put("role", "user")
                        val partsArray = JSONArray().apply {
                            val partObj = JSONObject().apply {
                                put("text", prompt)
                            }
                            put(partObj)
                        }
                        put("parts", partsArray)
                    }
                    put(contentObj)
                }
                put("contents", contentsArray)

                // systemInstruction
                if (systemInstruction.isNotEmpty()) {
                    val systemInstructionObj = JSONObject().apply {
                        val partsArray = JSONArray().apply {
                            val partObj = JSONObject().apply {
                                put("text", systemInstruction)
                            }
                            put(partObj)
                        }
                        put("parts", partsArray)
                    }
                    put("systemInstruction", systemInstructionObj)
                }

                // generationConfig (lightweight)
                val generationConfig = JSONObject().apply {
                    put("temperature", 0.7)
                }
                put("generationConfig", generationConfig)
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = requestBodyJson.toString().toRequestBody(mediaType)

            val url = "$BASE_URL?key=$apiKey"
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errBody = response.body?.string() ?: "Unknown error"
                    Log.e(TAG, "Unsuccessful response from Gemini: Code ${response.code}, Body: $errBody")
                    
                    // Display friendly advice if quota or format issue
                    if (response.code == 429) {
                        return@withContext "The FutureMe AI simulators are experiencing heavy traffic today. Please retry shortly! (API Quota Exceeded)"
                    }
                    return@withContext "Simulation failure: system error code ${response.code}. Please ensure your API key is correctly configured."
                }

                val responseBody = response.body?.string()
                if (responseBody.isNullOrEmpty()) {
                    return@withContext "Simulation failure: received empty cosmic energy transmission."
                }

                val jsonResponse = JSONObject(responseBody)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates == null || candidates.length() == 0) {
                    return@withContext "The future is currently unwritten or obscured. Try rephrasing your thought."
                }

                val content = candidates.getJSONObject(0).optJSONObject("content")
                if (content == null) {
                    return@withContext "My sight is blocked by the fog of choice. Let's restart this simulation."
                }

                val parts = content.optJSONArray("parts")
                if (parts == null || parts.length() == 0) {
                    return@withContext "My words are echoing in the void. What is your goal?"
                }

                val text = parts.getJSONObject(0).optString("text")
                return@withContext text
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during Gemini Call", e)
            return@withContext "Cosmic interference detected: ${e.localizedMessage ?: "connection lost in deep space"}"
        }
    }
}
