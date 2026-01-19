package com.uici.classmanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uici.classmanager.R;
import com.uici.classmanager.models.User;

import java.util.List;

public class EnrollAdapter extends RecyclerView.Adapter<EnrollAdapter.EnrollViewHolder> {

    private List<User> students;
    private OnEnrollClickListener listener;

    public interface OnEnrollClickListener {
        void onEnrollClick(User student);
    }

    public EnrollAdapter(List<User> students, OnEnrollClickListener listener) {
        this.students = students;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EnrollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_enroll_student, parent, false);
        return new EnrollViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EnrollViewHolder holder, int position) {
        User student = students.get(position);
        holder.textViewName.setText(student.getNom() + " " + student.getPrenom());
        holder.textViewEmail.setText(student.getEmail());

        holder.buttonEnroll.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEnrollClick(student);
            }
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    static class EnrollViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewEmail;
        Button buttonEnroll;

        public EnrollViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewStudentName);
            textViewEmail = itemView.findViewById(R.id.textViewStudentEmail);
            buttonEnroll = itemView.findViewById(R.id.buttonEnroll);
        }
    }
}
