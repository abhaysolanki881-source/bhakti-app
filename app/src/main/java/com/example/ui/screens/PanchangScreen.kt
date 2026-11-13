package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChoghadiyaItem
import com.example.data.model.CityLocation
import com.example.data.model.GujaratiCalendarDay
import com.example.ui.strings.AppStrings
import com.example.ui.strings.LocalAppLanguage
import com.example.data.model.HinduFestival
import com.example.data.model.PanchangData
import com.example.data.repository.PanchangCalculator
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.TempleGold

private val GUJARATI_MONTH_NAMES_ENGLISH_MAPPING = listOf(
    "જાન્યુઆરી (પોષ - મહા)",
    "ફેબ્રુઆરી (મહા - ફાગણ)",
    "માર્ચ (ફાગણ - ચૈત્ર)",
    "એપ્રિલ (ચૈત્ર - વૈશાખ)",
    "મે (વૈશાખ - જેઠ)",
    "જૂન (જેઠ - અષાઢ)",
    "જુલાઈ (અષાઢ - શ્રાવણ)",
    "ઓગસ્ટ (શ્રાવણ - ભાદરવો)",
    "સપ્ટેમ્બર (ભાદરવો - આસો)",
    "ઓક્ટોબર (આસો - કારતક)",
    "નવેમ્બર (કારતક - માગશર)",
    "ડિસેમ્બર (માગશર - પોષ)"
)

private val WEEKDAY_HEADERS = listOf("રવિ", "સોમ", "મંગળ", "બુધ", "ગુરુ", "શુક્ર", "શનિ")
private val AVAILABLE_YEARS = listOf(2026, 2027, 2028, 2029, 2030)

@Composable
fun PanchangScreen(
    panchangData: PanchangData,
    festivals: List<HinduFestival>,
    onSelectCity: (String) -> Unit,
    selectedCity: String = "અમદાવાદ",
    currentLanguage: String = "gu",
    selectedYear: Int = 2026,
    selectedMonth0: Int = 10, // 0-indexed (10 = November)
    calendarDays: List<GujaratiCalendarDay> = emptyList(),
    onSelectDate: (year: Int, month1: Int, day: Int) -> Unit = { _, _, _ -> },
    onSelectYear: (Int) -> Unit = {},
    onSelectMonth0: (Int) -> Unit = {},
    onNextMonth: () -> Unit = {},
    onPrevMonth: () -> Unit = {},
    onResetToday: () -> Unit = {},
    onChangeDateByDays: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current
    var selectedTab by remember { mutableIntStateOf(0) }
    var showCityDialog by remember { mutableStateOf(false) }

    if (showCityDialog) {
        CitySelectionDialog(
            currentCity = panchangData.cityName,
            cities = PanchangCalculator.SUPPORTED_CITIES,
            onCitySelected = {
                onSelectCity(it.nameGujarati)
                showCityDialog = false
            },
            onDismiss = { showCityDialog = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("panchang_screen")
    ) {
        // Location & Date Navigation Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = SaffronPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = if (lang == "hi") "स्थान (शहर)" else if (lang == "en") "Location (City)" else "સ્થળ (શહેર)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = panchangData.cityName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SaffronPrimary
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = onResetToday,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(Icons.Default.Today, contentDescription = null, modifier = Modifier.size(14.dp), tint = SaffronPrimary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (lang == "hi") "आज" else if (lang == "en") "Today" else "આજે", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = { showCityDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(if (lang == "hi") "शहर बदलें" else if (lang == "en") "Change City" else "શહેર બદલો", style = MaterialTheme.typography.labelSmall)
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.size(14.dp))
                }
            }
        }

        // Quick Date Shift Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val prevDateDesc = if (lang == "en") "Previous Date" else if (lang == "hi") "पिछली तिथि" else "પાછલી તારીખ"
            val nextDateDesc = if (lang == "en") "Next Date" else if (lang == "hi") "अगली तिथि" else "આગલી તારીખ"
            IconButton(
                onClick = { onChangeDateByDays(-1) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = prevDateDesc, tint = SaffronPrimary, modifier = Modifier.size(18.dp))
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = SaffronDark, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${panchangData.gregorianDateFormatted} (${panchangData.vaar})",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            IconButton(
                onClick = { onChangeDateByDays(1) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = nextDateDesc, tint = SaffronPrimary, modifier = Modifier.size(18.dp))
            }
        }

        HorizontalDivider()

        // 4 Core Navigation Tabs
        val panchangTabs = remember(lang) {
            when (lang) {
                "hi" -> listOf("कैलेंडर", "आज का पंचांग", "चौघड़िया", "त्यौहार कैलेंडर")
                "en" -> listOf("Calendar", "Today's Panchang", "Choghadiya", "Festivals")
                else -> listOf("ગુજરાતી કેલેન્ડર", "આજનું પંચાંગ", "ચોઘડિયા", "તહેવાર કેલેન્ડર")
            }
        }

        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 12.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = SaffronPrimary
                )
            },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            panchangTabs.forEachIndexed { idx, title ->
                Tab(
                    selected = selectedTab == idx,
                    onClick = { selectedTab = idx },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == idx) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == idx) SaffronPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> InteractiveCalendarTab(
                    selectedYear = selectedYear,
                    selectedMonth0 = selectedMonth0,
                    calendarDays = calendarDays,
                    panchangData = panchangData,
                    onSelectYear = onSelectYear,
                    onNextMonth = onNextMonth,
                    onPrevMonth = onPrevMonth,
                    onSelectDay = { day ->
                        onSelectDate(day.year, day.monthNumber, day.dayOfMonth)
                    },
                    onViewFullPanchang = { selectedTab = 1 }
                )
                1 -> PanchangMainTab(panchangData)
                2 -> ChoghadiyaTab(
                    dayList = panchangData.choghadiyaList,
                    nightList = panchangData.nightChoghadiyaList,
                    cityName = panchangData.cityName
                )
                3 -> FestivalCalendarTab(
                    festivals = festivals,
                    selectedYear = selectedYear,
                    onSelectYear = onSelectYear
                )
            }
        }

        com.example.ui.components.BannerAdView(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
    }
}

/**
 * Tab 0: Interactive Gujarati Monthly Calendar Grid (2026-2030).
 * Displays full 7-column month grid with English dates, Gujarati Tithis, and festival markers.
 */
@Composable
fun InteractiveCalendarTab(
    selectedYear: Int,
    selectedMonth0: Int,
    calendarDays: List<GujaratiCalendarDay>,
    panchangData: PanchangData,
    onSelectYear: (Int) -> Unit,
    onNextMonth: () -> Unit,
    onPrevMonth: () -> Unit,
    onSelectDay: (GujaratiCalendarDay) -> Unit,
    onViewFullPanchang: () -> Unit
) {
    val isDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    val lang = LocalAppLanguage.current

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Year Filter Chips (2026 - 2030)
        item {
            Column {
                val selectYearLabel = when (lang) {
                    "hi" -> "वर्ष चुनें (2026 से 2030):"
                    "en" -> "Select Year (2026 to 2030):"
                    else -> "વર્ષ પસંદ કરો (૨૦૨૬ થી ૨૦૩૦):"
                }
                Text(
                    text = selectYearLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(AVAILABLE_YEARS) { yr ->
                        val isSelected = yr == selectedYear
                        FilterChip(
                            selected = isSelected,
                            onClick = { onSelectYear(yr) },
                            label = {
                                Text(
                                    text = "$yr",
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SaffronPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // Month Switcher Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val prevMonthDesc = if (lang == "en") "Previous Month" else if (lang == "hi") "पिछला महीना" else "પાછલો મહિનો"
                    val nextMonthDesc = if (lang == "en") "Next Month" else if (lang == "hi") "अगला महीना" else "આગલો મહિનો"
                    IconButton(onClick = onPrevMonth) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = prevMonthDesc, tint = SaffronPrimary)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val monthTitle = AppStrings.getMonthNameWithGujarati(selectedMonth0, lang)
                        Text(
                            text = monthTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SaffronPrimary
                        )
                        val calSubtitle = when (lang) {
                            "hi" -> "विक्रम संवत कैलेंडर • $selectedYear"
                            "en" -> "Vikram Samvat Calendar • $selectedYear"
                            else -> "વિક્રમ સંવત કેલેન્ડર • $selectedYear"
                        }
                        Text(
                            text = calSubtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(onClick = onNextMonth) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = nextMonthDesc, tint = SaffronPrimary)
                    }
                }
            }
        }

        // 7-Column Day Names Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isDark) Color(0xFF262626) else Color(0xFFF3F4F6))
                    .padding(vertical = 8.dp)
            ) {
                val weekdayHeaders = remember(lang) { AppStrings.getWeekdayHeaders(lang) }
                weekdayHeaders.forEachIndexed { idx, name ->
                    val color = if (idx == 0) Color(0xFFD32F2F) else MaterialTheme.colorScheme.onSurface
                    Text(
                        text = name,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }
            }
        }

        // Monthly Calendar Grid
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(4.dp)) {
                    val rows = calendarDays.chunked(7)
                    rows.forEach { week ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            week.forEach { day ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(2.dp)
                                ) {
                                    CalendarDayCell(
                                        day = day,
                                        isDark = isDark,
                                        onClick = { onSelectDay(day) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Calendar Legend (પૂનમ, અમાસ, એકાદશી, તહેવાર)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val purnimaLabel = when (lang) { "hi" -> "पूर्णिमा"; "en" -> "Purnima"; else -> "પૂનમ" }
                val amavasyaLabel = when (lang) { "hi" -> "अमावस्या"; "en" -> "Amavasya"; else -> "અમાસ" }
                val ekadashiLabel = when (lang) { "hi" -> "एकादशी"; "en" -> "Ekadashi"; else -> "એકાદશી" }
                val festivalLabel = when (lang) { "hi" -> "त्योहार"; "en" -> "Festival"; else -> "તહેવાર" }
                LegendIndicator(color = TempleGold, label = purnimaLabel)
                LegendIndicator(color = Color(0xFF424242), label = amavasyaLabel)
                LegendIndicator(color = Color(0xFFEA580C), label = ekadashiLabel)
                LegendIndicator(color = Color(0xFFD32F2F), label = festivalLabel)
            }
        }

        // Selected Day Panchang Snapshot Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF2A1E17) else Color(0xFFFFF9EE)
                ),
                border = BorderStroke(1.5.dp, SaffronPrimary.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            val snapTitle = when (lang) {
                                "hi" -> "चयनित तिथि का पंचांग:"
                                "en" -> "Selected Date Panchang:"
                                else -> "પસંદ કરેલ તારીખનું પંચાંગ:"
                            }
                            Text(
                                text = snapTitle,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${panchangData.gregorianDateFormatted} (${panchangData.vaar})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SaffronPrimary
                            )
                        }

                        OutlinedButton(
                            onClick = onViewFullPanchang,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            val detailsLabel = when (lang) {
                                "hi" -> "विस्तार से देखें"
                                "en" -> "View Details"
                                else -> "વિગતવાર જુઓ"
                            }
                            Text(detailsLabel, style = MaterialTheme.typography.labelSmall)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${panchangData.vikramSamvat} • ${panchangData.gujaratiMonth} ${panchangData.paksha} ${panchangData.tithi}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (panchangData.specialFestival != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFD32F2F))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = panchangData.specialFestival,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val sunriseLabel = when (lang) { "hi" -> "सूर्योदय"; "en" -> "Sunrise"; else -> "સૂર્યોદય" }
                        val sunsetLabel = when (lang) { "hi" -> "सूर्यास्त"; "en" -> "Sunset"; else -> "સૂર્યાસ્ત" }
                        val nakshatraLabel = when (lang) { "hi" -> "नक्षत्र"; "en" -> "Nakshatra"; else -> "નક્ષત્ર" }
                        Text(
                            text = "$sunriseLabel: ${panchangData.sunrise}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "$sunsetLabel: ${panchangData.sunset}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "$nakshatraLabel: ${panchangData.nakshatra}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarDayCell(
    day: GujaratiCalendarDay,
    isDark: Boolean,
    onClick: () -> Unit
) {
    val cellBg = when {
        day.isToday -> SaffronPrimary.copy(alpha = 0.18f)
        day.isPurnima -> TempleGold.copy(alpha = 0.22f)
        day.isAmavasya -> if (isDark) Color(0xFF3F3F46).copy(alpha = 0.5f) else Color(0xFFE4E4E7)
        !day.isCurrentMonth -> if (isDark) Color(0xFF1E1E1E) else Color(0xFFFAFAFA)
        else -> MaterialTheme.colorScheme.surface
    }

    val borderColor = when {
        day.isToday -> SaffronPrimary
        day.isPurnima -> TempleGold
        day.isAmavasya -> Color(0xFF71717A)
        day.festivalName != null -> Color(0xFFEF4444).copy(alpha = 0.6f)
        else -> Color.Transparent
    }

    val dateColor = when {
        !day.isCurrentMonth -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        day.isToday -> SaffronPrimary
        day.isPurnima -> Color(0xFFB45309)
        day.isAmavasya -> if (isDark) Color(0xFFE4E4E7) else Color(0xFF18181B)
        else -> MaterialTheme.colorScheme.onSurface
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .clip(RoundedCornerShape(6.dp))
            .background(cellBg)
            .border(if (borderColor != Color.Transparent) 1.5.dp else 0.5.dp, if (borderColor != Color.Transparent) borderColor else Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Date Number
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${day.dayOfMonth}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (day.isToday || day.festivalName != null) FontWeight.Bold else FontWeight.SemiBold,
                color = dateColor,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 2.dp)
            )

            // Festival / Special Dot Indicator
            if (day.festivalName != null) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD32F2F))
                )
            } else if (day.isEkadashi) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEA580C))
                )
            }
        }

        // Tithi Label
        Text(
            text = day.tithiShort,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 8.5.sp,
            fontWeight = if (day.isPurnima || day.isAmavasya || day.isEkadashi) FontWeight.Bold else FontWeight.Normal,
            color = if (!day.isCurrentMonth) {
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)
            } else if (day.isPurnima) {
                Color(0xFFB45309)
            } else if (day.isAmavasya) {
                if (isDark) Color(0xFFF4F4F5) else Color(0xFF18181B)
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LegendIndicator(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(label, style = MaterialTheme.typography.bodySmall, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/**
 * Tab 1: Detailed Panchang View for Selected Date.
 */
@Composable
fun PanchangMainTab(data: PanchangData) {
    val lang = LocalAppLanguage.current
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Main Tithi Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "${data.vikramSamvat} • ${data.gregorianDateFormatted}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${data.gujaratiMonth} ${data.paksha} ${data.tithi}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = SaffronPrimary
                    )
                    val vaarLabel = when (lang) { "hi" -> "वार"; "en" -> "Day"; else -> "વાર" }
                    Text(
                        text = "$vaarLabel: ${data.vaar}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (data.specialFestival != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SaffronPrimary)
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            val festPrefix = when (lang) {
                                "hi" -> "आज का विशेष पर्व:"
                                "en" -> "Today's Special Festival:"
                                else -> "આજનો વિશેષ પર્વ:"
                            }
                            Text(
                                text = "$festPrefix ${data.specialFestival}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Sunrise & Sunset
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val sunriseTitle = when (lang) { "hi" -> "सूर्योदय"; "en" -> "Sunrise"; else -> "સૂર્યોદય" }
                val sunsetTitle = when (lang) { "hi" -> "सूर्यास्त"; "en" -> "Sunset"; else -> "સૂર્યાસ્ત" }
                InfoTile(
                    title = "$sunriseTitle (${data.cityName})",
                    value = data.sunrise,
                    color = Color(0xFFE65100),
                    modifier = Modifier.weight(1f)
                )
                InfoTile(
                    title = "$sunsetTitle (${data.cityName})",
                    value = data.sunset,
                    color = Color(0xFFC2185B),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Muhurat & Panchang Ango Details
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    val fiveLimbsTitle = when (lang) {
                        "hi" -> "पंचांग के पांच अंग:"
                        "en" -> "Five Limbs of Panchang:"
                        else -> "પંચાંગના પાંચ અંગો:"
                    }
                    Text(fiveLimbsTitle, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = SaffronPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    val tithiLabel = when (lang) { "hi" -> "तिथि"; "en" -> "Tithi"; else -> "તિથિ" }
                    val vaarLabel = when (lang) { "hi" -> "वार"; "en" -> "Day"; else -> "વાર" }
                    val nakshatraLabel = when (lang) { "hi" -> "नक्षत्र"; "en" -> "Nakshatra"; else -> "નક્ષત્ર" }
                    val yogaLabel = when (lang) { "hi" -> "योग"; "en" -> "Yoga"; else -> "યોગ" }
                    val karanaLabel = when (lang) { "hi" -> "करण"; "en" -> "Karana"; else -> "કરણ" }
                    PanchangRow(label = tithiLabel, value = "${data.paksha} ${data.tithi}")
                    PanchangRow(label = vaarLabel, value = data.vaar)
                    PanchangRow(label = nakshatraLabel, value = data.nakshatra)
                    PanchangRow(label = yogaLabel, value = data.yoga)
                    PanchangRow(label = karanaLabel, value = data.karana)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
                    val muhuratTitle = when (lang) {
                        "hi" -> "शुभ और अशुभ मुहूर्त:"
                        "en" -> "Auspicious & Inauspicious Muhurats:"
                        else -> "શુભ અને અશુભ મુહૂર્ત:"
                    }
                    Text(muhuratTitle, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = SaffronPrimary)
                    Spacer(modifier = Modifier.height(6.dp))
                    val abhijitLabel = when (lang) {
                        "hi" -> "अभिजित मुहूर्त (शुभ)"
                        "en" -> "Abhijit Muhurat (Auspicious)"
                        else -> "અભિજિત મુહૂર્ત (શુભ)"
                    }
                    val rahuLabel = when (lang) {
                        "hi" -> "राहु काल (अशुभ)"
                        "en" -> "Rahu Kaal (Inauspicious)"
                        else -> "રાહુ કાળ (અશુભ)"
                    }
                    PanchangRow(label = abhijitLabel, value = data.abhijitMuhurat, isAuspicious = true)
                    PanchangRow(label = rahuLabel, value = data.rahuKaal, isAuspicious = false)
                }
            }
        }

        item {
            com.example.ui.components.NativeAdCard(modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}

/**
 * Tab 2: Day & Night Choghadiya with toggle.
 */
@Composable
fun ChoghadiyaTab(
    dayList: List<ChoghadiyaItem>,
    nightList: List<ChoghadiyaItem>,
    cityName: String
) {
    val lang = LocalAppLanguage.current
    val isDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    var showNightChoghadiya by remember { mutableStateOf(false) }

    val activeList = if (showNightChoghadiya) nightList else dayList

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Toggle Day / Night
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { showNightChoghadiya = false },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = if (!showNightChoghadiya) {
                            ButtonDefaults.outlinedButtonColors(containerColor = SaffronPrimary, contentColor = Color.White)
                        } else {
                            ButtonDefaults.outlinedButtonColors()
                        }
                    ) {
                        Icon(Icons.Default.WbSunny, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(AppStrings.dayChoghadiya(lang), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { showNightChoghadiya = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = if (showNightChoghadiya) {
                            ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF1E3A8A), contentColor = Color.White)
                        } else {
                            ButtonDefaults.outlinedButtonColors()
                        }
                    ) {
                        Icon(Icons.Default.Nightlight, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(AppStrings.nightChoghadiya(lang), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Text(
                text = if (showNightChoghadiya) "${AppStrings.nightChoghadiya(lang)} ($cityName):" else "${AppStrings.dayChoghadiya(lang)} ($cityName):",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SaffronPrimary,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        items(activeList) { item ->
            val bg = if (item.isAuspicious) {
                if (isDark) Color(0xFF064E3B).copy(alpha = 0.55f) else Color(0xFFE8F5E9)
            } else {
                if (isDark) Color(0xFF7F1D1D).copy(alpha = 0.45f) else Color(0xFFFFEBEE)
            }
            val titleColor = if (item.isAuspicious) {
                if (isDark) Color(0xFF6EE7B7) else Color(0xFF1B5E20)
            } else {
                if (isDark) Color(0xFFFCA5A5) else Color(0xFFB71C1C)
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = bg),
                border = BorderStroke(1.dp, if (item.isAuspicious) Color(0xFF10B981).copy(alpha = 0.4f) else Color(0xFFEF4444).copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = item.nameGujarati,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = titleColor
                        )
                        Text(
                            text = item.natureGujarati,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = item.timeRange,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = AppStrings.choghadiyaNote(lang),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Tab 3: Hindu Festival Directory with Year selector (2026-2030).
 */
@Composable
fun FestivalCalendarTab(
    festivals: List<HinduFestival>,
    selectedYear: Int,
    onSelectYear: (Int) -> Unit
) {
    val lang = LocalAppLanguage.current
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Year filter selector
        item {
            Column {
                Text(
                    text = AppStrings.selectFestivalYear(lang),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(AVAILABLE_YEARS) { yr ->
                        val isSelected = yr == selectedYear
                        FilterChip(
                            selected = isSelected,
                            onClick = { onSelectYear(yr) },
                            label = { Text("$yr", fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SaffronPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = AppStrings.festivalCalendarTitle(selectedYear, lang),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SaffronPrimary
            )
        }

        itemsIndexed(festivals) { fIdx, fest ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = fest.nameGujarati,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SaffronPrimary
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .border(1.dp, SaffronPrimary.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = fest.dateDisplay,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = SaffronPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    val tithiLabel = when (lang) { "hi" -> "तिथि:"; "en" -> "Tithi:"; else -> "તિથિ:" }
                    Text(
                        text = "$tithiLabel ${fest.tithiGujarati}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = fest.descriptionGujarati,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    val ritualsLabel = when (lang) { "hi" -> "पूजन विधि:"; "en" -> "Rituals & Worship:"; else -> "પૂજન વિધિ:" }
                    Text(
                        text = "$ritualsLabel ${fest.ritualsGujarati}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if ((fIdx + 1) % 5 == 0) {
                com.example.ui.components.InlineNativeAd(modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}

@Composable
fun InfoTile(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun PanchangRow(label: String, value: String, isAuspicious: Boolean? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = when (isAuspicious) {
                true -> Color(0xFF2E7D32)
                false -> Color(0xFFC62828)
                else -> MaterialTheme.colorScheme.onSurface
            }
        )
    }
}

@Composable
fun CitySelectionDialog(
    currentCity: String,
    cities: List<CityLocation>,
    onCitySelected: (CityLocation) -> Unit,
    onDismiss: () -> Unit
) {
    val lang = LocalAppLanguage.current
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(AppStrings.selectCityTitle(lang), fontWeight = FontWeight.Bold) },
        text = {
            Column {
                cities.forEach { city ->
                    val isSelected = city.nameGujarati == currentCity
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCitySelected(city) }
                            .padding(vertical = 10.dp, horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${city.nameGujarati} (${city.nameEnglish})",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) SaffronPrimary else MaterialTheme.colorScheme.onSurface
                        )
                        if (isSelected) {
                             Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(AppStrings.cancel(lang))
            }
        }
    )
}
