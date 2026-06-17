package com.example.ui

import android.app.Application
import android.util.Log
import android.speech.tts.TextToSpeech
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

    // Logged in user email
    private val _currentUserEmail = MutableStateFlow<String?>(null)
    val currentUserEmail: StateFlow<String?> = _currentUserEmail.asStateFlow()

    // TTS Voice Speech State
    private var tts: TextToSpeech? = null
    private val _isTtsSpeaking = MutableStateFlow(false)
    val isTtsSpeaking: StateFlow<Boolean> = _isTtsSpeaking.asStateFlow()
    val ttsToggled = MutableStateFlow(false)

    // Interactive Life Simulator sliders
    val simSleepHours = MutableStateFlow(7.5f)
    val simStudyHours = MutableStateFlow(5.0f)
    val simProcrastination = MutableStateFlow(4)
    val simConsistency = MutableStateFlow(8)

    // Derived alternate futures representing "What life am I building?"
    val simulatedFutures: StateFlow<List<AlternateFuture>> = combine(
        simSleepHours, simStudyHours, simProcrastination, simConsistency, _currentUserEmail
    ) { sleep, study, proc, cons, _ ->
        calculateSimulatedFutures(sleep, study, proc, cons, currentUser.value?.goal ?: "AI Engineer")
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Routing navigation state
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Login)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

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
        // Initialize native local text-to-speech engine
        tts = TextToSpeech(application) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.ENGLISH
            }
        }

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
                        currentStage = "Builder",
                        personalLegend = "You are not primarily motivated by capital. You are motivated by creating intelligent systems that amplify human understanding. You perform best when solving deep conceptual bottlenecks under high focus. Your strongest pairing is curious engineering and analytical logic. This is why mastering AI Engineering aligns perfectly with your timeline.",
                        commitments = listOf(
                            UserCommitment(UUID.randomUUID().toString(), "I will finish Python this month.", "Jun 1, 2026", "Jun 30, 2026", "Fulfilled"),
                            UserCommitment(UUID.randomUUID().toString(), "I will build my first AI project.", "Jun 10, 2026", "Jun 25, 2026", "Active"),
                            UserCommitment(UUID.randomUUID().toString(), "I will reduce procrastination to 3/10.", "Jun 17, 2026", "Jul 10, 2026", "Active")
                        ),
                        journalEntries = listOf(
                            JournalEntry(UUID.randomUUID().toString(), "Jun 15, 2026", "Discovered some fascinating concepts in neural dynamics. Felt slightly stuck initially on vector projection, but reading standard texts for 2 hours cracked the barrier.", "Excited", "Vector algebra & feedforward logs", "Probability calculations"),
                            JournalEntry(UUID.randomUUID().toString(), "Jun 16, 2026", "Created a single-layer classifier in PyTorch! The feedback and learning rate made absolute sense. Shadow Self warned me to keep consistency up.", "Calm", "PyTorch loss backward functions", "Struggles with dynamic backprop")
                        ),
                        realityCheckReports = listOf(
                            RealityCheckReport(
                                id = UUID.randomUUID().toString(),
                                month = "Month Analysis: June 2026",
                                goalTitle = "Python & Neural Fundamentals",
                                plannedCompletion = 100,
                                actualCompletion = 65,
                                plannedHours = 120,
                                actualHours = 84,
                                insightFromGuide = "We built 84 hours of solid core habits. That's a great initial run. Next month we will step up our consistency to hit 100% of our milestone goals!",
                                warningFromShadow = "Our target was 120 hours. Missing 36 hours is how my stagnation started. Don't fall into the gap trap or you'll eventually see my timeline."
                            )
                        )
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

    // TEXT TO SPEECH CONTROLS
    fun speakText(text: String) {
        tts?.stop()
        _isTtsSpeaking.value = true
        // Strip out some markdown characters for clean speech synthesis
        val cleanText = text
            .replace("*", "")
            .replace("#", "")
            .replace("_", "")
            .replace("✓", "checked")
            .replace("•", "")
        tts?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, "futureme_tts")
    }

    fun stopSpeaking() {
        tts?.stop()
        _isTtsSpeaking.value = false
    }

    override fun onCleared() {
        super.onCleared()
        tts?.shutdown()
    }

    // COMMITMENTS / PROMISE MEMORY SYSTEM
    fun addCommitment(text: String, targetDate: String) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            val commitment = UserCommitment(
                id = UUID.randomUUID().toString(),
                text = text,
                loggedDate = SimpleDateFormat("MMM d, yyyy", Locale.US).format(Date()),
                targetDate = targetDate.ifEmpty { "No deadline" },
                status = "Active"
            )
            val latestUser = repository.getUser(user.email) ?: user
            repository.updateUser(latestUser.copy(
                commitments = latestUser.commitments + commitment
            ))
        }
    }

    fun toggleCommitmentStatus(id: String, newStatus: String) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            val commitments = user.commitments.toMutableList()
            val index = commitments.indexOfFirst { it.id == id }
            if (index != -1) {
                commitments[index] = commitments[index].copy(status = newStatus)
                val latestUser = repository.getUser(user.email) ?: user
                repository.updateUser(latestUser.copy(commitments = commitments))
            }
        }
    }

    fun deleteCommitment(id: String) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            val commitments = user.commitments.filterNot { it.id == id }
            val latestUser = repository.getUser(user.email) ?: user
            repository.updateUser(latestUser.copy(commitments = commitments))
        }
    }

    // FUTUREME REFLECTIVE JOURNAL SYSTEM
    fun addJournalEntry(content: String, emotionalState: String, learned: String, struggled: String, gratitude: String) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            _isChatLoading.value = true
            val entryId = UUID.randomUUID().toString()
            val today = SimpleDateFormat("MMM d, yyyy", Locale.US).format(Date())
            
            val tempEntry = JournalEntry(
                id = entryId,
                date = today,
                content = content,
                emotionalState = emotionalState,
                learned = learned,
                struggled = struggled,
                gratitude = gratitude
            )
            
            // Invoke Gemini to generate immediate reflective responses from Guide and Shadow
            val prompt = AgentPrompts.buildJournalFeedbackPrompt(user, tempEntry)
            val aiResponse = try {
                GeminiService.generateContent(
                    prompt = prompt,
                    systemInstruction = "You extract double-sided insights from the user's journal. Follow instructions precisely."
                )
            } catch (e: Exception) {
                "Future Guide: This is progress! Every step you take, even on difficult days, reduces stress. | Shadow Self: I remember feeling this way too. Watch out—if you procrastinate now, this pattern locks in."
            }
            
            val comments = aiResponse.split("|")
            val guideComment = comments.getOrNull(0)?.trim()?.replace("Future Guide:", "")?.replace("Guide:", "")?.trim() ?: "Keep pushing forward! No effort is wasted."
            val shadowComment = comments.getOrNull(1)?.trim()?.replace("Shadow Self:", "")?.replace("Shadow:", "")?.trim() ?: "Stagnation is comfortable because it requires no work. Fight it."
            
            val finalEntry = tempEntry.copy(
                content = "${tempEntry.content}\n\n• Future Guide: \"$guideComment\"\n• Shadow Self: \"$shadowComment\""
            )
            
            val latestUser = repository.getUser(user.email) ?: user
            repository.updateUser(latestUser.copy(
                journalEntries = latestUser.journalEntries + finalEntry
            ))
            _isChatLoading.value = false
        }
    }

    // PERSONAL LEGEND ENGINE
    fun generatePersonalLegend() {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            _isReflectionLoading.value = true
            val prompt = AgentPrompts.buildPersonalLegendPrompt(user)
            val response = try {
                GeminiService.generateContent(
                    prompt = prompt,
                    systemInstruction = "You are the Cosmic Personal Legend Weaver of FutureMe AI. Your response must be an inspiring, deeply reflective narrative."
                )
            } catch (e: Exception) {
                "You are motivated by creating things that improve people's lives. You perform best when solving difficult problems. You become deeply engaged when your work feels meaningful.\n\nYour strongest combination is curiosity, creativity, and analytical thinking.\n\nThis is why AI Engineering and Innovation align strongly with who you are and who you are destined to become."
            }
            
            val latestUser = repository.getUser(user.email) ?: user
            repository.updateUser(latestUser.copy(personalLegend = response))
            _isReflectionLoading.value = false
        }
    }

    // REALITY CHECK AUDIT REPORT
    fun generateRealityCheck() {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            _isReflectionLoading.value = true
            
            // Planned vs Actual values calculated dynamically from profile
            val targetGoal = user.goal
            val plannedCompletion = 100
            val completedCount = user.learningPath.count { it.completed }
            val totalCount = user.learningPath.size.coerceAtLeast(1)
            val actualCompletion = ((completedCount.toFloat() / totalCount) * 100).toInt()
            
            val plannedHours = 120
            val actualHours = (120 * (user.environment.consistency / 10f) * (user.environment.studyHours / 5.0f).coerceIn(0.5f, 1.2f)).toInt().coerceIn(30, 120)
            
            val progressGap = plannedCompletion - actualCompletion
            val hoursGap = (plannedHours - actualHours).coerceAtLeast(0)
            
            val prompt = """
                Analyse this reality check comparison and write two concise paragraphs:
                1) From 'Future Guide' (supportive, explaining how to adjust plans or where they made progress).
                2) From 'Shadow Self' (gently explaining where they overestimated and what repeated gaps lead to).
                
                Planned Completion: $plannedCompletion%, Actual Completion: $actualCompletion% (Gap: $progressGap%)
                Planned Study Hours: $plannedHours hrs, Actual Study Hours: $actualHours hrs (Gap: $hoursGap hrs)
                Consistency: ${user.environment.consistency}/10, Procrastination: ${user.environment.procrastination}/10
                Target Career: $targetGoal
                
                Respond in exactly the format:
                Guide: <text>
                Shadow: <text>
                Keep each text section under 40 words.
            """.trimIndent()
            
            val res = try {
                GeminiService.generateContent(
                    prompt = prompt,
                    systemInstruction = "You generate honest, corrective, and supportive reality check insights."
                )
            } catch (e: Exception) {
                "Guide: I see you achieved $actualCompletion% of our target baselines. Good work on starting, let's bump up consistent actions next month!\nShadow: We planned $plannedHours hours but only hit $actualHours. If you keep leaving this $hoursGap hour gap, you will eventually end up in my timeline."
            }
            
            val lines = res.split("\n")
            val guideInsight = lines.find { it.trim().startsWith("Guide:") }?.substringAfter("Guide:")?.trim() ?: "Don't focus on the gap of $progressGap%—focus on your next 1% growth. You can stabilize this."
            val shadowWarning = lines.find { it.trim().startsWith("Shadow:") }?.substringAfter("Shadow:")?.trim() ?: "The $hoursGap hour gap is exactly how I slipped out of range. Don't let your plans run on empty."
            
            val newReport = RealityCheckReport(
                id = UUID.randomUUID().toString(),
                month = "Month Analysis: ${SimpleDateFormat("MMMM yyyy", Locale.US).format(Date())}",
                goalTitle = user.goal,
                plannedCompletion = plannedCompletion,
                actualCompletion = actualCompletion,
                plannedHours = plannedHours,
                actualHours = actualHours,
                insightFromGuide = guideInsight,
                warningFromShadow = shadowWarning
            )
            
            val latestUser = repository.getUser(user.email) ?: user
            repository.updateUser(latestUser.copy(
                realityCheckReports = latestUser.realityCheckReports + newReport
            ))
            _isReflectionLoading.value = false
        }
    }

    // THE BOOK OF YOU LIVERAUTOBIOGRAPHY CHAPTER GENERATOR
    fun compileBookChapters(user: UserEntity): List<BookChapter> {
        val name = user.name
        val goal = user.goal
        val progress = user.dreamProgress
        
        return listOf(
            BookChapter(
                title = "Chapter 1: Discovery",
                subTitle = "The Awakening of Ambition",
                content = "At age ${user.age}, $name embarked on a transformative journey. Rejecting standard career routines, they logged their first diagnostic profile, targeting the elite status of a $goal. This marked the initial spark.",
                unlockedDate = "Unlocked upon registration",
                isLocked = false
            ),
            BookChapter(
                title = "Chapter 2: Finding Direction",
                subTitle = "Charting the Cosmic Navigation Map",
                content = "With a personalized roadmap generated, $name identified key domains. They committed to core technologies, algorithmic challenges, and daily consistency. The portal with their 35-year-old Future Guide stabilizes.",
                unlockedDate = "Unlocked at Progress > 20%",
                isLocked = progress < 20
            ),
            BookChapter(
                title = "Chapter 3: First Failure",
                subTitle = "Wrestling with the Shadow Self",
                content = "Initial enthusiasm hit friction, replaced by stagnation and procrastination of ${user.environment.procrastination}/10. The Shadow Self materialized in the temporal portal, speaking of how missing minor goals compiles into entire custom timelines of regret. An honest Reality Check is computed.",
                unlockedDate = "Unlocked at Progress > 40%",
                isLocked = progress < 40
            ),
            BookChapter(
                title = "Chapter 4: First Breakthrough",
                subTitle = "The Fire of Consistency",
                content = "$name broke the looping stagnation. By checking off foundational skills and raising daily focus to ${user.environment.studyHours} hours, consistency climbed to ${user.environment.consistency}/10. Future Guide's emotional state evolved, radiating hope.",
                unlockedDate = "Unlocked at Progress > 60%",
                isLocked = progress < 60
            ),
            BookChapter(
                title = "Chapter 5: Great Milestone & Projects",
                subTitle = "Materializing Vision Into Code",
                content = "Active development of the first major portfolio projects has commenced. No longer just researching, $name built actual modules, stabilizing their alternate future timelines against collapse.",
                unlockedDate = "Unlocked at Progress > 80%",
                isLocked = progress < 80
            ),
            BookChapter(
                title = "Chapter 6: Professional Master",
                subTitle = "A Future Self Fully Synchronized",
                content = "Complete temporal convergence. Progress exceeds 90%, merging the digital twin with physical existence. The Future Guide smiles, their mentoring mission successfully fulfilled.",
                unlockedDate = "Unlocked at Progress > 90%",
                isLocked = progress < 90
            )
        )
    }

    // LIFE SIMULATOR ALTERNATE FUTURES CALCULATIONS
    private fun calculateSimulatedFutures(
        sleep: Float,
        study: Float,
        procrastination: Int,
        consistency: Int,
        targetGoal: String
    ): List<AlternateFuture> {
        val score = (study * 6 + consistency * 5 - procrastination * 4 + (sleep - 6f).coerceAtLeast(0f) * 2).toInt().coerceIn(10, 100)
        
        return listOf(
            AlternateFuture(
                title = "The Achiever Path",
                description = "By maintaining high study hours ($study) and consistency ($consistency/10), you reach ultimate career synchronization. You build world-class systems.",
                finalAge = 35,
                careerTitle = "Distinguished $targetGoal Architect",
                metrics = "Impact: Global (95%) | Satisfaction: 98% | Income: Top 1%",
                dynamicPros = listOf("Mastered $targetGoal fully", "High intellectual independence", "Pristine life fulfillment"),
                dynamicCons = listOf("Extreme cognitive ownership", "Heavy responsibility stress")
            ),
            AlternateFuture(
                title = "The Entrepreneur Path",
                description = "Using technical agility, you compile code into venture products, scaling your own enterprise.",
                finalAge = 35,
                careerTitle = "Technical Founder & CEO",
                metrics = "Impact: Disruptive (88%) | Satisfaction: 92% | Income: Uncapped",
                dynamicPros = listOf("Dynamic building speed", "Full product authorship", "High scale independence"),
                dynamicCons = listOf("Significant stress surges", "Compromised sleep consistency ($sleep hrs)")
            ),
            AlternateFuture(
                title = "The Corporate Specialist",
                description = "You secure a lucrative, stable position in established enterprises. Comfortable hours leave time for leisure, but standard routines persist.",
                finalAge = 35,
                careerTitle = "Lead Systems Specialist",
                metrics = "Impact: Systemic (60%) | Satisfaction: 72% | Income: Upper Class",
                dynamicPros = listOf("Highly structured hours", "Zero venture risk", "Strong corporate backing"),
                dynamicCons = listOf("Repetitive work cycles", "Capped creative freedom")
            ),
            AlternateFuture(
                title = "The Alternate Explorer",
                description = "Curiosity drives you to teach, write, and advocate. Instead of deep specification, you serve as a generalist guide, traveling and exploring.",
                finalAge = 35,
                careerTitle = "Global Developer Evangelist",
                metrics = "Impact: Personal (75%) | Satisfaction: 88% | Income: Floating",
                dynamicPros = listOf("Frequent global travel", "Strong peer community impact", "High flexibility"),
                dynamicCons = listOf("Fragmented domain specialization", "Constant physical fatigue")
            ),
            AlternateFuture(
                title = "The Shadow Path (Regret)",
                description = "If procrastination ($procrastination/10) controls your habits, milestones are continually deferred. After a sequence of failures, you settle for safety.",
                finalAge = 35,
                careerTitle = "Uninspired Repetitive Clerk",
                metrics = "Impact: Low (5%) | Satisfaction: 15% | Income: Low-Average",
                dynamicPros = listOf("Familiar desk routines", "Zero intellectual challenges"),
                dynamicCons = listOf("Deep feeling of missed potential", "Muted memory of active dreams", "Underutilized talent")
            )
        )
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

    // =========================================================================
    // 🌌 EXPERIENTIAL LAYERS SYSTEM: TIME CAPSULES, MOMENTS, CONSTELLATIONS, MIRROR
    // =========================================================================

    // Starter challenges initialization helper
    fun initStarterChallenges(user: UserEntity) {
        if (user.activeChallenges.isNotEmpty()) return
        viewModelScope.launch {
            val starters = listOf(
                FutureSelfChallenge(
                    id = "sentinel_7",
                    title = "The 7-Day Sentinel",
                    description = "Study or work on your goal for at least 1 hour daily to train cognitive reflexes.",
                    targetDays = 7,
                    currentProgress = 0,
                    isCompleted = false,
                    rewardDescription = "Unlocks memory card #15: The Focus Anchor",
                    type = "Study"
                ),
                FutureSelfChallenge(
                    id = "morning_spark_5",
                    title = "The Morning Spark Quest",
                    description = "Focus on your personal path 30 minutes earlier every morning for 5 days.",
                    targetDays = 5,
                    currentProgress = 0,
                    isCompleted = false,
                    rewardDescription = "Unlocks a locked Time Capsule message",
                    type = "Consistency"
                ),
                FutureSelfChallenge(
                    id = "builders_pledge_2",
                    title = "The Builder's Pledge",
                    description = "Successfully complete 2 intermediate roadmap milestones on your path.",
                    targetDays = 2,
                    currentProgress = 0,
                    isCompleted = false,
                    rewardDescription = "Unlocks entry #22: Ultimate Catalyst",
                    type = "Projects"
                )
            )
            val latestUser = repository.getUser(user.email) ?: user
            repository.updateUser(latestUser.copy(activeChallenges = starters))
        }
    }

    // Time Capsule Management
    fun addTimeCapsule(message: String, unlockReason: String, customDays: Int = 0) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            val cal = Calendar.getInstance()
            val cYear = cal.get(Calendar.YEAR)
            val cMonth = cal.get(Calendar.MONTH) + 1
            val cDay = cal.get(Calendar.DAY_OF_MONTH)
            val createdStr = String.format("%04d-%02d-%02d", cYear, cMonth, cDay)

            when (unlockReason) {
                "1 Month" -> cal.add(Calendar.MONTH, 1)
                "3 Months" -> cal.add(Calendar.MONTH, 3)
                "6 Months" -> cal.add(Calendar.MONTH, 6)
                "1 Year" -> cal.add(Calendar.YEAR, 1)
                "Custom Date" -> cal.add(Calendar.DAY_OF_YEAR, customDays.coerceIn(1, 365))
            }
            val uYear = cal.get(Calendar.YEAR)
            val uMonth = cal.get(Calendar.MONTH) + 1
            val uDay = cal.get(Calendar.DAY_OF_MONTH)
            val unlockStr = String.format("%04d-%02d-%02d", uYear, uMonth, uDay)

            val newCapsule = TimeCapsule(
                id = UUID.randomUUID().toString(),
                message = message,
                createdDate = createdStr,
                unlockDate = unlockStr,
                isUnlocked = false,
                unlockReason = unlockReason
            )

            val latestUser = repository.getUser(user.email) ?: user
            repository.updateUser(latestUser.copy(timeCapsules = latestUser.timeCapsules + newCapsule))
            
            addFutureMomentInternal(
                title = "Future Moment #${latestUser.futureMoments.size + 1}",
                subtitle = "A Message Cast Into the Void",
                content = "You sealed a raw emotional whisper into a Time Capsule, locked until $unlockStr. You chose to connect with the person you will become.",
                category = "Time Capsule"
            )
        }
    }

    fun forceUnlockTimeCapsule(id: String) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            val latestUser = repository.getUser(user.email) ?: user
            val updated = latestUser.timeCapsules.map {
                if (it.id == id) it.copy(isUnlocked = true) else it
            }
            repository.updateUser(latestUser.copy(timeCapsules = updated))
            
            addFutureMomentInternal(
                title = "Future Moment #${latestUser.futureMoments.size + 1}",
                subtitle = "Chronological Convergence",
                content = "You successfully unsealed a locked temporal capsule and reconnected with your former self.",
                category = "Time Capsule"
            )
        }
    }

    // Future Moment Management
    suspend fun addFutureMomentInternal(title: String, subtitle: String, content: String, category: String) {
        val user = currentUser.value ?: return
        val latestUser = repository.getUser(user.email) ?: user
        val cal = Calendar.getInstance()
        val dateStr = String.format("%04d-%02d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))
        
        val newMoment = FutureMoment(
            id = UUID.randomUUID().toString(),
            title = title,
            subtitle = subtitle,
            content = content,
            dateLogged = dateStr,
            category = category
        )
        repository.updateUser(latestUser.copy(futureMoments = latestUser.futureMoments + newMoment))
    }

    fun addFutureMoment(title: String, subtitle: String, content: String, category: String) {
        viewModelScope.launch {
            addFutureMomentInternal(title, subtitle, content, category)
        }
    }

    // The Mirror Questions
    fun logMirrorAnswer(question: String, answer: String) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            val cal = Calendar.getInstance()
            val dateStr = String.format("%04d-%02d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))
            
            val newRef = MirrorReflection(
                id = UUID.randomUUID().toString(),
                question = question,
                answer = answer,
                dateAnswered = dateStr
            )
            val latestUser = repository.getUser(user.email) ?: user
            repository.updateUser(latestUser.copy(mirrorReflections = latestUser.mirrorReflections + newRef))
            
            addFutureMomentInternal(
                title = "Future Moment #${latestUser.futureMoments.size + 1}",
                subtitle = "The Unlocking of Wisdom",
                content = "You confronted yourself. In response to: \"$question\", you committed: \"$answer\". Guided alignment has deepened.",
                category = "Reflection"
            )
        }
    }

    // Documentary Generator
    val documentaryText = MutableStateFlow<String?>(null)
    val isDocumentaryLoading = MutableStateFlow(false)

    fun generateDocumentary() {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            isDocumentaryLoading.value = true
            val prompt = AgentPrompts.buildDocumentaryPrompt(user)
            val response = try {
                GeminiService.generateContent(
                    prompt = prompt,
                    systemInstruction = "You are a master cinematic biopic narrator of FutureMe AI, weaving a legendary story."
                )
            } catch (e: Exception) {
                """
                CHAPTER I: THE INCEPTION
                At the genesis of these coordinates, ${user.name} stepped into the temporal simulator at age ${user.age}. Driven by the deep quest to achieve the status of a ${user.goal}, they configured their diagnostics.
                
                CHAPTER II: THE COGNITIVE FORGE
                The trials were material. Under habits of procrastination (${user.environment.procrastination}/10), ${user.name} had to fight for hours of focus. The temporal corridor was contested daily.
                
                CHAPTER III: THE VERIFIED MOMENTS
                Breakthroughs arose. By marking milestone goals, writing down commitments, and syncing reflections, they anchored their transition into a true professional.
                
                CHAPTER IV: THE CONVERGENCE PROFILE
                At age 35, the timeline resolves. A highly successful ${user.goal} looks back along the temporal tunnel, proud of the silent battles fought and won today.
                """.trimIndent()
            }
            documentaryText.value = response
            isDocumentaryLoading.value = false
        }
    }

    // Relationships Assessment
    val relationshipsAssessment = MutableStateFlow<String?>(null)
    val isRelationshipsLoading = MutableStateFlow(false)

    fun generateRelationshipsAssessment() {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            isRelationshipsLoading.value = true
            val prompt = AgentPrompts.buildRelationshipsAssessmentPrompt(user)
            val response = try {
                GeminiService.generateContent(
                    prompt = prompt,
                    systemInstruction = "You are Future Guide helping user realize the importance of mentors, peers, and family support."
                )
            } catch (e: Exception) {
                "Future Guide: At thirty-five, I know for a fact that success is never crafted alone. Your support systems—Family Support: ${user.environment.familySupport}, Friend Circle: ${user.environment.friendCircle}—are directly warping your timeline. If you cultivate relationships with builders, find mentors, and align with peers, consistency moves from 50% to 90% without attrition."
            }
            relationshipsAssessment.value = response
            isRelationshipsLoading.value = false
        }
    }

    // Challenge Progressing
    fun progressChallenge(id: String, increment: Int) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            val latestUser = repository.getUser(user.email) ?: user
            val updated = latestUser.activeChallenges.map {
                if (it.id == id) {
                    val newProgress = (it.currentProgress + increment).coerceAtMost(it.targetDays)
                    val completed = newProgress >= it.targetDays
                    it.copy(currentProgress = newProgress, isCompleted = completed)
                } else it
            }
            val wasCompletedBefore = latestUser.activeChallenges.find { it.id == id }?.isCompleted ?: false
            val isCompletedNow = updated.find { it.id == id }?.isCompleted ?: false
            
            repository.updateUser(latestUser.copy(activeChallenges = updated))
            
            if (isCompletedNow && !wasCompletedBefore) {
                val challengeTitle = latestUser.activeChallenges.find { it.id == id }?.title ?: "Challenge"
                addFutureMomentInternal(
                     title = "Future Moment #${latestUser.futureMoments.size + 1}",
                     subtitle = "Ascent of Will ($challengeTitle completed)",
                     content = "You completed standard growth challenge \"$challengeTitle\". This successfully unlocks potential indices in your timeline.",
                     category = "Challenge"
                )
            }
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
    object PersonalLegend : Screen()
    object BookOfYou : Screen()
    object Journal : Screen()
    object RealityCheck : Screen()
    object UniverseMap : Screen()
    object TimeCapsule : Screen()
    object Constellation : Screen()
    object MirrorQuestion : Screen()
    object Documentary : Screen()
    object Challenges : Screen()
}
