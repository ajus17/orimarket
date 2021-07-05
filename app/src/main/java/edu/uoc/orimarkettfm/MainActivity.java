package edu.uoc.orimarkettfm;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;


public class MainActivity extends AppCompatActivity {

    public EditText cajaUser, cajapwd;
    public Button btnConsultar, btnReg;
    public ProgressDialog dialogo;
    public static String KEY = "SESSION";
    public FirebaseAuth fbauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cajaUser = findViewById(R.id.et_user);
        cajapwd = findViewById(R.id.et_pass);
        btnConsultar = findViewById(R.id.btnLogin);
        btnReg = findViewById(R.id.btReg);

        fbauth = FirebaseAuth.getInstance();

        dialogo = new ProgressDialog(this);

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UserRegister.class);
                startActivity(i);
            }
        });

    }


    private void login(){
        final String email = cajaUser.getText().toString();
        final String password = cajapwd.getText().toString();

        if (email.equals("")){
            cajaUser.setError("Debes escribir un correo");

            return;
        }

        if (password.equals("")){
            cajapwd.setError("Debes escribir una contraseña");

            return;
        }
        dialogo.setMessage("Realizando consulta.");
        dialogo.show();

        fbauth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Bienvenido: "+email, Toast.LENGTH_LONG).show();
                            guardaSesion(email, password);
                            Intent intent = new Intent(getApplicationContext(), inicio1.class);
                            intent.putExtra(inicio1.usuario, email);
                            startActivity(intent);
                        }else{

                            Toast.makeText(getApplicationContext(), "Error:::.. Datos incorrectos.", Toast.LENGTH_LONG).show();

                        }
                        dialogo.dismiss();
                    }
                });
    }

    private void guardaSesion(String c, String p){

            SharedPreferences.Editor editor = getApplicationContext()
                    .getSharedPreferences(KEY, Activity.MODE_PRIVATE).edit();

            editor.putString("correo", c);
            editor.putString("contrasen", p);
            editor.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        String nombreuser, contrasenauser;
        SharedPreferences prefs =
                getSharedPreferences("SESSION",
                        Context.MODE_PRIVATE);
        nombreuser = prefs.getString("correo",
                "");

        if(nombreuser!=""){
            Toast.makeText(getApplicationContext(), "Bienvenido: "+nombreuser, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), inicio1.class);
            intent.putExtra(inicio1.usuario, nombreuser);
            startActivity(intent);
        }

    }
}