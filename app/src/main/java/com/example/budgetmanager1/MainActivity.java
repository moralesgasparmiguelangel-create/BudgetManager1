package com.example.budgetmanager1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Vistas de la interfaz de usuario con nombres descriptivos
    private TextView tvBalance, tvIngresosTotal, tvGastosTotal;
    private Button btnAgregarIngreso, btnAgregarGasto, btnLimpiarHistorial;
    private ListView lvHistorial;

    // Instancia de la base de datos local
    private AppDatabase baseDatos;

    // Variables de control de estado del presupuesto
    private double totalIngresos = 0.0;
    private double totalGastos = 0.0;

    // Lista y adaptador para el historial de movimientos
    private ArrayList<Transaccion> listaTransacciones = new ArrayList<>();
    private ArrayAdapter<Transaccion> adaptadorHistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar la base de datos de Room
        baseDatos = AppDatabase.getInstance(this);

        // Vincular componentes de la interfaz
        tvBalance = findViewById(R.id.tvBalance);
        tvIngresosTotal = findViewById(R.id.tvIngresosTotal);
        tvGastosTotal = findViewById(R.id.tvGastosTotal);
        btnAgregarIngreso = findViewById(R.id.btnAgregarIngreso);
        btnAgregarGasto = findViewById(R.id.btnAgregarGasto);
        lvHistorial = findViewById(R.id.lvHistorial);
        btnLimpiarHistorial = findViewById(R.id.btnLimpiarHistorial);
        Button btnCambiarContrasena = findViewById(R.id.btnCambiarContrasena);

        // Acción para cambiar la contraseña de acceso de la app
        btnCambiarContrasena.setOnClickListener(vista -> {
            final EditText etNuevaClave = new EditText(this);
            etNuevaClave.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etNuevaClave.setHint("Escribe la nueva contraseña");

            // Contenedor para dar margen estético al EditText dentro del diálogo
            FrameLayout contenedorDialogo = new FrameLayout(this);
            FrameLayout.LayoutParams parametrosDiseño = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            parametrosDiseño.leftMargin = 50;
            parametrosDiseño.rightMargin = 50;
            etNuevaClave.setLayoutParams(parametrosDiseño);
            contenedorDialogo.addView(etNuevaClave);

            // Crear y mostrar el cuadro de diálogo
            new AlertDialog.Builder(this)
                    .setTitle("Cambiar Contraseña de Acceso")
                    .setMessage("Introduce tu nueva clave de seguridad:")
                    .setView(contenedorDialogo)
                    .setPositiveButton("Guardar", (dialogo, botonId) -> {
                        String nuevaContrasena = etNuevaClave.getText().toString().trim();

                        if (!nuevaContrasena.isEmpty()) {
                            SharedPreferences preferencias = getSharedPreferences("AppLockPrefs", Context.MODE_PRIVATE);
                            preferencias.edit().putString("password", nuevaContrasena).apply();
                            Toast.makeText(this, "Contraseña actualizada con éxito", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Operación cancelada: No puedes dejar el campo vacío", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", (dialogo, botonId) -> dialogo.dismiss())
                    .show();
        });

        // Acción para vaciar por completo los datos guardados
        btnLimpiarHistorial.setOnClickListener(vista -> {
            baseDatos.transaccionDao().borrarTodo();
            listaTransacciones.clear();
            adaptadorHistorial.notifyDataSetChanged();
            actualizarInterfazPresupuesto();
            Toast.makeText(this, "Historial eliminado por completo", Toast.LENGTH_SHORT).show();
        });

        // Adaptador optimizado que fuerza el color del texto a negro
        adaptadorHistorial = new ArrayAdapter<Transaccion>(this, android.R.layout.simple_list_item_1, listaTransacciones) {
            @Override
            public View getView(int posicion, View vistaConvertida, ViewGroup padre) {
                View vistaCelda = super.getView(posicion, vistaConvertida, padre);

                TextView textoFila = vistaCelda.findViewById(android.R.id.text1);
                if (textoFila != null) {
                    textoFila.setTextColor(Color.parseColor("#1E293B")); // Negro carbón moderno
                    textoFila.setTextSize(15f);
                }

                return vistaCelda;
            }
        };
        lvHistorial.setAdapter(adaptadorHistorial);

        // Navegación hacia los diferentes flujos de la aplicación
        btnAgregarIngreso.setOnClickListener(vista -> {
            Intent intent = new Intent(MainActivity.this, AgregarIngresoActivity.class);
            startActivity(intent);
        });

        btnAgregarGasto.setOnClickListener(vista -> {
            Intent intent = new Intent(MainActivity.this, AgregarGastoActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnVerReportes).setOnClickListener(vista -> {
            Intent intent = new Intent(this, ReportesActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Sincronizar datos de la base de datos local al volver a la pantalla
        listaTransacciones.clear();
        listaTransacciones.addAll(baseDatos.transaccionDao().obtenerTodas());
        actualizarInterfazPresupuesto();
        adaptadorHistorial.notifyDataSetChanged();
    }

    /**
     * Calcula los ingresos/gastos basados en la lista actual y actualiza los TextViews.
     * Reemplaza las funciones duplicadas anteriores para simplificar el código.
     */
    private void actualizarInterfazPresupuesto() {
        totalIngresos = 0.0;
        totalGastos = 0.0;

        for (Transaccion transaccionActual : listaTransacciones) {
            if (transaccionActual.getTipo().equals("INGRESO")) {
                totalIngresos += transaccionActual.getMonto();
            } else if (transaccionActual.getTipo().equals("GASTO")) {
                totalGastos += transaccionActual.getMonto();
            }
        }

        double balanceNeto = totalIngresos - totalGastos;

        tvIngresosTotal.setText(String.format("$%.2f", totalIngresos));
        tvGastosTotal.setText(String.format("$%.2f", totalGastos));
        tvBalance.setText(String.format("$%.2f", balanceNeto));
    }
}