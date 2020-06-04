package com.khdv.habitstracker.data.repositories

import com.khdv.habitstracker.data.db.Repetition
import com.khdv.habitstracker.data.db.RepetitionDao
import com.khdv.habitstracker.data.network.HabitsService
import com.khdv.habitstracker.data.utils.performSafely
import com.khdv.habitstracker.domain.models.Habit
import com.khdv.habitstracker.domain.repositories.RepetitionsRepository
import com.khdv.habitstracker.domain.shared.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepetitionsRepositoryImpl @Inject constructor(
    private val repetitionDao: RepetitionDao,
    private val habitsService: HabitsService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RepetitionsRepository {

    override suspend fun getHabitRepetitions(habit: Habit) = withContext(ioDispatcher) {
        repetitionDao.getByHabitId(habit.id).map { it.date }
    }

    override suspend fun insert(habit: Habit, date: Date): Result<Unit> {
        return withContext(ioDispatcher) {
            performSafely({ insert(habit, date) }) {
                habitsService.repeatHabit(HabitDone(date.time, habit.id))
            }.also {
                if (it is Result.Success)
                    repetitionDao.insert(Repetition(habit.id, date))
            }
        }
    }
}