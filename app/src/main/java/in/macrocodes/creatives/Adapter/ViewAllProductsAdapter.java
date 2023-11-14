package in.macrocodes.creatives.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.macrocodes.creatives.BuildConfig;
import in.macrocodes.creatives.Fragments.HomeFragment;
import in.macrocodes.creatives.Fragments.ProductInfoBottom;
import in.macrocodes.creatives.Models.TrendingProducts;
import in.macrocodes.creatives.R;
import in.macrocodes.creatives.ViewAllProductsActivity;

public class ViewAllProductsAdapter extends RecyclerView.Adapter<ViewAllProductsAdapter.Viewholder> {
    ArrayList<TrendingProducts> products = new ArrayList<>();
    Context context;

    public ViewAllProductsAdapter(ViewAllProductsActivity viewAllProductsActivity, ArrayList<TrendingProducts> products) {
        context = viewAllProductsActivity;
        this.products = products;

    }

    public ViewAllProductsAdapter(FragmentActivity activity, ArrayList<TrendingProducts> arrayList) {
        this.context = activity;
        this.products = arrayList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_all_products,parent,false);


        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewAllProductsAdapter.Viewholder holder, int position) {

        TrendingProducts model = products.get(position);
        holder.productName.setText(model.getName());
        holder.productPrice.setText(model.getPrice());
        holder.productDescription.setText(model.getDescription());
        Picasso.get().load(model.getImage())
                .into(holder.productImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                ProductInfoBottom bottomSheet = new ProductInfoBottom(context, model, HomeFragment.historyUpdated);
                bottomSheet.show(manager, "ModalBottomSheet");
            }
        });
        holder.productShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Download the Crefti App from the google play store in order to view the product : https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder{

        TextView productName,productPrice,productDescription;
        ImageView productImage,productShare;
        public Viewholder(@NonNull  View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.pName);
            productDescription = (TextView) itemView.findViewById(R.id.pDesc);
            productPrice = (TextView) itemView.findViewById(R.id.pPrice);
            productImage = (ImageView) itemView.findViewById(R.id.pImage);
            productShare = (ImageView) itemView.findViewById(R.id.shareBtn);
        }
    }
}
