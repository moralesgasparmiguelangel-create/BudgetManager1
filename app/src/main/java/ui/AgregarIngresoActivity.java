package ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.budgetmanager1.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import data.Transaccion;
import database.AppDatabase;

public class AgregarIngresoActivity extends AppCompatActivity {

    private EditText etMontoIngreso, etConceptoIngreso;
    private Spinner spCategoriaIngreso;
    private Button btnGuardarIngreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ingreso);

        etMontoIngreso = findViewById(R.id.etMontoIngreso);
        etConceptoIngreso = findViewById(R.id.etConceptoIngreso);
        spCategoriaIngreso = findViewById(R.id.spCategoriaIngreso);
        btnGuardarIngreso = findViewById(R.id.btnGuardarIngreso);

        btnGuardarIngreso.setOnClickListener(v -> {
            String montoStr = etMontoIngreso.getText().toString().trim();
            String concepto = etConceptoIngreso.getText().toString().trim();
            String categoriaSeleccionada = spCategoriaIngreso.getSelectedItem().toString();

            if (!montoStr.isEmpty() && !concepto.isEmpty()) {
                double monto = Double.parseDouble(montoStr);
                String fechaHoraActual = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

                // CREAR OBJETO
                Transaccion nuevaTransaccion = new Transaccion(concepto, monto, "INGRESO", fechaHoraActual, categoriaSeleccionada);

                // GUARDAR EN BASE DE DATOS LOCAL
                AppDatabase.getInstance(this).transaccionDao().insertar(nuevaTransaccion);

                Toast.makeText(this, "Ingreso guardado en BD", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}