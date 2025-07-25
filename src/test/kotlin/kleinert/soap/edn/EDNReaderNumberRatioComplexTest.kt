package kleinert.soap.edn

import kleinert.soap.data.Complex
import kleinert.soap.data.Ratio
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNReaderNumberRatioComplexTest {
    @Test
    fun parseRatio() {
        EDN.read("0/1").let {
            Assertions.assertTrue(it is Ratio)
            Assertions.assertEquals(Ratio.ZERO, it)
        }
        EDN.read("-0/1").let {
            Assertions.assertTrue(it is Ratio)
            Assertions.assertEquals(Ratio.ZERO, it)
        }

        EDN.read("1/1").let {
            Assertions.assertTrue(it is Ratio)
            Assertions.assertEquals(Ratio.valueOf(1), it)
        }
        EDN.read("-1/1").let {
            Assertions.assertTrue(it is Ratio)
            Assertions.assertEquals(Ratio.valueOf(1).negate(), it)
        }

        EDN.read("1/2").let {
            Assertions.assertTrue(it is Ratio)
            Assertions.assertEquals(Ratio.valueOf(1, 2), it)
        }
        EDN.read("-1/2").let {
            Assertions.assertTrue(it is Ratio)
            Assertions.assertEquals(Ratio.valueOf(1, 2).negate(), it)
        }

        EDN.read("1/2567").let {
            Assertions.assertTrue(it is Ratio)
            Assertions.assertEquals(Ratio.valueOf(1, 2567), it)
        }
        EDN.read("-1/2567").let {
            Assertions.assertTrue(it is Ratio)
            Assertions.assertEquals(Ratio.valueOf(1, 2567).negate(), it)
        }

        EDN.read("123456789/2567").let {
            Assertions.assertTrue(it is Ratio)
            Assertions.assertEquals(Ratio.valueOf(123456789, 2567), it)
        }
        EDN.read("-123456789/2567").let {
            Assertions.assertTrue(it is Ratio)
            Assertions.assertEquals(Ratio.valueOf(123456789, 2567).negate(), it)
        }
    }

    @Test
    fun parseComplex2() {
        val optionsWithComplex = EDN.defaultOptions.copy(allowComplexNumberLiterals = true)

        EDN.read("1i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(0, 1), it)
        }
        EDN.read("+1i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(0, +1), it)
        }
        EDN.read("-1i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(0, -1), it)
        }
        EDN.read("1.2i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(0.0, 1.2), it)
        }
        EDN.read("+1.2i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(0.0, 1.2), it)
        }
        EDN.read("-1.2i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(0.0, -1.2), it)
        }

        EDN.read("1+i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(1, 1), it)
        }
        EDN.read("+1+i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(+1, 1), it)
        }
        EDN.read("-1+i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(-1, 1), it)
        }
        EDN.read("1.5+i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(1.5, 1.0), it)
        }
        EDN.read("+1.5+i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(+1.5, 1.0), it)
        }

        EDN.read("1+2i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(1, 2), it)
        }
        EDN.read("1-2i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(1, -2), it)
        }
        EDN.read("1+2.3i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(1.0, 2.3), it)
        }
        EDN.read("1-2.3i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(1.0, -2.3), it)
        }
        EDN.read("+1+2i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(+1, 2), it)
        }
        EDN.read("+1-2i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(+1, -2), it)
        }
        EDN.read("+1+2.3i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(+1.0, +2.3), it)
        }
        EDN.read("+1-2.3i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(+1.0, -2.3), it)
        }
        EDN.read("1.5+2i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(1.5, 2.0), it)
        }
        EDN.read("1.5-2i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(1.5, -2.0), it)
        }
        EDN.read("1.5+2.3i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(1.5, 2.3), it)
        }
        EDN.read("1.5-2.3i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(1.5, -2.3), it)
        }
        EDN.read("+1.5+2i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(1.5, 2.0), it)
        }
        EDN.read("+1.5-2i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(1.5, -2.0), it)
        }
        EDN.read("+1.5+2.3i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(1.5, 2.3), it)
        }
        EDN.read("+1.5-2.3i", optionsWithComplex).let {
            Assertions.assertTrue(it is Complex)
            Assertions.assertEquals(Complex.valueOf(1.5, -2.3), it)
        }
    }

    //            "([+\\-]?\\d+(.\\d)?+i)", // imaginary only
    //            "([+\\-]?\\d+(.\\d+)?[+\\-](\\d+(.\\d+)?)?i)" // real only or real +/- imaginary
    private val complexRegex = Regex("([+\\-]?\\d+(.\\d)?+i)|([+\\-]?\\d+(.\\d+)?[+\\-](\\d+(.\\d+)?)?i)")

    @Test
    fun testComplexNumberRecognitionNoMatch() {
        Assertions.assertFalse(complexRegex.matches("i"))
        Assertions.assertFalse(complexRegex.matches("+i"))
        Assertions.assertFalse(complexRegex.matches("-i"))
        Assertions.assertFalse(complexRegex.matches("1.i"))
        Assertions.assertFalse(complexRegex.matches(".1i"))
        Assertions.assertFalse(complexRegex.matches("+1.i"))
        Assertions.assertFalse(complexRegex.matches("+.1i"))
    }

    @Test
    fun testComplexNumberRecognitionMatchImagOnly() {
        Assertions.assertTrue(complexRegex.matches("1i"))
        Assertions.assertTrue(complexRegex.matches("+1i"))
        Assertions.assertTrue(complexRegex.matches("-1i"))
        Assertions.assertTrue(complexRegex.matches("1.2i"))
        Assertions.assertTrue(complexRegex.matches("+1.2i"))
        Assertions.assertTrue(complexRegex.matches("-1.2i"))
    }

    @Test
    fun testComplexNumberRecognitionMatchRealOnly() {
        Assertions.assertTrue(complexRegex.matches("1+i"))
        Assertions.assertTrue(complexRegex.matches("+1+i"))
        Assertions.assertTrue(complexRegex.matches("1.5+i"))
        Assertions.assertTrue(complexRegex.matches("+1.5+i"))
    }

    @Test
    fun testComplexNumberRecognitionMatchAll() {
        Assertions.assertTrue(complexRegex.matches("1+2i"))
        Assertions.assertTrue(complexRegex.matches("1-2i"))
        Assertions.assertTrue(complexRegex.matches("1+2.3i"))
        Assertions.assertTrue(complexRegex.matches("1-2.3i"))
        Assertions.assertTrue(complexRegex.matches("+1+2i"))
        Assertions.assertTrue(complexRegex.matches("+1-2i"))
        Assertions.assertTrue(complexRegex.matches("+1+2.3i"))
        Assertions.assertTrue(complexRegex.matches("+1-2.3i"))
        Assertions.assertTrue(complexRegex.matches("1.5+2i"))
        Assertions.assertTrue(complexRegex.matches("1.5-2i"))
        Assertions.assertTrue(complexRegex.matches("1.5+2.3i"))
        Assertions.assertTrue(complexRegex.matches("1.5-2.3i"))
        Assertions.assertTrue(complexRegex.matches("+1.5+2i"))
        Assertions.assertTrue(complexRegex.matches("+1.5-2i"))
        Assertions.assertTrue(complexRegex.matches("+1.5+2.3i"))
        Assertions.assertTrue(complexRegex.matches("+1.5-2.3i"))
    }
}
