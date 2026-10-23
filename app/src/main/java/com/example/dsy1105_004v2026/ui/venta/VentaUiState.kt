package com.example.dsy1105_004v2026.ui.venta

data class VentaUiState(
    val id: Int = 0,
    val tipoServicio: String = "",
    val fechaOperacion: String = "",
    val cliente: String = "",
    val monto: String = "",
    val codigoAutorizacion: String = "",
    val respaldoUri: String? = null,
    val estado: String = "Borrador",
    val observaciones: String = "",
    val tipoServicioError: String? = null,
    val fechaError: String? = null,
    val clienteError: String? = null,
    val montoError: String? = null,
    val codigoError: String? = null,
    val guardando: Boolean = false,
    val guardadoExitosamente: Boolean = false
)
