package br.com.app_android_ecommerce.item;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import br.com.app_android_ecommerce.MainActivity;
import br.com.app_android_ecommerce.R;
import br.com.app_android_ecommerce.interfaces.ArquivoDadoImagemStatus;
import br.com.app_android_ecommerce.model.Item;
import br.com.app_android_ecommerce.utils.Singleton;
import br.com.app_android_ecommerce.utils.UtilitariosData;
import mehdi.sakout.fancybuttons.FancyButton;

public class AddItemActivity extends AppCompatActivity {

    @VisibleForTesting
    private static final int SELECIONE_IMAGEM = 1;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Item item;
    private Uri caminhoArquivo;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private FirebaseFirestore db = Singleton.getDb();

    private String uID;

    private ImageButton addItemImagemm;
    private EditText addItemNome;
    private EditText addItemDesc;
    private EditText addItemPreco;

    private FancyButton addItemBotao;

    public static String docID = null;

    ArrayList imagem = new ArrayList();

    private static String paypalID = "";
    private static String latitude = "";
    private static String longitude = "";
    private static String endereco = "";
    private static String categoria = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        addItemImagemm = findViewById(R.id.addItemImagem);
        addItemNome = findViewById(R.id.addItemNome);
        addItemDesc = findViewById(R.id.addItemDesc);
        addItemPreco = findViewById(R.id.addItemPreco);
        addItemBotao = findViewById(R.id.addItemBotao);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        uID = auth.getCurrentUser().getUid();
        storage = Singleton.getStorage();
        storageReference = storage.getReference();

        db.collection("USUARIOS")
                .document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        AddItemActivity.paypalID = task.getResult().get("paypalID").toString();
                        AddItemActivity.latitude = task.getResult().get("latitude").toString();
                        AddItemActivity.longitude = task.getResult().get("longitude").toString();
                        AddItemActivity.endereco = task.getResult().get("endereco").toString();

//                        Log.v("diego", AddItemActivity.paypalID + "," + AddItemActivity.latitude + "," + AddItemActivity.longitude);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        addItemImagemm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                Log.v("diego", "adicionado imagem");

                startActivityForResult(Intent.createChooser(intent, "Selecione a imagem"), SELECIONE_IMAGEM);
            }
        });
        final Spinner categoria = (Spinner) findViewById(R.id.addItemCategoria);

        categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object cat = adapterView.getItemAtPosition(i);

                AddItemActivity.categoria = cat.toString();

                Log.v("categoria", "selecionada" + AddItemActivity.categoria);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECIONE_IMAGEM && resultCode == RESULT_OK && data != null && data.getData() != null) {
            caminhoArquivo = data.getData();

            enviarImagem(new ArquivoDadoImagemStatus() {

                @Override
                public void comSucesso(Uri uri) {
                    Glide.with(AddItemActivity.this).load(uri).into(addItemImagemm);

                    addItemBotao.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String itemNome = addItemNome.getText().toString();
                            String itemDesc = addItemDesc.getText().toString();
                            String itemPreco = addItemPreco.getText().toString();
                            String itemCriadoHora = new UtilitariosData().getCurrentTimeInMillis();

                            if (TextUtils.isEmpty(itemNome) == false &&
                            TextUtils.isEmpty(itemDesc) == false &&
                            TextUtils.isEmpty(itemPreco) == false &&

                            AddItemActivity.categoria.equals("Selecionar Categortia") == false) {
                                item = new Item(itemNome, itemDesc, uID, Float.valueOf(itemPreco), uri.toString(), imagem);

                                // Adicionar item ao banco de dados
                                Map<String, Object> dbItem = new HashMap<>();
                                dbItem.put("vendedorUID", item.getItemVendedorUID());
                                dbItem.put("itemNome", item.getItemNome());
                                dbItem.put("itemDescricao", item.getItemDescricao());
                                dbItem.put("itemPreco", item.getItemPreco());
                                dbItem.put("itemImagem", item.getItemImagem());
                                dbItem.put("itemImagemLista", item.getItemImagemLista());
                                dbItem.put("itemPedido", false);
                                dbItem.put("itemEscolhido", false);
                                dbItem.put("itemLatitude", AddItemActivity.latitude);
                                dbItem.put("itemLongitude", AddItemActivity.longitude);
                                dbItem.put("itemEndereco", AddItemActivity.endereco);
                                dbItem.put("itemCategoria", AddItemActivity.categoria);
                                dbItem.put("itemCriadoHora", itemCriadoHora);

                                db.collection("ITEMS")
                                        .add(dbItem)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                AddItemActivity.docID = documentReference.getId();

                                                documentReference.update("itemID", AddItemActivity.docID);

                                                Log.v("addItemActivity", "Item Adicionado com sucesso ID: " + AddItemActivity.docID);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.v("addItemActivity", "Falha ao adicionar item");
                                            }
                                        });
                                Toast.makeText(AddItemActivity.this, "Item Adicionado", Toast.LENGTH_SHORT).show();
                                
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                Toast.makeText(AddItemActivity.this, "Item não Adicionado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                @Override
                public void comErro(String e) {

                }
            });
        }
    }
    private void enviarImagem(final ArquivoDadoImagemStatus arquivoDadoImagemStatus) {
        if (caminhoArquivo != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);

            progressDialog.setTitle("Enviando Imagem...");
            progressDialog.show();

            final StorageReference reference = storageReference.child("images/" + user.getUid() + '/' + UUID.randomUUID().toString());

            reference.putFile(caminhoArquivo)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            Log.v("enviarImagem", reference.getDownloadUrl().toString());

                            UploadTask uploadTask = reference.putFile(caminhoArquivo);

                            Task<Uri> urlTarefa = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    Log.v("enviarImagem", reference.getDownloadUrl().toString());
                                    return reference.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri enviarUri = task.getResult();

                                        arquivoDadoImagemStatus.comSucesso(enviarUri);
                                        imagem.add(enviarUri.toString());

                                        Log.v("enviarImagem", enviarUri.toString());
                                    } else {
                                        Toast.makeText(AddItemActivity.this, "Erro em ArquivoDadoImagemStatus", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            arquivoDadoImagemStatus.comErro("Erro");

                            Toast.makeText(AddItemActivity.this, "Erro no envio de imagem" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progresso = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Enviando Imagem... " + (int) progresso + "%");
                        }
                    });
        }
    }
}