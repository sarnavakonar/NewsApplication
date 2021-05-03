package com.sarnava.newsapplication.ui.details;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.sarnava.newsapplication.R;
import com.sarnava.newsapplication.data.News;
import com.sarnava.newsapplication.databinding.ActivityNewsBinding;
import com.sarnava.newsapplication.ui.base.BaseActivity;
import com.sarnava.newsapplication.ui.main.WebViewActivity;

public class NewsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActivityNewsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getActivityComponent().inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        binding.collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));

        if (getIntent() != null) {

            binding.setNews((News) getIntent().getSerializableExtra("news"));

        }
        binding.readmore.setOnClickListener(v -> {
            if (isNetworkConnected()) {
                Intent intent = new Intent(v.getContext(), WebViewActivity.class);
                intent.putExtra("newsurl", (News) getIntent().getSerializableExtra("news"));
                v.getContext().startActivity(intent);
            } else {
                Toast.makeText(this, "Internet Not Available", Toast.LENGTH_SHORT).show();


            }


        });
        // binding.ivBack.setOnClickListener(v -> onBackPressed());
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}