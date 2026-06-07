package com.example.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatMessage(
    val id: String,
    val sender: String, // "user", "guide", "shadow"
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

@JsonClass(generateAdapter = true)
data class EnvironmentConfig(
    val sleepHours: Float = 7.0f,
    val studyHours: Float = 4.0f,
    val stressLevel: Int = 5,
    val confidenceLevel: Int = 5,
    val anxietyLevel: Int = 5,
    val burnoutLevel: Int = 5,
    val familySupport: String = "Medium", // High, Medium, Low
    val friendCircle: String = "Neutral", // Motivating, Neutral, Distracting
    val mentorship: String = "Medium", // Strong, Medium, None
    val financialResources: String = "Medium", // High, Medium, Low
    val consistency: Int = 5,
    val procrastination: Int = 5
)

@JsonClass(generateAdapter = true)
data class RoadmapStep(
    val title: String,
    val category: String, // "Foundation", "Intermediate", "Advanced", "Professional"
    val description: String,
    val resource: String,
    val youtube: String,
    val course: String,
    val practice: String,
    val miniProject: String,
    val completed: Boolean = false
)

@JsonClass(generateAdapter = true)
data class SkillAssessmentItem(
    val name: String,
    val level: String // "Not Started", "Beginner", "Intermediate", "Advanced"
)

@JsonClass(generateAdapter = true)
data class GoalDiscoveryResult(
    val goalName: String,
    val percentage: Int,
    val reason: List<String>
)

@JsonClass(generateAdapter = true)
data class WeeklyProgressItem(
    val weekDate: String,
    val progressScore: Int,
    val stage: String,
    val reason: String
)
