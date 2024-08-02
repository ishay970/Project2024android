package com.example.youtube.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.youtube.MyApplication;
import com.example.youtube.R;
import com.example.youtube.dao.VideoDao;
import com.example.youtube.entities.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoAPI {

    private MutableLiveData<List<Video>> videoListData;
    private VideoDao dao;
    Retrofit retrofit;
    videoWebServiceAPI webServiceAPI;

    public VideoAPI(MutableLiveData<List<Video>> videoListData, VideoDao dao) {
        this.videoListData = videoListData;
        this.dao = dao;
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create()).build();
        webServiceAPI = retrofit.create(videoWebServiceAPI.class);
    }

    public void getAllVideos(MutableLiveData<List<Video>> videos) {
        Call<List<Video>> call = webServiceAPI.getVideos();
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                videos.postValue(response.body());
            }
            @Override
            public void onFailure (Call<List<Video>> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    public void addVideo(Video video) {
        Call<Void> call = webServiceAPI.addVideo(video);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("VideoAPI", "Video added successfully.");
                    // Optionally, refresh the video list
                    getAllVideos(videoListData);
                } else {
                    Log.e("VideoAPI", "Failed to add video: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("VideoAPI", "Error adding video: " + t.getMessage());
            }
        });
    }

    public void deleteVideo(String id) {
        Call<Void> call = webServiceAPI.deleteVideo(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("VideoAPI", "Video deleted successfully.");
                    // Optionally, refresh the video list
                    getAllVideos(videoListData);
                } else {
                    Log.e("VideoAPI", "Failed to delete video: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("VideoAPI", "Error deleting video: " + t.getMessage());
            }
        });
    }

    public MutableLiveData<Video> getVideoById(String videoId) {
        MutableLiveData<Video> videoLiveData = new MutableLiveData<>();

        Call<Video> call = webServiceAPI.getVideoById(videoId);
        call.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                if (response.isSuccessful() && response.body() != null) {
                    videoLiveData.setValue(response.body());
                } else {
                    Log.e("VideoAPI", "Failed to fetch video: " + response.message());
                    videoLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
                Log.e("VideoAPI", "Error fetching video: " + t.getMessage());
                videoLiveData.setValue(null);
            }
        });

        return videoLiveData;
    }

    public void getVideosByUser(String username, MutableLiveData<List<Video>> videos) {
        Call<List<Video>> call = webServiceAPI.getVideosByUser(username);
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    videos.postValue(response.body());
                } else {
                    Log.e("VideoAPI", "Failed to fetch videos by user: " + response.message());
                    videos.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                Log.e("VideoAPI", "Error fetching videos by user: " + t.getMessage());
                videos.postValue(null);
            }
        });
    }

}
