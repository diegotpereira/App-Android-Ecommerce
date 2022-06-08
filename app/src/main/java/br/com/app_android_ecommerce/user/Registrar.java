package br.com.app_android_ecommerce.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import br.com.app_android_ecommerce.MainActivity;
import br.com.app_android_ecommerce.R;
import br.com.app_android_ecommerce.utils.Singleton;
import mehdi.sakout.fancybuttons.FancyButton;

public class Registrar extends AppCompatActivity {

    private EditText registrarNome;
    private FancyButton registrarCadastroBtn;
    private EditText registrarSobrenome;
    private EditText registrarEmail;
    private EditText registrarSenha;

    private TextView registrarCadastreLogin;
    private ProgressBar registrarProgressBar;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String uID;
    public static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        registrarNome = findViewById(R.id.registrar_nome);
        registrarSobrenome = findViewById(R.id.registrar_sobrenome);
        registrarEmail = findViewById(R.id.registrar_email);
        registrarSenha = findViewById(R.id.registrar_senha);

        registrarCadastroBtn = findViewById(R.id.registrar_cadastro_btn);

        registrarCadastreLogin = findViewById(R.id.registrar_cadastro_login);
        registrarProgressBar = findViewById(R.id.registrar_progress_bar);

        auth = FirebaseAuth.getInstance();
        db = Singleton.getDb();

        registrarCadastroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = registrarEmail.getText().toString().trim();
                final String password = registrarSenha.getText().toString().trim();
                final String nome = registrarNome.getText().toString().trim();
                final String sobrenome = registrarSobrenome.getText().toString().trim();

                if (TextUtils.isEmpty(nome)) {
                    registrarNome.setError("Digite o nome");

                    return;
                }
                if (TextUtils.isEmpty(sobrenome)) {
                    registrarSobrenome.setError("Digite seu sobrenome");

                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    registrarEmail.setError("Digite seu email");

                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    registrarSenha.setError("Digite sua senha");

                    return;
                }
                if (password.length() < 8) {
                    registrarSenha.setError("A senha deve conter pelo menos 8 caracteres");

                    return;
                }
                registrarProgressBar.setVisibility(View.VISIBLE);

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            uID = auth.getCurrentUser().getUid();

                            DocumentReference documentReference = db.collection("usuarios").document(uID);

                            Map<String, Object> usuario = new HashMap<>();
                            usuario.put("nome", nome);
                            usuario.put("sobrenome", sobrenome);
                            usuario.put("email", email);
                            usuario.put("password", password);
                            usuario.put("paypalid", "");
                            usuario.put("endereco", "");
                            usuario.put("apt", "");
                            usuario.put("telefone", "");
                            usuario.put("latitude", "");
                            usuario.put("longitude", "");

                            documentReference.set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "onSuccess: O perfil do usuário é criado para " + uID );
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });

//                            DocumentReference documentReference2 = db.collection("preferencias").document(uID);
//
//                            Map<String, Object> dbRef = new HashMap<>();
//                            dbRef.put("distancia", 25);
//                            dbRef.put("grabon", true);
//                            dbRef.put("ebay", true);
//                            dbRef.put("craigslist", true);
//                            dbRef.put("precoMin", 0);
//                            dbRef.put("precoMax", 2000);
//                            dbRef.put("numeroItens", 15);
//                            dbRef.put("usuarioEndereco", "");
//                            dbRef.put("usuarioCep", "");
//                            dbRef.put("usuarioCidade", "");
//                            dbRef.put("usuarioLatitude", "");
//                            dbRef.put("usuarioLongitude", "");
//                            dbRef.put("atualUsuarioLatitude", "");
//                            dbRef.put("atualUsuarioLongitude", "");
//                            dbRef.put("atualUsuarioCidade", "");
//                            dbRef.put("atualUsuarioCep", "");
//
//                            documentReference2.set(dbRef).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        task.addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void unused) {
//                                                Log.d(TAG, "onSuccess: As Preferências do Usuário são criadas para " + uID);
//                                            }
//                                        }).addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Log.d(TAG, "onFailure: Preferências do usuário não criadas para " + uID);
//                                            }
//                                        });
//                                    }
//                                }
//                            });
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    registrarProgressBar.setVisibility(View.GONE);
                                    Log.d(TAG, "signInWithCredential:success");

                                    Toast.makeText(Registrar.this, "Usuário registrado e logado com sucesso", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                            }, 2000);
                        } else {
                            Toast.makeText(Registrar.this, "Erro ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        registrarCadastreLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}