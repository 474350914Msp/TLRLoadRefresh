package com.think.uiloader.ui;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.think.uiloader.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by borney on 4/28/17.
 */
public class MainActivity extends AppCompatActivity {
    private RecyclerView mContentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentView = (RecyclerView) LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        setContentView(mContentView);
        mContentView.setLayoutManager(new LinearLayoutManager(this));
        mContentView.setHasFixedSize(true);
        mContentView.addItemDecoration(new ItemDecorationVerticalDivider(this));
        mContentView.setAdapter(new MainAdapter(initList()));
    }

    private List<ListItem<AppCompatActivity>> initList() {
        return new ArrayList<ListItem<AppCompatActivity>>() {
            {
                add(new ListItem(getString(R.string.label_listview), ListViewActivity.class));
                add(new ListItem(getString(R.string.label_recyclerview), RecyclerViewActivity.class));
                add(new ListItem(getString(R.string.label_viewgroup), ViewGroupActivity.class));
                add(new ListItem(getString(R.string.label_textview), TextViewActivity.class));
                add(new ListItem(getString(R.string.label_keephead), KeepHeadActivity.class));
                add(new ListItem(getString(R.string.label_cannot_move_head_by_tlr), CannotMoveHeadByTLRActivity.class));
            }
        };
    }

    private class MainAdapter extends RecyclerView.Adapter<MainHolder> {
        private List<ListItem<AppCompatActivity>> mItems;

        public MainAdapter(List<ListItem<AppCompatActivity>> items) {
            mItems = items;
        }

        @Override
        public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView view = (TextView) LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(getBackground());
            }
            return new MainHolder(view);
        }

        @Override
        public void onBindViewHolder(MainHolder holder, int position) {
            if (position >= 0 && position < getItemCount()) {
                if (mItems != null) {
                    final ListItem<AppCompatActivity> item = mItems.get(position);
                    holder.itemView.setText(item.name);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            item.start();
                        }
                    });
                }
            }
        }

        @Override
        public int getItemCount() {
            return mItems != null ? mItems.size() : 0;
        }

        private Drawable getBackground() {
            int[] itemBackground = new int[]{android.R.attr.selectableItemBackground};
            TypedArray a = MainActivity.this.obtainStyledAttributes(itemBackground);
            try {
                return a.getDrawable(0);
            } finally {
                a.recycle();
            }
        }
    }

    private class MainHolder extends RecyclerView.ViewHolder {
        private TextView itemView;

        public MainHolder(TextView itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    private class ListItem<T extends AppCompatActivity> {
        private String name;
        private Class<T> activityClass;

        ListItem(String name, Class<T> activityClass) {
            this.name = name;
            this.activityClass = activityClass;
        }

        void start() {
            Intent intent = new Intent(MainActivity.this, activityClass);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, name + " not support!!!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
