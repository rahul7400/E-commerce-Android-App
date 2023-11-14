package in.macrocodes.creatives.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import in.macrocodes.creatives.BuyActivity;
import in.macrocodes.creatives.CustomDatabase;
import in.macrocodes.creatives.DatabaseHelper.DatabaseHelper;
import in.macrocodes.creatives.DatabaseHelper.ProductHistory;
import in.macrocodes.creatives.ImageViewActivity;
import in.macrocodes.creatives.Interfaces.HistoryUpdated;
import in.macrocodes.creatives.LoginActivity;
import in.macrocodes.creatives.Models.TrendingProducts;
import in.macrocodes.creatives.R;
import in.macrocodes.creatives.ReportActivity;

public class ProductInfoBottom extends BottomSheetDialogFragment {
    private  Context mContext;
    private TrendingProducts trendingProducts;
    private ArrayList<TrendingProducts>savedProducts = new ArrayList<>();
    ImageView imageView,imageView2;
    ImageView like;
    Button buyBtn;
    DatabaseHelper databaseHelper;
    ProductHistory productHistory;
    RelativeLayout parent;
    Button report;
    int click =0 ;
    CardView amazonIcon;
    FirebaseUser mAuth;

    HistoryUpdated historyUpdated;
    TextView productName,productPrice,productDesc;
    public ProductInfoBottom(Context mContext, TrendingProducts trendingProducts, HistoryUpdated historyUpdated) {
        this.mContext=mContext;
        this.trendingProducts = trendingProducts;
        this.historyUpdated=historyUpdated;
    }

    public ProductInfoBottom(Context mContext, TrendingProducts trendingProducts) {
        this.mContext=mContext;
        this.trendingProducts = trendingProducts;
        this.historyUpdated=null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_info_bottom,container);

        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        imageView = view.findViewById(R.id.mainImage);
        imageView2 = view.findViewById(R.id.img1);
        productName = (TextView) view.findViewById(R.id.pName);
        productPrice = (TextView) view.findViewById(R.id.pPrice);
        parent = (RelativeLayout) view.findViewById(R.id.parent);
        productDesc = (TextView) view.findViewById(R.id.pDesc);
        like = (ImageView) view.findViewById(R.id.like);
        report = (Button) view.findViewById(R.id.productReport);
        buyBtn = (Button) view.findViewById(R.id.buyBtn);
        amazonIcon = (CardView) view.findViewById(R.id.amazonIcon);

        databaseHelper = new DatabaseHelper(getContext());
        productHistory = new ProductHistory(getContext());

        ArrayList<TrendingProducts> arrayList = new ArrayList<>();
        arrayList = databaseHelper.getAllData();
        for (TrendingProducts trendingProducts1 : arrayList){
            if (trendingProducts1.getId().equalsIgnoreCase(trendingProducts.getId())){
                like.setImageResource(R.drawable.heart_filled2);
                ++click;
            }
        }

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trendingProducts.getAmazon()));
                startActivity(browserIntent);

//                if (mAuth != null){
//                    Intent intent = new Intent(getContext(), BuyActivity.class);
//                    startActivity(intent);
//                }else{
//                    Intent intent = new Intent(getContext(), LoginActivity.class);
//                    startActivity(intent);
//                }

            }
        });
        amazonIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trendingProducts.getAmazon()));
                startActivity(browserIntent);
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click ==0 ){
                    like.setImageResource(R.drawable.heart_filled2);
                    ++click;
                    if(databaseHelper.addText(trendingProducts))
                        Toast.makeText(mContext, "Added to cart", Toast.LENGTH_SHORT).show();

                }else{
                    like.setImageResource(R.drawable.heart2);
                    --click;
                     if(databaseHelper.deleteRow(trendingProducts.getId()))
                         Toast.makeText(mContext, "Removed from cart", Toast.LENGTH_SHORT).show();


                }

            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ReportActivity.class);
                intent.putExtra("product", trendingProducts);
                startActivity(intent);
            }
        });

        reported();
        addInHistory();

        productName.setText(trendingProducts.getName());
        productPrice.setText(trendingProducts.getPrice());
        productDesc.setText(trendingProducts.getDescription());

        Picasso.get().load(trendingProducts.getImage()).into(imageView);
        Picasso.get().load(trendingProducts.getImage()).into(imageView2);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ImageViewActivity.class);
                intent.putExtra("uri",trendingProducts.getImage());
                startActivity(intent);

            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ImageViewActivity.class);
                intent.putExtra("uri",trendingProducts.getImage());
                startActivity(intent);
            }
        });

        return view;
    }
    public void reported(){
        CustomDatabase customDatabase = new CustomDatabase() ;
        CollectionReference products  = customDatabase.getSettings().collection("reports");
        products.document(trendingProducts.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String solved = documentSnapshot.getString("solved");
                    if (solved.equalsIgnoreCase("false")){
                        report.setText("In Review");
                        report.setEnabled(false);
                    }else{
                        report.setEnabled(true);
                    }
                }

            }
        });
    }

    public void addInHistory(){
        if (historyUpdated!=null){
            ArrayList<TrendingProducts>products = productHistory.getAllData();
            historyUpdated.getUpdateResult(true);
            if (products.size() < 4){

                productHistory.addProducts(trendingProducts);
            }else{
                int random = ThreadLocalRandom.current().nextInt(0, 3);
                productHistory.updateProducts(trendingProducts,random);
            }
        }
    }

}
