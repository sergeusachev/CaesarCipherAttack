import java.io.BufferedReader
import java.io.File
import java.util.Scanner
import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    val stringBuilder = StringBuilder()
    val scanner = Scanner(System.`in`)
    val bufferedReader: BufferedReader = File("src/cipher.txt").bufferedReader()

    val russianCharFrequencies = createCharFrequencyMap()
    val alphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя"
    val cipherText = bufferedReader.use { it.readText().lowercase() }
    val cipherCharFrequencies = calculateFrequencies(
        text = cipherText,
        alphabet = alphabet
    )
    println(russianCharFrequencies)
    println(cipherCharFrequencies)
    println()

    var isSuccessfullyDecode = false
    var cipherIndex = 0
    var baseIndex = 0

    while (!isSuccessfullyDecode) {
        stringBuilder.clear()
        val shiftTo = cipherCharFrequencies[cipherIndex].first
        val s1 = alphabet.indexOf(char = shiftTo)
        val shiftFrom = russianCharFrequencies[baseIndex].first
        val s0 = alphabet.indexOf(char = shiftFrom)
        val suggestedShift = if (s1 > s0) { s1 - s0 } else { alphabet.length - s0 +s1 }

        cipherText.forEach {
            if (alphabet.contains(char = it)) {
                val decoded = decodeChar(c = it, shift = suggestedShift, alphabet = alphabet)
                stringBuilder.append(decoded)
            } else {
                stringBuilder.append(it)
            }
        }

        println("suggested shift: $suggestedShift")
        println("suggested open text: $stringBuilder")
        println("Is it true?")
        if (scanner.nextInt() > 0) {
            isSuccessfullyDecode = true
        }

        baseIndex += 1
        if (baseIndex > russianCharFrequencies.lastIndex) {
            baseIndex = 0
            cipherIndex += 1
        }
    }
}

fun decodeChar(c: Char, shift: Int, alphabet: String): Char {
    val indexOfChar = alphabet.indexOf(char = c)
    val decodedIndex = (indexOfChar - shift + alphabet.length) % alphabet.length
    return alphabet[decodedIndex]
}

fun calculateFrequencies(text: String, alphabet: String): List<Pair<Char, Int>> {
    val calculatedFrequencies = mutableMapOf<Char, Int>()
    text.forEach {
        if (alphabet.contains(it)) {
            if (calculatedFrequencies.contains(key = it)) {
                calculatedFrequencies[it] = calculatedFrequencies[it]!!.inc()
            } else {
                calculatedFrequencies[it] = 1
            }
        }
    }

    return calculatedFrequencies.toList().sortedByDescending { it.second }
}

fun createCharFrequencyMap(): List<Pair<Char, Float>> {
    val frequencyTable = listOf(
        'а' to 0.062f,
        'б' to 0.014f,
        'в' to 0.038f,
        'г' to 0.013f,
        'д' to 0.025f,
        'е' to 0.072f,
        'ё' to 0.072f,
        'ж' to 0.007f,
        'з' to 0.016f,
        'и' to 0.062f,
        'й' to 0.010f,
        'к' to 0.028f,
        'л' to 0.035f,
        'м' to 0.026f,
        'н' to 0.053f,
        'о' to 0.090f,
        'п' to 0.023f,
        'р' to 0.040f,
        'с' to 0.045f,
        'т' to 0.053f,
        'у' to 0.021f,
        'ф' to 0.002f,
        'х' to 0.009f,
        'ц' to 0.004f,
        'ч' to 0.012f,
        'ш' to 0.006f,
        'щ' to 0.003f,
        'ъ' to 0.014f,
        'ы' to 0.016f,
        'ь' to 0.014f,
        'э' to 0.003f,
        'ю' to 0.006f,
        'я' to 0.018f
    )
    return frequencyTable.sortedByDescending { it.second }
}