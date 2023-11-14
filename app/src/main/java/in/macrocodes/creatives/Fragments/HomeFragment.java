package in.macrocodes.creatives.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.macrocodes.creatives.Adapter.AllProductsAdapter;
import in.macrocodes.creatives.Adapter.CartAdapter;
import in.macrocodes.creatives.Adapter.CategoriesAdapter;
import in.macrocodes.creatives.Adapter.RecentHistoryAdapter;
import in.macrocodes.creatives.Adapter.TrendingRecyclerAdapter;
import in.macrocodes.creatives.CustomDatabase;
import in.macrocodes.creatives.DatabaseHelper.DatabaseHelper;
import in.macrocodes.creatives.DatabaseHelper.ProductHistory;
import in.macrocodes.creatives.Interfaces.HistoryUpdated;
import in.macrocodes.creatives.Models.BannerModel;
import in.macrocodes.creatives.Models.CategoriesModel;
import in.macrocodes.creatives.Models.TrendingProducts;
import in.macrocodes.creatives.R;
import in.macrocodes.creatives.ViewAllProductsActivity;
import io.realm.mongodb.mongo.MongoDatabase;

public class HomeFragment extends Fragment implements HistoryUpdated {


    ProductHistory productHistory;
    LinearLayout noHistoryImage;
    View mMainView;
    Button viewAllBtn,viewlocalAllBtn;
    RecyclerView mTrendingView,mProductsAcrossView,mCategoriesView,mLocalViews,mRecentView;
    MongoDatabase mongoDatabase;
    ImageView bannerImage,midbannerImage;
    TrendingRecyclerAdapter mAdapter;

    NestedScrollView nestedScrollView;

    public static HistoryUpdated historyUpdated;
    AllProductsAdapter mAdapter2;
    AllProductsAdapter mAdapter4;
    CategoriesAdapter mAdapter3;
    RecentHistoryAdapter mRecentAdapter;


    ArrayList<TrendingProducts>trendingList = new ArrayList<>();
    ArrayList<TrendingProducts>abannerHomellProducts = new ArrayList<>();
    ArrayList<CategoriesModel>categoriesList = new ArrayList<>();
    ArrayList<TrendingProducts>locals = new ArrayList<>();
    ArrayList<TrendingProducts>recentHistory = new ArrayList<>();
    ArrayList<TrendingProducts>allProducts = new ArrayList<>();

    ArrayList<String>ranks = new ArrayList<>();
    ArrayList<BannerModel>banners = new ArrayList<>();
    Button clearHistory;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initialize(){
        mTrendingView = (RecyclerView) mMainView.findViewById(R.id.trendingView);
        mProductsAcrossView = (RecyclerView) mMainView.findViewById(R.id.productsAcrossIndia);
        mCategoriesView = (RecyclerView) mMainView.findViewById(R.id.categoriesView);
        mRecentView = (RecyclerView) mMainView.findViewById(R.id.recentProduct);
        mLocalViews = (RecyclerView) mMainView.findViewById(R.id.suportLocals);
        bannerImage = (ImageView) mMainView.findViewById(R.id.bannerHome);
        midbannerImage = (ImageView) mMainView.findViewById(R.id.midBanner);
        clearHistory = (Button) mMainView.findViewById(R.id.clearHistory);
        noHistoryImage = (LinearLayout) mMainView.findViewById(R.id.noHistory);
        viewAllBtn = (Button) mMainView.findViewById(R.id.viewAllBtn);
        viewlocalAllBtn = (Button) mMainView.findViewById(R.id.viewlocalsBtn);
        nestedScrollView = (NestedScrollView) mMainView.findViewById(R.id.nestedScroll);
        historyUpdated = (HistoryUpdated) this;


        midbannerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:macrocodes7400@gmail.com")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_SUBJECT, "here is my valueable suggestion for you.");
                if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        bannerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:macrocodes7400@gmail.com")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_SUBJECT, "here is my valueable suggestion for you.");
                if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_home, container, false);
        initialize();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getBanner();
                addCategories();
                getTrendingData();
                getAllProducts();

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
                mAdapter = new TrendingRecyclerAdapter(trendingList,getContext(),requireActivity().getSupportFragmentManager(),historyUpdated);
                mTrendingView.setLayoutManager(linearLayoutManager);
                mTrendingView.setAdapter(mAdapter);

                //products accross india adapter
                mProductsAcrossView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                mAdapter2 = new AllProductsAdapter(allProducts,getContext(),requireActivity().getSupportFragmentManager(),historyUpdated);
                mProductsAcrossView.setAdapter(mAdapter2);



                //categories adapter
                LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
                mCategoriesView.setLayoutManager(linearLayoutManager2);
                mAdapter3 = new CategoriesAdapter(categoriesList,getContext());
                mCategoriesView.setAdapter(mAdapter3);


                //support locals adapter
                mAdapter4 = new AllProductsAdapter(locals,getContext(),requireActivity().getSupportFragmentManager(),historyUpdated);
                mLocalViews.setLayoutManager(new GridLayoutManager(getContext(), 2));
                mLocalViews.setAdapter(mAdapter4);

                //Recent Product History
                productHistory = new ProductHistory(getContext());
                recentHistory = productHistory.getAllData();
                DatabaseHelper databaseHelper = new DatabaseHelper(getContext());

                mRecentAdapter = new RecentHistoryAdapter(recentHistory,databaseHelper,getContext(),getFragmentManager());
                LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(getContext());
                mRecentView.setLayoutManager(linearLayoutManager4);
                mRecentView.setAdapter(mRecentAdapter);

            }
        },1000);
        //trending


        if (recentHistory.size() == 0){
            noHistoryImage.setVisibility(View.VISIBLE);
        }else{
            noHistoryImage.setVisibility(View.GONE);
        }

        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productHistory.deleteRow();
                recentHistory.clear();
                noHistoryImage.setVisibility(View.VISIBLE);
                mRecentAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Product view history cleared", Toast.LENGTH_SHORT).show();
            }
        });

        viewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllProductsActivity.class);
                intent.putExtra("type","all");
                startActivity(intent);
            }
        });
        viewlocalAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAllProductsActivity.class);
                intent.putParcelableArrayListExtra("list",locals);
                intent.putExtra("type","local");
                startActivity(intent);
            }
        });


        return mMainView;
    }
    private void getBanner(){
        Picasso.get().load(R.drawable.poster1)
                .placeholder(R.drawable.img3)
                .into(bannerImage);
//        CustomDatabase customDatabase = new CustomDatabase() ;
//        CollectionReference products  = customDatabase.getSettings().collection("banner");
//        products.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for (QueryDocumentSnapshot datasnapshot : queryDocumentSnapshots){
//                    BannerModel bannerModel = datasnapshot.toObject(BannerModel.class);
//                    banners.add(bannerModel);
//
//
//
//                }
//            }
//        });

//        if (banners.size() == 1){
//
//
//                String nav = banners.get(0).getNavigate();
//                bannerImage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (nav.equalsIgnoreCase("inApp")){
//                            //Query for searching product with id
//                        }else{
//                            //Search in web
//                        }
//                    }
//                });
//        }
    }
    private void getLocalProducts(){
        CustomDatabase customDatabase = new CustomDatabase() ;
        CollectionReference products  = customDatabase.getSettings().collection("locals");
        products.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshots) {
                for (QueryDocumentSnapshot datasnapshot : snapshots){
                    TrendingProducts trendingProducts1 = datasnapshot.toObject(TrendingProducts.class);
                    locals.add(trendingProducts1);
                    mAdapter4.notifyDataSetChanged();

                }

            }
        });
    }
    private void getAllProducts(){
        allProducts.clear();
        CustomDatabase customDatabase = new CustomDatabase() ;
        CollectionReference products  = customDatabase.getSettings().collection("Trending");
        products.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshots) {
                for (QueryDocumentSnapshot datasnapshot : snapshots){
                    TrendingProducts trendingProducts1 = datasnapshot.toObject(TrendingProducts.class);
                    allProducts.add(trendingProducts1);
                    mAdapter2.notifyDataSetChanged();

                }

            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                getLocalProducts();
            }
        });
    }
    private void addCategories(){

        categoriesList.clear();
        categoriesList.add( new CategoriesModel("https://m.media-amazon.com/images/I/81oBlS3rKXL._UY575_.jpg","Jewellery"));
        categoriesList.add( new CategoriesModel("https://images-eu.ssl-images-amazon.com/images/I/41lICpaGo9L._SX300_SY300_QL70_FMwebp_.jpg","Home Decor"));
        categoriesList.add( new CategoriesModel("https://images-eu.ssl-images-amazon.com/images/I/41wKsI9yrZL._SY300_SX300_QL70_FMwebp_.jpg ","Ayurvedic"));
        categoriesList.add( new CategoriesModel("https://m.media-amazon.com/images/I/911EKUNq1+L._SL1500_.jpg","Furniture"));

    }
    private void getTrendingData(){

        trendingList.clear();
        CustomDatabase customDatabase = new CustomDatabase() ;
        CollectionReference products  = customDatabase.getSettings().collection("Trending");
        products.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshots) {
                for (QueryDocumentSnapshot datasnapshot : snapshots){
                    TrendingProducts trendingProducts1 = datasnapshot.toObject(TrendingProducts.class);
                    trendingList.add(trendingProducts1);
                    mAdapter.notifyDataSetChanged();

                }

            }
        });
    }


    @Override
    public void getUpdateResult(boolean isUpdated) {
        if (isUpdated){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recentHistory.clear();
                    recentHistory.addAll(productHistory.getAllData());
                    mRecentAdapter.notifyDataSetChanged();
                    if(recentHistory.size() > 0)
                        noHistoryImage.setVisibility(View.GONE);
                }
            },1000);
        }
    }
}
