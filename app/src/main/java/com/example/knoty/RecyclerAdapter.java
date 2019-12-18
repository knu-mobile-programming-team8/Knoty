package com.example.knoty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //adapter에 들어갈 list
    private ArrayList<Data> list = new ArrayList<>();

    public static final int VIEW_TYPE_NORMAL = 0;
    public static final int VIEW_TYPE_DIVIDER = 1;
    public static final int VIEW_TYPE_TOGGLE = 2;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //normal_recyclerview_item.xml을 inflate 시킨다
        View view;
        if(viewType == VIEW_TYPE_NORMAL) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.normal_recyclerview_item, parent, false);
            return new NormalViewHolder(view);
        } else if(viewType == VIEW_TYPE_DIVIDER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.divider_recyclerview_item, parent, false);
            return new DividerViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.toggle_recyclerview_item, parent, false);
            return new ToggleViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getItemViewType();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        //item 하나하나의 textView와 imageView 등을 설정한다
        if(holder instanceof NormalViewHolder) {
            ((NormalViewHolder)holder).onBind(list.get(position));
        } else if(holder instanceof  DividerViewHolder) {
            //구분선은 onBind를 할 게 없음
            //((DividerViewHolder)holder).onBind(list.get(position));
        } else  {
            ((ToggleViewHolder)holder).onBind(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //외부에서 item을 추가할 수 있다. image, button의 배경 설정을 안 하려면 -1을 넣으면 된다. str은 맘대로, viewType은 VIEW_TYPE_NORMAL, DIVIDER, TOGGLE 중 하나
    public void addItem(int image, String str, int button, int viewType) {
        Data data = new Data(image, str, button, viewType);
        list.add(data);
    }

    public void addItem(int image, String str, int button, int viewType, View.OnClickListener itemListener) {
        Data data = new Data(image, str, button, viewType, itemListener);
        list.add(data);
    }

    public void addItem(int image, String str, int button, int viewType, View.OnClickListener itemListener, View.OnClickListener buttonListener) {
        Data data = new Data(image, str, button, viewType, itemListener, buttonListener);
        list.add(data);
    }

    public void addItem(int image, String str, int button, int viewType, View.OnClickListener itemListener, CompoundButton.OnCheckedChangeListener toggleListener) {
        Data data = new Data(image, str, button, viewType, itemListener, toggleListener);
        list.add(data);
    }


    //가장 기본적인 아이템뷰 (사진, 텍스트, 버튼)
    public class NormalViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;
        private Button button;

        public NormalViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            button = itemView.findViewById(R.id.button);
        }

        void onBind(Data data) {
            int imageId = data.getImageDrawable();
            int buttonBackgroundId = data.getButtonDrawable();

            if(imageId != -1) imageView.setImageResource(imageId);
            textView.setText(data.getContent());
            if(buttonBackgroundId != -1) { //버튼 백그라운드가 설정되있으면 그걸로 보여주고 아니면 아예 버튼 숨김
                button.setBackground(ContextCompat.getDrawable(button.getContext(), buttonBackgroundId));
            } else {
                button.setVisibility(View.INVISIBLE);
            }
        }
    }

    //구분선
    public class DividerViewHolder extends RecyclerView.ViewHolder {
        public DividerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    //토글 버튼이 있는 아이템뷰 (사진, 텍스트, 토글버튼)
    public class ToggleViewHolder extends RecyclerView.ViewHolder {
        private  View itemView;
        private ImageView imageView;
        private TextView textView;
        private Switch toggle;

        public ToggleViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            toggle = itemView.findViewById(R.id.switch1);
        }

        void onBind(Data data) {
            int imageId = data.getImageDrawable();
            View.OnClickListener itemListener = data.getItemListener();
            CompoundButton.OnCheckedChangeListener toggleListener = data.getToggleListener();

            if(imageId != -1) imageView.setImageResource(imageId);
            textView.setText(data.getContent());
            toggle.setChecked(data.getButtonDrawable() == 1 ? true : false);
            if(itemListener != null) itemView.setOnClickListener(itemListener);
            if(toggleListener != null) toggle.setOnCheckedChangeListener(toggleListener);
        }
    }





    public class Data {
        private int imageDrawable;
        private String content;
        private int buttonDrawable;
        private int itemViewType;
        private View.OnClickListener itemListener = null; //행 자체를 클릭할 때 이벤트
        private View.OnClickListener buttonListener = null; //오른쪽 버튼을 클릭할 때 이벤트
        private CompoundButton.OnCheckedChangeListener toggleListener = null; //오른쪽 토글 버튼을 클릭할 때 이벤트

        //옵션 액티비티에서 한 줄에 있는 이미지와 텍스트 설정 (ViewType이 0일 때는 button이 백그라운드 drawable 값이고 ViewType이 1일 때는 무조건 button값 -1 ViewType이 2일 때는 button값이 토글 여부에 따라 0 또는 1)
        public Data(int image, String str, int button, int itemViewType) {
            this.imageDrawable = image;
            this.content = str;
            this.buttonDrawable = button;
            this.itemViewType = itemViewType;
        }

        public  Data(int image, String str, int button, int itemViewType, View.OnClickListener itemListener) {
            this(image, str, button, itemViewType);
            this.itemListener = itemListener;
        }

        public  Data(int image, String str, int button, int itemViewType, View.OnClickListener itemListener, View.OnClickListener buttonListener) {
            this(image, str, button, itemViewType);
            this.itemListener = itemListener;
            this.buttonListener = buttonListener;
        }

        public  Data(int image, String str, int button, int itemViewType, View.OnClickListener itemListener, CompoundButton.OnCheckedChangeListener toggleListener) {
            this(image, str, button, itemViewType);
            this.itemListener = itemListener;
            this.toggleListener = toggleListener;
        }

        public void setImageDrawable(int id) {
            this.imageDrawable = id;
        }

        public void setContent(String str) {
            this.content = str;
        }

        public void setButtonDrawable(int id) {
            this.buttonDrawable = id;
        }

        public void setItemViewType(int itemViewType) {
            this.itemViewType = itemViewType;
        }

        public void setButtonListener(View.OnClickListener buttonListener) {
            this.buttonListener = buttonListener;
        }


        public void setItemListener(View.OnClickListener itemListener) {
            this.itemListener = itemListener;
        }

        public void setToggleListener(CompoundButton.OnCheckedChangeListener toggleListener) {
            this.toggleListener = toggleListener;
        }

        public int getImageDrawable() {
            return this.imageDrawable;
        }

        public String getContent() {
            return this.content;
        }

        public  int getButtonDrawable() {
            return this.buttonDrawable;
        }

        public int getItemViewType() {
            return itemViewType;
        }

        public View.OnClickListener getItemListener() {
            return itemListener;
        }

        public View.OnClickListener getButtonListener() {
            return buttonListener;
        }
        public CompoundButton.OnCheckedChangeListener getToggleListener() {
            return toggleListener;
        }
    }
}
