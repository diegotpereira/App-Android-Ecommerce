package br.com.app_android_ecommerce.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.app_android_ecommerce.R;
import br.com.app_android_ecommerce.utils.Singleton;
import mehdi.sakout.fancybuttons.FancyButton;

public class LoginActivity extends AppCompatActivity {

    private EditText entrarEmail;
    private EditText entrarSenha;
    private FancyButton loginBotao;
    private FancyButton loginGmailBotao;
    private LoginButton loginFacebookBotao;
    private ProgressBar loginProgressBar;
    private FirebaseAuth auth;
    private TextView loginCriarUsuario;
    private FirebaseFirestore db;
    private String uID;

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "AppEcommerce";
    public static GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;

    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        entrarEmail = findViewById(R.id.LoginEmail);
        entrarSenha = findViewById(R.id.LoginPassword);
        loginBotao = findViewById(R.id.LoginButton);
        loginProgressBar = findViewById(R.id.LoginProgressBar);
        loginCriarUsuario = findViewById(R.id.LoginCreateUser);
        loginGmailBotao = findViewById(R.id.LoginGmailButton);
        loginFacebookBotao = findViewById(R.id.LoginFacebookButton);

        auth = FirebaseAuth.getInstance();
        db = Singleton.getDb();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }
}