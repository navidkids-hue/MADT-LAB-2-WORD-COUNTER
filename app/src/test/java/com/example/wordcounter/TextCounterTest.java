package com.example.wordcounter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for TextCounter class covering all four metrics and edge cases:
 * - Empty, null, and whitespace-only text
 * - Sentence counting (with terminators, without terminators, and with decimal numbers)
 * - Word counting (ignoring numeric tokens, supporting contractions)
 * - Number counting (integers and decimals)
 * - Punctuation mark counting
 */
public class TextCounterTest {

    @Test
    public void testEmptyAndWhitespaceInput() {
        // Null string
        assertTrue(TextCounter.isEmptyOrWhitespace(null));
        assertEquals(0, TextCounter.countWords(null));
        assertEquals(0, TextCounter.countNumbers(null));
        assertEquals(0, TextCounter.countPunctuation(null));
        assertEquals(0, TextCounter.countSentences(null));

        // Empty string
        assertTrue(TextCounter.isEmptyOrWhitespace(""));
        assertEquals(0, TextCounter.countWords(""));
        assertEquals(0, TextCounter.countNumbers(""));
        assertEquals(0, TextCounter.countPunctuation(""));
        assertEquals(0, TextCounter.countSentences(""));

        // Whitespace only
        assertTrue(TextCounter.isEmptyOrWhitespace("   \t  \n  "));
        assertEquals(0, TextCounter.countWords("   \t  \n  "));
        assertEquals(0, TextCounter.countNumbers("   \t  \n  "));
        assertEquals(0, TextCounter.countPunctuation("   \t  \n  "));
        assertEquals(0, TextCounter.countSentences("   \t  \n  "));
    }

    @Test
    public void testWordCount() {
        assertFalse(TextCounter.isEmptyOrWhitespace("Hello world"));

        // Basic sentence
        assertEquals(2, TextCounter.countWords("Hello world"));
        assertEquals(9, TextCounter.countWords("The quick brown fox jumps over the lazy dog."));

        // Contractions
        assertEquals(4, TextCounter.countWords("Don't say it's easy."));

        // Words mixed with numbers: numbers should not be counted as words
        assertEquals(5, TextCounter.countWords("There are 3 cats and 2 dogs."));

        // Pure numbers
        assertEquals(0, TextCounter.countWords("100 200 300"));
    }

    @Test
    public void testNumberCount() {
        // Integers
        assertEquals(2, TextCounter.countNumbers("There are 3 cats and 2 dogs."));

        // Decimals and integers
        assertEquals(2, TextCounter.countNumbers("Room 101 has 3.14 liters of water."));

        // Multiple numbers
        assertEquals(4, TextCounter.countNumbers("Values: 10, 20.5, 30, and 40.75."));

        // No numbers
        assertEquals(0, TextCounter.countNumbers("Just regular plain text here!"));
    }

    @Test
    public void testPunctuationCount() {
        // Standard punctuation
        assertEquals(2, TextCounter.countPunctuation("Hello, world!"));

        // Varied punctuation: ?, (, :, !, )
        assertEquals(5, TextCounter.countPunctuation("Question? (Answer: Yes!)"));

        // Text without punctuation
        assertEquals(0, TextCounter.countPunctuation("Simple words with zero punctuation"));

        // Repeated punctuation marks: 3 periods + 1 question mark + 1 exclamation mark = 5
        assertEquals(5, TextCounter.countPunctuation("Wait... What?!"));
    }

    @Test
    public void testSentenceCount() {
        // Single sentence with period
        assertEquals(1, TextCounter.countSentences("This is a single sentence."));

        // Sentence without trailing punctuation
        assertEquals(1, TextCounter.countSentences("Sentence without period"));

        // Multiple sentences separated by different terminators
        assertEquals(3, TextCounter.countSentences("First sentence. Second sentence! Third sentence?"));

        // Decimal numbers should not falsely split sentences
        assertEquals(2, TextCounter.countSentences("The price is 3.14 dollars. That is very cheap."));

        // Repeated punctuation should not create phantom sentences
        assertEquals(3, TextCounter.countSentences("Wait... What?! Let's go!"));

        // Only punctuation marks with no words or digits
        assertEquals(0, TextCounter.countSentences("....   ???  !!!"));
    }

    @Test
    public void testComprehensiveSample() {
        String sample = "Hello world! Mobile Application Development Lab 2 is fun. Can you count 3.14, 42, and 100 items?";

        assertEquals(3, TextCounter.countSentences(sample));
        assertEquals(13, TextCounter.countWords(sample));
        assertEquals(4, TextCounter.countNumbers(sample));
        assertEquals(6, TextCounter.countPunctuation(sample));
    }
}
