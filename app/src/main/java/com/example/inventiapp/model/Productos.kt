package com.example.inventiapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Productos(
    @PrimaryKey val sku: String,
    val nombre: String,
    var stock: Int,
    val minStock: Int
)