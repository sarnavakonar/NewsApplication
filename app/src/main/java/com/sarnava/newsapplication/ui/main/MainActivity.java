package com.sarnava.newsapplication.ui.main;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.sarnava.newsapplication.R;
import com.sarnava.newsapplication.data.News;
import com.sarnava.newsapplication.databinding.ActivityMainBinding;
import com.sarnava.newsapplication.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;

    @Inject
    NewsAdapter adapter;

    @Inject
    MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        getActivityComponent().inject(this);

        setupAdapter();
        fetchFromDB();
    }

    private void setupAdapter(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rv.setLayoutManager(linearLayoutManager);
        binding.rv.setItemAnimator(new DefaultItemAnimator());
        binding.rv.setAdapter(adapter);
    }

    private void fetchFromDB(){

        viewModel.fetchNewsFromDB();

        viewModel.getDbNews().observe(this, dbNews -> {

            if (dbNews.size() > 0) {
                viewModel.setShouldUpdate(true);

                List<News> fetchedDromDB = new ArrayList<>();
                for (int i = 0; i < dbNews.size(); i++) {

                    News db_news = new News();
                    db_news.setTitle(dbNews.get(i).getTitle());
                    db_news.setDescription(dbNews.get(i).getDescription());
                    db_news.setUrlToImage(dbNews.get(i).getUrlToImage());
                    db_news.setPublishedAt(dbNews.get(i).getPublishedAt());

                    News.Source source = new News.Source();
                    source.setName(dbNews.get(i).getSource());
                    db_news.setSource(source);

                    fetchedDromDB.add(db_news);
                }

                adapter.setNews(fetchedDromDB);
            }
            fetchFromServer();
        });
    }

    private void fetchFromServer(){

        binding.pb.setVisibility(View.VISIBLE);
        viewModel.fetchNewsFromServer();

        viewModel.getNews().observe(this, news -> {

            binding.pb.setVisibility(View.GONE);
            if(news != null && news.size()>0){

                adapter.setNews(news);
                viewModel.saveServerDataInDB(news);
            }
            else {

                Toast.makeText(MainActivity.this, "Unable to refresh feed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}