package com.rickmorty.domain

import io.mockk.MockKAnnotations
import org.junit.Before

abstract class BaseUseCaseTest {

    @Before
    open fun setUp() {
        MockKAnnotations.init(this)
    }

}