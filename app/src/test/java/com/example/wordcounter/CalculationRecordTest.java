package com.example.wordcounter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link CalculationRecord} data model class.
 *
 * Coverage targets:
 *  • Constructor initialization and mapping to all 6 getters:
 *      - getOriginalText()
 *      - getSentences()
 *      - getWords()
 *      - getPunctuation()
 *      - getNumbers()
 *      - getTimestamp()
 *  • Edge cases:
 *      - Standard typical values
 *      - Zero and empty string values
 *      - Whitespace-only values
 *      - Null string values
 *      - Large values (large integer metrics, Integer.MAX_VALUE, long text strings)
 */
public class CalculationRecordTest {

    @Test
    public void constructorAndGetters_standardValues_allFieldsMatch() {
        CalculationRecord record = new CalculationRecord("Hello world", 1, 2, 0, 0, "10:30:00");

        assertEquals("Hello world", record.getOriginalText());
        assertEquals(1, record.getSentences());
        assertEquals(2, record.getWords());
        assertEquals(0, record.getPunctuation());
        assertEquals(0, record.getNumbers());
        assertEquals("10:30:00", record.getTimestamp());
    }

    @Test
    public void constructorAndGetters_zeroAndEmptyValues_allFieldsMatch() {
        CalculationRecord record = new CalculationRecord("", 0, 0, 0, 0, "");

        assertEquals("", record.getOriginalText());
        assertEquals(0, record.getSentences());
        assertEquals(0, record.getWords());
        assertEquals(0, record.getPunctuation());
        assertEquals(0, record.getNumbers());
        assertEquals("", record.getTimestamp());
    }

    @Test
    public void constructorAndGetters_whitespaceOnlyValues_allFieldsMatch() {
        CalculationRecord record = new CalculationRecord("   \t\n   ", 0, 0, 0, 0, "   ");

        assertEquals("   \t\n   ", record.getOriginalText());
        assertEquals(0, record.getSentences());
        assertEquals(0, record.getWords());
        assertEquals(0, record.getPunctuation());
        assertEquals(0, record.getNumbers());
        assertEquals("   ", record.getTimestamp());
    }

    @Test
    public void constructorAndGetters_nullValues_preservesNullsAndZeroes() {
        CalculationRecord record = new CalculationRecord(null, 0, 0, 0, 0, null);

        assertNull(record.getOriginalText());
        assertNull(record.getTimestamp());
        assertEquals(0, record.getSentences());
        assertEquals(0, record.getWords());
        assertEquals(0, record.getPunctuation());
        assertEquals(0, record.getNumbers());
    }

    @Test
    public void constructorAndGetters_highMetricsAndLongText_allFieldsMatch() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            sb.append("Sentence number ").append(i).append(". ");
        }
        String largeText = sb.toString();

        CalculationRecord record = new CalculationRecord(largeText, 500, 1500, 500, 500, "2026-10-24 21:30:00");

        assertEquals(largeText, record.getOriginalText());
        assertEquals(500, record.getSentences());
        assertEquals(1500, record.getWords());
        assertEquals(500, record.getPunctuation());
        assertEquals(500, record.getNumbers());
        assertEquals("2026-10-24 21:30:00", record.getTimestamp());
    }

    @Test
    public void constructorAndGetters_maxIntegerMetrics_allFieldsMatch() {
        CalculationRecord record = new CalculationRecord(
                "Max values test",
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                "23:59:59"
        );

        assertEquals("Max values test", record.getOriginalText());
        assertEquals(Integer.MAX_VALUE, record.getSentences());
        assertEquals(Integer.MAX_VALUE, record.getWords());
        assertEquals(Integer.MAX_VALUE, record.getPunctuation());
        assertEquals(Integer.MAX_VALUE, record.getNumbers());
        assertEquals("23:59:59", record.getTimestamp());
    }
}
