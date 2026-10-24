package com.example.wordcounter;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * UI / Integration tests for Word Counter application using AndroidX Test and Espresso.
 *
 * replaceText() is used instead of typeText() throughout to avoid the
 * RootViewWithoutFocusException that occurs when the soft keyboard opens and
 * temporarily steals window focus from the activity.
 */
@RunWith(AndroidJUnit4.class)
public class WordCounterUITest {

    // -------------------------------------------------------------------------
    // Scenario 1: App launch & initial state
    // -------------------------------------------------------------------------
    @Test
    public void testMainScreenInitialState() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            // Input field is present and empty
            onView(withId(R.id.inputText)).check(matches(isDisplayed()));
            onView(withId(R.id.inputText)).check(matches(withText("")));

            // All four metric counters start at 0
            onView(withId(R.id.textSentenceCount)).check(matches(withText("0")));
            onView(withId(R.id.textWordCount)).check(matches(withText("0")));
            onView(withId(R.id.textPunctuationCount)).check(matches(withText("0")));
            onView(withId(R.id.textNumberCount)).check(matches(withText("0")));

            // Empty-history placeholder is visible
            onView(withId(R.id.emptyHistoryView)).check(matches(isDisplayed()));
        }
    }

    // -------------------------------------------------------------------------
    // Scenario 2: Text input & metric calculation
    // "Hello world! Counting 123 numbers."
    //   sentences  = 2  (split on '!' and '.')
    //   words      = 4  (Hello, world, Counting, numbers)
    //   punctuation= 2  ('!' and '.')
    //   numbers    = 1  (123)
    // -------------------------------------------------------------------------
    @Test
    public void testTextInputAndMetricCounting() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.inputText)).perform(replaceText("Hello world! Counting 123 numbers."));
            onView(withId(R.id.btnCount)).perform(click());

            onView(withId(R.id.textSentenceCount)).check(matches(withText("2")));
            onView(withId(R.id.textWordCount)).check(matches(withText("4")));
            onView(withId(R.id.textPunctuationCount)).check(matches(withText("2")));
            onView(withId(R.id.textNumberCount)).check(matches(withText("1")));
        }
    }

    // -------------------------------------------------------------------------
    // Scenario 3: Clear button resets input field and all metric counters
    // "Sample text to clear." -> 1 sentence, 4 words, 1 punctuation, 0 numbers
    // -------------------------------------------------------------------------
    @Test
    public void testClearButtonResetsInputAndMetrics() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            // Populate and count
            onView(withId(R.id.inputText)).perform(replaceText("Sample text to clear."));
            onView(withId(R.id.btnCount)).perform(click());

            // Confirm counting happened (word count changed from 0)
            onView(withId(R.id.textWordCount)).check(matches(withText("4")));

            // Clear
            onView(withId(R.id.btnClear)).perform(click());

            // Input field is empty
            onView(withId(R.id.inputText)).check(matches(withText("")));

            // All counters reset to 0
            onView(withId(R.id.textSentenceCount)).check(matches(withText("0")));
            onView(withId(R.id.textWordCount)).check(matches(withText("0")));
            onView(withId(R.id.textPunctuationCount)).check(matches(withText("0")));
            onView(withId(R.id.textNumberCount)).check(matches(withText("0")));
        }
    }

    // -------------------------------------------------------------------------
    // Scenario 4: Empty input validation
    // -------------------------------------------------------------------------
    @Test
    public void testEmptyInputValidation() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            // Tap Count with nothing entered
            onView(withId(R.id.btnCount)).perform(click());

            // Error text appears on the EditText
            onView(withId(R.id.inputText))
                    .check(matches(hasErrorText("Please enter some text before counting!")));

            // Counters must remain 0
            onView(withId(R.id.textSentenceCount)).check(matches(withText("0")));
            onView(withId(R.id.textWordCount)).check(matches(withText("0")));
            onView(withId(R.id.textPunctuationCount)).check(matches(withText("0")));
            onView(withId(R.id.textNumberCount)).check(matches(withText("0")));
        }
    }

    // -------------------------------------------------------------------------
    // Scenario 5: Sample button populates text and Count produces correct metrics
    // First sample text (SAMPLE_TEXTS[0]):
    // "Hello world! Mobile Application Development Lab 2 is fun. Can you count 3.14, 42, and 100 items?"
    //   sentences   = 3
    //   words       = 13
    //   punctuation = 6
    //   numbers     = 4  (2, 3.14, 42, 100)
    // -------------------------------------------------------------------------
    @Test
    public void testSampleButtonPopulatesAndCounts() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.btnSample)).perform(click());
            onView(withId(R.id.btnCount)).perform(click());

            onView(withId(R.id.textSentenceCount)).check(matches(withText("3")));
            onView(withId(R.id.textWordCount)).check(matches(withText("13")));
            onView(withId(R.id.textPunctuationCount)).check(matches(withText("6")));
            onView(withId(R.id.textNumberCount)).check(matches(withText("4")));
        }
    }
}
