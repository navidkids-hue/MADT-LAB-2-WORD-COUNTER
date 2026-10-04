package com.example.wordcounter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * RecyclerView Adapter to display calculation history items in a clean card list.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private final List<CalculationRecord> historyList;

    public HistoryAdapter(List<CalculationRecord> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        CalculationRecord record = historyList.get(position);

        holder.textTimestamp.setText(record.getTimestamp());
        holder.textOriginal.setText(record.getOriginalText());

        String formattedMetrics = holder.itemView.getContext().getString(
                R.string.history_item_metrics,
                record.getSentences(),
                record.getWords(),
                record.getPunctuation(),
                record.getNumbers()
        );
        holder.textMetrics.setText(formattedMetrics);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        final TextView textTimestamp;
        final TextView textOriginal;
        final TextView textMetrics;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textTimestamp = itemView.findViewById(R.id.historyTimestamp);
            textOriginal = itemView.findViewById(R.id.historyOriginalText);
            textMetrics = itemView.findViewById(R.id.historyMetrics);
        }
    }
}
