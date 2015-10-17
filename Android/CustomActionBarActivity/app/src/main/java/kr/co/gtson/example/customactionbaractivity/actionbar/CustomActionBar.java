package kr.co.gtson.example.customactionbaractivity.actionbar;

import android.app.ActionBar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import kr.co.gtson.example.customactionbaractivity.R;

/**
 * Created by genteelson on 2015-04-17.
 */
public class CustomActionBar {

    private Context context;
    private TextView tvTitle;
    private ImageButton imageButton;

    public CustomActionBar(final Context context, ActionBar actionBar) {
        this.context = context;

        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_actionbar, null);
        this.tvTitle = (TextView)view.findViewById(R.id.title_text);
        this.tvTitle.setText("Title Text");

        this.imageButton = (ImageButton)view.findViewById(R.id.imageButton);
        this.imageButton.setOnClickListener(viewOnClickListener);

        actionBar.setCustomView(view);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    private View.OnClickListener viewOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Toast.makeText(context, "Refresh Clicked!", Toast.LENGTH_LONG).show();
        }
    };
}
