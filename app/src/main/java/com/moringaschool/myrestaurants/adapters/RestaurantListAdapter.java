package com.moringaschool.myrestaurants.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.moringaschool.myrestaurants.Constants;
import com.moringaschool.myrestaurants.R;
import com.moringaschool.myrestaurants.models.Business;
import com.moringaschool.myrestaurants.ui.RestaurantDetailActivity;
import com.moringaschool.myrestaurants.ui.RestaurantDetailFragment;
import com.moringaschool.myrestaurants.util.OnRestaurantSelectedListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder> {

    private List<Business> mRestaurants;
    private Context mContext;
    private OnRestaurantSelectedListener mOnRestaurantSelectedListener;

    public RestaurantListAdapter(Context context, List<Business> restaurants, OnRestaurantSelectedListener restaurantSelectedListener) {
        mContext = context;
        mRestaurants = restaurants;
        mOnRestaurantSelectedListener = restaurantSelectedListener;
    }

    @Override
    public RestaurantListAdapter.RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list_item,parent,false);
        RestaurantViewHolder viewHolder = new RestaurantViewHolder(view, mRestaurants, mOnRestaurantSelectedListener    );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder (RestaurantListAdapter.RestaurantViewHolder holder, int position) {
        holder.bindRestaurant(mRestaurants.get(position));
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.restaurantImageView) ImageView mRestaurantImageView;
        @BindView(R.id.restaurantNameTextView) TextView mNameTextView;
        @BindView(R.id.categoryTextView) TextView mCategoryTextView;
        @BindView(R.id.ratingTextView) TextView mRatingTextView;

        private Context mContext;
        private int mOrientation;

        public RestaurantViewHolder(View itemView, List<Business> restaurants, OnRestaurantSelectedListener restaurantSelectedListener) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            mContext = itemView.getContext();
            mRestaurants = restaurants;
            mOnRestaurantSelectedListener = restaurantSelectedListener;

            //determines the current orientation
            mOrientation = itemView.getResources().getConfiguration().orientation;

            if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                createDetailFragment(0);
            }

            itemView.setOnClickListener(this);
        }

        public void bindRestaurant(Business restaurant) {
            Picasso.get().load(restaurant.getImageUrl()).into(mRestaurantImageView);
            mNameTextView.setText(restaurant.getName());
            mCategoryTextView.setText(restaurant.getCategories().get(0).getTitle());
            mRatingTextView.setText("Rating: " + restaurant.getRating() + "/5");
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();
            mOnRestaurantSelectedListener.onRestaurantSelected(itemPosition, mRestaurants, Constants.SOURCE_FIND);

            if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                createDetailFragment(itemPosition);
            }
            else {
                Intent intent = new Intent(mContext, RestaurantDetailActivity.class);
                intent.putExtra(Constants.EXTRA_KEY_POSITION, itemPosition);
                intent.putExtra(Constants.EXTRA_KEY_RESTAURANTS, Parcels.wrap(mRestaurants));
                intent.putExtra(Constants.KEY_SOURCE, Constants.SOURCE_FIND);
                mContext.startActivity(intent);
            }
        }

        private void createDetailFragment(int position) {
            RestaurantDetailFragment detailFragment = RestaurantDetailFragment.newInstance(mRestaurants, position, Constants.SOURCE_FIND);

            FragmentTransaction ft = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.restaurantDetailContainer, detailFragment);
            ft.commit();
        }
    }
}
