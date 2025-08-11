package com.febriandev.vocary.ui.onboard

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OnboardViewModel @Inject constructor() : ViewModel() {

    private val _formStep = MutableStateFlow(FormStep.NAME)
    val formStep: StateFlow<FormStep> = _formStep

    val name = MutableStateFlow("")
    val old = MutableStateFlow("")
    val gender = MutableStateFlow("")
    val word = MutableStateFlow("")
    val level = MutableStateFlow("")
    val goal = MutableStateFlow("")

    private val _selectedCategory = MutableStateFlow<CategoryType?>(null)
    val selectedCategory: StateFlow<CategoryType?> = _selectedCategory.asStateFlow()

    private val _selectedTopic = MutableStateFlow<TopicType?>(null)
    val selectedTopic: StateFlow<TopicType?> = _selectedTopic.asStateFlow()

    fun nextStep() {
        _formStep.value = when (_formStep.value) {
            FormStep.AUTH -> FormStep.NAME
            FormStep.NAME -> FormStep.OLD
            FormStep.OLD -> FormStep.GENDER
            FormStep.GENDER -> FormStep.WORD
            FormStep.WORD -> FormStep.LEVEL
            FormStep.LEVEL -> FormStep.GOAL
            FormStep.GOAL -> FormStep.TOPIC
            FormStep.TOPIC -> FormStep.TOPIC

        }
    }

    fun prevStep() {
        _formStep.value = when (_formStep.value) {
            FormStep.TOPIC -> FormStep.GOAL
            FormStep.GOAL -> FormStep.LEVEL
            FormStep.LEVEL -> FormStep.WORD
            FormStep.WORD -> FormStep.GENDER
            FormStep.GENDER -> FormStep.OLD
            FormStep.OLD -> FormStep.NAME
            FormStep.NAME -> FormStep.AUTH
            FormStep.AUTH -> TODO()
        }
    }

    fun onTopicSelected(topic: TopicType) {
        _selectedTopic.value = topic
    }

}
