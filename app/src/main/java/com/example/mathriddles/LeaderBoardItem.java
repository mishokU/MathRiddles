package com.example.mathriddles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;

public class LeaderBoardItem extends View {

    private View view;
    private TextView itemIndex;
    private TextView itemScore;
    private TextView userItemName;

    public LeaderBoardItem(Context context) {
        super(context);

        setItemView();
        setAllViews();
    }

    private void setAllViews() {
        itemIndex = view.findViewById(R.id.item_index);
        itemScore = view.findViewById(R.id.item_score);
        userItemName = view.findViewById(R.id.user_item_name);
    }

    public View getItemView(){
        return view;
    }

    public void setItemView(){
        view = View.inflate(getContext(), R.layout.leader_board_item,null);
    }

    public void setItemIndex(String index){
        itemIndex.setText(index);
    }

    public void setItemScore(String score){
        itemScore.setText(score);
    }

    public void setUserItemName(String user_name){
        userItemName.setText(user_name);
    }
}
