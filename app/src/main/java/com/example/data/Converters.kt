package com.example.data

import androidx.room.TypeConverter
import com.example.data.model.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        return moshi.adapter<List<String>>(type).toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        return moshi.adapter<List<String>>(type).fromJson(value)
    }

    @TypeConverter
    fun fromChatMessageList(value: List<ChatMessage>?): String? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, ChatMessage::class.java)
        return moshi.adapter<List<ChatMessage>>(type).toJson(value)
    }

    @TypeConverter
    fun toChatMessageList(value: String?): List<ChatMessage>? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, ChatMessage::class.java)
        return moshi.adapter<List<ChatMessage>>(type).fromJson(value)
    }

    @TypeConverter
    fun fromEnvironmentConfig(value: EnvironmentConfig?): String? {
        if (value == null) return null
        return moshi.adapter(EnvironmentConfig::class.java).toJson(value)
    }

    @TypeConverter
    fun toEnvironmentConfig(value: String?): EnvironmentConfig? {
        if (value == null) return null
        return moshi.adapter(EnvironmentConfig::class.java).fromJson(value)
    }

    @TypeConverter
    fun fromRoadmapStepList(value: List<RoadmapStep>?): String? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, RoadmapStep::class.java)
        return moshi.adapter<List<RoadmapStep>>(type).toJson(value)
    }

    @TypeConverter
    fun toRoadmapStepList(value: String?): List<RoadmapStep>? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, RoadmapStep::class.java)
        return moshi.adapter<List<RoadmapStep>>(type).fromJson(value)
    }

    @TypeConverter
    fun fromSkillAssessmentList(value: List<SkillAssessmentItem>?): String? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, SkillAssessmentItem::class.java)
        return moshi.adapter<List<SkillAssessmentItem>>(type).toJson(value)
    }

    @TypeConverter
    fun toSkillAssessmentList(value: String?): List<SkillAssessmentItem>? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, SkillAssessmentItem::class.java)
        return moshi.adapter<List<SkillAssessmentItem>>(type).fromJson(value)
    }

    @TypeConverter
    fun fromWeeklyHistoryList(value: List<WeeklyProgressItem>?): String? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, WeeklyProgressItem::class.java)
        return moshi.adapter<List<WeeklyProgressItem>>(type).toJson(value)
    }

    @TypeConverter
    fun toWeeklyHistoryList(value: String?): List<WeeklyProgressItem>? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, WeeklyProgressItem::class.java)
        return moshi.adapter<List<WeeklyProgressItem>>(type).fromJson(value)
    }
}
