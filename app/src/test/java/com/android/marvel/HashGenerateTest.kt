package com.android.marvel

import com.android.marvel.app.utils.HashGenerate
import org.junit.Assert.assertEquals
import org.junit.Test

class HashGenerateTest {

    @Test
    fun `test generate() should return correct MD5 hash`() {
        val timestamp: Long = 1234567890
        val privateKey = "privateKey"
        val publicKey = "publicKey"

        val result = HashGenerate.generate(timestamp, privateKey, publicKey)

        val expectedHash = "d91c1876a7d2e41b0e247d13252591c6"
        assertEquals(expectedHash, result)
    }
}
