package br.com.app_android_ecommerce.adapters;

import android.content.Context;
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

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
