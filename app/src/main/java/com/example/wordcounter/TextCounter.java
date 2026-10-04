package com.example.wordcounter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TextCounter is a reusable Java class responsible for calculating text metrics:
 * 1. Sentences
 * 2. Words
 * 3. Punctuation marks
 * 4. Numbers
 *
 * It contains zero Android dependencies, making it pure Java,
 * easy to unit test, and beginner-friendly to explain.
 */
public class TextCounter {

    // Regex to match words (alphabetic sequences, including contractions like "don't")
    private static final Pattern WORD_PATTERN = Pattern.compile("\\b[a-zA-Z]+(?:'[a-zA-Z]+)?\\b");

    // Regex to match integer and decimal numbers (e.g. 42 or 3.14)
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\b\\d+(?:\\.\\d+)?\\b");

    // Regex to match punctuation marks (standard ASCII and common Unicode punctuation)
    private static final Pattern PUNCTUATION_PATTERN = Pattern.compile("[\\p{Punct}“”‘’—–]");

    // Regex to find decimal points between digits so they do not falsely split sentences
    private static final Pattern DECIMAL_PERIOD_PATTERN = Pattern.compile("(?<=\\d)\\.(?=\\d)");

    // Regex to split text by one or more sentence terminators (. ! ?)
    private static final Pattern SENTENCE_SPLIT_PATTERN = Pattern.compile("[.!?]+");

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private TextCounter() {
    }

    /**
     * Checks if the text is null, empty, or consists only of whitespace characters.
     *
     * @param text the input string to validate
     * @return true if empty or whitespace only, false otherwise
     */
    public static boolean isEmptyOrWhitespace(String text) {
        return text == null || text.trim().isEmpty();
    }

    /**
     * Counts the number of words in the given text.
     * Pure numbers (e.g., "100") are excluded from the word count.
     *
     * @param text the input string
     * @return the number of words found
     */
    public static int countWords(String text) {
        if (isEmptyOrWhitespace(text)) {
            return 0;
        }

        Matcher matcher = WORD_PATTERN.matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    /**
     * Counts the number of numbers (integers or decimals) in the given text.
     *
     * @param text the input string
     * @return the number of numeric values found
     */
    public static int countNumbers(String text) {
        if (isEmptyOrWhitespace(text)) {
            return 0;
        }

        Matcher matcher = NUMBER_PATTERN.matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    /**
     * Counts the number of punctuation marks in the given text.
     *
     * @param text the input string
     * @return the number of punctuation characters found
     */
    public static int countPunctuation(String text) {
        if (isEmptyOrWhitespace(text)) {
            return 0;
        }

        Matcher matcher = PUNCTUATION_PATTERN.matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    /**
     * Counts the number of sentences in the given text.
     * Sentences are delimited by '.', '!', or '?'.
     * Decimal periods (such as in "3.14") are preserved and not treated as sentence ends.
     * Consecutive terminators (such as "..." or "!?") are treated as a single boundary.
     * Unpunctuated text with words is counted as 1 sentence.
     *
     * @param text the input string
     * @return the number of sentences found
     */
    public static int countSentences(String text) {
        if (isEmptyOrWhitespace(text)) {
            return 0;
        }

        // Temporarily remove periods inside decimal numbers so "3.14" does not split a sentence
        String normalized = DECIMAL_PERIOD_PATTERN.matcher(text.trim()).replaceAll("");

        // Split by sentence terminators
        String[] parts = SENTENCE_SPLIT_PATTERN.split(normalized);

        int count = 0;
        for (String part : parts) {
            // Only count fragments that contain at least one letter or digit
            if (part.matches(".*[a-zA-Z0-9].*")) {
                count++;
            }
        }

        return count;
    }
}
