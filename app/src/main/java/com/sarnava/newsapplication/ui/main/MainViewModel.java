package com.sarnava.newsapplication.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sarnava.newsapplication.NewsApplication;
import com.sarnava.newsapplication.data.News;
import com.sarnava.newsapplication.data.Repository;
import com.sarnava.newsapplication.data.local.DBNews;
import com.sarnava.newsapplication.di.component.DaggerViewModelComponent;
import com.sarnava.newsapplication.di.component.ViewModelComponent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<List<News>> news = new MutableLiveData<>();
    private MutableLiveData<List<DBNews>> dbNews = new MutableLiveData<>();
    private boolean shouldUpdate;

    @Inject
    Repository repository;

    @Inject
    public MainViewModel(@NonNull Application application) {
        super(application);

        ViewModelComponent viewModelComponent = DaggerViewModelComponent.builder()
                .appComponent(NewsApplication.getComponent())
                .build();

        viewModelComponent.inject(this);
    }

    public LiveData<List<News>> getNews() {
        return news;
    }

    public LiveData<List<DBNews>> getDbNews() {
        return dbNews;
    }

    public void setShouldUpdate(boolean shouldUpdate) {
        this.shouldUpdate = shouldUpdate;
    }

    public void fetchNewsFromDB(){

        dbNews = repository.fetchNewsFromDB();
    }

    public void fetchNewsFromServer(){

        news = repository.fetchNewsFromServer("us");
    }

    public void saveServerDataInDB(List<News> news){

        final List<DBNews> dbNewsList = new ArrayList<>();
        for(int i=0; i<news.size(); i++){

            DBNews dbNews = new DBNews();
            dbNews.setTitle(news.get(i).getTitle());
            dbNews.setDescription(news.get(i).getDescription());
            dbNews.setUrlToImage(news.get(i).getUrlToImage());
            dbNews.setPublishedAt(news.get(i).getPublishedAt());
            dbNews.setSource(news.get(i).getSource().getName());

            dbNewsList.add(dbNews);
        }

        if(shouldUpdate){

            repository.deleteNewsFromDbAndThenInsert(dbNewsList);
        }
        else {

            repository.insertNewsInDb(dbNewsList);
        }
    }
}
