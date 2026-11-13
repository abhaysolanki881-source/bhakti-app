package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AudioTrack
import com.example.data.model.DailyDarshanItem
import com.example.data.model.DeityCategory
import com.example.data.model.GitaShloka
import com.example.data.model.HinduFestival
import com.example.data.model.PanchangData
import com.example.ui.components.BannerAdView
import com.example.ui.components.AnimatedScaleButton
import com.example.ui.components.DiyaIcon
import com.example.ui.components.SectionHeader
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.SunsetOrange
import com.example.ui.theme.TempleGold

import com.example.ui.strings.AppStrings
import com.example.ui.strings.LocalAppLanguage

data class DayDarshanTab(
    val dayOfWeek: Int,
    val dayNameGujarati: String,
    val deityNameGujarati: String
)

@Composable
fun HomeScreen(
    panchangData: PanchangData,
    dailyDarshan: DailyDarshanItem,
    gitaShloka: GitaShloka,
    deityCategories: List<DeityCategory>,
    featuredAarti: AudioTrack,
    featuredBhajan: AudioTrack,
    upcomingFestivals: List<HinduFestival>,
    onSelectDeity: (DeityCategory) -> Unit,
    onSelectTrack: (AudioTrack) -> Unit = {},
    onPlayTrack: (AudioTrack) -> Unit,
    onNavigateToDarshan: () -> Unit,
    onNavigateToGita: () -> Unit,
    onNavigateToPanchang: () -> Unit,
    onNavigateToJapMala: () -> Unit,
    onNavigateToTemples: () -> Unit,
    selectedDarshanDayOfWeek: Int = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK),
    todayDayOfWeek: Int = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK),
    onSelectDarshanDay: (Int) -> Unit = {},
    onNextDarshanDay: () -> Unit = {},
    onPrevDarshanDay: () -> Unit = {},
    onResetDarshanDay: () -> Unit = {},
    currentLanguage: String = LocalAppLanguage.current,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val weekDays = remember(currentLanguage) {
        listOf(
            DayDarshanTab(java.util.Calendar.SUNDAY, AppStrings.dayNameShort(java.util.Calendar.SUNDAY, currentLanguage), AppStrings.dayDeityShort(java.util.Calendar.SUNDAY, currentLanguage)),
            DayDarshanTab(java.util.Calendar.MONDAY, AppStrings.dayNameShort(java.util.Calendar.MONDAY, currentLanguage), AppStrings.dayDeityShort(java.util.Calendar.MONDAY, currentLanguage)),
            DayDarshanTab(java.util.Calendar.TUESDAY, AppStrings.dayNameShort(java.util.Calendar.TUESDAY, currentLanguage), AppStrings.dayDeityShort(java.util.Calendar.TUESDAY, currentLanguage)),
            DayDarshanTab(java.util.Calendar.WEDNESDAY, AppStrings.dayNameShort(java.util.Calendar.WEDNESDAY, currentLanguage), AppStrings.dayDeityShort(java.util.Calendar.WEDNESDAY, currentLanguage)),
            DayDarshanTab(java.util.Calendar.THURSDAY, AppStrings.dayNameShort(java.util.Calendar.THURSDAY, currentLanguage), AppStrings.dayDeityShort(java.util.Calendar.THURSDAY, currentLanguage)),
            DayDarshanTab(java.util.Calendar.FRIDAY, AppStrings.dayNameShort(java.util.Calendar.FRIDAY, currentLanguage), AppStrings.dayDeityShort(java.util.Calendar.FRIDAY, currentLanguage)),
            DayDarshanTab(java.util.Calendar.SATURDAY, AppStrings.dayNameShort(java.util.Calendar.SATURDAY, currentLanguage), AppStrings.dayDeityShort(java.util.Calendar.SATURDAY, currentLanguage))
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("home_screen_lazy_column"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // 0. Interactive 7-Day Darshan Selector Strip (સાત વાર વિશેષ દર્શન)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        DiyaIcon(modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.dayStripTitle(currentLanguage),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    if (selectedDarshanDayOfWeek != todayDayOfWeek) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(SaffronPrimary.copy(alpha = 0.18f))
                                .border(1.dp, SaffronPrimary, RoundedCornerShape(12.dp))
                                .clickable { onResetDarshanDay() }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                .testTag("btn_reset_darshan_today")
                        ) {
                            Text(
                                text = "${AppStrings.todayBadge(currentLanguage)} (Today)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SaffronDark
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    items(weekDays) { day ->
                        val isSelected = day.dayOfWeek == selectedDarshanDayOfWeek
                        val isToday = day.dayOfWeek == todayDayOfWeek

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (isSelected) SaffronPrimary
                                    else if (isToday) SaffronPrimary.copy(alpha = 0.16f)
                                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                )
                                .border(
                                    width = if (isSelected) 2.dp else if (isToday) 1.5.dp else 1.dp,
                                    color = if (isSelected) SaffronDark else if (isToday) SaffronPrimary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .clickable { onSelectDarshanDay(day.dayOfWeek) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("darshan_day_${day.dayOfWeek}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = day.dayNameGujarati,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color(0xFF1F1206) else MaterialTheme.colorScheme.onSurface
                                    )
                                    if (isToday) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(if (isSelected) Color(0xFF1F1206) else SaffronPrimary)
                                        )
                                    }
                                }
                                Text(
                                    text = day.deityNameGujarati,
                                    fontSize = 10.sp,
                                    color = if (isSelected) Color(0xFF3B1D04) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // 1. Full-Bleed Realistic Hero Banner with Day-wise Divine Darshan
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .shadow(16.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black)
            ) {
                AnimatedContent(
                    targetState = dailyDarshan,
                    transitionSpec = {
                        fadeIn(tween(350)) togetherWith fadeOut(tween(250))
                    },
                    label = "DarshanHeroCard"
                ) { darshanItem ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(290.dp)
                    ) {
                        // Realistic Photography Asset
                        val imageRes = darshanItem.imageResId ?: R.drawable.img_darshan_krishna_1793948267799
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = darshanItem.titleGujarati,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Cinematic Gradient Scrim (Golden Sunburst to Deep Shadow)
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0x77000000),
                                            Color(0x22000000),
                                            Color(0xE60F0A06)
                                        )
                                    )
                                )
                        )

                        // Top Badge: "આજનું દિવ્ય દર્શન • સોમવાર વિશેષ"
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF0F0A06).copy(alpha = 0.82f))
                                    .border(1.dp, Color(0xFFFBBF24).copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    DiyaIcon(modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${AppStrings.todayDarshanTitle(currentLanguage)} • ${darshanItem.dayOfWeekGujarati}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFDE68A)
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.Black.copy(alpha = 0.6f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = darshanItem.templeName,
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.95f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Left Navigation Arrow (Previous Day)
                        IconButton(
                            onClick = onPrevDarshanDay,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 6.dp)
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.45f))
                                .testTag("btn_prev_darshan_day")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = "Previous Day Darshan",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        // Right Navigation Arrow (Next Day)
                        IconButton(
                            onClick = onNextDarshanDay,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 6.dp)
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.45f))
                                .testTag("btn_next_darshan_day")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Next Day Darshan",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        // Bottom Content on Hero Banner
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .fillMaxWidth()
                                .padding(18.dp)
                        ) {
                            Text(
                                text = darshanItem.deityName,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFFBEB)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "“${darshanItem.suvicharGujarati}”",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFFEF3C7),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AnimatedScaleButton(
                                    onClick = onNavigateToDarshan,
                                    backgroundColor = SaffronPrimary,
                                    modifier = Modifier.testTag("home_view_darshan_button")
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = AppStrings.darshanKaro(currentLanguage),
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1F1206),
                                            fontSize = 13.sp
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null,
                                            tint = Color(0xFF1F1206),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color.White.copy(alpha = 0.2f))
                                        .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                                        .clickable {
                                            val sendIntent = Intent().apply {
                                                action = Intent.ACTION_SEND
                                                putExtra(
                                                    Intent.EXTRA_TEXT,
                                                    "${darshanItem.titleGujarati}\n${darshanItem.templeName}\n\n“${darshanItem.suvicharGujarati}”\n\n- ભક્તિ સાગર ગુજરાતી"
                                                )
                                                type = "text/plain"
                                            }
                                            context.startActivity(Intent.createChooser(sendIntent, AppStrings.share(currentLanguage)))
                                        }
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Share,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(AppStrings.share(currentLanguage), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Modern Glassmorphic Panchang Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clickable { onNavigateToPanchang() }
                    .shadow(4.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(
                        listOf(Color(0xFFFDE68A), Color(0xFFF59E0B).copy(alpha = 0.4f))
                    )
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFFFEF3C7), Color(0xFFFDE68A))
                                )
                            )
                            .border(1.5.dp, Color(0xFFF59E0B), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = SaffronDark,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = AppStrings.todayPanchangTitle(currentLanguage),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .border(1.dp, SaffronPrimary.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = panchangData.cityName,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SaffronPrimary
                                )
                            }
                        }
                        Text(
                            text = "${panchangData.gujaratiMonth} ${panchangData.paksha} ${panchangData.tithi} • ${panchangData.vaar}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SaffronDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${AppStrings.sunrise(currentLanguage)}: ${panchangData.sunrise} • ${AppStrings.nakshatra(currentLanguage)}: ${panchangData.nakshatra}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    val detailsDesc = if (currentLanguage == "en") "Details" else if (currentLanguage == "hi") "विवरण" else "વિગતો"
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = detailsDesc,
                        tint = SaffronDark,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // 3. Modern Interactive Quick Tools
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ModernQuickToolCard(
                    title = AppStrings.toolJapMala(currentLanguage),
                    subtitle = AppStrings.toolJapMalaSub(currentLanguage),
                    icon = Icons.Default.AutoAwesome,
                    gradient = listOf(Color(0xFFEA580C), Color(0xFFF59E0B)),
                    onClick = onNavigateToJapMala,
                    modifier = Modifier.weight(1f)
                )
                ModernQuickToolCard(
                    title = AppStrings.toolGita(currentLanguage),
                    subtitle = AppStrings.toolGitaSub(currentLanguage),
                    icon = Icons.Default.Book,
                    gradient = listOf(Color(0xFF047857), Color(0xFF10B981)),
                    onClick = onNavigateToGita,
                    modifier = Modifier.weight(1f)
                )
                ModernQuickToolCard(
                    title = AppStrings.toolTemples(currentLanguage),
                    subtitle = AppStrings.toolTemplesSub(currentLanguage),
                    icon = Icons.Default.WbSunny,
                    gradient = listOf(Color(0xFFB91C1C), Color(0xFFEA580C)),
                    onClick = onNavigateToTemples,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 4. Deity Categories Carousel
        item {
            SectionHeader(
                titleGujarati = AppStrings.deityDarshanSection(currentLanguage),
                subtitleGujarati = null,
                actionTextGujarati = AppStrings.viewAll(currentLanguage),
                onActionClick = onNavigateToDarshan
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(deityCategories) { deity ->
                    ModernDeityCategoryCard(deity = deity, onClick = { onSelectDeity(deity) })
                }
            }
        }

        // 5. Daily Gita Shloka of the Day
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clickable { onNavigateToGita() }
                    .shadow(4.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(
                        listOf(Color(0xFFFDE68A), Color(0xFFD97706).copy(alpha = 0.3f))
                    )
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(SaffronPrimary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${AppStrings.gitaShlokaBodh(currentLanguage)} (${AppStrings.chapter(currentLanguage)} ${gitaShloka.chapterNumber})",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = SaffronDark
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Book,
                            contentDescription = null,
                            tint = SaffronDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val isDarkTheme = MaterialTheme.colorScheme.surface.luminance() < 0.5f

                    Text(
                        text = gitaShloka.sanskrit,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color(0xFFFDE68A) else Color(0xFF991B1B),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(1.dp, SaffronPrimary.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${AppStrings.meaning(currentLanguage)} ${gitaShloka.gujaratiMeaning}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${AppStrings.lifeLesson(currentLanguage)} ${gitaShloka.practicalLessonGujarati}",
                        style = MaterialTheme.typography.bodySmall,
                        color = SaffronDark,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // 6. Featured Daily Aarti & Bhajan
        item {
            SectionHeader(
                titleGujarati = AppStrings.featuredAudioSection(currentLanguage),
                subtitleGujarati = null
            )

            ModernTrackRowItem(
                track = featuredAarti,
                onSelectTrack = { onSelectTrack(featuredAarti) },
                onPlayClick = { onPlayTrack(featuredAarti) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
            )

            ModernTrackRowItem(
                track = featuredBhajan,
                onSelectTrack = { onSelectTrack(featuredBhajan) },
                onPlayClick = { onPlayTrack(featuredBhajan) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
            )
        }

        // Native Ad between major sections
        item {
            com.example.ui.components.NativeAdCard(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        // 7. Upcoming Hindu Festivals
        item {
            SectionHeader(
                titleGujarati = AppStrings.upcomingFestivalsSection(currentLanguage),
                subtitleGujarati = null,
                actionTextGujarati = AppStrings.viewAll(currentLanguage),
                onActionClick = onNavigateToPanchang
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(upcomingFestivals.take(4)) { festival ->
                    ModernFestivalCard(festival = festival, onClick = onNavigateToPanchang)
                }
            }
        }

        // 8. Dedicated Next-Gen Banner Ad
        item {
            Spacer(modifier = Modifier.height(12.dp))
            BannerAdView(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp))
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ModernQuickToolCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradient: List<Color>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "ToolCardScale"
    )

    Card(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(18.dp))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .shadow(if (isPressed) 2.dp else 6.dp, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(gradient)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ModernDeityCategoryCard(
    deity: DeityCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "DeityScale"
    )

    // Realistic deity image mapping where available
    val imageRes = deity.imageResId ?: when (deity.id) {
        "krishna" -> R.drawable.img_darshan_krishna_1793948267799
        "shiva" -> R.drawable.img_darshan_shiva_1793948288707
        "hanuman" -> R.drawable.img_darshan_hanuman_1793948310273
        "ganesha" -> R.drawable.img_deity_ganesh_1793958512473
        "ambaji" -> R.drawable.img_deity_ambaji_1793958532560
        "ram" -> R.drawable.img_deity_ram_1793958549091
        "swaminarayan" -> R.drawable.img_deity_swaminarayan_1793958571996
        else -> null
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(88.dp)
            .scale(scale)
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(74.dp)
                .clip(CircleShape)
                .border(2.dp, SaffronPrimary, CircleShape)
                .shadow(8.dp, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (imageRes != null) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = deity.nameGujarati,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                listOf(Color(0xFFFEF3C7), Color(0xFFF59E0B), Color(deity.primaryColorHex))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    DiyaIcon(modifier = Modifier.size(34.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = AppStrings.getDeityName(deity.id, deity.nameGujarati, LocalAppLanguage.current),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ModernTrackRowItem(
    track: AudioTrack,
    onSelectTrack: () -> Unit,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelectTrack() }
            .shadow(2.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val trackImageRes = track.imageResId ?: when {
                track.deityNameGujarati.contains("ગણેશ") -> R.drawable.img_deity_ganesh_1793958512473
                track.deityNameGujarati.contains("અંબા") || track.deityNameGujarati.contains("ગાયત્રી") -> R.drawable.img_deity_ambaji_1793958532560
                track.deityNameGujarati.contains("રામ") -> R.drawable.img_deity_ram_1793958549091
                track.deityNameGujarati.contains("શિવ") -> R.drawable.img_darshan_shiva_1793948288707
                track.deityNameGujarati.contains("હનુમાન") -> R.drawable.img_darshan_hanuman_1793948310273
                track.deityNameGujarati.contains("સ્વામિનારાયણ") -> R.drawable.img_deity_swaminarayan_1793958571996
                else -> R.drawable.img_darshan_krishna_1793948267799
            }
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.5.dp, SaffronPrimary.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                    .shadow(4.dp, RoundedCornerShape(14.dp))
                    .clickable { onPlayClick() }
            ) {
                Image(
                    painter = painterResource(id = trackImageRes),
                    contentDescription = track.titleGujarati,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(SaffronPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        val lang = com.example.ui.strings.LocalAppLanguage.current
                        val playDesc = if (lang == "en") "Play" else if (lang == "hi") "चलाएं" else "રમો"
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = playDesc,
                            tint = Color(0xFF1F1206),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = track.titleGujarati,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${track.deityNameGujarati} • ${AppStrings.getCategoryTitle(track.category.name, LocalAppLanguage.current)} • ${track.durationSeconds / 60} min",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AnimatedScaleButton(
                onClick = onPlayClick,
                backgroundColor = SaffronPrimary,
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = AppStrings.listenNow(LocalAppLanguage.current),
                    fontSize = 11.sp,
                    color = Color(0xFF1F1206),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ModernFestivalCard(
    festival: HinduFestival,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(170.dp)
            .clickable { onClick() }
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(Color(0xFFFDE68A), Color(0xFFF59E0B).copy(alpha = 0.5f))
            )
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, SaffronPrimary.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = festival.dateDisplay,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = SaffronPrimary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = festival.nameGujarati,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = festival.tithiGujarati,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}
