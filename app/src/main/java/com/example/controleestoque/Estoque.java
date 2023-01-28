package com.example.controleestoque;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
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

    //Metodos

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



    public static long getUniqueId(Realm realm, Class className) {
        Number number = realm.where(className).max("_id");
        if (number == null) return 1;
        else return (long) number + 1;
    }

    public String ler(Realm realm){
        String result = null;
        List<Estoque> et = realm.where(Estoque.class).findAll();
        for(int i=0;i<et.size();i++){

            result = result + "ID: " + et.get(i).get_id() +
                    "\nNome do Produto: " + et.get(i).getNomeProduto() +
                    "\nPreço: R$" + et.get(i).getValor() +
                    "\nQuantidade Atual: " + et.get(i).getQtdAtual() +
                    "\nQuantidade minima: " + et.get(i).getQtdMinima() +
                    "\n ----------------- \n";
        }
        return result;
    }


}
