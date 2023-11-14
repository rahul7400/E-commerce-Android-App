package in.macrocodes.creatives.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import in.macrocodes.creatives.CustomDatabase;
import in.macrocodes.creatives.LoginActivity;
import in.macrocodes.creatives.Models.SurveyModel;
import in.macrocodes.creatives.Models.TrendingProducts;
import in.macrocodes.creatives.R;
import in.macrocodes.creatives.ReportActivity;

import static android.content.Context.MODE_PRIVATE;


public class ProfileSettingsFragment extends Fragment {



    View view;
    Button startSelling;
    LinearLayout layout;
    TextView email,surveyDone;
    FirebaseUser mAuth;
    ArrayList<SurveyModel> surveyModels = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_settings, container, false);
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        startSelling = (Button) view.findViewById(R.id.startSelling);

        startSelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Coming soon", Toast.LENGTH_SHORT).show();
//                if (mAuth!=null){
//                    Toast.makeText(getContext(), "Coming soon", Toast.LENGTH_SHORT).show();
//                }else{
//                    Intent intent = new Intent(getContext(), LoginActivity.class);
//                    startActivity(intent);
//                }
            }
        });

        if (mAuth!=null){
            getUserInfo();
        }

        return view;
    }
    private void getUserInfo(){

        CustomDatabase customDatabase = new CustomDatabase() ;
        CollectionReference products  = customDatabase.getSettings().collection("Users");
        products.document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}