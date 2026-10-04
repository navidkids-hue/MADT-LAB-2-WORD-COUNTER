package com.example.wordcounter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * MainActivity handles UI interactions for the Word Counter app:
 * - Accepts user text input
 * - Validates against empty or whitespace-only input
 * - Delegates metric counting to TextCounter
 * - Displays live results in dedicated cards
 * - Stores and displays calculation history for the current runtime
 * - Provides sample text generation and reset capabilities
 */
public class MainActivity extends AppCompatActivity {

    // UI elements
    private EditText inputText;
    private Button btnCount;
    private Button btnSample;
    private Button btnClear;
    private Button btnClearHistory;

    private TextView textSentenceCount;
    private TextView textWordCount;
    private TextView textPunctuationCount;
    private TextView textNumberCount;

    private RecyclerView historyRecyclerView;
    private TextView emptyHistoryView;

    // Runtime history data
    private final List<CalculationRecord> historyList = new ArrayList<>();
    private HistoryAdapter historyAdapter;

    // Sample sentences to cycle through
    private static final String[] SAMPLE_TEXTS = {
            "Hello world! Mobile Application Development Lab 2 is fun. Can you count 3.14, 42, and 100 items?",
            "The quick brown fox jumps over the lazy dog. It's an extraordinary day, isn't it?!",
            "Dr. Smith bought 5 apples, 10 bananas, and 2.5 kg of oranges. Did he spend $20?"
    };
    private int sampleIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        super.setContentView(R.layout.activity_main);

        // Adjust padding for edge-to-edge system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI view references
        initViews();

        // Setup RecyclerView for runtime history
        setupHistoryRecyclerView();

        // Setup button click listeners
        setupListeners();
    }

    /**
     * Finds and connects view references from the layout.
     */
    private void initViews() {
        inputText = findViewById(R.id.inputText);
        btnCount = findViewById(R.id.btnCount);
        btnSample = findViewById(R.id.btnSample);
        btnClear = findViewById(R.id.btnClear);
        btnClearHistory = findViewById(R.id.btnClearHistory);

        textSentenceCount = findViewById(R.id.textSentenceCount);
        textWordCount = findViewById(R.id.textWordCount);
        textPunctuationCount = findViewById(R.id.textPunctuationCount);
        textNumberCount = findViewById(R.id.textNumberCount);

        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        emptyHistoryView = findViewById(R.id.emptyHistoryView);
    }

    /**
     * Configures the RecyclerView and attaches the adapter.
     */
    private void setupHistoryRecyclerView() {
        historyAdapter = new HistoryAdapter(historyList);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerView.setAdapter(historyAdapter);
        updateHistoryVisibility();
    }

    /**
     * Attaches click listeners to all interactive buttons.
     */
    private void setupListeners() {
        // Count button: validates input, calculates metrics, updates UI and history
        btnCount.setOnClickListener(v -> performCalculation());

        // Sample button: inserts pre-configured sample text
        btnSample.setOnClickListener(v -> generateSampleText());

        // Clear button: resets input field and current metric cards
        btnClear.setOnClickListener(v -> clearInputAndResults());

        // Clear history button: resets runtime calculation log
        btnClearHistory.setOnClickListener(v -> clearHistory());
    }

    /**
     * Validates input, calculates metrics via TextCounter, and updates results and history.
     */
    private void performCalculation() {
        String rawText = inputText.getText().toString();

        // Requirement 4: Validate empty or whitespace-only input and show a warning
        if (TextCounter.isEmptyOrWhitespace(rawText)) {
            inputText.setError(getString(R.string.error_empty_input));
            Toast.makeText(this, R.string.error_empty_input, Toast.LENGTH_SHORT).show();
            return;
        }

        // Clear any previous error notification
        inputText.setError(null);

        // Requirement 1 & 5: Calculate metrics using TextCounter
        int sentences = TextCounter.countSentences(rawText);
        int words = TextCounter.countWords(rawText);
        int punctuation = TextCounter.countPunctuation(rawText);
        int numbers = TextCounter.countNumbers(rawText);

        // Update the metric display cards
        textSentenceCount.setText(String.valueOf(sentences));
        textWordCount.setText(String.valueOf(words));
        textPunctuationCount.setText(String.valueOf(punctuation));
        textNumberCount.setText(String.valueOf(numbers));

        // Requirement 3: Add to calculation history for the current runtime
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timestamp = sdf.format(new Date());

        CalculationRecord record = new CalculationRecord(
                rawText.trim(),
                sentences,
                words,
                punctuation,
                numbers,
                timestamp
        );

        // Add to the top of the history list so newest entry appears first
        historyList.add(0, record);
        historyAdapter.notifyItemInserted(0);
        historyRecyclerView.scrollToPosition(0);
        updateHistoryVisibility();
    }

    /**
     * Requirement 2: Generates sample test sentences into the input field.
     */
    private void generateSampleText() {
        inputText.setError(null);
        String sample = SAMPLE_TEXTS[sampleIndex];
        inputText.setText(sample);
        inputText.setSelection(sample.length()); // move cursor to end

        // Cycle to next sample for subsequent taps
        sampleIndex = (sampleIndex + 1) % SAMPLE_TEXTS.length;

        Toast.makeText(this, R.string.toast_sample_added, Toast.LENGTH_SHORT).show();
    }

    /**
     * Clears user input text and resets metric count displays to zero.
     */
    private void clearInputAndResults() {
        inputText.setText("");
        inputText.setError(null);

        String initialCount = getString(R.string.initial_count);
        textSentenceCount.setText(initialCount);
        textWordCount.setText(initialCount);
        textPunctuationCount.setText(initialCount);
        textNumberCount.setText(initialCount);

        Toast.makeText(this, R.string.toast_cleared, Toast.LENGTH_SHORT).show();
    }

    /**
     * Clears all calculation history records for the current runtime.
     */
    private void clearHistory() {
        if (historyList.isEmpty()) {
            return;
        }

        historyList.clear();
        historyAdapter.notifyItemRangeRemoved(0, historyList.size());
        updateHistoryVisibility();

        Toast.makeText(this, R.string.toast_history_cleared, Toast.LENGTH_SHORT).show();
    }

    /**
     * Toggles between the empty history placeholder and the RecyclerView.
     */
    private void updateHistoryVisibility() {
        if (historyList.isEmpty()) {
            emptyHistoryView.setVisibility(View.VISIBLE);
            historyRecyclerView.setVisibility(View.GONE);
        } else {
            emptyHistoryView.setVisibility(View.GONE);
            historyRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}