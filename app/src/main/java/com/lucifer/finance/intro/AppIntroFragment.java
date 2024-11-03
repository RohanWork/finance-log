package com.lucifer.finance.intro;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.lucifer.finance.R;

public class AppIntroFragment extends Fragment {

    private String title;
    private String description;
    private int imageResId;
    private int position;

    public AppIntroFragment(String title, String description, int imageResId, int position) {
        this.title = title;
        this.description = description;
        this.imageResId = imageResId;
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_intro, container, false);

        TextView titleView = view.findViewById(R.id.intro_title);
        TextView descriptionView = view.findViewById(R.id.intro_description);
        ImageView imageView = view.findViewById(R.id.intro_image);

        titleView.setText(title);
        descriptionView.setText(description);
        imageView.setImageResource(imageResId);

        // Add custom animations for views
//        imageView.setAlpha(0f);
//        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(imageView, "alpha", 1f);
//        fadeIn.setDuration(1000);
//        fadeIn.start();
//
//        titleView.setTranslationY(50f);
//        titleView.animate().translationY(0).alpha(1f).setDuration(1000).start();




        // Add custom animations for views
        imageView.setAlpha(0f);
        imageView.setScaleX(0.5f); // Start with a smaller scale
        imageView.setScaleY(0.5f); // Start with a smaller scale

// Create a fade-in and scale-up animation for the ImageView
        AnimatorSet imageAnimatorSet = new AnimatorSet();
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(imageView, "alpha", 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f);
        imageAnimatorSet.playTogether(fadeIn, scaleX, scaleY);
        imageAnimatorSet.setDuration(1000);
        imageAnimatorSet.setInterpolator(new DecelerateInterpolator()); // Smooth deceleration
        imageAnimatorSet.start();

// For the titleView, add translation and fade-in with a bounce effect
        titleView.setTranslationY(50f);
        titleView.setAlpha(0f); // Start with invisible

// Create a bounce animation for the titleView
        titleView.animate()
                .translationY(0)
                .alpha(1f)
//                .setDuration(1000)
                .setInterpolator(new OvershootInterpolator()) // Bounce effect
                .start();



        // Set ImageView size for the first slide
        if (position == 0) {  // For the first slide or whichever needs resizing
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }if (position == 5) {  // For the first slide or whichever needs resizing
            imageView.setVisibility(View.GONE);
        }

        return view;
    }
}





/*
public class AppIntroFragment extends Fragment {

    private String title;
    private String description;
    private int imageResId;
    private int position;

    public AppIntroFragment(String title, String description, int imageResId, int position) {
        this.title = title;
        this.description = description;
        this.imageResId = imageResId;
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_intro, container, false);

        TextView titleView = view.findViewById(R.id.intro_title);
        TextView descriptionView = view.findViewById(R.id.intro_description);
        ImageView imageView = view.findViewById(R.id.intro_image);

        titleView.setText(title);
        descriptionView.setText(description);
        imageView.setImageResource(imageResId);

        // Set initial visibility and alpha for animations
        imageView.setAlpha(0f);
        imageView.setScaleX(0.5f);
        imageView.setScaleY(0.5f);
        titleView.setTranslationY(50f);
        titleView.setAlpha(0f);

        // Set ImageView size for the first slide
        if (position == 0) {
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } else if (position == 5) {
            imageView.setVisibility(View.GONE);
        }

        return view;
    }

    public void startAnimations() {
        View view = getView();
        if (view != null) {
            ImageView imageView = view.findViewById(R.id.intro_image);
            TextView titleView = view.findViewById(R.id.intro_title);

            // Create and start animations
            AnimatorSet imageAnimatorSet = new AnimatorSet();
            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(imageView, "alpha", 1f);
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f);
            imageAnimatorSet.playTogether(fadeIn, scaleX, scaleY);
            imageAnimatorSet.setDuration(1000);
            imageAnimatorSet.setInterpolator(new DecelerateInterpolator());
            imageAnimatorSet.start();

            titleView.animate()
                    .translationY(0)
                    .alpha(1f)
                    .setDuration(1000)
                    .setInterpolator(new OvershootInterpolator())
                    .start();
        }
    }
}
*/
