package com.example.data

import com.example.data.model.RoadmapStep
import com.example.data.model.SkillAssessmentItem

object RoadmapGenerator {

    fun generateRoadmapAndSkills(goalName: String): Pair<List<RoadmapStep>, List<SkillAssessmentItem>> {
        val normalized = goalName.lowercase().trim()
        val skills: List<SkillAssessmentItem>
        val steps: List<RoadmapStep>

        when {
            normalized.contains("ai") || normalized.contains("engineer") || normalized.contains("developer") || normalized.contains("tech") -> {
                skills = listOf(
                    SkillAssessmentItem("Python Programming", "Not Started"),
                    SkillAssessmentItem("Data Structures & Algorithms", "Not Started"),
                    SkillAssessmentItem("Mathematics & Probability", "Not Started"),
                    SkillAssessmentItem("Statistics", "Not Started"),
                    SkillAssessmentItem("Machine Learning Models", "Not Started"),
                    SkillAssessmentItem("GitHub & Version Control", "Not Started"),
                    SkillAssessmentItem("Deep Learning & NLP", "Not Started"),
                    SkillAssessmentItem("Projects & Apps", "Not Started"),
                    SkillAssessmentItem("Problem Solving", "Not Started")
                )
                steps = listOf(
                    RoadmapStep(
                        title = "Computer Science Fundamentals & Python",
                        category = "Foundation",
                        description = "Understand computer architecture, basic terminal commands, and core Python variables, flow-control, lists, and OOP.",
                        resource = "Harvard CS50 Introduction to Computer Science",
                        youtube = "FreeCodeCamp Python for Beginners Course",
                        course = "edX CS50: Intro to Computer Science",
                        practice = "HackerRank Python Basic exercises",
                        miniProject = "Command Line Task Planner with JSON file storage"
                    ),
                    RoadmapStep(
                        title = "Data Structures & Algorithms (DSA)",
                        category = "Foundation",
                        description = "Master arrays, lists, hash tables, sorting/searching, recursion, tree traversals, and basic graphs.",
                        resource = "Clément Mihailescu's AlgoExpert or CLRS book",
                        youtube = "Abdul Bari Data Structures Video Playlist",
                        course = "Coursera Princeton Algorithms Part I",
                        practice = "LeetCode Top 75 Blind questions",
                        miniProject = "Interactive maze visualizer using custom DFS/BFS searches"
                    ),
                    RoadmapStep(
                        title = "Mathematics & Applied Statistics",
                        category = "Intermediate",
                        description = "In-depth study of Linear Algebra (vectors, matrices, eigenvalues), Calculus (derivatives, partial gradients), and Probability distribution curves.",
                        resource = "Mathematics for Machine Learning Textbook",
                        youtube = "3Blue1Brown Essence of Linear Algebra",
                        course = "MIT 18.06 Linear Algebra Online",
                        practice = "Kaggle Math Exercises",
                        miniProject = "Build multi-variable linear regression solver from scratch"
                    ),
                    RoadmapStep(
                        title = "Machine Learning Foundations",
                        category = "Intermediate",
                        description = "Learn supervised learning (Regression, Classification, SVM) and unsupervised learning (K-Means, PCA) using Scikit-Learn.",
                        resource = "Aurelien Geron's Hands-On Machine Learning Book",
                        youtube = "StatQuest Machine Learning with Josh Starmer",
                        course = "Stanford Online CS229: Machine Learning",
                        practice = "Kaggle House Prices competition",
                        miniProject = "Spam classification filter using Naive Bayes classifier"
                    ),
                    RoadmapStep(
                        title = "Deep Learning & Neural Networks",
                        category = "Advanced",
                        description = "Dive into forward/backpropagation, Activation functions, Optimizers (Adam, SGD), and neural frameworks like PyTorch or TensorFlow.",
                        resource = "DeepLearning.AI specialized course series",
                        youtube = "3Blue1Brown Neural Networks Playlists",
                        course = "HuggingFace Deep RL or NLP Course",
                        practice = "PyTorch Official Tutorials",
                        miniProject = "Image recognition network on CIFAR-10 data utilizing a CNN"
                    ),
                    RoadmapStep(
                        title = "Large Language Models & Transformers",
                        category = "Advanced",
                        description = "Explore attention mechanisms, self-attention, GPT-style architectures, embedding vectors, and RAG (Retrieval Augmented Generation).",
                        resource = "HuggingFace NLP Foundations",
                        youtube = "Andrej Karpathy's 'Intro to LLMs' or 'Let's build GPT'",
                        course = "DeepLearning.ai Prompt Engineering",
                        practice = "Build LLM agents on Hugging Face Spaces",
                        miniProject = "Local PDF semantic Q&A chatbot using Gemini API and vector indexes"
                    ),
                    RoadmapStep(
                        title = "Collaboration & Version Control (Git/GitHub)",
                        category = "Professional",
                        description = "Master branch creation, merging conflicts, Git rebase, pull requests, and automated review actions (CI/CD).",
                        resource = "GitHub Skills interactive guide doc",
                        youtube = "Git & GitHub Crash Course on FreeCodeCamp",
                        course = "Udacity Version Control with Git",
                        practice = "Contribute to a public open-source project",
                        miniProject = "Automate lint and compilation testing pipelines via GitHub Actions"
                    ),
                    RoadmapStep(
                        title = "Capstone Portfolio & Production Deployment",
                        category = "Professional",
                        description = "Package model binaries, design interactive frontends (Compose/Web), containerize with Docker, and host on Cloud platforms.",
                        resource = "Full Stack Deep Learning guides",
                        youtube = "Jeff Heaton Machine Learning channels",
                        course = "Docker and Kubernetes: Complete Guide",
                        practice = "Hugging Face Spaces or Render Cloud deployments",
                        miniProject = "Deploy a production-ready handwriting digitizer app with REST API"
                    )
                )
            }
            normalized.contains("doctor") || normalized.contains("medicine") || normalized.contains("health") || normalized.contains("surgeon") -> {
                skills = listOf(
                    SkillAssessmentItem("Anatomy & Physiology", "Not Started"),
                    SkillAssessmentItem("Organic Chemistry & Biology", "Not Started"),
                    SkillAssessmentItem("Patient Empathy", "Not Started"),
                    SkillAssessmentItem("Medical Ethics", "Not Started"),
                    SkillAssessmentItem("Clinical Diagnostics", "Not Started"),
                    SkillAssessmentItem("Pharmacology Basics", "Not Started"),
                    SkillAssessmentItem("Medical Research & Journals", "Not Started"),
                    SkillAssessmentItem("First Aid & Emergency Procedures", "Not Started")
                )
                steps = listOf(
                    RoadmapStep(
                        title = "Pre-Medical Scientific Foundations",
                        category = "Foundation",
                        description = "Study advanced cellular biology, organic and inorganic chemistry, molecular genetics, and physics principles.",
                        resource = "Campbell Biology textbook",
                        youtube = "CrashCourse Biology and Chemistry",
                        course = "Khan Academy MCAT Prep courses",
                        practice = "Practice MCAT mock exams",
                        miniProject = "Create detailed diagram guides explaining ATP synthesis cycles"
                    ),
                    RoadmapStep(
                        title = "Human Anatomy & Physiology",
                        category = "Foundation",
                        description = "Understand human organ systems, skeletal mapping, blood circulation, gas exchanges, and nervous signaling.",
                        resource = "Gray's Anatomy for Students",
                        youtube = "Ninja Nerd Lectures on Human Physiology",
                        course = "Harvard Online Human Anatomy",
                        practice = "Anatomy quiz apps",
                        miniProject = "Build interactive flashcard stack covering cranial nerves"
                    ),
                    RoadmapStep(
                        title = "Medical Ethics & Communication",
                        category = "Intermediate",
                        description = "Learn legal guidelines, the Hippocratic oath, patient confidentiality (HIPAA), active listening, and delivering sensitive diagnoses.",
                        resource = "Principles of Biomedical Ethics by Beauchamp",
                        youtube = "Geoff Norman clinical reasoning playlists",
                        course = "Penn State Bioethics course",
                        practice = "Roleplaying case studies with peers",
                        miniProject = "Case study paper detailing ethical patient triage policies"
                    ),
                    RoadmapStep(
                        title = "Pharmacology & Diagnostic Pathology",
                        category = "Intermediate",
                        description = "Study drug classes, pharmacokinetics, dynamic interactions, and how pathogens (bacteria, viruses) damage human tissues.",
                        resource = "Rang & Dale's Pharmacology",
                        youtube = "Ninja Nerd Pathology lectures",
                        course = "Coursera Clinical Terminology",
                        practice = "Medscape clinical diagnostic cases",
                        miniProject = "Write comparison guide detailing antibiotic vs antiviral modes"
                    ),
                    RoadmapStep(
                        title = "Clinical Diagnostic Reasoning",
                        category = "Advanced",
                        description = "Develop deductive paths: analyzing laboratory markers, interpreting ECGs, identifying chest abnormalities on X-Rays, and prioritizing differential diagnoses.",
                        resource = "Sapira's Art and Science of Bedside Diagnosis",
                        youtube = "Strong Medicine Diagnostic reasoning video list",
                        course = "UpToDate clinical simulation logs",
                        practice = "NEJM Clinical Cases portal",
                        miniProject = "Present a differential diagnosis flowchart for chronic heart failures"
                    ),
                    RoadmapStep(
                        title = "First Aid, Surgical Safety, & ER Care",
                        category = "Advanced",
                        description = "Master CPR, wound packing, basic life support (BLS), suturing techniques, and sterile scrub protocols.",
                        resource = "American Red Cross Wilderness & First Aid guides",
                        youtube = "Online MedEd surgical rotation guides",
                        course = "BLS Certification Course",
                        practice = "Suture practice kit simulations",
                        miniProject = "Assemble a first-response triage manual with visual algorithms"
                    ),
                    RoadmapStep(
                        title = "Clinical Internships & Shadowing",
                        category = "Professional",
                        description = "Shadow licensed physicians, record procedural logs, observe doctor-patient consults, and perform physical rounds safely.",
                        resource = "AAMC Shadowing Guidelines",
                        youtube = "Doctor Mike residency advice vlogs",
                        course = "Local Hospital Volunteering programs",
                        practice = "Record interactive symptom histories",
                        miniProject = "Compile daily analytical journals covering 50 hours of clinical observations"
                    ),
                    RoadmapStep(
                        title = "Medical Research Paper & Specialized Match Prep",
                        category = "Professional",
                        description = "Participate in academic clinical trials, author peer-reviewed abstracts, understand medical research, and study for match interviews.",
                        resource = "PubMed Central citation guidelines",
                        youtube = "Stanford Medicine admissions guides",
                        course = "UWorld USMLE exam prep suites",
                        practice = "Simulated residency board interviews",
                        miniProject = "Draft a formal clinical hypothesis paper under mentor supervision"
                    )
                )
            }
            normalized.contains("entrepreneur") || normalized.contains("business") || normalized.contains("startup") || normalized.contains("founder") -> {
                skills = listOf(
                    SkillAssessmentItem("Business Strategy", "Not Started"),
                    SkillAssessmentItem("Financial Modeling", "Not Started"),
                    SkillAssessmentItem("Product Design & MVP", "Not Started"),
                    SkillAssessmentItem("Pitching & Communication", "Not Started"),
                    SkillAssessmentItem("Sales & Marketing", "Not Started"),
                    SkillAssessmentItem("Customer Interviewing", "Not Started"),
                    SkillAssessmentItem("Legal & Compliance", "Not Started"),
                    SkillAssessmentItem("Leadership & Scaling", "Not Started")
                )
                steps = listOf(
                    RoadmapStep(
                        title = "Mindset, Ideation & Problem Discovery",
                        category = "Foundation",
                        description = "Shift from consumer to maker. Identify acute pain points, target specific demographics, and perform market sizing calculations.",
                        resource = "The Lean Startup by Eric Ries",
                        youtube = "Y Combinator Startup School channel",
                        course = "Y Combinator Startup School (Online Free)",
                        practice = "Run 5 customer gap interviews using 'The Mom Test'",
                        miniProject = "Startup Idea Pitch Deck outlining customer problem, solution, TAM"
                    ),
                    RoadmapStep(
                        title = "Product Design & MVP Prototyping",
                        category = "Foundation",
                        description = "Define core value offering. Build functional click-through UX layouts and launch landing pages with zero coding tools.",
                        resource = "Sprint by Jake Knapp",
                        youtube = "Figma official layout tutorial playlists",
                        course = "Interaction Design Foundation: MVP basics",
                        practice = "Design high-contrast mockups using Figma",
                        miniProject = "Launch fully functional Carrd sales landing page with email collectors"
                    ),
                    RoadmapStep(
                        title = "Customer Feedback & Dynamic Validation",
                        category = "Intermediate",
                        description = "Set up analytics, measure click interest, iterate on pricing options, and find initial product-market fit metrics.",
                        resource = "The Mom Test by Rob Fitzpatrick",
                        youtube = "Garry Tan Venture advice guides",
                        course = "Udacity Product Validation",
                        practice = "Collect 50 pre-launch waitlist signups",
                        miniProject = "Detailed feedback register log reflecting product feature pivots"
                    ),
                    RoadmapStep(
                        title = "Financial Analytics & Accounting",
                        category = "Intermediate",
                        description = "Establish bookkeeping basics, project unit economics (LTV/CAC ratio), calculate burn-rate runways, and draft cashflows.",
                        resource = "Financial Intelligence for Entrepreneurs book",
                        youtube = "Slidebean Startup Finance videos",
                        course = "Wharton School Introduction to Corporate Finance",
                        practice = "Draft pre-revenue sheets projecting startup costs",
                        miniProject = "Interactive automated 3-year P&L forecast spreadsheet in Sheets"
                    ),
                    RoadmapStep(
                        title = "Sales, Messaging, & Growth Marketing",
                        category = "Advanced",
                        description = "Formulate high-conversion copy, optimize search visibility (SEO), run email campaigns, configure targeted social advertising, and close deals.",
                        resource = "Contagious: Why Things Catch On by Jonah Berger",
                        youtube = "Miles Beckler SEO and digital strategy crash classes",
                        course = "Google Digital Marketing Certificate",
                        practice = "Run localized organic social content campaign",
                        miniProject = "Growth manual containing detailed high-converting cold-outreach templates"
                    ),
                    RoadmapStep(
                        title = "Legal, Governance & Fundraising Strategies",
                        category = "Advanced",
                        description = "Demystify startup registration, equity distributions, NDAs, SAFE note agreements, venture investments, and pre-money evaluation curves.",
                        resource = "Venture Deals by Brad Feld",
                        youtube = "Y Combinator 'How to Raise Money' videos",
                        course = "Coursera Venture Capital and Startups",
                        practice = "Draft mock cap-tables modeling dilution shares",
                        miniProject = "Secure investment presentation with standard term sheet variables"
                    ),
                    RoadmapStep(
                        title = "First Customer Sales & Operational Scale",
                        category = "Professional",
                        description = "Acquire paying customers, launch billing accounts, track Net Promoter Score (NPS), and establish reliable feedback loops.",
                        resource = "Zero to One by Peter Thiel",
                        youtube = "Alex Hormozi Scaling Business strategies",
                        course = "Harvard Business School Launching Tech Ventures",
                        practice = "Onboard 5 active paying beta users",
                        miniProject = "Weekly dashboard tracking subscription churn rate & retention KPI"
                    ),
                    RoadmapStep(
                        title = "Team Leadership, Culture, & Launching Global",
                        category = "Professional",
                        description = "Delegate core duties, hire specialist engineers, run sprint cycles, coordinate distributed employees, and manage brand public relations.",
                        resource = "High Output Management by Andrew Grove",
                        youtube = "Silicon Valley venture podcast series",
                        course = "HBS Leadership in Turbulent Times",
                        practice = "Run retrospective team alignment meetings",
                        miniProject = "A formal standard operating procedure manual detailing startup structure"
                    )
                )
            }
            normalized.contains("artist") || normalized.contains("creative") || normalized.contains("writer") || normalized.contains("music") || normalized.contains("design") -> {
                skills = listOf(
                    SkillAssessmentItem("Composition & Colors", "Not Started"),
                    SkillAssessmentItem("Figma & Digital Tools", "Not Started"),
                    SkillAssessmentItem("Art History & Styles", "Not Started"),
                    SkillAssessmentItem("Anatomy & Sketching", "Not Started"),
                    SkillAssessmentItem("3D Modeling & Rendering", "Not Started"),
                    SkillAssessmentItem("Portfolio Presentation", "Not Started"),
                    SkillAssessmentItem("Brand Identity Design", "Not Started"),
                    SkillAssessmentItem("Freelancing & Licensing", "Not Started")
                )
                steps = listOf(
                    RoadmapStep(
                        title = "Drawing Fundamentals & Visual Observation",
                        category = "Foundation",
                        description = "Master perspective grids, light behaviors, drop-shadow mappings, line weights, organic shapes, and charcoal/graphite control.",
                        resource = "Drawing on the Right Side of the Brain by Betty Edwards",
                        youtube = "Proko Anatomy and Drawing basics",
                        course = "Drawabox.com free structured course",
                        practice = "Draft 50 unique physical posture sketches",
                        miniProject = "Complete 5-angle highly detailed still-life rendering sheet"
                    ),
                    RoadmapStep(
                        title = "Color Theory & Digital Canvas Suite",
                        category = "Foundation",
                        description = "Learn color values, hue combinations, light gradients, brush dynamics, layer hierarchies, and digital software like Photoshop or Procreate.",
                        resource = "Color and Light by James Gurney",
                        youtube = "Ctrl+Paint Digital Drawing free library",
                        course = "Coursera Graphic Design Foundations",
                        practice = "Paint high-contrast visual portrait assets",
                        miniProject = "Create three dynamic thematic background plates in full color"
                    ),
                    RoadmapStep(
                        title = "Digital Vector Design & UI layouts",
                        category = "Intermediate",
                        description = "Understand vector math, anchor points, pen tool precision, font layouts, hierarchy styling, grid ratios, and Figma design tool workflows.",
                        resource = "Design Systems guidelines on Material.io",
                        youtube = "Flux Academy Web and Figma design tutorials",
                        course = "Google UX Design Professional Certificate",
                        practice = "Clone 5 top-rated landing page layouts in Figma",
                        miniProject = "Design custom pixel-perfect mobile portfolio application design"
                    ),
                    RoadmapStep(
                        title = "Art History & Conceptual Themes",
                        category = "Intermediate",
                        description = "Analyze Classical, Impressionism, Surrealism, Brutalism, Bauhaus, and modern aesthetics to inspire meaningful concepts.",
                        resource = "The Story of Art by E.H. Gombrich",
                        youtube = "The Art Assignment by PBS",
                        course = "MoMA Modern Art and Ideas on Coursera",
                        practice = "Deconstruct structural compositions of masterpieces",
                        miniProject = "Concept art portfolio analyzing and applying Bauhaus styles"
                    ),
                    RoadmapStep(
                        title = "3D Computer Graphics & Modeling",
                        category = "Advanced",
                        description = "Master Blender, Maya, or Cinema4D: mesh sculpting, vertex maps, keyframe animations, texture painting, and path tracing.",
                        resource = "Blender Manual and official wiki guides",
                        youtube = "Blender Guru donut modeling playlist series",
                        course = "CG Cookie Blender training center",
                        practice = "Model three-dimensional character props",
                        miniProject = "A 10-second high-detail animated isometric room sequence"
                    ),
                    RoadmapStep(
                        title = "Interactive Media & Generative Art",
                        category = "Advanced",
                        description = "Combine code and art: shader programming, generative geometry, particle physics, and projection mapping tools.",
                        resource = "The Nature of Code by Daniel Shiffman",
                        youtube = "The Coding Train Processing / p5.js videos",
                        course = "Kadenze Interactive Art foundations",
                        practice = "Write algorithmic pattern generator drawings",
                        miniProject = "Dynamic audio-reactive visualizer canvas code"
                    ),
                    RoadmapStep(
                        title = "Online Portfolio & Branding Identity",
                        category = "Professional",
                        description = "Establish visual identity: logos, typographies, personal sites (Behance, ArtStation), and high-resolution photo compilations.",
                        resource = "Show Your Work! by Austin Kleon",
                        youtube = "The Futur branding & design masterclasses",
                        course = "Udemy Social Media Design portfolios",
                        practice = "Build and refine public ArtStation profiles",
                        miniProject = "Publish self-authored cohesive branding guide document"
                    ),
                    RoadmapStep(
                        title = "Creative Business, Licensing, & Freelance",
                        category = "Professional",
                        description = "Understand artist contracts, NDA clauses, intellectual copyrights, commercial royalty licensing, invoicing schemes, and tax codes.",
                        resource = "Graphic Artists Guild Handbook of Pricing and Ethical Guidelines",
                        youtube = "Freelance UX and Creative Consultant diaries",
                        course = "LinkedIn Creative Business courses",
                        practice = "Compose custom service contract templates",
                        miniProject = "A detailed client outreach campaign template and invoice tracker"
                    )
                )
            }
            else -> {
                // Generative Fallback for generic goals
                skills = listOf(
                    SkillAssessmentItem("Core Domain Knowledge", "Not Started"),
                    SkillAssessmentItem("Essential Tools & Technology", "Not Started"),
                    SkillAssessmentItem("Communication & Presentation", "Not Started"),
                    SkillAssessmentItem("Problem Solving & Critical Thinking", "Not Started"),
                    SkillAssessmentItem("Hands-on Practical Experience", "Not Started"),
                    SkillAssessmentItem("Collaborative Teamwork", "Not Started"),
                    SkillAssessmentItem("Independet Projects & Portfolio", "Not Started"),
                    SkillAssessmentItem("Professional Networking", "Not Started")
                )
                steps = listOf(
                    RoadmapStep(
                        title = "$goalName Foundations & Basic Principles",
                        category = "Foundation",
                        description = "Acquire general domain overview, understanding core vocabularies, timelines, and primary reference materials.",
                        resource = "Introductory textbooks or online wikis",
                        youtube = "Basic introduction lectures Search",
                        course = "Introductory Online Certificate",
                        practice = "Basic vocabulary checks",
                        miniProject = "Create detailed conceptual dictionary detailing 20 core topics"
                    ),
                    RoadmapStep(
                        title = "Essential Tools of the Trade",
                        category = "Foundation",
                        description = "Learn how to use standard software, hardware, calculators, or platforms required by daily professionals in this sector.",
                        resource = "Official user manuals and technical checklists",
                        youtube = "Step by step tool guides",
                        course = "Tool Certification Course",
                        practice = "Repeat basic workflows",
                        miniProject = "Setup and customize personal environment for operations"
                    ),
                    RoadmapStep(
                        title = "Practical Application & Practice Workflows",
                        category = "Intermediate",
                        description = "Translate theories to practices. Learn standard operating procedures (SOP), workflows, and standard metrics.",
                        resource = "Industry case studies and reviews",
                        youtube = "Professional daily diaries videos",
                        course = "Practical Application Bootcamp",
                        practice = "Small structured exercises",
                        miniProject = "Draft comprehensive report detailing an industry workflow simulation"
                    ),
                    RoadmapStep(
                        title = "Critical Thinking & Analysis Case Studies",
                        category = "Intermediate",
                        description = "Develop troubleshooting mindsets and problem-solving structures to adapt to complex scenarios.",
                        resource = "Historical industry records and journals",
                        youtube = "Specialist breakdown panels",
                        course = "Problem Solving Strategy courses",
                        practice = "Solve simulated scenario challenges",
                        miniProject = "Draft a differential solutions report comparing three problems"
                    ),
                    RoadmapStep(
                        title = "Advanced Specialization Topics",
                        category = "Advanced",
                        description = "Study latest trends, modern methodologies, automated tools, and bleeding-edge standards of this practice.",
                        resource = "Advanced technical papers or manuals",
                        youtube = "Modern conference lectures streams",
                        course = "Advanced Specialized Certificate",
                        practice = "Solve complex advanced quizzes",
                        miniProject = "A research report proposing improvements in modern operations"
                    ),
                    RoadmapStep(
                        title = "Productive Tool Stack Integration",
                        category = "Advanced",
                        description = "Optimize productivity. Seamlessly integrate planning dashboards, collaboration networks, and tracking pipelines.",
                        resource = "Professional collaboration frameworks",
                        youtube = "Ecosystem tools overview videos",
                        course = "Productivity and Workflow Design",
                        practice = "Contribute case updates to teams",
                        miniProject = "A standardized, multi-step roadmap template ready for reuse"
                    ),
                    RoadmapStep(
                        title = "Complete Capstone Project",
                        category = "Professional",
                        description = "Demonstrate expertise. Combine all previous steps into a large, independent, end-to-end case study or physical project.",
                        resource = "Authoritative portfolios examples",
                        youtube = "Showcase videos of senior projects",
                        course = "Professional Portfolio Preparation",
                        practice = "Peer review code/design presentations",
                        miniProject = "A complete, public-facing, highly polished capstone portfolio item"
                    ),
                    RoadmapStep(
                        title = "Industry Launch & Professional Networking",
                        category = "Professional",
                        description = "Establish client channels, polish professional profiles, find mentors, join organizations, and secure interviews.",
                        resource = "Professional networking manuals",
                        youtube = "Career launch strategies videos",
                        course = "Business Communication & Networking",
                        practice = "Participate in simulated professional screenings",
                        miniProject = "A custom client acquisition script and 3-month career guide tracker"
                    )
                )
            }
        }

        return Pair(steps, skills)
    }
}
