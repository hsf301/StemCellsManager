package com.huashengfu.StemCellsManager.adapter.interaction.dynamic;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.interaction.Comment;
import com.huashengfu.StemCellsManager.utils.GlideCircleTransformation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentAdapter extends BaseAdapter<CommentViewHolder> {

    private List<Comment> comments = new ArrayList<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
    private List<Comment> select = new ArrayList<>();
    private boolean manager = false;
    private OnCommentAdapter onCommentAdapter;

    public void addAll(List<Comment> collection) {
        comments.addAll(collection);
    }

    public void clear(){
        comments.clear();
        select.clear();
    }

    public void setManager(boolean manager) {
        this.manager = manager;
    }

    public List<Comment> getSelect() {
        return select;
    }

    public void setAll(){
        select.clear();
        select.addAll(comments);
    }

    public void cancleAll(){
        select.clear();
    }

    public int getItemPosition(Comment comment){
        return comments.indexOf(comment);
    }

    public void removeAll(List<Comment> collection){
        comments.removeAll(collection);
    }

    public void setOnCommentAdapter(OnCommentAdapter onCommentAdapter) {
        this.onCommentAdapter = onCommentAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_coment, parent, false);
        CommentViewHolder viewHolder = new CommentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommentViewHolder viewHolder = (CommentViewHolder) holder;

        Comment comment = comments.get(position);

        viewHolder.tvDate.setText(sdf.format(new Date(comment.getCreateTime())));
        viewHolder.tvName.setText(comment.getNickName());
        viewHolder.tvContent.setText(comment.getCommentText());

        Glide.with(holder.itemView.getContext())
                .load(comment.getHeadPicUrl())
                .placeholder(R.mipmap.icon_header_default)
                .transform(new GlideCircleTransformation(holder.itemView.getContext(), 50))
                .into(viewHolder.ivHeader);

        if(manager){
            viewHolder.ivSelect.setVisibility(View.VISIBLE);
        }else{
            viewHolder.ivSelect.setVisibility(View.GONE);
        }

        Log.i(Constants.Log.Log, "manager --> " + manager);

        if(select.contains(comment)){
            viewHolder.ivSelect.setImageResource(R.mipmap.icon_checked_comment);
        }else{
            viewHolder.ivSelect.setImageResource(R.mipmap.icon_check_comment);
        }

        if(manager){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(select.contains(comment)){
                        select.remove(comment);
                    }else{
                        select.add(comment);
                    }

                    if(onCommentAdapter != null){
                        onCommentAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public interface OnCommentAdapter {
        public void notifyDataSetChanged();
    }
}
