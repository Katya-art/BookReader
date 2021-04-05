package ua.kpi.comsys.bookreader;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

public class SignInWindow extends AppCompatActivity {
    Button btnSignIn, btnCancel;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_window);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnCancel = findViewById(R.id.btnCancel1);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        MaterialEditText email = findViewById(R.id.emailField);
        MaterialEditText password = findViewById(R.id.passField);

        btnCancel.setOnClickListener(v -> finish());

        btnSignIn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(Objects.requireNonNull(email.getText()).toString())) {
                email.setError("Введіть пошту");
                return;
            }

            if (TextUtils.isEmpty(Objects.requireNonNull(password.getText()).toString())) {
                password.setError("Введіть пароль");
                return;
            }

            auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        startActivity(new Intent(SignInWindow.this, MainWindow.class));
                        finish();
                    }).addOnFailureListener(e -> email.setError("Помилка авторизації. Не вірно задана пошта чи пароль"));
        });
    }
}
