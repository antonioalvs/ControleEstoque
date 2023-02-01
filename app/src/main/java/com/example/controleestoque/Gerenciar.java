package com.example.controleestoque;

import android.annotation.SuppressLint;
import android.content.ContextParams;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class Gerenciar extends AppCompatActivity implements AdapterView.OnItemClickListener {




    Estoque et = new Estoque();
    Button pesquisar;
    EditText txtPesquisa;
    ListView txtConsulta;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar);

        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        pesquisar = findViewById(R.id.btnPesquisa);
        txtConsulta = findViewById(R.id.listView);
        txtPesquisa = findViewById(R.id.txtPesquisa);
        List<String> produto;
        produto = et.read(realm);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                et.read(realm));
        txtConsulta.setAdapter(adapter);
        txtConsulta.setOnItemClickListener(this);

        pesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int util = txtPesquisa.getText().toString().length();
                if (util == 0){
                    ArrayAdapter<String> adapt = new ArrayAdapter<>(Gerenciar.this,
                            android.R.layout.simple_list_item_1,
                            et.read(realm));
                    txtConsulta.setAdapter(adapt);
                }else{
                    List<String> search;
                    search = et.pesquisaNome(realm, txtPesquisa.getText().toString());
                    ArrayAdapter<String> adapterPesquisa = new ArrayAdapter<>(Gerenciar.this,
                            android.R.layout.simple_list_item_1,
                            search);
                    txtConsulta.setAdapter(adapterPesquisa);
                }

            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Gerenciar.this);
        builder.setTitle("Selecione uma opção");
        builder.setItems(new CharSequence[]
                        {"Editar", "Deletar"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                Log.i("HelloListView", "You clicked Item: " + id +
                                        " at position:" + position);
                                // Then you start a new Activity via Intent
                                Intent intent = new Intent();
                                intent.setClass(Gerenciar.this, Editar.class);
                                intent.putExtra("produto",
                                        String.valueOf(adapterView.getItemAtPosition(position)));
                                startActivity(intent);
                                break;
                            case 1:
                                AlertDialog.Builder deletar = new AlertDialog.Builder(Gerenciar.this);
                                deletar.setTitle("Deletar");
                                deletar.setMessage("Você deseja realmente deletar este item?" +
                                        "\nEsta ação não pode ser revertida");
                                deletar.setCancelable(false);
                                deletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Realm realm = Realm.getDefaultInstance();
                                        String item = adapterView.getItemAtPosition(position).toString();
                                        et.deletar(realm, item);
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                                Gerenciar.this,
                                                android.R.layout.simple_list_item_1,
                                                et.read(realm));
                                        txtConsulta.setAdapter(adapter);
                                        Toast.makeText(getApplicationContext(),"Item deletado"
                                                ,Toast.LENGTH_LONG).show();
                                    }
                                });
                                deletar.setNegativeButton("Não", null);
                                deletar.create().show();

                                break;
                        }
                    }
                });
        builder.create().show();
        /*
        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
        // Then you start a new Activity via Intent
        Intent intent = new Intent();
        intent.setClass(this, Editar.class);
        intent.putExtra("produto", String.valueOf(adapterView.getItemAtPosition(position)));
        startActivity(intent);

         */


    }
}