package com.example.mivecindarionuevo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class contacto extends AppCompatActivity {

    EditText et_asunto,et_mensaje;
    Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_contacto);
        et_asunto= findViewById(R.id.et_asunto);
        et_mensaje= findViewById(R.id.et_mensajeContacto);
        btnEnviar= findViewById(R.id.btnEnviarMesaje);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarMail();
            }
        });
    }

    private void enviarMail() {
        String emailEmpresa = "diego.navarro14@outlook.cl";
        String[] recipiente = emailEmpresa.split(",");
        String asunto = et_asunto.getText().toString();
        String mensaje = et_mensaje.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,recipiente);
        intent.putExtra(Intent.EXTRA_SUBJECT,asunto);
        intent.putExtra(Intent.EXTRA_TEXT,mensaje);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Escoja un cliente de email"));
    }


}
