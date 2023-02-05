package com.example.controleestoque;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;

public class EntradaSaida extends AppCompatActivity {
    Button pesquisa, entrada, saida;
    EditText id, qtd;
    TextView nome, qtdA,qtdM, valor;
    Estoque et = new Estoque();

    long idES;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada_saida);
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        pesquisa = findViewById(R.id.btnESPesquisar);
        entrada = findViewById(R.id.btnEntrada);
        saida = findViewById(R.id.btnSaida);
        id = findViewById(R.id.txtESID);
        qtd = findViewById(R.id.txtESQtd);
        nome = findViewById(R.id.lblESProduto);
        qtdA = findViewById(R.id.lblESQtdAtual);
        valor = findViewById(R.id.lblESValor);
        qtdM = findViewById(R.id.lblESQtdm);

        id.requestFocus();
        pesquisa.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if(produtoOk(id.length())) {
                    idES = Long.parseLong(id.getText().toString());
                    et = et.pesquisaID(realm, idES);
                    nome.setText("Produto: " + et.getNomeProduto());
                    qtdA.setText("Quantidade em estoque: " + String.valueOf(et.getQtdAtual()));
                    qtdM.setText("Quantidade mínima: " + String.valueOf(et.getQtdMinima()));
                    valor.setText("Preço: R$" + String.valueOf(et.getValor()));
                    if(et.getQtdAtual() < et.getQtdMinima()){
                        qtdA.setTextColor(Color.parseColor("#FF0000"));
                    }
                }
            }
        });

        entrada.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if(produtoOk(et.get_id())){
                    if(qtdOk()){
                        int qt = Integer.parseInt(String.valueOf(qtd.getText()));
                        et.entradaSaida(realm,
                                et.get_id(),
                                qt,
                                2);
                        et = et.pesquisaID(realm, idES);
                        Toast.makeText(getApplicationContext(),"Movimentação concluída!"
                                ,Toast.LENGTH_LONG).show();
                        if(et.getQtdAtual() > et.getQtdMinima()){
                            qtdA.setTextColor(Color.parseColor("#000000"));
                        }
                        qtdA.setText("Quantidade em estoque: " + String.valueOf(et.getQtdAtual()));
                    }
                }
            }
        });

        saida.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if(produtoOk(String.valueOf(et.get_id()).length())){
                    if(qtdOk()){
                        int qt = Integer.parseInt(String.valueOf(qtd.getText()));
                        et.entradaSaida(realm,
                                et.get_id(),
                                qt,
                                1);
                        et = et.pesquisaID(realm, idES);
                        estoqueOk(et.getQtdAtual(), et.getQtdMinima());
                        Toast.makeText(getApplicationContext(),"Movimentação concluída!"
                                ,Toast.LENGTH_LONG).show();
                        qtdA.setText("Quantidade em estoque: " + String.valueOf(et.getQtdAtual()));
                    }
                }
            }
        });


    }

    public boolean produtoOk(long checkId){
        if(checkId == 0){
            AlertDialog.Builder alert = new AlertDialog.Builder(EntradaSaida.this);
            alert.setTitle("Atenção!");
            alert.setMessage("Selecione um produto válido e tente novamente");
            alert.setCancelable(false);
            alert.setPositiveButton("ok", null);
            alert.create().show();
            return false;
        }else{
            return true;
        }
    }

    public boolean qtdOk(){

        AlertDialog.Builder alert = new AlertDialog.Builder(EntradaSaida.this);
        alert.setTitle("Atenção!");
        alert.setCancelable(false);
        alert.setPositiveButton("ok", null);

        if(qtd.length() == 0){
            alert.setMessage("Insira uma quantidade válida e tente novamente!");
            alert.create().show();
            return false;
        }else{
            return true;
        }
    }
    public void estoqueOk(int qta, int qtm){

        if(qta <= qtm){
            AlertDialog.Builder alert = new AlertDialog.Builder(EntradaSaida.this);
            alert.setTitle("Atenção!");
            alert.setCancelable(false);
            alert.setPositiveButton("ok", null);
            alert.setMessage("Repor o estoque assim que possível!");
            alert.create().show();
            qtdA.setTextColor(Color.parseColor("#FF0000"));
        }else{
            qtdA.setTextColor(Color.parseColor("#000000"));
        }

    }


}