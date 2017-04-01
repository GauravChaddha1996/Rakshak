package com.sih.rakshak.features.notes;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sih.rakshak.R;
import com.sih.rakshak.base.BaseFragment;
import com.sih.rakshak.features.FragmentIds;
import com.sih.rakshak.features.HomeActivity;

import java.util.Date;

import butterknife.ButterKnife;
import io.realm.Realm;

import static com.sih.rakshak.R.string.notes;

public class NotesDetailFragment extends BaseFragment {

    private TextView title, desc;
    NotesItem note;
    Realm realm;
    Boolean bool;

    public NotesDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_detail,container,false);
        ButterKnife.bind(this,view);
        initialiseViews(view);
        realm = Realm.getDefaultInstance();
        note = ((HomeActivity)getActivity()).getNotesItem();
        title.setText(note.getTitle());
        desc.setText(note.getDescription());
        return view;
    }

    private void initialiseViews(View v) {
        title = (TextView) v.findViewById(R.id.edit_title);
        desc = (TextView) v.findViewById(R.id.edit_desc);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_delete){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    Log.v("tag","Trying");
                    note.deleteFromRealm();
                }
            });
            ((HomeActivity)getActivity()).setFragment(FragmentIds.NOTES);
            return true;
        }
        return false;
    }

    public void checkandsave(){
        if(title.getText().toString().isEmpty()) note.deleteFromRealm();
        else{
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    note.setTitle(title.getText().toString());
                    note.setDescription(desc.getText().toString());
                    Date d = new Date();
                    CharSequence s = DateFormat.format("MMMM d, yyyy ", d.getTime());
                    note.setLastViewed(s.toString());
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit,menu);
    }

    @Override
    public FragmentIds getFragmentId() {
        return FragmentIds.NOTES;
    }

    @Override
    public FragmentIds getBackToFragmentId() {
        return FragmentIds.INBOX;
    }

}
