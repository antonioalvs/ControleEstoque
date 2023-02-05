package com.example.controleestoque;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

//Classe que gera o MyRecyclerView da lista em "Gerenciar estoque"
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    Estoque et;
    List<Estoque> listaEstoque = new ArrayList<>();
    private SelectListener listener;

    //Construtor sem Parâmetros
    public Adapter(){}

    //Construtor com parâmetros
    public Adapter(List<Estoque> listEt, SelectListener listener){
        this.listaEstoque = listEt;
        this.listener = listener;
    }


    //Parâmetro necessário para a lista
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_adapter,
                viewGroup,
                false);
        return new ViewHolder(itemView);
    }


    //Função que determina o conteúdo dos itens da lista
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int i) {
        String repor = "";
        if(this.listaEstoque.get(i).getQtdMinima() > this.listaEstoque.get(i).getQtdAtual()){
            repor = "(Repor Estoque)";
        }
        //Coloca o preço no padrão R$ 0,00
        String preco = String.valueOf(this.listaEstoque.get(i).getValor());
        String precoUtil[] = preco.split("\\.");
        String valorFinal;
        if(precoUtil[1] == "0"){
            valorFinal = precoUtil[0] + ",00";
        } else if (precoUtil[1].length() == 1) {
            valorFinal = precoUtil[0] + "," + precoUtil[1]+ "0";
        }else{
            valorFinal = precoUtil[0] + "," + precoUtil[1];
        }
        //Preenche os campos da lista
        holder.alID.setText("Código:"+String.valueOf(listaEstoque.get(i).get_id()));
        holder.alNome.setText("Produto:"+this.listaEstoque.get(i).getNomeProduto());
        holder.alValor.setText("Preço: R$"+ valorFinal);
        holder.alQtdAtual.setText("Em estoque: "+String.valueOf(this.listaEstoque.get(i).getQtdAtual()));
        holder.alRepor.setText(repor);

        //Define a imagem que será mostrada (Estoque suficiente e Repor estoque)
        if(listaEstoque.get(i).getQtdAtual() < listaEstoque.get(i).getQtdMinima()){
            holder.img.setImageResource(R.drawable.pdnotok);
        }else{
            holder.img.setImageResource(R.drawable.pdok);
        }

        //Configura a ação de clique em cada item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(listaEstoque.get(i));
            }
        });
    }



    //Função necessária para a lista
    @Override
    public int getItemCount() {
        int t = this.listaEstoque.size();
        return t;
    }

    //Classe que define os campos para posterior amostra
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //Variaveis dos campos
        private TextView alID;
        private TextView alNome;
        private TextView alValor;
        private TextView alQtdAtual;
        private TextView alQtdMinima;
        private TextView alRepor;
        private ImageView img;
        public ViewHolder(View view) {
            //Atribui os campos para as variaveis
            super(view);
            alID = view.findViewById(R.id.alID);
            alNome = view.findViewById(R.id.alNome);
            alValor = view.findViewById(R.id.alValor);
            alQtdAtual = view.findViewById(R.id.alQtdAtual);
            alQtdMinima = view.findViewById(R.id.alQtdMinima);
            alRepor = view.findViewById(R.id.alRepor);
            img = view.findViewById(R.id.imageView);
        }
    }
}