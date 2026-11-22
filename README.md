# Project Testing Summary — MADT Lab 3

## Introduction

This document provides a comprehensive summary of the testing and quality assurance activities conducted for **Mobile Applications Development Techniques (MADT) Lab #3 — Android App Testing**.

The target application for this lab is the **Word Counter** application previously created for Lab #2:
- **Language & Framework:** Java, built using Android SDK with classic XML view layouts (no Kotlin, no Jetpack Compose).
- **Module & Package:** Module `:app`, package `com.example.wordcounter`.
- **Core Logic Classes:**
  - [`TextCounter.java`](file:///f:/Semster%203/Mobile%20Applications%20Development%20Techniques/Lab_2/app/src/main/java/com/example/wordcounter/TextCounter.java): A standalone utility class responsible for calculating text metrics including sentence count, word count, punctuation mark count, and number count.
  - [`CalculationRecord.java`](file:///f:/Semster%203/Mobile%20Applications%20Development%20Techniques/Lab_2/app/src/main/java/com/example/wordcounter/CalculationRecord.java): The data model representing an individual calculation result entry in the session history.
  - [`HistoryAdapter.java`](file:///f:/Semster%203/Mobile%20Applications%20Development%20Techniques/Lab_2/app/src/main/java/com/example/wordcounter/HistoryAdapter.java): RecyclerView adapter rendering the runtime calculation log.
  - [`MainActivity.java`](file:///f:/Semster%203/Mobile%20Applications%20Development%20Techniques/Lab_2/app/src/main/java/com/example/wordcounter/MainActivity.java): The primary activity coordinating user input, button triggers, metric display, and history records.

### Lab Rules and Constraints Followed
1. **No Manual Code Editing:** All code modifications, test suites, and Gradle configuration changes were performed exclusively through an AI coding assistant (Google Antigravity). Android Studio was strictly reserved for running, building, and inspecting execution output.
2. **Strict Session Isolation:** In compliance with the assignment workflow, different phases of the testing lifecycle were performed across separate, focused AI sessions:
   - **Session A:** Unit testing and JaCoCo code coverage in conversation *"Android App JaCoCo Test Coverage"*.
   - **Session B:** Project inspection and Espresso UI testing in conversation *"Android App Testing Project Inspection"*.
   - **Session C:** Version control staging, commit, and push in conversation *"Git Commit And Push Tests"*.
3. **Preservation of Production Code:** Zero changes were made to production source code in `app/src/main`. All testing logic and fixes were maintained solely within `app/src/test` and `app/src/androidTest`.

---

## Session A: "Android App JaCoCo Test Coverage" (Unit Tests)

### My Requests to the AI

> **User Request (Initial Goal):**  
> "This is an Android app written in Java with XML views. Do not change anything in app/src/main. Only add or change files under app/src/test and the Gradle build config.  
>  
> Goal: 100% class and function coverage for TextCounter and CalculationRecord. Exclude MainActivity, HistoryAdapter, R and BuildConfig from the coverage report.  
> 1. Extend the existing TextCounterTest.java to cover every method and branch, including null, empty and whitespace input.  
> 2. Cover the private constructor of TextCounter with a reflection test.  
> 3. Create CalculationRecordTest.java for the constructor and all getters.  
> 4. Configure JaCoCo in app/build.gradle.kts with HTML and XML reports.  
> 5. Run the unit tests and the JaCoCo report yourself, then tell me the pass/fail result and the coverage percentage per class, and the path of the HTML report."

> **User Request (Edge Cases & Clear Test Naming):**  
> "Please rewrite TextCounterTest.java and CalculationRecordTest.java yourself in this session, with clear test names and additional edge cases (null, empty, whitespace, decimals, contractions, repeated punctuation, large values). Do not modify app/src/main. Then re-run testDebugUnitTest and jacocoUnitTestReport and confirm coverage is still 100%."

> **User Request (Continuation & Test Fix):**  
> "The previous attempt was interrupted by a quota error, so some files may be half-finished. Please continue:  
> 1. Read the current TextCounterTest.java and CalculationRecordTest.java in app/src/test. Check what is already there.  
> 2. Make sure BOTH files are complete, compile, and use clear test names. Cover: null, empty, whitespace-only, decimals, contractions, repeated punctuation, large values, and the private constructor of TextCounter (via reflection). Do not modify anything in app/src/main.  
> 3. Run: testDebugUnitTest, then jacocoUnitTestReport (use JAVA_HOME=\"C:\Program Files\Android\Android Studio\jbr\").  
> 4. Report: number of tests, how many passed, and the JaCoCo coverage per class. If anything fails or coverage is below 100%, fix only the test files and run again until all pass at 100%."

> **User Request (Final Assertion Calibration):**  
> "One unit test fails after your rewrite: TextCounterTest.comprehensiveSample_c... (72 tests total, 71 passed).  
> Please fix ONLY the test file app/src/test/.../TextCounterTest.java so the expected values match the real behavior of TextCounter. Do not modify anything in app/src/main. Then re-run testDebugUnitTest and jacocoUnitTestReport and report: total tests, passed, failed, and coverage per class (it must stay at 100%)."

### What the AI Produced

1. **Unit Test Suites:**
   - [`TextCounterTest.java`](file:///f:/Semster%203/Mobile%20Applications%20Development%20Techniques/Lab_2/app/src/test/java/com/example/wordcounter/TextCounterTest.java): Comprehensive suite of 67 test methods validating [`TextCounter`](file:///f:/Semster%203/Mobile%20Applications%20Development%20Techniques/Lab_2/app/src/main/java/com/example/wordcounter/TextCounter.java). Tests verify words, sentences, punctuation marks, numbers, whitespace variations, null handling, empty string handling, tab/newline characters, contractions (e.g., `don't`, `it's`), decimal values, repeated punctuation (`???`, `...`), large strings, and private constructor invocation via Java reflection (`Constructor.setAccessible(true)`).
   - [`CalculationRecordTest.java`](file:///f:/Semster%203/Mobile%20Applications%20Development%20Techniques/Lab_2/app/src/test/java/com/example/wordcounter/CalculationRecordTest.java): 6 unit tests validating the model constructor, null property assignments, formatted preview output, and all getter methods (`getOriginalText`, `getSentenceCount`, `getWordCount`, `getPunctuationCount`, `getNumberCount`, `getTimestamp`).
2. **JaCoCo Configuration:**
   - Modified [`app/build.gradle.kts`](file:///f:/Semster%203/Mobile%20Applications%20Development%20Techniques/Lab_2/app/build.gradle.kts) to apply the `jacoco` Gradle plugin (`0.8.12`).
   - Configured the custom task `jacocoUnitTestReport` depending on `testDebugUnitTest`.
   - Setup XML and HTML reporting targets and configured file exclusions to filter out Android framework components and generated code (`MainActivity*`, `HistoryAdapter*`, `R.class`, `R$*.class`, `BuildConfig.*`).
3. **Tools & Execution Environment:**
   - Gradle wrapper executing `testDebugUnitTest` and `jacocoUnitTestReport`.
   - JUnit 4 testing framework.
   - Pointed `JAVA_HOME` to Android Studio's bundled JBR runtime (`C:\Program Files\Android\Android Studio\jbr`).

### Final Result

- **Unit Tests Execution:** **74 passed, 0 failed, 0 skipped** across all test suites:
  - `TextCounterTest`: 67 passed
  - `CalculationRecordTest`: 6 passed
  - `ExampleUnitTest`: 1 passed
- **JaCoCo Code Coverage:** **100% coverage** achieved for all targeted production classes:
  - [`TextCounter`](file:///f:/Semster%203/Mobile%20Applications%20Development%20Techniques/Lab_2/app/src/main/java/com/example/wordcounter/TextCounter.java): **100% Class** (1/1), **100% Method** (6/6), **100% Line** (36/36), **100% Branch** (22/22), **100% Instruction** (121/121)
  - [`CalculationRecord`](file:///f:/Semster%203/Mobile%20Applications%20Development%20Techniques/Lab_2/app/src/main/java/com/example/wordcounter/CalculationRecord.java): **100% Class** (1/1), **100% Method** (7/7), **100% Line** (14/14), **100% Instruction** (39/39)
- **Report Location:** `app/build/reports/jacoco/jacocoUnitTestReport/html/index.html`

---

## Session B: "Android App Testing Project Inspection" (UI Tests)

### My Requests to the AI

> **User Request (Project Inspection & Planning):**  
> "I am working on MADT Lab #3 — Android App Testing.  
> I want to use my existing Android application from Lab 2, preferably my WordCounter application, for this assignment.  
> First, inspect the existing project and understand its structure. Do not create, edit, delete, or modify any files yet.  
> Please investigate and report the following: Application identification, Application code, Existing tests, Build and execution, Unit-test coverage plan, UI/integration-test plan...  
> Important assignment rules: Unit tests must be generated in a separate AI session. UI/integration tests must be generated in a separate AI session. GitHub commit and push operations must be performed in a separate AI session. Android Studio must only be used for building, running, and reviewing code. Do not manually edit code... Wait for my approval before proceeding."

> **User Request (Approval of Phase 2 UI Testing):**  
> "I approve Phase 2, but proceed carefully.  
> 1. First confirm whether the Pixel_8 or Pixel_4 emulator actually exists and can start.  
> 2. Review app/build.gradle.kts and the existing instrumented test files before making changes.  
> 3. Add androidx.test:rules:1.6.1 only if it is necessary and compatible with the current project dependencies.  
> 4. Create or update UI tests for the existing WordCounter application, testing the main screen, text input, word and character counts, and clear-text functionality where applicable.  
> 5. Do not modify production Java code or change the application's existing behavior.  
> 6. Run connectedAndroidTest only after an emulator is running. If no emulator is available, stop and explain how to configure one.  
> 7. Report the exact test results, including passed, failed, and skipped counts. Do not claim UI tests passed unless they actually ran successfully. Do not proceed to any additional phase without my approval."

> **User Request (Running on Connected Emulator & Diagnosing Issues):**  
> "Run WordCounterUITest on the currently connected emulator once.  
> Rules:  
> - Do NOT modify anything in app/src/main (production code).  
> - Only fix files under app/src/androidTest or Gradle dependencies if needed.  
> Steps:  
> 1. Verify the connected device ID (adb devices) and that the emulator is unlocked.  
> 2. Run: ./gradlew connectedAndroidTest  
> 3. Report the actual pass/fail result of each test and the full error message for any failure.  
> 4. If a test fails, explain the cause, fix only the test file, and run it again.  
> 5. Repeat until all tests pass, then give me the final summary: number of tests, number passed, and the path of the HTML report (app/build/reports/androidTests/connected/index.html)."

### What the AI Produced

1. **Instrumentation & Espresso UI Test Suite:**
   - [`WordCounterUITest.java`](file:///f:/Semster%203/Mobile%20Applications%20Development%20Techniques/Lab_2/app/src/androidTest/java/com/example/wordcounter/WordCounterUITest.java): UI test class using AndroidX Test and Espresso to test [`MainActivity`](file:///f:/Semster%203/Mobile%20Applications%20Development%20Techniques/Lab_2/app/src/main/java/com/example/wordcounter/MainActivity.java). It implements 5 scenarios:
     - `testMainScreenInitialState`: Verifies default UI component visibility, empty inputs, and metric card defaults (0 values).
     - `testSampleButtonPopulatesAndCounts`: Validates clicking the "Generate Sample" button populates text and immediately calculates metrics.
     - `testClearButtonResetsInputAndMetrics`: Tests typing text and pressing "Clear" properly clears input and resets count indicators.
     - `testEmptyInputValidation`: Tests attempting to count with empty input triggers the warning message without adding an invalid history item.
     - `testTextInputAndMetricCounting`: Validates entering multi-sentence custom text and asserting exact computed values.
2. **Defect Diagnosis & Reliability Fix:**
   - **Issue:** An initial run suffered from `RootViewWithoutFocusException` during `typeText()` on the emulator because the Android virtual soft keyboard briefly stole window focus from the activity root view.
   - **Resolution:** Replaced `typeText()` with `replaceText()` and removed `closeSoftKeyboard()` calls in [`WordCounterUITest.java`](file:///f:/Semster%203/Mobile%20Applications%20Development%20Techniques/Lab_2/app/src/androidTest/java/com/example/wordcounter/WordCounterUITest.java). This directly set the input buffer without opening the IME, eliminating the focus-loss race condition without altering any production code.
3. **Tools & Execution Environment:**
   - Espresso Core (`androidx.test.espresso:espresso-core:3.6.1`), AndroidX JUnit Test Runner (`androidx.test.ext:junit:1.2.1`), and AndroidX Test Rules (`androidx.test:rules:1.6.1`).
   - Android Emulator: `Pixel_4` (`emulator-5554`), Android API Level 37.
   - Executed via `./gradlew connectedAndroidTest`.

### Final Result

- **UI Tests Execution:** **6 tests passed on a Pixel 4 emulator**, 0 failed, 0 skipped:
  1. `WordCounterUITest.testMainScreenInitialState` — **PASSED**
  2. `WordCounterUITest.testSampleButtonPopulatesAndCounts` — **PASSED**
  3. `WordCounterUITest.testClearButtonResetsInputAndMetrics` — **PASSED**
  4. `WordCounterUITest.testEmptyInputValidation` — **PASSED**
  5. `WordCounterUITest.testTextInputAndMetricCounting` — **PASSED**
  6. `ExampleInstrumentedTest.useAppContext` — **PASSED**
- **Report Location:** `app/build/reports/androidTests/connected/debug/index.html`

---

## Session C: "Git Commit And Push Tests" (Git Commit and Push)

### My Requests to the AI

> **User Request (Selective Staging, Commit & Push):**  
> "This is a Git repository for an Android app (Word Counter). Do not modify any source or test code.  
>  
> 1. Run `git status` and show me what changed.  
> 2. Stage only the relevant files: the test files under app/src/test and app/src/androidTest, and app/build.gradle.kts. Do NOT commit .idea files, build/ output folders, or local.properties.  
> 3. Write a clear commit message that mentions the unit tests, the Espresso UI tests and the JaCoCo coverage setup.  
> 4. Commit, then push to origin on the current branch (master).  
> 5. Run `git log -1` and `git status` and report the result."

### What the AI Produced

1. **Repository Inspection:**
   - Executed `git status` to identify modified Gradle configurations and untracked test source files while filtering out `.idea` IDE settings and build output folders.
2. **Selective Staging:**
   - Staged exclusively the relevant testing files:
     - [`app/build.gradle.kts`](file:///f:/Semster%203/Mobile%20Applications%20Development%20Techniques/Lab_2/app/build.gradle.kts)
     - [`app/src/androidTest/java/com/example/wordcounter/WordCounterUITest.java`](file:///f:/Semster%203/Mobile%20Applications%20Development%20Techniques/Lab_2/app/src/androidTest/java/com/example/wordcounter/WordCounterUITest.java)
     - [`app/src/test/java/com/example/wordcounter/CalculationRecordTest.java`](file:///f:/Semster%203/Mobile%20Applications%20Development%20Techniques/Lab_2/app/src/test/java/com/example/wordcounter/CalculationRecordTest.java)
     - [`app/src/test/java/com/example/wordcounter/TextCounterTest.java`](file:///f:/Semster%203/Mobile%20Applications%20Development%20Techniques/Lab_2/app/src/test/java/com/example/wordcounter/TextCounterTest.java)
3. **Commit & Push Operations:**
   - Executed `git commit` with descriptive commit title and bulleted summaries.
   - Pushed commit to remote repository `https://github.com/navidkids-hue/MADT-LAB-2-WORD-COUNTER.git` on branch `master`.
   - Verified log and status via `git log -1` and `git status`.

### Final Result

- **Git Commit:** Commit `86f7f68045cf310d3d13db0762655528681b8756`:
  ```text
  Add unit tests, Espresso UI tests, and JaCoCo coverage setup
  
  - Add CalculationRecordTest and update TextCounterTest unit tests
  - Add WordCounterUITest Espresso UI tests
  - Configure JaCoCo code coverage plugin and tasks in app/build.gradle.kts
  ```
- **Push Destination:** Successfully pushed to `origin/master`.
- **Working Tree:** All 4 test and build configuration files cleanly tracked on `origin/master`, with zero modification to production application code.
