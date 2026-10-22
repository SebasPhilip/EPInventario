package com.example.inventiapp.model

data class DatosLogin (
    val usuario: String = "",
    val clave: String = "",
    val errores: LoginErrores = LoginErrores()
)