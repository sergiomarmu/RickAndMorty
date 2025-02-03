package com.rickmorty.ui

import com.rickmorty.ui.utils.MainDispatcherRule
import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Rule

abstract class BaseViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    open fun setUp() {
        MockKAnnotations.init(this)
    }

}