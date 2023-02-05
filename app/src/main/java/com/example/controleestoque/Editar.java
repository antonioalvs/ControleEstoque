package com.example.controleestoque;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class Editar extends AppCompatActivity {


    TextView lblID;
    EditText editNome;
    EditText editValor;
    EditText editQtdAtual;
    EditText editQtdMinima;
    Button btnEditar;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        Estoque et = new Estoque();
        Realm realm = Realm.getDefaultInstance();

        //Instanciando produto selecionado
        Intent intent = getIntent();
        String produtoPesquisado = intent.getStringExtra("idProduto");
        long id = Long.parseLong(produtoPesquisado);
        Estoque produto = new Estoque();
        produto = et.pesquisaID(realm, id);

        lblID = findViewById(R.id.lblID);
        editNome = findViewById(R.id.txtEditNome);
        editValor = findViewById(R.id.txtEditValor);
        editQtdAtual = findViewById(R.id.txtEditQtdA);
        editQtdMinima = findViewById(R.id.txtEditQtdM);
        btnEditar = findViewById(R.id.btnEditar);

        lblID.setText("Codigo do produto: "+String.valueOf(produto.get_id()));
        editNome.setText(produto.getNomeProduto());
        editValor.setText(String.valueOf(produto.getValor()));
        editQtdAtual.setText(String.valueOf(produto.getQtdAtual()));
        editQtdMinima.setText(String.valueOf(produto.getQtdMinima()));

        AlertDialog.Builder alert = new AlertDialog.Builder(Editar.this);
        alert.setTitle("Produto Editado!");
        alert.setMessage("O produto foi alterado com sucesso");
        alert.setCancelable(false);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                /*
                Intent intent = new Intent(Editar.this, Gerenciar.class);
                startActivity(intent);
                 */
                finish();
            }
        });

        //Estoque finalProduto = produto;
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et.editar(realm,
                        id,
                        editNome.getText().toString(),
                        Double.parseDouble(editValor.getText().toString()),
                        Integer.parseInt(editQtdAtual.getText().toString()),
                        Integer.parseInt(editQtdMinima.getText().toString()));
                alert.create().show();
            }
        });



    }
}