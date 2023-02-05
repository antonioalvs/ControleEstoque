package com.example.controleestoque;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {



    //Variáveis dos campos
    ImageButton ins; //Adicionar Item
    ImageButton es; //Entrada e saida

    ImageButton ger; //Gerenciar Estoque

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Abre a activity de Gerenciar Estoque
        ger = findViewById(R.id.btnGerenciar);
        ger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gerenciar = new Intent(getApplicationContext(), Gerenciar.class);
                startActivity(gerenciar);
            }
        });

        //Abre a acivity de adicionar produto
        ins = findViewById(R.id.btnAddItem);
        ins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent insert = new Intent(getApplicationContext(),Inserir.class);
                startActivity(insert);
            }
        });

        //Abre a acivity de entrada e saída
        es = findViewById(R.id.btnES);
        es.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent entradaSaida = new Intent(getApplicationContext(), EntradaSaida.class);
                startActivity(entradaSaida);
            }
        });

    }
}