package com.example.controleestoque;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;

public class Editar extends AppCompatActivity {
    //Variáveis para os campos
    TextView lblID;
    EditText editNome;
    EditText editValor;
    EditText editQtdAtual;
    EditText editQtdMinima;
    Button btnEditar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Métodos padrão da activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        Estoque et = new Estoque();
        //Instanciando banco de dados
        Realm realm = Realm.getDefaultInstance();
        //Buscado produto selecionado na tela de gerenciar produto
        Intent intent = getIntent();
        String produtoPesquisado = intent.getStringExtra("idProduto");
        long id = Long.parseLong(produtoPesquisado);
        Estoque produto = new Estoque();
        produto = et.pesquisaID(realm, id);

        //Atribuindo os campos às variáveis
        lblID = findViewById(R.id.lblID);
        editNome = findViewById(R.id.txtEditNome);
        editValor = findViewById(R.id.txtEditValor);
        editQtdAtual = findViewById(R.id.txtEditQtdA);
        editQtdMinima = findViewById(R.id.txtEditQtdM);
        btnEditar = findViewById(R.id.btnEditar);

        //Definindo conteúdo dos campos
        lblID.setText("Codigo do produto: "+String.valueOf(produto.get_id()));
        editNome.setText(produto.getNomeProduto());
        editValor.setText(converteCampo(produto.getValor()));
        editQtdAtual.setText(String.valueOf(produto.getQtdAtual()));
        editQtdMinima.setText(String.valueOf(produto.getQtdMinima()));

        //Caixa de diálogo para demonstrar que o produto foi devidamente editado
        AlertDialog.Builder alert = new AlertDialog.Builder(Editar.this);
        alert.setTitle("Produto Editado!");
        alert.setMessage("O produto foi alterado com sucesso");
        alert.setCancelable(false);

        //Coloca o botão de "Ok" para finalizar a activity
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });

        //Função de clique do botão de editar
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Joga as variáveis na função e faz as edições
                try{
                    //Verifica se os campos estão vazios
                    if(camposOk()){
                        //Verifica se o preço está correto
                        if(valorOk(editValor.getText().toString())){
                            double valorConvertido = converteValor(editValor.getText().toString());
                            et.editar(realm,
                                    id,
                                    editNome.getText().toString(),
                                    valorConvertido,
                                    Integer.parseInt(editQtdAtual.getText().toString()),
                                    Integer.parseInt(editQtdMinima.getText().toString()));
                            //Mostra caixa de confirmação
                            alert.create().show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Erro! Insira um valor válido",
                                    Toast.LENGTH_LONG).show();
                        }
                    }else{
                        alert.setTitle("Erro!");
                        alert.setMessage("Verifique se todos os campos foram preenchidos e tente novamente!");
                        alert.setCancelable(false);
                        alert.setPositiveButton("Ok", null);
                        alert.create().show();
                    }

                }catch(Exception e){
                    alert.setTitle("Erro!");
                    alert.setMessage("Erro ao editar!\n" + e.toString());
                    alert.setCancelable(false);
                    alert.setPositiveButton("Ok", null);
                    alert.create().show();
                }
            }
        });
    }

    //Verifica se o preço tem mais de duas casas após o ponto
    public boolean valorOk(String v){
        if(editValor.getText().toString().length() == 0){
            return true;
        }else{
            //String preco = String.valueOf(v);
            String util[] = v.split("[,\\.]");
            if(util.length == 2){
                if(util[1].length() > 2){
                    return false;
                }else{
                    return true;
                }
            }else{
                return false;
            }

        }
    }
    //Retorna o Preço corrigido para double substituindo a "," por "."
    public Double converteValor(String v) {
        Double valor;
        String util[] = v.split("[,\\.]");
        if (util.length == 1) {
            valor = Double.valueOf(v);
            return valor;
        } else {
            String temp = util[0] + "." + util[1];
            valor = Double.valueOf(temp);
            return valor;
        }
    }
    //Retorna a string substituindo ponto por virgula
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

    //Verifica se todos os campos estão preenchidos
    public boolean camposOk(){
        int cn = editNome.length();
        int cv = editValor.length();
        int cqm = editQtdMinima.length();

        if (cn==0 && cv==0&& cqm==0) {
            return false;
        }else{
            if (cn==0||cv==0||cqm==0) {
                return false;
            } else {
                return true;
            }
        }

    }
}