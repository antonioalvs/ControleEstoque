package com.example.controleestoque;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;


public class Estoque extends RealmObject {
    @PrimaryKey
    private long _id;
    private String nomeProduto;
    private double valor;
    private int qtdAtual, qtdMinima;
    //@Required private String status = TaskStatus.Open.name();
    //public RealmList<EstoqueTask> tasks = new RealmList<>();

    //Construtor
    public Estoque(long _id, String nomeProduto, double valor, int qtdAtual, int qtdMinima){
        this._id = _id;
        this.nomeProduto = nomeProduto;
        this.valor = valor;
        this.qtdAtual = qtdAtual;
        this.qtdMinima = qtdMinima;
    }

    public Estoque(){}

    //Getters e setters
    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getQtdAtual() {
        return qtdAtual;
    }

    public void setQtdAtual(int qtdAtual) {
        this.qtdAtual = qtdAtual;
    }

    public int getQtdMinima() {
        return qtdMinima;
    }

    public void setQtdMinima(int qtdMinima) {
        this.qtdMinima = qtdMinima;
    }

    //Funções

    //Função para inserir um produto
    public void insert(Realm realm,long id, String nomeProduto, String valor, int qtdAtual, int qtdMinima){
        realm.executeTransactionAsync(r -> {
            double v = Double.parseDouble(valor);
            Estoque est = r.createObject(Estoque.class, id);
            est.setNomeProduto(nomeProduto);
            est.setValor(v);
            est.setQtdAtual(qtdAtual);
            est.setQtdMinima(qtdMinima);
        });
    }

    //Função para conseguir um ID único
    public static long getUniqueId(Realm realm, Class className) {
        Number number = realm.where(className).max("_id");
        if (number == null) return 1;
        else return (long) number + 1;
    }


    //Função para ler todos os produtos
    public List<String> read(Realm realm){
        String repor = "";
        List<String> result = new ArrayList<>();
        List<Estoque> et = realm.where(Estoque.class).findAll();
        for(int i=0;i<et.size();i++){
            if(et.get(i).getQtdAtual() <= et.get(i).getQtdMinima()){
                repor = "(Repor estoque)";
            }else{
                repor = "";
            }
            result.add("ID: " + et.get(i).get_id() +
                    "\nNome do Produto: " + et.get(i).getNomeProduto() +
                    "\nPreço: R$" + et.get(i).getValor() +
                    "\nQuantidade Atual: " + et.get(i).getQtdAtual() + repor +
                    "\nQuantidade minima: " + et.get(i).getQtdMinima());
        }
        return result;
    }


    //Função para pesquisar por nome
    public List<String> pesquisaNome(Realm realm, String nome){
        String repor = "";
        List<String> result = new ArrayList<>();
        RealmQuery<Estoque> query = realm.where(Estoque.class).like("nomeProduto", nome, Case.INSENSITIVE);
        RealmResults<Estoque> pesquisa = query.findAll();
        for(int i=0;i<pesquisa.size();i++){
            if(pesquisa.get(i).getQtdAtual() <= pesquisa.get(i).getQtdMinima()){
                repor = "(Repor estoque)";
            }else{
                repor = "";
            }
            result.add("ID: " +pesquisa.get(i).get_id() +
                    "\nNome do Produto: " + pesquisa.get(i).getNomeProduto() +
                    "\nPreço: R$" + pesquisa.get(i).getValor() +
                    "\nQuantidade Atual: " + pesquisa.get(i).getQtdAtual() + repor +
                    "\nQuantidade minima: " + pesquisa.get(i).getQtdMinima());
        }
        return result;
    }

    //Funçao para pesquisar por ID
    public Estoque pesquisaID(Realm realm, int id){
        //String repor = "";
        List<String> result = new ArrayList<>();
        RealmQuery<Estoque> query = realm.where(Estoque.class).equalTo("_id", id);
        RealmResults<Estoque> pesquisa = query.findAll();
        Estoque temp = new Estoque();
        temp.set_id(pesquisa.get(0).get_id());
        temp.setNomeProduto(pesquisa.get(0).getNomeProduto());
        temp.setValor(pesquisa.get(0).getValor());
        temp.setQtdAtual(pesquisa.get(0).getQtdAtual());
        temp.setQtdMinima(pesquisa.get(0).getQtdMinima());


        return temp;
    }

    public void editar(Realm realm, int id,String nome, double valor, int qtda, int qtdm){
        realm.beginTransaction();
        Estoque produto = realm.where(Estoque.class).equalTo("_id", id).findFirst();
        produto.setNomeProduto(nome);
        produto.setValor(valor);
        produto.setQtdAtual(qtda);
        produto.setQtdMinima(qtdm);
        realm.commitTransaction();
        realm.close();
    }

    public void deletar(Realm realm, String item){
        String idProduto = item.split(":|:\\s|\n")[1].trim();
        ///String idOk = idProduto[1].trim();
        int id = Integer.parseInt(idProduto);

        realm.beginTransaction();
        Estoque produto= realm.where(Estoque.class).equalTo("_id", id).findFirst();
        produto.deleteFromRealm();
        realm.commitTransaction();
        realm.close();
    }


}
