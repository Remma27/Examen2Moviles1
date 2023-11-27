package com.example.a14_firebaseaccess

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class PantallaCompra : AppCompatActivity() {

    private lateinit var btnAgregarProducto: Button

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var txtCompania: EditText
    private lateinit var txtEntregar: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_compra)

        btnAgregarProducto = findViewById(R.id.btnAgregarProducto)

        btnAgregarProducto.setOnClickListener {
            MenuDeCompras()
        }

    }

    private fun MenuDeCompras(){
        val intent = Intent(this, PantallaProductos::class.java)
        startActivity(intent)
        finish() // Cierra la actividad actual para evitar que el usuario regrese con el botón "Atrás"
    }

}