package br.com.app_android_ecommerce.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.common.base.Strings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.app_android_ecommerce.R;
import br.com.app_android_ecommerce.utils.Singleton;

public class PerfilActivity extends AppCompatActivity {

    private static final int SELECIONAR_IMAGEM = 1;
    private static final String TAG = "Usuario Perfil";
    private ListenerRegistration documentoRefRegistro;
    private FirebaseUser logandoComUsuario;
    private String provedorID;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private FirebaseFirestore db = Singleton.getDb();
    private FirebaseAuth auth;

    ArrayList imagem = new ArrayList();

    private static String editTextAddrLine = null;
    private static String enderecoLinha = null;
    private static String caracteristica = null;
    private static String rua = null;
    private static String cidade = null;
    private static String pais = null;
    private static String cep;
    private static String latitude = null;
    private static String longitude = null;
    private static ImageView addPerfilFoto = null;
    private static ImageView addImagem = null;

    PlacesClient placesClient;

    private Uri caminhoArquivo;

    int PEGA_IMAGEM_CODIGO = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        final EditText nome;
        final EditText email;
        final EditText telefone;
        final EditText paypalid;
        final EditText apt;

        final ImageView telefoneBtn;
        final ImageView paypalidBtn;
        final ImageView enderecoBtn;
        final ImageView aptBtn;

        final AutocompleteSupportFragment endereco;

        final FirebaseAuth firebaseAuth;

        FirebaseFirestore firebaseFirestore;

        final String uID;

        final StorageReference perfilFoto;

        nome = findViewById(R.id.perfil_usuario_nome);
        email = findViewById(R.id.perfil_usuario_email);
        telefone = findViewById(R.id.perfil_usuario_telefone);

        telefoneBtn = findViewById(R.id.perfil_telefone_editar);

        paypalid = findViewById(R.id.profile_paypalid);
        paypalidBtn = findViewById(R.id.perfil_paypalid_editar);

        endereco = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.perfil_endereco);
        enderecoBtn = findViewById(R.id.perfil_endereco_editar);
        apt = findViewById(R.id.perfil_endereco_apt);
        aptBtn = findViewById(R.id.perfil_endereco_apt_editar);

        addPerfilFoto = findViewById(R.id.perfil_usuario_imagem);
        addImagem = findViewById(R.id.perfil_add_imagem);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        logandoComUsuario = firebaseAuth.getCurrentUser();
        auth = FirebaseAuth.getInstance();
        storage = Singleton.getStorage();
        storageReference = storage.getReference();

        if (!Places.isInitialized()) {
            Places.initialize(PerfilActivity.this, null);
        }
        placesClient = Places.createClient(this);
        endereco.setPlaceFields(Arrays.asList(Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS));

        if (logandoComUsuario != null) {
            provedorID = logandoComUsuario.getProviderData().get(1).getProviderId();
            uID = logandoComUsuario.getUid();

            perfilFoto = storageReference.child("perfilImagens/" + logandoComUsuario.getUid());

            Log.v("atualizarPerfilImagem", " Para o usuário em criar " + perfilFoto.getDownloadUrl().toString());

            DocumentReference documentReference = firebaseFirestore.collection("USUARIOS").document(uID);
            documentoRefRegistro = documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    String fn = documentSnapshot.getString("nomer");
                    String ln = documentSnapshot.getString("sobrenome");
                    nome.setText(fn + " " + ln);
                    email.setText(documentSnapshot.getString("email"));
                    telefone.setText(documentSnapshot.getString("telefone"));
                    endereco.setText(documentSnapshot.getString("endereco"));
                    paypalid.setText(documentSnapshot.getString("paypalid"));
                }
            });
            if (perfilFoto != null) {
                perfilFoto.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.v("atualizarPerfilImagem", " baixado URI " + uri.toString());
                        Glide.with(PerfilActivity.this).load(uri).into(addPerfilFoto);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v("atualizarPerfilImagem", " Não foi possível baixar a imagem ");
                    }
                });
            }
        }
        telefoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ph = telefone.getText().toString();


            }
        });
    }
    public static boolean ehValidoEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public static boolean ehValidoTelefone(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.PHONE.matcher(target).matches());
    }
    public static boolean ehValidoEndereco() {
        if (Strings.isNullOrEmpty(PerfilActivity.pais) || Strings.isNullOrEmpty(PerfilActivity.caracteristica)) {
            return false;
        }
        if (PerfilActivity.pais.equals("Estados Unidos") || PerfilActivity.pais.equals("Canada")) {
            if (!PerfilActivity.editTextAddrLine.startsWith(PerfilActivity.caracteristica)
            || Strings.isNullOrEmpty(PerfilActivity.caracteristica) || Strings.isNullOrEmpty(PerfilActivity.rua)
            || Strings.isNullOrEmpty(PerfilActivity.cidade) || Strings.isNullOrEmpty(PerfilActivity.cep)) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    private LatLng getLatLngDoEndereco(String endereco) {
        Geocoder geocoder = new Geocoder(PerfilActivity.this);

        List<Address> listaEnderecos;

        try {
            listaEnderecos = geocoder.getFromLocationName(endereco, 1);

            if (listaEnderecos != null) {
                Address unicoEndereco = listaEnderecos.get(0);
                LatLng latLng = new LatLng(unicoEndereco.getLatitude(), unicoEndereco.getLongitude());

                return latLng;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
    private Address getEnderecoDaLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(PerfilActivity.this);
        List<Address> enderecos;

        try {
            enderecos = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5);

            if (enderecos != null) {
                Address endereco = enderecos.get(0);

                return endereco;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}