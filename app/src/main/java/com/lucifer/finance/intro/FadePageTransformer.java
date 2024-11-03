package com.lucifer.finance.intro;

import android.view.View;
import androidx.viewpager2.widget.ViewPager2;

public class FadePageTransformer implements ViewPager2.PageTransformer {
    @Override
    public void transformPage(View page, float position) {
        page.setTranslationX(-position * page.getWidth());
        page.setAlpha(1 - Math.abs(position));
    }
}
