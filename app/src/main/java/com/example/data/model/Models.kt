package com.example.data.model

enum class ContentCategory(val titleGujarati: String) {
    AARTI("આરતી"),
    BHAJAN("ભજન"),
    MANTRA("મંત્ર"),
    DHUN("ધૂન"),
    STOTRA("સ્તોત્ર"),
    CHALISA("ચાલીસા"),
    GITA("ભગવદ્ ગીતા"),
    TEMPLE("મંદિર દર્શન")
}

data class DeityCategory(
    val id: String,
    val nameGujarati: String,
    val nameEnglish: String,
    val greetingGujarati: String,
    val descriptionGujarati: String,
    val primaryColorHex: Long = 0xFFE65100,
    val imageResId: Int? = null
)

data class DailyDarshanItem(
    val id: String,
    val deityName: String,
    val templeName: String,
    val titleGujarati: String,
    val quoteGujarati: String,
    val blessingGujarati: String,
    val suvicharGujarati: String,
    val imageResId: Int? = null,
    val captionGujarati: String = "",
    val dayOfWeekGujarati: String = "",
    val deityId: String = ""
)

data class CityLocation(
    val nameGujarati: String,
    val nameEnglish: String,
    val latitude: Double,
    val longitude: Double
)

data class ChoghadiyaItem(
    val nameGujarati: String,
    val timeRange: String,
    val natureGujarati: String, // અમૃત (શ્રેષ્ઠ), શુભ (સારું), લાભ (લાભદાયક), ચલ (સામાન્ય), રોગ (અશુભ), કાળ (અશુભ)
    val isAuspicious: Boolean
)

data class PanchangData(
    val cityName: String,
    val gregorianDateFormatted: String,
    val vikramSamvat: String,
    val gujaratiMonth: String,
    val paksha: String, // સુદ / વદ
    val tithi: String,
    val vaar: String, // રવિવાર, સોમવાર...
    val nakshatra: String,
    val yoga: String,
    val karana: String,
    val sunrise: String,
    val sunset: String,
    val rahuKaal: String,
    val abhijitMuhurat: String,
    val choghadiyaList: List<ChoghadiyaItem>,
    val nightChoghadiyaList: List<ChoghadiyaItem> = emptyList(),
    val specialFestival: String? = null
)

data class GujaratiCalendarDay(
    val dayOfMonth: Int,
    val monthNumber: Int, // 1-12
    val year: Int, // 2026-2030
    val vikramSamvat: Int,
    val gujaratiMonth: String,
    val paksha: String, // સુદ / વદ
    val tithi: String,
    val tithiShort: String,
    val isPurnima: Boolean,
    val isAmavasya: Boolean,
    val isEkadashi: Boolean,
    val festivalName: String? = null,
    val isCurrentMonth: Boolean,
    val isToday: Boolean
)


data class AudioTrack(
    val id: String,
    val titleGujarati: String,
    val deityNameGujarati: String,
    val category: ContentCategory,
    val durationSeconds: Int,
    val lyricsGujarati: String,
    val transliteration: String = "",
    val meaningGujarati: String,
    val baseFrequencyHz: Float = 432f,
    val chantToneName: String = "ઓમકાર & ઘંટનાદ",
    val rawResId: Int? = null,
    val audioUrl: String? = null,
    val isHumanVocal: Boolean = true,
    val imageResId: Int? = null,
    val isBonus: Boolean = false
)

data class GitaChapter(
    val number: Int,
    val titleSanskrit: String,
    val titleGujarati: String,
    val versesCount: Int,
    val summaryGujarati: String,
    val detailedSummaryGujarati: String = "",
    val coreTeachings: List<String> = emptyList()
)

data class GitaShloka(
    val chapterNumber: Int,
    val verseNumber: Int,
    val sanskrit: String,
    val transliteration: String,
    val gujaratiMeaning: String,
    val practicalLessonGujarati: String
)

data class ScriptureVerse(
    val verseNumber: Int,
    val originalText: String,
    val gujaratiText: String,
    val meaningGujarati: String
)

data class DevotionalScripture(
    val id: String,
    val titleGujarati: String,
    val deityName: String,
    val category: ContentCategory,
    val verses: List<ScriptureVerse>,
    val introductionGujarati: String
)

data class TempleInfo(
    val id: String,
    val nameGujarati: String,
    val location: String,
    val deity: String,
    val historicalOverview: String,
    val darshanTimings: String,
    val aartiTimings: String,
    val address: String,
    val websiteUrl: String,
    val highlightsGujarati: List<String>,
    val imageResId: Int? = null
)

data class HinduFestival(
    val id: String,
    val nameGujarati: String,
    val dateDisplay: String,
    val tithiGujarati: String,
    val daysRemaining: Int,
    val descriptionGujarati: String,
    val ritualsGujarati: String
)

data class WallpaperItem(
    val id: String,
    val titleGujarati: String,
    val suvicharGujarati: String,
    val deityName: String,
    val bgGradientColors: List<Long>,
    val imageResId: Int? = null
)

