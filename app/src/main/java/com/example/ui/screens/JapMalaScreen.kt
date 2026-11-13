package com.example.ui.screens

import android.app.Activity
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.filled.Lock
import com.example.ads.AdMobManager
import com.example.ui.components.BannerAdView
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
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
import com.example.ui.components.AnimatedScaleButton
import com.example.ui.components.BhaktiSubScreenTopBar
import com.example.ui.components.DiyaIcon
import com.example.ui.components.RewardedActionConfirmDialog
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

data class MalaMantraItem(
    val name: String,
    val deity: String,
    val imageResId: Int,
    val rawAudioResId: Int,
    val audioBadge: String = "🎙️ માત્ર માનવ કંઠ (No Music)"
)

@Composable
fun JapMalaScreen(
    lifetimeRounds: Int,
    lifetimeBeads: Int,
    onRecordMalaSession: (mantra: String, beads: Int, rounds: Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val lang = com.example.ui.strings.LocalAppLanguage.current

    val mantraList = remember {
        listOf(
            MalaMantraItem("શ્રી રામ જય રામ જય જય રામ", "શ્રી રામ", R.drawable.img_deity_ram_1793958549091, R.raw.mala_shree_ram),
            MalaMantraItem("રાધે કૃષ્ણ રાધે શ્યામ (મહામંત્ર)", "શ્રી કૃષ્ણ", R.drawable.img_darshan_krishna_1793948267799, R.raw.mala_hare_krishna),
            MalaMantraItem("ૐ નમઃ શિવાય (પંચાક્ષરી મંત્ર)", "ભગવાન શિવ", R.drawable.img_darshan_shiva_1793948288707, R.raw.mala_om_namah_shivaya),
            MalaMantraItem("જય શ્રી હનુમાન - સંકટમોચન", "હનુમાનજી", R.drawable.img_darshan_hanuman_1793948310273, R.raw.mala_hanuman),
            MalaMantraItem("ૐ ગં ગણપતયે નમઃ", "શ્રી ગણેશ", R.drawable.img_deity_ganesh_1793958512473, R.raw.mala_ganesh),
            MalaMantraItem("જય માતાજી - ૐ અંબે", "માતા અંબા", R.drawable.img_deity_ambaji_1793958532560, R.raw.mala_ambe_mata),
            MalaMantraItem("શ્રી સ્વામિનારાયણ હરે", "સ્વામિનારાયણ", R.drawable.img_deity_swaminarayan_1793958571996, R.raw.mala_swaminarayan),
            MalaMantraItem("ગાયત્રી મંત્ર (ૐ ભૂર્ભુવઃ સ્વઃ)", "માતા ગાયત્રી", R.drawable.img_deity_ambaji_1793958532560, R.raw.mala_gayatri),
            MalaMantraItem("મહામૃત્યુંજય મંત્ર (ૐ ત્ર્યંબકં)", "ભગવાન શિવ", R.drawable.img_darshan_shiva_1793948288707, R.raw.mala_mahamrityunjaya)
        )
    }

    var selectedMantraIndex by remember { mutableIntStateOf(0) }
    var currentBeadCount by remember { mutableIntStateOf(0) }
    var currentRoundsCount by remember { mutableIntStateOf(0) }
    var isSoundEnabled by remember { mutableStateOf(true) }
    var isHapticEnabled by remember { mutableStateOf(true) }
    var isChantingNow by remember { mutableStateOf(false) }

    var showMantraDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showCompletionDialog by remember { mutableStateOf(false) }
    var showMalaRewardDialog by remember { mutableStateOf(false) }

    var malaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    // Clean up MediaPlayer and active status on disposal
    DisposableEffect(Unit) {
        onDispose {
            try {
                malaPlayer?.stop()
                malaPlayer?.release()
            } catch (_: Exception) {}
            malaPlayer = null
            AdMobManager.setJapMalaActive(false)
        }
    }

    fun playMantraSound(rawResId: Int) {
        if (!isSoundEnabled) return
        try {
            malaPlayer?.stop()
            malaPlayer?.release()
        } catch (_: Exception) {}
        malaPlayer = null

        try {
            val player = MediaPlayer.create(context.applicationContext, rawResId)
            if (player != null) {
                player.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .build()
                )
                isChantingNow = true
                player.setOnCompletionListener {
                    isChantingNow = false
                    try { it.release() } catch (_: Exception) {}
                    if (malaPlayer == it) {
                        malaPlayer = null
                    }
                }
                player.setOnErrorListener { mp, _, _ ->
                    isChantingNow = false
                    try { mp.release() } catch (_: Exception) {}
                    if (malaPlayer == mp) {
                        malaPlayer = null
                    }
                    true
                }
                player.start()
                malaPlayer = player
            }
        } catch (e: Exception) {
            e.printStackTrace()
            isChantingNow = false
        }
    }

    val tapInteractionSource = remember { MutableInteractionSource() }
    val isTapPressed by tapInteractionSource.collectIsPressedAsState()
    val tapScale by animateFloatAsState(
        targetValue = if (isTapPressed) 0.91f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "MalaTapScale"
    )

    fun triggerHapticFeedback(isRoundComplete: Boolean = false) {
        if (!isHapticEnabled) return
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (isRoundComplete) {
                    vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 150, 80, 250), -1))
                } else {
                    vibrator?.vibrate(VibrationEffect.createOneShot(45, VibrationEffect.DEFAULT_AMPLITUDE))
                }
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(if (isRoundComplete) 300L else 40L)
            }
        } catch (_: Exception) {}
    }

    fun handleBeadTap() {
        AdMobManager.setJapMalaActive(true)
        val nextCount = currentBeadCount + 1
        val activeMantra = mantraList[selectedMantraIndex]

        // Play the chosen mantra's authentic audio chanting on touch
        playMantraSound(activeMantra.rawAudioResId)

        if (nextCount >= 108) {
            currentBeadCount = 0
            currentRoundsCount += 1
            triggerHapticFeedback(isRoundComplete = true)
            onRecordMalaSession(activeMantra.name, 108, 1)
            showCompletionDialog = true
        } else {
            currentBeadCount = nextCount
            triggerHapticFeedback(isRoundComplete = false)
        }
    }

    if (showMantraDialog) {
        AlertDialog(
            onDismissRequest = { showMantraDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    val dialogTitle = when (lang) {
                        "hi" -> "मंत्र एवं इष्टदेव चुनें"
                        "en" -> "Select Mantra & Deity"
                        else -> "મંત્ર અને ઈષ્ટદેવ પસંદ કરો"
                    }
                    Text(dialogTitle, fontWeight = FontWeight.Bold, color = SaffronDark)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    val dialogInstruction = when (lang) {
                        "hi" -> "जो मंत्र चुनेंगे, स्पर्श करने पर उसी मंत्र का पारंपरिक मानव कंठ में ऑडियो बजेगा:"
                        "en" -> "Tap any bead to chant with sacred authentic human vocals:"
                        else -> "જે મંત્ર પસંદ કરશો તેના પર ટચ કરતાં તે જ મંત્રનો માનવ કંઠમાં ઓડિયો વાગશે:"
                    }
                    Text(
                        text = dialogInstruction,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    mantraList.forEachIndexed { idx, m ->
                        val isSelected = selectedMantraIndex == idx
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent)
                                .clickable {
                                    selectedMantraIndex = idx
                                    showMantraDialog = false
                                    // Play immediate preview of the selected mantra
                                    playMantraSound(m.rawAudioResId)
                                }
                                .padding(vertical = 8.dp, horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, if (isSelected) SaffronPrimary else MaterialTheme.colorScheme.outlineVariant, CircleShape)
                                ) {
                                    Image(
                                        painter = painterResource(id = m.imageResId),
                                        contentDescription = m.deity,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = m.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) SaffronPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = m.deity,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = SaffronPrimary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        val vocalBadge = when (lang) {
                                            "hi" -> "• 🎙️ मानव स्वर (No Music)"
                                            "en" -> "• 🎙️ Sacred Vocals (No Music)"
                                            else -> "• 🎙️ માત્ર માનવ કંઠ (No Music)"
                                        }
                                        Text(
                                            text = vocalBadge,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (MaterialTheme.colorScheme.surface.luminance() < 0.5f) Color(0xFF34D399) else Color(0xFF059669),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                            if (isSelected) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = SaffronPrimary)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMantraDialog = false }) {
                    Text(com.example.ui.strings.AppStrings.close(lang))
                }
            }
        )
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(com.example.ui.strings.AppStrings.resetMalaTitle(lang), fontWeight = FontWeight.Bold) },
            text = { Text(com.example.ui.strings.AppStrings.resetMalaConfirm(lang)) },
            confirmButton = {
                Button(
                    onClick = {
                        if (currentBeadCount > 0) {
                            onRecordMalaSession(mantraList[selectedMantraIndex].name, currentBeadCount, 0)
                        }
                        currentBeadCount = 0
                        currentRoundsCount = 0
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text(com.example.ui.strings.AppStrings.confirm(lang))
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(com.example.ui.strings.AppStrings.cancel(lang))
                }
            }
        )
    }

    if (showCompletionDialog) {
        val completedMantra = mantraList[selectedMantraIndex]
        AlertDialog(
            onDismissRequest = { showCompletionDialog = false },
            title = {
                Text(
                    when (lang) {
                        "hi" -> "१ माला पूर्ण हुई! (१०८ जप)"
                        "en" -> "1 Mala Completed! (108 Chants)"
                        else -> "૧ માળા પૂર્ણ થઈ! (૧૦૮ જાપ)"
                    },
                    fontWeight = FontWeight.Bold,
                    color = SaffronDark
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .border(2.5.dp, SaffronPrimary, CircleShape)
                            .shadow(8.dp, CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = completedMantra.imageResId),
                            contentDescription = completedMantra.deity,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = when (lang) {
                            "hi" -> "धन्यवाद! आपने '${completedMantra.name}' (${completedMantra.deity}) मंत्र की एक पूर्ण १०८ मनकों की माला पूर्ण की है।"
                            "en" -> "Congratulations! You have completed a full 108 bead round of '${completedMantra.name}' (${completedMantra.deity})."
                            else -> "ધન્યવાદ! આપે '${completedMantra.name}' (${completedMantra.deity}) મંત્રની એક સંપૂર્ણ ૧૦૮ મણકાની માળા પૂર્ણ કરી છે."
                        },
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            confirmButton = {
                AnimatedScaleButton(
                    onClick = { showCompletionDialog = false },
                    backgroundColor = SaffronPrimary
                ) {
                    Text(
                        when (lang) {
                            "hi" -> "हरि ॐ - आगे बढ़ें"
                            "en" -> "Hari Om - Continue"
                            else -> "હરિ ૐ - આગળ વધો"
                        },
                        color = Color(0xFF1F1206),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showCompletionDialog = false
                        showMalaRewardDialog = true
                    }
                ) {
                    Text(
                        when (lang) {
                            "hi" -> "🎁 +१०८ डबल बोनस (वीडियो देखें)"
                            "en" -> "🎁 +108 Double Bonus (Watch Ad)"
                            else -> "🎁 +૧૦૮ ડબલ બોનસ (વિડિઓ જુઓ)"
                        },
                        color = SaffronDark,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )
    }

    if (showMalaRewardDialog) {
        val activeMantra = mantraList[selectedMantraIndex]
        RewardedActionConfirmDialog(
            title = when (lang) {
                "hi" -> "🎁 दिव्य १०८ मनके बोनस प्राप्त करें"
                "en" -> "🎁 Earn +108 Divine Beads Bonus"
                else -> "🎁 દિવ્ય ૧૦૮ મણકા બોનસ મેળવો"
            },
            message = when (lang) {
                "hi" -> "एक संक्षिप्त प्रायोजित वीडियो देखकर अपने कुल जाप में १०८ मनके (१ पूरी माला) का पुण्य आशीर्वाद तुरंत जोड़ें।"
                "en" -> "Watch a short sponsor video to instantly add a full 108 beads (1 complete mala) blessing to your total chanting record."
                else -> "એક ટૂંકો પ્રાયોજિત વિડિઓ જોઈને તમારા આજીવન જાપમાં ૧૦૮ મણકા (૧ સંપૂર્ણ માળા) નું પુણ્ય આશીર્વાદ તરત ઉમેરો."
            },
            confirmLabel = when (lang) {
                "hi" -> "वीडियो देखें (+१०८ मनके)"
                "en" -> "Watch Video (+108 Beads)"
                else -> "વીડિયો જુઓ (+૧૦૮ મણકા)"
            },
            freeNote = when (lang) {
                "hi" -> "सामान्य १०८ जप माला सदैव निःशुल्क एवं बिना किसी विज्ञापन के उपलब्ध है।"
                "en" -> "Standard 108-bead Jap Mala chanting remains 100% free and uninterrupted."
                else -> "સામાન્ય ૧૦૮ જાપ માળા હંમેશા ૧૦૦% મફત અને કોઈપણ અવરોધ વિના ઉપલબ્ધ છે."
            },
            onConfirmWatchAd = {
                showMalaRewardDialog = false
                AdMobManager.showMalaBonusRewardedAd(
                    activity = context as? Activity,
                    lang = lang,
                    onBonusGranted = {
                        onRecordMalaSession(activeMantra.name, 108, 1)
                        currentRoundsCount += 1
                        android.widget.Toast.makeText(
                            context,
                            if (lang == "hi") "🎉 १०८ मनके (१ माला) का दिव्य आशीर्वाद जुड़ गया!" else if (lang == "en") "🎉 +108 Divine Beads bonus added successfully!" else "🎉 ૧૦૮ મણકા (૧ માળા) નો દિવ્ય આશીર્વાદ સફળતાપૂર્વક ઉમેરાયો!",
                            android.widget.Toast.LENGTH_LONG
                        ).show()
                    },
                    onDismissedWithoutReward = { reason ->
                        android.widget.Toast.makeText(context, reason, android.widget.Toast.LENGTH_SHORT).show()
                    }
                )
            },
            onDismiss = { showMalaRewardDialog = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("jap_mala_screen")
    ) {
        val isDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f

        BhaktiSubScreenTopBar(
            title = com.example.ui.strings.AppStrings.japMalaTitle(lang),
            subtitle = com.example.ui.strings.AppStrings.japMalaDesc(lang),
            onBack = onBack,
            actions = {
                IconButton(onClick = { showResetDialog = true }) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset",
                            tint = SaffronPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Selected Mantra Card
            val activeMantra = mantraList[selectedMantraIndex]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showMantraDialog = true }
                    .shadow(4.dp, RoundedCornerShape(18.dp)),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(
                        listOf(SaffronPrimary.copy(alpha = 0.5f), Color(0xFFF59E0B).copy(alpha = 0.3f))
                    )
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .border(2.dp, SaffronPrimary, CircleShape)
                            .shadow(4.dp, CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = activeMantra.imageResId),
                            contentDescription = activeMantra.deity,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val chantLabel = when (lang) {
                                "hi" -> "जाप मंत्र (बदलने के लिए स्पर्श करें):"
                                "en" -> "Chant Mantra (Tap to change):"
                                else -> "જાપ મંત્ર (બદલવા ટચ કરો):"
                            }
                            Text(chantLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.width(4.dp))
                            if (isChantingNow) {
                                Icon(Icons.Default.GraphicEq, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(14.dp))
                            }
                        }
                        Text(
                            text = activeMantra.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SaffronPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val deityPrefix = when (lang) { "hi" -> "इष्टदेव"; "en" -> "Deity"; else -> "ઇષ્ટદેવ" }
                            Text(
                                text = "$deityPrefix: ${activeMantra.deity}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            val vocalBadge = when (lang) {
                                "hi" -> "• 🎙️ मानव स्वर"
                                "en" -> "• 🎙️ Sacred Vocals"
                                else -> "• 🎙️ માનવ કંઠ (No Music)"
                            }
                            Text(
                                text = vocalBadge,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color(0xFF34D399) else Color(0xFF047857),
                                fontSize = 10.sp
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        val changeDesc = when (lang) { "hi" -> "मंत्र बदलें"; "en" -> "Change Mantra"; else -> "મંત્ર બદલો" }
                        Icon(Icons.Default.AutoAwesome, contentDescription = changeDesc, tint = SaffronPrimary, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Audio & Haptic Toggle Control Strip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sound on Touch Toggle Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSoundEnabled) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface)
                        .border(
                            1.dp,
                            if (isSoundEnabled) SaffronPrimary else MaterialTheme.colorScheme.outlineVariant,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            isSoundEnabled = !isSoundEnabled
                            if (!isSoundEnabled) {
                                try {
                                    malaPlayer?.stop()
                                    malaPlayer?.release()
                                } catch (_: Exception) {}
                                malaPlayer = null
                                isChantingNow = false
                            }
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val soundDesc = when (lang) { "hi" -> "ध्वनि"; "en" -> "Audio"; else -> "ધ્વનિ" }
                        Icon(
                            imageVector = if (isSoundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                            contentDescription = soundDesc,
                            tint = if (isSoundEnabled) SaffronPrimary else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        val soundLabel = if (isSoundEnabled) {
                            when (lang) { "hi" -> "🔊 ध्वनि: चालू"; "en" -> "🔊 Audio: ON"; else -> "🔊 મંત્ર ધ્વનિ: ચાલુ" }
                        } else {
                            when (lang) { "hi" -> "🔇 ध्वनि: बंद"; "en" -> "🔇 Audio: OFF"; else -> "🔇 મંત્ર ધ્વનિ: બંધ" }
                        }
                        Text(
                            text = soundLabel,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isSoundEnabled) SaffronPrimary else Color.Gray
                        )
                    }
                }

                // Haptic Toggle Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isHapticEnabled) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface)
                        .border(
                            1.dp,
                            if (isHapticEnabled) SaffronPrimary.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outlineVariant,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { isHapticEnabled = !isHapticEnabled }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val hapticDesc = when (lang) { "hi" -> "कंपन"; "en" -> "Haptics"; else -> "વાઇબ્રેશન" }
                        Icon(
                            imageVector = Icons.Default.Vibration,
                            contentDescription = hapticDesc,
                            tint = if (isHapticEnabled) SaffronPrimary else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        val hapticLabel = if (isHapticEnabled) {
                            when (lang) { "hi" -> "📳 कंपन: चालू"; "en" -> "📳 Haptic: ON"; else -> "📳 કંપન: ચાલુ" }
                        } else {
                            when (lang) { "hi" -> "📳 कंपन: बंद"; "en" -> "📳 Haptic: OFF"; else -> "📳 કંપન: બંધ" }
                        }
                        Text(
                            text = hapticLabel,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isHapticEnabled) SaffronPrimary else Color.Gray
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(14.dp))

            // 108 Beads Circular Interactive Wheel
            Box(
                modifier = Modifier
                    .size(285.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { handleBeadTap() })
                    },
                contentAlignment = Alignment.Center
            ) {
                // Draw 108 small beads on the perimeter with gold glow
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val radius = (size.width / 2) - 18.dp.toPx()
                    val totalBeads = 108

                    val countedColor = if (isDark) Color(0xFFF97316) else Color(0xFFEA580C)
                    val uncountedColor = if (isDark) Color(0xFF4B5563) else Color(0xFFE5E7EB)

                    for (i in 0 until totalBeads) {
                        val angle = (2 * PI * i / totalBeads) - (PI / 2)
                        val x = center.x + radius * cos(angle).toFloat()
                        val y = center.y + radius * sin(angle).toFloat()
                        val isCounted = i < currentBeadCount

                        drawCircle(
                            color = if (isCounted) countedColor else uncountedColor,
                            radius = if (i == 0) 6.5.dp.toPx() else 3.5.dp.toPx(),
                            center = Offset(x, y)
                        )
                    }
                }

                // Big Center Tap Button with Animated Spring Scale
                val centerGradient = if (isDark) {
                    Brush.radialGradient(
                        listOf(Color(0xFF292524), Color(0xFF1C1917), Color(0xFF0C0A09))
                    )
                } else {
                    Brush.radialGradient(
                        listOf(Color(0xFFFFFFFF), Color(0xFFF9FAFB), Color(0xFFF3F4F6))
                    )
                }

                Box(
                    modifier = Modifier
                        .scale(tapScale)
                        .size(190.dp)
                        .clip(CircleShape)
                        .background(centerGradient)
                        .border(
                            width = if (isChantingNow) 4.5.dp else 3.dp,
                            color = if (isChantingNow) Color(0xFFEA580C) else SaffronPrimary,
                            shape = CircleShape
                        )
                        .shadow(if (isDark) 4.dp else 10.dp, CircleShape)
                        .clickable(interactionSource = tapInteractionSource, indication = null) {
                            handleBeadTap()
                        }
                        .testTag("mala_tap_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        DiyaIcon(modifier = Modifier.size(34.dp))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$currentBeadCount",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = SaffronPrimary,
                            fontSize = 38.sp
                        )
                        Text(
                            text = when (lang) {
                                "hi" -> "१०८ में से"
                                "en" -> "out of 108"
                                else -> "૧૦૮ માંથી"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(SaffronPrimary)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isSoundEnabled) Icons.Default.VolumeUp else Icons.Default.TouchApp,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = Color(0xFF1F1206)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isSoundEnabled) {
                                        when (lang) {
                                            "hi" -> "टच करें (नाम जप)"
                                            "en" -> "Tap to Chant"
                                            else -> "ટચ કરો (નામ જાપ)"
                                        }
                                    } else {
                                        when (lang) {
                                            "hi" -> "टच करें (जप)"
                                            "en" -> "Tap to Count"
                                            else -> "ટચ કરો (જાપ)"
                                        }
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1F1206)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Progress Details: Rounds & Lifetime Counts
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = when (lang) {
                                "hi" -> "आज के फेरे"
                                "en" -> "Today's Rounds"
                                else -> "આજના રાઉન્ડ"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("$currentRoundsCount", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = SaffronDark)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = when (lang) {
                                "hi" -> "कुल माला"
                                "en" -> "Total Rounds"
                                else -> "કુલ આજીવન માળા"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("${lifetimeRounds + currentRoundsCount}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = when (lang) {
                                "hi" -> "कुल मनके"
                                "en" -> "Total Beads"
                                else -> "કુલ મણકા"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("${lifetimeBeads + currentBeadCount}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFFB91C1C))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Voluntary Rewarded Blessing: Earn +108 beads / +1 mala round
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showMalaRewardDialog = true }
                    .shadow(3.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(SaffronPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFF1F1206),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = when (lang) {
                                "hi" -> "🎁 दिव्य १०८ मनके बोनस प्राप्त करें"
                                "en" -> "🎁 Earn +108 Divine Beads Bonus"
                                else -> "🎁 દિવ્ય ૧૦૮ મણકા બોનસ મેળવો"
                            },
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF78350F)
                        )
                        Text(
                            text = when (lang) {
                                "hi" -> "वीडियो देखकर १ पूरी माला का पुण्य जोड़ें"
                                "en" -> "Watch video to add 1 full mala (+108 beads)"
                                else -> "ટૂંકો વિડિઓ જોઈને ૧ સંપૂર્ણ માળાનું પુણ્ય ઉમેરો"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF92400E)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Button(
                        onClick = { showMalaRewardDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = when (lang) {
                                "hi" -> "+१०८"
                                "en" -> "+108"
                                else -> "+૧૦૮"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F1206)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = when (lang) {
                    "hi" -> "“मंत्र जप से मन शुद्ध होता है और आंतरिक शांति का अनुभव होता है।”"
                    "en" -> "“Chanting sacred mantras purifies the mind and brings inner peace.”"
                    else -> "“મંત્ર જાપથી મન પવિત્ર થાય છે અને આંતરિક શાંતિનો અનુભવ થાય છે.”"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Non-intrusive banner safely pinned at bottom of page away from bead wheel
        BannerAdView(modifier = Modifier.padding(horizontal = 4.dp))
    }
}
