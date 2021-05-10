package com.klamerek.fantasyrealms

import org.junit.Assert
import org.junit.jupiter.api.Test

class NormalizeStringTest {

    @Test
    fun stripAccent(){
        Assert.assertEquals("aeeeou", "àéèêöù".normalize())
    }
}
