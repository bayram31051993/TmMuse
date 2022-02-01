package geek.space.tmmuse.Activity.Main_menu;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import geek.space.tmmuse.Common.Font.Font;
import geek.space.tmmuse.Common.Utils;
import geek.space.tmmuse.Fragment.CardFragment.CardFragment;
import geek.space.tmmuse.Fragment.CategoryFragment.CategoryFragment;
import geek.space.tmmuse.Fragment.HomeFragment.HomeFragment;
import geek.space.tmmuse.Fragment.MessageFragment.MessageFragment;
import geek.space.tmmuse.Fragment.OpenMessage.OpenMessageFragment;
import geek.space.tmmuse.Fragment.ProfileFragment.Profiles;
import geek.space.tmmuse.Fragment.ProfileFragment.UserProfileFragment;
import geek.space.tmmuse.Fragment.SettingsFragment.SettingsFragment;
import geek.space.tmmuse.R;
import io.alterac.blurkit.BlurLayout;
import soup.neumorphism.NeumorphCardView;
import soup.neumorphism.NeumorphImageView;

public class Main_Menu extends AppCompatActivity implements View.OnClickListener {


    NeumorphImageView main_menu_btn, category_menu_btn, card_menu_btn, message_menu_btn, settings_menu_btn;
    FrameLayout menu_frame;
    public static Fragment firstFragment = new HomeFragment();
    public static Fragment secondFragment = new CategoryFragment();
    public static Fragment thirdFragment = new CardFragment();
    public static Fragment fourthFragment = new MessageFragment();
    public static Fragment fivesFragment = new SettingsFragment();
    Context context = this;
    private BlurLayout blurLayout;
    private static Main_Menu INSTANCE;
    private TextView no_connection_txt;
    private ImageView reload_app_img;
    private RelativeLayout connection_is_not_ok_rel, connection_is_ok_rel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main__menu);
        INSTANCE = this;
        intiComponents();
        setListeners();
        setFont();
        getLang();
        InternetConnectionFunction();
    }

    private void setFont() {
        no_connection_txt.setTypeface(Font.getInstance(this).getMontserrat_500());
    }

    public static void setVisibilityBottomNavigation(FragmentActivity activity,int isVisible){
        NeumorphCardView bottomNavigationView=activity.findViewById(R.id.bottom_menu);
        bottomNavigationView.setVisibility(isVisible);
    }

    public static Main_Menu get() {
        return INSTANCE;
    }

    public BlurLayout getBlurLayout() {
        return blurLayout;
    }

    private void setListeners() {
        main_menu_btn.setOnClickListener(this);
        category_menu_btn.setOnClickListener(this);
        card_menu_btn.setOnClickListener(this);
        message_menu_btn.setOnClickListener(this);
        settings_menu_btn.setOnClickListener(this);
        reload_app_img.setOnClickListener(view -> InternetConnectionFunction());
    }

    private void intiComponents() {
        main_menu_btn = findViewById(R.id.main_menu_btn);
        category_menu_btn = findViewById(R.id.category_menu_btn);
        card_menu_btn = findViewById(R.id.card_menu_btn);
        message_menu_btn = findViewById(R.id.message_menu_btn);
        settings_menu_btn = findViewById(R.id.settings_menu_btn);
        menu_frame = findViewById(R.id.menu_frame);
        blurLayout = findViewById(R.id.blurLayout);
        no_connection_txt = findViewById(R.id.no_connection_txt);
        reload_app_img = findViewById(R.id.reload_app_img);
        connection_is_not_ok_rel = findViewById(R.id.connection_is_not_ok_rel);
        connection_is_ok_rel = findViewById(R.id.connection_is_ok_rel);
        no_connection_txt = findViewById(R.id.no_connection_txt);
        openFragment(0);
    }

    @Override
    public void onBackPressed() {
        try{
            if(Profiles.draw_profile.isDrawerOpen(GravityCompat.END)){
                Profiles.draw_profile.closeDrawer(GravityCompat.END);
                return;
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        SettingsFragment settingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag(SettingsFragment.class.getSimpleName());
        CardFragment cardFragment = (CardFragment) getSupportFragmentManager().findFragmentByTag(CardFragment.class.getSimpleName());
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getSimpleName());
        MessageFragment messageFragment = (MessageFragment) getSupportFragmentManager().findFragmentByTag(MessageFragment.class.getSimpleName());
        CategoryFragment categoryFragment = (CategoryFragment) getSupportFragmentManager().findFragmentByTag(CategoryFragment.class.getSimpleName());
        if ((settingsFragment != null && settingsFragment.isVisible())
                || (cardFragment != null && cardFragment.isVisible())
                || (homeFragment != null && homeFragment.isVisible())
                || (messageFragment != null && messageFragment.isVisible())
                || (categoryFragment != null && categoryFragment.isVisible())) {
            finish();
        }

        Profiles myFragment = (Profiles) getSupportFragmentManager().findFragmentByTag(Profiles.class.getSimpleName());
        if (myFragment != null && myFragment.isVisible()) {
            secondFragment = new CategoryFragment();
            Utils.removeShow(new CategoryFragment(), CategoryFragment.class.getSimpleName(), getSupportFragmentManager(), R.id.menu_frame);
        }

        OpenMessageFragment openMessageFragment = (OpenMessageFragment) getSupportFragmentManager().findFragmentByTag(OpenMessageFragment.class.getSimpleName());
        if (openMessageFragment != null && openMessageFragment.isVisible()) {
            fourthFragment = new MessageFragment();
            Utils.removeShow(new MessageFragment(), MessageFragment.class.getSimpleName(), getSupportFragmentManager(), R.id.menu_frame);
        }

        UserProfileFragment userProfileFragment = (UserProfileFragment) getSupportFragmentManager().findFragmentByTag(UserProfileFragment.class.getSimpleName());
        if (userProfileFragment != null && userProfileFragment.isVisible()) {
            fivesFragment = new SettingsFragment();
            Utils.removeShow(new SettingsFragment(), SettingsFragment.class.getSimpleName(), getSupportFragmentManager(), R.id.menu_frame);
        }



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_menu_btn:
                openFragment(0);
                break;
            case R.id.category_menu_btn:
                openFragment(1);
                break;
            case R.id.card_menu_btn:
                openFragment(2);
                break;
            case R.id.message_menu_btn:
                openFragment(3);
                break;
            case R.id.settings_menu_btn:
                openFragment(4);
                break;
        }
    }

    private void openFragment(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();


        switch (index) {
            case 0:

                disableAll();
                Utils.setImgPressed(main_menu_btn, 2, R.color.aply_text_color, context);
                Utils.hideAdd(firstFragment, firstFragment.getClass().getSimpleName(), fragmentManager, R.id.menu_frame);
                break;
            case 1:
                disableAll();
                Utils.setImgPressed(category_menu_btn, 2, R.color.aply_text_color, context);
                Utils.hideAdd(secondFragment, secondFragment.getClass().getSimpleName(), fragmentManager, R.id.menu_frame);
                break;
            case 2:
                disableAll();
                Utils.setImgPressed(card_menu_btn, 2, R.color.aply_text_color, context);
                Utils.hideAdd(thirdFragment, thirdFragment.getClass().getSimpleName(), fragmentManager, R.id.menu_frame);
                break;
            case 3:
                disableAll();
                Utils.setImgPressed(message_menu_btn, 2, R.color.aply_text_color, context);
                Utils.hideAdd(fourthFragment, fourthFragment.getClass().getSimpleName(), fragmentManager, R.id.menu_frame);
                break;
            case 4:
                disableAll();
                Utils.setImgPressed(settings_menu_btn, 2, R.color.aply_text_color, context);
                Utils.hideAdd(fivesFragment, fivesFragment.getClass().getSimpleName(), fragmentManager, R.id.menu_frame);
                break;
        }

    }

    private void disableAll() {
        main_menu_btn.setShapeType(0);
        main_menu_btn.setColorFilter(context.getResources().getColor(R.color.text_color));

        category_menu_btn.setShapeType(0);
        category_menu_btn.setColorFilter(context.getResources().getColor(R.color.text_color));

        card_menu_btn.setShapeType(0);
        card_menu_btn.setColorFilter(context.getResources().getColor(R.color.text_color));

        message_menu_btn.setShapeType(0);
        message_menu_btn.setColorFilter(context.getResources().getColor(R.color.text_color));

        settings_menu_btn.setShapeType(0);
        settings_menu_btn.setColorFilter(context.getResources().getColor(R.color.text_color));
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            for (NetworkInfo networkInfo : info) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public void InternetConnectionFunction(){
        if (isNetworkAvailable(this)){
            connection_is_ok_rel.setVisibility(View.VISIBLE);
            connection_is_not_ok_rel.setVisibility(View.INVISIBLE);
        } else {
            connection_is_ok_rel.setVisibility(View.INVISIBLE);
            connection_is_not_ok_rel.setVisibility(View.VISIBLE);
        }
    }

    // НАстройка языкого панеля
    public String getLang() {
        return getSharedPreferences("mysettings", MODE_PRIVATE).getString("My_Lang", "");
    }

}