package com.khdv.habitstracker.domain.interactors

import com.khdv.habitstracker.domain.models.Habit
import com.khdv.habitstracker.domain.shared.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadHabitsUseCase @Inject constructor(private val repository: HabitsRepository) {

    fun loadHabits(): Flow<Result<List<Habit>>> = repository.getAll()
}