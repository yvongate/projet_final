package com.uici.classmanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uici.classmanager.R;
import com.uici.classmanager.models.Annonce;

import java.util.List;

public class AnnonceAdapter extends RecyclerView.Adapter<AnnonceAdapter.AnnonceViewHolder> {

    private List<Annonce> annonces;
    private OnAnnonceClickListener listener;

    public interface OnAnnonceClickListener {
        void onAnnonceClick(Annonce annonce);
    }

    public AnnonceAdapter(List<Annonce> annonces, OnAnnonceClickListener listener) {
        this.annonces = annonces;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AnnonceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_annonce, parent, false);
        return new AnnonceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnonceViewHolder holder, int position) {
        Annonce annonce = annonces.get(position);
        holder.textViewTitre.setText(annonce.getTitre());
        holder.textViewDate.setText(annonce.getDatePublication());

        String preview = annonce.getContenu();
        if (preview.length() > 100) {
            preview = preview.substring(0, 100) + "...";
        }
        holder.textViewPreview.setText(preview);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAnnonceClick(annonce);
            }
        });
    }

    @Override
    public int getItemCount() {
        return annonces.size();
    }

    static class AnnonceViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitre;
        TextView textViewDate;
        TextView textViewPreview;

        public AnnonceViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitre = itemView.findViewById(R.id.textViewAnnonceTitre);
            textViewDate = itemView.findViewById(R.id.textViewAnnonceDate);
            textViewPreview = itemView.findViewById(R.id.textViewAnnoncePreview);
        }
    }
}
