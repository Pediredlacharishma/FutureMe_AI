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

    @TypeConverter
    fun fromUserCommitmentList(value: List<UserCommitment>?): String? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, UserCommitment::class.java)
        return moshi.adapter<List<UserCommitment>>(type).toJson(value)
    }

    @TypeConverter
    fun toUserCommitmentList(value: String?): List<UserCommitment>? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, UserCommitment::class.java)
        return moshi.adapter<List<UserCommitment>>(type).fromJson(value)
    }

    @TypeConverter
    fun fromJournalEntryList(value: List<JournalEntry>?): String? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, JournalEntry::class.java)
        return moshi.adapter<List<JournalEntry>>(type).toJson(value)
    }

    @TypeConverter
    fun toJournalEntryList(value: String?): List<JournalEntry>? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, JournalEntry::class.java)
        return moshi.adapter<List<JournalEntry>>(type).fromJson(value)
    }

    @TypeConverter
    fun fromBookChapterList(value: List<BookChapter>?): String? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, BookChapter::class.java)
        return moshi.adapter<List<BookChapter>>(type).toJson(value)
    }

    @TypeConverter
    fun toBookChapterList(value: String?): List<BookChapter>? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, BookChapter::class.java)
        return moshi.adapter<List<BookChapter>>(type).fromJson(value)
    }

    @TypeConverter
    fun fromRealityCheckList(value: List<RealityCheckReport>?): String? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, RealityCheckReport::class.java)
        return moshi.adapter<List<RealityCheckReport>>(type).toJson(value)
    }

    @TypeConverter
    fun toRealityCheckList(value: String?): List<RealityCheckReport>? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, RealityCheckReport::class.java)
        return moshi.adapter<List<RealityCheckReport>>(type).fromJson(value)
    }

    @TypeConverter
    fun fromAlternateFutureList(value: List<AlternateFuture>?): String? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, AlternateFuture::class.java)
        return moshi.adapter<List<AlternateFuture>>(type).toJson(value)
    }

    @TypeConverter
    fun toAlternateFutureList(value: String?): List<AlternateFuture>? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, AlternateFuture::class.java)
        return moshi.adapter<List<AlternateFuture>>(type).fromJson(value)
    }

    @TypeConverter
    fun fromTimeCapsuleList(value: List<TimeCapsule>?): String? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, TimeCapsule::class.java)
        return moshi.adapter<List<TimeCapsule>>(type).toJson(value)
    }

    @TypeConverter
    fun toTimeCapsuleList(value: String?): List<TimeCapsule>? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, TimeCapsule::class.java)
        return moshi.adapter<List<TimeCapsule>>(type).fromJson(value)
    }

    @TypeConverter
    fun fromFutureMomentList(value: List<FutureMoment>?): String? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, FutureMoment::class.java)
        return moshi.adapter<List<FutureMoment>>(type).toJson(value)
    }

    @TypeConverter
    fun toFutureMomentList(value: String?): List<FutureMoment>? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, FutureMoment::class.java)
        return moshi.adapter<List<FutureMoment>>(type).fromJson(value)
    }

    @TypeConverter
    fun fromMirrorReflectionList(value: List<MirrorReflection>?): String? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, MirrorReflection::class.java)
        return moshi.adapter<List<MirrorReflection>>(type).toJson(value)
    }

    @TypeConverter
    fun toMirrorReflectionList(value: String?): List<MirrorReflection>? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, MirrorReflection::class.java)
        return moshi.adapter<List<MirrorReflection>>(type).fromJson(value)
    }

    @TypeConverter
    fun fromFutureSelfChallengeList(value: List<FutureSelfChallenge>?): String? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, FutureSelfChallenge::class.java)
        return moshi.adapter<List<FutureSelfChallenge>>(type).toJson(value)
    }

    @TypeConverter
    fun toFutureSelfChallengeList(value: String?): List<FutureSelfChallenge>? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, FutureSelfChallenge::class.java)
        return moshi.adapter<List<FutureSelfChallenge>>(type).fromJson(value)
    }
}

