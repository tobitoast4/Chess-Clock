package com.tozil.chessclock;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TimerListAdapter extends RecyclerView.Adapter<TimerListAdapter.TimerListViewHolder> {

    private ArrayList<TimerItem> timers;
    private OnItemClickListener listener;

    public TimerListAdapter(ArrayList<TimerItem> timers){
        this.timers = timers;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TimerListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timer_list_item, parent, false);
        TimerListViewHolder timerListViewHolder = new TimerListViewHolder(v, listener);
        return timerListViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TimerListViewHolder holder, int position) {
        TimerItem currentItem = timers.get(position);
        holder.textView_timer_name.setText(currentItem.getTimerName());
        String total1 = generateTimeArray(currentItem.getTimerTopMilliSeconds());
        holder.textView_total_time_1.setText(total1);
        String total2 = generateTimeArray(currentItem.getTimerBottomMilliSeconds());
        holder.textView_total_time_2.setText(total2);
        String delay1 = generateTimeArray(currentItem.getTimerTopMilliSecondsDelay());
        holder.textView_delay_1.setText(delay1);
        String delay2 = generateTimeArray(currentItem.getTimerBottomMilliSecondsDelay());
        holder.textView_delay_2.setText(delay2);
        String increment1 = generateTimeArray(currentItem.getTimerTopMilliSecondsIncrement());
        holder.textView_increment_1.setText(increment1);
        String increment2 = generateTimeArray(currentItem.getTimerBottomMilliSecondsIncrement());
        holder.textView_increment_2.setText(increment2);
    }

    public String generateTimeArray(long milliSecs){
        int hours = (int) (milliSecs / 3600000);
        milliSecs = milliSecs - (hours * 3600000L);
        int minutes = (int) (milliSecs / 60000);
        milliSecs = milliSecs - (minutes * 60000L);
        int seconds = (int) (milliSecs / 1000);

        String new_time = String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);

        return new_time;
    }

    @Override
    public int getItemCount() {
        return timers.size();
    }

    public static class TimerListViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_timer_name;
        public TextView textView_total_time_1;
        public TextView textView_total_time_2;
        public TextView textView_delay_1;
        public TextView textView_delay_2;
        public TextView textView_increment_1;
        public TextView textView_increment_2;
        public ConstraintLayout constraintLayout_timers;
        public ConstraintLayout item;
        public ImageView button_expand;
        public ImageView button_delete;

        public TimerListViewHolder(View itemView, OnItemClickListener listener){
            super(itemView);
            textView_timer_name = itemView.findViewById(R.id.textView_timer_name);
            textView_total_time_1 = itemView.findViewById(R.id.textView_total_time_1);
            textView_total_time_2 = itemView.findViewById(R.id.textView_total_time_2);
            textView_delay_1 = itemView.findViewById(R.id.textView_delay_1);
            textView_delay_2 = itemView.findViewById(R.id.textView_delay_2);
            textView_increment_1 = itemView.findViewById(R.id.textView_increment_1);
            textView_increment_2 = itemView.findViewById(R.id.textView_increment_2);
            item = itemView.findViewById(R.id.item_background);
            button_expand = itemView.findViewById(R.id.imageView_expand);
            button_delete = itemView.findViewById(R.id.imageView_delete);
            constraintLayout_timers = itemView.findViewById(R.id.constraintLayout_timers);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            button_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int visibility = constraintLayout_timers.getVisibility();
                    if(visibility == View.GONE){
                        Animation rotateAnimation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.rotate_open);
                        button_expand.startAnimation(rotateAnimation);
                        expand(constraintLayout_timers);
                    } else {
                        Animation rotateAnimation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.rotate_close);
                        button_expand.startAnimation(rotateAnimation);
                        collapse(constraintLayout_timers);
                    }
                }
            });

            button_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }

        // see https://stackoverflow.com/questions/4946295/android-expand-collapse-animation
        public static void expand(final View v) {
            int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
            int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
            final int targetHeight = v.getMeasuredHeight();

            // Older versions of android (pre API 21) cancel animations for views with a height of 0.
            v.getLayoutParams().height = 1;
            v.setVisibility(View.VISIBLE);
            Animation a = new Animation()
            {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    v.getLayoutParams().height = interpolatedTime == 1
                            ? ViewGroup.LayoutParams.WRAP_CONTENT
                            : (int)(targetHeight * interpolatedTime);
                    v.requestLayout();
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            // Expansion speed of 1dp/ms
            a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
            v.startAnimation(a);
        }

        // see https://stackoverflow.com/questions/4946295/android-expand-collapse-animation
        public static void collapse(final View v) {
            final int initialHeight = v.getMeasuredHeight();

            Animation a = new Animation()
            {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    if(interpolatedTime == 1){
                        v.setVisibility(View.GONE);
                    }else{
                        v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                        v.requestLayout();
                    }
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            // Collapse speed of 1dp/ms
            a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
            v.startAnimation(a);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

}
