package com.example.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.data.model.*
import com.example.data.network.GeminiService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = AppDatabase.getDatabase(application).userDao()
    private val repository = UserRepository(userDao)

    // Routing navigation state
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Login)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Logged in user email
    private val _currentUserEmail = MutableStateFlow<String?>(null)
    val currentUserEmail: StateFlow<String?> = _currentUserEmail.asStateFlow()

    // Current user entity
    val currentUser: StateFlow<UserEntity?> = _currentUserEmail
        .flatMapLatest { email ->
            if (email == null) flowOf(null)
            else repository.getUserFlow(email)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Interactive message loading state
    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    // Weekly reflection generation loading state
    private val _isReflectionLoading = MutableStateFlow(false)
    val isReflectionLoading: StateFlow<Boolean> = _isReflectionLoading.asStateFlow()

    // Goal Discovery Quiz Variables
    private val _discoveryStage = MutableStateFlow(1) // 1=World, 2=Branch, 3=DeepBranch, 4=Result
    val discoveryStage: StateFlow<Int> = _discoveryStage.asStateFlow()

    private val _selectedWorld = MutableStateFlow("")
    val selectedWorld: StateFlow<String> = _selectedWorld.asStateFlow()

    private val _selectedBranch = MutableStateFlow("")
    val selectedBranch: StateFlow<String> = _selectedBranch.asStateFlow()

    private val _selectedDeepBranch = MutableStateFlow("")
    val selectedDeepBranch: StateFlow<String> = _selectedDeepBranch.asStateFlow()

    private val _discoveryResults = MutableStateFlow<List<GoalDiscoveryResult>>(emptyList())
    val discoveryResults: StateFlow<List<GoalDiscoveryResult>> = _discoveryResults.asStateFlow()

    init {
        // Pre-create a dummy default mock user in SQLite so testing in AI Studio is seamless and instant
        viewModelScope.launch {
            try {
                val existing = repository.getAllUsers()
                if (existing.isEmpty()) {
                    val defaultUser = UserEntity(
                        email = "charishma.pediredla7@gmail.com",
                        name = "Charishma",
                        age = "20",
                        classLevel = "Year 3 College",
                        goal = "AI Engineer",
                        interests = listOf("Coding", "Artificial Intelligence", "Neural Networks"),
                        skills = listOf(
                            SkillAssessmentItem("Python Programming", "Intermediate"),
                            SkillAssessmentItem("Data Structures & Algorithms", "Beginner"),
                            SkillAssessmentItem("Mathematics & Probability", "Not Started"),
                            SkillAssessmentItem("Statistics", "Not Started"),
                            SkillAssessmentItem("Machine Learning Models", "Not Started"),
                            SkillAssessmentItem("GitHub & Version Control", "Beginner")
                        ),
                        environment = EnvironmentConfig(
                            sleepHours = 7.5f,
                            studyHours = 5.0f,
                            stressLevel = 6,
                            confidenceLevel = 7,
                            burnoutLevel = 4,
                            familySupport = "High",
                            friendCircle = "Motivating",
                            consistency = 8,
                            procrastination = 4
                        ),
                        dreamProgress = 43,
                        currentStage = "Builder"
                    )
                    // Generate initial roadmap steps for default goals
                    val (steps, skills) = RoadmapGenerator.generateRoadmapAndSkills("AI Engineer")
                    // Merge dummy state
                    val updatedUser = defaultUser.copy(
                        learningPath = steps,
                        skills = skills.map { s ->
                            val match = defaultUser.skills.find { it.name == s.name }
                            if (match != null) s.copy(level = match.level) else s
                        }
                    )
                    val finalScore = calculateDreamProgress(updatedUser)
                    repository.insertUser(updatedUser.copy(dreamProgress = finalScore, currentStage = detectStage(finalScore)))
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Failed to insert default user entity", e)
            }
        }
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    // AUTH ACTIONS
    fun login(email: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val trimmedEmail = email.trim().lowercase()
            if (trimmedEmail.isEmpty()) {
                onError("Please write your email address.")
                return@launch
            }
            val user = repository.getUser(trimmedEmail)
            if (user != null) {
                _currentUserEmail.value = trimmedEmail
                onSuccess()
            } else {
                onError("User profile not found. Please Sign Up to create a new simulation profile!")
            }
        }
    }

    fun signup(name: String, email: String, age: String, classLevel: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val trimmedEmail = email.trim().lowercase()
            if (trimmedEmail.isEmpty() || name.trim().isEmpty() || age.trim().isEmpty()) {
                onError("Please fill in all standard validation fields.")
                return@launch
            }
            val user = repository.getUser(trimmedEmail)
            if (user != null) {
                onError("This email is already registered. Please Login instead.")
            } else {
                val newUser = UserEntity(
                    email = trimmedEmail,
                    name = name.trim(),
                    age = age.trim(),
                    classLevel = classLevel.trim(),
                    goal = "AI Engineer", // Pre-populate standard target
                    environment = EnvironmentConfig()
                )
                val (steps, skills) = RoadmapGenerator.generateRoadmapAndSkills("AI Engineer")
                val completedUser = newUser.copy(
                    learningPath = steps,
                    skills = skills
                )
                val score = calculateDreamProgress(completedUser)
                val finalizedUser = completedUser.copy(
                    dreamProgress = score,
                    currentStage = detectStage(score)
                )

                repository.insertUser(finalizedUser)
                _currentUserEmail.value = trimmedEmail
                onSuccess()
            }
        }
    }

    fun logout() {
        _currentUserEmail.value = null
        _currentScreen.value = Screen.Login
    }

    // GOAL DISCOVERY ENGINE BRAIN
    fun startGoalDiscovery() {
        _discoveryStage.value = 1
        _selectedWorld.value = ""
        _selectedBranch.value = ""
        _selectedDeepBranch.value = ""
        _discoveryResults.value = emptyList()
        navigateTo(Screen.GoalDiscovery)
    }

    fun selectWorld(worldName: String) {
        _selectedWorld.value = worldName
        _discoveryStage.value = 2
    }

    fun selectBranch(branchName: String) {
        _selectedBranch.value = branchName
        if (_selectedWorld.value == "Technology" && branchName == "AI") {
            _discoveryStage.value = 3
        } else {
            calculateQuizResult(branchName)
        }
    }

    fun selectDeepBranch(deepBranchName: String) {
        _selectedDeepBranch.value = deepBranchName
        calculateQuizResult(deepBranchName)
    }

    private fun calculateQuizResult(finalNode: String) {
        val results = mutableListOf<GoalDiscoveryResult>()
        when (finalNode) {
            "AI" -> {
                results.add(GoalDiscoveryResult("AI Research Engineer", 56, listOf("✓ Exceptional analytical intelligence", "✓ Strong interest in code algorithms", "✓ Prefers advanced math and statistics", "✓ Passion for machine brain models")))
                results.add(GoalDiscoveryResult("Data Architect", 22, listOf("✓ Passion for cataloging statistics", "✓ Highly organized structurer")))
                results.add(GoalDiscoveryResult("Roboticist Assistant", 12, listOf("✓ Interest in hardware dynamics")))
                results.add(GoalDiscoveryResult("Entrepreneur Creator", 10, listOf("✓ Venturesome mindset", "✓ Business-oriented")))
            }
            "Teach People" -> {
                results.add(GoalDiscoveryResult("AI Educator / Trainer", 58, listOf("✓ Exceptional focus on human education", "✓ Passion for tech tutoring", "✓ Translates high-science to friendly tutorials")))
                results.add(GoalDiscoveryResult("AI Engineer", 24, listOf("✓ Prefers algorithms", "✓ Heavy coding mindset")))
                results.add(GoalDiscoveryResult("Psychologist Guide", 18, listOf("✓ Understanding human behavior")))
            }
            "Build Assistants" -> {
                results.add(GoalDiscoveryResult("Conversational AI Developer", 55, listOf("✓ Passion for dynamic agent building", "✓ High understanding of natural speech", "✓ Wants computers to speak like human guides")))
                results.add(GoalDiscoveryResult("Product Designer", 30, listOf("✓ Human-centric interface designing")))
                results.add(GoalDiscoveryResult("Full-stack Engineer", 15, listOf("✓ General systems builder")))
            }
            "Analyze Data" -> {
                results.add(GoalDiscoveryResult("Data Scientist", 52, listOf("✓ Natural ability to extract deep meanings", "✓ Loves graphs, predictive math, and trendlines", "✓ Analytical problem solver")))
                results.add(GoalDiscoveryResult("ML Ops Engineer", 28, listOf("✓ Heavy system pipeline automater")))
                results.add(GoalDiscoveryResult("Financial Quant Analyst", 20, listOf("✓ Passion for financial markets")))
            }
            "Automate Work" -> {
                results.add(GoalDiscoveryResult("RPA Architect", 54, listOf("✓ Loves deleting boring repetitive chores", "✓ Script orchestration genius", "✓ Efficiency maximization mindset")))
                results.add(GoalDiscoveryResult("Software Developer", 26, listOf("✓ Code engineering generalist")))
                results.add(GoalDiscoveryResult("Workflow Designer", 20, listOf("✓ Process optimizer and drawer")))
            }
            "Create Art" -> {
                results.add(GoalDiscoveryResult("Generative Media Synthesizer", 57, listOf("✓ Cross-disciplined creative technologist", "✓ Pushes vectors into artistic imagery", "✓ AI tool explorer")))
                results.add(GoalDiscoveryResult("Creative UI Animator", 25, listOf("✓ Dynamic screen fluid designer")))
                results.add(GoalDiscoveryResult("Game Developer", 18, listOf("✓ Loves sandbox environments")))
            }
            // Medicine nodes
            "Saving Lives in Emergency" -> {
                results.add(GoalDiscoveryResult("Emergency Room (ER) Doctor", 60, listOf("✓ Absolute mental calm during high pressure", "✓ Decisive and rapid medical reflexes", "✓ Loves helping patients in immediate need")))
                results.add(GoalDiscoveryResult("Trauma Surgery Specialist", 25, listOf("✓ Microscale mechanical motor actions")))
                results.add(GoalDiscoveryResult("Critical Care Nurse", 15, listOf("✓ Empathetic recovery supervisor")))
            }
            "Mental Well-being" -> {
                results.add(GoalDiscoveryResult("Psychiatrist Guide", 62, listOf("✓ Deep chemical and biological understand of brains", "✓ Professional therapy strategist", "✓ Loves guiding souls beyond trauma")))
                results.add(GoalDiscoveryResult("Neuroscientist Researcher", 20, listOf("✓ Focus on brain circuit mappings")))
                results.add(GoalDiscoveryResult("Clinical Psychologist", 18, listOf("✓ Conversational health guidance")))
            }
            "Researching Cures" -> {
                results.add(GoalDiscoveryResult("Biomedical Laboratory Scientist", 64, listOf("✓ Prefers microfluidics and genetic engineering", "✓ Dedicated to discovering systemic treatments", "✓ Extremely methodical researcher")))
                results.add(GoalDiscoveryResult("Clinical Trial Coordinator", 20, listOf("✓ Managing test group parameters")))
                results.add(GoalDiscoveryResult("Bioinformatics Specialist", 16, listOf("✓ Synthesizing big data biological files")))
            }
            "Surgical Precision" -> {
                results.add(GoalDiscoveryResult("Neurosurgeon Specialist", 61, listOf("✓ Tremendous hand stability and focus", "✓ Extreme stamina in microscopic workspaces", "✓ Prefers technical surgical systems")))
                results.add(GoalDiscoveryResult("Orthopaedic Surgeon", 24, listOf("✓ Physical restructuring biomechanics")))
                results.add(GoalDiscoveryResult("Anesthesiologist Coordinator", 15, listOf("✓ Patient parameter supervisor")))
            }
            // Business nodes
            "Launch New Products" -> {
                results.add(GoalDiscoveryResult("Tech Startup Entrepreneur", 58, listOf("✓ High tolerance for dynamic scaling risks", "✓ Fast product iteration capabilities", "✓ Combines vision with venture execution")))
                results.add(GoalDiscoveryResult("Product Manager", 26, listOf("✓ Roadmap designer and task organizer")))
                results.add(GoalDiscoveryResult("VC Incubator Consultant", 16, listOf("✓ Evaluating business opportunities")))
            }
            "Manage/Invest Capital" -> {
                results.add(GoalDiscoveryResult("Venture Capital (VC) Analyst", 60, listOf("✓ Highly analytical financial projection mindset", "✓ Detects winning structures in early days", "✓ Prefers managing investment pools")))
                results.add(GoalDiscoveryResult("Investment Banker", 22, listOf("✓ Managing large firm equity files")))
                results.add(GoalDiscoveryResult("Wealth Management Advisor", 18, listOf("✓ Safe long term asset allocator")))
            }
            "Organize & Lead Teams" -> {
                results.add(GoalDiscoveryResult("Chief Operations Officer (COO)", 59, listOf("✓ Exceptional group alignment strategies", "✓ Tactical business logistics execution", "✓ Prefers people structure design")))
                results.add(GoalDiscoveryResult("HR Culture Director", 25, listOf("✓ Internal support system creator")))
                results.add(GoalDiscoveryResult("Agile Delivery Manager", 16, listOf("✓ Sprints and team speed optimizer")))
            }
            "Creative Marketing" -> {
                results.add(GoalDiscoveryResult("Growth Growth Marketer", 63, listOf("✓ Outstanding psychology of consumption", "✓ Dynamic copy writing and viral loop designs", "✓ Performance statistics tracker")))
                results.add(GoalDiscoveryResult("Brand Creative Director", 23, listOf("✓ Design, layout, and visual story focus")))
                results.add(GoalDiscoveryResult("SEO Campaign Planner", 14, listOf("✓ Page rank and semantic indexing locator")))
            }
            // Arts nodes
            "Digital Designs & UI" -> {
                results.add(GoalDiscoveryResult("UI/UX Product Designer", 64, listOf("✓ Seamless human computer design interfaces", "✓ Outstanding grid and font layout aesthetics", "✓ Prefers user empathy styling")))
                results.add(GoalDiscoveryResult("Motion Graphics Specialist", 22, listOf("✓ Interface spring animation timing")))
                results.add(GoalDiscoveryResult("Creative Front-end Developer", 14, listOf("✓ Translates colors to Compose widgets")))
            }
            "Music & Soundscapes" -> {
                results.add(GoalDiscoveryResult("Sound Interactive Producer", 60, listOf("✓ Tremendous acoustic frequency ear", "✓ Prefers synthesizer loops and noise scopes", "✓ Professional digital audio workstation master")))
                results.add(GoalDiscoveryResult("Game Audio Engineer", 25, listOf("✓ Dynamic sound triggers code")))
                results.add(GoalDiscoveryResult("Studio Mixing Specialist", 15, listOf("✓ Audio level adjustments master")))
            }
            "Stories & Writing" -> {
                results.add(GoalDiscoveryResult("Creative Novelist / Screenwriter", 61, listOf("✓ Compelling character growth narratives", "✓ Rich lore and semantic context mapping", "✓ Prefers typing words to writing code")))
                results.add(GoalDiscoveryResult("Copywriter Consultant", 24, listOf("✓ Conversational conversion marketing")))
                results.add(GoalDiscoveryResult("Game Lore Architect", 15, listOf("✓ Rich fantasy sandbox architect")))
            }
            "Physical Artifacts/Painting" -> {
                results.add(GoalDiscoveryResult("Industrial Design Artist", 57, listOf("✓ High material and 3D space understanding", "✓ Physical artifact synthesis talent", "✓ Loves traditional drawing/painting tools")))
                results.add(GoalDiscoveryResult("Exhibition Fine Artist", 25, listOf("✓ Conceptual galleriest creator")))
                results.add(GoalDiscoveryResult("Ceramics & Craft Creator", 18, listOf("✓ Craftmanship material physical sculptor")))
            }
            // General Fallback Node
            else -> {
                results.add(GoalDiscoveryResult("$finalNode Specialist", 55, listOf("✓ High affinity for $finalNode", "✓ Motivated to master relevant domains")))
                results.add(GoalDiscoveryResult("General Manager", 25, listOf("✓ Operations and execution supervisor")))
                results.add(GoalDiscoveryResult("Consulting Adviser", 20, listOf("✓ Guidance and strategic analytical reviews")))
            }
        }
        _discoveryResults.value = results
        _discoveryStage.value = 4
    }

    fun applySelectedGoal(goalResultName: String) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            val (roadmap, skills) = RoadmapGenerator.generateRoadmapAndSkills(goalResultName)
            val updatedUser = user.copy(
                goal = goalResultName,
                learningPath = roadmap,
                skills = skills
            )
            val finalScore = calculateDreamProgress(updatedUser)
            val savedUser = updatedUser.copy(
                dreamProgress = finalScore,
                currentStage = detectStage(finalScore)
            )
            repository.updateUser(savedUser)
            navigateTo(Screen.Dashboard)
        }
    }

    // USER SETTING HABITS / ENVIRONMENT
    fun updateEnvironment(config: EnvironmentConfig) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            val updatedUser = user.copy(environment = config)
            val finalScore = calculateDreamProgress(updatedUser)
            val savedUser = updatedUser.copy(
                dreamProgress = finalScore,
                currentStage = detectStage(finalScore)
            )
            repository.updateUser(savedUser)
        }
    }

    // SKILL PROGRESSION / ROADMAP TASK CHECK
    fun toggleRoadmapTask(stepTitle: String) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            val path = user.learningPath.toMutableList()
            val index = path.indexOfFirst { it.title == stepTitle }
            if (index != -1) {
                val step = path[index]
                val toggledStep = step.copy(completed = !step.completed)
                path[index] = toggledStep

                // Update skill assessment level accordingly
                val skills = user.skills.toMutableList()
                val skillMatchIndex = skills.indexOfFirst { skillItem ->
                    step.title.lowercase().contains(skillItem.name.substringBefore(" ").lowercase()) ||
                    skillItem.name.lowercase().contains(step.title.substringBefore(" ").lowercase())
                }

                if (skillMatchIndex != -1) {
                    val currentSkill = skills[skillMatchIndex]
                    val newLevel = when {
                        toggledStep.completed -> "Intermediate"
                        else -> "Not Started"
                    }
                    skills[skillMatchIndex] = currentSkill.copy(level = newLevel)
                } else if (skills.isNotEmpty()) {
                    // Update a skill to Beginner or intermediate randomly if no match to make it interactive
                    val randIndex = (stepTitle.hashCode() % skills.size).coerceIn(0, skills.size - 1)
                    val currentSkill = skills[randIndex]
                    val newLevel = if (toggledStep.completed) "Intermediate" else "Not Started"
                    skills[randIndex] = currentSkill.copy(level = newLevel)
                }

                val updatedUser = user.copy(
                    learningPath = path,
                    skills = skills
                )
                val finalScore = calculateDreamProgress(updatedUser)
                val savedUser = updatedUser.copy(
                    dreamProgress = finalScore,
                    currentStage = detectStage(finalScore)
                )
                repository.updateUser(savedUser)
            }
        }
    }

    // MANUAL SKILL ASSESS DIRECT LEVEL TOGGLE
    fun updateSkillLevel(skillName: String, level: String) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            val skills = user.skills.toMutableList()
            val idx = skills.indexOfFirst { it.name == skillName }
            if (idx != -1) {
                skills[idx] = skills[idx].copy(level = level)
                val updatedUser = user.copy(skills = skills)
                val finalScore = calculateDreamProgress(updatedUser)
                val savedUser = updatedUser.copy(
                    dreamProgress = finalScore,
                    currentStage = detectStage(finalScore)
                )
                repository.updateUser(savedUser)
            }
        }
    }

    // AGENT CHATTER INTEGRATION (GEMINI)
    fun sendMessageToFutureGuide(text: String) {
        val user = currentUser.value ?: return
        if (text.trim().isEmpty()) return

        viewModelScope.launch {
            _isChatLoading.value = true
            // Append user msg locally
            val userMsg = ChatMessage(UUID.randomUUID().toString(), "user", text)
            val updatedChats = user.futureGuideChats + userMsg
            repository.updateUser(user.copy(futureGuideChats = updatedChats))

            // Invoke Gemini with dynamic context injected in System Instruction
            val systemInstruction = AgentPrompts.buildFutureGuideInstruction(user)
            val contextPrompt = buildChatPromptContext(updatedChats)
            
            val replyText = GeminiService.generateContent(
                prompt = contextPrompt,
                systemInstruction = systemInstruction
            )

            val guideMsg = ChatMessage(UUID.randomUUID().toString(), "guide", replyText)
            val latestUser = repository.getUser(user.email) ?: user
            repository.updateUser(latestUser.copy(futureGuideChats = latestUser.futureGuideChats + guideMsg))
            _isChatLoading.value = false
        }
    }

    fun sendMessageToShadowSelf(text: String) {
        val user = currentUser.value ?: return
        if (text.trim().isEmpty()) return

        viewModelScope.launch {
            _isChatLoading.value = true
            // Append user msg locally
            val userMsg = ChatMessage(UUID.randomUUID().toString(), "user", text)
            val updatedChats = user.shadowSelfChats + userMsg
            repository.updateUser(user.copy(shadowSelfChats = updatedChats))

            // Invoke Gemini
            val systemInstruction = AgentPrompts.buildShadowSelfInstruction(user)
            val contextPrompt = buildChatPromptContext(updatedChats)

            val replyText = GeminiService.generateContent(
                prompt = contextPrompt,
                systemInstruction = systemInstruction
            )

            val shadowMsg = ChatMessage(UUID.randomUUID().toString(), "shadow", replyText)
            val latestUser = repository.getUser(user.email) ?: user
            repository.updateUser(latestUser.copy(shadowSelfChats = latestUser.shadowSelfChats + shadowMsg))
            _isChatLoading.value = false
        }
    }

    private fun buildChatPromptContext(chats: List<ChatMessage>): String {
        // Build last 6 turns of conversations for small request footprint and contextual memory
        val historyToSubmit = chats.takeLast(10)
        return historyToSubmit.joinToString("\n") { msg ->
            val speaker = when (msg.sender) {
                "user" -> "Me (User)"
                "guide" -> "Future Successful Guide Self"
                "shadow" -> "Shadow Failed Regretful Self"
                else -> msg.sender
            }
            "$speaker: ${msg.message}"
        } + "\nRespond to this as your persona:"
    }

    // WEEKLY REFLECTION ENGINE (GEMINI POWERED)
    fun triggerWeeklyReflectionSim() {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            _isReflectionLoading.value = true
            val skillsList = user.skills.joinToString(", ") { "${it.name}: ${it.level}" }
            val env = user.environment
            
            val prompt = """
                Analyse this student's life simulator snapshot and write a highly concise weekly growth update in 1 short paragraph (under 4 sentences).
                Identify exactly why progress changed or is stuck. Point out habits!
                
                SNAPSHOT:
                - Dream Progress: ${user.dreamProgress}%
                - Current Stage: ${user.currentStage}
                - Target Goal: ${user.goal}
                - Academic: ${user.classLevel}
                - Sleep: ${env.sleepHours} hrs, Study: ${env.studyHours} hrs, Stress: ${env.stressLevel}/10, Consistency: ${env.consistency}/10, Procrastination: ${env.procrastination}/10
                - Skills: $skillsList
                
                Respond speaking like an advanced future cosmic time-tracker computer. Keep it objective, deeply insightful, and format it highlighting:
                1) A summary of the weekly shift.
                2) Highlight 2 concrete variables (e.g., + Study Hours, or - Procrastination).
            """.trimIndent()

            val aiResponse = GeminiService.generateContent(
                prompt = prompt,
                systemInstruction = "You are the Life Timeline Analysis Engine of FutureMe AI. Your response must be an objective temporal summary. Keep it below 80 words."
            )

            val today = SimpleDateFormat("MMM d, yyyy", Locale.US).format(Date())
            val newItem = WeeklyProgressItem(
                weekDate = today,
                progressScore = user.dreamProgress,
                stage = user.currentStage,
                reason = aiResponse
            )

            val latestUser = repository.getUser(user.email) ?: user
            repository.updateUser(latestUser.copy(
                weeklyHistory = (latestUser.weeklyHistory + newItem).takeLast(10)
            ))
            _isReflectionLoading.value = false
        }
    }

    // HELPER EVALUATIVE ENGINE
    fun calculateDreamProgress(user: UserEntity): Int {
        // 1. Skills completion score (out of 40 points)
        val skillsCount = user.skills.size
        val skillsScore = if (skillsCount > 0) {
            val earned = user.skills.fold(0.0f) { acc, item ->
                acc + when (item.level) {
                    "Advanced" -> 4.0f
                    "Intermediate" -> 2.5f
                    "Beginner" -> 1.0f
                    else -> 0.0f
                }
            }
            val totalMax = skillsCount * 4.0f
            (earned / totalMax) * 40.0f
        } else {
            0.0f
        }

        // 2. Habits compliance score (out of 30 points)
        val env = user.environment
        var habitsPct = 0.0f
        
        habitsPct += when (env.studyHours) {
            in 4.0f..8.0f -> 5f
            in 2.0f..4.0f -> 3f
            else -> 1f
        }
        
        habitsPct += when (env.sleepHours) {
            in 7.0f..9.0f -> 5f
            in 5.0f..7.0f -> 3f
            else -> 1f
        }
        
        habitsPct += (10 - env.stressLevel).coerceIn(0, 10) * 0.5f // max 5
        habitsPct += env.confidenceLevel.coerceIn(0, 10) * 0.5f // max 5
        
        habitsPct += when (env.familySupport) {
            "High" -> 5f
            "Medium" -> 3f
            else -> 1f
        }
        
        habitsPct += when (env.friendCircle) {
            "Motivating" -> 5f
            "Neutral" -> 3f
            else -> 1f
        }

        // 3. Focus & Consistency factor (out of 30 points)
        var consPct = 0.0f
        consPct += env.consistency.coerceIn(1, 10) * 1.5f // max 15
        consPct += (11 - env.procrastination).coerceIn(1, 10) * 1.5f // max 15

        return (skillsScore + habitsPct + consPct).toInt().coerceIn(0, 100)
    }

    fun detectStage(progress: Int): String {
        return when {
            progress < 15 -> "Stage 0: Explorer"
            progress < 35 -> "Stage 1: Beginner"
            progress < 60 -> "Stage 2: Builder"
            progress < 80 -> "Stage 3: Practitioner"
            else -> "Stage 4: Future Professional"
        }
    }
}

// Sealed class matching navigation screens
sealed class Screen {
    object Login : Screen()
    object Signup : Screen()
    object Dashboard : Screen()
    object GoalDiscovery : Screen()
    object EnvironmentForm : Screen()
    object SkillsRoadmap : Screen()
    object GuideChat : Screen()
    object ShadowChat : Screen()
    object WeeklyHistoryScreen : Screen()
}
