package com.example.ui.components

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.material3.CircularProgressIndicator
import com.example.ads.AdMobConfig
import com.example.ads.AdMobManager
import com.example.ui.strings.AppStrings
import com.example.ui.strings.LocalAppLanguage
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import com.google.android.libraries.ads.mobile.sdk.banner.AdSize
import com.google.android.libraries.ads.mobile.sdk.banner.AdView
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAd
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdRequest
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import com.google.android.libraries.ads.mobile.sdk.nativead.MediaView
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAd
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdView

/**
 * Reusable Adaptive Banner Ad container for Jetpack Compose.
 * Resizes dynamically according to screen width and anchors cleanly without layout jumps.
 */
@Composable
fun BannerAdView(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isAdLoaded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 50.dp, max = 68.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
            .testTag("banner_ad_container"),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { ctx ->
                val adView = AdView(ctx)
                try {
                    val dm = ctx.resources.displayMetrics
                    val widthDp = (dm.widthPixels / dm.density).toInt()
                    val adSize = try {
                        AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(ctx, widthDp)
                    } catch (_: Exception) {
                        AdSize(320, 50)
                    }
                    adView.resize(adSize)
                    val request = BannerAdRequest.Builder(AdMobConfig.getBannerAdUnitId(), adSize).build()
                    adView.loadAd(
                        request,
                        object : AdLoadCallback<BannerAd> {
                            override fun onAdLoaded(ad: BannerAd) {
                                isAdLoaded = true
                                Log.d("BannerAdView", "Next-Gen BannerAd successfully displayed")
                            }

                            override fun onAdFailedToLoad(error: LoadAdError) {
                                Log.w("BannerAdView", "Next-Gen BannerAd load failed: ${error.message}")
                            }
                        }
                    )
                } catch (e: Exception) {
                    Log.e("BannerAdView", "Error initializing BannerAdView", e)
                }
                adView
            },
            onRelease = { adView ->
                try {
                    adView.destroy()
                } catch (_: Exception) {}
            }
        )
    }
}

/**
 * AdaptiveBannerAd: Alias for BannerAdView for clear naming across screens.
 */
@Composable
fun AdaptiveBannerAd(modifier: Modifier = Modifier) {
    BannerAdView(modifier = modifier)
}

/**
 * BottomBannerAd: Bottom-anchored banner respecting navigation insets to prevent UI overlap.
 */
@Composable
fun BottomBannerAd(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        BannerAdView(modifier = Modifier.fillMaxWidth())
    }
}

/**
 * Dedicated Next-Gen Native Ad Card for content feeds (Home, Bhajans, Gita chapters, Temples).
 * Complies with Google AdMob policy with clear "Ad • પ્રાયોજિત" badge, headline, body, and CTA.
 */
@Composable
fun NativeAdCard(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

    LaunchedEffect(Unit) {
        AdMobManager.loadNativeAd(context) { ad ->
            nativeAd = ad
        }
    }

    if (nativeAd == null) {
        return
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .testTag("native_ad_card"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { ctx ->
                createNativeAdView(ctx, nativeAd!!, isCompact = false)
            },
            onRelease = { view ->
                try {
                    view.destroy()
                } catch (_: Exception) {}
            }
        )
    }
}

/**
 * InlineNativeAd: Compact inline native ad row designed to sit cleanly inside LazyColumn content lists.
 */
@Composable
fun InlineNativeAd(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

    LaunchedEffect(Unit) {
        AdMobManager.loadNativeAd(context) { ad ->
            nativeAd = ad
        }
    }

    if (nativeAd == null) {
        return
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .testTag("inline_native_ad"),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { ctx ->
                createNativeAdView(ctx, nativeAd!!, isCompact = true)
            },
            onRelease = { view ->
                try {
                    view.destroy()
                } catch (_: Exception) {}
            }
        )
    }
}

private fun createNativeAdView(context: Context, ad: NativeAd, isCompact: Boolean = false): NativeAdView {
    val nativeAdView = NativeAdView(context)
    val rootLayout = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(20, 16, 20, 16)
        layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    // Row 1: Ad Badge & Advertiser/Store
    val headerRow = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    val adBadge = TextView(context).apply {
        text = "Ad • પ્રાયોજિત"
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
        setTypeface(null, Typeface.BOLD)
        setTextColor(android.graphics.Color.parseColor("#9A3412"))
        setBackgroundColor(android.graphics.Color.parseColor("#FFEDD5"))
        setPadding(10, 3, 10, 3)
    }
    headerRow.addView(adBadge)

    val advertiserText = TextView(context).apply {
        text = ad.advertiser ?: ""
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f)
        setTextColor(android.graphics.Color.GRAY)
        setPadding(14, 0, 0, 0)
        visibility = if (ad.advertiser.isNullOrEmpty()) View.GONE else View.VISIBLE
    }
    headerRow.addView(advertiserText)
    rootLayout.addView(headerRow)

    // Row 2: Headline
    val headlineText = TextView(context).apply {
        text = ad.headline ?: ""
        setTextSize(TypedValue.COMPLEX_UNIT_SP, if (isCompact) 13f else 14f)
        setTypeface(null, Typeface.BOLD)
        setTextColor(android.graphics.Color.parseColor("#1F1206"))
        setPadding(0, 8, 0, 2)
    }
    rootLayout.addView(headlineText)

    // Row 3: Body (omitted or 1 line in compact mode)
    val bodyText = TextView(context).apply {
        text = ad.body ?: ""
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f)
        setTextColor(android.graphics.Color.DKGRAY)
        maxLines = if (isCompact) 1 else 2
        setPadding(0, 0, 0, 8)
        visibility = if (ad.body.isNullOrEmpty()) View.GONE else View.VISIBLE
    }
    rootLayout.addView(bodyText)

    // MediaView for Next-Gen NativeAdView
    val mediaView = MediaView(context).apply {
        if (isCompact) {
            visibility = View.GONE
            layoutParams = LinearLayout.LayoutParams(0, 0)
        } else {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                160
            ).apply {
                bottomMargin = 10
            }
        }
    }
    rootLayout.addView(mediaView)

    // Call To Action Button
    val ctaButton = Button(context).apply {
        text = ad.callToAction ?: "વિઝિટ કરો"
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        setTypeface(null, Typeface.BOLD)
        setTextColor(android.graphics.Color.WHITE)
        setBackgroundColor(android.graphics.Color.parseColor("#EA580C"))
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
    rootLayout.addView(ctaButton)

    // Register views with Next-Gen NativeAdView
    nativeAdView.headlineView = headlineText
    nativeAdView.bodyView = bodyText
    nativeAdView.callToActionView = ctaButton
    nativeAdView.advertiserView = advertiserText

    nativeAdView.addView(rootLayout)
    nativeAdView.registerNativeAd(ad, mediaView)

    return nativeAdView
}

/**
 * Optional Bonus Audio Content Unlock Dialog.
 * Clearly explains that the track is a special bonus composition that can be permanently
 * unlocked by voluntarily watching a short video. Standard audio remains 100% free.
 */
@Composable
fun BonusAudioUnlockDialog(
    trackTitle: String,
    onConfirmWatchAd: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val lang = LocalAppLanguage.current
    var isWaitingForAd by remember { mutableStateOf(false) }
    var adErrorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        if (!AdMobManager.isRewardedAdReady()) {
            AdMobManager.loadRewardedAd(
                context = context,
                onLoaded = {
                    adErrorMessage = null
                },
                onFailed = { err ->
                    Log.w("BonusAudioUnlock", "Rewarded Ad preload failed: $err")
                }
            )
        }
    }

    val handleWatchClick: () -> Unit = {
        adErrorMessage = null
        if (AdMobManager.isRewardedAdReady()) {
            onConfirmWatchAd()
        } else {
            isWaitingForAd = true
            AdMobManager.loadRewardedAd(
                context = context,
                onLoaded = {
                    isWaitingForAd = false
                    onConfirmWatchAd()
                },
                onFailed = { err ->
                    isWaitingForAd = false
                    adErrorMessage = AppStrings.bonusAudioAdFailed(err, lang)
                }
            )
        }
    }

    AlertDialog(
        onDismissRequest = {
            if (!isWaitingForAd) onDismiss()
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Stars,
                contentDescription = null,
                tint = SaffronPrimary,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                text = AppStrings.bonusAudioTitle(lang),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SaffronDark
            )
        },
        text = {
            Column {
                Text(
                    text = AppStrings.bonusAudioMessage(trackTitle, lang),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFEF3C7))
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Stars,
                        contentDescription = null,
                        tint = SaffronPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.bonusAudioFreeNote(lang),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF92400E),
                        fontWeight = FontWeight.SemiBold
                    )
                }

                adErrorMessage?.let { err ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = err,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = handleWatchClick,
                enabled = !isWaitingForAd,
                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
            ) {
                if (isWaitingForAd) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color(0xFF1F1206),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.bonusAudioLoadingAd(lang),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F1206)
                    )
                } else {
                    Text(
                        text = AppStrings.bonusAudioWatchAndUnlock(lang),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isWaitingForAd
            ) {
                Text(
                    text = AppStrings.cancel(lang),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}

/**
 * Reusable voluntary Rewarded Ad confirmation dialog for any optional in-app benefit
 * (e.g., +108 Divine Beads Bonus, HD Wallpaper Unlock, etc.).
 * Handles preloading, active loading spinner when waiting, and clear localized error feedback.
 */
@Composable
fun RewardedActionConfirmDialog(
    title: String,
    message: String,
    confirmLabel: String,
    freeNote: String? = null,
    onConfirmWatchAd: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val lang = LocalAppLanguage.current
    var isWaitingForAd by remember { mutableStateOf(false) }
    var adErrorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        if (!AdMobManager.isRewardedAdReady()) {
            AdMobManager.loadRewardedAd(
                context = context,
                onLoaded = {
                    adErrorMessage = null
                },
                onFailed = { err ->
                    Log.w("RewardedActionDialog", "Rewarded Ad preload failed: $err")
                }
            )
        }
    }

    val handleWatchClick: () -> Unit = {
        adErrorMessage = null
        if (AdMobManager.isRewardedAdReady()) {
            onConfirmWatchAd()
        } else {
            isWaitingForAd = true
            AdMobManager.loadRewardedAd(
                context = context,
                onLoaded = {
                    isWaitingForAd = false
                    onConfirmWatchAd()
                },
                onFailed = { err ->
                    isWaitingForAd = false
                    adErrorMessage = AppStrings.bonusAudioAdFailed(err, lang)
                }
            )
        }
    }

    AlertDialog(
        onDismissRequest = {
            if (!isWaitingForAd) onDismiss()
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Stars,
                contentDescription = null,
                tint = SaffronPrimary,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SaffronDark
            )
        },
        text = {
            Column {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (!freeNote.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFEF3C7))
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = null,
                            tint = SaffronPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = freeNote,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF92400E),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                adErrorMessage?.let { err ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = err,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = handleWatchClick,
                enabled = !isWaitingForAd,
                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
            ) {
                if (isWaitingForAd) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color(0xFF1F1206),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.bonusAudioLoadingAd(lang),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F1206)
                    )
                } else {
                    Text(
                        text = confirmLabel,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F1206)
                    )
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isWaitingForAd
            ) {
                Text(
                    text = AppStrings.cancel(lang),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}
