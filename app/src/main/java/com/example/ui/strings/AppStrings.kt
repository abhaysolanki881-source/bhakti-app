package com.example.ui.strings

import androidx.compose.runtime.compositionLocalOf

val LocalAppLanguage = compositionLocalOf { "gu" }

object AppStrings {

    // -------------------------------------------------------------
    // App Branding & Navigation
    // -------------------------------------------------------------
    fun appTitle(lang: String): String = when (lang) {
        "hi" -> "भक्ति सागर"
        "en" -> "Bhakti Sagar"
        else -> "ભક્તિ સાગર"
    }

    fun appSubtitle(lang: String): String = when (lang) {
        "hi" -> "आपके मोबाइल में एक डिजिटल मंदिर"
        "en" -> "A Sacred Digital Temple on Your Mobile"
        else -> "તમારા મોબાઇલમાં એક ડિજિટલ મંદિર"
    }

    fun tabHome(lang: String): String = when (lang) {
        "hi" -> "होम"
        "en" -> "Home"
        else -> "હોમ"
    }

    fun tabListen(lang: String): String = when (lang) {
        "hi" -> "सुनिए"
        "en" -> "Listen"
        else -> "સાંભળો"
    }

    fun tabRead(lang: String): String = when (lang) {
        "hi" -> "पढ़िए"
        "en" -> "Read"
        else -> "વાંચો"
    }

    fun tabPanchang(lang: String): String = when (lang) {
        "hi" -> "पंचांग"
        "en" -> "Panchang"
        else -> "પંચાંગ"
    }

    fun tabMore(lang: String): String = when (lang) {
        "hi" -> "अधिक"
        "en" -> "More"
        else -> "વધુ"
    }

    // -------------------------------------------------------------
    // Common Actions & Labels
    // -------------------------------------------------------------
    fun share(lang: String): String = when (lang) {
        "hi" -> "शेयर"
        "en" -> "Share"
        else -> "શેર"
    }

    fun back(lang: String): String = when (lang) {
        "hi" -> "वापस"
        "en" -> "Back"
        else -> "પાછા જાઓ"
    }

    fun cancel(lang: String): String = when (lang) {
        "hi" -> "रद्द करें"
        "en" -> "Cancel"
        else -> "રદ કરો"
    }

    fun confirm(lang: String): String = when (lang) {
        "hi" -> "हाँ, रीसेट करें"
        "en" -> "Yes, Reset"
        else -> "હા, રીસેટ કરો"
    }

    fun viewAll(lang: String): String = when (lang) {
        "hi" -> "सभी देखें"
        "en" -> "View All"
        else -> "બધા જુઓ"
    }

    fun listenNow(lang: String): String = when (lang) {
        "hi" -> "सुनें"
        "en" -> "Listen"
        else -> "સાંભળો"
    }

    fun darshanKaro(lang: String): String = when (lang) {
        "hi" -> "दर्शन करें"
        "en" -> "View Darshan"
        else -> "દર્શન કરો"
    }

    fun bigDarshan(lang: String): String = when (lang) {
        "hi" -> "विशाल दर्शन"
        "en" -> "Full Darshan"
        else -> "મોટું દર્શન કરો"
    }

    // -------------------------------------------------------------
    // Home Screen Sections
    // -------------------------------------------------------------
    fun todayDarshanTitle(lang: String): String = when (lang) {
        "hi" -> "आज का दिव्य दर्शन"
        "en" -> "Today's Divine Darshan"
        else -> "આજનું દિવ્ય દર્શન"
    }

    fun todayDarshanSubtitle(lang: String): String = when (lang) {
        "hi" -> "आज का मंगल दर्शन और आशीर्वाद"
        "en" -> "Today's Sacred Blessing & Darshan"
        else -> "આજનું મંગલ દર્શન અને આશીર્વાદ"
    }

    fun dayStripTitle(lang: String): String = when (lang) {
        "hi" -> "सातों दिन के विशेष दर्शन"
        "en" -> "7-Day Sacred Darshan"
        else -> "સાત વાર વિશેષ દર્શન"
    }

    fun todayBadge(lang: String): String = when (lang) {
        "hi" -> "आज"
        "en" -> "Today"
        else -> "આજે"
    }

    fun quickToolsTitle(lang: String): String = when (lang) {
        "hi" -> "दैनिक साधना टूल्स"
        "en" -> "Daily Devotional Tools"
        else -> "દૈનિક સાધના"
    }

    fun toolJapMala(lang: String): String = when (lang) {
        "hi" -> "जाप माला"
        "en" -> "Jap Mala"
        else -> "જાપ માળા"
    }

    fun toolJapMalaSub(lang: String): String = when (lang) {
        "hi" -> "१०८ मनके"
        "en" -> "108 Beads"
        else -> "૧૦૮ મણકા"
    }

    fun toolGita(lang: String): String = when (lang) {
        "hi" -> "भगवद् गीता"
        "en" -> "Bhagavad Gita"
        else -> "ભગવદ્ ગીતા"
    }

    fun toolGitaSub(lang: String): String = when (lang) {
        "hi" -> "१८ अध्याय"
        "en" -> "18 Chapters"
        else -> "૧૮ અધ્યાય"
    }

    fun toolTemples(lang: String): String = when (lang) {
        "hi" -> "तीर्थ दर्शन"
        "en" -> "Temples"
        else -> "તીર્થ દર્શન"
    }

    fun toolTemplesSub(lang: String): String = when (lang) {
        "hi" -> "पवित्र धाम"
        "en" -> "Gujarat Dhams"
        else -> "ગુજરાત ધામ"
    }

    fun todayPanchangTitle(lang: String): String = when (lang) {
        "hi" -> "आज का पंचांग"
        "en" -> "Today's Panchang"
        else -> "આજનું પંચાંગ"
    }

    fun viewFullPanchang(lang: String): String = when (lang) {
        "hi" -> "संपूर्ण पंचांग देखें"
        "en" -> "View Full Panchang"
        else -> "સંપૂર્ણ પંચાંગ જુઓ"
    }

    fun sunrise(lang: String): String = when (lang) {
        "hi" -> "सूर्योदय"
        "en" -> "Sunrise"
        else -> "સૂર્યોદય"
    }

    fun sunset(lang: String): String = when (lang) {
        "hi" -> "सूर्यास्त"
        "en" -> "Sunset"
        else -> "સૂર્યાસ્ત"
    }

    fun rahuKaal(lang: String): String = when (lang) {
        "hi" -> "राहुकाल"
        "en" -> "Rahu Kaal"
        else -> "રાહુકાળ"
    }

    fun abhijitMuhurat(lang: String): String = when (lang) {
        "hi" -> "अभिजित मुहूर्त"
        "en" -> "Abhijit Muhurat"
        else -> "અભિજિત મુહૂર્ત"
    }

    fun deityDarshanSection(lang: String): String = when (lang) {
        "hi" -> "देव दर्शन"
        "en" -> "Deity Darshan"
        else -> "દેવ દર્શન"
    }

    fun gitaShlokaBodh(lang: String): String = when (lang) {
        "hi" -> "गीता श्लोक बोध"
        "en" -> "Gita Verse of the Day"
        else -> "ગીતા શ્લોક બોધ"
    }

    fun chapter(lang: String): String = when (lang) {
        "hi" -> "अध्याय"
        "en" -> "Chapter"
        else -> "અધ્યાય"
    }

    fun shloka(lang: String): String = when (lang) {
        "hi" -> "श्लोक"
        "en" -> "Shloka"
        else -> "શ્લોક"
    }

    fun meaning(lang: String): String = when (lang) {
        "hi" -> "अर्थ:"
        "en" -> "Meaning:"
        else -> "ગુજરાતી અર્થ:"
    }

    fun lifeLesson(lang: String): String = when (lang) {
        "hi" -> "जीवन बोध:"
        "en" -> "Life Lesson:"
        else -> "જીવન બોધ:"
    }

    fun featuredAudioSection(lang: String): String = when (lang) {
        "hi" -> "पवित्र आरती एवं भजन"
        "en" -> "Sacred Aarti & Bhajan"
        else -> "પવિત્ર આરતી અને ભજન"
    }

    fun upcomingFestivalsSection(lang: String): String = when (lang) {
        "hi" -> "आगामी उत्सव एवं त्यौहार"
        "en" -> "Upcoming Festivals"
        else -> "આગામી ઉત્સવ અને તહેવારો"
    }

    fun daysLeft(count: Int, lang: String): String = when (lang) {
        "hi" -> if (count == 0) "आज है" else "$count दिन शेष"
        "en" -> if (count == 0) "Today" else "In $count days"
        else -> if (count == 0) "આજે છે" else "$count દિવસ બાકી"
    }

    // -------------------------------------------------------------
    // Days of Week
    // -------------------------------------------------------------
    fun dayNameShort(dayOfWeek: Int, lang: String): String = when (dayOfWeek) {
        java.util.Calendar.SUNDAY -> when (lang) { "hi" -> "रवि"; "en" -> "Sun"; else -> "રવિ" }
        java.util.Calendar.MONDAY -> when (lang) { "hi" -> "सोम"; "en" -> "Mon"; else -> "સોમ" }
        java.util.Calendar.TUESDAY -> when (lang) { "hi" -> "मंगल"; "en" -> "Tue"; else -> "મંગળ" }
        java.util.Calendar.WEDNESDAY -> when (lang) { "hi" -> "बुध"; "en" -> "Wed"; else -> "બુધ" }
        java.util.Calendar.THURSDAY -> when (lang) { "hi" -> "गुरु"; "en" -> "Thu"; else -> "ગુરુ" }
        java.util.Calendar.FRIDAY -> when (lang) { "hi" -> "शुक्र"; "en" -> "Fri"; else -> "શુક્ર" }
        java.util.Calendar.SATURDAY -> when (lang) { "hi" -> "शनि"; "en" -> "Sat"; else -> "શનિ" }
        else -> ""
    }

    fun dayDeityShort(dayOfWeek: Int, lang: String): String = when (dayOfWeek) {
        java.util.Calendar.SUNDAY -> when (lang) { "hi" -> "सूर्य देव"; "en" -> "Surya"; else -> "સૂર્યનારાયણ" }
        java.util.Calendar.MONDAY -> when (lang) { "hi" -> "महादेव"; "en" -> "Shiva"; else -> "મહાદેવ" }
        java.util.Calendar.TUESDAY -> when (lang) { "hi" -> "हनुमान जी"; "en" -> "Hanuman"; else -> "હનુમાનજી" }
        java.util.Calendar.WEDNESDAY -> when (lang) { "hi" -> "गणेश जी"; "en" -> "Ganesha"; else -> "ગણેશજી" }
        java.util.Calendar.THURSDAY -> when (lang) { "hi" -> "श्री विष्णु"; "en" -> "Vishnu"; else -> "શ્રી વિષ્ણુ" }
        java.util.Calendar.FRIDAY -> when (lang) { "hi" -> "माँ अम्बा"; "en" -> "Ambaji"; else -> "માં અંબા" }
        java.util.Calendar.SATURDAY -> when (lang) { "hi" -> "द्वारकाधीश"; "en" -> "Dwarkadhish"; else -> "દ્વારકાધીશ" }
        else -> ""
    }

    // -------------------------------------------------------------
    // Audio Library Screen
    // -------------------------------------------------------------
    fun audioLibraryTitle(lang: String): String = when (lang) {
        "hi" -> "दिव्य भक्ति संगीत"
        "en" -> "Divine Devotional Audio"
        else -> "દિવ્ય ભક્તિ સંગીત"
    }

    fun audioLibrarySubtitle(lang: String): String = when (lang) {
        "hi" -> "सभी पवित्र आरती, भजन, धुन और मंत्र"
        "en" -> "All Sacred Aartis, Bhajans, Dhuns & Mantras"
        else -> "સર્વ પવિત્ર આરતી, ભજન, ધૂન અને મંત્રો"
    }

    fun catAll(lang: String): String = when (lang) {
        "hi" -> "सभी"
        "en" -> "All"
        else -> "બધા"
    }

    fun catAarti(lang: String): String = when (lang) {
        "hi" -> "आरती"
        "en" -> "Aarti"
        else -> "આરતી"
    }

    fun catBhajan(lang: String): String = when (lang) {
        "hi" -> "भजन"
        "en" -> "Bhajan"
        else -> "ભજન"
    }

    fun catMantra(lang: String): String = when (lang) {
        "hi" -> "मंत्र"
        "en" -> "Mantra"
        else -> "મંત્ર"
    }

    fun catDhun(lang: String): String = when (lang) {
        "hi" -> "धुन"
        "en" -> "Dhun"
        else -> "ધૂન"
    }

    fun catStuti(lang: String): String = when (lang) {
        "hi" -> "સ્તુતિ/ચાલીસા"
        "en" -> "Stuti/Chalisa"
        else -> "સ્તુતિ/ચાલીસા"
    }

    // -------------------------------------------------------------
    // Reading & Gita Screen
    // -------------------------------------------------------------
    fun tabGita(lang: String): String = when (lang) {
        "hi" -> "श्रीमद् भगवद् गीता"
        "en" -> "Bhagavad Gita"
        else -> "શ્રીમદ્ ભગવદ્ ગીતા"
    }

    fun tabHanuman(lang: String): String = when (lang) {
        "hi" -> "श्री हनुमान चालीसा"
        "en" -> "Hanuman Chalisa"
        else -> "શ્રી હનુમાન ચાલીસા"
    }

    fun tabShiv(lang: String): String = when (lang) {
        "hi" -> "श्री शिव चालीसा"
        "en" -> "Shiv Chalisa"
        else -> "શ્રી શિવ ચાલીસા"
    }

    fun tabGanesh(lang: String): String = when (lang) {
        "hi" -> "गणेश अथर्वशीर्ष"
        "en" -> "Ganesh Atharvashirsha"
        else -> "ગણેશ અથર્વશીર્ષ"
    }

    fun tabDurga(lang: String): String = when (lang) {
        "hi" -> "अंबाजी स्तुति"
        "en" -> "Ambaji Stuti"
        else -> "અંબાજી સ્તુતિ"
    }

    fun fontSize(lang: String): String = when (lang) {
        "hi" -> "अक्षर आकार:"
        "en" -> "Font Size:"
        else -> "અક્ષર કદ:"
    }

    fun gitaBannerTitle(lang: String): String = when (lang) {
        "hi" -> "श्रीमद् भगवद् गीता (१८ अध्याय)"
        "en" -> "Shrimad Bhagavad Gita (18 Chapters)"
        else -> "શ્રીમદ્ ભગવદ્ ગીતા (૧૮ અધ્યાય)"
    }

    fun gitaBannerDesc(lang: String): String = when (lang) {
        "hi" -> "कुरुक्षेत्र के पावन संग्राम में भगवान श्रीकृष्ण द्वारा अर्जुन को दिया गया दिव्य जीवन बोध।"
        "en" -> "The eternal divine wisdom spoken by Lord Krishna to Arjuna on the sacred field of Kurukshetra."
        else -> "કુરુક્ષેત્રના પાવન સંગ્રામમાં ભગવાન શ્રીકૃષ્ણ દ્વારા અર્જુનને આપેલો દિવ્ય જીવન બોધ."
    }

    fun versesLabel(count: Int, lang: String): String = when (lang) {
        "hi" -> "$count श्लोक"
        "en" -> "$count Verses"
        else -> "$count શ્લોક"
    }

    fun chapterSummaryTitle(lang: String): String = when (lang) {
        "hi" -> "अध्याय सारांश:"
        "en" -> "Chapter Summary:"
        else -> "અધ્યાય સારાંશ:"
    }

    fun detailedBackgroundTitle(lang: String): String = when (lang) {
        "hi" -> "विस्तृत पृष्ठभूमि एवं दिव्य रहस्य:"
        "en" -> "Detailed Background & Divine Mystery:"
        else -> "વિસ્તૃત પૃષ્ઠભૂમિ અને દિવ્ય રહસ્ય:"
    }

    fun coreTeachingsTitle(lang: String): String = when (lang) {
        "hi" -> "मुख्य दिव्य जीवन बोध:"
        "en" -> "Core Divine Teachings:"
        else -> "મુખ્ય દિવ્ય જીવન બોધ:"
    }

    fun keyVersesHeader(count: Int, lang: String): String = when (lang) {
        "hi" -> "महत्वपूर्ण मूल श्लोक और अर्थ ($count):"
        "en" -> "Key Verses and Meanings ($count):"
        else -> "મહત્વપૂર્ણ મૂળ શ્લોક અને અર્થ ($count):"
    }

    fun pronunciation(lang: String): String = when (lang) {
        "hi" -> "उच्चारण:"
        "en" -> "Pronunciation:"
        else -> "ઉચ્ચારણ:"
    }

    fun prevChapter(lang: String): String = when (lang) {
        "hi" -> "अगामी"
        "en" -> "Prev"
        else -> "અગાઉનો"
    }

    fun nextChapter(lang: String): String = when (lang) {
        "hi" -> "अगला"
        "en" -> "Next"
        else -> "આગળનો"
    }

    fun bookmark(lang: String): String = when (lang) {
        "hi" -> "बुकमार्क"
        "en" -> "Bookmark"
        else -> "બુકમાર્ક"
    }

    // -------------------------------------------------------------
    // Panchang Screen
    // -------------------------------------------------------------
    fun panchangHeader(lang: String): String = when (lang) {
        "hi" -> "दैनिक वैदिक पंचांग"
        "en" -> "Daily Vedic Panchang"
        else -> "દૈનિક વૈદિક પંચાંગ"
    }

    fun panchangSubtitle(lang: String): String = when (lang) {
        "hi" -> "विक्रम संवत, तिथि, नक्षत्र, योग और शुभ मुहूर्त"
        "en" -> "Vikram Samvat, Tithi, Nakshatra, Yoga & Muhurat"
        else -> "વિક્રમ સંવત, તિથિ, નક્ષત્ર, યોગ અને શુભ મુહૂર્ત"
    }

    fun choghadiyaTitle(lang: String): String = when (lang) {
        "hi" -> "आज के शुभ चौघड़िया"
        "en" -> "Today's Auspicious Choghadiya"
        else -> "આજના શુભ ચોઘડિયા"
    }

    fun dayChoghadiya(lang: String): String = when (lang) {
        "hi" -> "दिन के चौघड़िया"
        "en" -> "Day Choghadiya"
        else -> "દિવસના ચોઘડિયા"
    }

    fun nightChoghadiya(lang: String): String = when (lang) {
        "hi" -> "रात्रि के चौघड़िया"
        "en" -> "Night Choghadiya"
        else -> "રાત્રિના ચોઘડિયા"
    }

    fun festivalCalendar(lang: String): String = when (lang) {
        "hi" -> "त्यौहार एवं व्रत कैलेंडर"
        "en" -> "Festivals & Vrats Calendar"
        else -> "તહેવારો અને વ્રત કેલેન્ડર"
    }

    fun tithi(lang: String): String = when (lang) {
        "hi" -> "तिथि"
        "en" -> "Tithi"
        else -> "તિથિ"
    }

    fun vaar(lang: String): String = when (lang) {
        "hi" -> "वार"
        "en" -> "Day"
        else -> "વાર"
    }

    fun nakshatra(lang: String): String = when (lang) {
        "hi" -> "नक्षत्र"
        "en" -> "Nakshatra"
        else -> "નક્ષત્ર"
    }

    fun yoga(lang: String): String = when (lang) {
        "hi" -> "योग"
        "en" -> "Yoga"
        else -> "યોગ"
    }

    fun karana(lang: String): String = when (lang) {
        "hi" -> "करण"
        "en" -> "Karana"
        else -> "કરણ"
    }

    fun paksha(lang: String): String = when (lang) {
        "hi" -> "पक्ष"
        "en" -> "Paksha"
        else -> "પક્ષ"
    }

    // -------------------------------------------------------------
    // More Screen & Utilities
    // -------------------------------------------------------------
    fun moreTitle(lang: String): String = when (lang) {
        "hi" -> "अधिक आध्यात्मिक सेवाएँ"
        "en" -> "More Spiritual Services"
        else -> "વધુ આધ્યાત્મિક સેવાઓ"
    }

    fun moreSubtitle(lang: String): String = when (lang) {
        "hi" -> "जाप माला, तीर्थ दर्शन, वॉलपेपर, सेटिंग्स"
        "en" -> "Jap Mala, Temples, Wallpapers & Settings"
        else -> "જાપ માળા, તીર્થ દર્શન, વૉલપેપર, સેટિંગ્સ"
    }

    fun japMalaTitle(lang: String): String = when (lang) {
        "hi" -> "डिजिटल जाप माला"
        "en" -> "Digital Jap Mala"
        else -> "ડિજિટલ જાપ માળા"
    }

    fun japMalaDesc(lang: String): String = when (lang) {
        "hi" -> "१०८ मनके माला से प्रभु नाम का जाप करें"
        "en" -> "Chant sacred mantras on 108 beads"
        else -> "૧૦૮ મણકા માળા સાથે પ્રભુ નામનો જાપ કરો"
    }

    fun templeDirectoryTitle(lang: String): String = when (lang) {
        "hi" -> "गुजरात तीर्थ दर्शन गाइड"
        "en" -> "Sacred Temple Guide"
        else -> "ગુજરાત તીર્થ દર્શન ગાઇડ"
    }

    fun templeDirectoryDesc(lang: String): String = when (lang) {
        "hi" -> "द्वारकाधीश, सोमनाथ, अंबाजी आदि पवित्र धाम"
        "en" -> "Dwarkadhish, Somnath, Ambaji & holy dhams"
        else -> "દ્વારકાધીશ, સોમનાથ, અંબાજી વગેરે પવિત્ર ધામ"
    }

    fun wallpaperStudioTitle(lang: String): String = when (lang) {
        "hi" -> "दिव्य वॉलपेपर एवं सुविचार"
        "en" -> "Devotional Wallpapers & Quotes"
        else -> "દિવ્ય વૉલપેપર અને સુવિચાર"
    }

    fun wallpaperStudioDesc(lang: String): String = when (lang) {
        "hi" -> "भगवान के दर्शन और सुविचार डाउनलोड व शेयर करें"
        "en" -> "Download & share divine darshan & quotes"
        else -> "ભગવાનના દર્શન અને સુવિચાર ડાઉનલોડ-શેર કરો"
    }

    fun privacyPolicyTitle(lang: String): String = when (lang) {
        "hi" -> "प्राइवेसी पॉलिसी"
        "en" -> "Privacy Policy"
        else -> "પ્રાઇવસી પોલિસી"
    }

    fun termsTitle(lang: String): String = when (lang) {
        "hi" -> "नियम एवं शर्तें"
        "en" -> "Terms & Conditions"
        else -> "નિયમો અને શરતો"
    }

    // -------------------------------------------------------------
    // Digital Jap Mala Screen
    // -------------------------------------------------------------
    fun bead(lang: String): String = when (lang) {
        "hi" -> "मनका"
        "en" -> "Bead"
        else -> "મણકો"
    }

    fun rounds(lang: String): String = when (lang) {
        "hi" -> "फेरे (Mala)"
        "en" -> "Rounds"
        else -> "ફેરા (Mala)"
    }

    fun totalChants(lang: String): String = when (lang) {
        "hi" -> "कुल जाप"
        "en" -> "Total Chants"
        else -> "કુલ જાપ"
    }

    fun tapToChant(lang: String): String = when (lang) {
        "hi" -> "मनके पर स्पर्श करके जाप आगे बढ़ाएं"
        "en" -> "Tap beads to advance chant"
        else -> "મણકા પર સ્પર્શ કરીને જાપ આગળ વધારો"
    }

    fun selectMantra(lang: String): String = when (lang) {
        "hi" -> "मंत्र चुनें:"
        "en" -> "Select Mantra:"
        else -> "મંત્ર પસંદ કરો:"
    }

    fun resetMala(lang: String): String = when (lang) {
        "hi" -> "माला रीसेट"
        "en" -> "Reset Mala"
        else -> "માળા રીસેટ"
    }

    // -------------------------------------------------------------
    // Offline Barrier Screen
    // -------------------------------------------------------------
    fun noInternetTitle(lang: String): String = when (lang) {
        "hi" -> "इंटरनेट कनेक्शन चेक करें"
        "en" -> "Check your internet connection"
        else -> "ઇન્ટરનેટ કનેક્શન તપાસો"
    }

    fun noInternetDesc(lang: String): String = when (lang) {
        "hi" -> "भक्ति सागर ऐप चलाने के लिए इंटरनेट चालू होना अनिवार्य है। कृपया अपने फोन में Wi-Fi या मोबाइल डेटा चालू करें।"
        "en" -> "An active internet connection is required to use Bhakti Sagar app. Please turn on Mobile Data or Wi-Fi."
        else -> "ભક્તિ સાગર એપ ચલાવવા માટે ઇન્ટરનેટ કનેક્શન ચાલુ હોવું જરૂરી છે. કૃપા કરીને આપના ફોનમાં Wi-Fi અથવા મોબાઇલ ડેટા ચાલુ કરો."
    }

    fun retryConnection(lang: String): String = when (lang) {
        "hi" -> "पुनः प्रयास करें"
        "en" -> "Retry Connection"
        else -> "ફરી પ્રયાસ કરો"
    }

    fun openInternetSettings(lang: String): String = when (lang) {
        "hi" -> "इंटरनेट सेटिंग्स खोलें"
        "en" -> "Open Network Settings"
        else -> "ઇન્ટરનેટ સેટિંગ્સ ખોલો"
    }

    // -------------------------------------------------------------
    // Settings Screen
    // -------------------------------------------------------------
    fun settingsTitle(lang: String): String = when (lang) {
        "hi" -> "सेटिंग्स"
        "en" -> "Settings"
        else -> "સેટિંગ્સ"
    }

    fun languageLabel(lang: String): String = when (lang) {
        "hi" -> "एप्लिकेशन भाषा (Language)"
        "en" -> "Application Language"
        else -> "એપ્લિકેશન ભાષા (Language)"
    }

    fun darkThemeLabel(lang: String): String = when (lang) {
        "hi" -> "डार्क थीम (रात्रि पठन)"
        "en" -> "Dark Theme (Night Reading)"
        else -> "ડાર્ક થીમ (રાત્રિ વાચન)"
    }

    fun darkThemeSub(lang: String): String = when (lang) {
        "hi" -> "कम रोशनी में आँखों के लिए आरामदायक"
        "en" -> "Comfortable for eyes in low light"
        else -> "ઓછી લાઈટમાં આંખો માટે આરામદાયક"
    }

    fun remindersTitle(lang: String): String = when (lang) {
        "hi" -> "नित्य पूजा और दर्शन स्मरण (Reminders)"
        "en" -> "Daily Prayer & Darshan Reminders"
        else -> "નિત્ય પૂજા અને દર્શન સ્મરણ"
    }

    fun morningReminder(lang: String): String = when (lang) {
        "hi" -> "प्रातः मंगला दर्शन स्मरण (सुबह ६:३૦)"
        "en" -> "Morning Darshan Reminder (6:30 AM)"
        else -> "પ્રાતઃ મંગળા દર્શન સ્મરણ (સવારે ૬:૩૦)"
    }

    fun eveningReminder(lang: String): String = when (lang) {
        "hi" -> "सायं संध्या आरती स्मरण (शाम ७:००)"
        "en" -> "Evening Aarti Reminder (7:00 PM)"
        else -> "સાયં સંધ્યા આરતી સ્મરણ (સાંજે ૭:૦૦)"
    }

    fun testReminderButton(lang: String): String = when (lang) {
        "hi" -> "🔔 स्मरण सूचना टेस्ट करें (Test Notification)"
        "en" -> "🔔 Test Notification Now"
        else -> "🔔 હમણાં સ્મરણ ટેસ્ટ કરો (Test Notification)"
    }

    fun testReminderSent(lang: String): String = when (lang) {
        "hi" -> "दर्शन स्मरण सूचना भेजी गई है!"
        "en" -> "Darshan reminder notification sent!"
        else -> "દર્શન સ્મરણ સૂચના મોકલાઈ ગઈ છે!"
    }

    fun panchangCityTitle(lang: String): String = when (lang) {
        "hi" -> "पंचांग हेतु शहर (Panchang City)"
        "en" -> "Panchang City"
        else -> "પંચાંગ માટે શહેર (Panchang City)"
    }

    fun resetMalaTitle(lang: String): String = when (lang) {
        "hi" -> "जाप माला इतिहास रीसेट करें"
        "en" -> "Reset Jap Mala History"
        else -> "જાપ માળા ઇતિહાસ રીસેટ કરો"
    }

    fun resetMalaConfirm(lang: String): String = when (lang) {
        "hi" -> "क्या आप निश्चित रूप से जाप माला के कुल फेरे और मनके शून्य करना चाहते हैं?"
        "en" -> "Are you sure you want to reset all completed Mala rounds and beads to zero?"
        else -> "શું આપ ખરેખર જાપ માળાના કુલ ફેરા અને મણકા શૂન્ય કરવા માંગો છો?"
    }

    fun clearFavoritesTitle(lang: String): String = when (lang) {
        "hi" -> "पसंदीदा सूची साफ़ करें"
        "en" -> "Clear Favorites & Bookmarks"
        else -> "મનપસંદ યાદી સાફ કરો"
    }

    fun clearFavoritesConfirm(lang: String): String = when (lang) {
        "hi" -> "क्या आप सभी सहेजे गए पसंदीदा भजन, आरती और बुकमार्क हटाना चाहते हैं?"
        "en" -> "Are you sure you want to remove all saved favorites and bookmarks?"
        else -> "શું આપ તમામ સાચવેલ મનપસંદ ભજન, આરતી અને બુકમાર્ક્સ દૂર કરવા માંગો છો?"
    }

    fun shareAppTitle(lang: String): String = when (lang) {
        "hi" -> "परिवार और मित्रों के साथ ऐप शेयर करें"
        "en" -> "Share App with Family & Friends"
        else -> "પરિવાર અને મિત્રો સાથે એપ શેર કરો"
    }

    fun shareAppText(lang: String): String = when (lang) {
        "hi" -> "भक्ति सागर - आपके मोबाइल में एक डिजिटल मंदिर। दैनिक दर्शन, आरती, भजन, भगवद् गीता और पंचांग के लिए डाउनलोड करें।"
        "en" -> "Bhakti Sagar - A Sacred Digital Temple on Your Mobile. Download for Daily Darshan, Aarti, Bhajan, Bhagavad Gita and Panchang."
        else -> "ભક્તિ સાગર - તમારા મોબાઇલમાં એક ડિજિટલ મંદિર. દૈનિક દર્શન, આરતી, ભજન, ભગવદ્ ગીતા અને પંચાંગ માટે આજે જ ડાઉનલોડ કરો."
    }

    fun feedbackTitle(lang: String): String = when (lang) {
        "hi" -> "सुझाव और फीडबैक दें"
        "en" -> "Send Feedback & Suggestions"
        else -> "સૂચન અને પ્રતિસાદ આપો"
    }

    fun aboutAppTitle(lang: String): String = when (lang) {
        "hi" -> "भक्ति सागर के बारे में"
        "en" -> "About Bhakti Sagar"
        else -> "ભક્તિ સાગર વિશે"
    }

    fun aboutAppDesc(lang: String): String = when (lang) {
        "hi" -> "यह एप्लिकेशन गुजरात और विश्वभर के श्रद्धालुओं के लिए दैनिक भक्ति, दर्शन, आरती, भजन, भगवद् गीता और पंचांग का एक पावन माध्यम है।"
        "en" -> "This application is a divine companion for Hindu devotees worldwide, offering Daily Darshan, Aarti, Bhajan, Bhagavad Gita, and authentic Gujarati Panchang."
        else -> "આ એપ્લિકેશન ગુજરાત અને વિશ્વભરના શ્રદ્ધાળુઓ માટે દૈનિક ભક્તિ, દર્શન, આરતી, ભજન, ભગવદ્ ગીતા અને પંચાંગનું એક પવિત્ર માધ્યમ છે."
    }

    fun favoritesTitle(lang: String): String = when (lang) {
        "hi" -> "पसंदीदा और बुकमार्क"
        "en" -> "Favorites & Bookmarks"
        else -> "મનપસંદ અને બુકમાર્ક્સ"
    }

    fun favoritesDesc(lang: String): String = when (lang) {
        "hi" -> "सहेजे गए भजन, आरती एवं गीता श्लोक"
        "en" -> "Saved Bhajans, Aartis & Gita Shlokas"
        else -> "સાચવેલ ભજન, આરતી અને ગીતા શ્લોક"
    }

    fun searchTitle(lang: String): String = when (lang) {
        "hi" -> "खोजें (आरती, भजन, श्लोक)"
        "en" -> "Search (Aarti, Bhajan, Shloka)"
        else -> "શોધો (આરતી, ભજન, શ્લોક)"
    }

    // -------------------------------------------------------------
    // Dynamic Model Name Translators
    // -------------------------------------------------------------
    fun getDeityName(deityId: String, defaultName: String, lang: String): String = when (deityId.lowercase()) {
        "krishna" -> when (lang) { "hi" -> "श्री कृष्ण"; "en" -> "Shree Krishna"; else -> defaultName }
        "shiva" -> when (lang) { "hi" -> "भगवान शिव"; "en" -> "Lord Shiva"; else -> defaultName }
        "hanuman" -> when (lang) { "hi" -> "हनुमान जी"; "en" -> "Hanuman Ji"; else -> defaultName }
        "ganesha", "ganesh" -> when (lang) { "hi" -> "श्री गणेश"; "en" -> "Shree Ganesha"; else -> defaultName }
        "ambaji", "mataji" -> when (lang) { "hi" -> "माँ अम्बाजी"; "en" -> "Mata Ambaji"; else -> defaultName }
        "ram" -> when (lang) { "hi" -> "श्री राम"; "en" -> "Shree Ram"; else -> defaultName }
        "vishnu" -> when (lang) { "hi" -> "भगवान विष्णु"; "en" -> "Lord Vishnu"; else -> defaultName }
        "swaminarayan" -> when (lang) { "hi" -> "स्वामीनारायण"; "en" -> "Swaminarayan"; else -> defaultName }
        else -> defaultName
    }

    fun getCategoryTitle(catName: String, lang: String): String = when (catName.uppercase()) {
        "AARTI" -> when (lang) { "hi" -> "आरती"; "en" -> "Aarti"; else -> "આરતી" }
        "BHAJAN" -> when (lang) { "hi" -> "भजन"; "en" -> "Bhajan"; else -> "ભજન" }
        "MANTRA" -> when (lang) { "hi" -> "मंत्र"; "en" -> "Mantra"; else -> "મંત્ર" }
        "DHUN" -> when (lang) { "hi" -> "धुन"; "en" -> "Dhun"; else -> "ધૂન" }
        "STOTRA" -> when (lang) { "hi" -> "स्तोत्र"; "en" -> "Stotra"; else -> "સ્તોત્ર" }
        "CHALISA" -> when (lang) { "hi" -> "चालीसा"; "en" -> "Chalisa"; else -> "ચાલીસા" }
        "GITA" -> when (lang) { "hi" -> "भगवद् गीता"; "en" -> "Bhagavad Gita"; else -> "ભગવદ્ ગીતા" }
        "TEMPLE" -> when (lang) { "hi" -> "मंदिर दर्शन"; "en" -> "Temple Darshan"; else -> "મંદિર દર્શન" }
        else -> catName
    }

    fun allDeitiesFilter(lang: String): String = when (lang) {
        "hi" -> "सभी देव दर्शन"
        "en" -> "All Deities"
        else -> "બધા દેવ દર્શન"
    }

    fun close(lang: String): String = when (lang) {
        "hi" -> "बंद करें"
        "en" -> "Close"
        else -> "બંધ કરો"
    }

    fun shareDarshan(lang: String): String = when (lang) {
        "hi" -> "दर्शन शेयर करें"
        "en" -> "Share Darshan"
        else -> "દર્શન શેર કરો"
    }

    fun templeLocation(lang: String): String = when (lang) {
        "hi" -> "स्थान:"
        "en" -> "Location:"
        else -> "સ્થળ:"
    }

    fun darshanTimings(lang: String): String = when (lang) {
        "hi" -> "दर्शन समय:"
        "en" -> "Darshan Timings:"
        else -> "દર્શન સમય:"
    }

    fun aartiTimings(lang: String): String = when (lang) {
        "hi" -> "आरती समय:"
        "en" -> "Aarti Timings:"
        else -> "આરતી સમય:"
    }

    fun address(lang: String): String = when (lang) {
        "hi" -> "पता:"
        "en" -> "Address:"
        else -> "સરનામું:"
    }

    fun viewOnMaps(lang: String): String = when (lang) {
        "hi" -> "गूगल मैप पर देखें"
        "en" -> "View on Google Maps"
        else -> "મેપ્સ પર જુઓ"
    }

    fun divineRecitationHeader(lang: String): String = when (lang) {
        "hi" -> "दिव्य स्तुति एवं वाणी पाठ"
        "en" -> "Divine Chanting & Prayers"
        else -> "દિવ્ય સ્તુતિ & વાણી પઠન"
    }

    fun humanVocalBadge(lang: String): String = when (lang) {
        "hi" -> "🎙️ पारंपरिक गायन (Divine Vocals)"
        "en" -> "🎙️ Traditional Vocals"
        else -> "🎙️ અસલ માનવ કંઠ (Divine Vocals)"
    }

    fun divineMusicBadge(lang: String): String = when (lang) {
        "hi" -> "🔔 दिव्य संगीत"
        "en" -> "🔔 Temple Chimes"
        else -> "🔔 દિવ્ય સંગીત"
    }

    fun shareOnWhatsApp(lang: String): String = when (lang) {
        "hi" -> "व्हाट्सएप पर शेयर करें"
        "en" -> "Share on WhatsApp"
        else -> "WhatsApp પર શેર કરો"
    }

    fun delete(lang: String): String = when (lang) {
        "hi" -> "हटाएं"
        "en" -> "Delete"
        else -> "કાઢી નાખો"
    }

    fun noFavorites(lang: String): String = when (lang) {
        "hi" -> "कोई पसंदीदा भजन या आरती सहेजी नहीं गई है"
        "en" -> "No favorite bhajans or aartis saved yet"
        else -> "કોઈ મનપસંદ ભજન સાચવેલ નથી"
    }

    fun noFavoritesSub(lang: String): String = when (lang) {
        "hi" -> "ऑडियो सूची में दिल (Heart) आइकन दबाकर पसंदीदा बनाएं"
        "en" -> "Tap the heart icon in the audio library to add favorites"
        else -> "ઓડિયો લિસ્ટમાં હાર્ટ આઈકોન દબાવીને ઉમેરો"
    }

    fun noBookmarks(lang: String): String = when (lang) {
        "hi" -> "कोई श्लोक बुकमार्क नहीं किया गया है"
        "en" -> "No Gita verses bookmarked yet"
        else -> "કોઈ બુકમાર્ક સાચવેલ નથી"
    }

    fun noBookmarksSub(lang: String): String = when (lang) {
        "hi" -> "गीता पठन के समय बुकमार्क आइकन दबाकर सहेजें"
        "en" -> "Tap the bookmark icon while reading Gita to save verses"
        else -> "ગીતા વાચન વખતે બુકમાર્ક આઈકોન દબાવીને સાચવો"
    }

    fun searchPlaceholder(lang: String): String = when (lang) {
        "hi" -> "खोजें (आरती, भजन, मंदिर, गीता)..."
        "en" -> "Search (Aarti, Bhajan, Temple, Gita)..."
        else -> "શોધો (આરતી, ભજન, મંદિર, ગીતા)..."
    }

    fun selectCityTitle(lang: String): String = when (lang) {
        "hi" -> "शहर चुनें"
        "en" -> "Select City"
        else -> "ગુજરાત શહેર પસંદ કરો"
    }

    fun meaningPrefix(lang: String): String = when (lang) {
        "hi" -> "अर्थ:"
        "en" -> "Meaning:"
        else -> "ગુજરાતી અર્થ:"
    }

    fun completeText(count: Int, lang: String): String = when (lang) {
        "hi" -> "सम्पूर्ण पाठ • $count पद / छंद"
        "en" -> "Complete Text • $count Verses"
        else -> "સંપૂર્ણ પાઠ • $count પદો / છંદ"
    }

    fun choghadiyaNote(lang: String): String = when (lang) {
        "hi" -> "नोट: अमृत, शुभ, लाभ और चल चौघड़िया शुभ कार्यों के लिए उत्तम माने जाते हैं।"
        "en" -> "Note: Amrit, Shubh, Labh, and Chal Choghadiya are considered auspicious."
        else -> "નોંધ: અમૃત, શુભ, લાભ અને ચલ ચોઘડિયા શુભ કાર્યો માટે ઉત્તમ ગણાય છે."
    }

    fun selectFestivalYear(lang: String): String = when (lang) {
        "hi" -> "त्योहार वर्ष चुनें (२०२६ से २०३०):"
        "en" -> "Select Festival Year (2026 - 2030):"
        else -> "તહેવાર વર્ષ પસંદ કરો (૨૦૨૬ થી ૨૦૩૦):"
    }

    fun festivalCalendarTitle(year: Int, lang: String): String = when (lang) {
        "hi" -> "हिंदू त्योहार कैलेंडर ($year):"
        "en" -> "Hindu Festival Calendar ($year):"
        else -> "ગુજરાતી હિન્દુ તહેવાર કેલેન્ડર ($year):"
    }

    fun getDayOfWeekTranslated(dayGujarati: String, lang: String): String = when (dayGujarati) {
        "રવિવાર" -> when (lang) { "hi" -> "रविवार"; "en" -> "Sunday"; else -> "રવિવાર" }
        "સોમવાર" -> when (lang) { "hi" -> "सोमवार"; "en" -> "Monday"; else -> "સોમવાર" }
        "મંગળવાર" -> when (lang) { "hi" -> "मंगलवार"; "en" -> "Tuesday"; else -> "મંગળવાર" }
        "બુધવાર" -> when (lang) { "hi" -> "बुधवार"; "en" -> "Wednesday"; else -> "બુધવાર" }
        "ગુરુવાર" -> when (lang) { "hi" -> "गुरुवार"; "en" -> "Thursday"; else -> "ગુરુવાર" }
        "શુક્રવાર" -> when (lang) { "hi" -> "शुक्रवार"; "en" -> "Friday"; else -> "શુક્રવાર" }
        "શનિવાર" -> when (lang) { "hi" -> "शनिवार"; "en" -> "Saturday"; else -> "શનિવાર" }
        "દૈનિક વિશેષ" -> when (lang) { "hi" -> "दैनिक विशेष"; "en" -> "Daily Special"; else -> "દૈનિક વિશેષ" }
        else -> dayGujarati
    }

    fun getMonthNameWithGujarati(month0: Int, lang: String): String = when (lang) {
        "en" -> listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        ).getOrElse(month0) { "" }
        "hi" -> listOf(
            "जनवरी (पौष - माघ)", "फ़रवरी (माघ - फाल्गुन)", "मार्च (फाल्गुन - चैत्र)", "अप्रैल (चैत्र - वैशाख)",
            "मई (वैशाख - ज्येष्ठ)", "जून (ज्येष्ठ - आषाढ़)", "जुलाई (आषाढ़ - श्रावण)", "अगस्त (श्रावण - भाद्रपद)",
            "सितंबर (भाद्रपद - आश्विन)", "अक्टूबर (आश्विन - कार्तिक)", "नवंबर (कार्तिक - मार्गशीर्ष)", "दिसंबर (मार्गशीर्ष - पौष)"
        ).getOrElse(month0) { "" }
        else -> listOf(
            "જાન્યુઆરી (પોષ - મહા)", "ફેબ્રુઆરી (મહા - ફાગણ)", "માર્ચ (ફાગણ - ચૈત્ર)", "એપ્રિલ (ચૈત્ર - વૈશાખ)",
            "મે (વૈશાખ - જેઠ)", "જૂન (જેઠ - અષાઢ)", "જુલાઈ (અષાઢ - શ્રાવણ)", "ઓગસ્ટ (શ્રાવણ - ભાદરવો)",
            "સપ્ટેમ્બર (ભાદરવો - આસો)", "ઓક્ટોબર (આસો - કારતક)", "નવેમ્બર (કારતક - માગશર)", "ડિસેમ્બર (માગશર - પોષ)"
        ).getOrElse(month0) { "" }
    }

    fun getWeekdayHeaders(lang: String): List<String> = when (lang) {
        "en" -> listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        "hi" -> listOf("रवि", "सोम", "मंगल", "बुध", "गुरु", "शुक्र", "शनि")
        else -> listOf("રવિ", "સોમ", "મંગળ", "બુધ", "ગુરુ", "શુક્ર", "શનિ")
    }

    // -------------------------------------------------------------
    // Rewarded Bonus Audio Strings
    // -------------------------------------------------------------
    fun bonusBadge(lang: String): String = when (lang) {
        "hi" -> "✨ बोनस"
        "en" -> "✨ Bonus"
        else -> "✨ બોનસ"
    }

    fun unlockedBadge(lang: String): String = when (lang) {
        "hi" -> "✓ अनलॉक"
        "en" -> "✓ Unlocked"
        else -> "✓ અનલૉક"
    }

    fun bonusAudioTitle(lang: String): String = when (lang) {
        "hi" -> "✨ विशेष भक्ति रचना अनलॉक करें"
        "en" -> "✨ Unlock Bonus Devotional Track"
        else -> "✨ વિશેષ બોનસ ભક્તિ રચના અનલૉક કરો"
    }

    fun bonusAudioMessage(trackTitle: String, lang: String): String = when (lang) {
        "hi" -> "'$trackTitle' एक विशेष भक्ति रचना है। एक संक्षिप्त प्रायोजित वीडियो देखकर इसे स्थायी रूप से अनलॉक करें। एक बार अनलॉक होने पर यह हमेशा निःशुल्क उपलब्ध रहेगी।"
        "en" -> "'$trackTitle' is an exclusive bonus devotional track. Watch a short sponsor video to permanently unlock it. Once unlocked, it is forever free to enjoy!"
        else -> "'$trackTitle' એક વિશેષ બોનસ ભક્તિ રચના છે. એક ટૂંકો પ્રાયોજિત વિડિઓ જોઈને તેને કાયમ માટે અનલૉક કરો. એકવાર અનલૉક થયા પછી તે હંમેશા ઉપલબ્ધ રહેશે!"
    }

    fun bonusAudioFreeNote(lang: String): String = when (lang) {
        "hi" -> "सामान्य ऑडियो एवं जप माला सदैव निःशुल्क उपलब्ध हैं।"
        "en" -> "Standard audio playback and Jap Mala remain 100% free."
        else -> "સામાન્ય ઑડિયો અને જાપ માળા હંમેશા ૧૦૦% મફત ઉપલબ્ધ છે."
    }

    fun bonusAudioWatchAndUnlock(lang: String): String = when (lang) {
        "hi" -> "वीडियो देखें और अनलॉक करें"
        "en" -> "Watch Video & Unlock"
        else -> "વીડિયો જુઓ અને અનલૉક કરો"
    }

    fun bonusAudioLoadingAd(lang: String): String = when (lang) {
        "hi" -> "वीडियो लोड हो रहा है, कृपया प्रतीक्षा करें..."
        "en" -> "Loading video, please wait..."
        else -> "વિડિઓ લોડ થઈ રહ્યો છે, કૃપા કરીને થોડીવાર રાહ જુઓ..."
    }

    fun bonusAudioUnlockedSuccess(trackTitle: String, lang: String): String = when (lang) {
        "hi" -> "🎉 '$trackTitle' हमेशा के लिए अनलॉक हो गया!"
        "en" -> "🎉 '$trackTitle' permanently unlocked!"
        else -> "🎉 '$trackTitle' કાયમ માટે અનલૉક થઈ ગયું!"
    }

    fun bonusAudioDismissedEarly(lang: String): String = when (lang) {
        "hi" -> "वीडियो अधूरा छोड़ दिया गया। बोनस रचना अनलॉक करने के लिए पूरा वीडियो देखना आवश्यक है।"
        "en" -> "Video was dismissed before finishing. Watching the full video is required to unlock this bonus track."
        else -> "વિડિઓ અધવચ્ચેથી બંધ કરવામાં આવ્યો હતો. બોનસ રચના અનલૉક કરવા માટે સંપૂર્ણ વિડિઓ જોવો આવશ્યક છે."
    }

    fun bonusAudioAdUnavailable(lang: String): String = when (lang) {
        "hi" -> "प्रायोजित वीडियो अभी उपलब्ध नहीं है। कृपया कुछ पलों बाद पुनः प्रयास करें।"
        "en" -> "Sponsored video is currently unavailable. Please try again in a few moments."
        else -> "પ્રાયોજિત વિડિઓ હાલમાં ઉપલબ્ધ નથી. કૃપા કરીને થોડી ક્ષણો પછી ફરી પ્રયાસ કરો."
    }

    fun bonusAudioAdFailed(reason: String, lang: String): String = when (lang) {
        "hi" -> "वीडियो प्रदर्शित करने में समस्या आई: $reason"
        "en" -> "Problem displaying video: $reason"
        else -> "વિડિઓ પ્રદર્શિત કરવામાં સમસ્યા આવી: $reason"
    }
}
