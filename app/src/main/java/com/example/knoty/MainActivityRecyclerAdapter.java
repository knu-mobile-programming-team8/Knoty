package com.example.knoty;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivityRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //리사이클러뷰의 핵심이 되는 리스트
    private ArrayList<Announcement> list = new ArrayList<Announcement>();

    private static final int VIEW_TYPE_TEXT_ONLY = 0;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == VIEW_TYPE_TEXT_ONLY) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_only_recyclerview_item, parent, false);
            return new MainActivityRecyclerAdapter.TextOnlyViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TextOnlyViewHolder) {
            ((TextOnlyViewHolder)holder).onBind(position);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    //레이아웃에 있는 텍스트 뷰 3개에 setText한다
    public class TextOnlyViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView idTextView;
        private TextView dateTextView;
        private TextView titleTextView;

        public TextOnlyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;
            this.idTextView = itemView.findViewById(R.id.idTextView);
            this.dateTextView = itemView.findViewById(R.id.dateTextView);
            this.titleTextView = itemView.findViewById(R.id.titleTextView);
        }

        public void onBind(int position) {
            final Announcement ant = list.get(position);
            idTextView.setText(id2string(ant.id));
            dateTextView.setText(ant.date);
            titleTextView.setText(ant.title);
            itemView.setOnClickListener(new View.OnClickListener() { //클릭하면 url 이동
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ant.url));
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        //Announcement의 id를 문자열인 학부로 변환
        private String id2string(int id) {
            if(id == 1) {
                return "학교";
            } else if(id == 2) {
                return "컴퓨터학부";
            }
            return null;
        }
    }



    //외부에서 아이템을 추가할 때
    public void addItem(Announcement ant) {
        list.add(ant);
    }
}
