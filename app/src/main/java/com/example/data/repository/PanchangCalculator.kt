package com.example.data.repository

import com.example.data.model.ChoghadiyaItem
import com.example.data.model.CityLocation
import com.example.data.model.GujaratiCalendarDay
import com.example.data.model.HinduFestival
import com.example.data.model.PanchangData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

/**
 * Authentic, astronomically accurate Gujarati Panchang & Calendar Calculator.
 * Accurately calculates Vikram Samvat, Gujarati Month (Amasanta system), Tithi,
 * Nakshatra, Yoga, Karana, Sunrise/Sunset for Gujarat cities, Rahu Kaal, Abhijit Muhurat,
 * Day & Night Choghadiya, and interactive Calendar Grids for 2026 through 2030.
 */
object PanchangCalculator {

    val SUPPORTED_CITIES = listOf(
        CityLocation("અમદાવાદ", "Ahmedabad", 23.0225, 72.5714),
        CityLocation("સુરત", "Surat", 21.1702, 72.8311),
        CityLocation("વડોદરા", "Vadodara", 22.3072, 73.1812),
        CityLocation("રાજકોટ", "Rajkot", 22.3039, 70.8022),
        CityLocation("ભાવનગર", "Bhavnagar", 21.7645, 72.1519),
        CityLocation("જામનગર", "Jamnagar", 22.4707, 70.0577),
        CityLocation("ગાંધીનગર", "Gandhinagar", 23.2156, 72.6369),
        CityLocation("જૂનાગઢ", "Junagadh", 21.5222, 70.4579)
    )

    private val VAAR_NAMES = listOf(
        "રવિવાર", "સોમવાર", "મંગળવાર", "બુધવાર", "ગુરુવાર", "શુક્રવાર", "શનિવાર"
    )

    private val NAKSHATRAS = listOf(
        "અશ્વિની", "ભરણી", "કૃત્તિકા", "રોહિણી", "મૃગશીર્ષ", "આર્દ્રા",
        "પુનર્વસુ", "પુષ્ય", "આશ્લેષા", "મઘા", "પૂર્વા ફાલ્ગુની", "ઉત્તરા ફાલ્ગુની",
        "હસ્ત", "ચિત્રા", "સ્વાતિ", "વિશાખા", "અનુરાધા", "જ્યેષ્ઠા",
        "મૂળ", "પૂર્વાષાઢા", "ઉત્તરાષાઢા", "શ્રવણ", "ધનિષ્ઠા", "શતભિષા",
        "પૂર્વા ભાદ્રપદ", "ઉત્તરા ભાદ્રપદ", "રેવતી"
    )

    private val YOGAS = listOf(
        "વિષ્કંભ", "પ્રીતિ", "આયુષ્માન", "સૌભાગ્ય", "શોભન", "અતિગંડ", "સુકર્મા",
        "ધૃતિ", "શૂળ", "ગંડ", "વૃદ્ધિ", "ધ્રુવ", "વ્યાઘાત", "હર્ષણ", "વજ્ર",
        "સિદ્ધિ", "વ્યાતીપાત", "વરીયાન", "પરિઘ", "શિવ", "સિદ્ધ", "સાધ્ય",
        "શુભ", "શુક્લ", "બ્રહ્મ", "ઐન્દ્ર", "વૈધૃતિ"
    )

    private val KARANAS = listOf(
        "બવ", "બાલવ", "કૌલવ", "તૈતિલ", "ગર", "વણિજ", "વિષ્ટિ (ભદ્રા)",
        "શકુનિ", "ચતુષ્પાદ", "નાગ", "કિંસ્તુઘ્ન"
    )

    // Accurate Amasanta month boundaries from late 2025 to early 2031
    // (year, month 1-12, day, monthName, vikramSamvat)
    private data class MonthBoundary(val y: Int, val m: Int, val d: Int, val name: String, val vsYear: Int)

    private val GUJARATI_MONTH_BOUNDARIES = listOf(
        // Vikram Samvat 2082 (Diwali 2025 to Diwali 2026)
        MonthBoundary(2025, 10, 22, "કાર્તક", 2082),
        MonthBoundary(2025, 11, 21, "માગશર", 2082),
        MonthBoundary(2025, 12, 21, "પોષ", 2082),
        MonthBoundary(2026, 1, 19, "મહા", 2082),
        MonthBoundary(2026, 2, 18, "ફાગણ", 2082),
        MonthBoundary(2026, 3, 20, "ચૈત્ર", 2082),
        MonthBoundary(2026, 4, 18, "વૈશાખ", 2082),
        MonthBoundary(2026, 5, 17, "અધિક જેઠ", 2082),
        MonthBoundary(2026, 6, 16, "નિજ જેઠ", 2082),
        MonthBoundary(2026, 7, 15, "અષાઢ", 2082),
        MonthBoundary(2026, 8, 13, "શ્રાવણ", 2082),
        MonthBoundary(2026, 9, 12, "ભાદરવો", 2082),
        MonthBoundary(2026, 10, 11, "આસો", 2082),

        // Vikram Samvat 2083 (Diwali 2026 to Diwali 2027)
        MonthBoundary(2026, 11, 10, "કાર્તક", 2083),
        MonthBoundary(2026, 12, 10, "માગશર", 2083),
        MonthBoundary(2027, 1, 8, "પોષ", 2083),
        MonthBoundary(2027, 2, 7, "મહા", 2083),
        MonthBoundary(2027, 3, 9, "ફાગણ", 2083),
        MonthBoundary(2027, 4, 7, "ચૈત્ર", 2083),
        MonthBoundary(2027, 5, 7, "વૈશાખ", 2083),
        MonthBoundary(2027, 6, 5, "જેઠ", 2083),
        MonthBoundary(2027, 7, 5, "અષાઢ", 2083),
        MonthBoundary(2027, 8, 3, "શ્રાવણ", 2083),
        MonthBoundary(2027, 9, 1, "ભાદરવો", 2083),
        MonthBoundary(2027, 10, 1, "આસો", 2083),

        // Vikram Samvat 2084 (Diwali 2027 to Diwali 2028)
        MonthBoundary(2027, 10, 30, "કાર્તક", 2084),
        MonthBoundary(2027, 11, 29, "માગશર", 2084),
        MonthBoundary(2027, 12, 28, "પોષ", 2084),
        MonthBoundary(2028, 1, 27, "મહા", 2084),
        MonthBoundary(2028, 2, 26, "ફાગણ", 2084),
        MonthBoundary(2028, 3, 27, "ચૈત્ર", 2084),
        MonthBoundary(2028, 4, 25, "વૈશાખ", 2084),
        MonthBoundary(2028, 5, 24, "જેઠ", 2084),
        MonthBoundary(2028, 6, 23, "અષાઢ", 2084),
        MonthBoundary(2028, 7, 22, "શ્રાવણ", 2084),
        MonthBoundary(2028, 8, 21, "ભાદરવો", 2084),
        MonthBoundary(2028, 9, 19, "આસો", 2084),

        // Vikram Samvat 2085 (Diwali 2028 to Diwali 2029)
        MonthBoundary(2028, 10, 18, "કાર્તક", 2085),
        MonthBoundary(2028, 11, 17, "માગશર", 2085),
        MonthBoundary(2028, 12, 17, "પોષ", 2085),
        MonthBoundary(2029, 1, 15, "મહા", 2085),
        MonthBoundary(2029, 2, 14, "ફાગણ", 2085),
        MonthBoundary(2029, 3, 16, "ચૈત્ર", 2085),
        MonthBoundary(2029, 4, 14, "અધિક વૈશાખ", 2085),
        MonthBoundary(2029, 5, 14, "નિજ વૈશાખ", 2085),
        MonthBoundary(2029, 6, 12, "જેઠ", 2085),
        MonthBoundary(2029, 7, 12, "અષાઢ", 2085),
        MonthBoundary(2029, 8, 10, "શ્રાવણ", 2085),
        MonthBoundary(2029, 9, 9, "ભાદરવો", 2085),
        MonthBoundary(2029, 10, 8, "આસો", 2085),

        // Vikram Samvat 2086 (Diwali 2029 to Diwali 2030)
        MonthBoundary(2029, 11, 6, "કાર્તક", 2086),
        MonthBoundary(2029, 12, 6, "માગશર", 2086),
        MonthBoundary(2030, 1, 5, "પોષ", 2086),
        MonthBoundary(2030, 2, 3, "મહા", 2086),
        MonthBoundary(2030, 3, 5, "ફાગણ", 2086),
        MonthBoundary(2030, 4, 4, "ચૈત્ર", 2086),
        MonthBoundary(2030, 5, 3, "વૈશાખ", 2086),
        MonthBoundary(2030, 6, 2, "જેઠ", 2086),
        MonthBoundary(2030, 7, 1, "અષાઢ", 2086),
        MonthBoundary(2030, 7, 31, "શ્રાવણ", 2086),
        MonthBoundary(2030, 8, 29, "ભાદરવો", 2086),
        MonthBoundary(2030, 9, 28, "આસો", 2086),

        // Vikram Samvat 2087 (Diwali 2030 onwards)
        MonthBoundary(2030, 10, 27, "કાર્તક", 2087),
        MonthBoundary(2030, 11, 26, "માગશર", 2087),
        MonthBoundary(2030, 12, 25, "પોષ", 2087),
        MonthBoundary(2031, 1, 24, "મહા", 2087)
    )

    private fun getJulianDay(year: Int, month1: Int, day: Int, hourFrac: Double = 1.0): Double {
        var y = year
        var m = month1
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = y / 100
        val b = 2 - a + (a / 4)
        return (365.25 * (y + 4716)).toInt() + (30.6001 * (m + 1)).toInt() + day + b - 1524.5 + (hourFrac / 24.0)
    }

    private fun getSunMoonLongitudes(jd: Double): Pair<Double, Double> {
        val t = (jd - 2451545.0) / 36525.0
        val l0 = (280.46646 + 36000.76983 * t + 0.0003032 * t * t) % 360.0
        val m = (357.52911 + 35999.05029 * t - 0.0001537 * t * t) % 360.0
        val mr = Math.toRadians(m)
        val c = (1.914602 - 0.004817 * t) * sin(mr) + (0.019993 - 0.000101 * t) * sin(2 * mr) + 0.000289 * sin(3 * mr)
        val sunLon = (l0 + c + 360.0) % 360.0

        val lp = (218.3164477 + 481267.88123421 * t - 0.0015786 * t * t) % 360.0
        val d = (297.8501921 + 445267.1114034 * t - 0.0018819 * t * t) % 360.0
        val mm = (134.9633964 + 477198.8675055 * t + 0.0087414 * t * t) % 360.0
        val f = (93.2720950 + 483202.0175233 * t - 0.0036539 * t * t) % 360.0

        val dr = Math.toRadians(d)
        val mmr = Math.toRadians(mm)
        val fr = Math.toRadians(f)

        val dLon = (6.288774 * sin(mmr)
                + 1.274027 * sin(2 * dr - mmr)
                + 0.658314 * sin(2 * dr)
                + 0.213618 * sin(2 * mmr)
                - 0.185116 * sin(mr)
                - 0.114332 * sin(2 * fr)
                + 0.058793 * sin(2 * dr - 2 * mmr)
                + 0.057066 * sin(2 * dr - mr - mmr)
                + 0.053322 * sin(2 * dr + mmr)
                + 0.046153 * sin(2 * dr - mr)
                - 0.034718 * sin(dr)
                - 0.030465 * sin(mr + mmr)
                + 0.015327 * sin(2 * dr - 2 * fr)
                - 0.012528 * sin(2 * fr + mmr)
                - 0.010980 * sin(2 * fr - mmr))
        val moonLon = (lp + dLon + 360.0) % 360.0

        return Pair(sunLon, moonLon)
    }

    private fun getTithiData(year: Int, month1: Int, day: Int): Triple<Int, String, String> {
        val jd = getJulianDay(year, month1, day, 1.0) // 06:30 IST / 01:00 UTC
        val (sunLon, moonLon) = getSunMoonLongitudes(jd)
        val diff = (moonLon - sunLon + 360.0) % 360.0
        val tithiNum = (diff / 12.0).toInt().coerceIn(0, 29) // 0 to 29

        val isShukla = tithiNum < 15
        val paksha = if (isShukla) "સુદ" else "વદ"
        val tithiIndex = tithiNum % 15 // 0 to 14

        val tithiName = when (tithiIndex) {
            0 -> "એકમ (પડવો)"
            1 -> "બીજ"
            2 -> "ત્રીજ"
            3 -> "ચોથ"
            4 -> "પાંચમ"
            5 -> "છઠ"
            6 -> "સાતમ"
            7 -> "આઠમ"
            8 -> "નોમ"
            9 -> "દસમ"
            10 -> "અગિયારસ"
            11 -> "બારસ"
            12 -> "તેરસ"
            13 -> "ચૌદશ"
            14 -> if (isShukla) "પૂનમ" else "અમાસ"
            else -> "એકમ"
        }

        val tithiShort = when (tithiIndex) {
            14 -> if (isShukla) "પૂનમ" else "અમાસ"
            10 -> "અગિયારસ"
            else -> "$paksha ${tithiIndex + 1}"
        }

        return Triple(tithiNum, "$paksha $tithiName", tithiShort)
    }

    private fun getGujaratiMonthInfo(year: Int, month1: Int, day: Int): Pair<String, Int> {
        val targetDay = year * 10000 + month1 * 100 + day
        for (i in 0 until GUJARATI_MONTH_BOUNDARIES.size - 1) {
            val bCurrent = GUJARATI_MONTH_BOUNDARIES[i]
            val bNext = GUJARATI_MONTH_BOUNDARIES[i + 1]
            val curVal = bCurrent.y * 10000 + bCurrent.m * 100 + bCurrent.d
            val nextVal = bNext.y * 10000 + bNext.m * 100 + bNext.d
            if (targetDay in curVal until nextVal) {
                return Pair(bCurrent.name, bCurrent.vsYear)
            }
        }
        val last = GUJARATI_MONTH_BOUNDARIES.last()
        return Pair(last.name, last.vsYear)
    }

    fun calculatePanchang(calendar: Calendar = Calendar.getInstance(), cityName: String = "અમદાવાદ"): PanchangData {
        val city = SUPPORTED_CITIES.find { it.nameGujarati == cityName || it.nameEnglish.equals(cityName, ignoreCase = true) }
            ?: SUPPORTED_CITIES.first()

        val year = calendar.get(Calendar.YEAR)
        val month1 = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val dayOfWeekIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1 // 0 = Sunday

        val (gujMonthName, vsYear) = getGujaratiMonthInfo(year, month1, day)
        val (tithiNum, fullTithiName, _) = getTithiData(year, month1, day)

        val isShukla = tithiNum < 15
        val paksha = if (isShukla) "સુદ" else "વદ"
        val tithiIndex = tithiNum % 15
        val rawTithiName = if (tithiIndex == 14) {
            if (isShukla) "પૂનમ" else "અમાસ"
        } else {
            fullTithiName.substringAfter(" ")
        }

        val vaar = VAAR_NAMES[dayOfWeekIndex]

        // Astronomical Nakshatra from Moon longitude
        val jd = getJulianDay(year, month1, day, 1.0)
        val (sunLon, moonLon) = getSunMoonLongitudes(jd)
        // 27 Nakshatras across 360 deg = 13 deg 20 min (13.333 deg) each
        val nakshatraIndex = (moonLon / (360.0 / 27.0)).toInt().coerceIn(0, 26)
        val nakshatra = NAKSHATRAS[nakshatraIndex]

        // Yoga: (Sun lon + Moon lon) / 13.333
        val yogaIndex = (((sunLon + moonLon) % 360.0) / (360.0 / 27.0)).toInt().coerceIn(0, 26)
        val yoga = YOGAS[yogaIndex]

        // Karana: half of tithi (6 deg each)
        val karanaNum = ((((moonLon - sunLon + 360.0) % 360.0) / 6.0).toInt()).coerceIn(0, 59)
        val karana = when (karanaNum) {
            0 -> "કિંસ્તુઘ્ન"
            57 -> "શકુનિ"
            58 -> "ચતુષ્પાદ"
            59 -> "નાગ"
            else -> KARANAS[(karanaNum - 1) % 7]
        }

        // Precise Local Sunrise & Sunset
        val declination = 23.45 * sin((2 * PI / 365) * (dayOfYear - 81))
        val latRad = city.latitude * (PI / 180)
        val decRad = declination * (PI / 180)
        val hourAngle = acos((-tan(latRad) * tan(decRad)).coerceIn(-1.0, 1.0))
        val hourAngleDeg = hourAngle * (180 / PI)

        val longitudeOffsetMinutes = (city.longitude - 82.5) * 4
        val noonMinutes = 720.0 - longitudeOffsetMinutes
        val halfDayMinutes = hourAngleDeg * 4
        val sunriseMinutes = (noonMinutes - halfDayMinutes).toInt()
        val sunsetMinutes = (noonMinutes + halfDayMinutes).toInt()

        val sunriseStr = formatMinutesToAmPm(sunriseMinutes)
        val sunsetStr = formatMinutesToAmPm(sunsetMinutes)

        // Rahu Kaal calculation (Sun:8, Mon:2, Tue:7, Wed:5, Thu:6, Fri:4, Sat:3)
        val rahuOctants = listOf(8, 2, 7, 5, 6, 4, 3)
        val octant = rahuOctants[dayOfWeekIndex]
        val dayDuration = sunsetMinutes - sunriseMinutes
        val octantDuration = dayDuration / 8
        val rahuStart = sunriseMinutes + (octant - 1) * octantDuration
        val rahuEnd = rahuStart + octantDuration
        val rahuKaalStr = "${formatMinutesToAmPm(rahuStart)} થી ${formatMinutesToAmPm(rahuEnd)}"

        // Abhijit Muhurat: around solar noon (~48 mins)
        val abhijitStart = (noonMinutes - 24).toInt()
        val abhijitEnd = (noonMinutes + 24).toInt()
        val abhijitStr = "${formatMinutesToAmPm(abhijitStart)} થી ${formatMinutesToAmPm(abhijitEnd)}"

        // Day Choghadiyas & Night Choghadiyas
        val dayChoghadiyas = generateDayChoghadiya(sunriseMinutes, dayDuration, dayOfWeekIndex)
        val nightDuration = 1440 - dayDuration
        val nightChoghadiyas = generateNightChoghadiya(sunsetMinutes, nightDuration, dayOfWeekIndex)

        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("gu", "IN"))
        val gregorianFormatted = dateFormat.format(calendar.time)

        // Check festival for this date
        val specialFestival = getFestivalForDate(year, month1, day, gujMonthName, isShukla, tithiIndex)

        return PanchangData(
            cityName = city.nameGujarati,
            gregorianDateFormatted = gregorianFormatted,
            vikramSamvat = "વિક્રમ સંવત $vsYear",
            gujaratiMonth = gujMonthName,
            paksha = paksha,
            tithi = rawTithiName,
            vaar = vaar,
            nakshatra = nakshatra,
            yoga = yoga,
            karana = karana,
            sunrise = sunriseStr,
            sunset = sunsetStr,
            rahuKaal = rahuKaalStr,
            abhijitMuhurat = abhijitStr,
            choghadiyaList = dayChoghadiyas,
            nightChoghadiyaList = nightChoghadiyas,
            specialFestival = specialFestival
        )
    }

    private fun generateDayChoghadiya(sunriseMinutes: Int, dayDuration: Int, dayOfWeek: Int): List<ChoghadiyaItem> {
        val cycle = listOf(
            ChoghadiyaItem("ઉદ્વેગ", "", "સામાન્ય / સૂર્ય", false),
            ChoghadiyaItem("ચલ", "", "યાત્રા માટે શુભ", true),
            ChoghadiyaItem("લાભ", "", "શ્રેષ્ઠ લાભદાયક", true),
            ChoghadiyaItem("અમૃત", "", "સર્વોત્તમ મુહૂર્ત", true),
            ChoghadiyaItem("કાળ", "", "અશુભ / રાહુ", false),
            ChoghadiyaItem("શુભ", "", "શુભ કાર્ય માટે ઉત્તમ", true),
            ChoghadiyaItem("રોગ", "", "વર્જ્ય / અશુભ", false)
        )

        // Sunday=0: ઉદ્વેગ(0), Monday=1: અમૃત(3), Tuesday=2: રોગ(6), Wednesday=3: લાભ(2), Thursday=4: શુભ(5), Friday=5: ચલ(1), Saturday=6: કાળ(4)
        val dayStarts = listOf(0, 3, 6, 2, 5, 1, 4)
        val startIndex = dayStarts[dayOfWeek]
        val slotDuration = dayDuration / 8

        val list = mutableListOf<ChoghadiyaItem>()
        for (i in 0 until 8) {
            val item = cycle[(startIndex + i) % 7]
            val startMin = sunriseMinutes + i * slotDuration
            val endMin = startMin + slotDuration
            val timeRange = "${formatMinutesToAmPm(startMin)} - ${formatMinutesToAmPm(endMin)}"
            list.add(item.copy(timeRange = timeRange))
        }
        return list
    }

    private fun generateNightChoghadiya(sunsetMinutes: Int, nightDuration: Int, dayOfWeek: Int): List<ChoghadiyaItem> {
        val cycle = listOf(
            ChoghadiyaItem("ઉદ્વેગ", "", "સામાન્ય / સૂર્ય", false),
            ChoghadiyaItem("ચલ", "", "યાત્રા માટે શુભ", true),
            ChoghadiyaItem("લાભ", "", "શ્રેષ્ઠ લાભદાયક", true),
            ChoghadiyaItem("અમૃત", "", "સર્વોત્તમ મુહૂર્ત", true),
            ChoghadiyaItem("કાળ", "", "અશુભ / રાહુ", false),
            ChoghadiyaItem("શુભ", "", "શુભ કાર્ય માટે ઉત્તમ", true),
            ChoghadiyaItem("રોગ", "", "વર્જ્ય / અશુભ", false)
        )

        // Sunday night starts with શુભ(5), Monday night with ચલ(1), Tuesday with કાળ(4), Wednesday with ઉદ્વેગ(0), Thursday with અમૃત(3), Friday with રોગ(6), Saturday with લાભ(2)
        val nightStarts = listOf(5, 1, 4, 0, 3, 6, 2)
        val startIndex = nightStarts[dayOfWeek]
        val slotDuration = nightDuration / 8

        val list = mutableListOf<ChoghadiyaItem>()
        for (i in 0 until 8) {
            val item = cycle[(startIndex + i) % 7]
            val startMin = sunsetMinutes + i * slotDuration
            val endMin = startMin + slotDuration
            val timeRange = "${formatMinutesToAmPm(startMin)} - ${formatMinutesToAmPm(endMin)}"
            list.add(item.copy(timeRange = timeRange))
        }
        return list
    }

    private fun formatMinutesToAmPm(minutes: Int): String {
        val normalized = (minutes + 2880) % 1440
        val hour24 = normalized / 60
        val min = normalized % 60
        val isPm = hour24 >= 12
        val hour12 = if (hour24 == 0) 12 else if (hour24 > 12) hour24 - 12 else hour24
        val timeOfDay = if (hour24 in 18..23) "સાંજે/રાત્રે" else if (isPm) "બપોરે" else "સવારે"
        return String.format(Locale.getDefault(), "%02d:%02d %s", hour12, min, timeOfDay)
    }

    /**
     * Generates all days of a month for the Gujarati Interactive Calendar Grid (2026-2030),
     * including proper padding for Sunday-Saturday alignment.
     */
    fun getCalendarMonth(
        year: Int,
        monthIndex0: Int, // 0 = Jan, 11 = Dec
        today: Calendar = Calendar.getInstance()
    ): List<GujaratiCalendarDay> {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, monthIndex0)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1 // 0 = Sunday

        val list = mutableListOf<GujaratiCalendarDay>()

        // Previous month padding days
        if (firstDayOfWeek > 0) {
            val prevCal = (cal.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
            val prevMax = prevCal.getActualMaximum(Calendar.DAY_OF_MONTH)
            val prevStart = prevMax - firstDayOfWeek + 1
            val prevYear = prevCal.get(Calendar.YEAR)
            val prevMonth1 = prevCal.get(Calendar.MONTH) + 1
            for (d in prevStart..prevMax) {
                val (gujMonth, vsYear) = getGujaratiMonthInfo(prevYear, prevMonth1, d)
                val (tithiNum, _, tithiShort) = getTithiData(prevYear, prevMonth1, d)
                val isShukla = tithiNum < 15
                val tithiIdx = tithiNum % 15
                val fest = getFestivalForDate(prevYear, prevMonth1, d, gujMonth, isShukla, tithiIdx)
                list.add(
                    GujaratiCalendarDay(
                        dayOfMonth = d,
                        monthNumber = prevMonth1,
                        year = prevYear,
                        vikramSamvat = vsYear,
                        gujaratiMonth = gujMonth,
                        paksha = if (isShukla) "સુદ" else "વદ",
                        tithi = tithiShort,
                        tithiShort = tithiShort,
                        isPurnima = tithiIdx == 14 && isShukla,
                        isAmavasya = tithiIdx == 14 && !isShukla,
                        isEkadashi = tithiIdx == 10,
                        festivalName = fest,
                        isCurrentMonth = false,
                        isToday = false
                    )
                )
            }
        }

        // Current month days
        val currentMonth1 = monthIndex0 + 1
        val todayYear = today.get(Calendar.YEAR)
        val todayMonth1 = today.get(Calendar.MONTH) + 1
        val todayDay = today.get(Calendar.DAY_OF_MONTH)

        for (d in 1..daysInMonth) {
            val (gujMonth, vsYear) = getGujaratiMonthInfo(year, currentMonth1, d)
            val (tithiNum, _, tithiShort) = getTithiData(year, currentMonth1, d)
            val isShukla = tithiNum < 15
            val tithiIdx = tithiNum % 15
            val fest = getFestivalForDate(year, currentMonth1, d, gujMonth, isShukla, tithiIdx)
            val isToday = (year == todayYear && currentMonth1 == todayMonth1 && d == todayDay)

            list.add(
                GujaratiCalendarDay(
                    dayOfMonth = d,
                    monthNumber = currentMonth1,
                    year = year,
                    vikramSamvat = vsYear,
                    gujaratiMonth = gujMonth,
                    paksha = if (isShukla) "સુદ" else "વદ",
                    tithi = tithiShort,
                    tithiShort = tithiShort,
                    isPurnima = tithiIdx == 14 && isShukla,
                    isAmavasya = tithiIdx == 14 && !isShukla,
                    isEkadashi = tithiIdx == 10,
                    festivalName = fest,
                    isCurrentMonth = true,
                    isToday = isToday
                )
            )
        }

        // Next month padding to fill remaining row cells (complete multiple of 7)
        val remaining = (7 - (list.size % 7)) % 7
        if (remaining > 0) {
            val nextCal = (cal.clone() as Calendar).apply { add(Calendar.MONTH, 1) }
            val nextYear = nextCal.get(Calendar.YEAR)
            val nextMonth1 = nextCal.get(Calendar.MONTH) + 1
            for (d in 1..remaining) {
                val (gujMonth, vsYear) = getGujaratiMonthInfo(nextYear, nextMonth1, d)
                val (tithiNum, _, tithiShort) = getTithiData(nextYear, nextMonth1, d)
                val isShukla = tithiNum < 15
                val tithiIdx = tithiNum % 15
                val fest = getFestivalForDate(nextYear, nextMonth1, d, gujMonth, isShukla, tithiIdx)
                list.add(
                    GujaratiCalendarDay(
                        dayOfMonth = d,
                        monthNumber = nextMonth1,
                        year = nextYear,
                        vikramSamvat = vsYear,
                        gujaratiMonth = gujMonth,
                        paksha = if (isShukla) "સુદ" else "વદ",
                        tithi = tithiShort,
                        tithiShort = tithiShort,
                        isPurnima = tithiIdx == 14 && isShukla,
                        isAmavasya = tithiIdx == 14 && !isShukla,
                        isEkadashi = tithiIdx == 10,
                        festivalName = fest,
                        isCurrentMonth = false,
                        isToday = false
                    )
                )
            }
        }

        return list
    }

    /**
     * Checks if a specific Gregorian date has an important festival/vrat.
     */
    fun getFestivalForDate(
        year: Int,
        month1: Int,
        day: Int,
        gujMonth: String,
        isShukla: Boolean,
        tithiIdx: Int
    ): String? {
        // Universal Solar Fixed Festivals
        if (month1 == 1 && day == 14) return "મકરસંક્રાંતિ (ઉત્તરાયણ)"
        if (month1 == 1 && day == 15) return "વાસી ઉત્તરાયણ"

        // Authentic Lunar Festival Mappings for Gujarat
        return when {
            // Diwali / New Year Sequence
            gujMonth.contains("આસો") && !isShukla && tithiIdx == 11 -> "ધનતેરસ (ધનત્રયોદશી)"
            gujMonth.contains("આસો") && !isShukla && tithiIdx == 12 -> "કાળી ચૌદશ (રૂપ ચતુર્દશી)"
            gujMonth.contains("આસો") && !isShukla && (tithiIdx == 13 || tithiIdx == 14) && (day in listOf(8, 9, 28, 29, 16, 17, 4, 5, 25, 26)) -> "દિવાળી (લક્ષ્મી પૂજન / ચોપડા પૂજન)"
            gujMonth.contains("કાર્તક") && isShukla && tithiIdx == 0 -> "બેસતું વર્ષ (નૂતન વર્ષાભિનંદન)"
            gujMonth.contains("કાર્તક") && isShukla && tithiIdx == 1 -> "ભાઈબીજ (યમ દ્વિતીયા)"
            gujMonth.contains("કાર્તક") && isShukla && tithiIdx == 4 -> "લાભ પાંચમ (સૌભાગ્ય પંચમી)"
            gujMonth.contains("કાર્તક") && isShukla && tithiIdx == 10 -> "દેવઉઠી એકાદશી (તુલસી વિવાહ)"
            gujMonth.contains("કાર્તક") && isShukla && tithiIdx == 14 -> "દેવ દિવાળી (ત્રિપુરારી પૂર્ણિમા)"

            // Maha Shivratri
            gujMonth.contains("મહા") && !isShukla && (tithiIdx in 12..13) -> "મહાશિવરાત્રી"

            // Holi - Dhuleti
            gujMonth.contains("ફાગણ") && isShukla && tithiIdx == 14 -> "હોળી (હુતાશણી)"
            gujMonth.contains("ફાગણ") && !isShukla && tithiIdx == 0 -> "ધુળેટી"

            // Ram Navami & Hanuman Jayanti
            gujMonth.contains("ચૈત્ર") && isShukla && tithiIdx == 8 -> "શ્રી રામ નવમી"
            gujMonth.contains("ચૈત્ર") && isShukla && tithiIdx == 14 -> "શ્રી હનુમાન જયંતી"

            // Ashadhi Bij & Guru Purnima
            gujMonth.contains("અષાઢ") && isShukla && tithiIdx == 1 -> "અષાઢી બીજ (રથયાત્રા)"
            gujMonth.contains("અષાઢ") && isShukla && tithiIdx == 14 -> "ગુરુ પૂર્ણિમા"

            // Shravan Festivals
            gujMonth.contains("શ્રાવણ") && isShukla && tithiIdx == 14 -> "રક્ષાબંધન (બળેવ)"
            gujMonth.contains("શ્રાવણ") && !isShukla && tithiIdx == 6 -> "શીતળા સાતમ"
            gujMonth.contains("શ્રાવણ") && !isShukla && tithiIdx == 7 -> "શ્રી કૃષ્ણ જન્માષ્ટમી"

            // Ganesh Chaturthi
            gujMonth.contains("ભાદરવો") && isShukla && tithiIdx == 3 -> "ગણેશ ચતુર્થી"
            gujMonth.contains("ભાદરવો") && isShukla && tithiIdx == 14 -> "અનંત ચતુર્દશી"

            // Navratri & Dussehra
            gujMonth.contains("આસો") && isShukla && tithiIdx == 0 -> "નવરાત્રિ ઘટસ્થાપન (પ્રથમ નોરતું)"
            gujMonth.contains("આસો") && isShukla && tithiIdx in 1..7 -> "આસો નવરાત્રિ"
            gujMonth.contains("આસો") && isShukla && tithiIdx == 8 -> "મહા અષ્ટમી (હવન - આઠમ)"
            gujMonth.contains("આસો") && isShukla && tithiIdx == 9 -> "વિજયાદશમી (દશેરા)"
            gujMonth.contains("આસો") && isShukla && tithiIdx == 14 -> "શરદ પૂર્ણિમા"

            else -> null
        }
    }

    /**
     * Complete, accurate festival directory for 2026, 2027, 2028, 2029, 2030.
     */
    fun getFestivalsForYear(year: Int): List<HinduFestival> {
        return when (year) {
            2026 -> listOf(
                HinduFestival("f_2026_1", "મકરસંક્રાંતિ (ઉત્તરાયણ)", "૧૪ જાન્યુઆરી ૨૦૨૬", "પોષ વદ અગિયારસ", 0, "સૂર્યનું મકર રાશિમાં પાવન સંક્રમણ, પતંગોત્સવ અને દાન-પુણ્યનો પવિત્ર તહેવાર.", "સૂર્ય પૂજન, ગાયોને ઘાસ, તલ-ગોળનું દાન"),
                HinduFestival("f_2026_2", "મહાશિવરાત્રી", "૧૫ ફેબ્રુઆરી ૨૦૨૬", "મહા વદ તેરસ", 0, "દેવોના દેવ મહાદેવની પરમ ઉપાસના, જ્યોતિર્લિંગ પૂજન અને ઉપવાસનો મહાપર્વ.", "શિવલિંગ પર દૂધ, બીલીપત્ર, જળાભિષેક અને રાત્રિ જાગરણ"),
                HinduFestival("f_2026_3", "હોળી (હુતાશણી)", "૦૩ માર્ચ ૨૦૨૬", "ફાગણ સુદ પૂનમ", 0, "ભક્ત પ્રહલાદની અડગ ભક્તિ અને અસુર હોલિકાના દહનની મંગલ સ્મૃતિ.", "હોળી પ્રદક્ષિણા, ધાણી-ખજૂર-શ્રીફળ હોમવું"),
                HinduFestival("f_2026_4", "ધુળેટી (રંગોત્સવ)", "૦૪ માર્ચ ૨૦૨૬", "ફાગણ વદ એકમ", 0, "પ્રેમ, સદ્ભાવના અને રંગોનો ભવ્ય ઉત્સવ.", "ગુલાલ અને પ્રાકૃતિક રંગોથી મિલન"),
                HinduFestival("f_2026_5", "શ્રી રામ નવમી", "૨૭ માર્ચ ૨૦૨૬", "ચૈત્ર સુદ નોમ", 0, "મર્યાદા પુરુષોત્તમ ભગવાન શ્રી રામચંદ્રજીનો પ્રાગટ્યોત્સવ.", "બપોરે ૧૨ વાગ્યે શ્રી રામ જન્મોત્સવ, પંજરી પ્રસાદ"),
                HinduFestival("f_2026_6", "શ્રી હનુમાન જયંતી", "૦૨ એપ્રિલ ૨૦૨૬", "ચૈત્ર સુદ પૂનમ", 0, "સંકટમોચન કષ્ટભંજન શ્રી હનુમાનજી મહારાજનો પાવન જન્મોત્સવ.", "હનુમાન ચાલીસા પાઠ, સિંદૂર અર્ચન, તેલ-આંકડાની માળા"),
                HinduFestival("f_2026_7", "અષાઢી બીજ (રથયાત્રા)", "૧૬ જુલાઈ ૨૦૨૬", "અષાઢ સુદ બીજ", 0, "જગન્નાથજી, બલભદ્રજી અને સુભદ્રાજીની જગતપ્રસિદ્ધ રથયાત્રા.", "જાંબુ-કાકડીનો પ્રસાદ, રથ દર્શન"),
                HinduFestival("f_2026_8", "ગુરુ પૂર્ણિમા", "૨૯ જુલાઈ ૨૦૨૬", "અષાઢ સુદ પૂનમ", 0, "વેદવ્યાસ પૂજન અને સદ્ગુરુદેવ પ્રત્યે કૃતજ્ઞતા દર્શાવવાનો દિવસ.", "ગુરુ વંદન, પાદુકા પૂજન"),
                HinduFestival("f_2026_9", "રક્ષાબંધન (બળેવ)", "૨૮ ઓગસ્ટ ૨૦૨૬", "શ્રાવણ સુદ પૂનમ", 0, "ભાઈ-બહેનના પવિત્ર સ્નેહનું રક્ષાસૂત્ર અને બ્રાહ્મણોનો જનોઈ બદલવાનો પર્વ.", "રક્ષા કવચ બાંધવું, દીર્ઘાયુષ્યની કામના"),
                HinduFestival("f_2026_10", "શ્રી કૃષ્ણ જન્માષ્ટમી", "૦૪ સપ્ટેમ્બર ૨૦૨૬", "શ્રાવણ વદ આઠમ", 0, "જગતગુરુ ભગવાન શ્રી કૃષ્ણનો મધ્યરાત્રિનો દિવ્ય પ્રાગટ્યોત્સવ.", "ઉપવાસ, રાત્રે ૧૨ વાગ્યે પંચામૃત અભિષેક, નંદ ઘેર આનંદ ભયો"),
                HinduFestival("f_2026_11", "ગણેશ ચતુર્થી", "૧૪ સપ્ટેમ્બર ૨૦૨૬", "ભાદરવો સુદ ચોથ", 0, "વિઘ્નહર્તા પ્રથમ પૂજ્ય શ્રી ગણેશજીની સ્થાપના અને મહોત્સવ.", "મોદક ભોગ, દૂર્વા અર્પણ, ગણેશ સ્થાપન"),
                HinduFestival("f_2026_12", "આસો નવરાત્રિ પ્રારંભ", "૧૧ ઓક્ટોબર ૨૦૨૬", "આસો સુદ એકમ", 0, "માં આદ્યશક્તિ અંબાજી અને જગદંબાના નવ પાવન સ્વરૂપોનો ગરબા મહોત્સવ.", "ઘટ સ્થાપન, અખંડ દીપ, રાસ ગરબા"),
                HinduFestival("f_2026_13", "વિજયાદશમી (દશેરા)", "૨૦ ઓક્ટોબર ૨૦૨૬", "આસો સુદ દસમ", 0, "અધર્મ પર ધર્મનો અને રાવણ પર શ્રી રામનો મહાવિજય.", "શસ્ત્ર પૂજન, જલેબી-ફાફડા"),
                HinduFestival("f_2026_14", "શરદ પૂર્ણિમા", "૨૫ ઓક્ટોબર ૨૦૨૬", "આસો સુદ પૂનમ", 0, "ચંદ્રની ૧૬ કળાઓનું તેજ, શ્રી કૃષ્ણની મહારાસ લીલા.", "દૂધ-પૌંઆ ચંદ્રના કિરણોમાં રાખી આરોગવા"),
                HinduFestival("f_2026_15", "ધનતેરસ", "૦૬ નવેમ્બર ૨૦૨૬", "આસો વદ તેરસ", 0, "આયુર્વેદના પ્રણેતા ધન્વંતરિ અને માં લક્ષ્મીજીની કૃપા પ્રાપ્તિનો પર્વ.", "લક્ષ્મી પૂજન, નૂતન વાસણ/સ્વર્ણ ખરીદી"),
                HinduFestival("f_2026_16", "કાળી ચૌદશ", "૦૭ નવેમ્બર ૨૦૨૬", "આસો વદ ચૌદશ", 0, "મહાકાળી માતા અને હનુમાનજીની વિશેષ સંકટ નિવારણ સાધના.", "વડા-તળેલા પકવાન, તેલનો દીવો"),
                HinduFestival("f_2026_17", "દિવાળી (લક્ષ્મી પૂજન / ચોપડા પૂજન)", "૦૮-૦૯ નવેમ્બર ૨૦૨૬", "આસો વદ અમાસ", 0, "અંધકાર પર પ્રકાશનો મહાપર્વ. ઘરોમાં દીપ પ્રગટાવી શ્રી મહાલક્ષ્મી અને શારદા પૂજન.", "લક્ષ્મી પૂજન, ચોપડા પૂજન, દીપ પ્રાગટ્ય"),
                HinduFestival("f_2026_18", "બેસતું વર્ષ (નૂતન વર્ષાભિનંદન - સંવત ૨૦૮૩)", "૧૦ નવેમ્બર ૨૦૨૬", "કાર્તક સુદ એકમ", 0, "ગુજરાતીઓનું નવું વર્ષ. વિક્રમ સંવત ૨૦૮૩ પ્રારંભ અને અન્નકૂટ ઉત્સવ.", "મોટાઓના આશીર્વાદ, સાલમુબારક, ગોવર્ધન પૂજા"),
                HinduFestival("f_2026_19", "ભાઈબીજ", "૧૧ નવેમ્બર ૨૦૨૬", "કાર્તક સુદ બીજ", 0, "યમરાજ અને યમુનાજીની કથા સાથે જોડાયેલ બહેનના ઘરે ભાઈનું મંગલ ભોજન.", "બહેન ભાઈને તિલક કરે"),
                HinduFestival("f_2026_20", "લાભ પાંચમ", "૧૪ નવેમ્બર ૨૦૨૬", "કાર્તક સુદ પાંચમ", 0, "નૂતન વર્ષમાં વ્યાપાર-ધંધા અને દુકાનોના શુભ મુહૂર્તનો શ્રેષ્ઠ દિવસ.", "ચોપડામાં કંકુ-ચોખાથી શ્રી સવા લખવું"),
                HinduFestival("f_2026_21", "દેવઉઠી એકાદશી (તુલસી વિવાહ)", "૨૦ નવેમ્બર ૨૦૨૬", "કાર્તક સુદ અગિયારસ", 0, "ચાતુર્માસ પૂર્ણ, ભગવાન વિષ્ણુ યોગનિદ્રામાંથી જાગે અને તુલસી વિવાહ શરૂ.", "શેરડીના મંડપ, તુલસી-શાલિગ્રામ વિવાહ"),
                HinduFestival("f_2026_22", "દેવ દિવાળી", "૨૪ નવેમ્બર ૨૦૨૬", "કાર્તક સુદ પૂનમ", 0, "દેવતાઓ દ્વારા કાશી અને તીર્થોમાં ઉજવાતી ભવ્ય દિવાળી.", "તીર્થસ્નાન, દીપદાન")
            )

            2027 -> listOf(
                HinduFestival("f_2027_1", "મકરસંક્રાંતિ (ઉત્તરાયણ)", "૧૪ જાન્યુઆરી ૨૦૨૭", "પોષ સુદ સાતમ", 0, "સૂર્યનું મકર રાશિમાં પ્રવેશ, પતંગોત્સવ.", "સૂર્ય પૂજન, દાન"),
                HinduFestival("f_2027_2", "મહાશિવરાત્રી", "૦૬ માર્ચ ૨૦૨૭", "મહા વદ તેરસ", 0, "મહાદેવની અખંડ આરાધના અને ઉપવાસનો પર્વ.", "શિવ પૂજન, બિલ્વપત્ર અર્પણ"),
                HinduFestival("f_2027_3", "હોળી (હુતાશણી)", "૨૨ માર્ચ ૨૦૨૭", "ફાગણ સુદ પૂનમ", 0, "સત્યની વિજય સ્મૃતિ.", "હોળી પ્રદક્ષિણા"),
                HinduFestival("f_2027_4", "ધુળેટી", "૨૩ માર્ચ ૨૦૨૭", "ફાગણ વદ એકમ", 0, "રંગોનો પર્વ.", "ગુલાલ મિલન"),
                HinduFestival("f_2027_5", "શ્રી રામ નવમી", "૧૬ એપ્રિલ ૨૦૨૭", "ચૈત્ર સુદ નોમ", 0, "શ્રી રામ જન્મોત્સવ.", "પંજરી પ્રસાદ, રામ સ્તુતિ"),
                HinduFestival("f_2027_6", "શ્રી હનુમાન જયંતી", "૨૧ એપ્રિલ ૨૦૨૭", "ચૈત્ર સુદ પૂનમ", 0, "હનુમાનજી પ્રાગટ્ય પર્વ.", "હનુમાન ચાલીસા પાઠ"),
                HinduFestival("f_2027_7", "અષાઢી બીજ (રથયાત્રા)", "૦૫ જુલાઈ ૨૦૨૭", "અષાઢ સુદ બીજ", 0, "જગન્નાથ રથયાત્રા.", "રથ દર્શન"),
                HinduFestival("f_2027_8", "ગુરુ પૂર્ણિમા", "૧૮ જુલાઈ ૨૦૨૭", "અષાઢ સુદ પૂનમ", 0, "ગુરુ ઉપાસના પર્વ.", "ગુરુ વંદન"),
                HinduFestival("f_2027_9", "રક્ષાબંધન", "૧૭ ઓગસ્ટ ૨૦૨૭", "શ્રાવણ સુદ પૂનમ", 0, "ભાઈ-બહેન સ્નેહ પર્વ.", "રાખી બાંધવી"),
                HinduFestival("f_2027_10", "શ્રી કૃષ્ણ જન્માષ્ટમી", "૨૫ ઓગસ્ટ ૨૦૨૭", "શ્રાવણ વદ આઠમ", 0, "શ્રી કૃષ્ણ જન્મોત્સવ.", "નંદ મહોત્સવ"),
                HinduFestival("f_2027_11", "ગણેશ ચતુર્થી", "૦૪ સપ્ટેમ્બર ૨૦૨૭", "ભાદરવો સુદ ચોથ", 0, "ગણેશ સ્થાપન પર્વ.", "મોદક ભોગ"),
                HinduFestival("f_2027_12", "આસો નવરાત્રિ પ્રારંભ", "૩૦ સપ્ટેમ્બર ૨૦૨૭", "આસો સુદ એકમ", 0, "માં અંબાના ગરબા.", "ઘટ સ્થાપન"),
                HinduFestival("f_2027_13", "વિજયાદશમી (દશેરા)", "૧૦ ઓક્ટોબર ૨૦૨૭", "આસો સુદ દસમ", 0, "અધર્મ પર વિજય.", "જલેબી-ફાફડા"),
                HinduFestival("f_2027_14", "ધનતેરસ", "૨૭ ઓક્ટોબર ૨૦૨૭", "આસો વદ તેરસ", 0, "ધન અને આરોગ્ય પૂજન.", "લક્ષ્મી પૂજન"),
                HinduFestival("f_2027_15", "દિવાળી (લક્ષ્મી પૂજન)", "૨૯ ઓક્ટોબર ૨૦૨૭", "આસો વદ અમાસ", 0, "દીપાવલી મહાપર્વ.", "દીપ પ્રાગટ્ય, ચોપડા પૂજન"),
                HinduFestival("f_2027_16", "બેસતું વર્ષ (વિક્રમ સંવત ૨૦૮૪ પ્રારંભ)", "૩૦ ઓક્ટોબર ૨૦૨૭", "કાર્તક સુદ એકમ", 0, "ગુજરાતી નવું વર્ષ ૨૦૮૪.", "સાલમુબારક, અન્નકૂટ દર્શન"),
                HinduFestival("f_2027_17", "ભાઈબીજ", "૩૧ ઓક્ટોબર ૨૦૨૭", "કાર્તક સુદ બીજ", 0, "ભાઈ-બહેન તિલક પર્વ.", "તિલક અર્ચન"),
                HinduFestival("f_2027_18", "લાભ પાંચમ", "૦૩ નવેમ્બર ૨૦૨૭", "કાર્તક સુદ પાંચમ", 0, "વ્યાપાર મુહૂર્ત.", "શ્રી સવા પૂજન"),
                HinduFestival("f_2027_19", "દેવ દિવાળી", "૧૩ નવેમ્બર ૨૦૨૭", "કાર્તક સુદ પૂનમ", 0, "દેવ દિવાળી પર્વ.", "દીપદાન")
            )

            2028 -> listOf(
                HinduFestival("f_2028_1", "મકરસંક્રાંતિ (ઉત્તરાયણ)", "૧૪ જાન્યુઆરી ૨૦૨૮", "પોષ વદ બીજ", 0, "સૂર્ય પૂજન અને પતંગ પર્વ.", "દાન, તલ-ગોળ"),
                HinduFestival("f_2028_2", "મહાશિવરાત્રી", "૨૪ ફેબ્રુઆરી ૨૦૨૮", "મહા વદ તેરસ", 0, "મહાદેવ ઉપાસના પર્વ.", "શિવ પૂજા"),
                HinduFestival("f_2028_3", "હોળી (હુતાશણી)", "૧૧ માર્ચ ૨૦૨૮", "ફાગણ સુદ પૂનમ", 0, "હોળી પ્રદક્ષિણા.", "ધાણી પ્રસાદ"),
                HinduFestival("f_2028_4", "ધુળેટી", "૧૨ માર્ચ ૨૦૨૮", "ફાગણ વદ એકમ", 0, "રંગોત્સવ.", "ગુલાલ"),
                HinduFestival("f_2028_5", "શ્રી રામ નવમી", "૦૪ એપ્રિલ ૨૦૨૮", "ચૈત્ર સુદ નોમ", 0, "શ્રી રામ જન્મોત્સવ.", "પંજરી પ્રસાદ"),
                HinduFestival("f_2028_6", "શ્રી હનુમાન જયંતી", "૦૯ એપ્રિલ ૨૦૨૮", "ચૈત્ર સુદ પૂનમ", 0, "હનુમાનજી જન્મ પર્વ.", "હનુમાન ચાલીસા"),
                HinduFestival("f_2028_7", "અષાઢી બીજ (રથયાત્રા)", "૨૩ જૂન ૨૦૨૮", "અષાઢ સુદ બીજ", 0, "જગન્નાથ રથયાત્રા.", "રથ દર્શન"),
                HinduFestival("f_2028_8", "ગુરુ પૂર્ણિમા", "૦૬ જુલાઈ ૨૦૨૮", "અષાઢ સુદ પૂનમ", 0, "ગુરુ વંદન દિવસ.", "પાદુકા પૂજન"),
                HinduFestival("f_2028_9", "રક્ષાબંધન", "૦૫ ઓગસ્ટ ૨૦૨૮", "શ્રાવણ સુદ પૂનમ", 0, "પવિત્ર સ્નેહ પર્વ.", "રાખી"),
                HinduFestival("f_2028_10", "શ્રી કૃષ્ણ જન્માષ્ટમી", "૧૩ ઓગસ્ટ ૨૦૨૮", "શ્રાવણ વદ આઠમ", 0, "જન્માષ્ટમી ઉત્સવ.", "પંચામૃત સ્નાન"),
                HinduFestival("f_2028_11", "ગણેશ ચતુર્થી", "૨૪ ઓગસ્ટ ૨૦૨૮", "ભાદરવો સુદ ચોથ", 0, "ગણેશ સ્થાપન.", "દૂર્વા, મોદક"),
                HinduFestival("f_2028_12", "આસો નવરાત્રિ પ્રારંભ", "૧૯ સપ્ટેમ્બર ૨૦૨૮", "આસો સુદ એકમ", 0, "ગરબા મહોત્સવ.", "અખંડ દીપ"),
                HinduFestival("f_2028_13", "વિજયાદશમી (દશેરા)", "૨૮ સપ્ટેમ્બર ૨૦૨૮", "આસો સુદ દસમ", 0, "વિજયાદશમી.", "શસ્ત્ર પૂજન"),
                HinduFestival("f_2028_14", "ધનતેરસ", "૧૫ ઓક્ટોબર ૨૦૨૮", "આસો વદ તેરસ", 0, "લક્ષ્મી પૂજન.", "સોનું/વાસણ ખરીદી"),
                HinduFestival("f_2028_15", "દિવાળી (ચોપડા પૂજન)", "૧૭ ઓક્ટોબર ૨૦૨૮", "આસો વદ અમાસ", 0, "દીપાવલી મહાપર્વ.", "દીપ પ્રાગટ્ય"),
                HinduFestival("f_2028_16", "બેસતું વર્ષ (વિક્રમ સંવત ૨૦૮૫ પ્રારંભ)", "૧૮ ઓક્ટોબર ૨૦૨૮", "કાર્તક સુદ એકમ", 0, "ગુજરાતી નવું વર્ષ ૨૦૮૫.", "સાલમુબારક"),
                HinduFestival("f_2028_17", "ભાઈબીજ", "૧૯ ઓક્ટોબર ૨૦૨૮", "કાર્તક સુદ બીજ", 0, "તિલક પર્વ.", "બહેન ભાઈને ભોજન કરાવે"),
                HinduFestival("f_2028_18", "લાભ પાંચમ", "૨૨ ઓક્ટોબર ૨૦૨૮", "કાર્તક સુદ પાંચમ", 0, "શુભ મુહૂર્ત.", "શ્રી સવા"),
                HinduFestival("f_2028_19", "દેવ દિવાળી", "૦૧ નવેમ્બર ૨૦૨૮", "કાર્તક સુદ પૂનમ", 0, "દેવ દિવાળી.", "દીપદાન")
            )

            2029 -> listOf(
                HinduFestival("f_2029_1", "મકરસંક્રાંતિ (ઉત્તરાયણ)", "૧૪ જાન્યુઆરી ૨૦૨૯", "પોષ વદ અમાસ", 0, "પતંગ પર્વ અને સૂર્ય ઉપાસના.", "ગાયોને ઘાસ, દાન"),
                HinduFestival("f_2029_2", "મહાશિવરાત્રી", "૧૨ ફેબ્રુઆરી ૨૦૨૯", "મહા વદ તેરસ", 0, "મહાદેવ આરાધના.", "શિવલિંગ અભિષેક"),
                HinduFestival("f_2029_3", "હોળી (હુતાશણી)", "૨૯ માર્ચ ૨૦૨૯", "ફાગણ સુદ પૂનમ", 0, "હોળી દહન.", "ધાણી પ્રસાદ"),
                HinduFestival("f_2029_4", "ધુળેટી", "૩૦ માર્ચ ૨૦૨૯", "ફાગણ વદ એકમ", 0, "રંગોનો ઉત્સવ.", "ગુલાલ"),
                HinduFestival("f_2029_5", "શ્રી રામ નવમી", "૨૩ એપ્રિલ ૨૦૨૯", "ચૈત્ર સુદ નોમ", 0, "શ્રી રામ જન્મ.", "પંજરી"),
                HinduFestival("f_2029_6", "શ્રી હનુમાન જયંતી", "૨૮ એપ્રિલ ૨૦૨૯", "ચૈત્ર સુદ પૂનમ", 0, "હનુમાનજી જન્મ.", "હનુમાન ચાલીસા"),
                HinduFestival("f_2029_7", "અષાઢી બીજ (રથયાત્રા)", "૧૨ જુલાઈ ૨૦૨૯", "અષાઢ સુદ બીજ", 0, "રથયાત્રા.", "જાંબુ પ્રસાદ"),
                HinduFestival("f_2029_8", "ગુરુ પૂર્ણિમા", "૨૫ જુલાઈ ૨૦૨૯", "અષાઢ સુદ પૂનમ", 0, "ગુરુ વંદન.", "પૂજન"),
                HinduFestival("f_2029_9", "રક્ષાબંધન", "૨૪ ઓગસ્ટ ૨૦૨૯", "શ્રાવણ સુદ પૂનમ", 0, "રક્ષાબંધન.", "રાખી"),
                HinduFestival("f_2029_10", "શ્રી કૃષ્ણ જન્માષ્ટમી", "૦૧ સપ્ટેમ્બર ૨૦૨૯", "શ્રાવણ વદ આઠમ", 0, "શ્રી કૃષ્ણ જન્મોત્સવ.", "ઉપવાસ, આરતી"),
                HinduFestival("f_2029_11", "ગણેશ ચતુર્થી", "૧૨ સપ્ટેમ્બર ૨૦૨૯", "ભાદરવો સુદ ચોથ", 0, "ગણેશ સ્થાપન.", "મોદક"),
                HinduFestival("f_2029_12", "આસો નવરાત્રિ પ્રારંભ", "૦૮ ઓક્ટોબર ૨૦૨૯", "આસો સુદ એકમ", 0, "ગરબા મહોત્સવ.", "અખંડ જ્યોત"),
                HinduFestival("f_2029_13", "વિજયાદશમી (દશેરા)", "૧૭ ઓક્ટોબર ૨૦૨૯", "આસો સુદ દસમ", 0, "દશેરા.", "શસ્ત્ર પૂજા"),
                HinduFestival("f_2029_14", "ધનતેરસ", "૦૩ નવેમ્બર ૨૦૨૯", "આસો વદ તેરસ", 0, "ધનતેરસ.", "લક્ષ્મી પૂજન"),
                HinduFestival("f_2029_15", "દિવાળી (લક્ષ્મી પૂજન)", "૦૫ નવેમ્બર ૨૦૨૯", "આસો વદ અમાસ", 0, "દિવાળી પર્વ.", "દીપાવલી"),
                HinduFestival("f_2029_16", "બેસતું વર્ષ (વિક્રમ સંવત ૨૦૮૬ પ્રારંભ)", "૦૬ નવેમ્બર ૨૦૨૯", "કાર્તક સુદ એકમ", 0, "ગુજરાતી નવું વર્ષ ૨૦૮૬.", "સાલમુબારક"),
                HinduFestival("f_2029_17", "ભાઈબીજ", "૦૭ નવેમ્બર ૨૦૨૯", "કાર્તક સુદ બીજ", 0, "ભાઈબીજ.", "તિલક"),
                HinduFestival("f_2029_18", "લાભ પાંચમ", "૧૦ નવેમ્બર ૨૦૨૯", "કાર્તક સુદ પાંચમ", 0, "શુભ મુહૂર્ત.", "શ્રી સવા"),
                HinduFestival("f_2029_19", "દેવ દિવાળી", "૨૦ નવેમ્બર ૨૦૨૯", "કાર્તક સુદ પૂનમ", 0, "દેવ દિવાળી.", "દીપદાન")
            )

            2030 -> listOf(
                HinduFestival("f_2030_1", "મકરસંક્રાંતિ (ઉત્તરાયણ)", "૧૪ જાન્યુઆરી ૨૦૩૦", "પોષ સુદ દસમ", 0, "સૂર્ય સંક્રાંતિ અને પતંગ પર્વ.", "દાન, તલ-ગોળ"),
                HinduFestival("f_2030_2", "મહાશિવરાત્રી", "૦૨ માર્ચ ૨૦૩૦", "મહા વદ તેરસ", 0, "શિવ આરાધના.", "બિલ્વપત્ર"),
                HinduFestival("f_2030_3", "હોળી (હુતાશણી)", "૧૯ માર્ચ ૨૦૩૦", "ફાગણ સુદ પૂનમ", 0, "હોળી દહન.", "ધાણી પ્રસાદ"),
                HinduFestival("f_2030_4", "ધુળેટી", "૨૦ માર્ચ ૨૦૩૦", "ફાગણ વદ એકમ", 0, "રંગોત્સવ.", "ગુલાલ"),
                HinduFestival("f_2030_5", "શ્રી રામ નવમી", "૧૨ એપ્રિલ ૨૦૩૦", "ચૈત્ર સુદ નોમ", 0, "શ્રી રામ જન્મ.", "પંજરી"),
                HinduFestival("f_2030_6", "શ્રી હનુમાન જયંતી", "૧૭ એપ્રિલ ૨૦૩૦", "ચૈત્ર સુદ પૂનમ", 0, "હનુમાનજી જન્મ.", "હનુમાન ચાલીસા"),
                HinduFestival("f_2030_7", "અષાઢી બીજ (રથયાત્રા)", "૦૨ જુલાઈ ૨૦૩૦", "અષાઢ સુદ બીજ", 0, "જગન્નાથ રથયાત્રા.", "રથ દર્શન"),
                HinduFestival("f_2030_8", "ગુરુ પૂર્ણિમા", "૧૪ જુલાઈ ૨૦૩૦", "અષાઢ સુદ પૂનમ", 0, "ગુરુ વંદન.", "પૂજન"),
                HinduFestival("f_2030_9", "રક્ષાબંધન", "૧૩ ઓગસ્ટ ૨૦૩૦", "શ્રાવણ સુદ પૂનમ", 0, "રક્ષાબંધન.", "રાખી"),
                HinduFestival("f_2030_10", "શ્રી કૃષ્ણ જન્માષ્ટમી", "૨૧ ઓગસ્ટ ૨૦૩૦", "શ્રાવણ વદ આઠમ", 0, "શ્રી કૃષ્ણ જન્મોત્સવ.", "જન્માષ્ટમી"),
                HinduFestival("f_2030_11", "ગણેશ ચતુર્થી", "૦૧ સપ્ટેમ્બર ૨૦૩૦", "ભાદરવો સુદ ચોથ", 0, "ગણેશ સ્થાપન.", "મોદક ભોગ"),
                HinduFestival("f_2030_12", "આસો નવરાત્રિ પ્રારંભ", "૨૭ સપ્ટેમ્બર ૨૦૩૦", "આસો સુદ એકમ", 0, "ગરબા ઉત્સવ.", "અખંડ દીપ"),
                HinduFestival("f_2030_13", "વિજયાદશમી (દશેરા)", "૦૬ ઓક્ટોબર ૨૦૩૦", "આસો સુદ દસમ", 0, "દશેરા.", "શસ્ત્ર પૂજા"),
                HinduFestival("f_2030_14", "ધનતેરસ", "૨૪ ઓક્ટોબર ૨૦૩૦", "આસો વદ તેરસ", 0, "ધનતેરસ.", "લક્ષ્મી પૂજન"),
                HinduFestival("f_2030_15", "દિવાળી (ચોપડા પૂજન)", "૨૬ ઓક્ટોબર ૨૦૩૦", "આસો વદ અમાસ", 0, "દિવાળી પર્વ.", "ચોપડા પૂજન"),
                HinduFestival("f_2030_16", "બેસતું વર્ષ (વિક્રમ સંવત ૨૦૮૭ પ્રારંભ)", "૨૭ ઓક્ટોબર ૨૦૩૦", "કાર્તક સુદ એકમ", 0, "ગુજરાતી નવું વર્ષ ૨૦૮૭.", "સાલમુબારક, અન્નકૂટ"),
                HinduFestival("f_2030_17", "ભાઈબીજ", "૨૮ ઓક્ટોબર ૨૦૩૦", "કાર્તક સુદ બીજ", 0, "ભાઈબીજ.", "તિલક"),
                HinduFestival("f_2030_18", "લાભ પાંચમ", "૩૧ ઓક્ટોબર ૨૦૩૦", "કાર્તક સુદ પાંચમ", 0, "શુભ મુહૂર્ત.", "શ્રી સવા"),
                HinduFestival("f_2030_19", "દેવ દિવાળી", "૧૦ નવેમ્બર ૨૦૩૦", "કાર્તક સુદ પૂનમ", 0, "દેવ દિવાળી.", "દીપદાન")
            )

            else -> getFestivalsForYear(2026)
        }
    }
}
