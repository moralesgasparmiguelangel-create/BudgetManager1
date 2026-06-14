package com.example.budgetmanager1;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface TransaccionDao {

    @Insert
    void insertar(Transaccion transaccion);

    @Query("SELECT * FROM tabla_transacciones ORDER BY id DESC")
    List<Transaccion> obtenerTodas(); // Trae el historial, el más reciente primero

    // NUEVA CONSULTA: Borra todas las filas de la tabla
    @Query("DELETE FROM tabla_transacciones")
    void borrarTodo();
}