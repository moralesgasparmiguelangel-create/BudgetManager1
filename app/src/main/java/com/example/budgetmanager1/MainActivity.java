package com.example.budgetmanager1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView tvBalance, tvIngresosTotal, tvGastosTotal;
    private Button btnAgregarIngreso, btnAgregarGasto, btnLimpiarHistorial;
    private ListView lvHistorial;

    private com.example.budgetmanager1.AppDatabase db;

    // Variables globales simuladas
    public static double ingresos = 0.0;
    public static double gastos = 0.0;

    // Lista global donde se guardarán todos los movimientos
    public static ArrayList<Transaccion> listaTransacciones = new ArrayList<>();
    private ArrayAdapter<Transaccion> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar base de datos de Room
        db = com.example.budgetmanager1.AppDatabase.getInstance(this);
        tvBalance = findViewById(R.id.tvBalance);
        tvIngresosTotal = findViewById(R.id.tvIngresosTotal);
        tvGastosTotal = findViewById(R.id.tvGastosTotal);
        btnAgregarIngreso = findViewById(R.id.btnAgregarIngreso);
        btnAgregarGasto = findViewById(R.id.btnAgregarGasto);
        lvHistorial = findViewById(R.id.lvHistorial);
        btnLimpiarHistorial = findViewById(R.id.btnLimpiarHistorial);
        Button btnCambiarContrasena = findViewById(R.id.btnCambiarContrasena);

        btnCambiarContrasena.setOnClickListener(v -> {
            // Crear un campo de texto dinámico para la ventana flotante
            final EditText etNuevaClave = new EditText(this);
            etNuevaClave.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etNuevaClave.setHint("Escribe la nueva contraseña");

            // Contenedor para darle margen estético al EditText dentro del diálogo
            android.widget.FrameLayout container = new android.widget.FrameLayout(this);
            android.widget.FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.leftMargin = 50; // Margen izquierdo
            params.rightMargin = 50; // Margen derecho
            etNuevaClave.setLayoutParams(params);
            container.addView(etNuevaClave);

            // Crear el cuadro de diálogo (AlertDialog)
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Cambiar Contraseña de Acceso")
                    .setMessage("Introduce tu nueva clave de seguridad:")
                    .setView(container)
                    .setPositiveButton("Guardar", (dialog, which) -> {
                        String nuevaContrasena = etNuevaClave.getText().toString().trim();

                        if (!nuevaContrasena.isEmpty()) {
                            // Acceder al mismo archivo de preferencias que usa el LoginActivity
                            android.content.SharedPreferences preferences = getSharedPreferences("AppLockPrefs", android.content.Context.MODE_PRIVATE);

                            // Guardar la nueva contraseña encima de la anterior
                            preferences.edit().putString("password", nuevaContrasena).apply();

                            android.widget.Toast.makeText(this, "Contraseña actualizada con éxito", android.widget.Toast.LENGTH_SHORT).show();
                        } else {
                            android.widget.Toast.makeText(this, "Operación cancelada: No puedes dejar el campo vacío", android.widget.Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        btnLimpiarHistorial.setOnClickListener(v -> {
            db.transaccionDao().borrarTodo();
            listaTransacciones.clear();
            adapter.notifyDataSetChanged();
            recalcularTotales();
            android.widget.Toast.makeText(this, "Historial eliminado por completo", android.widget.Toast.LENGTH_SHORT).show();
        });

        // Adaptador optimizado que fuerza el color del texto a negro de forma permanente
        adapter = new ArrayAdapter<Transaccion>(this, android.R.layout.simple_list_item_1, listaTransacciones) {
            @Override
            public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
                android.view.View vistaCelda = super.getView(position, convertView, parent);

                // Buscamos el elemento de texto interno nativo y le aplicamos color negro
                TextView textoFila = vistaCelda.findViewById(android.R.id.text1);
                if (textoFila != null) {
                    textoFila.setTextColor(android.graphics.Color.parseColor("#1E293B")); // Negro carbón moderno
                    textoFila.setTextSize(15f); // Tamaño estilizado
                }

                return vistaCelda;
            }
        };
        lvHistorial.setAdapter(adapter);

        btnAgregarIngreso.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AgregarIngresoActivity.class);
            startActivity(intent);
        });

        btnAgregarGasto.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AgregarGastoActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnVerReportes).setOnClickListener(v -> {
            Intent intent = new Intent(this, ReportesActivity.class);
            startActivity(intent);
        });

        actualizarResumen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 1. Limpiar la lista actual en memoria
        listaTransacciones.clear();

        // 2. Traer los datos guardados en la Base de Datos Local
        listaTransacciones.addAll(db.transaccionDao().obtenerTodas());

        // 3. Recalcular los totales basados en la BD
        recalcularTotales();

        // 4. Refrescar la pantalla
        adapter.notifyDataSetChanged();
    }

    private void recalcularTotales() {
        ingresos = 0.0;
        gastos = 0.0;

        // Recorremos la base de datos para sumar los montos correspondientes
        for (Transaccion t : listaTransacciones) {
            if (t.getTipo().equals("INGRESO")) {
                ingresos += t.getMonto();
            } else if (t.getTipo().equals("GASTO")) {
                gastos += t.getMonto();
            }
        }

        double balance = ingresos - gastos;
        tvIngresosTotal.setText(String.format("$%.2f", ingresos));
        tvGastosTotal.setText(String.format("$%.2f", gastos));
        tvBalance.setText(String.format("$%.2f", balance));
    }

    private void actualizarResumen() {
        double balance = ingresos - gastos;
        tvIngresosTotal.setText(String.format("$%.2f", ingresos));
        tvGastosTotal.setText(String.format("$%.2f", gastos));
        tvBalance.setText(String.format("$%.2f", balance));
    }
}