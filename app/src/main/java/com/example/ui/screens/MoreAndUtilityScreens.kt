package com.example.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.ads.AdMobManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.BookmarkEntity
import com.example.data.local.FavoriteEntity
import com.example.data.model.AudioTrack
import com.example.data.model.DeityCategory
import com.example.data.model.GitaChapter
import com.example.data.model.TempleInfo
import com.example.data.model.WallpaperItem
import com.example.services.AdMobService
import com.example.services.NotificationHelper
import com.example.ui.components.BhaktiSubScreenTopBar
import com.example.ui.components.BannerAdView
import com.example.ui.components.NativeAdCard
import com.example.ui.components.InlineNativeAd
import com.example.ui.components.DiyaIcon
import com.example.ui.components.RewardedActionConfirmDialog
import com.example.ui.strings.AppStrings
import com.example.ui.strings.LocalAppLanguage
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.TempleGold

@Composable
fun MoreMenuScreen(
    onNavigateToJapMala: () -> Unit,
    onNavigateToTemples: () -> Unit,
    onNavigateToWallpapers: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToPrivacyPolicy: () -> Unit = {},
    onNavigateToTerms: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val lang = com.example.ui.strings.LocalAppLanguage.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("more_menu_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DiyaIcon(modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = AppStrings.appTitle(lang),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SaffronPrimary
                            )
                            Text(
                                text = AppStrings.appSubtitle(lang),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            item {
                MoreNavigationCard(
                    title = AppStrings.japMalaTitle(lang),
                    subtitle = AppStrings.japMalaDesc(lang),
                    icon = Icons.Default.AutoAwesome,
                    onClick = onNavigateToJapMala
                )
            }

            item {
                MoreNavigationCard(
                    title = AppStrings.templeDirectoryTitle(lang),
                    subtitle = AppStrings.templeDirectoryDesc(lang),
                    icon = Icons.Default.LocationOn,
                    onClick = onNavigateToTemples
                )
            }

            item {
                MoreNavigationCard(
                    title = AppStrings.wallpaperStudioTitle(lang),
                    subtitle = AppStrings.wallpaperStudioDesc(lang),
                    icon = Icons.Default.Image,
                    onClick = onNavigateToWallpapers
                )
            }

            // Native Ad Card inside More options
            item {
                NativeAdCard(modifier = Modifier.padding(vertical = 4.dp))
            }

            item {
                MoreNavigationCard(
                    title = AppStrings.favoritesTitle(lang),
                    subtitle = when (lang) {
                        "hi" -> "सहेजी गई आरती, भजन और गीता श्लोक"
                        "en" -> "Saved Aarti, Bhajan and Gita Shlokas"
                        else -> "સાચવેલ આરતી, ભજન અને ગીતા શ્લોક"
                    },
                    icon = Icons.Default.Bookmark,
                    onClick = onNavigateToFavorites
                )
            }

            item {
                MoreNavigationCard(
                    title = AppStrings.settingsTitle(lang),
                    subtitle = when (lang) {
                        "hi" -> "डार्क थीम, पूजा स्मरण, भाषा और सूचनाएं"
                        "en" -> "Dark theme, Pooja reminders, language & alerts"
                        else -> "ડાર્ક થીમ, પૂજા સ્મરણ, ભાષા અને સૂચનાઓ"
                    },
                    icon = Icons.Default.Settings,
                    onClick = onNavigateToSettings
                )
            }

            item {
                MoreNavigationCard(
                    title = AppStrings.privacyPolicyTitle(lang),
                    subtitle = when (lang) {
                        "hi" -> "100% स्थानीय स्टोरेज और डेटा सुरक्षा नीति"
                        "en" -> "100% local storage & data protection policy"
                        else -> "૧૦૦% સ્થાનિક સ્ટોરેજ & ડેટા સુરક્ષા નીતિ"
                    },
                    icon = Icons.Default.Security,
                    onClick = onNavigateToPrivacyPolicy
                )
            }

            item {
                MoreNavigationCard(
                    title = AppStrings.termsTitle(lang),
                    subtitle = when (lang) {
                        "hi" -> "एप्लिकेशन उपयोग के नियम और डिस्क्लेमर"
                        "en" -> "Application terms and legal disclaimer"
                        else -> "એપ્લિકેશન ઉપયોગના નિયમો અને ડિસ્ક્લેમર"
                    },
                    icon = Icons.Default.Gavel,
                    onClick = onNavigateToTerms
                )
            }
        }

        // Bottom Adaptive Banner Ad
        BannerAdView(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun MoreNavigationCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .shadow(2.dp, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = SaffronPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
}

private fun resolveWallpaperImageRes(wall: WallpaperItem): Int {
    return wall.imageResId ?: when {
        wall.deityName.contains("ગણેશ") -> R.drawable.img_deity_ganesh_1793958512473
        wall.deityName.contains("અંબા") -> R.drawable.img_deity_ambaji_1793958532560
        wall.deityName.contains("રામ") -> R.drawable.img_deity_ram_1793958549091
        wall.deityName.contains("શિવ") -> R.drawable.img_darshan_shiva_1793948288707
        wall.deityName.contains("હનુમાન") -> R.drawable.img_darshan_hanuman_1793948310273
        wall.deityName.contains("સ્વામિનારાયણ") -> R.drawable.img_deity_swaminarayan_1793958571996
        else -> R.drawable.img_darshan_krishna_1793948267799
    }
}

@Composable
fun WallpaperStudioScreen(
    wallpapers: List<WallpaperItem>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val lang = LocalAppLanguage.current

    val unlockedWallpapers = remember(wallpapers) {
        mutableStateMapOf<String, Boolean>().apply {
            wallpapers.forEach { put(it.id, AdMobService.isWallpaperUnlocked(context, it.id)) }
        }
    }
    var pendingRewardWallpaper by remember { mutableStateOf<WallpaperItem?>(null) }
    var previewWallpaper by remember { mutableStateOf<WallpaperItem?>(null) }

    pendingRewardWallpaper?.let { targetWall ->
        RewardedActionConfirmDialog(
            title = when (lang) {
                "hi" -> "✨ एचडी दिव्य वॉलपेपर अनलॉक करें"
                "en" -> "✨ Unlock HD Divine Wallpaper"
                else -> "✨ એચડી દિવ્ય વૉલપેપર અનલૉક કરો"
            },
            message = when (lang) {
                "hi" -> "एक छोटा वीडियो देखकर '${targetWall.deityName}' का फुल-स्क्रीन एचडी दर्शन वॉलपेपर हमेशा के लिए अनलॉक करें।"
                "en" -> "Watch a short sponsored video to permanently unlock the Full-Screen HD Darshan preview for '${targetWall.deityName}'."
                else -> "એક ટૂંકો પ્રાયોજિત વિડિઓ જોઈને '${targetWall.deityName}' નો ફુલ-સ્ક્રીન એચડી દર્શન પ્રિવ્યૂ કાયમ માટે અનલૉક કરો."
            },
            confirmLabel = when (lang) {
                "hi" -> "वीडियो देखें और अनलॉक करें"
                "en" -> "Watch Ad & Unlock"
                else -> "વીડિયો જુઓ અને અનલૉક કરો"
            },
            freeNote = when (lang) {
                "hi" -> "सभी वॉलपेपर देखना और सुविचार साझा करना हमेशा 100% निःशुल्क है।"
                "en" -> "Browsing wallpapers and sharing Suvichar quotes remain 100% free."
                else -> "બધા વૉલપેપર જોવા અને સુવિચાર શેર કરવા હંમેશા ૧૦૦% મફત છે."
            },
            onConfirmWatchAd = {
                pendingRewardWallpaper = null
                AdMobManager.showWallpaperRewardedAd(
                    activity = context as? Activity,
                    wallpaperName = targetWall.deityName,
                    lang = lang,
                    onUnlocked = {
                        AdMobService.unlockWallpaper(context, targetWall.id)
                        unlockedWallpapers[targetWall.id] = true
                        previewWallpaper = targetWall
                        Toast.makeText(
                            context,
                            if (lang == "hi") "🎉 '${targetWall.deityName}' एचडी वॉलपेपर अनलॉक हो गया!" else if (lang == "en") "🎉 '${targetWall.deityName}' HD Wallpaper unlocked!" else "🎉 '${targetWall.deityName}' એચડી વૉલપેપર અનલૉક થઈ ગયું!",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    onDismissedWithoutReward = { reason ->
                        Toast.makeText(context, reason, Toast.LENGTH_SHORT).show()
                    }
                )
            },
            onDismiss = { pendingRewardWallpaper = null }
        )
    }

    previewWallpaper?.let { wall ->
        Dialog(
            onDismissRequest = { previewWallpaper = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                Image(
                    painter = painterResource(id = resolveWallpaperImageRes(wall)),
                    contentDescription = wall.deityName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0x88000000),
                                    Color(0x22000000),
                                    Color(0xEE0F0A06)
                                )
                            )
                        )
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF047857).copy(alpha = 0.9f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (lang == "hi") "✓ एचडी दर्शन अनलॉक" else if (lang == "en") "✓ HD Darshan Unlocked" else "✓ એચડી દર્શન અનલૉક",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(
                        onClick = { previewWallpaper = null },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.55f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = AppStrings.close(lang),
                            tint = Color.White
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DiyaIcon(modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "“${wall.suvicharGujarati}”",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFFBEB),
                        textAlign = TextAlign.Center,
                        lineHeight = 30.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = wall.deityName,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = SaffronPrimary
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Button(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "“${wall.suvicharGujarati}”\n- ${wall.deityName}\n\n- ભક્તિ સાગર ગુજરાતી"
                                )
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, AppStrings.share(lang)))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, tint = Color(0xFF1F1206), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(AppStrings.shareOnWhatsApp(lang), color = Color(0xFF1F1206), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("wallpaper_studio_screen")
    ) {
        BhaktiSubScreenTopBar(
            title = AppStrings.wallpaperStudioTitle(lang),
            subtitle = AppStrings.wallpaperStudioDesc(lang),
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(wallpapers) { index, wall ->
                if (index > 0 && index % 4 == 0) {
                    InlineNativeAd(modifier = Modifier.padding(vertical = 4.dp))
                }
                val isUnlocked = unlockedWallpapers[wall.id] == true
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                    ) {
                        Image(
                            painter = painterResource(id = resolveWallpaperImageRes(wall)),
                            contentDescription = wall.deityName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Divine Dark Vignette Scrim for maximum text contrast
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Color(0x66000000),
                                            Color(0x88000000),
                                            Color(0xF20F0A06)
                                        )
                                    )
                                )
                                .padding(20.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                DiyaIcon(modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "“${wall.suvicharGujarati}”",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFFBEB),
                                    textAlign = TextAlign.Center,
                                    lineHeight = 26.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(SaffronPrimary.copy(alpha = 0.9f))
                                        .padding(horizontal = 10.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = wall.deityName,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1F1206)
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(Color.White.copy(alpha = 0.25f))
                                            .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                                            .clickable {
                                                val sendIntent = Intent().apply {
                                                    action = Intent.ACTION_SEND
                                                    putExtra(
                                                        Intent.EXTRA_TEXT,
                                                        "“${wall.suvicharGujarati}”\n- ${wall.deityName}\n\n- ભક્તિ સાગર ગુજરાતી"
                                                    )
                                                    type = "text/plain"
                                                }
                                                context.startActivity(Intent.createChooser(sendIntent, AppStrings.share(lang)))
                                            }
                                            .padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(AppStrings.shareOnWhatsApp(lang), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }

                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(if (isUnlocked) Color(0xFFD1FAE5) else SaffronPrimary.copy(alpha = 0.95f))
                                            .clickable {
                                                if (isUnlocked) {
                                                    previewWallpaper = wall
                                                } else {
                                                    pendingRewardWallpaper = wall
                                                }
                                            }
                                            .padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = if (isUnlocked) Icons.Default.CheckCircle else Icons.Default.Stars,
                                            contentDescription = null,
                                            tint = if (isUnlocked) Color(0xFF047857) else Color(0xFF1F1206),
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isUnlocked) {
                                                if (lang == "hi") "✓ एचडी दर्शन" else if (lang == "en") "✓ HD View" else "✓ એચડી દર્શન"
                                            } else {
                                                if (lang == "hi") "✨ एचडी अनलॉक" else if (lang == "en") "✨ HD Unlock" else "✨ એચડી અનલૉક"
                                            },
                                            color = if (isUnlocked) Color(0xFF047857) else Color(0xFF1F1206),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom Adaptive Banner Ad
        BannerAdView(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun FavoritesScreen(
    favorites: List<FavoriteEntity>,
    bookmarks: List<BookmarkEntity>,
    onPlayFavoriteAudio: (String) -> Unit,
    onDeleteFavorite: (String) -> Unit,
    onDeleteBookmark: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val lang = LocalAppLanguage.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("favorites_screen")
    ) {
        BhaktiSubScreenTopBar(
            title = AppStrings.favoritesTitle(lang),
            subtitle = AppStrings.favoritesDesc(lang),
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                val favHeader = when (lang) {
                    "hi" -> "पसंदीदा भजन एवं आरती (${favorites.size})"
                    "en" -> "Favorite Bhajans & Aartis (${favorites.size})"
                    else -> "મનપસંદ ભજન & આરતી (${favorites.size})"
                }
                Text(
                    text = favHeader,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SaffronPrimary
                )
            }

            if (favorites.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFFBBF24), modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(AppStrings.noFavorites(lang), fontWeight = FontWeight.Bold)
                            Text(AppStrings.noFavoritesSub(lang), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            } else {
                items(favorites) { fav ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPlayFavoriteAudio(fav.id) },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(fav.titleGujarati, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                Text("${fav.subtitle} • ${fav.category}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            IconButton(onClick = { onDeleteFavorite(fav.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = AppStrings.delete(lang), tint = Color.Gray)
                            }
                        }
                    }
                }
            }

            // Inline Native Ad in saved content feed
            if (favorites.isNotEmpty() || bookmarks.isNotEmpty()) {
                item {
                    InlineNativeAd(modifier = Modifier.padding(vertical = 4.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(14.dp))
                val bmHeader = when (lang) {
                    "hi" -> "सहेजे गए गीता श्लोक एवं बुकमार्क (${bookmarks.size})"
                    "en" -> "Saved Gita Shlokas & Bookmarks (${bookmarks.size})"
                    else -> "સાચવેલા ગીતા શ્લોક & બુકમાર્ક્સ (${bookmarks.size})"
                }
                Text(
                    text = bmHeader,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SaffronPrimary
                )
            }

            if (bookmarks.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Bookmark, contentDescription = null, tint = Color(0xFFFBBF24), modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(AppStrings.noBookmarks(lang), fontWeight = FontWeight.Bold)
                            Text(AppStrings.noBookmarksSub(lang), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            } else {
                items(bookmarks) { bm ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Bookmark, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(bm.titleGujarati, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                Text(bm.snippetGujarati, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
                            }
                            IconButton(onClick = { onDeleteBookmark(bm.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = AppStrings.delete(lang), tint = Color.Gray)
                            }
                        }
                    }
                }
            }
        }

        // Bottom Adaptive Banner Ad
        BannerAdView(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun SearchScreen(
    allTracks: List<AudioTrack>,
    allTemples: List<TempleInfo>,
    allChapters: List<GitaChapter>,
    onSelectTrack: (AudioTrack) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val lang = LocalAppLanguage.current
    var searchQuery by remember { mutableStateOf("") }

    val matchedTracks = remember(searchQuery) {
        if (searchQuery.isBlank()) emptyList()
        else allTracks.filter { it.titleGujarati.contains(searchQuery, ignoreCase = true) || it.deityNameGujarati.contains(searchQuery, ignoreCase = true) }
    }

    val matchedTemples = remember(searchQuery) {
        if (searchQuery.isBlank()) emptyList()
        else allTemples.filter { it.nameGujarati.contains(searchQuery, ignoreCase = true) || it.location.contains(searchQuery, ignoreCase = true) }
    }

    val matchedChapters = remember(searchQuery) {
        if (searchQuery.isBlank()) emptyList()
        else allChapters.filter { it.titleGujarati.contains(searchQuery, ignoreCase = true) || it.titleSanskrit.contains(searchQuery, ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("search_screen")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.size(44.dp)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = AppStrings.back(lang), tint = SaffronPrimary)
            }
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(AppStrings.searchPlaceholder(lang)) },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SaffronPrimary,
                    unfocusedBorderColor = Color(0xFFFFE082)
                )
            )
        }

        HorizontalDivider()

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (searchQuery.isBlank()) {
                item {
                    val popSearch = when (lang) {
                        "hi" -> "लोकप्रिय खोज:"
                        "en" -> "Popular Searches:"
                        else -> "લોકપ્રિય શોધ:"
                    }
                    Text(
                        text = popSearch,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SaffronPrimary
                    )
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("દ્વારકાધીશ", "ગણેશ આરતી", "હનુમાન ચાલીસા", "શિવ તાંડવ").forEach { q ->
                            Card(
                                modifier = Modifier.clickable { searchQuery = q },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(q, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), style = MaterialTheme.typography.bodySmall, color = SaffronPrimary)
                            }
                        }
                    }
                }
            } else {
                if (matchedTracks.isNotEmpty()) {
                    item {
                        val header = when (lang) {
                            "hi" -> "आरती एवं भजन (${matchedTracks.size})"
                            "en" -> "Aarti & Bhajan (${matchedTracks.size})"
                            else -> "આરતી & ભજન (${matchedTracks.size})"
                        }
                        Text(header, fontWeight = FontWeight.Bold, color = SaffronPrimary)
                    }
                    items(matchedTracks) { track ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectTrack(track) },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Text(track.titleGujarati, modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Medium)
                        }
                    }

                    if (matchedTemples.isNotEmpty() || matchedChapters.isNotEmpty()) {
                        item {
                            InlineNativeAd(modifier = Modifier.padding(vertical = 4.dp))
                        }
                    }
                }

                if (matchedTemples.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        val header = when (lang) {
                            "hi" -> "तीर्थ मंदिर (${matchedTemples.size})"
                            "en" -> "Sacred Temples (${matchedTemples.size})"
                            else -> "તીર્થ મંદિર (${matchedTemples.size})"
                        }
                        Text(header, fontWeight = FontWeight.Bold, color = SaffronPrimary)
                    }
                    items(matchedTemples) { temple ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(temple.nameGujarati, fontWeight = FontWeight.Bold)
                                Text(temple.location, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }

                if (matchedChapters.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        val header = when (lang) {
                            "hi" -> "भगवद् गीता अध्याय (${matchedChapters.size})"
                            "en" -> "Bhagavad Gita Chapters (${matchedChapters.size})"
                            else -> "ભગવદ્ ગીતા અધ્યાય (${matchedChapters.size})"
                        }
                        Text(header, fontWeight = FontWeight.Bold, color = SaffronPrimary)
                    }
                    items(matchedChapters) { ch ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            val chLabel = when (lang) {
                                "hi" -> "अध्याय"
                                "en" -> "Chapter"
                                else -> "અધ્યાય"
                            }
                            Text("$chLabel ${ch.number}: ${ch.titleGujarati}", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Medium)
                        }
                    }
                }

                if (matchedTracks.isEmpty() && matchedTemples.isEmpty() && matchedChapters.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(30.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            val noRes = when (lang) {
                                "hi" -> "कोई परिणाम नहीं मिला।"
                                "en" -> "No results found."
                                else -> "કોઈ પરિણામ મળ્યા નથી."
                            }
                            Text(noRes, color = Color.Gray)
                        }
                    }
                }
            }
        }

        // AdMob Policy compliance: Do not display ads on empty search result states
        val hasSearchResults = matchedTracks.isNotEmpty() || matchedTemples.isNotEmpty() || matchedChapters.isNotEmpty()
        if (searchQuery.isBlank() || hasSearchResults) {
            BannerAdView(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun SettingsScreen(
    currentLanguage: String,
    isDarkTheme: Boolean,
    selectedCity: String,
    morningReminder: Boolean,
    eveningReminder: Boolean,
    onLanguageChange: (String) -> Unit,
    onThemeChange: (Boolean) -> Unit,
    onCityChange: (String) -> Unit = {},
    onMorningReminderChange: (Boolean) -> Unit,
    onEveningReminderChange: (Boolean) -> Unit,
    onResetMala: () -> Unit = {},
    onClearFavorites: () -> Unit = {},
    onNavigateToPrivacyPolicy: () -> Unit = {},
    onNavigateToTerms: () -> Unit = {},
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    val sendTestNotification: () -> Unit = {
        val sent = NotificationHelper.showMorningDarshanNotification(
            context,
            AppStrings.appTitle(currentLanguage),
            if (currentLanguage == "hi") "आज के मंगल दर्शन और आरती भक्ति सागर में उपलब्ध हैं।" else if (currentLanguage == "en") "Today's sacred Darshan and Aarti are ready in Bhakti Sagar." else "આજના મંગલ દર્શન અને આરતી ભક્તિ સાગરમાં ઉપલબ્ધ છે."
        )
        if (sent) {
            Toast.makeText(context, AppStrings.testReminderSent(currentLanguage), Toast.LENGTH_SHORT).show()
        }
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            sendTestNotification()
        } else {
            val deniedMsg = when (currentLanguage) {
                "hi" -> "सूचना अनुमति अस्वीकृत की गई। कृपया सेटिंग्स में अनुमति दें।"
                "en" -> "Notification permission denied. Please enable notifications in system settings."
                else -> "સૂચના પરવાનગી નકારવામાં આવી છે. કૃપા કરીને સેટિંગ્સમાંથી ચાલુ કરો."
            }
            Toast.makeText(context, deniedMsg, Toast.LENGTH_SHORT).show()
        }
    }

    var showResetMalaDialog by remember { mutableStateOf(false) }
    var showClearFavDialog by remember { mutableStateOf(false) }

    if (showResetMalaDialog) {
        AlertDialog(
            onDismissRequest = { showResetMalaDialog = false },
            title = { Text(AppStrings.resetMalaTitle(currentLanguage), fontWeight = FontWeight.Bold) },
            text = { Text(AppStrings.resetMalaConfirm(currentLanguage)) },
            confirmButton = {
                Button(
                    onClick = {
                        onResetMala()
                        showResetMalaDialog = false
                        Toast.makeText(context, "જાપ માળા ઇતિહાસ શૂન્ય થયો છે.", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text(AppStrings.confirm(currentLanguage))
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetMalaDialog = false }) {
                    Text(AppStrings.cancel(currentLanguage))
                }
            }
        )
    }

    if (showClearFavDialog) {
        AlertDialog(
            onDismissRequest = { showClearFavDialog = false },
            title = { Text(AppStrings.clearFavoritesTitle(currentLanguage), fontWeight = FontWeight.Bold) },
            text = { Text(AppStrings.clearFavoritesConfirm(currentLanguage)) },
            confirmButton = {
                Button(
                    onClick = {
                        onClearFavorites()
                        showClearFavDialog = false
                        Toast.makeText(context, "મનપસંદ યાદી સાફ થઈ ગઈ છે.", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text(AppStrings.confirm(currentLanguage))
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearFavDialog = false }) {
                    Text(AppStrings.cancel(currentLanguage))
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("settings_screen")
    ) {
        BhaktiSubScreenTopBar(
            title = AppStrings.settingsTitle(currentLanguage),
            subtitle = AppStrings.appTitle(currentLanguage),
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Language Selection (Functional & Live)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Language, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(AppStrings.languageLabel(currentLanguage), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        val languages = listOf("gu" to "ગુજરાતી (Default)", "hi" to "हिन्दी (Hindi)", "en" to "English")
                        languages.forEach { (code, label) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onLanguageChange(code)
                                        Toast.makeText(context, "ભાષા પસંદ થઈ: $label", Toast.LENGTH_SHORT).show()
                                    }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = currentLanguage == code,
                                    onClick = {
                                        onLanguageChange(code)
                                        Toast.makeText(context, "ભાષા પસંદ થઈ: $label", Toast.LENGTH_SHORT).show()
                                    }
                                )
                                Text(label, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }

            // 2. Interactive Theme Selection (White Theme & Dark Theme)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isDarkTheme) Icons.Default.Nightlight else Icons.Default.WbSunny,
                                    contentDescription = null,
                                    tint = SaffronPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "એપ્લિકેશન થીમ (Theme Selection)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Switch(
                                checked = isDarkTheme,
                                onCheckedChange = onThemeChange,
                                colors = SwitchDefaults.colors(checkedThumbColor = SaffronPrimary)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // White / Light Mode Card
                            val isWhiteSelected = !isDarkTheme
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onThemeChange(false) },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isWhiteSelected) Color(0xFFFFFBEB) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    width = if (isWhiteSelected) 2.dp else 1.dp,
                                    color = if (isWhiteSelected) SaffronPrimary else MaterialTheme.colorScheme.outline
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.WbSunny,
                                        contentDescription = "White Theme",
                                        tint = if (isWhiteSelected) SaffronDark else Color.Gray,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "સફેદ થીમ",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = if (isWhiteSelected) SaffronDark else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "White / Light",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    if (isWhiteSelected) {
                                        Text(
                                            text = "✓ સક્રિય (Active)",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF047857)
                                        )
                                    }
                                }
                            }

                            // Dark / Night Mode Card
                            val isDarkSelected = isDarkTheme
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onThemeChange(true) },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isDarkSelected) Color(0xFF292524) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    width = if (isDarkSelected) 2.dp else 1.dp,
                                    color = if (isDarkSelected) SaffronPrimary else MaterialTheme.colorScheme.outline
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Nightlight,
                                        contentDescription = "Dark Theme",
                                        tint = if (isDarkSelected) SaffronPrimary else Color.Gray,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "ડાર્ક થીમ",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = if (isDarkSelected) SaffronPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Dark / Night",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    if (isDarkSelected) {
                                        Text(
                                            text = "✓ સક્રિય (Active)",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF10B981)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 3. Panchang City Selection
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(AppStrings.panchangCityTitle(currentLanguage), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        val cities = listOf("અમદાવાદ", "સુરત", "રાજકોટ", "વડોદરા", "ભાવનગર", "જામનગર", "જૂનાગઢ", "ગાંધીનગર")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            cities.take(4).forEach { city ->
                                val isSelected = (selectedCity == city)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) SaffronPrimary else MaterialTheme.colorScheme.surfaceVariant)
                                        .clickable {
                                            onCityChange(city)
                                            Toast.makeText(context, "$city શહેર પસંદ થયું", Toast.LENGTH_SHORT).show()
                                        }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = city,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color(0xFF1F1206) else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            cities.drop(4).forEach { city ->
                                val isSelected = (selectedCity == city)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) SaffronPrimary else MaterialTheme.colorScheme.surfaceVariant)
                                        .clickable {
                                            onCityChange(city)
                                            Toast.makeText(context, "$city શહેર પસંદ થયું", Toast.LENGTH_SHORT).show()
                                        }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = city,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color(0xFF1F1206) else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. Prayer Reminders with REAL TEST NOTIFICATION BUTTON
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Notifications, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(AppStrings.remindersTitle(currentLanguage), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(AppStrings.morningReminder(currentLanguage), style = MaterialTheme.typography.bodyMedium)
                            Switch(checked = morningReminder, onCheckedChange = onMorningReminderChange)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(AppStrings.eveningReminder(currentLanguage), style = MaterialTheme.typography.bodyMedium)
                            Switch(checked = eveningReminder, onCheckedChange = onEveningReminderChange)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Test notification button
                        OutlinedButton(
                            onClick = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                    !NotificationHelper.hasNotificationPermission(context)
                                ) {
                                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                } else {
                                    sendTestNotification()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(16.dp), tint = SaffronPrimary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(AppStrings.testReminderButton(currentLanguage), color = SaffronPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // 5. Data Reset Actions (Mala & Favorites)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Refresh, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("ડેટા અને સાધના વ્યવસ્થાપન", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showResetMalaDialog = true }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(AppStrings.resetMalaTitle(currentLanguage), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                Text("કુલ ફેરા અને મણકા શૂન્ય કરો", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFDC2626))
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showClearFavDialog = true }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(AppStrings.clearFavoritesTitle(currentLanguage), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                Text("બધા સાચવેલા બુકમાર્ક અને મનપસંદ હટાવો", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFDC2626))
                        }
                    }
                }
            }

            // 6. Share & Feedback
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, AppStrings.shareAppText(currentLanguage))
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "શેર કરો"))
                                }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(AppStrings.shareAppTitle(currentLanguage), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    try {
                                        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                            data = Uri.parse("mailto:support@bhaktisagar.app")
                                            putExtra(Intent.EXTRA_SUBJECT, "Bhakti Sagar App Feedback")
                                        }
                                        context.startActivity(Intent.createChooser(emailIntent, "પ્રતિસાદ મોકલો"))
                                    } catch (_: Exception) {
                                        Toast.makeText(context, "પ્રતિસાદ માટે support@bhaktisagar.app પર ઈમેલ કરો", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Feedback, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(AppStrings.feedbackTitle(currentLanguage), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // 7. Legal & Play Store Compliance
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("કાનૂની અને નીતિ નિયમો (Legal & Compliance)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToPrivacyPolicy() }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("ગોપનીયતા નીતિ (Privacy Policy)", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                Text("૧૦૦% સ્થાનિક સ્ટોરેજ, કોઈ ડેટા ટ્રેકિંગ નહીં", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(18.dp))
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToTerms() }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("નિયમો અને શરતો (Terms of Use)", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                Text("ધાર્મિક સેવા, કોપીરાઇટ અને પંચાંગ ડિસ્ક્લેમર", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            // 8. About App Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = SaffronPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(AppStrings.aboutAppTitle(currentLanguage), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = SaffronPrimary)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = AppStrings.aboutAppDesc(currentLanguage),
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "સંસ્કરણ: ૧.૦ (Version 1.0)\nહરિ ૐ તત્ સત્",
                            style = MaterialTheme.typography.labelSmall,
                            color = SaffronPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Bottom Adaptive Banner Ad
        BannerAdView(modifier = Modifier.fillMaxWidth())
    }
}
