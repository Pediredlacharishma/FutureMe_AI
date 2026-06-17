package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import com.example.data.model.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch


// FutureMe Cyberpunk Color Constants
val CyberBackgroundStart = Color(0xFF03001C)
val CyberBackgroundEnd = Color(0xFF0B0128)
val CyberPurple = Color(0xFFB5179E)
val CyberNeonCyan = Color(0xFF4CC9F0)
val CyberElectricBlue = Color(0xFF3F37C9)
val CyberGlassCardBg = Color(0x2B1B1236)
val CyberNeonPurpleGlow = Color(0xFFD946EF)
val CyberAlertPink = Color(0xFFF72585)

@Composable
fun CyberpunkBackground(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(CyberBackgroundStart, CyberBackgroundEnd)
                )
            )
            .drawBehind {
                // Add soft glowing spatial background orbs
                drawCircle(
                    color = CyberPurple.copy(alpha = 0.25f),
                    radius = size.width * 0.5f,
                    center = Offset(x = size.width * 0.1f, y = size.height * 0.2f)
                )
                drawCircle(
                    color = CyberNeonCyan.copy(alpha = 0.15f),
                    radius = size.width * 0.4f,
                    center = Offset(x = size.width * 0.9f, y = size.height * 0.7f)
                )
            }
    ) {
        content()
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    borderGlowColor: Color = CyberNeonPurpleGlow.copy(alpha = 0.3f),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            1.dp,
            Brush.linearGradient(
                colors = listOf(
                    borderGlowColor,
                    Color.White.copy(alpha = 0.05f),
                    borderGlowColor.copy(alpha = 0.1f)
                )
            )
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            content()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FutureMeApp(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    CyberpunkBackground {
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                slideInHorizontally { width -> width / 2 } + fadeIn() with
                        slideOutHorizontally { width -> -width / 2 } + fadeOut()
            },
            label = "ScreenTransition"
        ) { target ->
            when (target) {
                Screen.Login -> LoginScreen(viewModel)
                Screen.Signup -> SignupScreen(viewModel)
                Screen.Dashboard -> DashboardScreen(viewModel, currentUser)
                Screen.GoalDiscovery -> GoalDiscoveryScreen(viewModel)
                Screen.EnvironmentForm -> EnvironmentFormScreen(viewModel, currentUser)
                Screen.SkillsRoadmap -> SkillsRoadmapScreen(viewModel, currentUser)
                Screen.GuideChat -> ChatScreen(viewModel, currentUser, isGuide = true)
                Screen.ShadowChat -> ChatScreen(viewModel, currentUser, isGuide = false)
                Screen.WeeklyHistoryScreen -> WeeklyHistoryScreen(viewModel, currentUser)
                Screen.PersonalLegend -> PersonalLegendScreen(viewModel, currentUser)
                Screen.BookOfYou -> BookOfYouScreen(viewModel, currentUser)
                Screen.Journal -> JournalScreen(viewModel, currentUser)
                Screen.RealityCheck -> RealityCheckScreen(viewModel, currentUser)
                Screen.UniverseMap -> UniverseMapScreen(viewModel, currentUser)
                Screen.TimeCapsule -> TimeCapsuleScreen(viewModel, currentUser)
                Screen.Constellation -> ConstellationScreen(viewModel, currentUser)
                Screen.MirrorQuestion -> MirrorQuestionScreen(viewModel, currentUser)
                Screen.Documentary -> DocumentaryScreen(viewModel, currentUser)
                Screen.Challenges -> ChallengesScreen(viewModel, currentUser)
            }
        }
    }
}

@Composable
fun LoginScreen(viewModel: MainViewModel) {
    var email by remember { mutableStateOf("charishma.pediredla7@gmail.com") }
    var password by remember { mutableStateOf("demo123") }
    var errorMsg by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(30.dp))
                // Title
                Text(
                    text = "FUTUREME AI",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White,
                    letterSpacing = 4.sp,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        shadow = Shadow(
                            color = CyberNeonPurpleGlow,
                            blurRadius = 16f,
                            offset = Offset(0f, 0f)
                        )
                    )
                )

                Text(
                    text = "Meet the future version of yourself before it becomes reality.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CyberNeonCyan,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp, bottom = 32.dp, start = 16.dp, end = 16.dp)
                )

                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    borderGlowColor = CyberPurple
                ) {
                    Text(
                        text = "Initialize Timeline Sync",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Email input
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Quantum Space Email") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberNeonCyan,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedLabelColor = CyberNeonCyan,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email", tint = CyberNeonCyan) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("email_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password input
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Chronos Keycode (Password)") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberNeonPurpleGlow,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedLabelColor = CyberNeonPurpleGlow,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password", tint = CyberNeonPurpleGlow) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("password_input")
                    )

                    if (errorMsg.isNotEmpty()) {
                        Text(
                            text = errorMsg,
                            color = CyberAlertPink,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Login Button
                    Button(
                        onClick = {
                            viewModel.login(email,
                                onSuccess = { viewModel.navigateTo(Screen.Dashboard) },
                                onError = { errorMsg = it }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberElectricBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("login_button")
                    ) {
                        Text("AUTHENTICATE TIMELINE", fontWeight = FontWeight.Bold, color = Color.White, letterSpacing = 1.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Don't have a timeline?", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                        TextButton(onClick = { viewModel.navigateTo(Screen.Signup) }) {
                            Text("Create FutureMe Profile", color = CyberNeonCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Rapid Tester Shortcut Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .clickable {
                            email = "charishma.pediredla7@gmail.com"
                            viewModel.login(email,
                                onSuccess = { viewModel.navigateTo(Screen.Dashboard) },
                                onError = { }
                            )
                        },
                    border = BorderStroke(1.dp, CyberNeonCyan.copy(alpha = 0.5f)),
                    colors = CardDefaults.cardColors(containerColor = CyberPurple.copy(alpha = 0.15f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Star, contentDescription = "Fast Sync", tint = CyberNeonCyan, modifier = Modifier.size(30.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Quantum Fast Pass (Try App Instantly)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Click here to instantly enter with the loaded trial profile.", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun SignupScreen(viewModel: MainViewModel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var classLevel by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "FUTUREME AI",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White,
                    letterSpacing = 2.sp,
                )

                Text(
                    text = "Deploy your custom timeline simulator.",
                    fontSize = 14.sp,
                    color = CyberNeonCyan,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    borderGlowColor = CyberNeonPurpleGlow
                ) {
                    Text(
                        text = "Register Cosmic Identity",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("What is your Name?") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberNeonCyan,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedLabelColor = CyberNeonCyan,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                            focusedTextColor = Color.White
                        ),
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name", tint = CyberNeonCyan) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("E-Mail Address") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberNeonCyan,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedLabelColor = CyberNeonCyan,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                            focusedTextColor = Color.White
                        ),
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email", tint = CyberNeonCyan) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = age,
                        onValueChange = { age = it },
                        label = { Text("Your Age") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberNeonCyan,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedLabelColor = CyberNeonCyan,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                            focusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = classLevel,
                        onValueChange = { classLevel = it },
                        label = { Text("Your Class Year (e.g. 1st Year College, Grade 12)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberNeonCyan,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedLabelColor = CyberNeonCyan,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                            focusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (errorMsg.isNotEmpty()) {
                        Text(
                            text = errorMsg,
                            color = CyberAlertPink,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            viewModel.signup(name, email, age, classLevel,
                                onSuccess = { viewModel.navigateTo(Screen.GoalDiscovery) }, // Navigate first to help discovery dream goal
                                onError = { errorMsg = it }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberAlertPink),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("signup_submit_button")
                    ) {
                        Text("SEED TEMPORAL MATRIX", fontWeight = FontWeight.Bold, color = Color.White, letterSpacing = 1.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Already registered?", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                        TextButton(onClick = { viewModel.navigateTo(Screen.Login) }) {
                            Text("Login Profile", color = CyberNeonCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun DashboardScreen(viewModel: MainViewModel, user: UserEntity?) {
    if (user == null) return

    val animatedProgress by animateFloatAsState(
        targetValue = user.dreamProgress / 100f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "ProgressRing"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Cosmic Header Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp, top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "MEET THE FUTURE",
                        fontSize = 11.sp,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberNeonCyan
                    )
                    Text(
                        text = "Hello, ${user.name}!",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                IconButton(
                    onClick = { viewModel.logout() },
                    modifier = Modifier
                        .background(CyberGlassCardBg, CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Sign Out", tint = CyberAlertPink)
                }
            }

            // Flagship Feature: Dream Progress Orbit Ring
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 18.dp),
                borderGlowColor = CyberNeonCyan
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "CURRENT TIMELINE PACING",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberNeonPurpleGlow,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = user.goal.ifEmpty { "Undetermined Goal" },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = user.currentStage,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CyberNeonCyan,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        
                        Text(
                            text = "Timeline Level: ${(user.dreamProgress / 20).coerceIn(0, 4)} / 5",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }

                    // Rounded Progress Ring
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(8.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawArc(
                                color = Color.White.copy(alpha = 0.1f),
                                startAngle = -90f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                            )
                            drawArc(
                                brush = Brush.linearGradient(
                                    colors = listOf(CyberPurple, CyberNeonCyan)
                                ),
                                startAngle = -90f,
                                sweepAngle = animatedProgress * 360f,
                                useCenter = false,
                                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${user.dreamProgress}%",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "DREAM",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 8.sp,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }

                HorizontalDivider(color = Color.White.copy(alpha = 0.1f), modifier = Modifier.padding(vertical = 12.dp))

                // Optimized Timeline speed
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = "Pace", tint = CyberNeonCyan, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Predicted Mastery Pace:",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }
                    val defaultAgeNum = user.age.toIntOrNull() ?: 20
                    val currentMasteryAge = defaultAgeNum + ((100 - user.dreamProgress) / 10).coerceIn(2, 8)
                    val optimizedAge = defaultAgeNum + ((100 - user.dreamProgress) / 18).coerceIn(1, 4)
                    Text(
                        text = "Current: Age $currentMasteryAge | Optimized: Age $optimizedAge",
                        color = CyberNeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // FLAGSHIP FEATURES: TWO HUGE REFLECTION VISIONS
            Text(
                text = "🌅 TEMPORAL COMM-PORTALS (FLAGSHIP)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.7f),
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(bottom = 12.dp, top = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // GUIDE PORTAL
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(200.dp)
                        .clickable { viewModel.navigateTo(Screen.GuideChat) }
                        .testTag("future_guide_shortcut_card"),
                    colors = CardDefaults.cardColors(containerColor = CyberPurple.copy(alpha = 0.12f)),
                    border = BorderStroke(1.dp, CyberNeonCyan.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🌅 FUTURE GUIDE\n(SUCCESSFUL SELF)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CyberNeonCyan,
                            lineHeight = 16.sp
                        )

                        Text(
                            text = "I achieved our dream of becoming a ${user.goal} master. Speak with me, learn secrets of how I did it.",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f),
                            lineHeight = 14.sp
                        )

                        Button(
                            onClick = { viewModel.navigateTo(Screen.GuideChat) },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberNeonCyan.copy(alpha = 0.8f)),
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("COM-SYNC", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }
                }

                // SHADOW PORTAL
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(200.dp)
                        .clickable { viewModel.navigateTo(Screen.ShadowChat) }
                        .testTag("shadow_self_shortcut_card"),
                    colors = CardDefaults.cardColors(containerColor = CyberAlertPink.copy(alpha = 0.1f)),
                    border = BorderStroke(1.dp, CyberAlertPink.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🌪 SHADOW SELF\n(FAILED TIMELINE)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CyberAlertPink,
                            lineHeight = 16.sp
                        )

                        Text(
                            text = "I gave up when things got too challenging. Hear my regrets, avoid the pitfalls of my timeline.",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f),
                            lineHeight = 14.sp
                        )

                        Button(
                            onClick = { viewModel.navigateTo(Screen.ShadowChat) },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberAlertPink.copy(alpha = 0.8f)),
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("HEAR WARNING", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            // 🌌 THE FUTURE SANCTUARY (EMOTIONAL CONNECTION)
            Text(
                text = "🌌 THE FUTURE SANCTUARY (EMOTIONAL CONNECTION)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.7f),
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(bottom = 12.dp, top = 8.dp)
            )

            // Rows of new emotional and experiential layers
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Time Capsule
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.navigateTo(Screen.TimeCapsule) }
                        .testTag("time_capsule_portal_card"),
                    colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                    border = BorderStroke(1.dp, CyberNeonCyan.copy(alpha = 0.25f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.DateRange, contentDescription = "Capsule", tint = CyberNeonCyan, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Time Capsule", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Write a raw message to your future self. Lock it in a temporal seal.",
                            color = Color.White.copy(alpha = 0.65f),
                            fontSize = 10.sp,
                            lineHeight = 13.sp
                        )
                    }
                }

                // Constellation
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.navigateTo(Screen.Constellation) }
                        .testTag("constellation_portal_card"),
                    colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                    border = BorderStroke(1.dp, CyberNeonPurpleGlow.copy(alpha = 0.25f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = "Constellation", tint = CyberNeonPurpleGlow, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Constellation", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Gaze at the stellar map of your character growth. Interactive dimensions.",
                            color = Color.White.copy(alpha = 0.65f),
                            fontSize = 10.sp,
                            lineHeight = 13.sp
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Mirror Question
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.navigateTo(Screen.MirrorQuestion) }
                        .testTag("mirror_portal_card"),
                    colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                    border = BorderStroke(1.dp, CyberAlertPink.copy(alpha = 0.25f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Face, contentDescription = "Mirror", tint = CyberAlertPink, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Mirror Question", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Confront the raw questions of life and purpose. Deep personal evaluation.",
                            color = Color.White.copy(alpha = 0.65f),
                            fontSize = 10.sp,
                            lineHeight = 13.sp
                        )
                    }
                }

                // Documentary
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.navigateTo(Screen.Documentary) }
                        .testTag("documentary_portal_card"),
                    colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                    border = BorderStroke(1.dp, Color.Green.copy(alpha = 0.25f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Documentary", tint = Color.Green, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Documentary", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Generate a mini cinematic documentary narrating your entire life trajectory.",
                            color = Color.White.copy(alpha = 0.65f),
                            fontSize = 10.sp,
                            lineHeight = 13.sp
                        )
                    }
                }
            }

            // Challenge card (full-width for premium presence)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 18.dp)
                    .clickable { viewModel.navigateTo(Screen.Challenges) }
                    .testTag("challenges_portal_card"),
                colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                border = BorderStroke(1.dp, CyberNeonCyan.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(CyberNeonCyan.copy(alpha = 0.15f), CircleShape)
                            .padding(10.dp)
                    ) {
                        Icon(Icons.Default.Build, contentDescription = "Challenges", tint = CyberNeonCyan, modifier = Modifier.size(22.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Future Guide Quests & Challenges", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(
                            "Complete personal accountability challenges issued directly by your Future Guide to unlock rewards.",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 11.sp,
                            lineHeight = 14.sp
                        )
                    }
                    Icon(Icons.Default.PlayArrow, contentDescription = "Enter", tint = Color.White.copy(alpha = 0.4f))
                }
            }

            // ✨ STELLAR CHRONO-MEMORIES (FUTUREME MOMENTS)
            Text(
                text = "✨ STELLAR CHRONO-MEMORIES (MILESTONES)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.7f),
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(bottom = 12.dp, top = 8.dp)
            )

            if (user?.futureMoments.isNullOrEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "No moments",
                                tint = CyberNeonPurpleGlow.copy(alpha = 0.4f),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "No timeline milestones crystallized yet.",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 11.sp
                            )
                            Text(
                                text = "Complete quests, logs, or study plans to record memorable achievements.",
                                color = Color.White.copy(alpha = 0.4f),
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
            } else {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(user!!.futureMoments.reversed()) { moment ->
                        val cardBorderColor = when (moment.category.lowercase()) {
                            "project" -> CyberNeonCyan
                            "internship" -> CyberNeonPurpleGlow
                            "rejection" -> CyberAlertPink
                            "job offer" -> Color.Green
                            else -> Color.White.copy(alpha = 0.3f)
                        }
                        
                        Card(
                            modifier = Modifier
                                .width(280.dp)
                                .testTag("moment_card_${moment.id}"),
                            colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                            border = BorderStroke(1.dp, cardBorderColor.copy(alpha = 0.4f)),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(cardBorderColor.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = moment.category.uppercase(),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = cardBorderColor
                                        )
                                    }
                                    Text(
                                        text = moment.dateLogged,
                                        fontSize = 10.sp,
                                        color = Color.White.copy(alpha = 0.5f)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(10.dp))
                                
                                Text(
                                    text = moment.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = moment.subtitle,
                                    fontSize = 11.sp,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    color = Color.White.copy(alpha = 0.7f),
                                    lineHeight = 14.sp,
                                    modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                                )
                                
                                Text(
                                    text = moment.content,
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.85f),
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }
            }

            // DNA IDENTITY & REFLECTION HARMONIZERS (PRIMARY VISUAL IDENTITY HUB)
            Text(
                text = "🧬 IDENTITY, STORY & TIMELINE DYNAMICS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.7f),
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(bottom = 12.dp, top = 8.dp)
            )

            // Portal Card 1: Personal Legend & Autobiography Book of You (Row)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.navigateTo(Screen.PersonalLegend) }
                        .testTag("personal_legend_portal_card"),
                    colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                    border = BorderStroke(1.dp, CyberNeonCyan.copy(alpha = 0.25f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = "Legend", tint = CyberNeonCyan, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Personal Legend", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Discover your core motivators, life purpose, and destination identity narrative.",
                            color = Color.White.copy(alpha = 0.65f),
                            fontSize = 10.sp,
                            lineHeight = 13.sp
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.navigateTo(Screen.BookOfYou) }
                        .testTag("book_of_you_portal_card"),
                    colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                    border = BorderStroke(1.dp, CyberNeonPurpleGlow.copy(alpha = 0.25f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = "Book", tint = CyberNeonPurpleGlow, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Book of You", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "A living autobiography. Read chapters of your journey as your progress unlocks them.",
                            color = Color.White.copy(alpha = 0.65f),
                            fontSize = 10.sp,
                            lineHeight = 13.sp
                        )
                    }
                }
            }

            // Portal Card 2: FutureMe Journal, Reality Check & Universe Map Map (Vertical Row/List)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .clickable { viewModel.navigateTo(Screen.Journal) }
                    .testTag("journal_portal_card"),
                colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(CyberNeonCyan.copy(alpha = 0.15f), CircleShape)
                            .padding(10.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Journal", tint = CyberNeonCyan, modifier = Modifier.size(22.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("FutureMe Echo Journal", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(
                            "Log daily reflections and challenges. Receive instant response guidance from both selves inside the entry.",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 11.sp,
                            lineHeight = 14.sp
                        )
                    }
                    Icon(Icons.Default.PlayArrow, contentDescription = "Enter", tint = Color.White.copy(alpha = 0.4f))
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.navigateTo(Screen.RealityCheck) }
                        .testTag("reality_check_portal_card"),
                    colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                    border = BorderStroke(1.dp, CyberAlertPink.copy(alpha = 0.25f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = "Audit", tint = CyberAlertPink, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Reality Check", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Compare actual action stats with active target plans. Audit timeline balance gaps.",
                            color = Color.White.copy(alpha = 0.65f),
                            fontSize = 10.sp,
                            lineHeight = 13.sp
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.navigateTo(Screen.UniverseMap) }
                        .testTag("universe_map_portal_card"),
                    colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                    border = BorderStroke(1.dp, Color.Green.copy(alpha = 0.25f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Place, contentDescription = "Sim", tint = Color.Green, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Universe Sim", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Interactive Life Simulator. Adjust slide habits and preview branches of alternative paths.",
                            color = Color.White.copy(alpha = 0.65f),
                            fontSize = 10.sp,
                            lineHeight = 13.sp
                        )
                    }
                }
            }

            // CORE SETTINGS AND SYSTEM UTILITIES
            Text(
                text = "⚡️ JOURNEY SYSTEMS & SETTINGS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.7f),
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Journey Step Options
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Skills checklist
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.navigateTo(Screen.SkillsRoadmap) },
                    colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.List, contentDescription = "Roadmap", tint = CyberNeonCyan, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Active Roadmap", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Tick off milestones", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                    }
                }

                // Habits adjuster
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.navigateTo(Screen.EnvironmentForm) },
                    colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = "Habits", tint = CyberNeonPurpleGlow, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Daily Habits", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Adjust study index", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Goal brancher
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.startGoalDiscovery() },
                    colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Re-discover Goal", tint = CyberAlertPink, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Pivot Target Goal", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Goal discovery engine", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                    }
                }

                // AI Weekly Evaluation History
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.navigateTo(Screen.WeeklyHistoryScreen) },
                    colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Star, contentDescription = "History", tint = Color.Green, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Weekly Analytics", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("AI chronos assessment", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun GoalDiscoveryScreen(viewModel: MainViewModel) {
    val stage by viewModel.discoveryStage.collectAsStateWithLifecycle()
    val world by viewModel.selectedWorld.collectAsStateWithLifecycle()
    val branch by viewModel.selectedBranch.collectAsStateWithLifecycle()
    val deepBranch by viewModel.selectedDeepBranch.collectAsStateWithLifecycle()
    val results by viewModel.discoveryResults.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                IconButton(onClick = { viewModel.navigateTo(Screen.Dashboard) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text("Branching Goal Discovery", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            // Indicator
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(4) { idx ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(CircleShape)
                            .background(if (idx + 1 <= stage) CyberNeonCyan else Color.White.copy(alpha = 0.15f))
                    )
                }
            }

            Text(
                text = "TIMELINE CONVERSATIONAL ENGINE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = CyberNeonPurpleGlow,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            AnimatedVisibility(visible = stage == 1, enter = fadeIn() + slideInVertically(), exit = fadeOut()) {
                Column {
                    Text("Step 1: Which of these professional sectors aligns with your excitement?", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val worlds = listOf("Technology", "Medicine", "Business", "Arts")
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(worlds) { w ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.selectWorld(w) },
                                colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                            ) {
                                Row(modifier = Modifier.padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text(w, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                                    Icon(Icons.Default.PlayArrow, contentDescription = "Go", tint = CyberNeonCyan)
                                }
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(visible = stage == 2, enter = fadeIn() + slideInVertically(), exit = fadeOut()) {
                Column {
                    Text("Step 2: Exploring ($world). Which niche domain calls to your curiosity?", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))

                    val branches = when (world) {
                        "Technology" -> listOf("AI", "Robotics", "Cybersecurity", "Web Development", "Data Science")
                        "Medicine" -> listOf("Saving Lives in Emergency", "Mental Well-being", "Researching Cures", "Surgical Precision")
                        "Business" -> listOf("Launch New Products", "Manage/Invest Capital", "Organize & Lead Teams", "Creative Marketing")
                        "Arts" -> listOf("Digital Designs & UI", "Music & Soundscapes", "Stories & Writing", "Physical Artifacts/Painting")
                        else -> emptyList()
                    }

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(branches) { b ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.selectBranch(b) },
                                colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                                border = BorderStroke(1.dp, CyberNeonCyan.copy(alpha = 0.3f))
                            ) {
                                Row(modifier = Modifier.padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text(b, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                                    Icon(Icons.Default.PlayArrow, contentDescription = "Go", tint = CyberNeonPurpleGlow)
                                }
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(visible = stage == 3, enter = fadeIn() + slideInVertically(), exit = fadeOut()) {
                Column {
                    Text("Step 3: Defining ($branch). What mission excites you most?", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))

                    val deepBranches = listOf("Teach People", "Build Assistants", "Analyze Data", "Automate Work", "Create Art")
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(deepBranches) { dp ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.selectDeepBranch(dp) },
                                colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                                border = BorderStroke(1.dp, CyberAlertPink.copy(alpha = 0.3f))
                            ) {
                                Row(modifier = Modifier.padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text(dp, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                                    Icon(Icons.Default.PlayArrow, contentDescription = "Go", tint = CyberNeonCyan)
                                }
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(visible = stage == 4, enter = fadeIn() + slideInVertically(), exit = fadeOut()) {
                Column {
                    Text("Step 4: AI Simulator Potential Alignments", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Select a goal to automatically construct and install its detailed professional roadmap.", fontSize = 13.sp, color = CyberNeonCyan, modifier = Modifier.padding(top = 4.dp, bottom = 16.dp))

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        items(results) { res ->
                            GlassCard(borderGlowColor = if (res.percentage > 50) CyberNeonCyan else Color.White.copy(alpha = 0.1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(res.goalName, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                    Box(
                                        modifier = Modifier
                                            .background(CyberPurple.copy(alpha = 0.3f), CircleShape)
                                            .border(1.dp, CyberNeonCyan, CircleShape)
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text("${res.percentage}% Match", color = CyberNeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("Reasoning factors:", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                res.reason.forEach { factor ->
                                    Text(factor, color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = { viewModel.applySelectedGoal(res.goalName) },
                                    colors = ButtonDefaults.buttonColors(containerColor = CyberAlertPink),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text("Install Roadmap", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EnvironmentFormScreen(viewModel: MainViewModel, user: UserEntity?) {
    if (user == null) return

    val currentConfig = user.environment
    var sleepHours by remember { mutableStateOf(currentConfig.sleepHours) }
    var studyHours by remember { mutableStateOf(currentConfig.studyHours) }
    var stressLevel by remember { mutableStateOf(currentConfig.stressLevel.toFloat()) }
    var confidenceLevel by remember { mutableStateOf(currentConfig.confidenceLevel.toFloat()) }
    var anxietyLevel by remember { mutableStateOf(currentConfig.anxietyLevel.toFloat()) }
    var burnoutLevel by remember { mutableStateOf(currentConfig.burnoutLevel.toFloat()) }
    var familySupport by remember { mutableStateOf(currentConfig.familySupport) }
    var friendCircle by remember { mutableStateOf(currentConfig.friendCircle) }
    var consistency by remember { mutableStateOf(currentConfig.consistency.toFloat()) }
    var procrastination by remember { mutableStateOf(currentConfig.procrastination.toFloat()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                IconButton(onClick = { viewModel.navigateTo(Screen.Dashboard) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text("Temporal Habits Adjuster", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Your daily habits dictate how fast you build your future timeline. Adjusting these indicators immediately mutates the dream progress percentage and calculated stage indexes.",
                        fontSize = 13.sp,
                        color = CyberNeonCyan,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                item {
                    GlassCard {
                        Text("HEALTH & WORK LOAD REST TIME", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyberNeonPurpleGlow)
                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Sleep Hours: ${String.format("%.1f", sleepHours)} Hrs", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Slider(
                            value = sleepHours,
                            onValueChange = { sleepHours = it },
                            valueRange = 4.0f..10.0f,
                            colors = SliderDefaults.colors(thumbColor = CyberNeonCyan, activeTrackColor = CyberNeonCyan)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Study Hours: ${String.format("%.1f", studyHours)} Hrs", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Slider(
                            value = studyHours,
                            onValueChange = { studyHours = it },
                            valueRange = 1.0f..12.0f,
                            colors = SliderDefaults.colors(thumbColor = CyberNeonPurpleGlow, activeTrackColor = CyberNeonPurpleGlow)
                        )
                    }
                }

                item {
                    GlassCard {
                        Text("TIMELINE FOCUS PARAMETERS (1-10)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyberNeonCyan)
                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Consistency Index: ${consistency.toInt()}/10", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Slider(
                            value = consistency,
                            onValueChange = { consistency = it },
                            valueRange = 1f..10f,
                            steps = 9,
                            colors = SliderDefaults.colors(thumbColor = CyberNeonCyan, activeTrackColor = CyberNeonCyan)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Procrastination Index: ${procrastination.toInt()}/10", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Slider(
                            value = procrastination,
                            onValueChange = { procrastination = it },
                            valueRange = 1f..10f,
                            steps = 9,
                            colors = SliderDefaults.colors(thumbColor = CyberAlertPink, activeTrackColor = CyberAlertPink)
                        )
                    }
                }

                item {
                    GlassCard {
                        Text("SOUL INDICATORS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyberAlertPink)
                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Internal Stress Curve: ${stressLevel.toInt()}/10", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Slider(
                            value = stressLevel,
                            onValueChange = { stressLevel = it },
                            valueRange = 1f..10f,
                            steps = 9,
                            colors = SliderDefaults.colors(thumbColor = CyberAlertPink, activeTrackColor = CyberAlertPink)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Confidence Amplitude: ${confidenceLevel.toInt()}/10", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Slider(
                            value = confidenceLevel,
                            onValueChange = { confidenceLevel = it },
                            valueRange = 1f..10f,
                            steps = 9,
                            colors = SliderDefaults.colors(thumbColor = CyberNeonCyan, activeTrackColor = CyberNeonCyan)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Burnout Index: ${burnoutLevel.toInt()}/10", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Slider(
                            value = burnoutLevel,
                            onValueChange = { burnoutLevel = it },
                            valueRange = 1f..10f,
                            steps = 9,
                            colors = SliderDefaults.colors(thumbColor = CyberPurple, activeTrackColor = CyberPurple)
                        )
                    }
                }

                item {
                    GlassCard {
                        Text("ENVIRONMENT ENVIRONMENT CONDITIONS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Family Support Scale:", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("High", "Medium", "Low").forEach { s ->
                                Button(
                                    onClick = { familySupport = s },
                                    colors = ButtonDefaults.buttonColors(containerColor = if (familySupport == s) CyberNeonCyan else CyberGlassCardBg),
                                    border = BorderStroke(1.dp, if (familySupport == s) CyberNeonCyan else Color.White.copy(alpha = 0.2f)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(s, fontSize = 11.sp, color = if (familySupport == s) Color.Black else Color.White)
                                }
                            }
                        }

                        Text("Social Friend Circle Matrix:", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 6.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Motivating", "Neutral", "Distracting").forEach { c ->
                                Button(
                                    onClick = { friendCircle = c },
                                    colors = ButtonDefaults.buttonColors(containerColor = if (friendCircle == c) CyberNeonPurpleGlow else CyberGlassCardBg),
                                    border = BorderStroke(1.dp, if (friendCircle == c) CyberNeonPurpleGlow else Color.White.copy(alpha = 0.2f)),
                                    modifier = Modifier.weight(1f),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text(c, fontSize = 10.sp, color = if (friendCircle == c) Color.Black else Color.White)
                                }
                            }
                        }
                    }
                }

                item {
                    Button(
                        onClick = {
                            val cfg = EnvironmentConfig(
                                sleepHours = sleepHours,
                                studyHours = studyHours,
                                stressLevel = stressLevel.toInt(),
                                confidenceLevel = confidenceLevel.toInt(),
                                anxietyLevel = anxietyLevel.toInt(),
                                burnoutLevel = burnoutLevel.toInt(),
                                familySupport = familySupport,
                                friendCircle = friendCircle,
                                consistency = consistency.toInt(),
                                procrastination = procrastination.toInt()
                            )
                            viewModel.updateEnvironment(cfg)
                            viewModel.navigateTo(Screen.Dashboard)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberElectricBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("save_environment_button")
                    ) {
                        Text("MUTATE TIMELINE", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

@Composable
fun SkillsRoadmapScreen(viewModel: MainViewModel, user: UserEntity?) {
    if (user == null) return

    val learningPath = user.learningPath
    val categories = listOf("Foundation", "Intermediate", "Advanced", "Professional")
    var selectedStep by remember { mutableStateOf<RoadmapStep?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                IconButton(onClick = { viewModel.navigateTo(Screen.Dashboard) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text("Installed Future Timeline Roadmap", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Master checkpoints to automatically unlock milestones. Each completion immediately recalculates your chronos progression speed.",
                        fontSize = 13.sp,
                        color = CyberNeonCyan
                    )
                }

                categories.forEach { category ->
                    val stepsInCategory = learningPath.filter { it.category == category }
                    if (stepsInCategory.isNotEmpty()) {
                        item {
                            Text(
                                text = category.uppercase(),
                                color = when (category) {
                                    "Foundation" -> CyberNeonCyan
                                    "Intermediate" -> CyberNeonPurpleGlow
                                    "Advanced" -> CyberAlertPink
                                    else -> Color.Green
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.5.sp,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }

                        items(stepsInCategory) { step ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedStep = step },
                                colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                                border = BorderStroke(1.dp, if (step.completed) Color.Green.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.1f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Checkbox(
                                            checked = step.completed,
                                            onCheckedChange = { viewModel.toggleRoadmapTask(step.title) },
                                            colors = CheckboxDefaults.colors(checkedColor = Color.Green, checkmarkColor = Color.Black)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = step.title,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                text = step.description,
                                                color = Color.White.copy(alpha = 0.6f),
                                                fontSize = 11.sp,
                                                maxLines = 1,
                                                modifier = Modifier.padding(top = 2.dp)
                                            )
                                        }
                                    }
                                    IconButton(onClick = { selectedStep = step }) {
                                        Icon(Icons.Default.Info, contentDescription = "Syllabus details", tint = CyberNeonCyan)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Details Modal Dialog
        selectedStep?.let { step ->
            AlertDialog(
                onDismissRequest = { selectedStep = null },
                containerColor = CosmicSlate,
                icon = { Icon(Icons.Default.Star, contentDescription = "Roadmap Info", tint = CyberNeonCyan, modifier = Modifier.size(36.dp)) },
                title = { Text(step.title, color = Color.White, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center) },
                text = {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(2.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(step.description, color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                        
                        HorizontalDivider(color = Color.White.copy(alpha = 0.15f))
                        
                        Row {
                            Text("🎓 Curated syllabus course: ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyberNeonCyan)
                            Text(step.course, fontSize = 12.sp, color = Color.White)
                        }

                        Row {
                            Text("📖 Practice link: ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyberNeonCyan)
                            Text(step.practice, fontSize = 12.sp, color = Color.White)
                        }

                        Row {
                            Text("🛠 High fidelity project: ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyberNeonCyan)
                            Text(step.miniProject, fontSize = 12.sp, color = Color.White)
                        }

                        Text("Explore on YouTube: \"${step.youtube}\"", color = CyberNeonPurpleGlow, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = CyberNeonCyan),
                        onClick = { selectedStep = null }
                    ) {
                        Text("Portal sync", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}

@Composable
fun ChatScreen(viewModel: MainViewModel, user: UserEntity?, isGuide: Boolean) {
    if (user == null) return

    val chats = if (isGuide) user.futureGuideChats else user.shadowSelfChats
    val isChatLoading by viewModel.isChatLoading.collectAsStateWithLifecycle()
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Scroll to the bottom as soon as list changes
    LaunchedEffect(chats.size) {
        if (chats.isNotEmpty()) {
            listState.animateScrollToItem(chats.size - 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                IconButton(onClick = { viewModel.navigateTo(Screen.Dashboard) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text(
                    text = if (isGuide) "🌅 Future Guide Portal" else "🌪 Shadow Self Portal",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Description Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isGuide) CyberPurple.copy(alpha = 0.15f) else CyberAlertPink.copy(alpha = 0.1f)
                ),
                border = BorderStroke(1.dp, if (isGuide) CyberNeonCyan.copy(alpha = 0.4f) else CyberAlertPink.copy(alpha = 0.4f))
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isGuide) Icons.Default.ThumbUp else Icons.Default.Warning,
                        contentDescription = "AvatarInfo",
                        tint = if (isGuide) CyberNeonCyan else CyberAlertPink,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (isGuide) {
                            "Communicating with you at age 35. I succeeded in becoming ${user.goal}. Send me any doubt or prompt!"
                        } else {
                            "Communicating with your failed alternative timeline. I stopped studying at progress ${user.dreamProgress}%. Ask me how I failed."
                        },
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        lineHeight = 14.sp
                    )
                }
            }

            // Chat Messages List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(chats) { msg ->
                    val isMe = msg.sender == "user"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 12.dp,
                                        topEnd = 12.dp,
                                        bottomStart = if (isMe) 12.dp else 4.dp,
                                        bottomEnd = if (isMe) 4.dp else 12.dp
                                    )
                                )
                                .background(
                                    if (isMe) CyberElectricBlue
                                    else if (isGuide) CyberPurple.copy(alpha = 0.4f)
                                    else Color.DarkGray.copy(alpha = 0.8f)
                                )
                                .border(
                                    1.dp,
                                    if (isMe) CyberNeonCyan.copy(alpha = 0.4f)
                                    else if (isGuide) CyberNeonPurpleGlow.copy(alpha = 0.3f)
                                    else CyberAlertPink.copy(alpha = 0.3f),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp)
                                .widthIn(max = 260.dp)
                        ) {
                            Column {
                                Text(
                                    text = if (isMe) "ME" else if (isGuide) "FUTURE GUIDE" else "SHADOW SELF",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isMe) CyberNeonCyan else if (isGuide) CyberNeonCyan else CyberAlertPink,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = msg.message,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }

                if (isChatLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .background(CyberGlassCardBg, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text("Syncing temporal signals...", color = CyberNeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Quick floating prompts
            val suggestions = if (isGuide) {
                listOf("How did you stay consistent?", "Best study technique?", "Is our goal worth it?")
            } else {
                listOf("When did you stop trying?", "What was your biggest mistake?", "Tell me about my consistency.")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                suggestions.forEach { suggestion ->
                    Box(
                        modifier = Modifier
                            .background(CyberGlassCardBg, RoundedCornerShape(50))
                            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(50))
                            .clickable {
                                if (isGuide) {
                                    viewModel.sendMessageToFutureGuide(suggestion)
                                } else {
                                    viewModel.sendMessageToShadowSelf(suggestion)
                                }
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(suggestion, color = CyberNeonCyan, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Reply composition panel
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Transmit mental signal...") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberNeonCyan,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                        focusedTextColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input")
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (messageText.trim().isNotEmpty()) {
                            val txt = messageText
                            messageText = ""
                            if (isGuide) {
                                viewModel.sendMessageToFutureGuide(txt)
                            } else {
                                viewModel.sendMessageToShadowSelf(txt)
                            }
                        }
                    },
                    modifier = Modifier
                        .background(CyberNeonCyan, CircleShape)
                        .size(48.dp)
                        .testTag("send_button")
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.Black)
                }
            }
        }
    }
}

@Composable
fun WeeklyHistoryScreen(viewModel: MainViewModel, user: UserEntity?) {
    if (user == null) return

    val history = user.weeklyHistory
    val isReflectionLoading by viewModel.isReflectionLoading.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                IconButton(onClick = { viewModel.navigateTo(Screen.Dashboard) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text("Timeline Analytics History", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Text(
                text = "Chronos logs register the historical delta of your Dream score after every habit adjustment study, keeping you honest to your timeline trajectory.",
                fontSize = 13.sp,
                color = CyberNeonCyan,
                lineHeight = 18.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Trigger simulated Evaluation
            Button(
                onClick = { viewModel.triggerWeeklyReflectionSim() },
                colors = ButtonDefaults.buttonColors(containerColor = CyberAlertPink),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .height(48.dp)
            ) {
                if (isReflectionLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("TRIGGER AI CHRONOS EVALUATION", fontWeight = FontWeight.Bold)
                }
            }

            if (history.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(CyberGlassCardBg, RoundedCornerShape(16.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No history recorded yet.\nClick 'Trigger Evaluation' to record today's timeline checkpoint!",
                        color = Color.White.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(history.reversed()) { log ->
                        GlassCard {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(log.weekDate, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CyberNeonCyan)
                                Box(
                                    modifier = Modifier
                                        .background(CyberPurple.copy(alpha = 0.2f), CircleShape)
                                        .border(1.dp, CyberNeonPurpleGlow, CircleShape)
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text("Score: ${log.progressScore}%", color = CyberNeonPurpleGlow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            Text("Classification: ${log.stage}", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = log.reason,
                                color = Color.White,
                                fontSize = 13.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// 🧬 FLAGSHIP COMPOSABLES: PERSONAL LEGEND, BOOK OF YOU, JOURNAL, AUDIT, SIM
// =========================================================================

@Composable
fun PersonalLegendScreen(viewModel: MainViewModel, user: UserEntity?) {
    if (user == null) return

    val isReflectionLoading by viewModel.isReflectionLoading.collectAsStateWithLifecycle()
    val isTtsSpeaking by viewModel.isTtsSpeaking.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                IconButton(onClick = { viewModel.navigateTo(Screen.Dashboard) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text("DNA Identity: Personal Legend", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Text(
                text = "YOUR SOUL INTEGRATION COEFFICIENT",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = CyberNeonCyan,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Text(
                text = "The Personal Legend Engine parses your custom interests, core metrics, daily performance curves, and active goals to weave the narrative of who you are and where your timeline converges.",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.7f),
                lineHeight = 18.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Button to Recompile
            Button(
                onClick = { viewModel.generatePersonalLegend() },
                colors = ButtonDefaults.buttonColors(containerColor = CyberPulsePurpleGlow()),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .height(48.dp)
                    .testTag("recompile_legend_button")
            ) {
                if (isReflectionLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Refresh, contentDescription = "Sync", tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("RE-COMPILE PERSONAL LEGEND", fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (user.personalLegend.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(CyberGlassCardBg, RoundedCornerShape(16.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Your narrative is currently unparsed.\nTap 'RE-COMPILE' to sync your cosmic profile!",
                        color = Color.White.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp
                    )
                }
            } else {
                GlassCard(borderGlowColor = CyberNeonCyan) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DESTINY SEQUENCE: VERIFIED",
                            fontSize = 11.sp,
                            color = Color.Green,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        // TTS Voice Speech Control
                        Button(
                            onClick = {
                                if (isTtsSpeaking) viewModel.stopSpeaking()
                                else viewModel.speakText(user.personalLegend)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = if (isTtsSpeaking) CyberAlertPink else CyberElectricBlue),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text(if (isTtsSpeaking) "STOP AUDIO" else "LISTEN REFLECTION", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = user.personalLegend,
                        color = Color.White,
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        fontFamily = FontFamily.SansSerif
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Primary Pairing: Curiosity & Analytical Mastery\nIdeal Career Path: ${user.goal}\nGrowth Alignment Status: STABLE",
                        color = CyberNeonCyan,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun BookOfYouScreen(viewModel: MainViewModel, user: UserEntity?) {
    if (user == null) return

    val chapters = viewModel.compileBookChapters(user)
    val isTtsSpeaking by viewModel.isTtsSpeaking.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                IconButton(onClick = { viewModel.navigateTo(Screen.Dashboard) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text("Autobiography: Book of You", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Text(
                text = "UNFOLDING LIFE NARRATIVE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = CyberNeonPurpleGlow,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Text(
                text = "A living autogenerated chronology of your timeline. Chapters unlock as you tick off milestones and expand your Dream score.",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.7f),
                lineHeight = 18.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(chapters) { cat ->
                    if (cat.isLocked) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.02f)),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                        ) {
                            Row(
                                modifier = Modifier.padding(18.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(Color.White.copy(alpha = 0.05f), CircleShape)
                                        .padding(12.dp)
                                ) {
                                    Icon(Icons.Default.Warning, contentDescription = "Locked", tint = Color.Gray, modifier = Modifier.size(24.dp))
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(cat.title, color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text("LOCKED TIMELINE SEGMENT", color = CyberAlertPink, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text(cat.unlockedDate, color = Color.White.copy(alpha = 0.4f), fontSize = 11.sp, modifier = Modifier.padding(top = 2.dp))
                                }
                            }
                        }
                    } else {
                        GlassCard(borderGlowColor = CyberNeonPurpleGlow) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(cat.title, color = CyberNeonPurpleGlow, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.sp)
                                    Text(cat.subTitle, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                                }

                                Button(
                                    onClick = { viewModel.speakText("${cat.title}. ${cat.subTitle}. ${cat.content}") },
                                    colors = ButtonDefaults.buttonColors(containerColor = CyberElectricBlue),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                    modifier = Modifier.height(26.dp)
                                ) {
                                    Text("LISTEN", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = cat.content,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "✓ Timeline synchronized properly",
                                color = Color.Green,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

@Composable
fun JournalScreen(viewModel: MainViewModel, user: UserEntity?) {
    if (user == null) return

    val records = user.journalEntries
    val isChatLoading by viewModel.isChatLoading.collectAsStateWithLifecycle()

    var reflection by remember { mutableStateOf("") }
    var emotion by remember { mutableStateOf("Focused") }
    var learned by remember { mutableStateOf("") }
    var struggled by remember { mutableStateOf("") }
    var gratitude by remember { mutableStateOf("") }

    var expandedForm by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                IconButton(onClick = { viewModel.navigateTo(Screen.Dashboard) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text("FutureMe Echo Journal", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Text(
                text = "REFLECTIVE TIMELINE FEEDBACK",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = CyberNeonCyan,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Text(
                text = "This is not a traditional static diary. Write down your struggles and successes, and watch your Future Guide and Shadow Self leave instant twin evaluation reviews directly inside the entry.",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f),
                lineHeight = 16.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Overwrite form collapsible
            Button(
                onClick = { expandedForm = !expandedForm },
                colors = ButtonDefaults.buttonColors(containerColor = CyberElectricBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .height(40.dp)
            ) {
                Text(if (expandedForm) "✕ CLOSE REFLECTION BOX" else "✍ LOG TODAY'S REFLECTION", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            AnimatedVisibility(visible = expandedForm) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                    border = BorderStroke(1.dp, CyberNeonCyan.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(14.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text("Log daily details below:", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = reflection,
                            onValueChange = { reflection = it },
                            placeholder = { Text("What did you work on and how did it feel?") },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, focusedBorderColor = CyberNeonCyan),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Text("Current Emotion: ", color = Color.White, fontSize = 12.sp, modifier = Modifier.weight(1f))
                            val emotionsList = listOf("Excited", "Focused", "Calm", "Anxious", "Stuck")
                            emotionsList.forEach { em ->
                                Button(
                                    onClick = { emotion = em },
                                    colors = ButtonDefaults.buttonColors(containerColor = if (emotion == em) CyberNeonCyan else Color.Transparent),
                                    contentPadding = PaddingValues(horizontal = 6.dp),
                                    modifier = Modifier
                                        .height(28.dp)
                                        .padding(horizontal = 2.dp)
                                ) {
                                    Text(em, fontSize = 9.sp, color = if (emotion == em) Color.Black else Color.White)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = learned,
                            onValueChange = { learned = it },
                            placeholder = { Text("What crucial thing did you learn?") },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, focusedBorderColor = CyberNeonCyan),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = struggled,
                            onValueChange = { struggled = it },
                            placeholder = { Text("What are you currently struggling with?") },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, focusedBorderColor = CyberNeonCyan),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = gratitude,
                            onValueChange = { gratitude = it },
                            placeholder = { Text("One thing you are grateful for today?") },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, focusedBorderColor = CyberNeonCyan),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (reflection.isNotEmpty()) {
                                    viewModel.addJournalEntry(reflection, emotion, learned, struggled, gratitude)
                                    reflection = ""
                                    learned = ""
                                    struggled = ""
                                    gratitude = ""
                                    expandedForm = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberAlertPink),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("TRANSMIT TO TIMELINE", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // List of previous reflections
            if (records.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(CyberGlassCardBg, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No journal reflections recorded yet.", color = Color.White.copy(alpha = 0.5f))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(records.reversed()) { jot ->
                        GlassCard {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(jot.date, fontWeight = FontWeight.Bold, color = CyberNeonCyan, fontSize = 14.sp)
                                Box(
                                    modifier = Modifier
                                        .background(CyberPurple.copy(alpha = 0.3f), CircleShape)
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(jot.emotionalState, color = CyberNeonPurpleGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(jot.content, color = Color.White, fontSize = 13.sp, lineHeight = 18.sp)

                            Spacer(modifier = Modifier.height(6.dp))

                            Row {
                                Text("💡 Learned: ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyberNeonCyan)
                                Text(jot.learned, fontSize = 12.sp, color = Color.White)
                            }
                            Row {
                                Text("⚠️ Stuck: ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyberAlertPink)
                                Text(jot.struggled, fontSize = 12.sp, color = Color.White)
                            }
                            Row {
                                Text("🙏 Gratitude: ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Green)
                                Text(jot.gratitude, fontSize = 12.sp, color = Color.White)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = { viewModel.speakText(jot.content) },
                                colors = ButtonDefaults.buttonColors(containerColor = CyberElectricBlue),
                                modifier = Modifier.height(24.dp).align(Alignment.End),
                                contentPadding = PaddingValues(horizontal = 10.dp)
                            ) {
                                Text("SPEAK NOTES", fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RealityCheckScreen(viewModel: MainViewModel, user: UserEntity?) {
    if (user == null) return

    val reports = user.realityCheckReports
    val isReflectionLoading by viewModel.isReflectionLoading.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                IconButton(onClick = { viewModel.navigateTo(Screen.Dashboard) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text("Reality Check Audit Reports", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Text(
                text = "ACTION VS INTENT AUDIT INDEX",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = CyberAlertPink,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Text(
                text = "Monthly comparative review comparing planned study hours and achievements with actual verified records. Evaluates the physical consistency delta.",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f),
                lineHeight = 16.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Button(
                onClick = { viewModel.generateRealityCheck() },
                colors = ButtonDefaults.buttonColors(containerColor = CyberAlertPink),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
                    .height(48.dp)
            ) {
                if (isReflectionLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("TRIGGER INTENT GAP AUDIT", fontWeight = FontWeight.Bold)
                }
            }

            if (reports.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(CyberGlassCardBg, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No audit reports calculated yet.", color = Color.White.copy(alpha = 0.5f))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(reports.reversed()) { rep ->
                        GlassCard {
                            Text(rep.month, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = CyberNeonCyan)
                            Text("Goal Focus: ${rep.goalTitle}", fontSize = 11.sp, color = Color.White.copy(alpha = 0.5f))

                            Spacer(modifier = Modifier.height(10.dp))

                            // Action Completion rate bar
                            Text("Roadmap Completion rate: ${rep.actualCompletion}% (Goal: ${rep.plannedCompletion}%)", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            Box(modifier = Modifier.fillMaxWidth().height(8.dp).background(Color.White.copy(alpha = 0.1f), CircleShape)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(rep.actualCompletion / 100f)
                                        .background(CyberNeonPurpleGlow, CircleShape)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Hour baseline completion rate
                            Text("Verified study hours: ${rep.actualHours} hrs (Commitment: ${rep.plannedHours} hrs)", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            Box(modifier = Modifier.fillMaxWidth().height(8.dp).background(Color.White.copy(alpha = 0.1f), CircleShape)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(rep.actualHours / 120f)
                                        .background(CyberNeonCyan, CircleShape)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Double insights
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White.copy(alpha = 0.03f), RoundedCornerShape(8.dp))
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row {
                                    Icon(Icons.Default.ThumbUp, contentDescription = "Guide", tint = CyberNeonCyan, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text("Future Guide Insight:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyberNeonCyan)
                                        Text(rep.insightFromGuide, fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f), lineHeight = 15.sp)
                                    }
                                }
                                Row {
                                    Icon(Icons.Default.Warning, contentDescription = "Shadow", tint = CyberAlertPink, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text("Shadow Warning:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyberAlertPink)
                                        Text(rep.warningFromShadow, fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f), lineHeight = 15.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UniverseMapScreen(viewModel: MainViewModel, user: UserEntity?) {
    if (user == null) return

    val simSleep by viewModel.simSleepHours.collectAsStateWithLifecycle()
    val simStudy by viewModel.simStudyHours.collectAsStateWithLifecycle()
    val simProc by viewModel.simProcrastination.collectAsStateWithLifecycle()
    val simCons by viewModel.simConsistency.collectAsStateWithLifecycle()

    val simulationList by viewModel.simulatedFutures.collectAsStateWithLifecycle()

    var expandedBranchTitle by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                IconButton(onClick = { viewModel.navigateTo(Screen.Dashboard) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text("Universe Simulator & Map", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            // Simulator Controls Slider Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                border = BorderStroke(1.dp, CyberNeonCyan.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "🧬 LIVE COGNITIVE SIMULATOR SLIDERS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberNeonCyan,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Text("SleepHours: ${String.format("%.1f", simSleep)}h", color = Color.White, fontSize = 11.sp, modifier = Modifier.width(105.dp))
                        Slider(
                            value = simSleep,
                            onValueChange = { viewModel.simSleepHours.value = it },
                            valueRange = 4.0f..10.0f,
                            modifier = Modifier.weight(1f),
                            colors = SliderDefaults.colors(thumbColor = CyberNeonCyan, activeTrackColor = CyberNeonCyan)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Text("StudyHours: ${String.format("%.1f", simStudy)}h", color = Color.White, fontSize = 11.sp, modifier = Modifier.width(105.dp))
                        Slider(
                            value = simStudy,
                            onValueChange = { viewModel.simStudyHours.value = it },
                            valueRange = 1.0f..12.0f,
                            modifier = Modifier.weight(1f),
                            colors = SliderDefaults.colors(thumbColor = CyberNeonPurpleGlow, activeTrackColor = CyberNeonPurpleGlow)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Text("Procrastination: $simProc/10", color = Color.White, fontSize = 11.sp, modifier = Modifier.width(105.dp))
                        Slider(
                            value = simProc.toFloat(),
                            onValueChange = { viewModel.simProcrastination.value = it.toInt() },
                            valueRange = 1f..10f,
                            steps = 9,
                            modifier = Modifier.weight(1f),
                            colors = SliderDefaults.colors(thumbColor = CyberAlertPink, activeTrackColor = CyberAlertPink)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Text("Consistency: $simCons/10", color = Color.White, fontSize = 11.sp, modifier = Modifier.width(105.dp))
                        Slider(
                            value = simCons.toFloat(),
                            onValueChange = { viewModel.simConsistency.value = it.toInt() },
                            valueRange = 1f..10f,
                            steps = 9,
                            modifier = Modifier.weight(1f),
                            colors = SliderDefaults.colors(thumbColor = Color.Green, activeTrackColor = Color.Green)
                        )
                    }
                }
            }

            Text(
                text = "🌌 UNIVERSE MAP: ACTIVE BRANCH MUTATIONS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.7f),
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(simulationList) { branch ->
                    val isExpanded = expandedBranchTitle == branch.title
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedBranchTitle = if (isExpanded) "" else branch.title },
                        colors = CardDefaults.cardColors(
                            containerColor = if (branch.title.contains("Shadow")) CyberAlertPink.copy(alpha = 0.07f)
                            else if (branch.title.contains("Achiever")) CyberPurple.copy(alpha = 0.12f)
                            else CyberGlassCardBg
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (branch.title.contains("Shadow")) CyberAlertPink.copy(alpha = 0.3f)
                            else if (branch.title.contains("Achiever")) CyberNeonCyan.copy(alpha = 0.3f)
                            else Color.White.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                if (branch.title.contains("Shadow")) CyberAlertPink.copy(alpha = 0.2f)
                                                else if (branch.title.contains("Achiever")) CyberNeonCyan.copy(alpha = 0.2f)
                                                else Color.White.copy(alpha = 0.1f),
                                                CircleShape
                                            )
                                            .padding(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (branch.title.contains("Shadow")) Icons.Default.Warning else Icons.Default.Star,
                                            contentDescription = "Map icon",
                                            tint = if (branch.title.contains("Shadow")) CyberAlertPink else CyberNeonCyan,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = branch.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (branch.title.contains("Shadow")) CyberAlertPink else if (branch.title.contains("Achiever")) CyberNeonCyan else Color.White
                                    )
                                }
                                Icon(
                                    imageVector = if (isExpanded) Icons.Default.ArrowBack else Icons.Default.PlayArrow,
                                    contentDescription = "Ex",
                                    tint = Color.White.copy(alpha = 0.4f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(branch.careerTitle, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.8f))

                            if (isExpanded) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = branch.description,
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "📈 Metrics:\n${branch.metrics}",
                                    color = CyberNeonPurpleGlow,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(6.dp))
                                Text("💡 Dynamic Pros:", color = Color.Green, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                branch.dynamicPros.forEach { pro ->
                                    Text("• $pro", color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp)
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text("⚠️ Dynamic Cons:", color = CyberAlertPink, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                branch.dynamicCons.forEach { con ->
                                    Text("• $con", color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

// Special custom glowing button helper
@Composable
fun CyberPulsePurpleGlow(): Color {
    val infiniteTransition = rememberInfiniteTransition(label = "StarryGlow")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "StarPulse"
    )
    return CyberNeonPurpleGlow.copy(alpha = alpha)
}

// =========================================================================
// 🌌 EXPERIENTIAL SCREENS: TIME CAPSULE, CONSTELLATION, MIRROR, DOCUMENTARY, CHALLENGES
// =========================================================================

@Composable
fun TimeCapsuleScreen(viewModel: MainViewModel, user: UserEntity?) {
    if (user == null) return
    var message by remember { mutableStateOf("") }
    var selectedReason by remember { mutableStateOf("1 Month") }
    var customDaysText by remember { mutableStateOf("30") }
    val reasons = listOf("1 Month", "3 Months", "6 Months", "1 Year", "Custom Date")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.navigateTo(Screen.Dashboard) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "FUTUREME TIME CAPSULE",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Text(
                text = "Cast a message to your future self through the chronos field. Choose an unsealing date. Once locked, it can only be recovered when the timeline converges.",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f),
                lineHeight = 16.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Compose Card
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                borderGlowColor = CyberNeonCyan
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "WRITE YOUR TEMPORAL WHISPER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberNeonCyan,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .testTag("time_capsule_message_field"),
                        placeholder = { 
                            Text("Dear Future Me,\nI have doubts, but I want to believe I can become a true expert. Are you proud of who I became?", color = Color.White.copy(alpha = 0.4f), fontSize = 12.sp) 
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberNeonCyan,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                            focusedContainerColor = Color.Black.copy(alpha = 0.3f),
                            unfocusedContainerColor = Color.Black.copy(alpha = 0.3f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "WHEN SHOULD THIS CAPSULE UNSEAL?",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Options list
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        reasons.forEach { r ->
                            val isSelected = selectedReason == r
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (isSelected) CyberNeonCyan.copy(alpha = 0.2f) else CyberGlassCardBg,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) CyberNeonCyan else Color.White.copy(alpha = 0.1f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedReason = r }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = r,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) CyberNeonCyan else Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    if (selectedReason == "Custom Date") {
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = customDaysText,
                            onValueChange = { customDaysText = it },
                            label = { Text("Days to Lock (1-365)", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberNeonCyan,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (message.isNotBlank()) {
                                val customDays = customDaysText.toIntOrNull() ?: 30
                                viewModel.addTimeCapsule(message, selectedReason, customDays)
                                message = ""
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("seal_capsule_button"),
                        enabled = message.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberNeonCyan),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = "Lock", tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("SEAL IN TEMPORAL VAULT", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 13.sp)
                    }
                }
            }

            // Sealed Capsules Listing
            Text(
                text = "📁 ACTIVE TEMPORAL VAULTS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.7f),
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (user!!.timeCapsules.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CyberGlassCardBg, RoundedCornerShape(12.dp))
                        .padding(30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Your temporal vault is empty. No messages cast yet.",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                user!!.timeCapsules.reversed().forEach { cap ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                        border = BorderStroke(
                            1.dp,
                            if (cap.isUnlocked) Color.Green.copy(alpha = 0.4f) else CyberNeonPurpleGlow.copy(alpha = 0.25f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (cap.isUnlocked) Icons.Default.CheckCircle else Icons.Default.Lock,
                                        contentDescription = "Status",
                                        tint = if (cap.isUnlocked) Color.Green else CyberNeonPurpleGlow,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (cap.isUnlocked) "UNSEALED" else "LOCKED TEMPORAL CAPSULE",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (cap.isUnlocked) Color.Green else CyberNeonPurpleGlow
                                    )
                                }
                                Text(
                                    text = "Locked on: ${cap.createdDate}",
                                    fontSize = 10.sp,
                                    color = Color.White.copy(alpha = 0.5f)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            if (cap.isUnlocked) {
                                Text(
                                    text = cap.message,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Black.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                                        .padding(12.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = "Whisper contents are digitally scrambled to prevent timeline contamination.",
                                            color = Color.White.copy(alpha = 0.5f),
                                            fontSize = 11.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Scheduled Reopens: ${cap.unlockDate} (${cap.unlockReason})",
                                            fontWeight = FontWeight.Bold,
                                            color = CyberNeonCyan,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Force unlock developer portal trigger
                                Button(
                                    onClick = { viewModel.forceUnlockTimeCapsule(cap.id) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(36.dp)
                                        .testTag("force_decrypt_${cap.id}"),
                                    colors = ButtonDefaults.buttonColors(containerColor = CyberAlertPink.copy(alpha = 0.15f)),
                                    border = BorderStroke(1.dp, CyberAlertPink),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = "Force", tint = CyberAlertPink, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("ACCELERATE TIME (FORCE UNLOCK)", color = CyberAlertPink, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConstellationScreen(viewModel: MainViewModel, user: UserEntity?) {
    if (user == null) return

    val discipline = (user.environment.consistency * 10).coerceIn(10, 100)
    val curiosity = (user.interests.size * 15 + user.skills.size * 10).coerceIn(15, 100)
    val confidence = user.dreamProgress.coerceIn(10, 100)
    val leadership = (if (user.classLevel.contains("Senior") || user.classLevel.contains("Graduate")) 85 else 55).coerceIn(10, 100)
    val communication = (user.journalEntries.size * 20 + user.futureGuideChats.size * 5).coerceIn(10, 100)

    val dimensions = listOf(
        ConstellationDimension(
            name = "Curiosity",
            score = curiosity,
            x = 0.5f,
            y = 0.2f,
            description = "Curiosity prevents timeline stagnation. Your exploration level is $curiosity% based on your ${user.interests.size} declared career interest configurations.",
            wisdom = "Curiosity is the warp drive. It allows you to visualize alternative pathways when traditional routes appear completely blocked. Never stop experimenting with hard problems."
        ),
        ConstellationDimension(
            name = "Discipline",
            score = discipline,
            x = 0.8f,
            y = 0.5f,
            description = "Discipline forms the stable gravitational pull of your trajectory. Level paced at $discipline% matched directly by your work consistency level.",
            wisdom = "Aspirational vision is only draft ink; consistent effort is the physical scaffolding of your thirty-five-year-old reality. Small routines compound into galaxies of masterwork."
        ),
        ConstellationDimension(
            name = "Confidence",
            score = confidence,
            x = 0.2f,
            y = 0.5f,
            description = "Confidence prevents timeline divergence into regret. Currently synchronized at $confidence% matching your ultimate timeline progress.",
            wisdom = "Do not wait for formal permission to believe you can succeed. Confidence is a daily conviction to stand in the storm, knowing we both have what it takes."
        ),
        ConstellationDimension(
            name = "Leadership",
            score = leadership,
            x = 0.5f,
            y = 0.8f,
            description = "Leadership extends your scope of influence, reflecting a score of $leadership% aligning with your clinical development stage.",
            wisdom = "The greatest professionals carry others forward. As you acquire engineering and strategic proficiency, always stretch your hand back along the timeline to support those starting."
        ),
        ConstellationDimension(
            name = "Communication",
            score = communication,
            x = 0.5f,
            y = 0.5f,
            description = "Communication bridges your conceptual mind with collaborative execution. Paced at $communication% backed by your journal entries.",
            wisdom = "Your ability to compress deep technical concepts into plain human language is your highest leverage. Documenting weekly logs aligns the creative and analytical quadrants of your mind."
        )
    )

    var selectedDim by remember { mutableStateOf<ConstellationDimension?>(dimensions[0]) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.navigateTo(Screen.Dashboard) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "MY CHARACTER CONSTELLATION",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Text(
                text = "Gaze into the starmap representing your core character pillars. As you study, make promises, and write journals, the stars expand and brighten, weaving a stellar guardian.",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.7f),
                lineHeight = 15.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Star Map Canvas Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                    .border(1.dp, CyberNeonPurpleGlow.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Connect all points to make a constellation polygon
                    val points = dimensions.map { it.x * w to it.y * h }
                    
                    // Draw connections
                    for (i in points.indices) {
                        for (j in i + 1 until points.size) {
                            drawLine(
                                color = CyberNeonCyan.copy(alpha = 0.15f),
                                start = Offset(points[i].first, points[i].second),
                                end = Offset(points[j].first, points[j].second),
                                strokeWidth = 1.dp.toPx()
                            )
                        }
                    }
                }

                // Layout star icons on top at relative offsets
                dimensions.forEach { dim ->
                    val isSel = selectedDim?.name == dim.name
                    Box(
                        modifier = Modifier
                            .align(
                                androidx.compose.ui.BiasAlignment(
                                    horizontalBias = (dim.x * 2) - 1f,
                                    verticalBias = (dim.y * 2) - 1f
                                )
                            )
                            .size(if (isSel) 44.dp else 34.dp)
                            .background(
                                if (isSel) CyberNeonPurpleGlow.copy(alpha = 0.35f) else Color.White.copy(alpha = 0.05f),
                                CircleShape
                            )
                            .border(
                                1.5.dp,
                                if (isSel) CyberNeonCyan else Color.White.copy(alpha = 0.2f),
                                CircleShape
                            )
                            .clickable { selectedDim = dim }
                            .testTag("star_${dim.name}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = dim.name,
                            tint = if (isSel) CyberNeonCyan else Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(if (isSel) 18.dp else 12.dp)
                        )
                    }
                }

                Text(
                    text = "🌌 STELLAR NAV-MAP",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.4f),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Wisdom Detail Card
            selectedDim?.let { dim ->
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    borderGlowColor = CyberNeonPurpleGlow
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "✦ PILLAR OF ${dim.name.uppercase()}",
                                    fontWeight = FontWeight.ExtraBold,
                                    color = CyberNeonCyan,
                                    fontSize = 15.sp,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "LEVEL: ${dim.score}%",
                                    fontWeight = FontWeight.Bold,
                                    color = CyberNeonPurpleGlow,
                                    fontSize = 13.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = dim.description,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "💌 TEMPORAL WISDOM MESSAGE FROM FUTURE SELF:",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "\"${dim.wisdom}\"",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                lineHeight = 16.sp
                            )
                        }

                        Column(modifier = Modifier.padding(top = 16.dp)) {
                            // Display progress bar
                            LinearProgressIndicator(
                                progress = { dim.score / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = CyberNeonCyan,
                                trackColor = Color.White.copy(alpha = 0.1f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Build skills and consistency to strengthen this stellar coordinate.",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Orbit of Support Panel (Integrated)
            Text(
                text = "👥 ORBIT OF SUPPORT CIRCLE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.7f),
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            val relationshipsLoading by viewModel.isRelationshipsLoading.collectAsStateWithLifecycle()
            val relationshipText by viewModel.relationshipsAssessment.collectAsStateWithLifecycle()

            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderGlowColor = Color.Green
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("The Gravity of Alignment", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(
                                "Family Support: ${user!!.environment.familySupport} | Friends Peer Circle: ${user!!.environment.friendCircle}",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 11.sp
                            )
                        }
                        Button(
                            onClick = { viewModel.generateRelationshipsAssessment() },
                            modifier = Modifier
                                .height(32.dp)
                                .testTag("assess_relationships_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Green.copy(alpha = 0.2f)),
                            border = BorderStroke(1.dp, Color.Green),
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Text("ASSESS ALIGNMENT", color = Color.Green, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }

                    if (relationshipsLoading) {
                        Spacer(modifier = Modifier.height(12.dp))
                        CircularProgressIndicator(
                            color = Color.Green,
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    } else if (relationshipText != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = relationshipText!!,
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 11.sp,
                                lineHeight = 15.sp,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}

data class ConstellationDimension(
    val name: String,
    val score: Int,
    val x: Float,
    val y: Float,
    val description: String,
    val wisdom: String
)

@Composable
fun ComparisonRow(label: String, currentVal: Int, potentialVal: Int, color: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            Row {
                Text("Current: ", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
                Text("$currentVal% ", color = color, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                Text(" | ", color = Color.White.copy(alpha = 0.3f), fontSize = 11.sp)
                Text("Potential: ", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
                Text("$potentialVal%", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(4.dp))
        ) {
            // Draw potential background (subtle gradient/color)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(potentialVal / 100f)
                    .background(color.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                    .border(0.5.dp, color.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
            )
            // Draw current on top
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(currentVal / 100f)
                    .background(color, RoundedCornerShape(4.dp))
            )
        }
    }
}

@Composable
fun MirrorQuestionScreen(viewModel: MainViewModel, user: UserEntity?) {
    if (user == null) return
    var answer by remember { mutableStateOf("") }
    
    val mirrorQuestions = listOf(
        "If your 10-year-old self met you today, would they be proud of who you've become?",
        "Are your current habits creating a future you once dreamed about, or a timeline of repetitive loop?",
        "What is the single most constructive piece of advice your 35-year-old self would yell to you right now?",
        "If you stripped away all status labels, classes, and wealth, what core of your identity still remains?",
        "What is a silent promise you forgot you made to yourself when you were younger?"
    )

    // Unanswered question matching
    val unansQuestions = mirrorQuestions.filterNot { q ->
        user.mirrorReflections.any { it.question == q }
    }
    val currentQuestion = unansQuestions.firstOrNull() ?: mirrorQuestions.random()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.navigateTo(Screen.Dashboard) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "THE MIRROR OF POTENTIAL",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Text(
                text = "Looking in the mirror reveals more than physical reflection—it shows potential. Tackle powerful, non-metric prompts that examine identity, purpose, and growth.",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.7f),
                lineHeight = 15.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Current Active Prompt Card
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                borderGlowColor = CyberAlertPink
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "THE MIRROR PROMPT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberAlertPink,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text(
                        text = "\"$currentQuestion\"",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = answer,
                        onValueChange = { answer = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .testTag("reflection_answer_field"),
                        placeholder = { Text("Peer deeply. Write your honest statement...", color = Color.White.copy(alpha = 0.4f), fontSize = 12.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberAlertPink,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                            focusedContainerColor = Color.Black.copy(alpha = 0.25f),
                            unfocusedContainerColor = Color.Black.copy(alpha = 0.25f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (answer.isNotBlank()) {
                                viewModel.logMirrorAnswer(currentQuestion, answer)
                                answer = ""
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("confront_mirror_button"),
                        enabled = answer.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberAlertPink)
                    ) {
                        Text("CONFRONT TRUTH", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ⚖️ REALITY VS POTENTIAL HARMONIZER
            Text(
                text = "⚖️ CURRENT REALITY vs POTENTIAL TIMELINE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.7f),
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                borderGlowColor = CyberNeonCyan
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "THE DUALITY OF SELF",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberNeonCyan,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Text(
                        text = "Your potential timeline is NOT a standard designed to make you feel guilty. It is a magnetic pulling force—the version of you that is fully realized if you choose weekly discipline.",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.65f),
                        lineHeight = 15.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Dimension 1: Work Consistency
                    val currentConsistency = (user.environment.consistency * 10).coerceIn(10, 100)
                    ComparisonRow(
                        label = "Consistency Index",
                        currentVal = currentConsistency,
                        potentialVal = 95,
                        color = CyberNeonCyan
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Dimension 2: Timeline Learning Pace
                    val currentPace = (user.skills.count { it.level.lowercase() != "none" } * 20 + 30).coerceIn(30, 100)
                    ComparisonRow(
                        label = "Skill Mastery Pace",
                        currentVal = currentPace,
                        potentialVal = 100,
                        color = CyberNeonPurpleGlow
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Dimension 3: Dream Aligner sync
                    ComparisonRow(
                        label = "Dream Synchronization",
                        currentVal = user.dreamProgress,
                        potentialVal = 100,
                        color = Color.Green
                    )
                }
            }

            // Historical Reflections
            Text(
                text = "📜 TRUTHS REVEALED IN THE CORRIDOR",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.7f),
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (user!!.mirrorReflections.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CyberGlassCardBg, RoundedCornerShape(12.dp))
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No mirror thoughts documented yet. Look in the mirror first.",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )
                }
            } else {
                user!!.mirrorReflections.reversed().forEach { ref ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "REFLECTED ON ${ref.dateAnswered}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberAlertPink
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = ref.question,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "\"${ref.answer}\"",
                                fontSize = 13.sp,
                                color = Color.White,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DocumentaryScreen(viewModel: MainViewModel, user: UserEntity?) {
    if (user == null) return
    val text by viewModel.documentaryText.collectAsStateWithLifecycle()
    val loading by viewModel.isDocumentaryLoading.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.navigateTo(Screen.Dashboard) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "BIOPIC MINI-DOCUMENTARY",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Text(
                text = "Weave your complete profile, completed moments, daily stress stats, and recorded commitments into an epic storytelling mini-biography narrated by a cinematic temporal chronicle-expert.",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.7f),
                lineHeight = 15.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            if (loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(CyberGlassCardBg, RoundedCornerShape(16.dp))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = CyberNeonCyan)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "COMPILING CHRONOLOGY MATRIX...",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberNeonCyan,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Synthesizing commitments and future timelines into text biopic",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 10.sp
                        )
                    }
                }
            } else if (text == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(CyberGlassCardBg, RoundedCornerShape(16.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Tape",
                            tint = CyberNeonCyan,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "BIOPIC UNCOMPILED",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Press play to generate a high-narrative text story of your life based on timeline telemetry.",
                            textAlign = TextAlign.Center,
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { viewModel.generateDocumentary() },
                            modifier = Modifier
                                .height(44.dp)
                                .testTag("compile_documentary_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = CyberNeonCyan)
                        ) {
                            Text("COMPILE & PREVIEW", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.5f)),
                    border = BorderStroke(1.dp, CyberNeonCyan.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "✦ TRAJECTORY BIOPIC DOCU-SCRIPT",
                            fontWeight = FontWeight.Bold,
                            color = CyberNeonCyan,
                            fontSize = 12.sp,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                        ) {
                            val paragraphList = text!!.split("\n\n")
                            paragraphList.forEach { block ->
                                if (block.startsWith("#") || block.startsWith("CHAPTER") || block.contains("CHAPTER")) {
                                    Text(
                                        text = block.replace("#", "").trim(),
                                        fontWeight = FontWeight.ExtraBold,
                                        color = CyberNeonPurpleGlow,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(top = 12.dp, bottom = 6.dp)
                                    )
                                } else {
                                    Text(
                                        text = block.trim(),
                                        fontSize = 13.sp,
                                        color = Color.White.copy(alpha = 0.9f),
                                        lineHeight = 18.sp,
                                        modifier = Modifier.padding(bottom = 12.dp),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { viewModel.generateDocumentary() },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp)
                                    .testTag("regenerate_docu_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1f)),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
                            ) {
                                Text("RE-SYNC", color = Color.White, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChallengesScreen(viewModel: MainViewModel, user: UserEntity?) {
    if (user == null) return

    // Trigger starter challenges if not exists
    LaunchedEffect(user) {
        viewModel.initStarterChallenges(user)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.navigateTo(Screen.Dashboard) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "FUTURE引导挑战 (CHALLENGES)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Text(
                text = "Deep consistency requires deliberate friction. Your Future Guide issues challenges that must be advanced manually through actual study or completion. Fulfilling them alters state variables and unlocks exclusive timeline memory nodes.",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.7f),
                lineHeight = 15.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Current Active Challenges List
            Text(
                text = "🎯 ACTIVE QUESTS ISSUED BY GUIDE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.7f),
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (user!!.activeChallenges.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CyberGlassCardBg, RoundedCornerShape(12.dp))
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Initializing challenges in starmap...", color = Color.White.copy(alpha = 0.5f))
                }
            } else {
                user!!.activeChallenges.forEach { challenge ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = CyberGlassCardBg),
                        border = BorderStroke(
                            1.dp,
                            if (challenge.isCompleted) Color.Green.copy(alpha = 0.4f) else CyberNeonCyan.copy(alpha = 0.25f)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (challenge.isCompleted) Icons.Default.CheckCircle else Icons.Default.Star,
                                        contentDescription = "Icon",
                                        tint = if (challenge.isCompleted) Color.Green else CyberNeonCyan,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = challenge.title.uppercase(),
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 13.sp,
                                        color = if (challenge.isCompleted) Color.Green else Color.White
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .background(
                                            if (challenge.isCompleted) Color.Green.copy(alpha = 0.15f) else CyberNeonCyan.copy(alpha = 0.15f),
                                            RoundedCornerShape(6.dp)
                                        )
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = challenge.type,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (challenge.isCompleted) Color.Green else CyberNeonCyan
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = challenge.description,
                                color = Color.White.copy(alpha = 0.82f),
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                            Spacer(modifier = Modifier.height(10.dp))

                            // Progress
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "REWARD: ${challenge.rewardDescription}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = CyberNeonPurpleGlow
                                )
                                Text(
                                    text = "${challenge.currentProgress} / ${challenge.targetDays} Days",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { challenge.currentProgress / challenge.targetDays.toFloat() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = if (challenge.isCompleted) Color.Green else CyberNeonCyan,
                                trackColor = Color.White.copy(alpha = 0.1f)
                            )

                            if (!challenge.isCompleted) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = { viewModel.progressChallenge(challenge.id, 1) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(36.dp)
                                        .testTag("advance_challenge_${challenge.id}"),
                                    colors = ButtonDefaults.buttonColors(containerColor = CyberNeonCyan.copy(alpha = 0.15f)),
                                    border = BorderStroke(1.dp, CyberNeonCyan),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Add", tint = CyberNeonCyan, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("DOCUMENT 1 COMPLETED DAY", color = CyberNeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
