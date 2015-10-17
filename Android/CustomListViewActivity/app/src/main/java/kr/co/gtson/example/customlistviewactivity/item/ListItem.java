package kr.co.gtson.example.customlistviewactivity.item;

/**
 * Created by genteelson on 2015-04-07.
 */
public class ListItem {
    private int imageId;
    private String mainTitle;
    private String subTitle;

    public ListItem(int imageId, String mainTitle, String subTitle) {
        this.imageId = imageId;
        this.mainTitle = mainTitle;
        this.subTitle = subTitle;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
