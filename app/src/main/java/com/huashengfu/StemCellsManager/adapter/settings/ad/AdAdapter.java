package com.huashengfu.StemCellsManager.adapter.settings.ad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.settings.Ad;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;

import java.util.ArrayList;
import java.util.List;

public class AdAdapter extends BaseAdapter<AdViewHolder> {

    private List<Ad> ads = new ArrayList<>();

    private final int TYPE_NORMAL = 1;
    private final int TYPE_FOOTER = 2;

    private boolean modify = false;

    public AdAdapter(){
        ads.add(new Ad());
    }

    public void add(Ad ad){
        ads.add(ads.size() - 1, ad);
    }

    public List<Ad> getAds() {
        return ads;
    }

    public void clear(){
        ads.clear();
        ads.add(new Ad());
    }

    public int remove(Ad ad){
        int position = ads.indexOf(ad);
        ads.remove(ad);
        return position;
    }

    public boolean isModify() {
        return modify;
    }

    public void setModify(boolean modify) {
        this.modify = modify;
        notifyDataSetChanged();
    }

    /**
     * Return the view type of the item at <code>position</code> for the purposes
     * of view recycling.
     * <p>
     * <p>The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * <code>position</code>. Type codes need not be contiguous.
     */
    @Override
    public int getItemViewType(int position) {
        Log.i(Constants.Log.Log, "getItemViewType --> " + position);
        if(position == getItemCount() - 1)
            return TYPE_FOOTER;
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(Constants.Log.Log, "onCreateViewHolder --> " + viewType);

        RecyclerView.ViewHolder holder = null;
        if(viewType == TYPE_NORMAL){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ad, parent, false);
            holder = new AdViewHolder(view);
        }else if(viewType == TYPE_FOOTER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ad_footer, parent, false);
            holder = new AdFooterViewHolder(view);
        }

        return holder;
    }

    private void move(int from ,int to){
        ads.add(to, ads.remove(from));
        notifyItemMoved(from, to);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i(Constants.Log.Log, "onBindViewHolder --> " + position);
        if(getItemViewType(position) == TYPE_NORMAL){
            AdViewHolder viewHolder = (AdViewHolder) holder;

            Ad ad = ads.get(position);

            Glide.with(holder.itemView.getContext())
                    .load(ad.getPic())
                    .transform(new GlideRoundTransformation(holder.itemView.getContext(), 5))
                    .into(viewHolder.ivImage);

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    modify = true;
                    viewHolder.llMenu.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();

                    return true;
                }
            });

            if(modify){
                viewHolder.llMenu.setVisibility(View.VISIBLE);
            }else{
                viewHolder.llMenu.setVisibility(View.GONE);
            }

            if(position == 0){
                viewHolder.ivUp.setImageResource(R.mipmap.icon_move_up_non);
            }else{
                viewHolder.ivUp.setImageResource(R.mipmap.icon_move_up);
            }

            if(position == getItemCount() - 2){
                viewHolder.ivDown.setImageResource(R.mipmap.icon_move_down_non);
            }else{
                viewHolder.ivDown.setImageResource(R.mipmap.icon_move_down);
            }

            viewHolder.llDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position == getItemCount() - 2)
                        return;

                    move(position, position + 1);
                }
            });

            viewHolder.llUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position == 0)
                        return;

                    move(position, position - 1);
                }
            });

            viewHolder.llDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog dialog = new AlertDialog.Builder(holder.itemView.getContext())
                            .setMessage(R.string.dialog_message_delete_ad)
                            .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    ads.remove(ad);
                                    notifyItemChanged(position);
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton(R.string.btn_cancle, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .create();
                    dialog.show();
                }
            });
        }else{
            AdFooterViewHolder viewHolder = (AdFooterViewHolder) holder;
            viewHolder.rlAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getOnItemClickListener() != null && !modify)
                        getOnItemClickListener().onItemClick(view, null);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ads.size();
    }
}
