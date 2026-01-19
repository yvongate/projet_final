package com.uici.classmanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uici.classmanager.R;
import com.uici.classmanager.models.Classe;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private List<Classe> classes;
    private OnClassClickListener listener;

    public interface OnClassClickListener {
        void onClassClick(Classe classe);
    }

    public ClassAdapter(List<Classe> classes, OnClassClickListener listener) {
        this.classes = classes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        holder.bind(classes.get(position));
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    class ClassViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNom;
        private TextView textViewAnnee;
        private TextView textViewDescription;

        public ClassViewHolder(View itemView) {
            super(itemView);
            textViewNom = itemView.findViewById(R.id.textViewNom);
            textViewAnnee = itemView.findViewById(R.id.textViewAnnee);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
        }

        public void bind(Classe classe) {
            textViewNom.setText(classe.getNom());
            textViewAnnee.setText(classe.getAnneeAcademique());
            textViewDescription.setText(classe.getDescription());

            itemView.setOnClickListener(v -> listener.onClassClick(classe));
        }
    }
}
