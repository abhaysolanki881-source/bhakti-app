package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import com.example.ui.components.NativeAdCard
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DevotionalScripture
import com.example.data.model.GitaChapter
import com.example.data.model.GitaShloka
import com.example.ui.components.BhaktiSubScreenTopBar
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.OutlinedButton
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronLight
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.TempleGold
import com.example.ui.strings.AppStrings
import com.example.ui.strings.LocalAppLanguage

@Composable
fun ReadScreen(
    scriptures: List<DevotionalScripture>,
    gitaChapters: List<GitaChapter>,
    onOpenGita: (GitaChapter?) -> Unit,
    onSaveBookmark: (title: String, snippet: String, chapter: Int, verse: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current
    var selectedTab by remember { mutableIntStateOf(0) }
    var fontScale by remember { mutableFloatStateOf(1.0f) }
    val tabs = remember(lang) {
        listOf(
            AppStrings.tabGita(lang),
            AppStrings.tabHanuman(lang),
            AppStrings.tabShiv(lang),
            AppStrings.tabGanesh(lang),
            AppStrings.tabDurga(lang)
        )
    }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("read_screen")
    ) {
        // Tab selector
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = SaffronPrimary
                )
            },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            tabs.forEachIndexed { idx, title ->
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

        // Font size control row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.FormatSize, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(AppStrings.fontSize(lang), style = MaterialTheme.typography.bodySmall)
            }
            Row {
                IconButton(onClick = { if (fontScale > 0.85f) fontScale -= 0.1f }) {
                    Text("A-", fontWeight = FontWeight.Bold, color = SaffronPrimary)
                }
                IconButton(onClick = { if (fontScale < 1.4f) fontScale += 0.1f }) {
                    Text("A+", fontWeight = FontWeight.Bold, color = SaffronPrimary)
                }
            }
        }

        if (selectedTab == 0) {
            // Bhagavad Gita 18 Chapters View
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .shadow(2.dp, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .border(1.5.dp, SaffronPrimary, RoundedCornerShape(14.dp))
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_darshan_krishna_1793948267799),
                                    contentDescription = AppStrings.getDeityName("KRISHNA", "શ્રી કૃષ્ણ", lang),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = AppStrings.gitaBannerTitle(lang),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = SaffronPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = AppStrings.gitaBannerDesc(lang),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                itemsIndexed(gitaChapters) { index, chapter ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenGita(chapter) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(SaffronPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${chapter.number}",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${AppStrings.chapter(lang)} ${chapter.number} : ${chapter.titleGujarati}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${chapter.titleSanskrit} • ${AppStrings.versesLabel(chapter.versesCount, lang)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SaffronPrimary
                                )
                                Text(
                                    text = chapter.summaryGujarati,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = SaffronPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    if ((index + 1) % 5 == 0) {
                        com.example.ui.components.InlineNativeAd(modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        } else {
            // Scripture Reading (Chalisas, Atharvashirsha)
            val currentScripture = when (selectedTab) {
                1 -> scriptures.find { it.id == "hanuman_chalisa" }
                2 -> scriptures.find { it.id == "shiv_chalisa" }
                3 -> scriptures.find { it.id == "ganesh_atharvashirsha" }
                else -> scriptures.find { it.id == "durga_chalisa" }
            }

            if (currentScripture != null) {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        val deityImg = when (selectedTab) {
                            1 -> R.drawable.img_darshan_hanuman_1793948310273
                            2 -> R.drawable.img_darshan_shiva_1793948288707
                            3 -> R.drawable.img_deity_ganesh_1793958512473
                            else -> R.drawable.img_deity_ambaji_1793958532560
                        }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(2.dp, RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .border(1.5.dp, SaffronPrimary, RoundedCornerShape(14.dp))
                                ) {
                                    Image(
                                        painter = painterResource(id = deityImg),
                                        contentDescription = currentScripture.deityName,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = currentScripture.titleGujarati,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = SaffronPrimary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = AppStrings.completeText(currentScripture.verses.size, lang),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = TempleGold
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = currentScripture.introductionGujarati,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    itemsIndexed(currentScripture.verses) { vIdx, verse ->
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
                                        text = verse.meaningGujarati,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = SaffronPrimary
                                    )
                                    val bookmarkDesc = when (lang) {
                                        "en" -> "Bookmark"
                                        "hi" -> "बुकमार्क"
                                        else -> "બુકમાર્ક"
                                    }
                                    IconButton(
                                        onClick = {
                                            onSaveBookmark(
                                                currentScripture.titleGujarati,
                                                verse.originalText,
                                                0,
                                                verse.verseNumber
                                            )
                                        },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.BookmarkBorder,
                                            contentDescription = bookmarkDesc,
                                            tint = SaffronPrimary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = verse.originalText,
                                    fontSize = (16 * fontScale).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (MaterialTheme.colorScheme.surface.luminance() < 0.5f) SaffronLight else Color(0xFF880E4F),
                                    lineHeight = (24 * fontScale).sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "${AppStrings.meaningPrefix(lang)}: ${verse.gujaratiText}",
                                    fontSize = (14 * fontScale).sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = (22 * fontScale).sp
                                )
                            }
                        }
                        if ((vIdx + 1) % 6 == 0) {
                            com.example.ui.components.InlineNativeAd(modifier = Modifier.padding(vertical = 4.dp))
                        }
                    }
                }
            }
        }

        com.example.ui.components.BannerAdView(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
    }
}

@Composable
fun GitaReaderScreen(
    chapter: GitaChapter,
    shlokas: List<GitaShloka>,
    allChapters: List<GitaChapter> = emptyList(),
    onSelectChapter: (GitaChapter) -> Unit = {},
    onBack: () -> Unit,
    onSaveBookmark: (chapter: Int, verse: Int, text: String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var fontScale by remember { mutableFloatStateOf(1.0f) }

    val lang = LocalAppLanguage.current
    val currentIndex = allChapters.indexOfFirst { it.number == chapter.number }
    val prevChapter = if (currentIndex > 0) allChapters.getOrNull(currentIndex - 1) else null
    val nextChapter = if (currentIndex in 0 until allChapters.size - 1) allChapters.getOrNull(currentIndex + 1) else null

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("gita_reader_screen")
    ) {
        BhaktiSubScreenTopBar(
            title = "${AppStrings.chapter(lang)} ${chapter.number}: ${chapter.titleGujarati}",
            subtitle = "${chapter.titleSanskrit} • ${AppStrings.versesLabel(chapter.versesCount, lang)}",
            onBack = onBack,
            actions = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { if (fontScale > 0.85f) fontScale -= 0.1f }) {
                        Text("A-", fontWeight = FontWeight.Bold, color = SaffronPrimary)
                    }
                    IconButton(onClick = { if (fontScale < 1.4f) fontScale += 0.1f }) {
                        Text("A+", fontWeight = FontWeight.Bold, color = SaffronPrimary)
                    }
                }
            }
        )

        // Quick Chapter Switcher Horizontal Bar (all 18 chapters accessible directly)
        if (allChapters.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(allChapters) { itemChapter ->
                    val isCurrent = itemChapter.number == chapter.number
                    FilterChip(
                        selected = isCurrent,
                        onClick = {
                            if (!isCurrent) {
                                onSelectChapter(itemChapter)
                            }
                        },
                        label = {
                            val chPfx = if (lang == "en") "Ch." else if (lang == "hi") "अ." else "અ."
                            Text(
                                text = "$chPfx${itemChapter.number} ${itemChapter.titleGujarati}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SaffronPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = if (isCurrent) SaffronPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            enabled = true,
                            selected = isCurrent
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Chapter Summary & Context Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(SaffronPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${chapter.number}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            Column {
                                Text(
                                    text = "${AppStrings.chapter(lang)} ${chapter.number} : ${chapter.titleGujarati}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = SaffronPrimary
                                )
                                Text(
                                    text = "${chapter.titleSanskrit} • ${AppStrings.versesLabel(chapter.versesCount, lang)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = chapter.summaryGujarati,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (chapter.detailedSummaryGujarati.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(
                                color = SaffronPrimary.copy(alpha = 0.25f),
                                thickness = 1.dp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = AppStrings.detailedBackgroundTitle(lang),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = SaffronDark
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = chapter.detailedSummaryGujarati,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 20.sp
                            )
                        }

                        if (chapter.coreTeachings.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = AppStrings.coreTeachingsTitle(lang),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = SaffronDark
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            chapter.coreTeachings.forEach { teaching ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = "• ",
                                        color = SaffronPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = teaching,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 19.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppStrings.keyVersesHeader(shlokas.size, lang),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SaffronPrimary
                    )
                }
            }

            // Shlokas
            items(shlokas) { shloka ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
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
                                text = "${AppStrings.shloka(lang)} ${shloka.verseNumber}",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = SaffronPrimary
                            )
                            Row {
                                IconButton(
                                    onClick = {
                                        val sendIntent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(
                                                Intent.EXTRA_TEXT,
                                                "શ્રીમદ્ ભગવદ્ ગીતા (${AppStrings.chapter(lang)} ${chapter.number}, ${AppStrings.shloka(lang)} ${shloka.verseNumber})\n\n${shloka.sanskrit}\n\n${AppStrings.meaning(lang)}\n${shloka.gujaratiMeaning}\n\n${AppStrings.lifeLesson(lang)} ${shloka.practicalLessonGujarati}\n\n- ભક્તિ સાગર ગુજરાતી"
                                            )
                                            type = "text/plain"
                                        }
                                        context.startActivity(Intent.createChooser(sendIntent, AppStrings.share(lang)))
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Share, contentDescription = AppStrings.share(lang), tint = SaffronPrimary, modifier = Modifier.size(18.dp))
                                }
                                IconButton(
                                    onClick = { onSaveBookmark(chapter.number, shloka.verseNumber, shloka.sanskrit) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.BookmarkBorder, contentDescription = AppStrings.bookmark(lang), tint = SaffronPrimary, modifier = Modifier.size(18.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .border(1.dp, SaffronPrimary.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = shloka.sanskrit,
                                fontSize = (16 * fontScale).sp,
                                fontWeight = FontWeight.Bold,
                                color = SaffronPrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                lineHeight = (24 * fontScale).sp
                            )
                        }

                        if (shloka.transliteration.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "${AppStrings.pronunciation(lang)} ${shloka.transliteration}",
                                fontSize = (13 * fontScale).sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = (18 * fontScale).sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${AppStrings.meaning(lang)} ${shloka.gujaratiMeaning}",
                            fontSize = (14 * fontScale).sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = (22 * fontScale).sp
                        )

                        if (shloka.practicalLessonGujarati.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "${AppStrings.lifeLesson(lang)} ${shloka.practicalLessonGujarati}",
                                fontSize = (13 * fontScale).sp,
                                fontWeight = FontWeight.Medium,
                                color = SaffronPrimary,
                                lineHeight = (20 * fontScale).sp
                            )
                        }
                    }
                }
            }

            // Bottom Previous / Next Chapter Navigation Buttons
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (prevChapter != null) {
                        OutlinedButton(
                            onClick = { onSelectChapter(prevChapter) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            val chPfx = if (lang == "en") "Ch." else if (lang == "hi") "अ." else "અ."
                            Text(
                                text = "${AppStrings.prevChapter(lang)}: $chPfx${prevChapter.number}",
                                maxLines = 1
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    if (nextChapter != null) {
                        Button(
                            onClick = { onSelectChapter(nextChapter) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                            modifier = Modifier.weight(1f)
                        ) {
                            val chPfx = if (lang == "en") "Ch." else if (lang == "hi") "अ." else "અ."
                            Text(
                                text = "${AppStrings.nextChapter(lang)}: $chPfx${nextChapter.number}",
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        com.example.ui.components.BannerAdView(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
    }
}
