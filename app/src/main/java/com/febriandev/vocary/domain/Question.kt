package com.febriandev.vocary.domain

import com.febriandev.vocary.data.db.entity.UnifiedDefinitionEntity
import com.febriandev.vocary.data.db.entity.VocabularyEntity

enum class QuestionType { WORD_TO_DEFINITION, DEFINITION_TO_WORD }

data class Question(
    val vocabId: String,
    val type: QuestionType,
    val prompt: String,
    val options: List<String>,
    val correctAnswer: String
)

// Helper: ambil definisi acak yg valid
private fun VocabularyEntity.randomDefinitionOrNull(): UnifiedDefinitionEntity? =
    definitions.randomOrNull()

// Generator: pilih 1 target + 3 distraktor, lalu susun opsi
fun generateQuestion(vocabs: List<VocabularyEntity>): Question? {
    // butuh minimal 4 entri yang punya definisi
    val pool = vocabs.filter { it.definitions.isNotEmpty() }
    if (pool.size < 4) return null

    val target = pool.random()
    val targetDef = target.randomDefinitionOrNull() ?: return null

    // pilih tipe secara acak
    val type = if ((0..1).random() == 0)
        QuestionType.WORD_TO_DEFINITION
    else
        QuestionType.DEFINITION_TO_WORD

    return when (type) {
        QuestionType.WORD_TO_DEFINITION -> {
            val correct = targetDef.definition
            val distractors = pool.asSequence()
                .filter { it.id != target.id }
                .mapNotNull { it.randomDefinitionOrNull()?.definition }
                .distinct()
                .shuffled()
                .take(3)
                .toList()
            if (distractors.size < 3) return null

            val options = (distractors + correct).shuffled()
            Question(
                vocabId = target.id,
                type = type,
                prompt = "What is the meaning of the word '${target.word}'?",
                options = options,
                correctAnswer = correct
            )
        }
        QuestionType.DEFINITION_TO_WORD -> {
            val correct = target.word
            val distractors = pool.asSequence()
                .filter { it.id != target.id }
                .map { it.word }
                .distinct()
                .shuffled()
                .take(3)
                .toList()
            if (distractors.size < 3) return null

            val options = (distractors + correct).shuffled()
            Question(
                vocabId = target.id,
                type = type,
                prompt = "Which word matches the definition:\n“${targetDef.definition}”",
                options = options,
                correctAnswer = correct
            )
        }
    }
}
