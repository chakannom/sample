package kr.co.gtson.example.customlistviewactivity.listener;


import android.os.Handler;
import android.widget.AbsListView;
import java.util.ArrayList;

import kr.co.gtson.example.customlistviewactivity.R;
import kr.co.gtson.example.customlistviewactivity.adapter.CustomArrayAdapter;
import kr.co.gtson.example.customlistviewactivity.item.ListItem;

/**
 * Created by genteelson on 2015-04-13.
 */
public class CustomListScrollListener implements AbsListView.OnScrollListener {

    private CustomArrayAdapter customArrayAdapter;
    private ArrayList<ListItem> listItems;

    private Boolean lockListView;

    public CustomListScrollListener(CustomArrayAdapter customArrayAdapter, ArrayList<ListItem> listItems) {
        this.customArrayAdapter = customArrayAdapter;
        this.listItems = listItems;
        this.lockListView = false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int count = totalItemCount - visibleItemCount;

        if(firstVisibleItem >= count && totalItemCount != 0 && lockListView == false) {
            addItems(10);
            // 모든 데이터를 로드하여 적용하였다면 어댑터에 알리고
            // 리스트뷰의 락을 해제합니다.
            customArrayAdapter.notifyDataSetChanged();
        }
    }

    private void addItems(final int size) {
        // 아이템을 추가하는 동안 중복 요청을 방지하기 위해 락을 걸어둡니다.
        lockListView = true;

        Runnable run = new Runnable() {
            @Override
            public void run() {
                for(int i = 0 ; i < size ; i++)  {
                    listItems.add(new ListItem(R.mipmap.ic_launcher, "MainTitle_" + i, "SubTitle_" + i));
                }
                // 모든 데이터를 로드하여 적용하였다면 어댑터에 알리고
                // 리스트뷰의 락을 해제합니다.
                customArrayAdapter.notifyDataSetChanged();
                lockListView = false;
            }
        };

        // 속도의 딜레이를 구현하기 위한 꼼수
        Handler handler = new Handler();
        handler.postDelayed(run, 5000);
    }
}
