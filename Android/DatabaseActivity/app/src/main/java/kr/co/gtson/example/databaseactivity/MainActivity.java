package kr.co.gtson.example.databaseactivity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import kr.co.gtson.example.databaseactivity.dbhelper.DBHelper;
import kr.co.gtson.example.databaseactivity.dbhelper.model.Item;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHelper dbHelper = new DBHelper(this);

        dbHelper.insertItem(new Item("DatabaseTest1"));
        dbHelper.insertItem(new Item("DatabaseTest2"));
        dbHelper.insertItem(new Item("DatabaseTest3"));

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout);

        List<Item> itemList = dbHelper.getAllItems();
        int nCnt = itemList.size();
        for (int i = 0; i < nCnt; i++) {
            Item item = itemList.get(i);
            TextView textView = new TextView(this);
            textView.setText(item.getName());
            linearLayout.addView(textView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
