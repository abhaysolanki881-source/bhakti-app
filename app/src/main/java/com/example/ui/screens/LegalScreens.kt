package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BannerAdView
import com.example.ui.components.AnimatedScaleButton
import com.example.ui.components.BhaktiSubScreenTopBar
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.TempleGold

@Composable
fun PrivacyPolicyScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("privacy_policy_screen")
    ) {
        BhaktiSubScreenTopBar(
            title = "ગોપનીયતા નીતિ (Privacy Policy)",
            subtitle = "તમારા ડેટાની સુરક્ષા અને અધિકારો",
            onBack = onBack
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(SaffronPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Color(0xFF1F1206), modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "૧૦૦% સલામત & ઑફલાઇન એપ",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = SaffronPrimary
                            )
                            Text(
                                text = "છેલ્લી સુધારેલ તારીખ: નવેમ્બર ૨૦૨૬ • આવૃત્તિ ૧.૦",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // 1. Data Collection & Privacy
            item {
                LegalSectionCard(
                    icon = Icons.Default.Lock,
                    title = "૧. વ્યક્તિગત માહિતીનો સંગ્રહ (Zero Personal Data Collection)",
                    content = "ભક્તિ સાગર (Bhakti Sagar) એપ્લિકેશન વપરાશકર્તાની કોઈપણ વ્યક્તિગત માહિતી (જેમ કે નામ, ફોન નંબર, ઈમેલ, ચોક્કસ લોકેશન અથવા સંપર્કો) એકત્રિત, સંગ્રહિત કે શેર કરતી નથી.\n\nઆ એપ્લિકેશન ૧૦૦% ડિવાઇસ-સ્થાનિક (device-local) સિદ્ધાંત પર કાર્ય કરે છે. તમારા દ્વારા સાચવેલ મનપસંદ ભજન, ગીતા શ્લોક બુકમાર્ક્સ અને જાપ માળાની ગણતરી ફક્ત તમારા ફોનની આંતરિક મેમરી (Room Local Database) માં સુરક્ષિત રહે છે."
                )
            }

            // 2. Permissions Justification (Play Store Mandatory)
            item {
                LegalSectionCard(
                    icon = Icons.Default.Security,
                    title = "૨. જરૂરી પરવાનગીઓ (Permissions & Usage)",
                    content = "ગૂગલ પ્લે ડેવલપર નીતિ અનુસાર આ એપ માત્ર જરૂરી સુવિધાઓ માટે ન્યૂનતમ પરવાનગીઓનો ઉપયોગ કરે છે:\n\n" +
                            "• VIBRATE (કંપન): જાપ માળા ગણતરી વખતે દરેક મણકા પર અને ૧૦૮ મણકા પૂર્ણ થતાં ભક્તને સાધનામાં વિક્ષેપ ન પડે તે માટે હળવું ટેક્ટાઇલ વાઇબ્રેશન આપવા.\n\n" +
                            "• POST_NOTIFICATIONS (સૂચનાઓ): જો તમે સેટિંગ્સમાં સવાર-સાંજની પૂજા કે આરતીનું સ્મરણ ચાલુ કર્યું હોય, તો નિયત સમયે યાદ અપાવવા માટે.\n\n" +
                            "• INTERNET & ACCESS_NETWORK_STATE: સત્સંગ જાહેરાત (AdMob) અને સલામત HTTPS કનેક્શન ચકાસણી માટે."
                )
            }

            // 3. Audio & Content Copyright Disclosure
            item {
                LegalSectionCard(
                    icon = Icons.Default.VolumeUp,
                    title = "૩. સામગ્રી અને કોપીરાઇટ સુરક્ષા (Content & Copyright)",
                    content = "• આ એપ્લિકેશનમાં શામેલ તમામ આરતી, ચાલીસા, ગીતા શ્લોકો અને વૈદિક મંત્રો સદીઓ જૂની સનાતન હિન્દુ ધાર્મિક પરંપરાના સાર્વજનિક ક્ષેત્ર (Public Domain) ના ગ્રંથોમાંથી લેવાયેલા છે.\n\n" +
                            "• તમામ ઓડિયો પવિત્ર પારંપરિક સૂર-તાલ અને મૂળ માનવ કંઠમાં આ એપ માટે ખાસ તૈયાર કરાયા છે, જેમાં કોઈ થર્ડ-પાર્ટી મ્યુઝિક લેબલનો કોપીરાઇટેડ કન્ટેન્ટ શામેલ નથી.\n\n" +
                            "• પંચાંગ અને ચોઘડિયા ગણતરીઓ સૂર્યોદય-સૂર્યાસ્ત આધારિત અમાસંત ગુજરાતી વૈદિક ગણિત પર આધારિત છે."
                )
            }

            // 4. Advertising & Google AdMob Disclosure
            item {
                LegalSectionCard(
                    icon = Icons.Default.CheckCircle,
                    title = "૪. જાહેરાતો & ગૂગલ એડમોબ (Advertisements & AdMob)",
                    content = "• આ એપ્લિકેશન આધ્યાત્મિક સેવાઓ જાળવવા માટે Google AdMob જાહેરાત પ્લેટફોર્મનો ઉપયોગ કરે છે.\n\n" +
                            "• Google AdMob નીતિ અનુસાર જાહેરાતો ગૂગલ પ્લે ડેવલપર પ્રોગ્રામ નીતિઓ અને સુરક્ષા ધોરણોનું પાલન કરે છે.\n\n" +
                            "• વપરાશકર્તાની અંગત માહિતી કે સંવેદનશીલ ડેટા ક્યારેય વેચવામાં કે તૃતીય પક્ષને આપવામાં આવતો નથી."
                )
            }

            // 5. User Data Deletion & Control
            item {
                LegalSectionCard(
                    icon = Icons.Default.Info,
                    title = "૫. ડેટા નિયંત્રણ & રદ કરવાની પ્રક્રિયા (User Rights)",
                    content = "તમે એપ્લિકેશનના 'સેટિંગ્સ' મેનૂમાં જઈને એક ક્લિકમાં તમારી જાપ માળાનો ઇતિહાસ રીસેટ કરી શકો છો અથવા મનપસંદ યાદી ખાલી કરી શકો છો. એપને અનઇન્સ્ટોલ કરવાથી તમામ સ્થાનિક ડેટા સંપૂર્ણપણે નાબૂદ થાય છે."
                )
            }

            // 6. Contact Developer
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Email, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("સંપર્ક અને પ્રશ્નોત્તરી (Developer Contact)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "જો તમને આ ગોપનીયતા નીતિ અથવા એપ્લિકેશન સંબંધિત કોઈ પ્રશ્ન હોય, તો તમે અમારો સીધો સંપર્ક કરી શકો છો:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        AnimatedScaleButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:abhaysolanki881@gmail.com")
                                    putExtra(Intent.EXTRA_SUBJECT, "Bhakti Sagar Gujarati - Privacy & Support Query")
                                }
                                context.startActivity(Intent.createChooser(intent, "ઈમેલ મોકલો"))
                            },
                            backgroundColor = SaffronPrimary,
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF1F1206), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("abhaysolanki881@gmail.com", color = Color(0xFF1F1206), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        BannerAdView(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
    }
}

@Composable
fun TermsAndConditionsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("terms_conditions_screen")
    ) {
        BhaktiSubScreenTopBar(
            title = "નિયમો અને શરતો (Terms of Use)",
            subtitle = "એપ્લિકેશન ઉપયોગ સંબંધિત નિયમો",
            onBack = onBack
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(SaffronPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Gavel, contentDescription = null, tint = Color(0xFF1F1206), modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "ઉપયોગની શરતો અને આધ્યાત્મિક સેવા",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = SaffronPrimary
                            )
                            Text(
                                text = "સ્વીકૃતિ: એપ ડાઉનલોડ કે ઉપયોગ કરવાથી શરતો માન્ય ગણાય છે.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // 1. Acceptance & Purpose
            item {
                LegalSectionCard(
                    icon = Icons.Default.CheckCircle,
                    title = "૧. શરતોનો સ્વીકાર અને હેતુ (Acceptance & Purpose)",
                    content = "ભક્તિ સાગર (Bhakti Sagar) નો મુખ્ય ઉદ્દેશ્ય ભક્તોને નિત્ય દર્શન, સ્તોત્ર પાઠ, ગીતા જ્ઞાન, પંચાંગ અને મંત્ર સાધનાનું પવિત્ર માધ્યમ પૂરું પાડવાનો છે. આ એપ્લિકેશનનો ઉપયોગ માત્ર વ્યક્તિગત અને ધાર્મિક સદભાવના માટે કરવાનો રહેશે."
                )
            }

            // 2. Spiritual & Calendar Disclaimer
            item {
                LegalSectionCard(
                    icon = Icons.Default.Info,
                    title = "૨. પંચાંગ અને મુહૂર્ત ડિસ્ક્લેમર (Calendar & Muhurat Disclaimer)",
                    content = "એપ્લિકેશનમાં પ્રદર્શિત પંચાંગ, તિથિ, સૂર્યોદય-સૂર્યાસ્ત અને ચોઘડિયા પારંપરિક અમાસંત ગુજરાતી ખગોળીય પદ્ધતિ અનુસાર ગણવામાં આવે છે. આ માહિતી સામાન્ય ધાર્મિક અવલોકન અને જાગૃતિ માટે છે. વિશેષ વૈદિક કે સંસ્કાર વિધિઓ માટે સ્થાનિક જ્યોતિષી કે આચાર્યનું માર્ગદર્શન લેવાની સલાહ આપવામાં આવે છે."
                )
            }

            // 3. Intellectual Property & Fair Use
            item {
                LegalSectionCard(
                    icon = Icons.Default.Security,
                    title = "૩. બૌદ્ધિક સંપત્તિ અને યોગ્ય ઉપયોગ (Fair Use & Sharing)",
                    content = "• ભક્તિ સુવિચાર કાર્ડ્સ અને દર્શન ઈમેજીસને WhatsApp કે સોશિયલ મીડિયા પર પરિવાર અને મિત્રો સાથે સદભાવના ફેલાવવા માટે શેર કરવાની છૂટ છે.\n\n" +
                            "• એપ્લિકેશનના કોડબેઝ, ગ્રાફિક્સ કે સંસાધનોનું કોઈપણ અનધિકૃત વ્યાવસાયિક પુનઃઉત્પાદન અથવા રિવર્સ-એન્જિનિયરિંગ પ્રતિબંધિત છે."
                )
            }

            // 4. Modifications & Availability
            item {
                LegalSectionCard(
                    icon = Icons.Default.VerifiedUser,
                    title = "૪. અપડેટ્સ અને ફેરફારો (Modifications & Updates)",
                    content = "એપ્લિકેશનની ગુણવત્તા સુધારવા માટે સમયાંતરે નવી સુવિધાઓ, ચાલીસા કે પંચાંગ વર્ષો (૨૦૨૬ થી ૨૦૩૦) ઉમેરવામાં આવી શકે છે. વપરાશકર્તાઓએ શ્રેષ્ઠ અનુભવ માટે પ્લે સ્ટોર પરથી નવીનતમ આવૃત્તિ અપડેટ રાખવી."
                )
            }

            // 5. Contact Support
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Email, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("સહાય અને પ્રતિક્રિયા (Support & Feedback)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "કોઈપણ સૂચન કે પ્રશ્ન માટે ડેવલપરનો સંપર્ક કરો:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        AnimatedScaleButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:abhaysolanki881@gmail.com")
                                    putExtra(Intent.EXTRA_SUBJECT, "Bhakti Sagar - Feedback & Inquiry")
                                }
                                context.startActivity(Intent.createChooser(intent, "ઈમેલ મોકલો"))
                            },
                            backgroundColor = SaffronPrimary,
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF1F1206), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("abhaysolanki881@gmail.com", color = Color(0xFF1F1206), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        BannerAdView(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
    }
}

@Composable
private fun LegalSectionCard(
    icon: ImageVector,
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 22.sp
            )
        }
    }
}
