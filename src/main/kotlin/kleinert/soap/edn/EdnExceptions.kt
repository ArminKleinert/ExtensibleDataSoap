package kleinert.soap.edn

open class EdnReaderException(
    val lineIndex: Int, val textIndex: Int,
    text: String?, cause: Throwable? = null,
) : Exception(text, cause) {
    class EdnClassConversionError(lineIndex: Int, textIndex: Int, text: String? = null, cause: Throwable? = null) :
        EdnReaderException(lineIndex, textIndex, text, cause)
}

class EdnWriterException(text: String?=null, cause: Throwable? = null,) : Exception(text,cause)
