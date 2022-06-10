package br.com.app_android_ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import br.com.app_android_ecommerce.R;
import br.com.app_android_ecommerce.interfaces.ClickListenerItem;
import br.com.app_android_ecommerce.model.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Item> itens;
    private ClickListenerItem itemClickListener;

    public ItemAdapter(Context context, ArrayList<Item> itens, ClickListenerItem itemClickListener) {
        this.context = context;
        this.itens = itens;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_itens_lista, parent, false);

        return new ViewHolder(v);
    }

    public ArrayList<Item> getItens() {
        return itens;
    }

    public int getItemCount() {
        return itens.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindModel(itens.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        Item item;
        TextView itemNome;
        TextView itemPreco;
        ImageView itemImagem;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNome = itemView.findViewById(R.id.layout_itens_categoria_texto);
            itemPreco = itemView.findViewById(R.id.layout_itens_preco);
            itemImagem = itemView.findViewById(R.id.layout_itens_imagem);
            itemView.setOnClickListener(this);
        }
        void bindModel(Item itens) {
            this.item = itens;

            itemNome.setText(item.getItemNome());
            itemPreco.setText("$"   +String.format("%.2f", item.getItemPreco()));

            Glide.with(context).load(item.getItemImagemLista().get(0)).into(itemImagem);

        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, item);
        }
    }
}
