package com.example.a14_firebaseaccess

import ProductAdapter
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import com.example.a14_firebaseaccess.entities.cls_Category
import com.example.a14_firebaseaccess.entities.cls_Product
import com.google.firebase.firestore.FirebaseFirestore

class PantallaProductos : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_productos)

        obtenerDatos()
    }

    private fun obtenerDatos() {
        val coleccion: ArrayList<cls_Product?> = ArrayList()
        val listaView: ListView = findViewById(R.id.lstProducts)

        db.collection("Products").orderBy("ProductID")
            .get()
            .addOnCompleteListener { docc ->
                if (docc.isSuccessful) {
                    for (document in docc.result!!) {
                        Log.d(TAG, "${document.id} => ${document.data}")

                        try {
                            // Intenta realizar conversiones seguras
                            val productId = document.get("ProductID").toString()
                            val productName = document.get("ProductName").toString()
                            val supplierID = document.get("SupplierID").toString()
                            val categoryID = document.get("CategoryID").toString()
                            val quantityPerUnit = document.get("QuantityPerUnit").toString()
                            val unitPrice = document.get("UnitPrice").toString()
                            val unitsInStock = document.get("UnitsInStock").toString()
                            val unitsOnOrder = document.get("UnitsOnOrder").toString()
                            val reorderLevel = document.get("ReorderLevel").toString()
                            val discontinued = document.get("Discontinued").toString()
                            val datos = cls_Product(
                                productId, productName, supplierID, categoryID, quantityPerUnit, unitPrice,unitsInStock,unitsOnOrder,reorderLevel,discontinued
                            )
                            coleccion.add(datos)
                        } catch (e: Exception) {
                            // Maneja la conversión de manera segura
                            Log.e(TAG, "Error al convertir datos", e)
                        }
                    }

                    // Configura el adaptador y establece en la lista
                    val adapter = ProductAdapter(this, coleccion)
                    listaView.adapter = adapter
                } else {
                    Log.w(TAG, "Error obteniendo documentos.", docc.exception)

                    // Muestra un mensaje al usuario
                    Toast.makeText(this, "Error al obtener datos. Inténtelo de nuevo más tarde.", Toast.LENGTH_SHORT).show()
                }
            }
    }



}