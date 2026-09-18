package com.example.myapplication.ui.role

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.SettingsRepository
import kotlinx.coroutines.launch

class RoleViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    fun selectRole(role: String, onRoleSelected: (String) -> Unit) {
        viewModelScope.launch {
            settingsRepository.setRole(role)
            onRoleSelected(role)
        }
    }
}
