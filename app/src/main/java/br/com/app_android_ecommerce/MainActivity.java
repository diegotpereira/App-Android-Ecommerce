package br.com.app_android_ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private Toolbar toolbar;
    private RecyclerView recyclerViewItens;
    private RecyclerView recyclerViewCategorias;

    private FirebaseFirestore db;

    private ProgressBar progressBar;

//    private BottomAppBar bottomAppBar;

//    private FloatingActionButton fabChat;
//    private FloatingActionButton fabCarrinho;

    private RecyclerView recyclerViewBuscar;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setarBottomAppBar();

        toolbar = findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.searchProgressBar);
        progressBar.setVisibility(View.GONE);

        recyclerViewItens = findViewById(R.id.HomeActivityItemsList);
        recyclerViewCategorias = findViewById(R.id.HomeActivityItemCategoriesList);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setSubtitle(null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
    // definir BottomAppBar
//    private void setarBottomAppBar() {
//        bottomAppBar = findViewById(R.id.bar);
//
//        setSupportActionBar(bottomAppBar);
//
//        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//    }
}