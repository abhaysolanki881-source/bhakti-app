package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.DailyDarshanItem
import com.example.data.model.DeityCategory
import com.example.data.model.TempleInfo
import com.example.ui.components.AnimatedScaleButton
import com.example.ui.components.BhaktiSubScreenTopBar
import com.example.ui.components.DiyaIcon
import com.example.ui.strings.AppStrings
import com.example.ui.strings.LocalAppLanguage
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronLight
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.SunsetOrange

@Composable
fun DarshanScreen(
    darshanList: List<DailyDarshanItem>,
    deityCategories: List<DeityCategory>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    initialDeityId: String? = null
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val lang = LocalAppLanguage.current
    var selectedDeityId by remember { mutableStateOf<String?>(initialDeityId) }
    var viewingDarshan by remember { mutableStateOf<DailyDarshanItem?>(null) }
    val isDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f

    if (viewingDarshan != null) {
        val item = viewingDarshan!!
        AlertDialog(
            onDismissRequest = { viewingDarshan = null },
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(item.titleGujarati, fontWeight = FontWeight.Bold, color = if (isDark) SaffronPrimary else SaffronDark)
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    val imgRes = item.imageResId ?: R.drawable.img_darshan_krishna_1793948267799
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Image(
                            painter = painterResource(id = imgRes),
                            contentDescription = item.deityName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(item.templeName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isDark) Color(0xFF26201B) else Color(0xFFFFFBEB))
                            .border(1.dp, if (isDark) SaffronDark.copy(alpha = 0.5f) else Color(0xFFFDE68A), RoundedCornerShape(10.dp))
                            .padding(8.dp)
                    ) {
                        Text(
                            item.quoteGujarati,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) SaffronLight else Color(0xFF991B1B),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(item.blessingGujarati, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
                }
            },
            confirmButton = {
                AnimatedScaleButton(
                    onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "${item.titleGujarati}\n${item.templeName}\n\n${item.quoteGujarati}\n\n${item.blessingGujarati}\n\n- ${AppStrings.appTitle(lang)}")
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, AppStrings.share(lang)))
                    },
                    backgroundColor = SaffronPrimary
                ) {
                    Text(AppStrings.shareDarshan(lang), color = Color(0xFF1F1206), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewingDarshan = null }) {
                    Text(AppStrings.close(lang), color = if (isDark) SaffronLight else MaterialTheme.colorScheme.onSurface)
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("darshan_screen")
    ) {
        BhaktiSubScreenTopBar(
            title = AppStrings.todayDarshanTitle(lang),
            subtitle = AppStrings.todayDarshanSubtitle(lang),
            onBack = onBack
        )

        // Deity Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedDeityId == null,
                    onClick = { selectedDeityId = null },
                    label = { Text(AppStrings.allDeitiesFilter(lang)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SaffronPrimary,
                        selectedLabelColor = Color(0xFF1F1206),
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
            items(deityCategories) { deity ->
                FilterChip(
                    selected = selectedDeityId == deity.id,
                    onClick = { selectedDeityId = deity.id },
                    label = { Text(AppStrings.getDeityName(deity.id, deity.nameGujarati, lang)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SaffronPrimary,
                        selectedLabelColor = Color(0xFF1F1206),
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        val filtered = remember(selectedDeityId, darshanList) {
            if (selectedDeityId == null) {
                darshanList
            } else {
                val byId = darshanList.filter { it.deityId.equals(selectedDeityId, ignoreCase = true) }
                if (byId.isNotEmpty()) {
                    byId
                } else {
                    // Safe fuzzy fallback by deity name
                    val cat = deityCategories.find { it.id == selectedDeityId }
                    val byName = if (cat != null) {
                        darshanList.filter {
                            it.deityName.contains(cat.nameGujarati.take(3)) || cat.nameGujarati.contains(it.deityName.take(3))
                        }
                    } else emptyList()
                    if (byName.isNotEmpty()) byName else darshanList
                }
            }
        }

        AnimatedContent(
            targetState = selectedDeityId,
            transitionSpec = {
                fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(180))
            },
            label = "DarshanFilterTransition",
            modifier = Modifier.weight(1f)
        ) { _ ->
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(filtered, key = { _, it -> it.id }) { dIdx, darshan ->
                    val imgRes = darshan.imageResId ?: R.drawable.img_darshan_krishna_1793948267799
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(8.dp, RoundedCornerShape(22.dp))
                            .clickable { viewingDarshan = darshan },
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(
                                listOf(Color(0xFFFDE68A), Color(0xFFF59E0B).copy(alpha = 0.4f))
                            )
                        )
                    ) {
                        Column {
                            // Realistic Photo Banner
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(190.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = imgRes),
                                    contentDescription = darshan.titleGujarati,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(Color.Transparent, Color(0xCC0F0A06))
                                            )
                                        )
                                    )
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = darshan.deityName,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFFFFBEB)
                                        )
                                        Text(
                                            text = darshan.templeName,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color(0xFFFDE68A)
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color.Black.copy(alpha = 0.6f))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        DiyaIcon(modifier = Modifier.size(18.dp))
                                    }
                                }
                            }

                            Column(modifier = Modifier.padding(16.dp)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isDark) Color(0xFF26201B) else Color(0xFFFFFBEB))
                                        .border(
                                            width = 1.dp,
                                            color = if (isDark) SaffronDark.copy(alpha = 0.5f) else Color(0xFFFDE68A),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = darshan.quoteGujarati,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) SaffronLight else Color(0xFF991B1B),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = darshan.blessingGujarati,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedButton(
                                        onClick = { viewingDarshan = darshan },
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(AppStrings.bigDarshan(lang))
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    AnimatedScaleButton(
                                        onClick = {
                                            val sendIntent = Intent().apply {
                                                action = Intent.ACTION_SEND
                                                putExtra(Intent.EXTRA_TEXT, "${darshan.titleGujarati}\n${darshan.templeName}\n\n${darshan.blessingGujarati}\n\n- ભક્તિ સાગર ગુજરાતી")
                                                type = "text/plain"
                                            }
                                            context.startActivity(Intent.createChooser(sendIntent, AppStrings.shareDarshan(lang)))
                                        },
                                        backgroundColor = SaffronPrimary,
                                        shape = RoundedCornerShape(12.dp),
                                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Share, contentDescription = null, tint = Color(0xFF1F1206), modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(AppStrings.share(lang), color = Color(0xFF1F1206), fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if ((dIdx + 1) % 4 == 0) {
                        com.example.ui.components.InlineNativeAd(modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        }

        com.example.ui.components.BannerAdView(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
    }
}

@Composable
fun TempleDirectoryScreen(
    temples: List<TempleInfo>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val lang = LocalAppLanguage.current
    var selectedTemple by remember { mutableStateOf<TempleInfo?>(null) }

    val isDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f

    if (selectedTemple != null) {
        val t = selectedTemple!!
        AlertDialog(
            onDismissRequest = { selectedTemple = null },
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(t.nameGujarati, fontWeight = FontWeight.Bold, color = if (isDark) SaffronPrimary else SaffronDark)
            },
            text = {
                Column {
                    val templeImg = t.imageResId ?: R.drawable.img_temple_dwarka_1793948331429
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Image(
                            painter = painterResource(id = templeImg),
                            contentDescription = t.nameGujarati,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("${AppStrings.templeLocation(lang)}: ${t.location}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(t.historicalOverview, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("${AppStrings.darshanTimings(lang)}: ${t.darshanTimings}", style = MaterialTheme.typography.bodySmall, color = if (isDark) SaffronPrimary else SaffronDark)
                    Text("${AppStrings.aartiTimings(lang)}: ${t.aartiTimings}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("${AppStrings.address(lang)}: ${t.address}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            confirmButton = {
                AnimatedScaleButton(
                    onClick = {
                        val mapUri = Uri.parse("geo:0,0?q=${Uri.encode(t.nameGujarati + " " + t.location)}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
                        context.startActivity(mapIntent)
                    },
                    backgroundColor = SaffronPrimary
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Map, contentDescription = null, tint = Color(0xFF1F1206), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(AppStrings.viewOnMaps(lang), color = Color(0xFF1F1206), fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedTemple = null }) {
                    Text(AppStrings.close(lang), color = if (isDark) SaffronLight else MaterialTheme.colorScheme.onSurface)
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("temple_directory_screen")
    ) {
        BhaktiSubScreenTopBar(
            title = AppStrings.templeDirectoryTitle(lang),
            subtitle = AppStrings.templeDirectoryDesc(lang),
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            itemsIndexed(temples) { tIdx, temple ->
                val templeImg = temple.imageResId ?: R.drawable.img_temple_dwarka_1793948331429
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(20.dp))
                        .clickable { selectedTemple = temple },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(
                            listOf(Color(0xFFFDE68A), Color(0xFFF59E0B).copy(alpha = 0.35f))
                        )
                    )
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        ) {
                            Image(
                                painter = painterResource(id = templeImg),
                                contentDescription = temple.nameGujarati,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color.Transparent, Color(0xCC0F0A06))
                                        )
                                    )
                            )
                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = temple.nameGujarati,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFFFBEB)
                                    )
                                    Text(
                                        text = temple.location,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFFDE68A)
                                    )
                                }
                            }
                        }

                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = temple.historicalOverview,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 2
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(15.dp), tint = Color(0xFF047857))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("${AppStrings.darshanTimings(lang)}: ${temple.darshanTimings.take(18)}...", style = MaterialTheme.typography.labelSmall, color = Color(0xFF047857))
                                }

                                AnimatedScaleButton(
                                    onClick = { selectedTemple = temple },
                                    backgroundColor = SaffronPrimary,
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    val detailsLabel = when (lang) {
                                        "hi" -> "विवरण देखें"
                                        "en" -> "View Details"
                                        else -> "વિગતો જુઓ"
                                    }
                                    Text(detailsLabel, color = Color(0xFF1F1206), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
                if ((tIdx + 1) % 4 == 0) {
                    com.example.ui.components.InlineNativeAd(modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }

        com.example.ui.components.BannerAdView(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
    }
}
