package com.example.wordcounter;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Comprehensive unit tests for {@link TextCounter}.
 *
 * Coverage targets
 * ────────────────
 *  • isEmptyOrWhitespace  – null / empty / whitespace-only / real content
 *  • countWords           – basic, contractions, mixed numbers, pure numbers,
 *                           punctuation-only, camel-case, extra whitespace
 *  • countNumbers         – integers, decimals, comma-separated sequences,
 *                           zero, no-number text
 *  • countPunctuation     – ASCII punct, Unicode smart-quotes/dashes,
 *                           repeated punctuation, no-punct text
 *  • countSentences       – single / multi sentence, unpunctuated text,
 *                           decimal guard, repeated terminators, only punct
 *  • private constructor  – reflection smoke-test (utility-class idiom)
 */
public class TextCounterTest {

    // ─────────────────────────────────────────────────────────────────────────
    // Private constructor (utility-class reflection test)
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    public void privateConstructor_reflectionInstantiation_returnsNonNullInstance()
            throws Exception {
        Constructor<TextCounter> ctor = TextCounter.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        TextCounter instance = ctor.newInstance();
        assertNotNull(instance);
    }

    @Test
    public void privateConstructor_isIndeedPrivate() throws NoSuchMethodException {
        Constructor<TextCounter> ctor = TextCounter.class.getDeclaredConstructor();
        assertTrue(java.lang.reflect.Modifier.isPrivate(ctor.getModifiers()));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // isEmptyOrWhitespace
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    public void isEmptyOrWhitespace_nullInput_returnsTrue() {
        assertTrue(TextCounter.isEmptyOrWhitespace(null));
    }

    @Test
    public void isEmptyOrWhitespace_emptyString_returnsTrue() {
        assertTrue(TextCounter.isEmptyOrWhitespace(""));
    }

    @Test
    public void isEmptyOrWhitespace_singleSpace_returnsTrue() {
        assertTrue(TextCounter.isEmptyOrWhitespace(" "));
    }

    @Test
    public void isEmptyOrWhitespace_tabAndNewlineOnly_returnsTrue() {
        assertTrue(TextCounter.isEmptyOrWhitespace("   \t  \n  "));
    }

    @Test
    public void isEmptyOrWhitespace_carriageReturnLineFeed_returnsTrue() {
        assertTrue(TextCounter.isEmptyOrWhitespace("\r\n\t"));
    }

    @Test
    public void isEmptyOrWhitespace_singleLetter_returnsFalse() {
        assertFalse(TextCounter.isEmptyOrWhitespace("a"));
    }

    @Test
    public void isEmptyOrWhitespace_singleDigit_returnsFalse() {
        assertFalse(TextCounter.isEmptyOrWhitespace("1"));
    }

    @Test
    public void isEmptyOrWhitespace_wordWithSurroundingSpaces_returnsFalse() {
        assertFalse(TextCounter.isEmptyOrWhitespace("  hello  "));
    }

    @Test
    public void isEmptyOrWhitespace_normalSentence_returnsFalse() {
        assertFalse(TextCounter.isEmptyOrWhitespace("Hello world!"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // countWords – null / empty / whitespace guard
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    public void countWords_nullInput_returnsZero() {
        assertEquals(0, TextCounter.countWords(null));
    }

    @Test
    public void countWords_emptyString_returnsZero() {
        assertEquals(0, TextCounter.countWords(""));
    }

    @Test
    public void countWords_whitespaceOnly_returnsZero() {
        assertEquals(0, TextCounter.countWords("   \t\n   "));
    }

    // countWords – normal content

    @Test
    public void countWords_twoSimpleWords_returnsTwo() {
        assertEquals(2, TextCounter.countWords("Hello world"));
    }

    @Test
    public void countWords_classicNineWordSentence_returnsNine() {
        assertEquals(9, TextCounter.countWords("The quick brown fox jumps over the lazy dog."));
    }

    @Test
    public void countWords_singleCharacterWords_countedCorrectly() {
        assertEquals(3, TextCounter.countWords("A big dog"));
    }

    @Test
    public void countWords_extraSpacesBetweenWords_ignoredCorrectly() {
        assertEquals(2, TextCounter.countWords("Alpha    Beta"));
    }

    @Test
    public void countWords_mixedCasing_allWordsCounted() {
        assertEquals(3, TextCounter.countWords("UPPER lower camelCase"));
    }

    // countWords – contractions

    @Test
    public void countWords_singleContraction_countedAsOneWord() {
        assertEquals(1, TextCounter.countWords("don't"));
    }

    @Test
    public void countWords_multipleContractions_eachCountedOnce() {
        // don't  say  it's  easy  => 4 words
        assertEquals(4, TextCounter.countWords("Don't say it's easy."));
    }

    @Test
    public void countWords_severalContractions_allCounted() {
        // They'll  know  it's  don't  I'm  => 5
        assertEquals(5, TextCounter.countWords("They'll know it's don't I'm"));
    }

    // countWords – numbers excluded from word count

    @Test
    public void countWords_sentenceWithInlineNumbers_numbersExcluded() {
        // There  are  cats  and  dogs  => 5  (3 and 2 are numbers)
        assertEquals(5, TextCounter.countWords("There are 3 cats and 2 dogs."));
    }

    @Test
    public void countWords_pureIntegerTokens_returnsZero() {
        assertEquals(0, TextCounter.countWords("100 200 300"));
    }

    @Test
    public void countWords_pureDecimalTokens_returnsZero() {
        assertEquals(0, TextCounter.countWords("3.14 2.71 1.41"));
    }

    @Test
    public void countWords_alphanumericTokensEmbeddedDigits_returnsZero() {
        // "Word1" and "Word2" contain digits so they don't match WORD_PATTERN
        assertEquals(0, TextCounter.countWords("Word1    Word2"));
    }

    // countWords – punctuation-only input

    @Test
    public void countWords_punctuationOnly_returnsZero() {
        assertEquals(0, TextCounter.countWords("!@#$%^&*()_+=-`~[]{}|;:,.<>?/"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // countNumbers – null / empty / whitespace guard
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    public void countNumbers_nullInput_returnsZero() {
        assertEquals(0, TextCounter.countNumbers(null));
    }

    @Test
    public void countNumbers_emptyString_returnsZero() {
        assertEquals(0, TextCounter.countNumbers(""));
    }

    @Test
    public void countNumbers_whitespaceOnly_returnsZero() {
        assertEquals(0, TextCounter.countNumbers("   \t\n   "));
    }

    // countNumbers – integers

    @Test
    public void countNumbers_singleZero_returnsOne() {
        assertEquals(1, TextCounter.countNumbers("0"));
    }

    @Test
    public void countNumbers_twoIntegers_returnTwo() {
        assertEquals(2, TextCounter.countNumbers("There are 3 cats and 2 dogs."));
    }

    @Test
    public void countNumbers_fourIntegersCommaSeparated_returnFour() {
        // 10, 20, 30, 40 — commas act as word boundaries
        assertEquals(4, TextCounter.countNumbers("10,20;30-40"));
    }

    // countNumbers – decimals

    @Test
    public void countNumbers_singleDecimal_returnsOne() {
        assertEquals(1, TextCounter.countNumbers("3.14159265"));
    }

    @Test
    public void countNumbers_integerAndDecimalMixed_returnTwo() {
        // Room 101, 3.14
        assertEquals(2, TextCounter.countNumbers("Room 101 has 3.14 liters of water."));
    }

    @Test
    public void countNumbers_multipleDecimalsMixed_returnFour() {
        assertEquals(4, TextCounter.countNumbers("Values: 10, 20.5, 30, and 40.75."));
    }

    @Test
    public void countNumbers_threeSequentialDecimals_returnThree() {
        assertEquals(3, TextCounter.countNumbers("0.1 0.25 100.999"));
    }

    // countNumbers – no numbers

    @Test
    public void countNumbers_purlyAlphabeticText_returnsZero() {
        assertEquals(0, TextCounter.countNumbers("Just regular plain text here!"));
    }

    @Test
    public void countNumbers_alphabeticWithPunctuation_returnsZero() {
        assertEquals(0, TextCounter.countNumbers("Zero numbers here."));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // countPunctuation – null / empty / whitespace guard
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    public void countPunctuation_nullInput_returnsZero() {
        assertEquals(0, TextCounter.countPunctuation(null));
    }

    @Test
    public void countPunctuation_emptyString_returnsZero() {
        assertEquals(0, TextCounter.countPunctuation(""));
    }

    @Test
    public void countPunctuation_whitespaceOnly_returnsZero() {
        assertEquals(0, TextCounter.countPunctuation("   \t\n   "));
    }

    // countPunctuation – ASCII punctuation

    @Test
    public void countPunctuation_commaAndExclamation_returnsTwo() {
        assertEquals(2, TextCounter.countPunctuation("Hello, world!"));
    }

    @Test
    public void countPunctuation_fiveDistinctMarks_returnsFive() {
        // ?, (, :, !, )  => 5
        assertEquals(5, TextCounter.countPunctuation("Question? (Answer: Yes!)"));
    }

    @Test
    public void countPunctuation_noPunctuationPresent_returnsZero() {
        assertEquals(0, TextCounter.countPunctuation("Simple words with zero punctuation"));
    }

    @Test
    public void countPunctuation_repeatedPeriodsAndOtherMarks_returnsFive() {
        // ... ? !  => 3+1+1 = 5
        assertEquals(5, TextCounter.countPunctuation("Wait... What?!"));
    }

    @Test
    public void countPunctuation_mathSymbolsAndSemicolon_returnsFour() {
        // = + * ;
        assertEquals(4, TextCounter.countPunctuation("a = b + c * d;"));
    }

    @Test
    public void countPunctuation_bracketsSlashesAndPath_returnsSeven() {
        // [, ], {, }, (, ), /
        assertEquals(7, TextCounter.countPunctuation("[tag] {id} (val) / path"));
    }

    // countPunctuation – Unicode smart-quotes and dashes

    @Test
    public void countPunctuation_unicodeSmartQuotesAndDashes_returnsSix() {
        // " " — ' ' –  => 6
        assertEquals(6, TextCounter.countPunctuation("\u201cHello\u201d\u2014said \u2018John\u2019\u2013"));
    }

    @Test
    public void countPunctuation_manyRepeatedPeriods_countedIndividually() {
        // 10 period characters
        assertEquals(10, TextCounter.countPunctuation(".........."));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // countSentences – null / empty / whitespace guard
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    public void countSentences_nullInput_returnsZero() {
        assertEquals(0, TextCounter.countSentences(null));
    }

    @Test
    public void countSentences_emptyString_returnsZero() {
        assertEquals(0, TextCounter.countSentences(""));
    }

    @Test
    public void countSentences_whitespaceOnly_returnsZero() {
        assertEquals(0, TextCounter.countSentences("   \t\n   "));
    }

    // countSentences – single sentence

    @Test
    public void countSentences_singleSentenceWithPeriod_returnsOne() {
        assertEquals(1, TextCounter.countSentences("This is a single sentence."));
    }

    @Test
    public void countSentences_singleSentenceWithExclamation_returnsOne() {
        assertEquals(1, TextCounter.countSentences("Watch out!"));
    }

    @Test
    public void countSentences_singleSentenceWithQuestionMark_returnsOne() {
        assertEquals(1, TextCounter.countSentences("Are you there?"));
    }

    @Test
    public void countSentences_unpunctuatedText_countedAsOneSentence() {
        assertEquals(1, TextCounter.countSentences("Sentence without period"));
    }

    // countSentences – multiple sentences

    @Test
    public void countSentences_threeSentencesMixedTerminators_returnsThree() {
        assertEquals(3, TextCounter.countSentences(
                "First sentence. Second sentence! Third sentence?"));
    }

    @Test
    public void countSentences_repeatedTerminatorsTreatedAsSingleBoundary_returnsThree() {
        // "Wait..." is one boundary, "What?!" is one boundary
        assertEquals(3, TextCounter.countSentences("Wait... What?! Let's go!"));
    }

    @Test
    public void countSentences_leadingTerminatorsBeforeWords_returnsTwo() {
        // "..." leads, then "Wait!" and "Is that true?"
        assertEquals(2, TextCounter.countSentences("...Wait! Is that true?"));
    }

    @Test
    public void countSentences_noiseFragmentsBetweenSentences_returnsTwo() {
        // "First sentence." then " --- #$%." (no letters/digits) then "Second sentence."
        assertEquals(2, TextCounter.countSentences(
                "First sentence. --- #$%. Second sentence."));
    }

    // countSentences – decimal number guard

    @Test
    public void countSentences_decimalDoesNotSplitSentence_returnTwo() {
        assertEquals(2, TextCounter.countSentences(
                "The price is 3.14 dollars. That is very cheap."));
    }

    @Test
    public void countSentences_multipleDecimalsAcrossMultipleSentences_returnsThree() {
        assertEquals(3, TextCounter.countSentences(
                "Price is 3.14. Weight is 2.5 kg. Tax is 0.05."));
    }

    // countSentences – only punctuation / no letter-or-digit fragments

    @Test
    public void countSentences_onlyTerminatorsNoWords_returnsZero() {
        assertEquals(0, TextCounter.countSentences("....   ???  !!!"));
    }

    @Test
    public void countSentences_punctuationAndWhitespaceOnly_returnsZero() {
        assertEquals(0, TextCounter.countSentences(" . ! ? ...  --- "));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Comprehensive integration sample
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    public void comprehensiveSample_labTwoSentence_allMetricsCorrect() {
        String sample =
                "Hello world! Mobile Application Development Lab 2 is fun. "
                + "Can you count 3.14, 42, and 100 items?";

        assertEquals(3,  TextCounter.countSentences(sample));
        assertEquals(13, TextCounter.countWords(sample));
        assertEquals(4,  TextCounter.countNumbers(sample));
        assertEquals(6,  TextCounter.countPunctuation(sample));
    }

    @Test
    public void comprehensiveSample_contractionsAndDecimals_allMetricsCorrect() {
        // "I don't know if it's 3.14 or 2.71!"
        // sentences: 1
        // words: "I", "don't", "know", "if", "it's", "or" => 6
        // numbers: 3.14, 2.71 => 2
        String sample = "I don't know if it's 3.14 or 2.71!";
        assertEquals(1, TextCounter.countSentences(sample));
        assertEquals(6, TextCounter.countWords(sample));
        assertEquals(2, TextCounter.countNumbers(sample));
    }
}
