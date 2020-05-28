package com.khdv.habitstracker.domain.interactors

import com.khdv.habitstracker.domain.repositories.HabitsRepository
import javax.inject.Inject

class GetHabitUseCase @Inject constructor(private val repository: HabitsRepository) {

    suspend fun getHabit(id: String) = repository.getById(id)
}