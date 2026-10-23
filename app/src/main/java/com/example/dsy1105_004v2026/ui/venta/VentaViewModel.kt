package com.example.dsy1105_004v2026.ui.venta

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.dsy1105_004v2026.data.local.AppDatabase
import com.example.dsy1105_004v2026.data.local.VentaEntity
import com.example.dsy1105_004v2026.data.repository.VentaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class VentaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = VentaRepository(
        AppDatabase.getDatabase(application).ventaDao()
    )

    // Estado del formulario
    private val _uiState = MutableStateFlow(VentaUiState())
    val uiState: StateFlow<VentaUiState> = _uiState.asStateFlow()

    // Lista de ventas guardadas en Room (se actualiza sola)
    val ventas: StateFlow<List<VentaEntity>> = repository.ventas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ---------- Cambios en los campos ----------
    fun onTipoServicioChange(value: String) =
        _uiState.update { it.copy(tipoServicio = value, tipoServicioError = null) }

    fun onFechaChange(value: String) =
        _uiState.update { it.copy(fechaOperacion = value, fechaError = null) }

    fun onClienteChange(value: String) =
        _uiState.update { it.copy(cliente = value, clienteError = null) }

    fun onMontoChange(value: String) =
        _uiState.update { it.copy(monto = value, montoError = null) }

    fun onCodigoChange(value: String) =
        _uiState.update { it.copy(codigoAutorizacion = value, codigoError = null) }

    fun onObservacionesChange(value: String) =
        _uiState.update { it.copy(observaciones = value) }

    // La foto tomada con la cámara se guarda como archivo y se asocia a la venta
    fun onFotoCapturada(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            val archivo = File(
                getApplication<Application>().filesDir,
                "respaldo_${System.currentTimeMillis()}.jpg"
            )
            FileOutputStream(archivo).use { salida ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, salida)
            }
            _uiState.update { it.copy(respaldoUri = archivo.absolutePath) }
        }
    }

    // ---------- Preparar el formulario ----------
    fun nuevaVenta() {
        _uiState.value = VentaUiState()
    }

    fun cargarVenta(id: Int) {
        _uiState.value = VentaUiState()
        viewModelScope.launch {
            val venta = repository.obtenerPorId(id) ?: return@launch
            _uiState.value = VentaUiState(
                id = venta.id,
                tipoServicio = venta.tipoServicio,
                fechaOperacion = venta.fechaOperacion,
                cliente = venta.cliente,
                monto = venta.monto.toString(),
                codigoAutorizacion = venta.codigoAutorizacion,
                respaldoUri = venta.respaldoUri,
                estado = venta.estado,
                observaciones = venta.observaciones
            )
        }
    }

    // ---------- Validar y guardar ----------
    // estadoNuevo: "Borrador" (solo guardar) o "Recibida" (enviar a revisión)
    fun guardar(estadoNuevo: String) {
        val s = _uiState.value
        val codigo = s.codigoAutorizacion.trim()
        val montoNumero = s.monto.trim().toDoubleOrNull()

        val tipoError = if (s.tipoServicio.isBlank()) "El tipo de servicio es obligatorio" else null
        val fechaError = if (s.fechaOperacion.isBlank()) "La fecha es obligatoria" else null
        val clienteError = if (s.cliente.isBlank()) "El cliente es obligatorio" else null
        val montoError = when {
            s.monto.isBlank() -> "El monto es obligatorio"
            montoNumero == null -> "El monto debe ser numérico"
            montoNumero <= 0 -> "El monto debe ser mayor que 0"
            else -> null
        }

        viewModelScope.launch {
            val codigoError = when {
                codigo.isBlank() -> "El código de autorización es obligatorio"
                repository.codigoExiste(codigo, s.id) -> "El código ya existe"
                else -> null
            }

            _uiState.update {
                it.copy(
                    tipoServicioError = tipoError,
                    fechaError = fechaError,
                    clienteError = clienteError,
                    montoError = montoError,
                    codigoError = codigoError
                )
            }

            val hayErrores = listOf(tipoError, fechaError, clienteError, montoError, codigoError)
                .any { it != null }
            if (hayErrores) return@launch

            _uiState.update { it.copy(guardando = true) }
            repository.guardar(
                VentaEntity(
                    id = s.id,
                    tipoServicio = s.tipoServicio.trim(),
                    fechaOperacion = s.fechaOperacion.trim(),
                    cliente = s.cliente.trim(),
                    monto = montoNumero!!,
                    codigoAutorizacion = codigo,
                    respaldoUri = s.respaldoUri,
                    estado = estadoNuevo,
                    observaciones = s.observaciones.trim()
                )
            )
            _uiState.update { it.copy(guardando = false, guardadoExitosamente = true) }
        }
    }
}
