package com.example.a14_firebaseaccess

import CategoryAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a14_firebaseaccess.entities.cls_Category
import com.example.a14_firebaseaccess.ui.users.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


const val valorIntentLogin = 1

class MainActivity : AppCompatActivity() {

    var auth = FirebaseAuth.getInstance()
    var email: String? = null
    var contra: String? = null

    var db = FirebaseFirestore.getInstance()
    var TAG = "YorkTestingApp"

    private lateinit var btnLogOut: Button
    private lateinit var btnMenuDeCompras: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogOut = findViewById(R.id.btnLogOut)
        btnMenuDeCompras = findViewById(R.id.btnMenuDeCompras)



        // intenta obtener el token del usuario del local storage, sino llama a la ventana de registro
        val prefe = getSharedPreferences("appData", Context.MODE_PRIVATE)
        email = prefe.getString("email", "")
        contra = prefe.getString("contra", "")

        if (email.toString().trim { it <= ' ' }.length == 0) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivityForResult(intent, valorIntentLogin)
        } else {
            val uid: String = auth.uid.toString()
            if (uid == "null") {
                auth.signInWithEmailAndPassword(email.toString(), contra.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Autenticación correcta", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
            obtenerDatos()
        }

        btnLogOut.setOnClickListener {
            LogOut()
        }

        btnMenuDeCompras.setOnClickListener {
            MenuDeCompras()
        }
    }

    private fun LogOut() {
        auth.signOut()
        Toast.makeText(this, "Cerrar sesión exitosa", Toast.LENGTH_SHORT).show()

        // Redirige a la pantalla de inicio de sesión
        val intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(intent, valorIntentLogin)
        finish() // Cierra la actividad actual para evitar que el usuario regrese con el botón "Atrás"
    }

    private fun MenuDeCompras(){
        val intent = Intent(this, PantallaCompra::class.java)
        startActivity(intent)
        finish() // Cierra la actividad actual para evitar que el usuario regrese con el botón "Atrás"
    }




    private fun obtenerDatos() {
        //Toast.makeText(this,"Esperando hacer algo importante", Toast.LENGTH_LONG).show()
        var coleccion: ArrayList<cls_Category?> = ArrayList()
        var listaView: ListView = findViewById(R.id.lstCategories)
        db.collection("Categories").orderBy("CategoryID")
            .get()
            .addOnCompleteListener { docc ->
                if (docc.isSuccessful) {
                    for (document in docc.result!!) {
                        Log.d(TAG, document.id + " => " + document.data)
                        var datos: cls_Category = cls_Category(
                            document.data["CategoryID"].toString().toInt(),
                            document.data["CategoryName"].toString(),
                            document.data["Description"].toString(),
                            document.data["urlImage"].toString()
                        )
                        coleccion.add(datos)
                    }
                    var adapter: CategoryAdapter = CategoryAdapter(this, coleccion)
                    listaView.adapter = adapter
                } else {
                    Log.w(TAG, "Error getting documents.", docc.exception)
                }
            }
    }
}