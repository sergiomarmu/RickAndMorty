package com.rickmorty.data.datasource

import io.mockk.MockKAnnotations
import org.junit.Before

abstract class DataSourceBaseTest {

    @Before
    open fun setUp() {
        MockKAnnotations.init(this)
    }
}