package kr.co.gtson.example.customlistviewactivity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import kr.co.gtson.example.customlistviewactivity.adapter.CustomArrayAdapter;
import kr.co.gtson.example.customlistviewactivity.item.ListItem;
import kr.co.gtson.example.customlistviewactivity.listener.CustomListScrollListener;


public class CustomListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list);

        ArrayList<ListItem> listItems = new ArrayList<ListItem>();
        for (int i = 0; i< 20; i++) {
            listItems.add(new ListItem(R.mipmap.ic_launcher, "MainTitle_" + i, "SubTitle_" + i));
        }

        ListView listView = (ListView)findViewById(R.id.listView);
        CustomArrayAdapter customArrayAdapter = new CustomArrayAdapter(this, R.layout.activity_custom_list_item, listItems);
        listView.setAdapter(customArrayAdapter);
        CustomListScrollListener customListScrollListener = new CustomListScrollListener(customArrayAdapter, listItems);
        listView.setOnScrollListener(customListScrollListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_custom_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
