package com.example.dsy1105_004v2026.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VentaDao {

    // Flow: la lista se actualiza sola cuando cambia la tabla
    @Query("SELECT * FROM ventas ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<VentaEntity>>

    @Query("SELECT * FROM ventas WHERE id = :id")
    suspend fun obtenerPorId(id: Int): VentaEntity?

    // Cuenta ventas con el mismo código (excluyendo la que se está editando)
    @Query("SELECT COUNT(*) FROM ventas WHERE codigoAutorizacion = :codigo AND id != :id")
    suspend fun contarPorCodigo(codigo: String, id: Int): Int

    @Insert
    suspend fun insertar(venta: VentaEntity)

    @Update
    suspend fun actualizar(venta: VentaEntity)
}
