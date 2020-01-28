package com.example.chatterbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "xzy";
    private TextView tvLogo;
    private EditText etUsername, etPassword;
    private Button btLogin, btRegister;
    private CheckBox cbRecordar;
    private Context context;
    private ProgressBar pbLoading;
    private ConstraintLayout clActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);

        init();
    }

    private void init() {
        context = this;
        tvLogo = findViewById(R.id.tvLogo);
        cbRecordar = findViewById(R.id.cbRecordar);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btLogin = findViewById(R.id.btLogin);
        btRegister = findViewById(R.id.btRegistrarse);

        pbLoading = findViewById(R.id.pbLoading);
        clActivity = findViewById(R.id.clActivity);
        setActivityLoading(false);
        checkRemember();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                //startActivity(intent);
                checkLogin();
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                final Boolean remember = cbRecordar.isChecked();
                if(password.length()<=30) {
                    if (!username.equalsIgnoreCase("") && !password.equalsIgnoreCase("")) {
                        setActivityLoading(true);

                        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(LoginActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();

                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if(remember)
                                    {
                                        SharedPreferences sharedPreferences = getSharedPreferences("recordar", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean("recordar", remember);
                                        editor.putString("email", username);
                                        editor.putString("password", password);
                                        editor.apply();
                                    }
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("user", user);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    setActivityLoading(false);
                                    Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(LoginActivity.this, R.string.vacio, Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(LoginActivity.this, R.string.passwordLength, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void checkLogin() {

        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();
        final Boolean remember = cbRecordar.isChecked();
        if(password.length()<=30) {
            if (!username.equalsIgnoreCase("") && !password.equalsIgnoreCase("")) {
                setActivityLoading(true);

                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signInWithEmailAndPassword(username, password).
                        addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {



                                if (task.isSuccessful()) {

                                    if (remember){
                                        SharedPreferences sharedPreferences = getSharedPreferences("recordar", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putInt("recordar", 1);
                                        editor.putString("email", username);
                                        editor.putString("password", password);
                                        editor.apply();
                                    }

                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    setActivityLoading(false);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("user", user);
                                    startActivity(intent);
                                    finish();
                                    //initComponents();
                                    //initUid();
                                } else {
                                    Log.v(TAG, task.getException().toString());
                                    setActivityLoading(false);
                                    Toast.makeText(LoginActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            } else {
                Toast.makeText(LoginActivity.this, R.string.vacio, Toast.LENGTH_SHORT).show();
            }
        } else{
            Toast.makeText(LoginActivity.this, R.string.passwordLength, Toast.LENGTH_SHORT).show();
        }
    }

    private void tryLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("recordar", Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString("email", "");
        final String password = sharedPreferences.getString("password", "");
        final Boolean remember = cbRecordar.isChecked();
        if(password.length()<=30) {
            if (!username.equalsIgnoreCase("") && !password.equalsIgnoreCase("")) {
                setActivityLoading(true);

                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signInWithEmailAndPassword(username, password).
                        addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    if (remember){
                                        SharedPreferences sharedPreferences = getSharedPreferences("recordar", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putInt("recordar", 1);
                                        editor.putString("email", username);
                                        editor.putString("password", password);
                                        editor.apply();
                                    }

                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    setActivityLoading(false);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("user", user);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Log.v(TAG, task.getException().toString());
                                    //setActivityLoading(false);
                                    Toast.makeText(LoginActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        }
    }

    public void checkRemember(){
        SharedPreferences sharedPreferences = getSharedPreferences("recordar", Context.MODE_PRIVATE);
        long id = sharedPreferences.getInt("recordar", 0);
        if(id != 0){
            tryLogin();
        }
    }

    private void setActivityLoading(boolean toggle)
    {
        if(toggle)
        {
            pbLoading.setVisibility(View.VISIBLE);
            clActivity.setVisibility(View.GONE);
        }
        else
        {
            pbLoading.setVisibility(View.GONE);
            clActivity.setVisibility(View.VISIBLE);
        }
    }

}
