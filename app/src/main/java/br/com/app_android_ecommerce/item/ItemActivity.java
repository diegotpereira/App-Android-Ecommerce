package br.com.app_android_ecommerce.item;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mukesh.tinydb.TinyDB;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Iterator;

import br.com.app_android_ecommerce.MainActivity;
import br.com.app_android_ecommerce.R;
import br.com.app_android_ecommerce.model.Item;
import br.com.app_android_ecommerce.utils.Singleton;
import br.com.app_android_ecommerce.utils.UtilitariosData;
import mehdi.sakout.fancybuttons.FancyButton;

public class ItemActivity extends AppCompatActivity {

    private String itemID;
    private String itemVendedorUID;
    private String itemNome;
    private String itemDescricao;
    private float itemPreco;
    private String itemImagem;
    private String itemEndereco;

    private ArrayList itemImagemLista;
    private boolean itemPedido;
    private String itemLatitude;
    private String itemLongitude;

    private TextView exibirItemNome;
    private TextView exibirItemDescricao;
    private TextView exibirItemPreco;
    private TextView vendedorNome;
    private TextView vendedorEmail;
    private TextView vendedorTelefone;
    private TextView vendedorEndereco;
    private TextView itemAddHora;
    private TextView itemVendedoDistanciaTexto;
    private TextView itemVendedoDistancia;


    private FancyButton exibirAddNoCarrinho;
    private boolean carrinhoClicado;

    private SliderLayout sliderLayout;

    private String TAG = "ITEM";

    private TinyDB tinyDB;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db = Singleton.getDb();
    private FusedLocationProviderClient client;

    private double lat = 0.0;
    private double lng = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        itemVendedoDistanciaTexto = findViewById(R.id.itemVendedoDistanciaTexto);
        itemVendedoDistancia = findViewById(R.id.itemVendedoDistancia);

        tinyDB = new TinyDB(this);

        itemID = (String) getIntent().getSerializableExtra("itemID");
        itemVendedorUID = (String) getIntent().getSerializableExtra("vendedorUID");
        itemNome = (String) getIntent().getSerializableExtra("itemNome");
        itemDescricao = (String) getIntent().getSerializableExtra("itemDescricao");
        itemPreco = (float) getIntent().getSerializableExtra("itemPreco");
        itemImagemLista = (ArrayList) getIntent().getSerializableExtra("itemImagemLista");
        itemEndereco = (String) getIntent().getSerializableExtra("itemEndereco");
        itemPedido = (boolean) getIntent().getSerializableExtra("itemPedido");
        itemLatitude = (String) getIntent().getSerializableExtra("itemLatitude");
        itemLongitude = (String) getIntent().getSerializableExtra("itemLongitude");

        exibirItemNome = findViewById(R.id.itemNome);
        exibirItemDescricao = findViewById(R.id.itemDescricao);
        exibirItemPreco = findViewById(R.id.itemPreco);
        itemAddHora = findViewById(R.id.itemAddHora);

        vendedorNome = findViewById(R.id.itemVendedorNome);
        vendedorEmail = findViewById(R.id.itemVendedorEmail);
        vendedorTelefone = findViewById(R.id.itemVendedorTelefone);
        vendedorEndereco = findViewById(R.id.itemVendedorEndereco);

        exibirAddNoCarrinho = findViewById(R.id.itemNoCarrinho);
        sliderLayout = findViewById(R.id.ItemImage);
        sliderLayout.setScrollTimeInSec(4);

        exibirItemNome.setText(itemNome);
        exibirItemDescricao.setText(itemDescricao);
        exibirItemPreco.setText("R$" + itemPreco);

        requisitaRermissao();
        if (ActivityCompat.checkSelfPermission(ItemActivity.this, ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            client = LocationServices.getFusedLocationProviderClient(ItemActivity.this);
            client.getLastLocation().addOnSuccessListener(ItemActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        double usuarioLat = location.getLatitude();
                        double usuarioLon = location.getLongitude();
                        double itemLat = Double.parseDouble(itemLatitude);
                        double itemLon = Double.parseDouble(itemLongitude);
//                        double distancia = new CalcularDistancia().distancia(usuarioLat, usuarioLon, itemLat, itemLon, 'M');

                    }
                }
            });
        } else {
            itemVendedoDistanciaTexto.setVisibility(View.GONE);
            itemVendedoDistancia.setVisibility(View.GONE);
        }
        if (user.getUid().equals(itemVendedorUID) || itemPedido) {
            exibirAddNoCarrinho.setVisibility(View.GONE);
        } else {
            exibirAddNoCarrinho.setVisibility(View.VISIBLE);
        }
        db.collection("USUARIOS")
        .document(itemVendedorUID)
        .get()
        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    vendedorNome.setText(task.getResult().get("nome").toString() + " " +
                            task.getResult().get("sobrenome").toString());
                    vendedorEmail.setText(task.getResult().get("email").toString());
                    vendedorTelefone.setText(task.getResult().get("telefone").toString());
                    vendedorEndereco.setText(task.getResult().get("endereco").toString());
                }
            }
        });
        db.collection("ITEMS")
        .document(itemID)
        .get()
        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String itemCriadoHora = "";

                    if (task.getResult().get("itemCriadoHora") == null) {
                        itemCriadoHora = "1586590726600";
                    } else {
                        itemCriadoHora = task.getResult().get("itemCriadoHora").toString();
                    }
                    itemAddHora.setText(new UtilitariosData().getdataEhHora(itemCriadoHora));
                }
            }
        });
        if (itemNoTinyDB(itemID, tinyDB)) {
            carrinhoClicado = true;
            exibirAddNoCarrinho.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            carrinhoClicado = false;
            exibirAddNoCarrinho.setBackgroundColor(getResources().getColor(R.color.black));
        }
        exibirAddNoCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item item = new Item(itemID, itemNome, itemDescricao, itemVendedorUID, itemPreco, itemImagemLista);
                item.setItemEndereco(itemEndereco);
                item.setItemPedido(itemPedido);
                item.setLatitude(itemLatitude);
                item.setLongitude(itemLongitude);

                if (carrinhoClicado == false) {
                    addItemNoTinyDB(item, tinyDB);
                    exibirAddNoCarrinho.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    carrinhoClicado = true;

                    Toast.makeText(ItemActivity.this, "Item adicionado", Toast.LENGTH_SHORT).show();
                } else {

                    removerItemDoTinyDB(item, tinyDB);
                    exibirAddNoCarrinho.setBackgroundColor(getResources().getColor(R.color.black));
                    carrinhoClicado = false;

                    Toast.makeText(ItemActivity.this, "Item removido", Toast.LENGTH_SHORT).show();
                }
                startActivity(new Intent(ItemActivity.this, MainActivity.class));
                finish();
            }
        });
        slideImagemExibir();
    }
    private void requisitaRermissao() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }
    public void addItemNoTinyDB(Item item, TinyDB tinyDB) {
        ArrayList<Object> salvarObjetos = tinyDB.getListObject(user.getUid(), Item.class);

        // Adicionar item como objeto ao tinyDB
        salvarObjetos.add((Object) item);
        tinyDB.putListObject(user.getUid(), salvarObjetos);

        Log.v("TinyDB adicionado", "Item: " + item.getItemID() + " está adicionado");
    }
    public void removerItemDoTinyDB(Item item, TinyDB tinyDB) {
        if (itemNoTinyDB(item.getItemID(), tinyDB)) {
            ArrayList<Object> salvarObjetos;

            salvarObjetos = tinyDB.getListObject(user.getUid(), Item.class);

            Iterator itr = salvarObjetos.iterator();

            while (itr.hasNext()) {
                Item proximoItem = (Item) itr.next();

                if (item.getItemID().equals(proximoItem.getItemID())) {
                    itr.remove();

                    Log.v("TinyDB removido", "Item: " + item.getItemID() + " foi removido");
                }
            }
            tinyDB.putListObject(user.getUid(), salvarObjetos);
        }
    }
    public boolean itemNoTinyDB(String itemID, TinyDB tinyDB) {
        ArrayList<Object> salvarObjetos;

        salvarObjetos = tinyDB.getListObject(user.getUid(), ItemActivity.class);

        ArrayList<Item> itens = new ArrayList<Item>();

        for(Object objs : salvarObjetos) {
            itens.add((Item) objs);
        }
        for(int index = 0; index < itens.size(); index++) {
            if (itens.get(index).getItemID().equalsIgnoreCase(itemID)) {

                return true;
            }
        }
        return false;
    }
    private void slideImagemExibir() {
        for(int index = 0; index < itemImagemLista.size(); index++) {
            DefaultSliderView defaultSliderView = new DefaultSliderView(this);

            defaultSliderView.setImageUrl(itemImagemLista.get(index).toString());
            defaultSliderView.setImageScaleType(ImageView.ScaleType.CENTER_INSIDE);

            defaultSliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {

                }
            });
            sliderLayout.addSliderView(defaultSliderView);
        }
    }
}