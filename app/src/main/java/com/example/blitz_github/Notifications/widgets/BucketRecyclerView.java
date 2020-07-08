package com.example.blitz_github.Notifications.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BucketRecyclerView extends RecyclerView {
    private List<View> mNonEmptyViews= Collections.emptyList();
    private List<View> mEmptyViews=Collections.emptyList();

    private AdapterDataObserver mObserver=new AdapterDataObserver() {
        @Override
        public void onChanged() {

            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {

            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {

            toggleViews();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {

            toggleViews();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {

            toggleViews();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {

            toggleViews();
        }
    };

    private void toggleViews() {
        if(getAdapter()!=null && !mEmptyViews.isEmpty()){
            if(getAdapter().getItemCount()==0){
                for(View view:mEmptyViews){
                    view.setVisibility(View.VISIBLE);
                }

                setVisibility(View.GONE);
            } else {
                for(View view:mEmptyViews){
                    view.setVisibility(View.GONE);
                }

                setVisibility(View.VISIBLE);
            }
        }
    }

    public BucketRecyclerView(@NonNull Context context) {
        super(context);
    }

    public BucketRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BucketRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAdapter(Adapter adapter){
        super.setAdapter(adapter);
        if(adapter!=null){
            adapter.registerAdapterDataObserver(mObserver);
        }
        mObserver.onChanged();
    }

    public void showIfEmpty(View ...emptyViews) {//1
        mEmptyViews= Arrays.asList(emptyViews);
    }
}
