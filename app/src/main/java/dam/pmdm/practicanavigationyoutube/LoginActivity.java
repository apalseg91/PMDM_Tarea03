package dam.pmdm.practicanavigationyoutube;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    /**
     * Declaracion y asignación de variables usadas.
     */
    private static final int RC_SIGN_IN = 123;
    private EditText emailEditText, passwordEditText;
    private Button loginButton, registerButton;
    private ImageView logo;
    private com.google.android.gms.common.SignInButton googleSignInButton;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    /**
     * método que crea y toda de contenido la pantalla
     *
     * @param savedInstanceState objeto Bundle con la información para re-cargar la pantalla
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //inicializo variables
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        googleSignInButton = findViewById(R.id.googleSignInButton);
        logo = findViewById(R.id.logoSlogan);
        mAuth = FirebaseAuth.getInstance();
        //llamo al inicio de sesión con cuenta Google
        configureGoogleSignIn();
        //manejo de botones
        loginButton.setOnClickListener(v -> loginUser());
        registerButton.setOnClickListener(v -> registerUser());
        googleSignInButton.setOnClickListener(v -> signInWithGoogle());
    }

    /**
     * método para manejar los inicios de sesión con cuenta Google
     */
    private void configureGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    /**
     * método que inicia el flujo de autenticación con cuenta Google
     */
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * método maneja el resultado de las actividades iniciadas, incluyendo la actividad de inicio de sesión con Google. Si el resultado corresponde al inicio de sesión,
     * obtiene la cuenta de Google del usuario y procede a autenticarla con Firebase usando el token de la cuenta.
     *
     * @param requestCode: Código que identifica la solicitud que originó el resultado.
     * @param resultCode:  Código que indica el estado del resultado.
     * @param data:        Intent que contiene los datos devueltos.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(Exception.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account.getIdToken());
                }
            } catch (Exception e) {
                Log.d(getString(R.string.error_de_login), e.toString());
                String text = getString(R.string.google_sign_in_failed);
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * método que realiza la autenticación con Firebase usando las credenciales obtenidas del inicio de sesión
     * con Google. Si la autenticación es exitosa, redirige al usuario a la pantalla principal de la aplicación.
     *
     * @param idToken Token de autenticación de la cuenta de Google del usuario.
     */
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "Bienvenido, " + (user != null ? user.getDisplayName() : "Usuario"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        String text = getString(R.string.error_al_autenticar_con_google);
                        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * método que verifica que los campos de correo y contraseña no estén vacíos.Si las credenciales
     * son válidas, redirige al usuario a la pantalla principal.
     */
    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            String text = getString(R.string.por_favor_llena_todos_los_campos);
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, getString(R.string.error) + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * método que registra un nuevo usuario en Firebase Authentication usando un correo electrónico
     * y contraseña.Verifica que los campos no estén vacíos y que la contraseña tenga al menos 6 caracteres.
     */
    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            String text = getString(R.string.rellena_todos_los_campos);
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            String text = getString(R.string.min_caracteres_psswd);
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, getString(R.string.usuario_registrado_exitosamente), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, getString(R.string.error) + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}


