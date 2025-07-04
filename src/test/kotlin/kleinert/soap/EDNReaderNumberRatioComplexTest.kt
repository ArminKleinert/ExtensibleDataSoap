package kleinert.soap

import kleinert.soap.data.Complex
import kleinert.soap.data.Ratio
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNReaderNumberRatioComplexTest {
    private fun soap(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.defaultOptions)
    }

    private fun soapE(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
    }

    @Test
    fun parseRatio() {
        soap("0/1").let {
            Assertions.assertInstanceOf(Ratio::class.java, it)
            Assertions.assertEquals(Ratio.ZERO, it)
        }
        soap("-0/1").let {
            Assertions.assertInstanceOf(Ratio::class.java, it)
            Assertions.assertEquals(Ratio.ZERO, it)
        }

        soap("1/1").let {
            Assertions.assertInstanceOf(Ratio::class.java, it)
            Assertions.assertEquals(Ratio.valueOf(1), it)
        }
        soap("-1/1").let {
            Assertions.assertInstanceOf(Ratio::class.java, it)
            Assertions.assertEquals(Ratio.valueOf(1).negate(), it)
        }

        soap("1/2").let {
            Assertions.assertInstanceOf(Ratio::class.java, it)
            Assertions.assertEquals(Ratio.valueOf(1, 2), it)
        }
        soap("-1/2").let {
            Assertions.assertInstanceOf(Ratio::class.java, it)
            Assertions.assertEquals(Ratio.valueOf(1, 2).negate(), it)
        }

        soap("1/2567").let {
            Assertions.assertInstanceOf(Ratio::class.java, it)
            Assertions.assertEquals(Ratio.valueOf(1, 2567), it)
        }
        soap("-1/2567").let {
            Assertions.assertInstanceOf(Ratio::class.java, it)
            Assertions.assertEquals(Ratio.valueOf(1, 2567).negate(), it)
        }

        soap("123456789/2567").let {
            Assertions.assertInstanceOf(Ratio::class.java, it)
            Assertions.assertEquals(Ratio.valueOf(123456789, 2567), it)
        }
        soap("-123456789/2567").let {
            Assertions.assertInstanceOf(Ratio::class.java, it)
            Assertions.assertEquals(Ratio.valueOf(123456789, 2567).negate(), it)
        }
    }

    @Test
    fun parseComplex() {
        soapE("0i").let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(0, 0), it)
        }
        soapE("+0i").let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(0, 0), it)
        }
        soapE("-0i").let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(0.0, -0.0), it)
        }

        soapE("1i").let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(0, 1), it)
        }
        soapE("+1i").let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(0, 1), it)
        }
        soapE("-1i").let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(0, -1), it)
        }

        soapE("1+0i").let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(1), it)
        }
        soapE("+1+0i").let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(1), it)
        }
        soapE("-1+0i").let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(-1), it)
        }

        soapE("1+1i").let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(1, 1), it)
        }
        soapE("+1+1i").let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(1, 1), it)
        }
        soapE("-1+1i").let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(-1, 1), it)
        }

        soapE("1-1i").let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(1, -1), it)
        }
        soapE("+1-1i").let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(1, -1), it)
        }
        soapE("-1-1i").let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(-1, -1), it)
        }

        soapE("1.5+0i").let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(1.5), it)
        }
        soapE("+1.5+0i").let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(1.5, 0.0), it)
        }
        soapE("-1.5+0i").let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(-1.5, 0.0), it)
        }

        soapE("1.5+0.5i").let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(1.5, 0.5), it)
        }
        soapE("+1.5+0.5i").let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(1.5, 0.5), it)
        }
        soapE("-1.5+0.5i").let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(-1.5, 0.5), it)
        }
    }
}
