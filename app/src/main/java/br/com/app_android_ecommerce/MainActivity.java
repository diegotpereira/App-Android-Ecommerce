package br.com.app_android_ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
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

import br.com.app_android_ecommerce.fragments.BottomSheetNavigationFragmento;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private Toolbar toolbar;
    private RecyclerView recyclerViewItens;
    private RecyclerView recyclerViewCategorias;
    private SwipeRefreshLayout puxeParaAtualizar;

    private FirebaseFirestore db;

    private ProgressBar progressBar;

    private BottomAppBar bottomAppBar;

    private FloatingActionButton fabChat;
    private FloatingActionButton fabCarrinho;

    private RecyclerView recyclerViewBuscar;

    private SearchView telaPesquisa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setarBottomAppBar();

        toolbar = findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.searchProgressBar);
        progressBar.setVisibility(View.GONE);

        recyclerViewItens = findViewById(R.id.HomeActivityItemsList);
        recyclerViewCategorias = findViewById(R.id.HomeActivityItemCategoriesList);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setSubtitle(null);

        // exbit itens
        puxeParaAtualizar = findViewById(R.id.MainItemPullToRefresh);
        puxeParaAtualizar.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

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