package in.macrocodes.creatives.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import in.macrocodes.creatives.Adapter.ViewAllProductsAdapter;
import in.macrocodes.creatives.Models.TrendingProducts;
import in.macrocodes.creatives.R;

public class SearchFragment extends Fragment {


    View mMainView;
    EditText searchBox;
    ImageButton searchBtn;
    RecyclerView searchView;
    ViewAllProductsAdapter mAdapter;
    ArrayList<TrendingProducts> arrayList = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_search, container, false);
        searchBox = (EditText) mMainView.findViewById(R.id.search);
        searchBtn = (ImageButton) mMainView.findViewById(R.id.searchBtn);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        searchView = (RecyclerView) mMainView.findViewById(R.id.searchView);
        searchView.setLayoutManager(linearLayoutManager);
        mAdapter = new ViewAllProductsAdapter(getActivity(),arrayList);
        searchView.setAdapter(mAdapter);

        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if(arg1 == EditorInfo.IME_ACTION_SEARCH)
                {
                    // search pressed and perform your functionality.
                    search(searchBox.getText().toString());
                }
                return false;
            }

        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(searchBox.getText().toString());
            }
        });



        return mMainView;
    }
    private void search(String phrase){

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference products = rootRef.collection("products");
        Query query = products.whereGreaterThanOrEqualTo("name", phrase)
                .whereLessThan("name", phrase+"\uF7FF");

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                        TrendingProducts productModel = document.toObject(TrendingProducts.class);
                        arrayList.add(productModel);
                        mAdapter.notifyDataSetChanged();


                    }
                }
            }
        });
    }
}