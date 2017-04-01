package com.sih.rakshak.features;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.sih.rakshak.R;
import com.sih.rakshak.base.BaseFragment;
import com.sih.rakshak.features.bin.BinFragment;
import com.sih.rakshak.features.inbox.InboxFragment;
import com.sih.rakshak.features.mail.MailFragment;
import com.sih.rakshak.features.notes.NotesDetailFragment;
import com.sih.rakshak.features.notes.NotesFragment;
import com.sih.rakshak.features.notes.NotesItem;
import com.sih.rakshak.features.sent.SentFragment;
import com.sih.rakshak.features.settings.SettingsFragment;

import javax.mail.Message;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private BaseFragment currentFragment;
    private Message message;
    private NotesItem notesItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setUpNavView();
        setFragment(FragmentIds.INBOX);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (currentFragment.getBackToFragmentId() != null) {
                if(currentFragment.getBackToFragmentId() == FragmentIds.NOTESDETAIL) {
                    ((NotesDetailFragment)currentFragment).checkandsave();
                }
                setFragment(currentFragment.getBackToFragmentId());

            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inbox) {
            setFragment(FragmentIds.INBOX);
        } else if (id == R.id.nav_sent) {
            setFragment(FragmentIds.SENT);
        } else if (id == R.id.nav_bin) {
            setFragment(FragmentIds.BIN);
        } else if (id == R.id.nav_notes) {
            setFragment(FragmentIds.NOTES);
        } else if (id == R.id.nav_settings) {
            setFragment(FragmentIds.SETTINGS);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setUpNavView() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
    }

    private BaseFragment handleNavViewTransition(FragmentIds idForFragment) {
        BaseFragment toReturnFragment = new InboxFragment();
        switch (idForFragment) {
            case INBOX:
                navView.setCheckedItem(R.id.nav_inbox);
                getSupportActionBar().setTitle(R.string.inbox);
                toReturnFragment = new InboxFragment();
                break;
            case SENT:
                navView.setCheckedItem(R.id.nav_sent);
                getSupportActionBar().setTitle(R.string.sent);
                toReturnFragment = new SentFragment();
                break;
            case BIN:
                navView.setCheckedItem(R.id.nav_bin);
                getSupportActionBar().setTitle(R.string.bin);
                toReturnFragment = new BinFragment();
                break;
            case NOTES:
                navView.setCheckedItem(R.id.nav_notes);
                getSupportActionBar().setTitle(R.string.notes);
                toReturnFragment = new NotesFragment();
                break;
            case SETTINGS:
                navView.setCheckedItem(R.id.nav_settings);
                getSupportActionBar().setTitle(R.string.settings);
                toReturnFragment = new SettingsFragment();
                break;
            case MAIL:
                navView.setCheckedItem(R.id.nav_inbox);
                getSupportActionBar().setTitle("");
                toReturnFragment = new MailFragment();
                break;
            case NOTESDETAIL:
                getSupportActionBar().setTitle(notesItem.getLastViewed());
                toReturnFragment = new NotesDetailFragment();
                break;
        }
        return toReturnFragment;
    }


    public void setFragment(FragmentIds idForFragment) {
        if (currentFragment != null && idForFragment == currentFragment.getFragmentId()) {
            return;
        }
        if(idForFragment == FragmentIds.NOTESDETAIL)((NotesDetailFragment)currentFragment).checkandsave();
        BaseFragment newFragment = handleNavViewTransition(idForFragment);
        currentFragment = newFragment;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frameLayout, newFragment);
        transaction.commit();
        appBarLayout.setExpanded(true, true);
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public NotesItem getNotesItem() {
        return notesItem;
    }

    public void setNotesItem(NotesItem notesItem) {
        this.notesItem = notesItem;
    }
}
