package com.example

import com.example.data.repository.PanchangCalculator
import org.junit.Assert.*
import org.junit.Test
import java.util.Calendar

class ExampleUnitTest {

    @Test
    fun testPanchangCalculator_validCity() {
        val cal = Calendar.getInstance()
        val panchang = PanchangCalculator.calculatePanchang(cal, "અમદાવાદ")

        assertEquals("અમદાવાદ", panchang.cityName)
        assertTrue(panchang.vikramSamvat.contains("વિક્રમ સંવત"))
        assertTrue(panchang.gujaratiMonth.isNotEmpty())
        assertTrue(panchang.tithi.isNotEmpty())
        assertTrue(panchang.vaar.isNotEmpty())
        assertTrue(panchang.sunrise.isNotEmpty())
        assertTrue(panchang.sunset.isNotEmpty())
        assertTrue(panchang.rahuKaal.isNotEmpty())
        assertTrue(panchang.abhijitMuhurat.isNotEmpty())
        assertEquals(8, panchang.choghadiyaList.size)
    }

    @Test
    fun testPanchangCalculator_surat() {
        val cal = Calendar.getInstance()
        val panchang = PanchangCalculator.calculatePanchang(cal, "Surat")

        assertEquals("સુરત", panchang.cityName)
        assertTrue(panchang.nakshatra.isNotEmpty())
        assertTrue(panchang.yoga.isNotEmpty())
        assertTrue(panchang.karana.isNotEmpty())
    }

    @Test
    fun testChoghadiyaAuspiciousness() {
        val cal = Calendar.getInstance()
        val panchang = PanchangCalculator.calculatePanchang(cal, "રાજકોટ")
        val auspiciousSlots = panchang.choghadiyaList.filter { it.isAuspicious }
        assertTrue("Should contain auspicious choghadiya slots like Amrut, Shubh, Labh", auspiciousSlots.isNotEmpty())
    }

    @Test
    fun testAccurateGujaratiDiwaliAndBestuVaras2026() {
        val calSurat = PanchangCalculator.SUPPORTED_CITIES.first { it.nameGujarati.contains("સુરત") }

        // Nov 9, 2026: Diwali / Aso Vad Amas in Vikram Samvat 2082
        val nov9 = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2026)
            set(Calendar.MONTH, Calendar.NOVEMBER)
            set(Calendar.DAY_OF_MONTH, 9)
        }
        val panchangNov9 = PanchangCalculator.calculatePanchang(nov9, calSurat.nameGujarati)
        assertTrue("Nov 9 2026 should be VS 2082, got: ${panchangNov9.vikramSamvat}", panchangNov9.vikramSamvat.contains("૨૦૮૨") || panchangNov9.vikramSamvat.contains("2082"))
        assertTrue("Nov 9 2026 must be Aso month, got: ${panchangNov9.gujaratiMonth}", panchangNov9.gujaratiMonth.contains("આસો"))
        assertTrue("Nov 9 2026 must be Amas, got: ${panchangNov9.tithi}", panchangNov9.tithi.contains("અમાસ"))
        assertEquals(8, panchangNov9.choghadiyaList.size)
        assertEquals(8, panchangNov9.nightChoghadiyaList.size)

        // Nov 10, 2026: Bestu Varas / Kartak Sud Ekam in Vikram Samvat 2083
        val nov10 = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2026)
            set(Calendar.MONTH, Calendar.NOVEMBER)
            set(Calendar.DAY_OF_MONTH, 10)
        }
        val panchangNov10 = PanchangCalculator.calculatePanchang(nov10, calSurat.nameGujarati)
        assertTrue("Nov 10 2026 should be VS 2083, got: ${panchangNov10.vikramSamvat}", panchangNov10.vikramSamvat.contains("૨૦૮૩") || panchangNov10.vikramSamvat.contains("2083"))
        assertTrue("Nov 10 2026 must be Kartak month, got: ${panchangNov10.gujaratiMonth}", panchangNov10.gujaratiMonth.contains("કાર્તક") || panchangNov10.gujaratiMonth.contains("કારતક"))
        assertTrue("Nov 10 2026 must be Sud Ekam, got: ${panchangNov10.tithi}", panchangNov10.tithi.contains("એકમ") || panchangNov10.tithi.contains("૧"))
    }

    @Test
    fun testCalendarAndFestivals2026To2030() {
        val today = Calendar.getInstance()

        for (year in 2026..2030) {
            val festivals = PanchangCalculator.getFestivalsForYear(year)
            assertTrue("Year $year must have festivals populated", festivals.isNotEmpty())

            // Verify all 12 months can be generated without errors
            for (month0 in 0..11) {
                val days = PanchangCalculator.getCalendarMonth(year, month0, today)
                assertTrue("Month $month0 in $year must return grid days", days.isNotEmpty())
                val actualDays = days.filter { it.isCurrentMonth && it.dayOfMonth > 0 }
                assertTrue("Days in month should be 28 to 31, got ${actualDays.size}", actualDays.size in 28..31)
            }
        }
    }

    @Test
    fun testCompleteScripturesAndGitaDatasets() {
        val hanumanChalisa = com.example.data.repository.ScripturesData.hanumanChalisa
        // Must contain 2 opening dohas + 40 chaupais + 1 closing doha = 43 verses
        assertEquals(43, hanumanChalisa.verses.size)
        assertTrue(hanumanChalisa.verses.any { it.meaningGujarati.contains("ચોપાઈ ૪૦") })
        assertTrue(hanumanChalisa.verses.any { it.meaningGujarati.contains("સમાપન દોહા") })

        val shivChalisa = com.example.data.repository.ScripturesData.shivChalisa
        // Must contain opening doha + 40 chaupais + closing doha = 42 verses
        assertEquals(42, shivChalisa.verses.size)
        assertTrue(shivChalisa.verses.any { it.meaningGujarati.contains("ચોપાઈ ૪૦") })

        val atharvashirsha = com.example.data.repository.ScripturesData.ganeshAtharvashirsha
        assertTrue("Must contain full Atharvashirsha mantras (>= 12), got ${atharvashirsha.verses.size}", atharvashirsha.verses.size >= 12)

        // Gita Chapters: 18 chapters, and all 18 must have detailed summary, core teachings, and key shlokas
        assertEquals(18, com.example.data.repository.GitaData.chapters.size)
        val allShlokas = com.example.data.repository.GitaData.keyShlokas
        assertTrue("Total Gita shlokas should be >= 50, got ${allShlokas.size}", allShlokas.size >= 50)

        for (ch in com.example.data.repository.GitaData.chapters) {
            assertTrue("Chapter ${ch.number} must have summary", ch.summaryGujarati.isNotEmpty())
            assertTrue("Chapter ${ch.number} must have detailed summary", ch.detailedSummaryGujarati.isNotEmpty())
            assertTrue("Chapter ${ch.number} must have core teachings", ch.coreTeachings.isNotEmpty())

            val shlokas = allShlokas.filter { it.chapterNumber == ch.number }
            assertTrue("Chapter ${ch.number} must have key shlokas, found none", shlokas.isNotEmpty())
            for (shloka in shlokas) {
                assertTrue(shloka.sanskrit.isNotEmpty())
                assertTrue(shloka.gujaratiMeaning.isNotEmpty())
                assertTrue(shloka.practicalLessonGujarati.isNotEmpty())
            }
        }
    }

    @Test
    fun testDailyDayWiseDarshan() {
        val repo = com.example.data.repository.BhaktiRepository()

        // Verify Sunday (રવિવાર) -> Surya Narayan
        val sun = repo.getDarshanForDayOfWeek(Calendar.SUNDAY)
        assertEquals("darshan_surya", sun.id)
        assertTrue(sun.deityName.contains("સૂર્ય"))
        assertEquals("રવિવાર", sun.dayOfWeekGujarati)

        // Verify Monday (સોમવાર) -> Somnath Mahadev
        val mon = repo.getDarshanForDayOfWeek(Calendar.MONDAY)
        assertEquals("darshan_somnath", mon.id)
        assertTrue(mon.deityName.contains("સોમનાથ") || mon.deityName.contains("મહાદેવ"))
        assertEquals("સોમવાર", mon.dayOfWeekGujarati)

        // Verify Tuesday (મંગળવાર) -> Kashtbhanjan Dev Sarangpur
        val tue = repo.getDarshanForDayOfWeek(Calendar.TUESDAY)
        assertEquals("darshan_salangpur", tue.id)
        assertTrue(tue.deityName.contains("કષ્ટભંજન"))
        assertEquals("મંગળવાર", tue.dayOfWeekGujarati)

        // Verify Wednesday (બુધવાર) -> Siddhivinayak Ganesha
        val wed = repo.getDarshanForDayOfWeek(Calendar.WEDNESDAY)
        assertEquals("darshan_ganesh", wed.id)
        assertTrue(wed.deityName.contains("ગણેશ"))
        assertEquals("બુધવાર", wed.dayOfWeekGujarati)

        // Verify Thursday (ગુરુવાર) -> Shri Lakshminarayan Vishnu
        val thu = repo.getDarshanForDayOfWeek(Calendar.THURSDAY)
        assertEquals("darshan_vishnu", thu.id)
        assertTrue(thu.deityName.contains("નારાયણ") || thu.deityName.contains("વિષ્ણુ"))
        assertEquals("ગુરુવાર", thu.dayOfWeekGujarati)

        // Verify Friday (શુક્રવાર) -> Ambaji Mataji
        val fri = repo.getDarshanForDayOfWeek(Calendar.FRIDAY)
        assertEquals("darshan_ambaji", fri.id)
        assertTrue(fri.deityName.contains("અંબા"))
        assertEquals("શુક્રવાર", fri.dayOfWeekGujarati)

        // Verify Saturday (શનિવાર) -> Dwarikadhish
        val sat = repo.getDarshanForDayOfWeek(Calendar.SATURDAY)
        assertEquals("darshan_dwarka", sat.id)
        assertTrue(sat.deityName.contains("દ્વારકાધીશ"))
        assertEquals("શનિવાર", sat.dayOfWeekGujarati)
    }
}
