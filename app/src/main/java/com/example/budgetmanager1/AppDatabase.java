package com.example.budgetmanager1;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Transaccion.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract com.example.budgetmanager1.TransaccionDao transaccionDao();
    private static AppDatabase instancia = null;

    // Patrón Singleton para evitar abrir varias instancias de la BD a la vez
    public static synchronized AppDatabase getInstance(Context context) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "presupuesto_db" // Nombre del archivo de la base de datos
                    )
                    .allowMainThreadQueries() // Permite consultas sencillas de forma directa
                    .build();
        }
        return instancia;
    }
}