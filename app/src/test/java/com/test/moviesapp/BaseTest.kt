package com.test.moviesapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

abstract class BaseTest {

    /*@ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()*/

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    abstract fun setup()
}