package ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.budgetmanager1.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.Transaccion;
import database.AppDatabase;

public class ReportesActivity extends AppCompatActivity {

    private PieChart pieChart;
    private BarChart barChart;
    private List<Transaccion> todasLasTransacciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        pieChart = findViewById(R.id.pieChart);
        barChart = findViewById(R.id.barChart);
        Button btnPDF = findViewById(R.id.btnExportarPDF);

        // Cargar datos de Room
        todasLasTransacciones = AppDatabase.getInstance(this).transaccionDao().obtenerTodas();

        configurarGraficoPastel();
        configurarGraficoBarras();

        btnPDF.setOnClickListener(v -> generarPDFProfesional());
    }

    private void configurarGraficoPastel() {
        Map<String, Float> gastosPorCategoria = new HashMap<>();
        for (Transaccion t : todasLasTransacciones) {
            if (t.getTipo().equals("GASTO")) {
                float actual = gastosPorCategoria.getOrDefault(t.getCategoria(), 0f);
                gastosPorCategoria.put(t.getCategoria(), actual + (float) t.getMonto());
            }
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : gastosPorCategoria.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Gastos por Rubro");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Distribución de Gastos");
        pieChart.animateY(1000);
        pieChart.invalidate();
    }

    private void configurarGraficoBarras() {
        float totalIngresos = 0, totalGastos = 0;
        for (Transaccion t : todasLasTransacciones) {
            if (t.getTipo().equals("INGRESO")) totalIngresos += t.getMonto();
            else totalGastos += t.getMonto();
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, totalIngresos));
        entries.add(new BarEntry(1f, totalGastos));

        BarDataSet dataSet = new BarDataSet(entries, "Ingresos vs Gastos");
        dataSet.setColors(new int[]{Color.GREEN, Color.RED});

        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.animateX(1000);
        barChart.invalidate();
    }

    private void generarPDFProfesional() {
        PdfDocument pdf = new PdfDocument();
        Paint paint = new Paint();
        Paint tituloPaint = new Paint();

        // Configuración de página A4
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdf.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // Encabezado de Auditoría
        tituloPaint.setTextSize(20f);
        tituloPaint.setFakeBoldText(true);
        canvas.drawText("REPORTE DE AUDITORÍA FINANCIERA", 150, 50, tituloPaint);

        paint.setTextSize(12f);
        canvas.drawText("Generado por: BudgetManager Pro", 50, 80, paint);
        java.util.Date fechaActual = new java.util.Date();
        java.text.SimpleDateFormat formateador = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault());
        String fechaFormateada = formateador.format(fechaActual);

        canvas.drawText("Fecha y Hora: " + fechaFormateada, 50, 100, paint);
        canvas.drawLine(50, 110, 550, 110, paint);

        // Tabla de datos
        int y = 140;
        canvas.drawText("Concepto", 50, y, tituloPaint);
        canvas.drawText("Categoría", 250, y, tituloPaint);
        canvas.drawText("Monto", 450, y, tituloPaint);
        y += 30;

        tituloPaint.setFakeBoldText(false);
        tituloPaint.setTextSize(10f);

        for (Transaccion t : todasLasTransacciones) {
            if (y > 800) break; // Evitar que se salga de la página (puedes mejorar esto con múltiples páginas)
            canvas.drawText(t.getConcepto(), 50, y, tituloPaint);
            canvas.drawText(t.getCategoria(), 250, y, tituloPaint);
            String monto = (t.getTipo().equals("INGRESO") ? "+" : "-") + "$" + t.getMonto();
            canvas.drawText(monto, 450, y, tituloPaint);
            y += 20;
        }

        pdf.finishPage(page);

        // Guardar en la carpeta de Descargas del teléfono
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Auditoria_Financiera.pdf");

        try {
            pdf.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "PDF Guardado en Descargas", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al generar PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        pdf.close();
    }
}