package in.macrocodes.creatives.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.macrocodes.creatives.BuildConfig;
import in.macrocodes.creatives.CartActivity;
import in.macrocodes.creatives.DatabaseHelper.DatabaseHelper;
import in.macrocodes.creatives.Fragments.ProductInfoBottom;
import in.macrocodes.creatives.Models.CartSize;
import in.macrocodes.creatives.Models.TrendingProducts;
import in.macrocodes.creatives.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Viewholder> {
    Context mContext;
    int click =1 ;
    FragmentManager fragmentManager;
    DatabaseHelper databaseHelper;

    ArrayList<TrendingProducts> arrayList = new ArrayList<>();
    public CartAdapter(CartActivity cartActivity, ArrayList<TrendingProducts> arrayList, DatabaseHelper databaseHelper, FragmentManager fragmentManager) {
        mContext = cartActivity;
        this.fragmentManager=fragmentManager;
        this.arrayList=arrayList;
        this.databaseHelper=databaseHelper;
    }

    @NonNull

    @Override
    public Viewholder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  CartAdapter.Viewholder holder, int position) {

        TrendingProducts trendingProducts = arrayList.get(position);

        holder.pName.setText(trendingProducts.getName());
        holder.pPrice.setText(trendingProducts.getPrice());
        holder.pDesc.setText(trendingProducts.getDescription());

        Picasso.get().load(trendingProducts.getImage())
                .into(holder.pImage);

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click ==0 ){
                    holder.likeBtn.setImageResource(R.drawable.heart_filled2);
                    ++click;
                    if(databaseHelper.addText(trendingProducts)){

                        Toast.makeText(mContext, "Added to cart", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    holder.likeBtn.setImageResource(R.drawable.heart2);
                    --click;
                    if(databaseHelper.deleteRow(trendingProducts.getId())){

                        Toast.makeText(mContext, "Removed from cart", Toast.LENGTH_SHORT).show();
                    }



                }

            }
        });
        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Download the Crefti App from the google play store in order to view the product : https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                mContext.startActivity(sendIntent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductInfoBottom bottomSheet = new ProductInfoBottom(mContext, trendingProducts);
                bottomSheet.show(fragmentManager, "ModalBottomSheet");
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder{

        private TextView pName,pDesc,pPrice;
        private ImageView pImage,likeBtn,shareBtn;
        public Viewholder(@NonNull  View itemView) {
            super(itemView);
            pName = (TextView) itemView.findViewById(R.id.pName);
            pPrice = (TextView) itemView.findViewById(R.id.pPrice);
            pDesc = (TextView) itemView.findViewById(R.id.pDesc);
            pImage = (ImageView) itemView.findViewById(R.id.pImage);
            likeBtn = (ImageView) itemView.findViewById(R.id.likeBtn);
            shareBtn = (ImageView) itemView.findViewById(R.id.shareBtn);
        }
    }
}
