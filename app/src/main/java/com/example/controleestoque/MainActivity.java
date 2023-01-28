package com.example.controleestoque;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    Button ins; //Adicionar Item
    Button ger; //Gerenciar
    Button rel; //Relatórios

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Trocar para tela de cadastro
        ins = findViewById(R.id.btnAddItem);
        ins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent insert = new Intent(getApplicationContext(),Inserir.class);
                startActivity(insert);
            }
        });

        rel = findViewById(R.id.btnRelatorio);
        rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent relatorio = new Intent(getApplicationContext(),Relatorio.class);
                startActivity(relatorio);
            }
        });
    }
}