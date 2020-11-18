package com.huashengfu.StemCellsManager.adapter.settings.customer.service;

import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.settings.TelePhone;
import com.huashengfu.StemCellsManager.fragment.BaseFragment;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.DialogCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.utils.StringUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TelePhoneAdapter extends BaseAdapter<TelePhoneViewHolder> {

    private List<TelePhone> telephones = new ArrayList<>();

    private int type;
    private BaseFragment fragment;

    public TelePhoneAdapter(BaseFragment fragment, int type){
        this.type = type;
        this.fragment = fragment;
    }

    public void addAll(List<TelePhone> telePhones) {
        if(telePhones == null)
            return;
        this.telephones.addAll(telePhones);
    }

    public void add(TelePhone telephone){
        telephones.add(telephone);
    }

    public List<TelePhone> getTelephones() {
        return telephones;
    }

    public int remove(String telephone){
        int position = telephones.indexOf(telephone);
        telephones.remove(telephone);
        return position;
    }

    public void clear(){
        telephones.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_customer_service, parent, false);
        TelePhoneViewHolder viewHolder = new TelePhoneViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TelePhoneViewHolder viewHolder = (TelePhoneViewHolder) holder;

        TelePhone telePhone = telephones.get(position);

        viewHolder.tvPhone.setText(telePhone.getPhone());

        viewHolder.ivModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.dialog_customer_telephone, null);

                Dialog dialog = new Dialog(holder.itemView.getContext(), R.style.dialog);
                dialog.setContentView(v);

                dialog.show();
                EditText etPhone = v.findViewById(R.id.et_phone);
                etPhone.setText(telePhone.getPhone());

                v.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(etPhone.getText().length() != 11){
                            Toast.makeText(holder.itemView.getContext(), etPhone.getHint(), Toast.LENGTH_LONG).show();
                            return;
                        }


                        try {
                            JSONObject obj = new JSONObject();
                            obj.put(HttpHelper.Params.ids, new JSONArray(telePhone.getId()));
                            obj.put(HttpHelper.Params.newPhone, etPhone.getText().toString());
                            obj.put(HttpHelper.Params.type, type);

                            StringUtils.print(obj.toString());

                            OkGo.<JSONObject>put(HttpHelper.Url.Store.modifyCustomerServices)
                                    .tag(this)
                                    .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                    .upJson(obj)
                                    .execute(new DialogCallback<JSONObject>(fragment.getActivity(), false) {
                                        @Override
                                        public void onFinish() {
                                            super.onFinish();
                                        }

                                        @Override
                                        public void onSuccess(Response<JSONObject> response) {
                                            super.onSuccess(response);
                                            try {
                                                if(ResponseUtils.ok(response.body())){
                                                    telePhone.setPhone(etPhone.getText().toString());
                                                    Toast.makeText(viewHolder.itemView.getContext(), R.string.success_customer_service_modify, Toast.LENGTH_LONG).show();
                                                    notifyDataSetChanged();
                                                }else{
                                                    fragment.showMessage(ResponseUtils.getMsg(response.body()));
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();
                    }
                });

                v.findViewById(R.id.btn_cancle).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return telephones.size();
    }
}
