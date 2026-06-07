package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.*

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val email: String,
    val name: String,
    val age: String,
    val classLevel: String,
    val goal: String = "",
    val interests: List<String> = emptyList(),
    val skills: List<SkillAssessmentItem> = emptyList(),
    val environment: EnvironmentConfig = EnvironmentConfig(),
    val dreamProgress: Int = 0,
    val currentStage: String = "Explorer",
    val learningPath: List<RoadmapStep> = emptyList(),
    val futureGuideChats: List<ChatMessage> = emptyList(),
    val shadowSelfChats: List<ChatMessage> = emptyList(),
    val weeklyHistory: List<WeeklyProgressItem> = emptyList()
)
