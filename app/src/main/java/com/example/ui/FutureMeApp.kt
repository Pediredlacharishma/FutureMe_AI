package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
