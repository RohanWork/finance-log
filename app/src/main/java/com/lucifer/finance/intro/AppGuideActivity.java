package com.lucifer.finance.intro;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroPageTransformerType;
import com.lucifer.finance.MainActivity;
import com.lucifer.finance.R;

public class AppGuideActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*
        addSlide(new AppIntroFragment("Welcome to Finance Log", "Manage your finances easily and effectively", R.drawable.logo5, 0));
        addSlide(new AppIntroFragment("Track Transactions", "Keep track of your credited and debited amounts.", R.drawable.dashboard, 1));
        addSlide(new AppIntroFragment("Transaction Log", "View the number of transactions per bank with total cash flow.", R.drawable.translog, 2));
        addSlide(new AppIntroFragment("Transaction History", "Access detailed transaction history, including date, time, and amount.", R.drawable.transhistory, 3));
        addSlide(new AppIntroFragment("Profile Management", "Manage your profile data, including image, first name, and last name.", R.drawable.userprofile2, 4));
        addSlide(new AppIntroFragment("Get Started", "Let's dive in and start managing your finances!", 0, 5));
*/
        addSlide(new AppIntroFragment("Welcome to Finance Log", "Your go-to app for effective financial management.", R.drawable.logo5, 0));
        addSlide(new AppIntroFragment("Track Transactions", "Effortlessly record your financial transactions in one place.", R.drawable.dashboard, 1));
        addSlide(new AppIntroFragment("Transaction Log", "Analyze your transactions per bank to understand your cash flow better.", R.drawable.translog, 2));
        addSlide(new AppIntroFragment("Transaction History", "Easily navigate through your transaction history for detailed insights.", R.drawable.transhistory, 3));
        addSlide(new AppIntroFragment("Profile Management", "Customize your profile to reflect your personal brand.", R.drawable.userprofile2, 4));
        addSlide(new AppIntroFragment("Get Started", "Ready to take control of your finances? Let's get started!", 0, 5));

        // Set custom text appearance for Skip and Done buttons
        setSkipTextAppearance(R.style.CustomButtonText);
        setDoneTextAppearance(R.style.CustomButtonText);

        // Set the animation for slide transitions
//        setTransformer(new FadePageTransformer()); // Change this to the desired effect
        setTransformer(AppIntroPageTransformerType.Flow.INSTANCE);
//        setTransformer(AppIntroPageTransformerType.Custom(new FadePageTransformer()));


    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Handle skip action, e.g., finish the activity
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Handle done action, e.g., go to MainActivity
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        finish();
    }
}





