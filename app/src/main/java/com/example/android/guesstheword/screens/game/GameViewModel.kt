package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import timber.log.Timber

class GameViewModel : ViewModel() {

    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 6000L
    }

    private val timer: CountDownTimer

    // The current word
    private val _word = MutableLiveData<String>()
    val word : LiveData<String>
        get() = _word

    // The current score
    private val _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _score

    // Event for game end
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish : LiveData<Boolean>
        get() = _eventGameFinish

    // the timer countdown
    private val _timerCount = MutableLiveData<Long>()

    val currentTimeString = Transformations.map(_timerCount, { time ->
        DateUtils.formatElapsedTime(time / 1000)
    })

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init {
        resetList()
        nextWord()
        _score.value = 0
        _word.value = ""
        _timerCount.value = 0
        _eventGameFinish.value = false
        Timber.i("GameViewModel created!")

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){
            override fun onTick(millisUntilFinished: Long) {
                _timerCount.value = millisUntilFinished
            }

            override fun onFinish() {
                _eventGameFinish.value = true
            }
        }

        nextWord()
        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        Timber.i("GameViewModel destroyed!")
    }


    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }

    fun onSkip() {
        _score.value = _score.value?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = _score.value?.plus(1)
        nextWord()
    }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }
}