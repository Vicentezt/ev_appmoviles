package com.example.dsy1105_004v2026.data.model

data class Convenio(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val beneficios: List<String>
)

// Catálogo fijo de convenios (datos ficticios, no requiere base de datos)
val catalogoConvenios = listOf(
    Convenio(
        id = "dental",
        nombre = "Clínicas dentales",
        descripcion = "Convenio con clínicas dentales asociadas.",
        beneficios = listOf(
            "Evaluación inicial sin costo",
            "Descuento en limpieza dental",
            "Descuento en ortodoncia"
        )
    ),
    Convenio(
        id = "automotriz",
        nombre = "Servicios automotrices",
        descripcion = "Convenio con talleres y centros de servicio.",
        beneficios = listOf(
            "Revisión técnica con descuento",
            "Mantención preventiva",
            "Cambio de aceite con descuento"
        )
    ),
    Convenio(
        id = "veterinario",
        nombre = "Servicios veterinarios",
        descripcion = "Convenio con clínicas veterinarias.",
        beneficios = listOf(
            "Consulta general con descuento",
            "Plan de vacunas",
            "Descuento en peluquería"
        )
    )
)
