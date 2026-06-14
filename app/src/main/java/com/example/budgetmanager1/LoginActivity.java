package com.example.budgetmanager1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etContrasena;
    private TextView tvInstruccion;
    private SharedPreferences preferences;
    private String contrasenaGuardada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etContrasena = findViewById(R.id.etContrasena);
        tvInstruccion = findViewById(R.id.tvInstruccion);
        Button btnIngresar = findViewById(R.id.btnIngresar);

        // Inicializar SharedPreferences para persistencia de la clave
        preferences = getSharedPreferences("AppLockPrefs", Context.MODE_PRIVATE);
        contrasenaGuardada = preferences.getString("password", null);

        // Si ya existe una contraseña, cambiamos el texto de instrucción
        if (contrasenaGuardada != null) {
            tvInstruccion.setText("Acceso Protegido\nIntroduce tu contraseña");
            btnIngresar.setText("Iniciar Sesión");
        } else {
            tvInstruccion.setText("Crear PIN de Seguridad\nDefine tu contraseña de acceso");
            btnIngresar.setText("Registrar Contraseña");
        }

        btnIngresar.setOnClickListener(v -> {
            String passwordInput = etContrasena.getText().toString().trim();

            if (passwordInput.isEmpty()) {
                Toast.makeText(this, "Por favor, escribe una contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            if (contrasenaGuardada == null) {
                // CASO 1: Es la primera vez, guardamos la nueva contraseña
                preferences.edit().putString("password", passwordInput).apply();
                Toast.makeText(this, "Contraseña configurada con éxito", Toast.LENGTH_SHORT).show();
                irAlMenuPrincipal();
            } else {
                // CASO 2: Ya existe contraseña, la validamos
                if (passwordInput.equals(contrasenaGuardada)) {
                    irAlMenuPrincipal();
                } else {
                    Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    etContrasena.setText(""); // Limpiar campo
                }
            }
        });
    }

    private void irAlMenuPrincipal() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Cierra la pantalla de Login para que el usuario no pueda regresar con el botón atrás
    }
}