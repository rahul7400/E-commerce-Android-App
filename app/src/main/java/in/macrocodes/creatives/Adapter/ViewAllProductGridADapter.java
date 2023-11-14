package in.macrocodes.creatives.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.macrocodes.creatives.Fragments.HomeFragment;
import in.macrocodes.creatives.Fragments.ProductInfoBottom;
import in.macrocodes.creatives.Models.TrendingProducts;
import in.macrocodes.creatives.R;
import in.macrocodes.creatives.ViewAllProductsActivity;

public class ViewAllProductGridADapter extends RecyclerView.Adapter<ViewAllProductGridADapter.Viewholder> {
    Context context;
    ArrayList<TrendingProducts> locals = new ArrayList<>();
    public ViewAllProductGridADapter(ViewAllProductsActivity viewAllProductsActivity, ArrayList<TrendingProducts> locals) {
        context = viewAllProductsActivity;
        this.locals=locals;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_product_layout,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        TrendingProducts model = locals.get(position);

        holder.productPrice.setText(model.getPrice());
        holder.productName.setText(model.getName());
        holder.productDesc.setText(model.getDescription());
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
    }

    @Override
    public int getItemCount() {
        return locals.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder{

        TextView productName,productPrice,productDesc;
        ImageView productImage;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.productName);
            productDesc = (TextView) itemView.findViewById(R.id.productDesc2);
            productPrice = (TextView) itemView.findViewById(R.id.productPrice);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
        }
    }
}
