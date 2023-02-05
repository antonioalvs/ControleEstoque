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

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class Gerenciar extends AppCompatActivity implements SelectListener{




    Estoque et = new Estoque();
    Button pesquisar;
    EditText txtPesquisa;

    ActivityGerenciarBinding binding;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar);

        binding = ActivityGerenciarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initRecyclerView();


        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        pesquisar = findViewById(R.id.btnPesquisa);
        txtPesquisa = findViewById(R.id.txtPesquisa);


        pesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

            }
        });


    }
    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
    }

    private void initRecyclerView(){
        Realm.init(this);
        Realm rlm = Realm.getDefaultInstance();
        List<Estoque> listOk = et.testeRecycler(rlm);
        Adapter adp = new Adapter(listOk,this);
        binding.rvConsulta.setLayoutManager(new LinearLayoutManager(this));
        binding.rvConsulta.setHasFixedSize(true);
        binding.rvConsulta.setAdapter(adp);
    }

    private void searchRecyclerView(Adapter adp){
        Realm.init(this);
        Realm rlm = Realm.getDefaultInstance();
        List<Estoque> listOk = et.testeRecycler(rlm);
        //Adapter adp = new Adapter(listOk,this);
        binding.rvConsulta.setLayoutManager(new LinearLayoutManager(this));
        binding.rvConsulta.setHasFixedSize(true);
        binding.rvConsulta.setAdapter(adp);
    }

    @Override
    public void onItemClicked(Estoque estoque) {
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
                                // Then you start a new Activity via Intent
                                Intent intent = new Intent();
                                intent.setClass(Gerenciar.this, Editar.class);
                                intent.putExtra("idProduto",String.valueOf(estoque.get_id()));
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
                                        et.deletar(realm, estoque.get_id());
                                        initRecyclerView();
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
    }
}