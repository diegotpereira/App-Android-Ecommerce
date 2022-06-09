package br.com.app_android_ecommerce.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.com.app_android_ecommerce.interfaces.ClickListenerItemCategoria;
import br.com.app_android_ecommerce.MainActivity;
import br.com.app_android_ecommerce.R;
import br.com.app_android_ecommerce.model.ItemCategoria;

public class ItemCategoriaAdapter extends RecyclerView.Adapter<ItemCategoriaAdapter.CategoriaViewHolder> {

    private Context context;
    private ArrayList<ItemCategoria> itensCategorias;
    private ClickListenerItemCategoria itemCategoriaClickListener;
    int posicao_selecionada = 0;

    public ItemCategoriaAdapter(Context context, ArrayList<ItemCategoria> itensCategorias, ClickListenerItemCategoria itemCategoriaClickListener) {
        this.context = context;
        this.itensCategorias = itensCategorias;
        this.itemCategoriaClickListener = itemCategoriaClickListener;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_categoria, parent, false);
        return new CategoriaViewHolder(v);
    }

    public ArrayList<ItemCategoria> getItensCategorias() {
        return itensCategorias;
    }
    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        ItemCategoria itemCategoria = itensCategorias.get(position);

        if (MainActivity.categoria.equals("")  || posicao_selecionada != position) {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#CABBEB"));
        }
        holder.bindModel(itemCategoria);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CategoriaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemCategoria itemCategoria;
        TextView itemCategoriaNome;
        ImageView itemCategoriaImagem;

        public CategoriaViewHolder(@NonNull View v) {
            super(v);
            itemCategoriaNome = itemView.findViewById(R.id.layout_itens_categoria_texto);
            itemCategoriaImagem = itemView.findViewById(R.id.layout_itens_categoria_imagem);
            v.setOnClickListener(this);
        }

        void bindModel(ItemCategoria itemCategoria) {
            this.itemCategoria = itemCategoria;
            itemCategoriaNome.setText(itemCategoria.getCategoriaNome);
            itemCategoriaImagem.setBackgroundResource(itemCategoria.getCategoriaIconeCor());
            itemCategoriaImagem.setImageResource(itemCategoria.getCategoriaIconeId());
        }
        @Override
        public void onClick(View view) {

        }
    }
}
