package com.teddy.catatharimu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

// Eddy Rochman
// 10120052
// IF-2
public class OnboardingAdapter extends PagerAdapter {
    static class ViewHolder {
        ImageView sliderImage;
        TextView sliderTitle;
        TextView sliderDesc;
    }

    Context context;
    int sliderAllImage[] = {
            R.drawable.welcome1,
            R.drawable.welcome2,
            R.drawable.welcome3,
    };

    int sliderAllTitle[] = {
            R.string.screen1,
            R.string.screen2,
            R.string.screen3,
    };

    int sliderAllDesc[] = {
            R.string.screen1desc,
            R.string.screen2desc,
            R.string.screen3desc,
    };

    public OnboardingAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return sliderAllTitle.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_onboarding_list, container, false);

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.sliderImage = view.findViewById(R.id.sliderImage);
        viewHolder.sliderTitle = view.findViewById(R.id.sliderTitle);
        viewHolder.sliderDesc = view.findViewById(R.id.sliderDesc);
        view.setTag(viewHolder);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.sliderImage.setImageResource(sliderAllImage[position]);
        holder.sliderTitle.setText(context.getString(sliderAllTitle[position]));
        holder.sliderDesc.setText(context.getString(sliderAllDesc[position]));

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
