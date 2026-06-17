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

@JsonClass(generateAdapter = true)
data class UserCommitment(
    val id: String,
    val text: String,
    val loggedDate: String,
    val targetDate: String,
    val status: String // "Active", "Fulfilled", "Abandoned"
)

@JsonClass(generateAdapter = true)
data class JournalEntry(
    val id: String,
    val date: String,
    val content: String,
    val emotionalState: String, // "Overwhelmed", "Excited", "Stuck", "Motivated", "Calm"
    val learned: String = "",
    val struggled: String = "",
    val gratitude: String = ""
)

@JsonClass(generateAdapter = true)
data class BookChapter(
    val title: String,
    val subTitle: String,
    val content: String,
    val unlockedDate: String,
    val isLocked: Boolean
)

@JsonClass(generateAdapter = true)
data class RealityCheckReport(
    val id: String,
    val month: String,
    val goalTitle: String,
    val plannedCompletion: Int,
    val actualCompletion: Int,
    val plannedHours: Int,
    val actualHours: Int,
    val insightFromGuide: String,
    val warningFromShadow: String
)

@JsonClass(generateAdapter = true)
data class AlternateFuture(
    val title: String,
    val description: String,
    val finalAge: Int,
    val careerTitle: String,
    val metrics: String,
    val dynamicPros: List<String> = emptyList(),
    val dynamicCons: List<String> = emptyList()
)

@JsonClass(generateAdapter = true)
data class TimeCapsule(
    val id: String,
    val message: String,
    val createdDate: String,
    val unlockDate: String,
    val isUnlocked: Boolean,
    val unlockReason: String // "1 Month", "3 Months", "6 Months", "1 Year", "Custom"
)

@JsonClass(generateAdapter = true)
data class FutureMoment(
    val id: String,
    val title: String, // e.g. "Future Moment #12"
    val subtitle: String, // e.g. "Today you stopped being a learner..."
    val content: String,
    val dateLogged: String,
    val category: String // "Project", "Internship", "Rejection", "Job Offer", etc.
)

@JsonClass(generateAdapter = true)
data class MirrorReflection(
    val id: String,
    val question: String,
    val answer: String,
    val dateAnswered: String
)

@JsonClass(generateAdapter = true)
data class FutureSelfChallenge(
    val id: String,
    val title: String,
    val description: String,
    val targetDays: Int,
    val currentProgress: Int,
    val isCompleted: Boolean,
    val rewardDescription: String,
    val type: String // "Study", "Sleep", "Consistency", "Habit"
)

