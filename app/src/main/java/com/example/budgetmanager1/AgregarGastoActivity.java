package com.example.budgetmanager1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AgregarGastoActivity extends AppCompatActivity {

    private EditText etMontoGasto, etConceptoGasto;
    private Spinner spCategoriaGasto;
    private Button btnGuardarGasto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_gasto);

        etMontoGasto = findViewById(R.id.etMontoGasto);
        etConceptoGasto = findViewById(R.id.etConceptoGasto);
        spCategoriaGasto = findViewById(R.id.spCategoriaGasto);
        btnGuardarGasto = findViewById(R.id.btnGuardarGasto);

        btnGuardarGasto.setOnClickListener(v -> {
            String montoStr = etMontoGasto.getText().toString().trim();
            String concepto = etConceptoGasto.getText().toString().trim();
            String categoriaSeleccionada = spCategoriaGasto.getSelectedItem().toString();

            if (!montoStr.isEmpty() && !concepto.isEmpty()) {
                double monto = Double.parseDouble(montoStr);
                String fechaHoraActual = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

                // CREAR OBJETO
                Transaccion nuevaTransaccion = new Transaccion(concepto, monto, "GASTO", fechaHoraActual, categoriaSeleccionada);

                // GUARDAR EN BASE DE DATOS LOCAL
                com.example.budgetmanager1.AppDatabase.getInstance(this).transaccionDao().insertar(nuevaTransaccion);

                Toast.makeText(this, "Gasto guardado en BD", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}