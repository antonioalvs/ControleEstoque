package com.example.controleestoque;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.controleestoque.databinding.ActivityGerenciarBinding;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;

//Activity para gerenciar o estoque
public class Gerenciar extends AppCompatActivity implements SelectListener{
    //Instanciando variáveis para os campos
    Button pesquisar;
    EditText txtPesquisa;
    ActivityGerenciarBinding binding;

    //Instanciando variável para o banco de dados
    Estoque et = new Estoque();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar);
        //Instanciando Banco de Dados
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        //Atribuindo os campos às variáveis
        binding = ActivityGerenciarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initRecyclerView();
        pesquisar = findViewById(R.id.btnPesquisa);
        txtPesquisa = findViewById(R.id.txtPesquisa);
        pesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    int util = txtPesquisa.getText().toString().length();
                    if (util == 0){
                        ArrayAdapter<String> adapt = new ArrayAdapter<>(Gerenciar.this,
                                android.R.layout.simple_list_item_1,
                                et.read(realm));
                        initRecyclerView();
                    }else{
                        List<Estoque> search;
                        search = et.pesquisaNome(realm, txtPesquisa.getText().toString());
                        Adapter srch = new Adapter(search, Gerenciar.this);
                        searchRecyclerView(srch);
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Erro na pesquisa"
                            ,Toast.LENGTH_LONG).show();
                }


            }
        });


    }
    //Função onResume que atualiza a tela quando volta da edição
    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
    }

    //Função que inicia e atualiza a lista de produtos
    private void initRecyclerView(){
        Realm.init(this);
        Realm rlm = Realm.getDefaultInstance();
        List<Estoque> listOk = et.listaDeProdutos(rlm);
        Adapter adp = new Adapter(listOk,this);
        binding.rvConsulta.setLayoutManager(new LinearLayoutManager(this));
        binding.rvConsulta.setHasFixedSize(true);
        binding.rvConsulta.setAdapter(adp);
    }

    //Função que mostra a lista de resultados das pesquisas
    private void searchRecyclerView(Adapter adp){
        Realm.init(this);
        Realm rlm = Realm.getDefaultInstance();
        List<Estoque> listOk = et.listaDeProdutos(rlm);
        //Adapter adp = new Adapter(listOk,this);
        binding.rvConsulta.setLayoutManager(new LinearLayoutManager(this));
        binding.rvConsulta.setHasFixedSize(true);
        binding.rvConsulta.setAdapter(adp);
    }

    //Função que determina o que acontece quando é clicado em um produto
    @Override
    public void onItemClicked(Estoque estoque) {
        //Cria uma caixa de diálogo para escolher entre editar o produto e excluir o produto
        AlertDialog.Builder builder = new AlertDialog.Builder(Gerenciar.this);
        builder.setTitle("Selecione uma opção");
        builder.setItems(new CharSequence[]
                        {"Editar", "Deletar"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            //Função de editar o produto
                            case 0:
                                //Abre a activity de editar produto e envia o ID para pesquisa
                                Intent intent = new Intent();
                                intent.setClass(Gerenciar.this, Editar.class);
                                intent.putExtra("idProduto",String.valueOf(estoque.get_id()));
                                startActivity(intent);
                                break;
                            //Função de excluir o produto
                            case 1:
                                //Caixa de diálogo para confirmação
                                AlertDialog.Builder deletar = new AlertDialog.Builder(Gerenciar.this);
                                deletar.setTitle("Deletar");
                                deletar.setMessage("Você deseja realmente deletar este item?" +
                                        "\nEsta ação não pode ser revertida");
                                deletar.setCancelable(false);
                                //Função para caso seja confirmado a exclusão
                                deletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Realm realm = Realm.getDefaultInstance();
                                        et.deletar(realm, estoque.get_id());
                                        initRecyclerView();
                                        Toast.makeText(getApplicationContext(),"Item deletado"
                                                ,Toast.LENGTH_LONG).show();
                                    }
                                });
                                //Função para caso a exclusão seja negada
                                deletar.setNegativeButton("Não", null);
                                deletar.create().show();
                                break;
                        }
                    }
                });
        //Mostra a caixa de diálogo para escolha
        builder.create().show();
    }
}