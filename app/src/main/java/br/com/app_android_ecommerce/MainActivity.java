package br.com.app_android_ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import br.com.app_android_ecommerce.adapters.ItemAdapter;
import br.com.app_android_ecommerce.adapters.ItemCategoriaAdapter;
import br.com.app_android_ecommerce.fragments.BottomSheetNavigationFragmento;
import br.com.app_android_ecommerce.interfaces.ClickListenerItemCategoria;
import br.com.app_android_ecommerce.item.Categorias;
import br.com.app_android_ecommerce.model.Item;
import br.com.app_android_ecommerce.model.ItemCategoria;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private Toolbar toolbar;
    private RecyclerView recyclerViewItens;
    private RecyclerView recyclerViewItemCategorias;
    private SwipeRefreshLayout puxeParaAtualizar;

    private FirebaseFirestore db;

    private ProgressBar progressBar;

    private ArrayList<Item> itensLista = new ArrayList<Item>();
    private ItemAdapter itemAdapter;
    private ArrayList<ItemCategoria> itemCategoriaLista = new ArrayList<>();
    private ItemCategoriaAdapter itemCategoriaAdapter;

    private BottomAppBar bottomAppBar;

    private FloatingActionButton fabChat;
    private FloatingActionButton fabCarrinho;

    private RecyclerView recyclerViewBuscar;

    private SearchView telaPesquisa;

    public static String categoria = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setarBottomAppBar();

        toolbar = findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.searchProgressBar);
        progressBar.setVisibility(View.GONE);

        recyclerViewItens = findViewById(R.id.HomeActivityItemsList);
        recyclerViewItemCategorias = findViewById(R.id.HomeActivityItemCategoriesList);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setSubtitle(null);

        // Context context
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);

        recyclerViewItemCategorias.setLayoutManager(linearLayoutManager);
        recyclerViewItemCategorias.setHasFixedSize(true);
        exibirCategorias();

        // mostrar 2 itens no layout de grade
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);

        recyclerViewItens.setLayoutManager(gridLayoutManager);
        recyclerViewItens.setNestedScrollingEnabled(false);


        // exbir itens
        puxeParaAtualizar = findViewById(R.id.MainItemPullToRefresh);
        puxeParaAtualizar.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemAdapter = null;
                itensLista = new ArrayList<>();

                GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getApplicationContext(), 2);

                recyclerViewItens.setLayoutManager(gridLayoutManager1);
                recyclerViewItens.setNestedScrollingEnabled(false);
                puxeParaAtualizar.setRefreshing(false);
            }
        });
        fabChat = findViewById(R.id.fabChat);
        fabCarrinho = findViewById(R.id.fabCarrinho);

        fabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        fabCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    private void exibirCategorias() {
        itemCategoriaLista = new ArrayList<>();
        ArrayList<String> catLista = new Categorias().getItemCategoriaLista();

        for(int index = 0; index < catLista.size(); index++) {
            String catNome = catLista.get(index);

            ArrayList<String> catValor = new Categorias().getItemCategoriaResource(catNome);

            int drawImg = getResources().getIdentifier(catValor.get(0), "drawable", "br.com.app_android_ecommerce");
            int corRes = getResources().getIdentifier(catValor.get(1), "color", "br.com.app_android_ecommerce");

            ItemCategoria ic = new ItemCategoria(catNome, drawImg, corRes);
            itemCategoriaLista.add(ic);
        }
        if (itemCategoriaAdapter == null) {
            // alterado a ordem.
            itemCategoriaAdapter = new ItemCategoriaAdapter(MainActivity.this, itemCategoriaLista, new ClickListenerItemCategoria() {
                            @Override
                            public void onClick(View view, ItemCategoria itemCategoria) {
                                if (categoria == itemCategoria.getCategoriaNome()) {
                                    Log.v("categoria", "unselect:" + categoria);
                                    categoria = "";
                                    puxeParaAtualizar.setEnabled(true);
                                    recyclerViewBuscar.setVisibility(View.GONE);
                                    recyclerViewItens.setVisibility(View.VISIBLE);
                                } else {
                                    categoria = itemCategoria.getCategoriaNome();
                                    Log.v("categoria", "select: " + categoria);

                                    recyclerViewItens.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.VISIBLE);

                                    puxeParaAtualizar.setEnabled(false);

                                }
                                itemAdapter = null;
                                itensLista = new ArrayList<>();

                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);

                                recyclerViewItens.setLayoutManager(gridLayoutManager);
                                recyclerViewItens.setNestedScrollingEnabled(false);

                            }
                        });
                        recyclerViewItemCategorias.setAdapter(itemCategoriaAdapter);
        } else {
            itemCategoriaAdapter.getItensCategorias().clear();
            itemCategoriaAdapter.getItensCategorias().addAll(itemCategoriaLista);
            itemCategoriaAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_pesquisar, menu);

        final MenuItem item = menu.findItem(R.id.menuSearch);
        telaPesquisa = (SearchView) item.getActionView();
        SearchView telaPesquisa = (SearchView) item.getActionView();

        Intent intent = getIntent();

        telaPesquisa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recyclerViewItens.setVisibility(View.GONE);
                puxeParaAtualizar.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String novoTexto) {
                Log.i("novoTexto", novoTexto);
                return false;
            }
        });
        telaPesquisa.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                recyclerViewItens.setVisibility(View.VISIBLE);

                if (recyclerViewBuscar != null)
                    recyclerViewBuscar.setVisibility(View.GONE);
                puxeParaAtualizar.setEnabled(true);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    // definir BottomAppBar
    private void setarBottomAppBar() {
        bottomAppBar = findViewById(R.id.bar);

        setSupportActionBar(bottomAppBar);

        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // abrir botao sheet
                BottomSheetNavigationFragmento bottomSheetNavigationFragmento = BottomSheetNavigationFragmento.newInstance();
                bottomSheetNavigationFragmento.show(getSupportFragmentManager(), "Fragmento da caixa de diálogo da folha inferior");
            }
        });
    }
}