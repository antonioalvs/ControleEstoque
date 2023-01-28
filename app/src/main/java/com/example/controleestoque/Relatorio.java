package com.example.controleestoque;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.realm.Realm;

public class Relatorio extends AppCompatActivity {


    Estoque et = new Estoque();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);
        Button list;
        TextView txtConsulta;
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        list = findViewById(R.id.btnLista);
        txtConsulta = findViewById(R.id.lblConsulta);

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    txtConsulta.setText(et.ler(realm));

            }
        });



    }


}