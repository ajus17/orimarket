package edu.uoc.orimarkettfm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.uoc.orimarkettfm.clases.user;

public class UserRegister extends AppCompatActivity {

    //Atributos de la clase o activity

    public EditText user, pass, pnombre, papellido, ntelf;
    public Button btnCrearCuenta;
    public ProgressDialog DialogoProgreso;
    public Spinner tipouser;
    public FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    public user usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        //Establecer enlace entre los atributos de la clase y los elementos de la interfaz gráfica

        user = findViewById(R.id.txemail);
        pass = findViewById(R.id.txpass);
        btnCrearCuenta = findViewById(R.id.btnCrear);
        tipouser = findViewById(R.id.tipoUsuario);
        pnombre = findViewById(R.id.txpnombre);
        papellido = findViewById(R.id.txpapellido);
        ntelf = findViewById(R.id.txtelf);

        //Arreglo con los tipos de usuarios existentes en la aplicación

        String [] ArrayTipos = {"Cliente", "Negocio"};

        //Mostrar los usuarios del arreglo en un combo o spinner

        tipouser.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ArrayTipos));

        //Instancia de la autenticación con firebase

        firebaseAuth = FirebaseAuth.getInstance();

        //Inicialización de la barra de progreso

        DialogoProgreso = new ProgressDialog(this);

        //Instancia de la base de datos en tiempo real de firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Evento clic del botón crear cuenta de usuario

        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });

    }


    //Método que permite registrar un nuevo usuario en firebase
    private void Register(){

        //Capturando los datos ingresados por medio de la interfaz
        final String email = user.getText().toString();
        final String password = pass.getText().toString();
        final String pnomb  = pnombre.getText().toString();
        final String pape = papellido.getText().toString();
        final String nt = ntelf.getText().toString();
        final String t = tipouser.getSelectedItem().toString();

        //creando una nueva instancia de la clase usuario
        usuario = new user(pnomb,pape,email, password, nt, t);

        //Comprobando los campos del formulario no se encuentren vacios
        if (email.equals("")){
            user.setError("Debes escribir un correo");

            return;
        }

        if (password.equals("")){
            pass.setError("Debes escribir una contraseña");

            return;
        }
        if (pnomb.equals("")){
            pnombre.setError("Debes escribir un nombre");

            return;
        }
        if (pape.equals("")){
            papellido.setError("Debes escribir un apellido");

            return;
        }

        if (nt.equals("")){
            ntelf.setError("Debes escribir un número de teléfono");

            return;
        }

        //Mostramos un progress dialog mientras se crea el usuario

        DialogoProgreso.setMessage("Creando cuenta, espere un poco.");
        DialogoProgreso.show();

        //Creamos el nuevo usuario mediante el correo electrónico y la contraseña

        firebaseAuth.createUserWithEmailAndPassword(email, password)

                //Este primer método permite verificar cuando el proceso de registro se lleva a cabo
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //Comprueba que el registro se realizó correctamente
                        if (task.isSuccessful()){

                            //mensaje indicando que el usuario se creó correctamente
                         Toast.makeText(getApplicationContext(), "Usuario creado exitosamente.", Toast.LENGTH_LONG).show();

                            //registroDBUser(email,password, pnomb, pape, nt, t);

                            //Obtenemos el UID del usuario creado en firebase. Este ID servirá como ID para el registro de usuarios en
                            //Realtime Database
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            String userID = mAuth.getCurrentUser().getUid();
                            mDatabase.child("user").child(userID).setValue(usuario);

                            //Al guardarse el dato, pasamos a la interfaz de login o inicio de sesión
                            Intent intento = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intento);
                        }else{

                            //con esta condición establecemos que no se pueda crear una nueva cuenta de usuario con el mismo
                            //correo electrónico
                            if (task.getException() instanceof FirebaseAuthUserCollisionException){

                                //Mensaje de error al crear cuenta de usuario con email existente
                                Toast.makeText(getApplicationContext(), "Usuario ya existe, intenta iniciar sesión", Toast.LENGTH_LONG).show();

                            }else{

                                //Mensaje de error, se mostrará si existe algún error en la creación de la cuenta de usuario
                                Toast.makeText(getApplicationContext(), "No se pudo crear la cuenta.", Toast.LENGTH_LONG).show();
                            }
                        }

                        //cerramos el progress dialog, una vez se haya ejecutado el bloque de instrucciones completo.
                        DialogoProgreso.dismiss();
                    }
                });
    }

}
