package io.github.bartvhelvert.jagex.fs

import io.github.bartvhelvert.jagex.fs.util.whirlPoolHashByteCount
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.math.BigInteger

class JagexCacheChecksumTest {
    @ParameterizedTest
    @MethodSource("encodeDecodeNoWPTestArgs", "encodeDecodeWPTestArgs")
    @ExperimentalUnsignedTypes
    fun encodeDecodeTest(
        whirlpool: Boolean,
        cacheCheckSum: CacheChecksum,
        mod: BigInteger?,
        pubKey: BigInteger?,
        privateKey: BigInteger?
    ) {
        Assertions.assertEquals(cacheCheckSum,
            CacheChecksum.decode(
                cacheCheckSum.encode(mod, pubKey),
                whirlpool,
                mod,
                privateKey
            )
        )
    }

    companion object {
        @JvmStatic
        fun encodeDecodeNoWPTestArgs() = listOf(
            Arguments.of(
                true,
                CacheChecksum(
                    arrayOf(
                        DictionaryChecksum(
                            crc = 87585,
                            version = 1,
                            fileCount = 0,
                            size = 12,
                            whirlpoolDigest = ByteArray(whirlPoolHashByteCount)
                        ),
                        DictionaryChecksum(
                            crc = 3331,
                            version = 3,
                            fileCount = 3,
                            size = 12,
                            whirlpoolDigest = ByteArray(whirlPoolHashByteCount)
                        )
                    )
                ),
                null, null, null
            )
        )

        @JvmStatic
        fun encodeDecodeWPTestArgs() = listOf(
            Arguments.of(
                false,
                CacheChecksum(
                    arrayOf(
                        DictionaryChecksum(
                            crc = 87585,
                            version = 1,
                            fileCount = 0,
                            size = 0,
                            whirlpoolDigest = null
                        ),
                        DictionaryChecksum(
                            crc = 3331,
                            version = 3,
                            fileCount = 0,
                            size = 0,
                            whirlpoolDigest = null
                        )
                    )
                ),
                BigInteger.valueOf(3233), // mod
                BigInteger.valueOf(17), // pub key
                BigInteger.valueOf(413) // private key
            ),
            Arguments.of(
                false,
                CacheChecksum(
                    arrayOf(
                        DictionaryChecksum(
                            crc = 87585,
                            version = 1,
                            fileCount = 0,
                            size = 0,
                            whirlpoolDigest = null
                        ),
                        DictionaryChecksum(
                            crc = 3331,
                            version = 3,
                            fileCount = 0,
                            size = 0,
                            whirlpoolDigest = null
                        )
                    )
                ),
                null, null, null
            )
        )
    }
}