package com.example.a14_firebaseaccess.ui.users

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.a14_firebaseaccess.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date


class SignupActivity : AppCompatActivity() {

    var auth = FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()

    private lateinit var txtRNombre: EditText
    private lateinit var txtREmail: EditText
    private lateinit var txtRContra: EditText
    private lateinit var txtRreContra: EditText
    private lateinit var btnRegistrarU: Button

    //Dos espacios nuevos
    private lateinit var txtCustomerID: EditText
    private lateinit var txtCompanyName: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        txtRNombre = findViewById(R.id.txtRNombre)
        txtREmail = findViewById(R.id.txtREmail)
        txtRContra = findViewById(R.id.txtRContra)
        txtRreContra = findViewById(R.id.txtRreContra)
        btnRegistrarU = findViewById(R.id.btnRegistrarU)

        //Dos espacios nuevos
        txtCustomerID = findViewById(R.id.txtCustomerID)
        txtCompanyName = findViewById(R.id.txtCompanyName)



        btnRegistrarU.setOnClickListener {
            registrarUsuario()
        }
    }

    private fun registrarUsuario() {
        // Declaración de variables
        val nombre = txtRNombre.text.toString()
        val email = txtREmail.text.toString()
        val contra = txtRContra.text.toString()
        val reContra = txtRreContra.text.toString()
        val customerID = txtCustomerID.text.toString().trim().toUpperCase()
        val companyName = txtCompanyName.text.toString()

        // Validación de que no esté vacío
        if (nombre.isEmpty() || email.isEmpty() || contra.isEmpty() || reContra.isEmpty() || customerID.isEmpty() || companyName.isEmpty()) {
            Toast.makeText(this, "Favor de llenar todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Validación contraseñas iguales
        if (contra != reContra) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        // Variables para la colección "Customers"
        val customersCollection = db.collection("Customers")

        // Realizar la consulta para encontrar el documento basado en CustomerID
        customersCollection.whereEqualTo("CustomerID", customerID)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val querySnapshot = task.result
                    if (querySnapshot != null && !querySnapshot.documents.isEmpty()) {
                        // El cliente existe, actualizar datos
                        val documentId = querySnapshot.documents[0].id // Obtener el ID del documento

                        // El ContactName y el ContactTitle de dicha colección, varíen de acuerdo con los datos
                        // suministrados por ustedes en el módulo de signup.
                        val updateData = mapOf(
                            "ContactName" to nombre,
                            "CompanyName" to companyName
                        )

                        customersCollection.document(documentId)
                            .update(updateData)
                            .addOnSuccessListener {
                                // La actualización fue exitosa

                                val ordersCollection = db.collection("Orders")
                                ordersCollection
                                    .whereEqualTo("CustomerID", customerID)
                                    .get()
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            val querySnapshot = task.result
                                            if (querySnapshot != null && !querySnapshot.documents.isEmpty()) {
                                                val document = querySnapshot.documents[0]
                                                Log.d("TuTag", document.toString())


                                                val shipVia = document.get("ShipVia")
                                                val shipName = document.get("ShipName")
                                                val shipAddress = document.get("ShipAddress")
                                                val shipCity = document.get("ShipCity")
                                                val shipRegion = document.get("ShipRegion")
                                                val shipPostalCode = document.get("ShipPostalCode")
                                                val shipCountry = document.get("ShipCountry")

                                                Log.d("TuTag", "ShipVia: $shipVia")
                                                Log.d("TuTag", "ShipName: $shipName")
                                                Log.d("TuTag", "ShipAddress: $shipAddress")
                                                Log.d("TuTag", "ShipCity: $shipCity")
                                                Log.d("TuTag", "ShipRegion: $shipRegion")
                                                Log.d("TuTag", "ShipPostalCode: $shipPostalCode")
                                                Log.d("TuTag", "ShipCountry: $shipCountry")


                                                // Resto del código para registrar usuario en Firebase Authentication
                                                auth.createUserWithEmailAndPassword(email, contra)
                                                    .addOnCompleteListener(this) { authTask ->
                                                        if (authTask.isSuccessful) {
                                                            val dt: Date = Date()
                                                            val user = hashMapOf(
                                                                "idemp" to authTask.result?.user?.uid,
                                                                "usuario" to nombre,
                                                                "email" to email,
                                                                // En la colección de datosUsuarios del FireStore agregar el campo referente al CustomerID
                                                                // referenciado en el proceso de registro.
                                                                "CustomerID" to customerID,
                                                                "ultAcceso" to dt.toString(),
                                                            )

                                                            db.collection("datosUsuarios")
                                                                .add(user)
                                                                .addOnSuccessListener { documentReference ->
                                                                    // Registrar datos en el almacenamiento local
                                                                    val prefe = this.getSharedPreferences(
                                                                        "appData",
                                                                        Context.MODE_PRIVATE
                                                                    )
                                                                    prefe.edit {
                                                                        putString("email", email)
                                                                        putString("contra", contra)
                                                                        // Además, en el conjunto de datos del localStorage de la aplicación, ya no solo grabe los
                                                                        // datos de eMail y Contraseña, sino que también los datos del Customer:
                                                                        putString("shipVia", shipVia?.toString() ?: "")
                                                                        putString("shipName", shipName?.toString() ?: "")
                                                                        putString("shipAddress", shipAddress?.toString() ?: "")
                                                                        putString("shipCity", shipCity?.toString() ?: "")
                                                                        putString("shipRegion", shipRegion?.toString() ?: "")
                                                                        putString("shipPostalCode", shipPostalCode?.toString() ?: "")
                                                                        putString("shipCountry", shipCountry?.toString() ?: "")
                                                                    }

                                                                    Toast.makeText(
                                                                        this,
                                                                        "Usuario registrado correctamente",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()

                                                                    setResult(Activity.RESULT_OK)
                                                                    finish()
                                                                }
                                                                .addOnFailureListener { e ->
                                                                    handleError("Error al registrar usuario: ${e.message}")
                                                                }
                                                        } else {
                                                            handleError("Error al registrar usuario: ${authTask.exception}")
                                                        }
                                                    }
                                            }
                                        }
                                    }
                            }
                            .addOnFailureListener { e ->
                                handleError("Error al modificar el documento: ${e.message}")
                            }
                    } else {
                        // El Customer no existe
                        Toast.makeText(
                            this,
                            "El Customer con ID $customerID no existe",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    handleError("Error al realizar la consulta: ${task.exception?.message}")
                }
            }
    }

    private fun handleError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }


}
