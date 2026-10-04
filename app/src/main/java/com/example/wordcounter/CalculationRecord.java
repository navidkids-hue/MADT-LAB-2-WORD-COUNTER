package com.example.wordcounter;

/**
 * Model class representing a single calculation record
 * stored in memory during the app's current runtime session.
 */
public class CalculationRecord {
    private final String originalText;
    private final int sentences;
    private final int words;
    private final int punctuation;
    private final int numbers;
    private final String timestamp;

    public CalculationRecord(String originalText, int sentences, int words, int punctuation, int numbers, String timestamp) {
        this.originalText = originalText;
        this.sentences = sentences;
        this.words = words;
        this.punctuation = punctuation;
        this.numbers = numbers;
        this.timestamp = timestamp;
    }

    public String getOriginalText() {
        return originalText;
    }

    public int getSentences() {
        return sentences;
    }

    public int getWords() {
        return words;
    }

    public int getPunctuation() {
        return punctuation;
    }

    public int getNumbers() {
        return numbers;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
