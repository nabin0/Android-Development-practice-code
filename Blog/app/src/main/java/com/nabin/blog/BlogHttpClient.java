package com.nabin.blog;

import android.util.Log;

import com.google.gson.Gson;
import com.nabin.blog.BlogData.BlogData;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public final class BlogHttpClient {
    private Executor executor;
    private OkHttpClient client;
    private Gson gson;

    public static final BlogHttpClient  CLIENT_INSTANCE =  new BlogHttpClient();

    public static final String BASE_URL = "https://bitbucket.org/dmytrodanylyk/travel-blog-resources";
    public static final String PATH = "/raw/3eede691af3e8ff795bf6d31effb873d484877be";
    public static final String BLOG_ARTICLE_URL = BASE_URL + PATH + "/blog_articles.json" ;

    private BlogHttpClient(){
        executor = Executors.newFixedThreadPool(4);
        client = new OkHttpClient();
        gson = new Gson();
    }

    public void loadBlogArticles(BlogArticlesCallback callback){
        Request request = new Request.Builder()
                .get()
                .url(BLOG_ARTICLE_URL)
                .build();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    ResponseBody responseBody = response.body();
                    if(responseBody != null){
                        String json = responseBody.string();
                        BlogData blogData = gson.fromJson(json, BlogData.class);

                        if(blogData != null){
                            callback.onSuccess(blogData.getData());
                            return;
                        }
                    }
                } catch (IOException e) {
                    Log.e("BlogHttpClient", "Error loading blog articles"+ e );
                }

                callback.onError();
            }
        });
    }

}
