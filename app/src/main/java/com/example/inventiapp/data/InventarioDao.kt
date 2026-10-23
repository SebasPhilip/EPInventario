package com.example.inventiapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.inventiapp.model.Productos
import kotlinx.coroutines.flow.Flow

@Dao
interface InventarioDao {
    @Query("SELECT * FROM productos")
    fun getAllProducts(): Flow<List<Productos>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Productos)

    @Update
    suspend fun updateProduct(product: Productos)

    @Query("SELECT * FROM productos WHERE sku = :sku")
    suspend fun getProductBySku(sku: String): Productos?
}