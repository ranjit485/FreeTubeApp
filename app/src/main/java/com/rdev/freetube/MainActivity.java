package com.rdev.freetube;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.rdev.freetube.databinding.ActivityMainBinding;
import com.rdev.freetube.fragment.Favorites;
import com.rdev.freetube.fragment.MenuBottomSheetFragment;
import com.rdev.freetube.fragment.home;
import com.rdev.freetube.modal.Language;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Menu menu;
    ActivityMainBinding binding;

    private DrawerLayout drawerLayout;

    private ImageView imageButtonToggleTheme;

    private MaterialToolbar materialToolbar;
    private BottomNavigationView bottomNavigationView;

    public static final String PREFS_NAME = "LanguagePrefs";
    public static final String PREF_LANGUAGE_KEY = "SelectedLanguage";

    private static final String URL = "https://raw.githubusercontent.com/ranjit485/FreeTube/main/languages.json"; // Replace with your JSON URL
    private List<Language> languages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        requestWindowFeature( Window.FEATURE_NO_TITLE );

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new home());

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if(itemId == R.id.nav_home){
                replaceFragment(new home());
            }else if(itemId == R.id.nav_saved ){
                replaceFragment(new Favorites());
            }else if(itemId == R.id.setting ){
                MenuBottomSheetFragment menuBottomSheetFragment = new MenuBottomSheetFragment();
                menuBottomSheetFragment.show(getSupportFragmentManager(), "MenuBottomSheetFragment");
            }

            return true;
        });

        materialToolbar = findViewById(R.id.topAppBar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String currentLanguage = sharedPreferences.getString(PREF_LANGUAGE_KEY, "English");

        Toast.makeText(this, currentLanguage, Toast.LENGTH_SHORT).show();

        Button showLanguageMenuButton = findViewById(R.id.languageSelectButton);
        showLanguageMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLanguagesAndShowDialog();
            }
        });


        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        imageButtonToggleTheme = findViewById(R.id.imageButton_toggle_theme);

        // Set initial image based on current theme
        updateButtonImage();

        imageButtonToggleTheme.setOnClickListener(v -> {
            int currentNightMode = AppCompatDelegate.getDefaultNightMode();
            if (currentNightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            updateButtonImage();
        });

    }
    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public void toggleDrawer(View view) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the home action
        } else if (id == R.id.nav_profile) {
            // Handle the profile action
        }

        drawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }
    private void updateButtonImage() {
        int currentNightMode = AppCompatDelegate.getDefaultNightMode();
        if (currentNightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            imageButtonToggleTheme.setImageResource(R.drawable.ic_dark_mode);
        } else {
            imageButtonToggleTheme.setImageResource(R.drawable.ic_light_mode);
        }
    }

    public void hideAppBar() {
        if (materialToolbar != null) {
            materialToolbar.setVisibility(View.GONE);
        }
    }

    public void showAppBar() {
        if (materialToolbar != null) {
            materialToolbar.setVisibility(View.VISIBLE);
        }
    }

    public void hideBottomNavigation() {
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    public void showBottomNavigation() {
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    private void fetchLanguagesAndShowDialog() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                response -> {
                    Moshi moshi = new Moshi.Builder().build();
                    Type listType = Types.newParameterizedType(List.class, Language.class);
                    JsonAdapter<List<Language>> jsonAdapter = moshi.adapter(listType);

                    try {
                        languages = jsonAdapter.fromJson(response.optJSONArray("languages").toString());
                        showLanguageSelectionDialog();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Failed to parse languages", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(MainActivity.this, "Failed to fetch languages", Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void showLanguageSelectionDialog() {
        if (languages == null || languages.isEmpty()) {
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String currentLanguage = sharedPreferences.getString(PREF_LANGUAGE_KEY, "Hindi");

        String[] languageNames = new String[languages.size()];
        int checkedItem = 0;
        for (int i = 0; i < languages.size(); i++) {
            languageNames[i] = languages.get(i).getName();
            if (languageNames[i].equals(currentLanguage)) {
                checkedItem = i;
            }
        }

        new MaterialAlertDialogBuilder(this)
                .setTitle("Select Language")
                .setSingleChoiceItems(languageNames, checkedItem, (dialog, which) -> {
                    // Save the selected language in SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(PREF_LANGUAGE_KEY, languageNames[which]);
                    editor.apply();
                })
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

}