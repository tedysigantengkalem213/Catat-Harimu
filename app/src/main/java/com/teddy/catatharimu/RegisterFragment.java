package com.teddy.catatharimu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;

// Eddy Rochman
// 10120052
// IF-2
public class RegisterFragment extends Fragment {
    private static final int RC_SIGN_IN = 9001;

    private TextInputLayout emailTextField, passwordTextField, passwordKonfirmasiTextField;
    private Button signUpButton, signInGoogleButton;

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        initViews(rootView);
        setupFirebaseAuth();
        setupGoogleSignIn();

        return rootView;
    }

    private void initViews(View rootView) {
        emailTextField = rootView.findViewById(R.id.emailTextField);
        passwordTextField = rootView.findViewById(R.id.passwordTextField);
        passwordKonfirmasiTextField = rootView.findViewById(R.id.passwordKonfirmasiTextField);
        signUpButton = rootView.findViewById(R.id.signUpButton);
        signInGoogleButton = rootView.findViewById(R.id.signInGoogleButton);

        signUpButton.setOnClickListener(v -> signUpWithEmail());
        signInGoogleButton.setOnClickListener(v -> signInWithGoogle());
    }

    private void setupFirebaseAuth() {
        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                redirectToMain();
            }
        };

        firebaseAuth.addAuthStateListener(authStateListener);
    }

    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso);
    }

    private void signUpWithEmail() {
        String email = emailTextField.getEditText().getText().toString().trim();
        String password = passwordTextField.getEditText().getText().toString().trim();
        String passwordRetype = passwordKonfirmasiTextField.getEditText().getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || passwordRetype.isEmpty()) {
            Toast.makeText(requireContext(), "Harap lengkapi semua data", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(passwordRetype)) {
            passwordKonfirmasiTextField.setError("Passwords do not match.");
            return;
        }

        // Check if the email is already registered using Firebase methods
        firebaseAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            if (result != null && result.getSignInMethods() != null && result.getSignInMethods().size() > 0) {
                                // Email is already registered using a different sign-in method
                                emailTextField.setError("Email already registered with a different sign-in method.");
                            } else {
                                // Email is not registered, proceed with email/password sign-up
                                firebaseAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Sign up success, update UI with the signed-in user's information
                                                    Toast.makeText(requireContext(), "Sign up successful!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    // If sign up fails, display a message to the user.
                                                    Toast.makeText(requireContext(), "Sign up failed.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            // Error occurred while checking sign-in methods for the email
                            Toast.makeText(requireContext(), "Error checking email availability.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void signInWithGoogle() {
        googleSignInClient.signOut().addOnCompleteListener(requireActivity(), task -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(requireContext(), "Google Sign in failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(requireContext(), "Google sign in successful!", Toast.LENGTH_SHORT).show();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(requireContext(), "Google sign in failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void redirectToMain() {
        Intent intent = new Intent(requireContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
