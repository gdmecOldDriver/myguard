package edu.gdmec.s07150815.myguard.m3communicationguard;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import edu.gdmec.s07150815.myguard.m3communicationguard.adapter.ContactAdapter;
import edu.gdmec.s07150815.myguard.m3communicationguard.entity.ContactInfo;

/**
 *
 * Created by 马达 on 2016/12/20.
 */
public class ContactSelectActivity extends AppCompatActivity implements View.OnClickListener{
   private ListView mListView;
    private ContactAdapter adapter;
    private List<ContactInfo> systemContacts;

    @Override
    public void onClick(View view) {

    }
}
