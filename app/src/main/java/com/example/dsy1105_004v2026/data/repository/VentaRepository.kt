package com.example.dsy1105_004v2026.data.repository

import com.example.dsy1105_004v2026.data.local.VentaDao
import com.example.dsy1105_004v2026.data.local.VentaEntity
import kotlinx.coroutines.flow.Flow

class VentaRepository(private val dao: VentaDao) {

    val ventas: Flow<List<VentaEntity>> = dao.obtenerTodas()

    suspend fun obtenerPorId(id: Int): VentaEntity? = dao.obtenerPorId(id)

    suspend fun codigoExiste(codigo: String, id: Int): Boolean =
        dao.contarPorCodigo(codigo, id) > 0

    // id = 0 -> venta nueva; id distinto de 0 -> se modifica la existente
    suspend fun guardar(venta: VentaEntity) {
        if (venta.id == 0) dao.insertar(venta) else dao.actualizar(venta)
    }
}
