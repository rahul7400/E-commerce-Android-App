package in.macrocodes.creatives;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import in.macrocodes.creatives.Adapter.ViewAllProductGridADapter;
import in.macrocodes.creatives.Adapter.ViewAllProductsAdapter;
import in.macrocodes.creatives.Models.TrendingProducts;
import in.macrocodes.creatives.databinding.ActivityViewAllProductsBinding;

public class ViewAllProductsActivity extends AppCompatActivity {

    String type;
    int limit =10;
    DocumentSnapshot lastVisible;
    ActivityViewAllProductsBinding binding;
    private boolean isScrolling = false;
    ArrayList<TrendingProducts> products = new ArrayList<>();
    ArrayList<TrendingProducts> locals = new ArrayList<>();
    ArrayList<TrendingProducts> categoryList = new ArrayList<>();
    private boolean isLastItemReached = false;
    ViewAllProductsAdapter productAdapter;
    ViewAllProductGridADapter gridADapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_view_all_products);


        type = getIntent().getStringExtra("type");
        if (type.equalsIgnoreCase("all")){

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            binding.allProductsView.setLayoutManager(linearLayoutManager);
            getLocalsProducts();
            productAdapter = new ViewAllProductsAdapter(this,products);
            binding.allProductsView.setAdapter(productAdapter);
            binding.title.setText("Handmade with ❤");

        }else if (type.equalsIgnoreCase("local")){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
            binding.allProductsView.setLayoutManager(gridLayoutManager);
            locals = getIntent().getParcelableArrayListExtra("list");
            gridADapter = new ViewAllProductGridADapter(this,locals);
            binding.allProductsView.setAdapter(gridADapter);
            binding.title.setText("Support the locals ❤");
        }
        else{
            String category = getIntent().getStringExtra("category");
            getCategoryProducts(category);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
            binding.allProductsView.setLayoutManager(gridLayoutManager);
            gridADapter = new ViewAllProductGridADapter(this,categoryList);
            binding.allProductsView.setAdapter(gridADapter);

            binding.title.setText(category+" ❤");
        }


    }


    private void getCategoryProducts(String category){

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference products = rootRef.collection("products");

        Query query = products.whereEqualTo("category", category);


//        if (category.equalsIgnoreCase("ayurvedic"))
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                        TrendingProducts productModel = document.toObject(TrendingProducts.class);
                        categoryList.add(productModel);


                    }
                    gridADapter.notifyDataSetChanged();
                }
            }
        });



    }
    private void getLocalsProducts() {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference productsRef = rootRef.collection("products");
        Query query = productsRef.orderBy("name", Query.Direction.ASCENDING).limit(limit);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        TrendingProducts productModel = document.toObject(TrendingProducts.class);
                        products.add(productModel);
                        assert productModel != null;

                    }
                    productAdapter.notifyDataSetChanged();
                    lastVisible = task.getResult().getDocuments().get(task.getResult().getDocuments().size() - 1);

                    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                isScrolling = true;
//                                binding.progressBar.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                            int visibleItemCount = linearLayoutManager.getChildCount();
                            int totalItemCount = linearLayoutManager.getItemCount();

                            if (isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {
                                isScrolling = false;
                                binding.progressBar.setVisibility(View.GONE);

                                Query nextQuery = productsRef.orderBy("name", Query.Direction.ASCENDING)
                                        .startAfter(lastVisible).limit(limit);
                                nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                        if (t.isSuccessful()) {
                                            for (DocumentSnapshot d : Objects.requireNonNull(t.getResult())) {
                                                TrendingProducts productModel = d.toObject(TrendingProducts.class);
                                                products.add(productModel);
                                            }
                                            productAdapter.notifyDataSetChanged();
                                            if(t.getResult().size()!=0)
                                             lastVisible = t.getResult().getDocuments().get(t.getResult().size() - 1);

                                            if (t.getResult().size() < limit) {
                                                isLastItemReached = true;
                                                binding.progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    };
                    binding.allProductsView.addOnScrollListener(onScrollListener);
                }
            }
        });
    }


}