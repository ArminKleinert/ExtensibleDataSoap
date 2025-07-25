package kleinert.soap.data

import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import kotlin.math.abs
import kotlin.math.absoluteValue

/**
 * @property num
 * @property den
 *
 * @author Armin Kleinert
 */
class Ratio private constructor(val num: Long, val den: Long) : Number(), Comparable<Number> {
    companion object {
        val ZERO: Ratio = valueOf(0, 1)

        fun valueOf(numeratorInput: Long, denominatorInput: Long = 1L): Ratio {
            var numerator = numeratorInput
            var denominator = denominatorInput

            if (denominator == 0L)
                throw NumberFormatException("kleinert.soap.Ratio with 0 denominator.")

            if (denominator < 0L) {
                // Convert `a/-b` to `-a/b` or `-a/-b` to `a/b`.
                numerator = -numerator
                denominator = -denominator
            }

            // reduce fraction
            if (denominator != 1L) {
                require(denominator > 1)
                val g = gcd(numerator.absoluteValue, denominator)
                numerator /= g
                denominator /= g
            }

            return Ratio(numerator, denominator)
        }

        fun valueOf(numerator: Int, denominator: Int = 1): Ratio =
            valueOf(numerator.toLong(), denominator.toLong())

        fun valueOf(n: BigInteger): Ratio =
            valueOf(n.toLong(), 1)

        fun valueOf(x: Double, epsilon: Double = 1E-10): Ratio =
            estimate(x, epsilon)

        fun valueOfOrNull(s: String): Ratio? {
            val parts = s.split('/')
            if (parts.isEmpty() || parts.size > 2)
                return null

            val num = parts[0].toLongOrNull() ?: return null
            val den = if (parts.size == 2) parts[1].toLongOrNull() ?: return null else 1L
            return valueOf(num, den)
        }

        fun valueOf(s: String): Ratio {
            return valueOfOrNull(s) ?: throw NumberFormatException("Illegal format for rational number $s.")
        }

        /**
         * Approximate rational number for a double.
         *
         * @param x
         * @param epsilon
         */
        fun estimate(x: Double, epsilon: Double = 1E-10): Ratio {
            val sign = if (x < 0.0) -1 else 1
            val xAbs = x.absoluteValue
            var leftNum = 0L
            var leftDen = 1L
            var rightNum = 1L
            var rightDen = 0L
            var bestNum = 0L // = leftNum
            var bestDen = leftDen
            var bestError = abs(xAbs)

            // do Stern-Brocot binary search
            while (bestError > epsilon) {

                // compute next possible rational approximation
                val mediantNum: Long = leftNum + rightNum
                val mediantDen: Long = leftDen + rightDen
                val mediantDouble = mediantNum / mediantDen.toDouble()

                if (xAbs < mediantDouble) { // go left
                    rightNum = mediantNum
                    rightDen = mediantDen
                } else {
                    // go right
                    leftNum = mediantNum
                    leftDen = mediantDen
                }

                // check if better and update champion
                val error: Double = abs(mediantDouble - xAbs)
                if (error < bestError) {
                    bestNum = mediantNum
                    bestDen = mediantDen
                    bestError = error
                    //print("$bestNum/$bestDen ")
                }
            }

            return valueOf(sign * bestNum, bestDen)
        }

        private fun gcd(m: Long, n: Long): Long =
            if (n == 0L) m else gcd(n, m % n)


        private fun lcm(m1: Long, n1: Long): Long {
            val m = if (m1 < 0) -m1 else m1
            val n = if (n1 < 0) -n1 else n1
            return m * (n / gcd(m, n)) // parentheses important to avoid overflow
        }
    }

    init {
        if (den == 0L) throw IllegalArgumentException("Ratio with 0 denominator.")
    }

    /**
     * Returns [num].
     * @return [num].
     */
    operator fun component1() = num

    /**
     * Returns [den].
     * @return [den].
     */
    operator fun component2() = den

    override fun toString(): String = "$num/$den"

    override fun toByte(): Byte = toLong().toByte()
    override fun toShort(): Short = toLong().toShort()
    override fun toInt(): Int = toLong().toInt()
    override fun toLong(): Long = num / den
    override fun toFloat(): Float = toDouble().toFloat()
    override fun toDouble(): Double = num.toDouble() / den.toDouble()

    fun toBigDecimal(mc: MathContext? = MathContext.UNLIMITED): BigDecimal =
        BigDecimal(num, mc).divide(BigDecimal(den, mc))

    fun toBigInteger(): BigInteger = BigInteger.valueOf(num).divide(BigInteger.valueOf(den))

    fun mediant(s: Ratio): Ratio {
        return valueOf(num + s.num, den + s.den)
    }

    // return |a|
    fun abs(): Ratio = if (num >= 0) this else negate()

    // return (b, a)
    fun reciprocal() = valueOf(den, num)

    fun negate(): Ratio = valueOf(-num, den)
    operator fun unaryMinus() = negate()

    // return a + b, staving off overflow
    operator fun plus(b: Ratio): Ratio {
        val a: Ratio = this

        // special cases
        if (a.compareTo(ZERO) == 0) return b
        if (b.compareTo(ZERO) == 0) return a

        // Find gcd of numerators and denominators
        val f = gcd(a.num.absoluteValue, b.num)
        val g = gcd(a.den.absoluteValue, b.den)

        // add cross-product terms for numerator
        val s = valueOf(a.num / f * (b.den / g) + b.num / f * (a.den / g), lcm(a.den, b.den))

        // multiply back in
        return valueOf(s.num * f, s.den)
    }

    operator fun minus(b: Ratio): Ratio =
        this.plus(b.negate())

    operator fun times(b: Ratio): Ratio {
        val c = valueOf(this.num, b.den)
        val d = valueOf(b.num, this.den)
        return valueOf(c.num * d.num, c.den * d.den)
    }

    operator fun div(b: Ratio): Ratio =
        this.times(b.reciprocal())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Number) return false

        return when (other) {
            is Ratio -> num == other.num && den == other.den

            is Byte, is Short, is Int, is Long -> den == 1L && num == other.toLong()
            is BigInteger -> den == 1L && num.toBigInteger() == other
            is Float, is Double -> toDouble() == other.toDouble()
            is BigDecimal -> toBigDecimal() == other
            is Complex -> other.isReal && equals(other.real)

            else -> false
        }
    }

    /**
     * Comparison with various [Number] subtypes. Works for [Byte], [Short], [Int], [Long], [Float], [Double], [BigInteger], [BigDecimal], [Ratio], and [Complex].
     * A comparison with [Complex] numbers may through [IllegalArgumentException] if the number has an imaginary part.
     *
     * @throws IllegalArgumentException if the input is of an unknown type or if it is a [Complex] with an imaginary part.
     */
    override operator fun compareTo(other: Number): Int = when (other) {
        is Byte, is Short, is Int, is Long, is Float, is Double ->
            (num.toDouble() / den.toDouble()).compareTo(other.toDouble())

        is BigInteger -> toBigDecimal().compareTo(BigDecimal(other))
        is BigDecimal -> toBigDecimal().compareTo(other)

        is Ratio -> {
            val a: Ratio = this
            val lhs = a.num * other.den
            val rhs = a.den * other.num

            if (lhs < rhs) -1
            else if (lhs > rhs) +1
            else 0
        }

        is Complex -> {
            if (!other.isReal) throw IllegalArgumentException()
            toDouble().compareTo(other.real)
        }

        else -> throw IllegalArgumentException()
    }

    override fun hashCode(): Int {
        var result = num.hashCode()
        result = 31 * result + den.hashCode()
        return result
    }
}
