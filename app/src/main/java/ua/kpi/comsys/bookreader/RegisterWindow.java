package ua.kpi.comsys.bookreader;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

import ua.kpi.comsys.bookreader.models.User;

public class RegisterWindow extends AppCompatActivity {
    Button btnRegister, btnCancel;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_window);

        btnRegister = findViewById(R.id.btnRegister);
        btnCancel = findViewById(R.id.btnCancel);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        MaterialEditText email = findViewById(R.id.emailField);
        MaterialEditText password = findViewById(R.id.passField);
        MaterialEditText name = findViewById(R.id.nameField);
        MaterialEditText phone = findViewById(R.id.phoneField);

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String phonePattern = "^[+]?[0-9]{10,13}$";

        btnCancel.setOnClickListener(v -> finish());

        btnRegister.setOnClickListener(v -> {
            if (TextUtils.isEmpty(Objects.requireNonNull(email.getText()).toString())) {
                email.setError("Введіть пошту");
                return;
            }

            if (!email.getText().toString().trim().matches(emailPattern)) {
                email.setError("Не вірна поштова адреса");
                return;
            }

            if (TextUtils.isEmpty(Objects.requireNonNull(password.getText()).toString())) {
                password.setError("Введіть пароль");
                return;
            }

            if (password.getText().toString().length() < 6 ||
                    password.getText().toString().length() > 20) {
                password.setError("Пароль має містити від 6 до 20 символів");
                return;
            }

            if (TextUtils.isEmpty(Objects.requireNonNull(name.getText()).toString())) {
                name.setError("Введіть ім'я користувача");
                return;
            }

            if (name.getText().toString().length() < 3 ||
                    name.getText().toString().length() > 30) {
                name.setError("Ім'я користувача має містити від 3 до 30 символів");
                return;
            }

            if (TextUtils.isEmpty(Objects.requireNonNull(phone.getText()).toString())) {
                //Snackbar.make(root, "Введіть номер телефону", Snackbar.LENGTH_SHORT).show();
                phone.setError("Введіть номер телефону");
                return;
            }

            if (!phone.getText().toString().trim().matches(phonePattern)) {
                phone.setError("Введіть номер телефону у форматі +38XXXXXXXXXX");
                return;
            }

            //user register
            auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        User user = new User();
                        user.setEmail(email.getText().toString());
                        user.setName(name.getText().toString());
                        user.setPassword(password.getText().toString());
                        user.setPhone(phone.getText().toString());

                        users.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                .setValue(user)
                                .addOnSuccessListener(unused -> {
                                    finish();
                                    //Snackbar.make(root, "Користувач був успішно доданий", Snackbar.LENGTH_SHORT).show();
                                    Log.i("Add ", "user");
                                });
                    }).addOnFailureListener(e -> {
                        finish();
                        //Snackbar.make(root, "Помилка реєстрації. Вже існує користувач з заданою електронною поштою", Snackbar.LENGTH_SHORT).show();
                        Log.i("Error", e.getMessage());
                    });
        });
    }
}