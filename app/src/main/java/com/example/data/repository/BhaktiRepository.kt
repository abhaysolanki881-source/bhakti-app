package com.example.data.repository

import com.example.data.local.BookmarkEntity
import com.example.data.local.DevotionalDao
import com.example.data.local.FavoriteEntity
import com.example.data.local.JapMalaRecordEntity
import com.example.data.local.RecentPlayEntity
import com.example.data.model.AudioTrack
import com.example.data.model.ContentCategory
import com.example.data.model.DailyDarshanItem
import com.example.data.model.DeityCategory
import com.example.data.model.DevotionalScripture
import com.example.data.model.GitaChapter
import com.example.data.model.GitaShloka
import com.example.data.model.HinduFestival
import com.example.data.model.ScriptureVerse
import com.example.data.model.TempleInfo
import com.example.data.model.WallpaperItem
import kotlinx.coroutines.flow.Flow

class BhaktiRepository(private val dao: DevotionalDao? = null) {

    // ---------------------------------------------------------
    // Room Database Operations
    // ---------------------------------------------------------
    val allFavorites: Flow<List<FavoriteEntity>> = dao?.getAllFavorites() ?: kotlinx.coroutines.flow.emptyFlow()
    val allBookmarks: Flow<List<BookmarkEntity>> = dao?.getAllBookmarks() ?: kotlinx.coroutines.flow.emptyFlow()
    val recentPlays: Flow<List<RecentPlayEntity>> = dao?.getRecentPlays() ?: kotlinx.coroutines.flow.emptyFlow()
    val totalMalaRounds: Flow<Int?> = dao?.getTotalLifetimeRounds() ?: kotlinx.coroutines.flow.flowOf(0)
    val totalMalaBeads: Flow<Int?> = dao?.getTotalLifetimeBeads() ?: kotlinx.coroutines.flow.flowOf(0)

    fun isFavorite(id: String): Flow<Boolean> = dao?.isFavorite(id) ?: kotlinx.coroutines.flow.flowOf(false)

    suspend fun toggleFavorite(id: String, titleGujarati: String, category: String, subtitle: String, currentStatus: Boolean) {
        if (currentStatus) {
            dao?.deleteFavoriteById(id)
        } else {
            dao?.insertFavorite(FavoriteEntity(id, titleGujarati, category, subtitle))
        }
    }

    suspend fun saveBookmark(bookmark: BookmarkEntity) { dao?.insertBookmark(bookmark) }
    suspend fun removeBookmark(id: String) { dao?.deleteBookmarkById(id) }

    suspend fun recordJapMalaSession(mantraName: String, beads: Int, rounds: Int) {
        dao?.insertMalaRecord(JapMalaRecordEntity(mantraName = mantraName, beadsCompleted = beads, roundsCompleted = rounds))
    }

    suspend fun clearAllFavorites() { dao?.deleteAllFavorites() }
    suspend fun clearAllBookmarks() { dao?.deleteAllBookmarks() }
    suspend fun resetMalaHistory() { dao?.deleteAllMalaRecords() }

    suspend fun addRecentPlay(track: AudioTrack) {
        dao?.insertRecentPlay(
            RecentPlayEntity(
                id = track.id,
                titleGujarati = track.titleGujarati,
                category = track.category.name,
                deityName = track.deityNameGujarati,
                durationSeconds = track.durationSeconds
            )
        )
    }

    // ---------------------------------------------------------
    // Curated Content Providers
    // ---------------------------------------------------------
    val deityCategories: List<DeityCategory> = listOf(
        DeityCategory("krishna", "શ્રી કૃષ્ણ", "Shree Krishna", "જય શ્રી કૃષ્ણ", "દ્વારકાધીશ, રણછોડરાય, શ્યામ સુંદર ભક્તિ", 0xFF0D47A1, com.example.R.drawable.img_darshan_krishna_1793948267799),
        DeityCategory("shiva", "ભગવાન શિવ", "Lord Shiva", "હર હર મહાદેવ", "સોમનાથ, ભોળાનાથ, મહાકાલેશ્વર આરાધના", 0xFF00695C, com.example.R.drawable.img_darshan_shiva_1793948288707),
        DeityCategory("hanuman", "હનુમાનજી", "Hanuman Ji", "જય શ્રી રામ - જય બજરંગબલી", "કષ્ટભંજન દેવ સારંગપુર, સંકટમોચન", 0xFFE65100, com.example.R.drawable.img_darshan_hanuman_1793948310273),
        DeityCategory("ganesha", "શ્રી ગણેશ", "Shree Ganesha", "ૐ ગં ગણપતયે નમઃ", "વિઘ્નહર્તા, રિદ્ધિ-સિદ્ધિ દાતા", 0xFFC2185B, com.example.R.drawable.img_deity_ganesh_1793958512473),
        DeityCategory("ambaji", "માતા અંબા", "Mataji", "જય માતાજી - જય અંબે", "આરાસુરી અંબાજી, પાવાગઢ કાળિકા માતા", 0xFFD32F2F, com.example.R.drawable.img_deity_ambaji_1793958532560),
        DeityCategory("ram", "શ્રી રામ", "Shree Ram", "જય સિયારામ", "મર્યાદા પુરુષોત્તમ પ્રભુ શ્રી રામ", 0xFFF57C00, com.example.R.drawable.img_deity_ram_1793958549091),
        DeityCategory("vishnu", "ભગવાન વિષ્ણુ", "Lord Vishnu", "ૐ નમો નારાયણાય", "જગતપાલક શ્રી લક્ષ્મીનારાયણ", 0xFF512DA8, com.example.R.drawable.img_darshan_krishna_1793948267799),
        DeityCategory("swaminarayan", "સ્વામિનારાયણ", "Swaminarayan", "જય સ્વામિનારાયણ", "અક્ષરધામ, સહજાનંદ સ્વામી ભક્તિ", 0xFFFFA000, com.example.R.drawable.img_deity_swaminarayan_1793958571996)
    )

    val dailyDarshanList: List<DailyDarshanItem> = listOf(
        DailyDarshanItem(
            id = "darshan_somnath",
            deityId = "shiva",
            deityName = "સોમનાથ મહાદેવ",
            templeName = "પ્રથમ જ્યોતિર્લિંગ, પ્રભાસ પાટણ",
            titleGujarati = "આજનું શ્રુંગાર દર્શન - દેવાધિદેવ મહાદેવ",
            quoteGujarati = "ૐ ત્ર્યંબકં યજામહે સુગંધિં પુષ્ટિવર્ધનમ્ | ઉર્વારુકમિવ બંધનાન્મૃત્યોર્મુક્ષીય મામૃતાત્ ||",
            blessingGujarati = "મહાદેવ આપના જીવનમાં સદા નિરોગી કાયા અને ભક્તિનો પ્રકાશ આપે.",
            suvicharGujarati = "સમય ગમે તેટલો કઠિન હોય, ભોળાનાથનું સ્મરણ હૃદયમાં અખૂટ બળ ભરી દે છે.",
            imageResId = com.example.R.drawable.img_darshan_shiva_1793948288707,
            captionGujarati = "દેવોના દેવ મહાદેવ સોમનાથના પ્રાતઃ શ્રુંગાર દર્શન. ત્રિલોકના નાથ આપના પરિવાર પર સદા સુખ અને અખંડ શાંતિની વર્ષા કરે. ૐ નમઃ શિવાય.",
            dayOfWeekGujarati = "સોમવાર"
        ),
        DailyDarshanItem(
            id = "darshan_bhavnath",
            deityId = "shiva",
            deityName = "શ્રી ભવનાથ મહાદેવ",
            templeName = "ગિરનાર તળેટી, જૂનાગઢ",
            titleGujarati = "દિવ્ય દર્શન - ભવનાથ દાદા",
            quoteGujarati = "ૐ નમઃ શિવાય શુભં શુભં કુરુ કુરુ શિવાય નમઃ ૐ ||",
            blessingGujarati = "ગિરનારી નાથ આપના સંકટો દૂર કરી મનને શાંતિ આપે.",
            suvicharGujarati = "ભોળાનાથના શરણમાં જે નમે છે, તેનો ભાર પ્રભુ સ્વયં ઉપાડી લે છે.",
            imageResId = com.example.R.drawable.img_darshan_shiva_1793948288707,
            captionGujarati = "સિદ્ધક્ષેત્ર ગિરનાર તળેટી સ્થિત ભવનાથ મહાદેવના પાવન દર્શન. હર હર મહાદેવ.",
            dayOfWeekGujarati = "દૈનિક વિશેષ"
        ),
        DailyDarshanItem(
            id = "darshan_salangpur",
            deityId = "hanuman",
            deityName = "કષ્ટભંજન દેવ",
            templeName = "શ્રી હનુમાનજી મંદિર, સારંગપુર",
            titleGujarati = "આજનું દિવ્ય દર્શન - દાદા કષ્ટભંજન",
            quoteGujarati = "મનોજવં મારુતતુલ્યવેગં જિતેન્દ્રિયં બુદ્ધિમતાં વરિષ્ઠમ્ | વાતાત્મજં વાનરયૂથમુખ્યં શ્રીરામદૂતં શરણં પ્રપદ્યે ||",
            blessingGujarati = "દાદા આપના સર્વ કષ્ટ અને બાધાઓનું નિવારણ કરે.",
            suvicharGujarati = "હનુમાનજીની ભક્તિ ભય મુક્ત કરે છે અને નિર્ભયતા અર્પે છે.",
            imageResId = com.example.R.drawable.img_darshan_hanuman_1793948310273,
            captionGujarati = "સારંગપુરના રાજા શ્રી કષ્ટભંજન દેવ હનુમાન દાદાના મંગલ દર્શન. દાદા આપના સર્વ દુઃખ, સંકટ અને ભય હરે. જય બજરંગબલી.",
            dayOfWeekGujarati = "મંગળવાર"
        ),
        DailyDarshanItem(
            id = "darshan_camp_hanuman",
            deityId = "hanuman",
            deityName = "કેમ્પ હનુમાનજી",
            templeName = "શાહીબાગ કેન્ટોનમેન્ટ, અમદાવાદ",
            titleGujarati = "મંગલ દર્શન - શ્રી કેમ્પ હનુમાન",
            quoteGujarati = "અંજનીગર્ભસંભૂત કપીન્દ્ર સચિવોત્તમ | રામપ્રિય નમસ્તુભ્યં હનુમન્ રક્ષ સર્વદા ||",
            blessingGujarati = "કેમ્પ હનુમાન દાદા આપની સર્વ મનોકામના પૂર્ણ કરે.",
            suvicharGujarati = "પ્રભુ રામના દૂત હનુમાનજીનું સ્મરણ અસાધ્યને પણ સાધ્ય બનાવે છે.",
            imageResId = com.example.R.drawable.img_darshan_hanuman_1793948310273,
            captionGujarati = "અમદાવાદના ઐતિહાસિક કેમ્પ હનુમાનજી દાદાના દિવ્ય મંગળા દર્શન. જય શ્રી રામ.",
            dayOfWeekGujarati = "દૈનિક વિશેષ"
        ),
        DailyDarshanItem(
            id = "darshan_ganesh",
            deityId = "ganesha",
            deityName = "શ્રી સિદ્ધિ વિનાયક ગણેશજી",
            templeName = "મહેમદાવાદ સિદ્ધિ વિનાયક દેવસ્થાન",
            titleGujarati = "આજનું મંગલ દર્શન - વિઘ્નહર્તા ગણેશ",
            quoteGujarati = "વક્રતુંડ મહાકાય સૂર્યકોટિ સમપ્રભ | નિર્વિઘ્નં કુરુ મે દેવ સર્વકાર્યેષુ સર્વદા ||",
            blessingGujarati = "શ્રી ગણેશજી આપના સર્વ કાર્યો નિર્વિઘ્ને સિદ્ધ કરે અને રિદ્ધિ-સિદ્ધિ અર્પે.",
            suvicharGujarati = "પ્રભુ ગણેશના ચરણોમાં શરણાગતિ જીવનના સર્વ વિઘ્નોનો અંત લાવે છે.",
            imageResId = com.example.R.drawable.img_deity_ganesh_1793958512473,
            captionGujarati = "પ્રથમ પૂજ્ય શ્રી ગણેશજીના દિવ્ય દર્શન. રિદ્ધિ-સિદ્ધિના દાતા આપના સર્વ મનોરથ પૂર્ણ કરે અને સદ્બુદ્ધિ અર્પે. ૐ ગં ગણપતયે નમઃ.",
            dayOfWeekGujarati = "બુધવાર"
        ),
        DailyDarshanItem(
            id = "darshan_vishnu",
            deityId = "vishnu",
            deityName = "શ્રી લક્ષ્મીનારાયણ પ્રભુ",
            templeName = "વૈકુંઠ ધામ, જગન્નાથજી",
            titleGujarati = "આજનું વૈકુંઠ દર્શન - શ્રી લક્ષ્મીનારાયણ",
            quoteGujarati = "શાંતાકારં ભુજગશયનં પદ્મનાભં સુરેશં | વિશ્વાધારં ગગનસદૃશં મેઘવર્ણં શુભાંગમ્ ||",
            blessingGujarati = "જગતપાલક ભગવાન વિષ્ણુ અને માં લક્ષ્મી આપના ઘરમાં અખૂટ સમૃદ્ધિ અને શાંતિ લાવે.",
            suvicharGujarati = "શ્રી હરિના શરણમાં રહેનાર ભક્તને ક્યારેય કોઈ વસ્તુની કમી રહેતી નથી.",
            imageResId = com.example.R.drawable.img_darshan_vishnu_1794026732054,
            captionGujarati = "જગતપાલક શ્રી વિષ્ણુ ભગવાન અને માતા લક્ષ્મીજીના પરમ પાવન દર્શન. પ્રભુ આપના ઘરમાં લક્ષ્મી અને સદ્વિચારનો વાસ રાખે. ૐ નમો ભગવતે વાસુદેવાય.",
            dayOfWeekGujarati = "ગુરુવાર"
        ),
        DailyDarshanItem(
            id = "darshan_ambaji",
            deityId = "ambaji",
            deityName = "મા આદ્યશક્તિ અંબાજી",
            templeName = "ગબ્બર તીર્થ, અંબાજી",
            titleGujarati = "આજનું મંગલમય દર્શન - માં અંબા",
            quoteGujarati = "સર્વમંગલ માંગલ્યે શિવે સર્વાર્થ સાધિકે | શરણ્યે ત્ર્યંબકે ગૌરિ નારાયણિ નમોઽસ્તુ તે ||",
            blessingGujarati = "માઁ અંબા સદાય આપના પરિવાર પર અમી દ્રષ્ટિ રાખે.",
            suvicharGujarati = "માતૃશક્તિની ઉપાસના જીવનમાં સકારાત્મક ઊર્જા અને સાત્ત્વિકતા લાવે છે.",
            imageResId = com.example.R.drawable.img_deity_ambaji_1793958532560,
            captionGujarati = "આરાસુરી માં અંબાના ચરણોમાં કોટિ કોટિ વંદન. જગતજનની જગદંબા આપના સમસ્ત સંકટો હરી મનોકામના પૂર્ણ કરે. બોલો અંબે માત કી જય.",
            dayOfWeekGujarati = "શુક્રવાર"
        ),
        DailyDarshanItem(
            id = "darshan_pavagadh",
            deityId = "ambaji",
            deityName = "શ્રી મહાકાળી માતાજી",
            templeName = "પાવાગઢ શક્તિપીઠ, પંચમહાલ",
            titleGujarati = "દિવ્ય દર્શન - કાળિકા માતાજી પાવાગઢ",
            quoteGujarati = "ૐ જયંતિ મંગલા કાલી ભદ્રકાલી કપાલીની | દુર્ગા ક્ષમા શિવા ધાત્રી સ્વાહા સ્વધા નમોઽસ્તુ તે ||",
            blessingGujarati = "જગદંબા મહાકાળી આપની સર્વ મનોકામનાઓ પૂર્ણ કરે.",
            suvicharGujarati = "માઁ કાળિકાના ચરણોમાં કરેલી સાચી પ્રાર્થના ક્યારેય નિષ્ફળ જતી નથી.",
            imageResId = com.example.R.drawable.img_deity_ambaji_1793958532560,
            captionGujarati = "પાવાગઢના શિખરે બિરાજમાન મહાકાળી માતાજીના દર્શન. જય માતાજી.",
            dayOfWeekGujarati = "દૈનિક વિશેષ"
        ),
        DailyDarshanItem(
            id = "darshan_dwarka",
            deityId = "krishna",
            deityName = "શ્રી દ્વારકાધીશ",
            templeName = "જગત મંદિર, દ્વારકા",
            titleGujarati = "આજનું મંગળા દર્શન - શ્રી દ્વારકાધીશ",
            quoteGujarati = "કર્મણ્યેવાધિકારસ્તે મા ફલેષુ કદાચન | મા કર્મફલહેતુર્ભૂર્મા તે સંગોઽસ્ત્વકર્મણિ ||",
            blessingGujarati = "ભગવાન દ્વારકાધીશ આપના સમસ્ત સંકટો દૂર કરી સુખ અને શાંતિ અર્પે.",
            suvicharGujarati = "જે વ્યક્તિ પ્રભુમાં સંપૂર્ણ શ્રદ્ધા રાખે છે, તેનો ભાર પ્રભુ સ્વયં ઉપાડી લે છે.",
            imageResId = com.example.R.drawable.img_darshan_krishna_1793948267799,
            captionGujarati = "કાળિયા ઠાકોર, જગતના નાથ શ્રી દ્વારકાધીશ પ્રભુના મંગળા દર્શન. આપનો આજનો દિવસ પ્રભુ કૃપાથી મંગલમય રહે. જય શ્રી કૃષ્ણ.",
            dayOfWeekGujarati = "શનિવાર"
        ),
        DailyDarshanItem(
            id = "darshan_dakor",
            deityId = "krishna",
            deityName = "શ્રી રણછોડરાયજી",
            templeName = "શ્રી રણછોડરાયજી મંદિર, ડાકોર",
            titleGujarati = "દિવ્ય દર્શન - ડાકોરના ઠાકોર",
            quoteGujarati = "કૃષ્ણાય વાસુદેવાય હરયે પરમાત્મને | પ્રણતક્લેશનાશાય ગોવિંદાય નમો નમઃ ||",
            blessingGujarati = "રણછોડરાયજી ભગવાન આપના ઘરમાં સુખ અને સમૃદ્ધિ આપે.",
            suvicharGujarati = "ભક્ત બોડાણા જેવી અનન્ય શ્રદ્ધા હોય તો ભગવાન દોડીને આવે છે.",
            imageResId = com.example.R.drawable.img_darshan_krishna_1793948267799,
            captionGujarati = "ડાકોરના રાજા શ્રી રણછોડરાયજી મહારાજના મંગલ દર્શન. જય રણછોડ, માખણ ચોર.",
            dayOfWeekGujarati = "દૈનિક વિશેષ"
        ),
        DailyDarshanItem(
            id = "darshan_surya",
            deityId = "surya",
            deityName = "ભગવાન શ્રી સૂર્યનારાયણ",
            templeName = "મોઢેરા સૂર્ય મંદિર, ગુજરાત",
            titleGujarati = "આજનું પ્રભાતી દર્શન - ભગવાન સૂર્યનારાયણ",
            quoteGujarati = "ૐ નમઃ સવિત્રે જગદેકસચક્ષુષે જગત્પ્રસૂતિસ્થિતિનાશહેતવે | ત્રયીમયાય ત્રિગુણાત્મધારિણે વિરંચિનારાયણશંકરાત્મને ||",
            blessingGujarati = "સૂર્યનારાયણ દેવ આપના જીવનમાં સૂર્ય જેવું તેજ, સ્વાસ્થ્ય અને દીર્ઘાયુષ્ય અર્પે.",
            suvicharGujarati = "જેમ સૂર્ય અંધકારને મિટાવી અજવાળું ફેલાવે છે, તેમ ઈશ્વર ભક્તિ મનના અજ્ઞાનને દૂર કરે છે.",
            imageResId = com.example.R.drawable.img_darshan_surya_1794026746069,
            captionGujarati = "પ્રત્યક્ષ પ્રગટ દેવ ભગવાન સૂર્યનારાયણના પ્રભાત દર્શન. સોનેરી કિરણો સાથે આપના જીવનમાં તેજ, ઉત્સાહ અને આરોગ્યની વૃદ્ધિ થાય. ૐ સૂર્યાય નમઃ.",
            dayOfWeekGujarati = "રવિવાર"
        ),
        DailyDarshanItem(
            id = "darshan_ram",
            deityId = "ram",
            deityName = "પ્રભુ શ્રી રામચંદ્રજી",
            templeName = "શ્રી રામ જન્મભૂમિ મંદિર, અયોધ્યા",
            titleGujarati = "આજનું દિવ્ય દર્શન - રઘુનંદન શ્રી રામ",
            quoteGujarati = "શ્રી રામચંદ્ર કૃપાલુ ભજુ મન હરણ ભવભય દારુણમ્ | નવકંજ લોચન કંજ મુખ કર કંજ પદ કંજારુણમ્ ||",
            blessingGujarati = "પ્રભુ શ્રી રામ આપના જીવનમાં સત્ય, ધર્મ અને સદાચારની દિવ્ય જ્યોત પ્રગટાવે.",
            suvicharGujarati = "રામ નામ એવું જહાજ છે જે ભવસાગર સહજતાથી પાર ઉતારે છે.",
            imageResId = com.example.R.drawable.img_deity_ram_1793958549091,
            captionGujarati = "મર્યાદા પુરુષોત્તમ પ્રભુ શ્રી રામચંદ્રજીના નયનરમ્ય દર્શન. રામ નામના જપથી અંતઃકરણ પવિત્ર અને નિર્ભય બને છે. જય સિયારામ.",
            dayOfWeekGujarati = "દૈનિક વિશેષ"
        ),
        DailyDarshanItem(
            id = "darshan_swaminarayan",
            deityId = "swaminarayan",
            deityName = "ભગવાન શ્રી સ્વામિનારાયણ",
            templeName = "અક્ષરધામ તીર્થ, ગાંધીનગર",
            titleGujarati = "આજનું પરમ દર્શન - સહજાનંદ સ્વામી",
            quoteGujarati = "સહજાનંદ સ્વામી હરિ હરિ બોલ | સ્વામિનારાયણ ભગવાન કી જય ||",
            blessingGujarati = "સહજાનંદ સ્વામી આપના હૃદયમાં પરમ શાંતિ અને સદ્ભાવના પ્રદાન કરે.",
            suvicharGujarati = "શુદ્ધ આચરણ અને પવિત્ર સત્સંગ મનુષ્યને પરમધામનો અધિકારી બનાવે છે.",
            imageResId = com.example.R.drawable.img_deity_swaminarayan_1793958571996,
            captionGujarati = "અક્ષરધામના અધિપતિ ભગવાન શ્રી સ્વામિનારાયણ (સહજાનંદ સ્વામી) ના દિવ્ય દર્શન. જય સ્વામિનારાયણ.",
            dayOfWeekGujarati = "દૈનિક વિશેષ"
        ),
        DailyDarshanItem(
            id = "darshan_vadtal",
            deityId = "swaminarayan",
            deityName = "શ્રી લક્ષ્મીનારાયણ દેવ",
            templeName = "શ્રી વડતાલ ધામ, ખેડા",
            titleGujarati = "મંગલ દર્શન - વડતાલ ધામ",
            quoteGujarati = "વંદે શ્રીપુરુષોત્તમં મુનિપતિં સહજાનંદં ગુરુમ્ ||",
            blessingGujarati = "વડતાલ બિરાજમાન લક્ષ્મીનારાયણ દેવ આપના સર્વ મનોરથ પૂર્ણ કરે.",
            suvicharGujarati = "પ્રભુ સ્વામિનારાયણનો સત્સંગ મનની સર્વ ચિંતાઓ હરી લે છે.",
            imageResId = com.example.R.drawable.img_deity_swaminarayan_1793958571996,
            captionGujarati = "સ્વામિનારાયણ સંપ્રદાયના મુખ્ય તીર્થ વડતાલ ધામના દિવ્ય દર્શન. જય સ્વામિનારાયણ.",
            dayOfWeekGujarati = "દૈનિક વિશેષ"
        )
    )

    fun getDarshanForDayOfWeek(dayOfWeek: Int): DailyDarshanItem {
        return when (dayOfWeek) {
            java.util.Calendar.SUNDAY -> dailyDarshanList.firstOrNull { it.id == "darshan_surya" } ?: dailyDarshanList.first()
            java.util.Calendar.MONDAY -> dailyDarshanList.firstOrNull { it.id == "darshan_somnath" } ?: dailyDarshanList.first()
            java.util.Calendar.TUESDAY -> dailyDarshanList.firstOrNull { it.id == "darshan_salangpur" } ?: dailyDarshanList.first()
            java.util.Calendar.WEDNESDAY -> dailyDarshanList.firstOrNull { it.id == "darshan_ganesh" } ?: dailyDarshanList.first()
            java.util.Calendar.THURSDAY -> dailyDarshanList.firstOrNull { it.id == "darshan_vishnu" } ?: dailyDarshanList.first()
            java.util.Calendar.FRIDAY -> dailyDarshanList.firstOrNull { it.id == "darshan_ambaji" } ?: dailyDarshanList.first()
            java.util.Calendar.SATURDAY -> dailyDarshanList.firstOrNull { it.id == "darshan_dwarka" } ?: dailyDarshanList.first()
            else -> dailyDarshanList.first()
        }
    }

    fun getTodayDarshan(calendar: java.util.Calendar = java.util.Calendar.getInstance()): DailyDarshanItem {
        val dayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK)
        return getDarshanForDayOfWeek(dayOfWeek)
    }

    fun getTodayGitaShloka(calendar: java.util.Calendar = java.util.Calendar.getInstance()): com.example.data.model.GitaShloka {
        if (keyShlokas.isEmpty()) return keyShlokas.first()
        val dayOfYear = calendar.get(java.util.Calendar.DAY_OF_YEAR)
        val index = (dayOfYear - 1).coerceAtLeast(0) % keyShlokas.size
        return keyShlokas[index]
    }

    fun getFeaturedAartiForDay(dayOfWeek: Int): AudioTrack {
        return when (dayOfWeek) {
            java.util.Calendar.SUNDAY -> audioTracks.firstOrNull { it.id == "mantra_gayatri" } ?: audioTracks.first()
            java.util.Calendar.MONDAY -> audioTracks.firstOrNull { it.id == "aarti_shiv" } ?: audioTracks.first()
            java.util.Calendar.TUESDAY -> audioTracks.firstOrNull { it.id == "aarti_hanuman" } ?: audioTracks.first()
            java.util.Calendar.WEDNESDAY -> audioTracks.firstOrNull { it.id == "aarti_ganesh" } ?: audioTracks.first()
            java.util.Calendar.THURSDAY -> audioTracks.firstOrNull { it.id == "aarti_jagdish" } ?: audioTracks.first()
            java.util.Calendar.FRIDAY -> audioTracks.firstOrNull { it.id == "aarti_ambe" } ?: audioTracks.first()
            java.util.Calendar.SATURDAY -> audioTracks.firstOrNull { it.id == "aarti_krishna" } ?: audioTracks.first()
            else -> audioTracks.first()
        }
    }

    fun getFeaturedBhajanForDay(dayOfWeek: Int): AudioTrack {
        return when (dayOfWeek) {
            java.util.Calendar.SUNDAY -> audioTracks.firstOrNull { it.id == "bhajan_vaishnav" } ?: audioTracks[1]
            java.util.Calendar.MONDAY -> audioTracks.firstOrNull { it.id == "mantra_mahamrityunjaya" } ?: audioTracks[1]
            java.util.Calendar.TUESDAY -> audioTracks.firstOrNull { it.id == "dhun_hare_krishna" } ?: audioTracks[1]
            java.util.Calendar.WEDNESDAY -> audioTracks.firstOrNull { it.id == "dhun_swaminarayan" } ?: audioTracks[1]
            java.util.Calendar.THURSDAY -> audioTracks.firstOrNull { it.id == "bhajan_vaishnav" } ?: audioTracks[1]
            java.util.Calendar.FRIDAY -> audioTracks.firstOrNull { it.id == "aarti_ambe" } ?: audioTracks[1]
            java.util.Calendar.SATURDAY -> audioTracks.firstOrNull { it.id == "dhun_hare_krishna" } ?: audioTracks[1]
            else -> audioTracks[1]
        }
    }

    val audioTracks: List<AudioTrack> = listOf(
        AudioTrack(
            id = "aarti_ganesh",
            titleGujarati = "જય ગણેશ જય ગણેશ દેવા (આરતી)",
            deityNameGujarati = "શ્રી ગણેશ",
            category = ContentCategory.AARTI,
            durationSeconds = 272,
            lyricsGujarati = """
                જય ગણેશ જય ગણેશ જય ગણેશ દેવા,
                માતા જાકી પાર્વતી પિતા મહાદેવા.
                
                એકદંત દયાવંત ચાર ભુજાધારી,
                માથે સિંદૂર સોહે મૂષ કી સવારી.
                પાન ચઢે ફૂલ ચઢે ઔર ચઢે મેવા,
                લડુઅન કા ભોગ લગે સંત કરેં સેવા.
                
                અંધન કો આંખ દેત કોઢિન કો કાયા,
                બાંઝન કો પુત્ર દેત નિર્ધન કો માયા.
                સૂર શ્યામ શરણ આયે સફલ કીજે સેવા,
                માતા જાકી પાર્વતી પિતા મહાદેવા.
            """.trimIndent(),
            meaningGujarati = "વિઘ્નહર્તા શ્રી ગણેશજીની આ મંગલ આરતી સર્વ પ્રકારના કાર્યોની નિર્વિઘ્ન પૂર્તિ કરે છે.",
            baseFrequencyHz = 432f,
            rawResId = com.example.R.raw.aarti_ganesh_deva,
            isHumanVocal = true,
            imageResId = com.example.R.drawable.img_deity_ganesh_1793958512473
        ),
        AudioTrack(
            id = "aarti_jagdish",
            titleGujarati = "ૐ જય જગદીશ હરે (સંધ્યા આરતી)",
            deityNameGujarati = "ભગવાન વિષ્ણુ",
            category = ContentCategory.AARTI,
            durationSeconds = 412,
            lyricsGujarati = """
                ૐ જય જગદીશ હરે, સ્વામી જય જગદીશ હરે,
                ભક્ત જનો કે સંકટ ક્ષણ મેં દૂર કરે... ૐ જય જગદીશ હરે.
                
                જો ધ્યાવે ફલ પાવે, દુઃખ બિનસે મન કા,
                સુખ સંપત્તિ ઘર આવે, કષ્ટ મિટે તન કા... ૐ જય જગદીશ હરે.
                
                માત પિતા તુમ મેરે, શરણ ગહૂઁ કિસકી,
                તુમ બિન ઔર ન દૂજા, આસ કરૂઁ જિસકી... ૐ જય જગદીશ હરે.
                
                તુમ પૂરણ પરમાત્મા, તુમ અંતર્યામી,
                પારબ્રહ્મ પરમેશ્વર, તુમ સબકે સ્વામી... ૐ જય જગદીશ હરે.
            """.trimIndent(),
            meaningGujarati = "સંપૂર્ણ વિશ્વના પાલનહાર શ્રી હરિ વિષ્ણુજીની સર્વકલ્યાણકારી વિશ્વપ્રસિદ્ધ આરતી.",
            baseFrequencyHz = 432f,
            rawResId = com.example.R.raw.aarti_om_jai_jagdish,
            isHumanVocal = true,
            imageResId = com.example.R.drawable.img_darshan_krishna_1793948267799
        ),
        AudioTrack(
            id = "aarti_ambe",
            titleGujarati = "જય આધ્યા શક્તિ (અંબા માતાની આરતી)",
            deityNameGujarati = "માતા અંબા",
            category = ContentCategory.AARTI,
            durationSeconds = 575,
            lyricsGujarati = """
                જય આધ્યા શક્તિ મા જય આધ્યા શક્તિ,
                અખંડ બ્રહ્માંડ નિપજાવ્યા પડવે પ્રગટ્યા મા... ૐ જયો જયો મા જગદંબે.
                
                દ્વિતીયા બેય સ્વરૂપ શિવશક્તિ જાણો,
                બ્રહ્મા ગણપતિ ગાયે હર ગાયે હર મા... ૐ જયો જયો મા જગદંબે.
                
                તૃતીયા ત્રણ સ્વરૂપ ત્રિભુવનમાં બેઠા,
                દયા થકી દીધાં ફળ દર્શન દીધાં મા... ૐ જયો જયો મા જગદંબે.
                
                ચોથે ચતુરા મહાલક્ષ્મી મા સચરાચર વ્યાપ્યા,
                ચાર ભુજા ચૌદિશ પ્રગટ્યા મન વંછિત આપ્યા... ૐ જયો જયો મા જગદંબે.
                
                પંચમે પંચ ઋષિ પંચમી ગુણ પદ્મા,
                પંચ તત્ત્વમાં વ્યાપ્યા પંચામૃત પીધાં... ૐ જયો જયો મા જગદંબે.
            """.trimIndent(),
            meaningGujarati = "ગુજરાતની પાવન પરંપરાની આધ્યશક્તિ અંબામાતાની સર્વશ્રેષ્ઠ સ્તુતિ આરતી.",
            baseFrequencyHz = 528f,
            rawResId = com.example.R.raw.aarti_jay_adhya_shakti,
            isHumanVocal = true,
            imageResId = com.example.R.drawable.img_deity_ambaji_1793958532560
        ),
        AudioTrack(
            id = "aarti_shiv",
            titleGujarati = "ૐ જય શિવ ઓમકારા (મહાદેવ આરતી)",
            deityNameGujarati = "ભગવાન શિવ",
            category = ContentCategory.AARTI,
            durationSeconds = 333,
            lyricsGujarati = """
                ૐ જય શિવ ઓમકારા, પ્રભુ હર શિવ ઓમકારા,
                બ્રહ્મા વિષ્ણુ સદાશિવ અર્ધાંગી ધારા... ૐ જય શિવ ઓમકારા.
                
                એકાનન ચતુરન પંચાનન રાજે,
                હંસાસન ગરુડાસન વૃષવાહન સાજે... ૐ જય શિવ ઓમકારા.
                
                દો ભુજ ચાર ચતુર્ભુજ દશ ભુજ અતિ સોહે,
                તીનોં રૂપ નિરખતા ત્રિભુવન જન મોહે... ૐ જય શિવ ઓમકારા.
                
                અક્ષમાલા વનમાલા રુંડમાલા ધારી,
                ચંદન મૃગમદ સોહે ભાલે શશિકારી... ૐ જય શિવ ઓમકારા.
            """.trimIndent(),
            meaningGujarati = "દેવોના દેવ મહાદેવની ૐકાર સ્વરૂપ આરતી જે ચિત્તને પરમ શાંતિ આપે છે.",
            baseFrequencyHz = 432f,
            rawResId = com.example.R.raw.aarti_om_jai_shiv_omkara,
            isHumanVocal = true,
            imageResId = com.example.R.drawable.img_darshan_shiva_1793948288707
        ),
        AudioTrack(
            id = "bhajan_vaishnav",
            titleGujarati = "વૈષ્ણવ જન તો તેને રે કહીએ",
            deityNameGujarati = "શ્રી કૃષ્ણ",
            category = ContentCategory.BHAJAN,
            durationSeconds = 177,
            lyricsGujarati = """
                વૈષ્ણવ જન તો તેને રે કહીએ જે પીડ પરાઈ જાણે રે,
                પરદુઃખે ઉપકાર કરે તોયે મન અભિમાન ન આણે રે.
                
                સકળ લોકમાં સહુને વંદે, નિંદા ન કરે કેની રે,
                વાચ કાચ મન નિર્મળ રાખે ધન ધન જનની તેની રે... વૈષ્ણવ જન તો તેને રે કહીએ.
                
                સમદ્રષ્ટિ ને તૃષ્ણા ત્યાગી, પરસ્ત્રી જેને માત રે,
                જિહ્વા થકી અસત્ય ન બોલે, પરધન નવ ઝાલે હાથ રે... વૈષ્ણવ જન તો તેને રે કહીએ.
                
                મોહ માયા વ્યાપે નહિ જેને, દૃઢ વૈરાગ્ય જેના મનમાં રે,
                રામનામ શૂં તાળી રે લાગી, સકળ તીરથ તેના તનમાં રે... વૈષ્ણવ જન તો તેને રે કહીએ.
            """.trimIndent(),
            meaningGujarati = "ભક્ત કવિ નરસિંહ મહેતા રચિત આ અમર ભજનમાં સાચા ઈશ્વરભક્તના સદ્ગુણો દર્શાવવામાં આવ્યા છે.",
            baseFrequencyHz = 432f,
            rawResId = com.example.R.raw.bhajan_vaishnav_jan_to,
            isHumanVocal = true,
            imageResId = com.example.R.drawable.img_darshan_krishna_1793948267799
        ),
        AudioTrack(
            id = "mantra_gayatri",
            titleGujarati = "ગાયત્રી મંત્ર (વેદિક સ્વર પાઠ)",
            deityNameGujarati = "માતા ગાયત્રી",
            category = ContentCategory.MANTRA,
            durationSeconds = 21,
            lyricsGujarati = """
                ૐ ભૂર્ભુવઃ સ્વઃ |
                તત્સવિતુર્વરેણ્યં ભર્ગો દેવસ્ય ધીમહિ |
                ધિયો યો નઃ પ્રચોદયાત્ ||
            """.trimIndent(),
            meaningGujarati = "હે સર્વજ્ઞ, પ્રાણસ્વરૂપ, દુઃખનાશક અને સુખપ્રદાતા પરમાત્મા! આપના તેજસ્વી દિવ્ય પ્રકાશને અમે ધારણ કરીએ છીએ, જે અમારી બુદ્ધિને સન્માર્ગે પ્રેરિત કરે.",
            baseFrequencyHz = 432f,
            rawResId = com.example.R.raw.gayatri_vedic_chant,
            isHumanVocal = true,
            imageResId = com.example.R.drawable.img_deity_ambaji_1793958532560
        ),
        AudioTrack(
            id = "mantra_mahamrityunjaya",
            titleGujarati = "મહામૃત્યુંજય મંત્ર (સ્વામી વૈદિક પાઠ)",
            deityNameGujarati = "ભગવાન શિવ",
            category = ContentCategory.MANTRA,
            durationSeconds = 240,
            lyricsGujarati = """
                ૐ ત્ર્યંબકં યજામહે સુગંધિં પુષ્ટિવર્ધનમ્ |
                ઉર્વારુકમિવ બંધનાન્મૃત્યોર્મુક્ષીય મામૃતાત્ ||
            """.trimIndent(),
            meaningGujarati = "અમે ત્રિનેત્રધારી, સુગંધીયુક્ત અને સર્વનું પોષણ કરનાર શિવજીની આરાધના કરીએ છીએ. જેવી રીતે પાકેલું કાકડી વેલાથી મુક્ત થાય છે, તેમ અમે મૃત્યુ અને સંસારના બંધનોમાંથી મુક્ત થઈ અમૃતત્વ પામીએ.",
            baseFrequencyHz = 432f,
            rawResId = com.example.R.raw.mantra_mahamrityunjaya,
            isHumanVocal = true,
            imageResId = com.example.R.drawable.img_darshan_shiva_1793948288707,
            isBonus = true
        ),
        AudioTrack(
            id = "dhun_hare_krishna",
            titleGujarati = "હરે કૃષ્ણ મહામંત્ર કીર્તન",
            deityNameGujarati = "શ્રી કૃષ્ણ",
            category = ContentCategory.DHUN,
            durationSeconds = 549,
            lyricsGujarati = """
                હરે કૃષ્ણ હરે કૃષ્ણ કૃષ્ણ કૃષ્ણ હરે હરે |
                હરે રામ હરે રામ રામ રામ હરે હરે ||
            """.trimIndent(),
            meaningGujarati = "કલિયુગમાં મનની શાંતિ અને મોક્ષ પ્રાપ્તિ માટેનું સર્વશ્રેષ્ઠ દિવ્ય મહામંત્ર સ્મરણ.",
            baseFrequencyHz = 432f,
            rawResId = com.example.R.raw.kirtan_hare_krishna_mahamantra,
            isHumanVocal = true,
            imageResId = com.example.R.drawable.img_darshan_krishna_1793948267799
        ),
        AudioTrack(
            id = "dhun_swaminarayan",
            titleGujarati = "સ્વામિનારાયણ મહામંત્ર ધૂન",
            deityNameGujarati = "સ્વામિનારાયણ",
            category = ContentCategory.DHUN,
            durationSeconds = 240,
            lyricsGujarati = """
                સ્વામિનારાયણ સ્વામિનારાયણ સ્વામિનારાયણ...
                સહજાનંદ સ્વામી હરિ હરિ બોલ...
                નારાયણ નારાયણ જય જય ગોવિંદ હરે...
                સ્વામિનારાયણ ભગવાન કી જય.
            """.trimIndent(),
            meaningGujarati = "શાંતિ અને આધ્યાત્મિક જાગૃતિ આપનાર પવિત્ર સ્વામિનારાયણ મંત્ર ધૂન.",
            baseFrequencyHz = 432f,
            rawResId = com.example.R.raw.dhun_swaminarayan,
            isHumanVocal = true,
            imageResId = com.example.R.drawable.img_deity_swaminarayan_1793958571996
        ),
        AudioTrack(
            id = "aarti_hanuman",
            titleGujarati = "આરતી કીજે હનુમાન લલા કી",
            deityNameGujarati = "હનુમાનજી",
            category = ContentCategory.AARTI,
            durationSeconds = 286,
            lyricsGujarati = """
                આરતી કીજે હનુમાન લલા કી,
                દુષ્ટ દલન રઘુનાથ કલા કી.
                
                જાકે બલ સે ગિરિવર કાંપે,
                રોગ દોષ જાકે ઢિંગ ન ચાંપે.
                
                અંજની પુત્ર મહા બલદાયી,
                સંતન કે પ્રભુ સદા સહાયી.
                
                દે બીરા રઘુનાથ પઠાયે,
                લંકા જારી સિયા સુધિ લાયે.
                
                લંકા સો કોટ સમુદ્ર સી ખાઈ,
                જાત પવનસુત બાર ન લાઈ.
                
                આરતી કીજે હનુમાન લલા કી,
                દુષ્ટ દલન રઘુનાથ કલા કી.
            """.trimIndent(),
            meaningGujarati = "કષ્ટભંજન શ્રી હનુમાન દાદાની સર્વ સંકટ અને ભય હરનારી દિવ્ય મંગલ આરતી.",
            baseFrequencyHz = 432f,
            rawResId = com.example.R.raw.aarti_kije_hanuman_lala,
            isHumanVocal = true,
            imageResId = com.example.R.drawable.img_darshan_hanuman_1793948310273
        ),
        AudioTrack(
            id = "aarti_krishna",
            titleGujarati = "આરતી કુંજબિહારી કી",
            deityNameGujarati = "શ્રી કૃષ્ણ",
            category = ContentCategory.AARTI,
            durationSeconds = 268,
            lyricsGujarati = """
                આરતી કુંજબિહારી કી,
                શ્રી ગિરિધર કૃષ્ણમુરારી કી.
                
                ગલે મેં બૈજંતી માલા,
                બજાવૈ મુરલી મધુર બાલા.
                
                શ્રવણ મેં કુંડલ ઝલકાલા,
                નંદ કે આનંદ નંદલાલા.
                
                ગગન સમ અંગ કાંતિ કાલિ,
                રાધિકા ચમક રહી આલી.
                
                લતન મેં ઠાઢે બનમાલી,
                ભ્રમર સી અલક કસ્તુરી તિલક.
                
                ચંદ્ર સી ઝલક લલિત છવિ શ્યામા પ્યારી કી,
                શ્રી ગિરિધર કૃષ્ણમુરારી કી.
            """.trimIndent(),
            meaningGujarati = "ભગવાન શ્રી કૃષ્ણચંદ્રની મનોહર કુંજબિહારી મંગલ આરતી.",
            baseFrequencyHz = 432f,
            rawResId = com.example.R.raw.aarti_kunj_bihari_ki,
            isHumanVocal = true,
            imageResId = com.example.R.drawable.img_darshan_krishna_1793948267799
        ),
        AudioTrack(
            id = "chalisa_hanuman_audio",
            titleGujarati = "શ્રી હનુમાન ચાલીસા (દિવ્ય ગાન)",
            deityNameGujarati = "હનુમાનજી",
            category = ContentCategory.CHALISA,
            durationSeconds = 587,
            lyricsGujarati = """
                શ્રીગુરુ ચરણ સરોજ રજ નિજ મનુ મુકુરુ સુધારિ,
                બરનઉઁ રઘુબર બિમલ જસુ જો દાયકુ ફલ ચારિ.
                
                બુદ્ધિહીન તનુ જાનિકે સુમિરૌં પવન-કુમાર,
                બલ બુદ્ધિ વિદ્યા દેહુ મોહિં હરહુ કલેસ બિકાર.
                
                જય હનુમાન જ્ઞાન ગુણ સાગર,
                જય કપીસ તિહું લોક ઉજાગર.
                
                રામદૂત અતુલિત બલધામા,
                અંજનીપુત્ર પવનસુત નામા.
                
                મહાવીર વિક્રમ બજરંગી,
                કુમતિ નિવાર સુમતિ કે સંગી.
                
                કંચન બરન બિરાજ સુબેસા,
                કાનન કુંડલ કુંચિત કેસા.
                
                હાથ બજ્ર ઔ ધ્વજા બિરાજૈ,
                કાંધે મૂંજ જનેઊ સાજૈ.
                
                શંકર સુવન કેસરીનંદન,
                તેજ પ્રતાપ મહા જગ વંદન.
                
                પવનતનય સંકટ હરન મંગલ મૂરતિ રૂપ,
                રામ લખન સીતા સહિત હૃદય બસહુ સુર ભૂપ.
            """.trimIndent(),
            meaningGujarati = "ગોસ્વામી તુલસીદાસજી રચિત સર્વ સંકટોનું નિવારણ કરનારી દિવ્ય હનુમાન ચાલીસા.",
            baseFrequencyHz = 432f,
            rawResId = com.example.R.raw.chalisa_shree_hanuman,
            isHumanVocal = true,
            imageResId = com.example.R.drawable.img_darshan_hanuman_1793948310273,
            isBonus = true
        ),
        AudioTrack(
            id = "aarti_ram",
            titleGujarati = "શ્રી રામચંદ્ર કૃપાલુ ભજુ મન",
            deityNameGujarati = "શ્રી રામ",
            category = ContentCategory.AARTI,
            durationSeconds = 118,
            lyricsGujarati = """
                શ્રી રામચંદ્ર કૃપાલુ ભજુ મન હરણ ભવભય દારુણમ્,
                નવકંજ લોચન કંજ મુખ કર કંજ પદ કંજારુણમ્.
                
                કંદર્પ અગણિત અમિત છવિ નવનીલ નીરદ સુંદરમ્,
                પટપીત માનહુ તડિત રુચિ શુચિ નૌમિ જનક સુતાવરમ્.
                
                ભજુ દીનબંધુ દિનેશ દાનવ દૈત્ય વંશ નિકંદનમ્,
                રઘુનંદ આનંદ કંદ કૌશલ ચંદ દશરથ નંદનમ્.
                
                મનુ જાહિં રાચેઉ મિલિહિ સો બરુ સહજ સુંદર સાંવરો,
                કરુણા નિધાન સુજાન સીલુ સનેહુ જાનત રાવરો.
            """.trimIndent(),
            meaningGujarati = "મર્યાદા પુરુષોત્તમ ભગવાન શ્રી રામની પરમ પાવન સ્તુતિ આરતી.",
            baseFrequencyHz = 432f,
            rawResId = com.example.R.raw.stuti_shri_ramchandra_kripalu,
            isHumanVocal = true,
            imageResId = com.example.R.drawable.img_deity_ram_1793958549091
        )
    )

    // ---------------------------------------------------------
    // Bhagavad Gita - All 18 Chapters with summaries and shlokas
    // ---------------------------------------------------------
    val gitaChapters: List<GitaChapter> = GitaData.chapters
    val keyShlokas: List<GitaShloka> = GitaData.keyShlokas

    // ---------------------------------------------------------
    // Devotional Scriptures & Chalisas (Hanuman Chalisa, Shiv Chalisa, Atharvashirsha, Durga Chalisa)
    // ---------------------------------------------------------
    val scriptures: List<DevotionalScripture> = ScripturesData.allScriptures

    // ---------------------------------------------------------
    // Gujarat Sacred Temples Directory
    // ---------------------------------------------------------
    val temples: List<TempleInfo> = listOf(
        TempleInfo(
            id = "temple_dwarka",
            nameGujarati = "શ્રી દ્વારકાધીશ જગત મંદિર",
            location = "દ્વારકા, દેવભૂમિ દ્વારકા",
            deity = "શ્રી કૃષ્ણ (દ્વારકાધીશ)",
            historicalOverview = "મોક્ષપુરી દ્વારકામાં ગોમતી નદીના તટે સ્થિત ૫૨ ગજની ધ્વજાવાળું આ પવિત્ર મંદિર ભગવાન શ્રીકૃષ્ણની પ્રાચીન રાજધાની છે.",
            darshanTimings = "સવારે ૬:૩૦ થી બપોરે ૧:૦૦ અને સાંજે ૫:૦૦ થી રાત્રે ૯:૩૦",
            aartiTimings = "મંગળા: સવારે ૬:૩૦, શ્રુંગાર: ૧૦:૩૦, સંધ્યા: ૭:૩૦, શયન: ૮:૩૦",
            address = "જગત મંદિર માર્ગ, દ્વારકા, ગુજરાત - ૩૬૧૩૩૫",
            websiteUrl = "https://dwarkadhish.org",
            highlightsGujarati = listOf("૫૨ ગજની દિવ્ય ધ્વજા", "ગોમતી ઘાટ સ્નાન", "રૂક્ષ્મણીજી મંદિર નજીક", "ચાર ધામ પૈકીનું પશ્ચિમ ધામ"),
            imageResId = com.example.R.drawable.img_temple_dwarka_1793948331429
        ),
        TempleInfo(
            id = "temple_somnath",
            nameGujarati = "શ્રી સોમનાથ જ્યોતિર્લિંગ મંદિર",
            location = "પ્રભાસ પાટણ, ગીર સોમનાથ",
            deity = "ભગવાન શિવ (સોમનાથ)",
            historicalOverview = "ભારતના બાર આદ્ય જ્યોતિર્લિંગોમાં પ્રથમ જ્યોતિર્લિંગ. સમુદ્રતટે બિરાજતા સોમનાથ મહાદેવ અખંડ આસ્થાનું કેન્દ્ર છે.",
            darshanTimings = "સવારે ૬:૦૦ થી રાત્રે ૧૦:૦૦ (સતત દર્શન)",
            aartiTimings = "પ્રાતઃ આરતી: સવારે ૭:૦૦, મધ્યાહ્ન: બપોરે ૧૨:૦૦, સાયં આરતી: સાંજે ૭:૦૦",
            address = "પ્રભાસ પાટણ, વેરાવળ, ગુજરાત - ૩૬૨૨૬૮",
            websiteUrl = "https://somnath.org",
            highlightsGujarati = listOf("પ્રથમ જ્યોતિર્લિંગ", "બાણસ્તંભ (દક્ષિણ ધ્રુવ સીધો માર્ગ)", "સાંજે ભવ્ય લાઈટ એન્ડ સાઉન્ડ શો", "ત્રિવેણી સંગમ"),
            imageResId = com.example.R.drawable.img_darshan_shiva_1793948288707
        ),
        TempleInfo(
            id = "temple_ambaji",
            nameGujarati = "શ્રી આરાસુરી અંબાજી માતા મંદિર",
            location = "અંબાજી, બનાસકાંઠા",
            deity = "માતા અંબા (આદ્યશક્તિ)",
            historicalOverview = "૫૧ શક્તિપીઠોમાં હૃદયસ્થાન સમાન અંબાજી. અહીં કોઈ મૂર્તિ નથી પરંતુ પવિત્ર 'શ્રી વિસાયંત્ર'ની પૂજા થાય છે.",
            darshanTimings = "સવારે ૭:૩૦ થી ૧૧:૩૦, બપોરે ૧૨:૩૦ થી ૪:૩૦, સાંજે ૬:૩૦ થી ૯:૦૦",
            aartiTimings = "સવારે ૭:૩૦ અને સાંજે ૬:૩૦",
            address = "અંબાજી, તા. દાંતા, જિ. બનાસકાંઠા, ગુજરાત - ૩૮૫૧૧૦",
            websiteUrl = "https://ambajitemple.in",
            highlightsGujarati = listOf("શ્રી વિસાયંત્ર પૂજન", "ગબ્બર પર્વત અખંડ જ્યોત", "ભાદરવી પૂનમનો મહામેળો", "સુવર્ણ શિખર"),
            imageResId = com.example.R.drawable.img_deity_ambaji_1793958532560
        ),
        TempleInfo(
            id = "temple_dakor",
            nameGujarati = "શ્રી રણછોડરાયજી મંદિર ડાકોર",
            location = "ડાકોર, ખેડા",
            deity = "રણછોડરાયજી (શ્રી કૃષ્ણ)",
            historicalOverview = "ભક્ત બોડાણાની અપ્રતિમ ભક્તિથી પ્રસન્ન થઈ દ્વારકાથી સ્વયં પધારેલા ઠાકોરજી શ્રી રણછોડરાયજીનું પ્રસિદ્ધ યાત્રાધામ.",
            darshanTimings = "સવારે ૬:૪૫ થી બપોરે ૧૨:૦૦ અને સાંજે ૪:૧૫ થી ૭:૩૦",
            aartiTimings = "મંગળા: ૬:૪૫, શ્રુંગાર: ૮:૩૦, ભોગ: ૧૦:૩૦, સંધ્યા: ૫:૩૦",
            address = "ડાકોર, તા. ઠાસરા, જિ. ખેડા, ગુજરાત - ૩૮૮૨૨૫",
            websiteUrl = "https://dabortemple.org",
            highlightsGujarati = listOf("ગોમતી તળાવ", "રણછોડરાયજી મૂર્તિ તુલા", "ડાકોરના પ્રસિદ્ધ ગોટા", "પૂનમનો ભવ્ય મેળો"),
            imageResId = com.example.R.drawable.img_darshan_krishna_1793948267799
        ),
        TempleInfo(
            id = "temple_salangpur",
            nameGujarati = "શ્રી કષ્ટભંજન દેવ હનુમાનજી મંદિર",
            location = "સારંગપુર, બોટાદ",
            deity = "શ્રી હનુમાનજી મહારાજ",
            historicalOverview = "ગોપાળાનંદ સ્વામી દ્વારા પ્રતિષ્ઠિત સાક્ષાત્ ચૈતન્ય કષ્ટભંજન હનુમાનજી. શનિવાર અને મંગળવારે લાખો ભક્તો આવે છે.",
            darshanTimings = "સવારે ૫:૩૦ થી બપોરે ૧૨:૦૦ અને બપોરે ૩:૦૦ થી રાત્રે ૯:૦૦",
            aartiTimings = "મંગળા: ૫:૩૦, શ્રુંગાર: ૭:૦૦, સંધ્યા: ૭:૦૦",
            address = "સારંગપુર, તા. બરવાળા, જિ. બોટાદ, ગુજરાત - ૩૮૨૪૫૦",
            websiteUrl = "https://salangpurhanumanji.org",
            highlightsGujarati = listOf("કિંગ ઑફ સાળંગપુર ૫૪ ફૂટ પ્રતિમા", "નિત્ય દિવ્ય શણગાર", "સંકટ નિવારણ ધામ", "સુવર્ણ સિંહાસન"),
            imageResId = com.example.R.drawable.img_darshan_hanuman_1793948310273
        ),
        TempleInfo(
            id = "temple_akshardham",
            nameGujarati = "બીએપીએસ સ્વામિનારાયણ અક્ષરધામ",
            location = "ગાંધીનગર",
            deity = "ભગવાન સ્વામિનારાયણ",
            historicalOverview = "ભારતીય સંસ્કૃતિ, શિલ્પકળા અને આધ્યાત્મિકતાનો અદ્ભુત સંગમ. પિંક સેન્ડસ્ટોનથી કંડારાયેલું ભવ્ય પરિસર.",
            darshanTimings = "સવારે ૯:૩૦ થી સાંજે ૭:૩૦ (સોમવારે બંધ)",
            aartiTimings = "સવારે ૧૦:૦૦ અને સાંજે ૬:૦૦",
            address = "સેક્ટર ૨૦, જે રોડ, ગાંધીનગર, ગુજરાત - ૩૮૨૦૨૦",
            websiteUrl = "https://akshardham.com",
            highlightsGujarati = listOf("સત-ચિત્-આનંદ વૉટર શો", "સહજાનંદ વન બગીચો", "આર્ષ રિસર્ચ સેન્ટર", "ભારતીય સંસ્કૃતિ પ્રદર્શન"),
            imageResId = com.example.R.drawable.img_deity_swaminarayan_1793958571996
        ),
        TempleInfo(
            id = "temple_pavagadh",
            nameGujarati = "શ્રી મહાકાળી માતાજી મંદિર પાવાગઢ",
            location = "પાવાગઢ, પંચમહાલ",
            deity = "માતા મહાકાળી",
            historicalOverview = "પાવાગઢ પર્વતની ટોચ પર આવેલું શક્તિપીઠ. માન્યતા છે કે અહીં સતીના જમણા પગની આંગળી પડી હતી.",
            darshanTimings = "સવારે ૬:૦૦ થી સાંજે ૭:૩૦",
            aartiTimings = "સવારે ૬:૩૦ અને સાંજે ૭:૦૦",
            address = "પાવાગઢ પર્વત, ચાંપાનેર, પંચમહાલ, ગુજરાત - ૩૮૯૩૬૦",
            websiteUrl = "https://pavagadh.org",
            highlightsGujarati = listOf("રોપ-વે સુવિધા (ઉડન ખટોલા)", "સુવર્ણ ધ્વજારોહણ શિખર", "ચાંપાનેર વર્લ્ડ હેરિટેજ", "દૂધિયું તળાવ"),
            imageResId = com.example.R.drawable.img_deity_ambaji_1793958532560
        )
    )

    // ---------------------------------------------------------
    // Major Hindu Festivals with countdown & details
    // ---------------------------------------------------------
    val festivals: List<HinduFestival> = listOf(
        HinduFestival("fest_diwali", "દિવાળી (દીપાવલી)", "આસો વદ અમાસ", "વિક્રમ સંવત આસો વદ અમાસ", 12, "અંધકાર પર પ્રકાશ અને અસત્ય પર સત્યના વિજયનો મહાપર્વ. ઘરોમાં દીપ પ્રગટાવી લક્ષ્મીજીનું પૂજન કરવામાં આવે છે.", "લક્ષ્મી પૂજન, ચોપડા પૂજન, દીપ પ્રાગટ્ય, રંગોળી"),
        HinduFestival("fest_besuvarsh", "બેસતું વર્ષ (નૂતન વર્ષ)", "કાર્તક સુદ એકમ", "ગુજરાતી નવું વર્ષ", 13, "ગુજરાતીઓનું નવું વર્ષ. વડીલોના આશીર્વાદ, સ્નેહીજનોને 'સાલમુબારક' અને અન્નકૂટ દર્શનનો દિવસ.", "અન્નકૂટ દર્શન, વડીલોના આશીર્વાદ, સાલમુબારક મિલન"),
        HinduFestival("fest_navratri", "આસો નવરાત્રિ મહોત્સવ", "આસો સુદ એકમથી નોમ", "આસો સુદ ૦૧ થી ૦૯", 35, "જગદંબાની આરાધના અને વિશ્વપ્રસિદ્ધ ગરબાનો ૯ દિવસનો મહા ઉત્સવ.", "ગરબા, અખંડ દીપ સ્થાપના, કન્યા પૂજન, હવન"),
        HinduFestival("fest_janmashtami", "શ્રી કૃષ્ણ જન્માષ્ટમી", "શ્રાવણ વદ આઠમ", "શ્રાવણ વદ ૦૮", 140, "ભગવાન શ્રીકૃષ્ણનો મધ્યરાત્રિએ જન્મોત્સવ. દ્વારકા અને ડાકોરમાં લાખો ભક્તો દર્શને ઉમટે છે.", "ઉપવાસ, રાત્રે ૧૨ વાગ્યે આરતી, મટકી ફોડ, પંજરી પ્રસાદ"),
        HinduFestival("fest_shivratri", "મહાશિવરાત્રી", "મહા વદ તેરસ", "મહા વદ ૧૩", 220, "ભોળાનાથ શિવ અને માતા પાર્વતીના વિવાહનો મહા મંગલ પર્વ. સોમનાથ અને ભવનાથ જૂનાગઢમાં ભવ્ય મેળો.", "ચાર પ્રહર પૂજા, બિલ્વપત્ર અર્પણ, શિવ તાંડવ સ્તોત્ર પાઠ"),
        HinduFestival("fest_ramnavami", "શ્રી રામ નવમી", "ચૈત્ર સુદ નોમ", "ચૈત્ર સુદ ૦૯", 260, "મર્યાદા પુરુષોત્તમ ભગવાન શ્રી રામચંદ્રજીનો પ્રાગટ્ય મહોત્સવ.", "બપોરે ૧૨ વાગ્યે પ્રાગટ્ય આરતી, રામચરિતમાનસ અખંડ પાઠ"),
        HinduFestival("fest_hanuman_jayanti", "શ્રી હનુમાન જયંતી", "ચૈત્ર સુદ પૂર્ણિમા", "ચૈત્ર સુદ ૧૫", 266, "પવનપુત્ર હનુમાનજીનો જન્મોત્સવ. કષ્ટભંજન સારંગપુરમાં વિશેષ આરાધના.", "સિંદૂર પૂજન, હનુમાન ચાલીસા પાઠ, બુંદી પ્રસાદ"),
        HinduFestival("fest_uttarayan", "ઉત્તરાયણ (મકરસંક્રાંતિ)", "૧૪ જાન્યુઆરી", "સૂર્ય મકર રાશિ પ્રવેશ", 68, "સૂર્યનારાયણનું ઉત્તરાયણ ગમન. પતંગોત્સવ, દાન-પુણ્ય અને તલ-ગોળનું મહાત્મ્ય.", "ગાયોને ઘાસ, દાન-પુણ્ય, પતંગ ચગાવવી, તલસાંકળી")
    )

    // ---------------------------------------------------------
    // Devotional Wallpapers & Suvichar Studio
    // ---------------------------------------------------------
    val wallpapers: List<WallpaperItem> = listOf(
        WallpaperItem("wall_1", "મંગલ દર્શન", "કર્મ એ જ પૂજા છે, અને ઈશ્વરમાં શ્રદ્ધા એ જ જીવનની સાચી શાંતિ છે.", "શ્રી કૃષ્ણ", listOf(0xFFE65100, 0xFFFFB300), com.example.R.drawable.img_darshan_krishna_1793948267799),
        WallpaperItem("wall_2", "મહાદેવ કૃપા", "જેના મનમાં ભોળાનાથનું સ્મરણ છે, તેને સંસારનો કોઈ ભય સ્પર્શી શકતો નથી.", "ભગવાન શિવ", listOf(0xFF004D40, 0xFF00897B), com.example.R.drawable.img_darshan_shiva_1793948288707),
        WallpaperItem("wall_3", "સંકટમોચન", "શ્રદ્ધા અને સમર્પણથી કરેલી પ્રાર્થના ક્યારેય નિષ્ફળ જતી નથી.", "હનુમાનજી", listOf(0xFFD84315, 0xFFFF7043), com.example.R.drawable.img_darshan_hanuman_1793948310273),
        WallpaperItem("wall_4", "માતૃકૃપા", "માઁ અંબાની છત્રછાયામાં પ્રત્યેક દિવસ સુરક્ષિત અને મંગલમય બને છે.", "માતા અંબા", listOf(0xFF880E4F, 0xFFE91E63), com.example.R.drawable.img_deity_ambaji_1793958532560),
        WallpaperItem("wall_5", "શાંતિ સંદેશ", "મન શાંત હોય તો પરિસ્થિતિ આપોઆપ અનુકૂળ બની જાય છે.", "શ્રી રામ", listOf(0xFF1A237E, 0xFF3949AB), com.example.R.drawable.img_deity_ram_1793958549091),
        WallpaperItem("wall_6", "વિઘ્નહર્તા મંગલ", "શ્રી ગણેશજીના સ્મરણથી જીવનના સર્વ વિઘ્નો દૂર થાય છે અને રિદ્ધિ-સિદ્ધિ પ્રાપ્ત થાય છે.", "શ્રી ગણેશ", listOf(0xFF880E4F, 0xFFC2185B), com.example.R.drawable.img_deity_ganesh_1793958512473),
        WallpaperItem("wall_7", "પરમ શાંતિ", "શુદ્ધ અંતઃકરણ અને પરમાત્મામાં અનન્ય નિષ્ઠા એ જ સાચી મુક્તિ છે.", "સ્વામિનારાયણ", listOf(0xFFE65100, 0xFFFFA000), com.example.R.drawable.img_deity_swaminarayan_1793958571996)
    )
}
