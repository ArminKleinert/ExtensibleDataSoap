package kleinert.soap.data

/**
 * TODO
 *
 * @property size
 * @property packedSize
 * @property subListSize
 * @property frozen
 *
 * @author Armin Kleinert
 */
class PackedList2D<T> : SimpleList<List<T>> {
    private var packed: MutableList<T>

    override val size: Int

    val packedSize: Int
        get() = packed.size

    val subListSize: Int
        get() = if (size == 0) 0 else packedSize / size

    val frozen: Boolean

    /**
     * TODO
     *
     * @throws IllegalArgumentException if [m] is below 0 or if the dimensions of [packed] are not divisible by [m].
     */
    constructor(m: Int, packed: List<T>, frozen: Boolean = true) {
        if (m < 0) throw IllegalArgumentException("Index $m is negative.")

        if (m == 0 && packed.isNotEmpty())
            throw IllegalArgumentException("With m=$m, the packed List must be empty.")
        else if (m != 0 && packed.size % m != 0)
            throw IllegalArgumentException("Invalid size of packed list. Must be divisble by $m but is ${packed.size}.")

        size = m
        this.packed = packed.toMutableList()
        this.frozen = frozen
    }

    /**
     * TODO
     *
     * @throws IllegalArgumentException if not all sublists have the same size.
     */
    constructor(unpacked: List<List<T>>, frozen: Boolean = true) {
        val packed = mutableListOf<T>()
        var firstSize: Int = -1
        for (l in unpacked) {
            if (firstSize == -1)
                firstSize = l.size
            if (l.size != firstSize)
                throw IllegalArgumentException("All lists must have the same size ($firstSize).")
            packed.addAll(l)
        }

        size = if (packed.isEmpty()) 0 else unpacked.size
        this.packed = packed
        this.frozen = frozen
    }

    /**
     * TODO
     */
    fun unpack(): List<List<T>> =
        (0..<size).map { getUnchecked(it) }

    /**
     * TODO
     */
    override fun getUnchecked(index: Int): List<T> =
        ArrayList(packed).subList(index * subListSize, index * subListSize + subListSize)

    /**
     * TODO
     */
    override fun setUnchecked(index: Int, element: List<T>): List<T> {
        if (frozen)
            throw UnsupportedOperationException()
        if (element.size != subListSize)
            throw IllegalArgumentException()

        val offset = index * subListSize
        val old = get(index)

        for ((i, item) in element.withIndex()) {
            packed[offset + i] = item
        }

        return old
    }

    override fun lastIndexOf(element: List<T>): Int {
        if (element.size != subListSize)
            return -1
        var lastIndex = -1
        for (i in IntProgression.fromClosedRange(0, size, subListSize)) {
            var found = true
            for (j in 0..<subListSize)
                if (element[j] != packed[i + j]) {
                    found = false
                    break
                }
            if (found)
                lastIndex = i
        }
        return lastIndex
    }

    override fun indexOf(element: List<T>): Int {
        if (element.size != subListSize)
            return -1
        for (i in IntProgression.fromClosedRange(0, size, subListSize)) {
            var found = true
            for (j in 0..<subListSize)
                if (element[j] != packed[i + j]) {
                    found = false
                    break
                }
            if (found)
                return i / subListSize
        }
        return -1
    }

    /**
     * TODO
     */
    fun flatten(): List<T> =
        packed.toList()

    /**
     * TODO
     */
    operator fun get(i: Int, j: Int): T {
        checkBounds(i, j)
        return packed[i * subListSize + j]
    }

    /**
     * TODO
     */
    operator fun set(i: Int, j: Int, element: T): T {
        checkBounds(i, j)
        val old = get(i, j)
        packed[i * subListSize + j] = element
        return old
    }

    /**
     * TODO
     */
    private fun checkBounds(index: Int, innerIndex: Int) {
        if (isEmpty())
            throw IndexOutOfBoundsException("Index [$index, $innerIndex] is not in empty list.")
        if (index < 0 || innerIndex < 0 || index >= size || innerIndex >= subListSize)
            throw IndexOutOfBoundsException("Index [$index, $innerIndex] out of bounds [0, 0] to [$size, $subListSize] (both exclusive).")
    }

    override fun toString(): String = joinToString(", ", prefix="[", postfix = "]")

    override fun equals(other: Any?): Boolean = commonEquals(other)

    override fun hashCode(): Int {
        var result = packed.hashCode()
        result = 31 * result + size
        return result
    }
}