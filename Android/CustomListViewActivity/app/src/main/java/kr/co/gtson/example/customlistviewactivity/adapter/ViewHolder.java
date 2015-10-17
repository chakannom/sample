package kr.co.gtson.example.customlistviewactivity.adapter;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by genteelson on 2015-04-13.
 */
public class ViewHolder {

    private ImageView image;
    private TextView mainTitle;
    private TextView subTitle;

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public TextView getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(TextView mainTitle) {
        this.mainTitle = mainTitle;
    }

    public TextView getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(TextView subTitle) {
        this.subTitle = subTitle;
    }
}
