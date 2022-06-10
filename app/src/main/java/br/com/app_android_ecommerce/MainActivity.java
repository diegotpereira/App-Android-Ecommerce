package br.com.app_android_ecommerce;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import br.com.app_android_ecommerce.adapters.ItemAdapter;
import br.com.app_android_ecommerce.adapters.ItemCategoriaAdapter;
import br.com.app_android_ecommerce.fragments.BottomSheetNavigationFragmento;
import br.com.app_android_ecommerce.interfaces.ArquivoDadoStatus;
import br.com.app_android_ecommerce.interfaces.ClickListenerItem;
import br.com.app_android_ecommerce.interfaces.ClickListenerItemCategoria;
import br.com.app_android_ecommerce.item.Categorias;
import br.com.app_android_ecommerce.item.ItemActivity;
import br.com.app_android_ecommerce.model.Item;
import br.com.app_android_ecommerce.model.ItemCategoria;
import br.com.app_android_ecommerce.utils.Singleton;

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

    private static int precoMin = 0;
    private static int precoMax = 2000;

    private double usuarioLat = 0;
    private double usuarioLon = 0;
    private double usuarioMilhas = 25;


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

        db = Singleton.getDb();


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
        exibirItens();


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
                exibirItens();
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
                                exibirItens();

                            }
                        });
                        recyclerViewItemCategorias.setAdapter(itemCategoriaAdapter);
        } else {
            itemCategoriaAdapter.getItensCategorias().clear();
            itemCategoriaAdapter.getItensCategorias().addAll(itemCategoriaLista);
            itemCategoriaAdapter.notifyDataSetChanged();
        }
    }

    private void getItens(final ArquivoDadoStatus arquivoDadoStatus) {
        db.collection("ITEMS").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        ArrayList<String> imgs = new ArrayList<>();
                        Map<String, Object> meuMapa = documentSnapshot.getData();

                        String itemLatitude = (String) meuMapa.get("itemLatitude");
                        String itemLongitude = (String) meuMapa.get("itemLongitude");
                        String itemEndereco = (String) meuMapa.get("itemEndereco");
                        String itemCategoria = (String) meuMapa.get("itemCategoria");
                        boolean itemPedido = (boolean) meuMapa.get("itemPedido");

//                        double lat = Double.parseDouble(itemLatitude);
//                        double lon = Double.parseDouble(itemLongitude);
//                        double curDistancia = 0.0;

//                        if (usuarioLat != 0 && usuarioLon != 0) {
//
//                        }
//
                        Double preco = 0.0;
//                        Object precoDoDB = meuMapa.get("itemPreco");
//                        if (precoDoDB.getClass() == Double.class) {
//                            preco = (Double) meuMapa.get("itemPreco");
//                        } else if (precoDoDB.getClass() == Long.class) {
//                            preco = ((Long) meuMapa.get("itemPreco")).doubleValue();
//                        }
//                        if (!(boolean) meuMapa.get("itemPedido")
//                        && curDistancia <= usuarioMilhas
//                        && (preco >= precoMin && preco <= precoMax)
//                        && (categoria.equals("") ||
//                                categoria.equals(itemCategoria) ||
//                                categoria.equals("Brindes")
//                        && preco == 0)) {
//                            for(Map.Entry<String, Object> entry : meuMapa.entrySet()) {
//                                if (entry.getKey().equals("itemImagemLista")) {
//                                    for(Object s : (ArrayList) entry.getValue()) {
//                                        imgs.add((String) s);
//                                    }
//                                    Log.v("TagImg", entry.getValue().toString());
//                                }
//                            }
//                        }
                        Item item = new Item();
                        item.setItemID(documentSnapshot.getId());
                        item.setItemVendedorUID((String) meuMapa.get("vendedorUID"));
                        item.setItemNome((String) meuMapa.get("itemNome"));
                        item.setItemDescricao((String) meuMapa.get("itemDescricao"));
                        item.setItemPreco(preco.floatValue());
                        item.setItemImagemLista(imgs);
                        item.setItemEndereco(itemEndereco);
                        item.setItemCategoria(itemCategoria);
                        item.setItemPedido(itemPedido);
                        item.setLatitude(itemLatitude);
                        item.setLongitude(itemLongitude);

                        itensLista.add(item);
                    }
                    arquivoDadoStatus.comSucesso(itensLista);
                } else {
                    arquivoDadoStatus.comErro("Erro em obter dados");

                    Log.w(TAG, "Erro em obter dados.", task.getException());
                }
            }
        });
    }
    private void exibirItens() {
        getItens(new ArquivoDadoStatus() {
            @Override
            public void comSucesso(ArrayList lista) {
                if (itemAdapter == null) {
                    itemAdapter = new ItemAdapter(MainActivity.this, itensLista, new ClickListenerItem() {

                                            @Override
                                            public void onClick(View view, Item item) {
                                                Intent itemPagina = new Intent(MainActivity.this, ItemActivity.class);

                                                itemPagina.putExtra("itemID", item.getItemID());
                                                itemPagina.putExtra("vendedorUID",item.getItemVendedorUID());
                                                itemPagina.putExtra("itemNome",item.getItemNome());
                                                itemPagina.putExtra("itemDescricao", item.getItemDescricao());
                                                itemPagina.putExtra("itemPreco", item.getItemPreco());
                                                itemPagina.putExtra("itemImagem", item.getItemImagem());
                                                itemPagina.putExtra("itemImagemLista", item.getItemImagemLista());
                                                itemPagina.putExtra("itemEndereco", item.getItemEndereco());
                                                itemPagina.putExtra("itemCategoria", item.getItemCategoria());
                                                itemPagina.putExtra("itemPedido", item.isItemPedido());
                                                itemPagina.putExtra("itemLatitude", item.getLatitude());
                                                itemPagina.putExtra("itemLongitude", item.getLongitude());

                                                startActivity(itemPagina);
                                            }
                                        });
                    recyclerViewItens.setAdapter(itemAdapter);
                } else {
                    itemAdapter.getItens().clear();
                    itemAdapter.getItens().addAll(itensLista);
                    itemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void comErro(String e) {

            }
        });
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