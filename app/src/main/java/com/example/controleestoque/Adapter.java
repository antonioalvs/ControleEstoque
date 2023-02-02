package com.example.controleestoque;

import static io.realm.Realm.getApplicationContext;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    Estoque et;
    List<Estoque> listaEstoque = new ArrayList<>();
    private SelectListener listener;

    public Adapter(){

    }

    public Adapter(List<Estoque> listEt, SelectListener listener){
        this.listaEstoque = listEt;
        this.listener = listener;
    }
    //List<Estoque> list;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_adapter,
                viewGroup,
                false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int i) {
        String repor = "";
        if(this.listaEstoque.get(i).getQtdMinima() < this.listaEstoque.get(i).getQtdAtual()){
            repor = "(Repor Estoque)";
        }
        holder.alID.setText("Código:"+String.valueOf(listaEstoque.get(i).get_id()));
        holder.alNome.setText("Produto:"+this.listaEstoque.get(i).getNomeProduto());
        holder.alValor.setText("Preço: R$"+String.valueOf(this.listaEstoque.get(i).getValor()));
        holder.alQtdAtual.setText("Quantidade em estoque:"+String.valueOf(this.listaEstoque.get(i).getQtdAtual()));
        holder.alRepor.setText(repor);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(listaEstoque.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        int t = this.listaEstoque.size();
        return t;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView alID;
        private TextView alNome;
        private TextView alValor;
        private TextView alQtdAtual;
        private TextView alQtdMinima;

        private TextView alRepor;



        public ViewHolder(View view) {

            super(view);

            // Define click listener for the ViewHolder's View
            alID = view.findViewById(R.id.alID);
            alNome = view.findViewById(R.id.alNome);
            alValor = view.findViewById(R.id.alValor);
            alQtdAtual = view.findViewById(R.id.alQtdAtual);
            alQtdMinima = view.findViewById(R.id.alQtdMinima);
            alRepor = view.findViewById(R.id.alRepor);

        }
    }
}

