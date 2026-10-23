package com.example.dsy1105_004v2026.ui.venta

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// Convenios que comercializa la agencia
private val tiposServicio = listOf("Dental", "Automotriz", "Veterinario")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentaFormScreen(
    ventaId: Int,
    navController: NavController,
    viewModel: VentaViewModel,
    hasCameraPermission: Boolean,
    onRequestCameraPermission: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    // Una venta validada solo se puede consultar
    val soloLectura = state.estado == "Validada"

    // Recurso nativo: cámara. La foto del comprobante queda asociada a la venta
    val camaraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) viewModel.onFotoCapturada(bitmap)
    }

    // Foto guardada de esta venta (si existe)
    val foto = remember(state.respaldoUri) {
        state.respaldoUri?.let { BitmapFactory.decodeFile(it) }
    }

    // Al entrar: formulario vacío (id = 0) o venta existente
    LaunchedEffect(ventaId) {
        if (ventaId == 0) viewModel.nuevaVenta() else viewModel.cargarVenta(ventaId)
    }

    // Al guardar correctamente se vuelve a la lista
    LaunchedEffect(state.guardadoExitosamente) {
        if (state.guardadoExitosamente) {
            navController.popBackStack()
            viewModel.nuevaVenta()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (ventaId == 0) "Nueva venta" else "Editar venta") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Tipo de convenio: se selecciona con chips
            Text("Tipo de convenio o servicio")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                tiposServicio.forEach { tipo ->
                    FilterChip(
                        selected = state.tipoServicio == tipo,
                        onClick = { viewModel.onTipoServicioChange(tipo) },
                        label = { Text(tipo) },
                        enabled = !soloLectura
                    )
                }
            }
            Text(
                text = state.tipoServicioError ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )

            CampoVenta(
                value = state.fechaOperacion,
                onChange = viewModel::onFechaChange,
                label = "Fecha de operación (dd/mm/aaaa)",
                error = state.fechaError,
                enabled = !soloLectura
            )
            CampoVenta(
                value = state.cliente,
                onChange = viewModel::onClienteChange,
                label = "Cliente (ficticio)",
                error = state.clienteError,
                enabled = !soloLectura
            )
            CampoVenta(
                value = state.monto,
                onChange = viewModel::onMontoChange,
                label = "Monto",
                error = state.montoError,
                enabled = !soloLectura,
                keyboardType = KeyboardType.Number
            )
            CampoVenta(
                value = state.codigoAutorizacion,
                onChange = viewModel::onCodigoChange,
                label = "Código de autorización",
                error = state.codigoError,
                enabled = !soloLectura
            )
            CampoVenta(
                value = state.observaciones,
                onChange = viewModel::onObservacionesChange,
                label = "Observaciones",
                error = null,
                enabled = !soloLectura
            )

            // Respaldo fotográfico (ficticio) del comprobante
            if (foto != null) {
                Image(
                    bitmap = foto.asImageBitmap(),
                    contentDescription = "Respaldo de la venta",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            } else {
                Text("Sin respaldo fotográfico")
            }
            OutlinedButton(
                onClick = {
                    if (hasCameraPermission) camaraLauncher.launch(null)
                    else onRequestCameraPermission()
                },
                enabled = !soloLectura,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (foto == null) "Tomar foto del comprobante" else "Tomar otra foto")
            }

            Text("Estado: ${state.estado}", color = MaterialTheme.colorScheme.secondary)
            Spacer(Modifier.height(16.dp))

            if (!soloLectura) {
                OutlinedButton(
                    onClick = { viewModel.guardar("Borrador") },
                    enabled = !state.guardando,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (state.guardando) "Guardando..." else "Guardar borrador")
                }
                Button(
                    onClick = { viewModel.guardar("Recibida") },
                    enabled = !state.guardando,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Enviar a revisión")
                }
            }

            TextButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}

// Campo de texto con el error visible bajo el campo
@Composable
private fun CampoVenta(
    value: String,
    onChange: (String) -> Unit,
    label: String,
    error: String?,
    enabled: Boolean,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        isError = error != null,
        supportingText = { Text(error ?: "") },
        enabled = enabled,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth()
    )
}
