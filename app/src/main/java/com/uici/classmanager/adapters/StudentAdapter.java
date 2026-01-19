package com.uici.classmanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uici.classmanager.R;
import com.uici.classmanager.models.User;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<User> students;
    private OnStudentActionListener listener;
    private boolean showDeleteButton;

    public interface OnStudentActionListener {
        void onDeleteStudent(User student);
    }

    public StudentAdapter(List<User> students, OnStudentActionListener listener, boolean showDeleteButton) {
        this.students = students;
        this.listener = listener;
        this.showDeleteButton = showDeleteButton;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        User student = students.get(position);
        holder.textViewName.setText(student.getNom() + " " + student.getPrenom());
        holder.textViewEmail.setText(student.getEmail());

        if (showDeleteButton) {
            holder.buttonDelete.setVisibility(View.VISIBLE);
            holder.buttonDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteStudent(student);
                }
            });
        } else {
            holder.buttonDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewEmail;
        ImageButton buttonDelete;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewStudentName);
            textViewEmail = itemView.findViewById(R.id.textViewStudentEmail);
            buttonDelete = itemView.findViewById(R.id.buttonDeleteStudent);
        }
    }
}
