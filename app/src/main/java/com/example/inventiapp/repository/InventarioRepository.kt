package com.example.inventiapp.repository



import com.example.inventiapp.data.InventarioDao
import com.example.inventiapp.model.Productos
import kotlinx.coroutines.flow.Flow

class InventarioRepository(private val inventarioDao: InventarioDao) {

    val producto: Flow<List<Productos>> = inventarioDao.getAllProducts()

    suspend fun updateStock(sku: String, quantity: Int, isEntry: Boolean): String {
        val product = inventarioDao.getProductBySku(sku)

        if (product != null) {
            if (!isEntry && product.stock < quantity) {
                return "Stock insuficiente para la salida"
            }
            product.stock = if (isEntry) product.stock + quantity else product.stock - quantity
            inventarioDao.updateProduct(product)
            return "Movimiento registrado con éxito"
        }
        return "Producto no encontrado"
    }

    suspend fun insertInitialData() {
        if (inventarioDao.getProductBySku("SKU001") == null) {
            inventarioDao.insertProduct(Productos("SKU001", "Router Wi-Fi D-Link", 15, 5))
            inventarioDao.insertProduct(Productos("SKU002", "Cable UTP Cat 6 (m)", 120, 50))
            inventarioDao.insertProduct(Productos("SKU003", "Switch 8 puertos", 3, 5))
        }
    }
}