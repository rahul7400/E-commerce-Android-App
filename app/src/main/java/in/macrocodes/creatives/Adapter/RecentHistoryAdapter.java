package in.macrocodes.creatives.Adapter;

import android.content.Context;
import android.util.Log;
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

import in.macrocodes.creatives.DatabaseHelper.DatabaseHelper;
import in.macrocodes.creatives.Fragments.ProductInfoBottom;
import in.macrocodes.creatives.Models.TrendingProducts;
import in.macrocodes.creatives.R;

public class RecentHistoryAdapter extends RecyclerView.Adapter<RecentHistoryAdapter.Viewholder> {
    private ArrayList<TrendingProducts> recentHistory = new ArrayList<>();
    private final Context context;
    int click =0;
    DatabaseHelper databaseHelper;
    FragmentManager fragmentManager;

    public RecentHistoryAdapter(ArrayList<TrendingProducts> recentHistory, DatabaseHelper databaseHelper, Context context, androidx.fragment.app.FragmentManager fragmentManager) {
        this.recentHistory = recentHistory;
        this.context = context;
        this.databaseHelper=databaseHelper;
        this.fragmentManager = fragmentManager;
    }




    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_layout,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  RecentHistoryAdapter.Viewholder holder, int position) {

        TrendingProducts trendingProducts = recentHistory.get(position);

        holder.pName.setText(trendingProducts.getName());
        holder.pPrice.setText(trendingProducts.getPrice());
        holder.pDesc.setText(trendingProducts.getDescription());

        Picasso.get().load(trendingProducts.getImage())
                .into(holder.pImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductInfoBottom bottomSheet = new ProductInfoBottom(context, trendingProducts);
                bottomSheet.show(fragmentManager, "model");
            }
        });


    }

    @Override
    public int getItemCount() {
        return recentHistory.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder{
        private TextView pName,pDesc,pPrice;
        private ImageView pImage;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            pName = (TextView) itemView.findViewById(R.id.pName);
            pPrice = (TextView) itemView.findViewById(R.id.pPrice);
            pDesc = (TextView) itemView.findViewById(R.id.pDesc);
            pImage = (ImageView) itemView.findViewById(R.id.pImage);

        }
    }
}
