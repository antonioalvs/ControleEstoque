package com.example.controleestoque;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

//Classe de Banco de Dados que utiliza-se de Realm, da MongoDB
public class Estoque extends RealmObject {

    //Campos da tabela
    @PrimaryKey
    private long _id;
    private String nomeProduto;
    private double valor;
    private int qtdAtual, qtdMinima;

    //Construtor com parâmetros
    public Estoque(long _id, String nomeProduto, double valor, int qtdAtual, int qtdMinima){
        this._id = _id;
        this.nomeProduto = nomeProduto;
        this.valor = valor;
        this.qtdAtual = qtdAtual;
        this.qtdMinima = qtdMinima;
    }

    //Construtor vazio
    public Estoque(){}

    //Getters e setters
    public long get_id() {return _id;}
    public void set_id(long _id) {this._id = _id;}
    public String getNomeProduto() {return nomeProduto;}
    public void setNomeProduto(String nomeProduto) {this.nomeProduto = nomeProduto;}
    public double getValor() {return valor;}
    public void setValor(double valor) {this.valor = valor;}
    public int getQtdAtual() {return qtdAtual;}
    public void setQtdAtual(int qtdAtual) {this.qtdAtual = qtdAtual;}
    public int getQtdMinima() {return qtdMinima;}
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

    //Função que deleta o produto no banco de dados
    public void deletar(Realm realm, long id){
        realm.beginTransaction();
        Estoque produto= realm.where(Estoque.class).equalTo("_id", id).findFirst();
        produto.deleteFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    //Função para conseguir um ID único para o produto, pois o banco de dados não possui AUTOINCREMENT por padrão
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

    //Função que retorna uma lista de todos os produtos
    public List<Estoque> listaDeProdutos(Realm realm){
        List<Estoque> result = new ArrayList<>();
        List<Estoque> et = realm.where(Estoque.class).findAll();
        for (int i=0;i<et.size();i++){
            result.add(et.get(i));
        }
        return result;
    }


    //Função que retorna uma lista de produtos pesquisados pelo nome
    public List<Estoque> pesquisaNome(Realm realm, String nome){
        String repor = "";
        List<Estoque> result = new ArrayList<>();
        RealmQuery<Estoque> query = realm.where(Estoque.class).like("nomeProduto", nome, Case.INSENSITIVE);
        RealmResults<Estoque> pesquisa = query.findAll();
        for(int i=0;i<pesquisa.size();i++){
            result.add(pesquisa.get(i));
        }
        return result;
    }

    //Funçao para pesquisar produtos por ID
    public Estoque pesquisaID(Realm realm, long id){
        RealmQuery<Estoque> query = realm.where(Estoque.class).equalTo("_id", id);
        Estoque pesquisa = query.findFirst();
        return pesquisa;
    }

    //Função que edita o produto no banco de dados
    public void editar(Realm realm, long id,String nome, double valor, int qtda, int qtdm){
        realm.beginTransaction();
        Estoque produto = realm.where(Estoque.class).equalTo("_id", id).findFirst();
        produto.setNomeProduto(nome);
        produto.setValor(valor);
        produto.setQtdAtual(qtda);
        produto.setQtdMinima(qtdm);
        realm.commitTransaction();
        realm.close();
    }

    //Função que efetua as operações de entrada e saída no banco de dados
    public void entradaSaida(Realm realm, long id, int qtd, int util){
        //util==1 = saída / util==2 = entrada
        if(util == 1) {
            realm.beginTransaction();
            Estoque saida = new Estoque();
            saida = realm.where(Estoque.class).equalTo("_id", id).findFirst();
            saida.setQtdAtual(saida.getQtdAtual() - qtd);
            realm.commitTransaction();
        }else {
            realm.beginTransaction();
            Estoque entrada = new Estoque();
            entrada = realm.where(Estoque.class).equalTo("_id", id).findFirst();
            entrada.setQtdAtual(entrada.getQtdAtual() + qtd);
            realm.commitTransaction();
        }
    }

    //Função que verifica se o produto existe através do ID
    public boolean produtoExiste(Realm realm, int id){
        RealmQuery<Estoque> query = realm.where(Estoque.class).equalTo("_id", id);
        Estoque pesquisa = query.findFirst();
        if (pesquisa == null){
            return false;
        }else{
            return true;
        }
    }
}