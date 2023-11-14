package in.macrocodes.creatives;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    EditText mEmail,mPassword,mFullName;
    RelativeLayout mLoginBtn;
    Button mRegisterBtn;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setContentView(R.layout.activity_register);
        initializations();
        clickListners();
    }

    private void initializations(){
        mAuth = FirebaseAuth.getInstance();
        mEmail = (EditText) findViewById(R.id.registerEmail);
        mFullName = (EditText) findViewById(R.id.registerName);
        mPassword = (EditText) findViewById(R.id.registerPassword);
        mLoginBtn = (RelativeLayout) findViewById(R.id.loginBtn);
        mRegisterBtn = (Button) findViewById(R.id.registerBtn);
    }
    private void clickListners(){
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString();
                String name = mFullName.getText().toString();
                String password = mPassword.getText().toString();

                if (email.isEmpty() || name.isEmpty() || password.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }else{
                    registerUser(email,password,name);
                }

            }
        });
    }

    private void registerUser(String email,String password,String name){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            HashMap hashMap = new HashMap();
                            hashMap.put("name",name);
                            hashMap.put("email",email);
                            hashMap.put("profile","default");
                            hashMap.put("user_type","staff");
                            hashMap.put("online","false");
                            hashMap.put("current_uid",user_id);

                            db.collection("Users").document(user_id)
                                    .update(hashMap)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()){
                                                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });



                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("registerError", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}