package ua.kpi.comsys.bookreader;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

import ua.kpi.comsys.bookreader.models.User;

public class RegisterActivity extends AppCompatActivity {
    Button btnRegister, btnCancel;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
            boolean isCorrect = true;
            if (TextUtils.isEmpty(Objects.requireNonNull(email.getText()).toString())) {
                email.setError("Введіть пошту");
                isCorrect = false;
            } else {
                if (!email.getText().toString().trim().matches(emailPattern)) {
                    email.setError("Не вірна поштова адреса");
                    isCorrect = false;
                }
            }

            if (TextUtils.isEmpty(Objects.requireNonNull(password.getText()).toString())) {
                password.setError("Введіть пароль");
                isCorrect = false;
            } else {
                if (password.getText().toString().length() < 6 ||
                        password.getText().toString().length() > 20) {
                    password.setError("Пароль має містити від 6 до 20 символів");
                    isCorrect = false;
                }
            }

            if (TextUtils.isEmpty(Objects.requireNonNull(name.getText()).toString())) {
                name.setError("Введіть ім'я користувача");
                isCorrect = false;
            } else {
                if (name.getText().toString().length() < 3 ||
                        name.getText().toString().length() > 30) {
                    name.setError("Ім'я користувача має містити від 3 до 30 символів");
                    isCorrect = false;
                }
            }

            if (TextUtils.isEmpty(Objects.requireNonNull(phone.getText()).toString())) {
                phone.setError("Введіть номер телефону");
                isCorrect = false;
            } else {
                if (!phone.getText().toString().trim().matches(phonePattern)) {
                    phone.setError("Введіть номер телефону у форматі +38XXXXXXXXXX");
                    isCorrect = false;
                }
            }

            if (!isCorrect) {
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
                                    auth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                                            .addOnSuccessListener(authResult1 -> {
                                                startActivity(new Intent(RegisterActivity.this, ListOfBooks.class));
                                                finish();
                                            });
                                    //finish();
                                    //Snackbar.make(root, "Користувач був успішно доданий", Snackbar.LENGTH_SHORT).show();
                                    //Log.i("Add ", "user");
                                });
                    }).addOnFailureListener(e -> {
                        email.setError("Помилка реєстрації. Вже існує користувач з заданою поштовою адресою");
                        //finish();
                        //Snackbar.make(root, "Помилка реєстрації. Вже існує користувач з заданою електронною поштою", Snackbar.LENGTH_SHORT).show();
                        //Log.i("Error", e.getMessage());
                    });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Initialize menu inflater
        MenuInflater menuInflater = getMenuInflater();
        //Inflate menu
        menuInflater.inflate(R.menu.night_mode_menu, menu);
        MenuItem changeModeItem = menu.findItem(R.id.night_mode);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            changeModeItem.setIcon(R.drawable.light_mode_white_24dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.night_mode) {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                return true;
            }
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
