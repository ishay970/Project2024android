package com.example.youtube;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.youtube.adapters.UsersListAdapter;
import com.example.youtube.adapters.VideosListAdapter;
import com.example.youtube.databinding.ActivityMainBinding;
import com.example.youtube.entities.User;
import com.example.youtube.repositories.UserRepository;
import com.example.youtube.viewModels.UserViewModel;
import com.example.youtube.viewModels.VideoViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private VideosListAdapter videoAdapter;
    private UsersListAdapter userAdapter;
    private ImageView youBtn;
    private UserRepository userRepository;
    private User loggedInUser;
    private UserViewModel userViewModel;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRepository = UserRepository.getInstance(this);

        ImageButton btnToggleDark = binding.modeBtn;
        btnToggleDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isDarkMode = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
                if (isDarkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                recreate();
            }
        });

        RecyclerView lstVideos = binding.lstVideos;
        videoAdapter = new VideosListAdapter(this);

        VideoViewModel viewModel = new ViewModelProvider(this).get(VideoViewModel.class);
        viewModel.getVideos().observe(this, videos -> {
            videoAdapter.setVideos(videos);
        });

        lstVideos.setAdapter(videoAdapter);
        lstVideos.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView lstUsers = binding.lstUsers;
        userAdapter = new UsersListAdapter(this);
        lstUsers.setAdapter(userAdapter);
        lstUsers.setLayoutManager(new LinearLayoutManager(this));

        LiveData<List<User>> users = userRepository.getAllUsers();
        users.observe(this, userList -> userAdapter.setUsers(userList));

        ImageButton searchBtn = binding.searchBtn;
        searchBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(i);
        });

        ImageButton homeBtn = binding.homeBtn;
        homeBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, MainActivity.class);
            //videoRepository.resetVideos();
            startActivity(i);
        });

        ImageButton btnUploadVideo = binding.uploadBtn;
        btnUploadVideo.setOnClickListener(v -> {
            if (userRepository.getLoggedInUser() == null) {
                Toast.makeText(MainActivity.this, "You need to be logged in to upload a video", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent i = new Intent(MainActivity.this, UploadActivity.class);
            startActivity(i);
        });

        loggedInUser = MyApplication.getCurrentUser();
        youBtn = binding.youBtn;
        if (loggedInUser == null) {
            setLoggedOutState();
        }
        else {
            setLoggedInState();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LiveData<List<User>> users = userRepository.getAllUsers();
        users.observe(this, userList -> userAdapter.setUsers(userList));
    }

    private void setLoggedInState() {
        youBtn.setImageResource(R.drawable.baseline_account_circle_24);
        if (loggedInUser != null) {
            binding.youBtnText.setText("Log Out");
            if (loggedInUser.getImageUrl() != null && !loggedInUser.getImageUrl().isEmpty()) {
                String imageUrl = "http://10.0.2.2:8080/" + loggedInUser.getImageUrl();
                Glide.with(this)
                        .load(imageUrl)
                        .transform(new CircleCrop())
                        .into(youBtn);
            }
        }
        youBtn.setOnClickListener(v -> {
            userRepository.logoutUser();
            setLoggedOutState();
        });
    }

    private void setLoggedOutState() {
        binding.youBtnText.setText("Log In");
        userRepository.logoutUser();
        youBtn.setImageResource(R.drawable.baseline_account_circle_24);
        youBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(i);
        });
    }
}
