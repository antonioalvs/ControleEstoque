package com.example.controleestoque;

import static com.example.controleestoque.Estoque.getUniqueId;

import io.realm.Realm;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Inserir extends AppCompatActivity {


    //Declaração de variáveis dos botões
    Button save;
    EditText nome, valor, qtdAtual, qtdMin;
    TextView res, idView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Realm.init(this); // Iniciando o Realm (BD)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserir);
        Realm realm = Realm.getDefaultInstance();

        //Definindo campos e botões
        save = findViewById(R.id.btnSalvar);
        idView = findViewById(R.id.lblCodigo);
        //Variáveis dos campos
        nome = findViewById(R.id.txtNome);
        valor = findViewById(R.id.txtValor);
        qtdAtual = findViewById(R.id.txtQtdAtual);
        qtdMin = findViewById(R.id.txtQtdMin);
        //Criando PopUps
        AlertDialog.Builder insertOK = new AlertDialog.Builder(Inserir.this); // Popup sucesso
        AlertDialog.Builder alert = new AlertDialog.Builder(Inserir.this); // Popup erro

        //Caracterizando Popups
        //Popup sucesso
        alert.setTitle("Erro!");
        alert.setMessage("Verifique se todos os campos foram preenchidos e tente novamente!");
        alert.setCancelable(false);
        alert.setPositiveButton("Ok", null);
        //Popup de erro
        insertOK.setTitle("Produto Inserido");
        insertOK.setMessage("O produto foi inserido com sucesso");
        insertOK.setCancelable(false);
        insertOK.setPositiveButton("Ok", null);

        Estoque estoque = new Estoque(); //Instanciando classe do banco de Dados
        //Definindo texto do campo de código
        idView.setText(String.format("Codigo do produto: "+ String.valueOf(getUniqueId(realm, Estoque.class))));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Confere se os campos estão vazios
                if (camposOk()){

                    //Variaveis comuns
                    String n; //nome
                    String v; //valor
                    int qta, qtm; // qtd atual e qtd minima

                    //Passando valor dos campos para variáveis
                    n = String.valueOf(nome.getText());
                    v = String.valueOf(valor.getText());
                    qta = Integer.parseInt(String.valueOf(qtdAtual.getText()));
                    qtm = Integer.parseInt(String.valueOf(qtdMin.getText()));
                    //Definindo ID do produto
                    long idOk = getUniqueId(realm,Estoque.class);
                    //Inserindo no Realm
                    estoque.insert(realm,idOk,n,v,qta,qtm);
                    idView.setText("Código do produto: " + String.valueOf(idOk+1));//Campo de código do produto
                    insertOK.create().show();

                    //Limpa campos
                    limpaCampos();
                }else{
                    alert.create().show();
                }

            }
        });
    }

    public void limpaCampos() {
        nome.setText("");
        valor.setText("");
        qtdAtual.setText("");
        qtdMin.setText("");

    }

    public boolean camposOk(){

        int cn = nome.length();
        int cv = valor.length();
        int cqm = qtdMin.length();
        int cqa = qtdAtual.length();

        if (cn==0 && cv==0&& cqm==0 && cqa==0) {
            return false;
        }else{
            if (cn==0||cv==0||cqm==0||cqa==0) {
                return false;
            } else {
                return true;
            }
        }

    }

}