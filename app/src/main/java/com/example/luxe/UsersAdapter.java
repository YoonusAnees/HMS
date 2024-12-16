package com.example.luxe;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private final Context context;
    private final ArrayList<User> userList;
    private final FirebaseFirestore fStore;

    public UsersAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
        this.fStore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.txtUsername.setText("Username: " + user.getFullName());
        holder.txtPhone.setText("Phone: " + user.getPhone());
        holder.txtEmail.setText("Email: " + user.getEmail());

        holder.btnEdit.setOnClickListener(v -> showEditDialog(user));
        holder.btnDelete.setOnClickListener(v -> deleteUser(user.getUserId(), position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    private void showEditDialog(User user) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.update_user_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(dialogView);
        EditText editName = dialogView.findViewById(R.id.editName);
        EditText editPhone = dialogView.findViewById(R.id.editPhone);
        EditText editEmail = dialogView.findViewById(R.id.editEmail);
        EditText editPassword = dialogView.findViewById(R.id.editPassword);

        editName.setText(user.getFullName());
        editPhone.setText(user.getPhone());
        editEmail.setText(user.getEmail());
        editPassword.setText(user.getPassword());

        builder.setPositiveButton("Update", (dialog, which) -> {
            String updatedName = editName.getText().toString().trim();
            String updatedPhone = editPhone.getText().toString().trim();
            String updatedEmail = editEmail.getText().toString().trim();
            String updatedPassword = editPassword.getText().toString().trim();

            if (!updatedName.isEmpty() && !updatedPhone.isEmpty() && !updatedEmail.isEmpty() && !updatedPassword.isEmpty()) {
                fStore.collection("Users").document(user.getUserId())
                        .update(
                                "Full Name", updatedName,
                                "Phone", updatedPhone,
                                "Email", updatedEmail,
                                "Password", updatedPassword
                        )
                        .addOnSuccessListener(unused -> Toast.makeText(context, "User updated successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(context, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Cancel", null);

        builder.create().show();
    }

    private void deleteUser(String userId, int position) {
        fStore.collection("Users").document(userId)
                .delete()
                .addOnSuccessListener(unused -> {
                    userList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "User deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to delete user: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show());
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txtUsername, txtPhone, txtEmail;
        Button btnEdit, btnDelete;

        public UserViewHolder(View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.TXT_Username);
            txtPhone = itemView.findViewById(R.id.TXT_Phone);
            txtEmail = itemView.findViewById(R.id.TXT_Email);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
