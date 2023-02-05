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

    //Variáveis para os campos
    Button pesquisa, entrada, saida;
    EditText id, qtd;
    TextView nome, qtdA,qtdM, valor;
    //Instanciando classe do Estoque
    Estoque et = new Estoque();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada_saida);
        //Instancia o banco de dados
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        //Atribui os campos às variáveis
        pesquisa = findViewById(R.id.btnESPesquisar);
        entrada = findViewById(R.id.btnEntrada);
        saida = findViewById(R.id.btnSaida);
        id = findViewById(R.id.txtESID);
        qtd = findViewById(R.id.txtESQtd);
        nome = findViewById(R.id.lblESProduto);
        qtdA = findViewById(R.id.lblESQtdAtual);
        valor = findViewById(R.id.lblESValor);
        qtdM = findViewById(R.id.lblESQtdm);
        //Coloca o cursor no campo de ID
        id.requestFocus();

        AlertDialog.Builder alert = new AlertDialog.Builder(EntradaSaida.this);
        alert.setTitle("Erro!");
        alert.setCancelable(false);
        alert.setPositiveButton("Ok", null);

        //Botão de pesquisa busca o produto e preenche os campos
        pesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //Verifica se o campo de código está vazio
                    if (produtoOk(id.length())) {

                        //Verifica se o produto pesquisado já foi adicionado
                        if (et.produtoExiste(realm, Integer.parseInt(id.getText().toString()))) {
                            //Verifica se o produto já está sendo mostrado
                            if (et.get_id() == Long.parseLong(id.getText().toString())) {
                                Toast.makeText(getApplicationContext(), "Produto já pesquisado!"
                                        , Toast.LENGTH_SHORT).show();
                            } else {
                                //Preenche os campos
                                et = et.pesquisaID(realm, Long.parseLong(id.getText().toString()));
                                nome.setText("Produto Pesquisado: " + et.getNomeProduto());
                                qtdA.setText("Quantidade em estoque: " + String.valueOf(et.getQtdAtual()));
                                qtdM.setText("Quantidade mínima: " + String.valueOf(et.getQtdMinima()));
                                valor.setText("Preço: R$" + converteCampo(et.getValor()));
                                estoqueOk(et.getQtdAtual(), et.getQtdMinima());
                            }
                        } else {
                            //Mensagem de erro
                            Toast.makeText(getApplicationContext(), "Insira um código válido!"
                                    , Toast.LENGTH_SHORT).show();
                        }


                    }
                }catch (Exception e){
                //Mensagem de erro
                alert.setMessage("Erro na pesquisa!\n" + e.toString());
                alert.create().show();
            }
            }
        });

        //Faz a movimentação de entrada de um produto e mostra uma mensagem
        entrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    //Verifica se o produto foi pesquisado
                    if(produtoOk(et.get_id())) {
                        //Verifica se o campo de quantidade para movimentação está vazio
                        if (qtdOk()) {
                            int qt = Integer.parseInt(String.valueOf(qtd.getText()));

                            //Executa entrada de produtos
                            et.entradaSaida(realm,
                                    et.get_id(),
                                    qt,
                                    2);
                            et = et.pesquisaID(realm, et.get_id());
                            Toast.makeText(getApplicationContext(), "Movimentação concluída!"
                                    , Toast.LENGTH_LONG).show();
                            estoqueOk(et.getQtdAtual(), et.getQtdMinima());
                            qtdA.setText("Quantidade em estoque: " + String.valueOf(et.getQtdAtual()));
                        }
                    }
                }catch (Exception e){
                    //Mensagem de erro
                    alert.setMessage("Erro na movimentação!\n" + e.toString());
                    alert.create().show();
                }
            }
        });

        //Faz a movimentação de saída de um produto e mostra uma mensagem
        saida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Verifica se o produto foi pesquisado
                if(produtoOk(et.get_id())){
                    //Verifica se o campo de quantidade para movimentação está vazio
                    if(qtdOk()){
                        int qt = Integer.parseInt(String.valueOf(qtd.getText()));
                        try{
                            //Executa a saida de produtos
                            et.entradaSaida(realm,
                                    et.get_id(),
                                    qt,
                                    1);
                            et = et.pesquisaID(realm, et.get_id());
                            Toast.makeText(getApplicationContext(),"Movimentação concluída!"
                                    ,Toast.LENGTH_LONG).show();
                            estoqueOk(et.getQtdAtual(), et.getQtdMinima());
                            qtdA.setText("Quantidade em estoque: " + String.valueOf(et.getQtdAtual()));
                        }catch (Exception e){
                            //Mensagem de erro
                            alert.setMessage("Erro na movimentação!\n" + e.toString());
                            alert.create().show();
                        }
                    }
                }
            }
        });
    }
    //Verifica se um produto foi pesquisado e se o campo de ID está vazio
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
    //Função se o estoque precisa de reposição e faz a cor do campo de quantidade atual ser alterada
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
    //Verifica se o campo de quantidade está com um número
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

    //Converte o valor para o padrão R$0,00
    public String converteCampo(double v){
        String preco = String.valueOf(v);
        String precoUtil[] = preco.split("\\.");
        String valorFinal;
        if(precoUtil[1] == "0"){
            valorFinal = precoUtil[0] + ",00";
        } else if (precoUtil[1].length() == 1) {
            valorFinal = precoUtil[0] + "," + precoUtil[1]+ "0";
        }else{
            valorFinal = precoUtil[0] + "," + precoUtil[1];
        }
        return valorFinal;
    }
}