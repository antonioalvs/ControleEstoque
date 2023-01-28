package com.example.controleestoque;

import static com.example.controleestoque.Estoque.getUniqueId;

import io.realm.Realm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
public class Inserir extends AppCompatActivity {


    Button save;
    EditText nome, valor, qtdAtual, qtdMin;
    TextView res, idView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Realm.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserir);
        Realm realm = Realm.getDefaultInstance();

        //Definindo campos e botões
        save = findViewById(R.id.btnSalvar);
        res = findViewById(R.id.lblResult);
        idView = findViewById(R.id.lblCodigo);

        Estoque estoque = new Estoque();
        long idProduto = getUniqueId(realm, Estoque.class);

        idView.setText(String.valueOf(idProduto));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                //Variáveis dos campos
                nome = findViewById(R.id.txtNome);
                valor = findViewById(R.id.txtValor);
                qtdAtual = findViewById(R.id.txtQtdAtual);
                qtdMin = findViewById(R.id.txtQtdMin);
                //Variaveis comuns
                String n; //nome
                String v; //valor
                int qta, qtm; // qtd atual e qtd minima

                //Passando valor dos campos para variáveis
                n = String.valueOf(nome.getText());
                v = String.valueOf(valor.getText());
                qta = Integer.parseInt(String.valueOf(qtdAtual.getText()));
                qtm = Integer.parseInt(String.valueOf(qtdMin.getText()));

                //Inserindo no Realm
                estoque.insert(realm,idProduto,n,v,qta,qtm);
                idView.setText(String.valueOf(idProduto));
                //Limpa campos
                limpaTela();
            }

            public void limpaTela() {
                nome.setText("");
                valor.setText("");
                qtdAtual.setText("");
                qtdMin.setText("");

            }

        });



    }

}