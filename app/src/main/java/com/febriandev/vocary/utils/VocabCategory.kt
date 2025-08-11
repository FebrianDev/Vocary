package com.febriandev.vocary.utils

enum class VocabCategory(val label: String) {
    DAILY("Daily"),
    EDUCATION("Education"),
    HEALTH("Health"),
    WORK("Work"),
    TRAVEL("Travel"),
    TECHNOLOGY("Technology"),
    NATURE("Nature"),
    EMOTION("Emotion"),
    CULTURE("Culture"),
    ADVANCED("Advanced"),
    SPORTS("Sports"),
    ART("Art"),
    SHOPPING("Shopping"),
    COMMUNICATION("Communication"),
    TRANSPORT("Transport"),
    OTHER("Other")
}

val vocabTopics = mapOf(
    VocabCategory.DAILY to listOf("weather", "food", "drink", "routine", "clothes", "house", "market", "shopping", "kitchen", "bathroom", "sleep", "cleaning"),
    VocabCategory.EDUCATION to listOf("school", "subjects", "classroom", "university", "exam", "homework", "teacher", "student", "library"),
    VocabCategory.HEALTH to listOf("body", "illness", "medicine", "nutrition", "doctor", "hospital", "disease", "exercise", "fitness", "emotion", "mental"),
    VocabCategory.WORK to listOf("office", "jobs", "career", "salary", "meeting", "business", "project", "resume", "interview"),
    VocabCategory.TRAVEL to listOf("places", "hotel", "flight", "ticket", "airport", "beach", "mountain", "transport", "passport", "tourism"),
    VocabCategory.TECHNOLOGY to listOf("computer", "internet", "software", "hardware", "gadget", "media", "social", "device", "robot", "ai"),
    VocabCategory.NATURE to listOf("animals", "plants", "weather", "climate", "season", "forest", "ocean", "desert", "earthquake", "volcano"),
    VocabCategory.EMOTION to listOf("feelings", "love", "anger", "joy", "fear", "anxiety", "mood", "behavior", "personality"),
    VocabCategory.CULTURE to listOf("family", "holiday", "tradition", "music", "dance", "religion", "festival", "language", "custom"),
    VocabCategory.ADVANCED to listOf("economy", "finance", "science", "technology", "law", "politics", "history", "philosophy", "ethics", "energy"),
    VocabCategory.SPORTS to listOf("soccer", "basketball", "tennis", "cricket", "swimming", "gym", "athlete", "competition", "score"),
    VocabCategory.ART to listOf("music", "drawing", "painting", "design", "theater", "film", "fashion", "dance"),
    VocabCategory.SHOPPING to listOf("market", "price", "discount", "clothes", "mall", "cashier", "bag", "receipt"),
    VocabCategory.COMMUNICATION to listOf("phone", "email", "text", "chat", "letter", "call", "message", "meeting", "greeting"),
    VocabCategory.TRANSPORT to listOf("car", "bus", "train", "bike", "plane", "boat", "taxi", "traffic", "station"),
    VocabCategory.OTHER to listOf("random", "general", "mixed", "unknown")
)
