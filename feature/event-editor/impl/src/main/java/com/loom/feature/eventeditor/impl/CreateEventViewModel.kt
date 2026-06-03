package com.loom.feature.eventeditor.impl

import androidx.lifecycle.ViewModel
import com.loom.core.data.repository.HomeRepository
import com.loom.core.data.repository.UserDataRepository
import com.loom.core.data.repository.UserRepository
import javax.inject.Inject

class CreateEventViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userDataRepository: UserDataRepository,
) : ViewModel() {


}