package com.dachen.imsdk.vchat.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.dachen.common.adapter.ViewHolder;
import com.dachen.common.utils.ToastUtil;
import com.dachen.imsdk.vchat.R;;
import com.dachen.imsdk.entity.GroupInfo2Bean.Data.UserInfo;
import com.dachen.imsdk.utils.ImUtils;
import com.dachen.imsdk.vchat.VChatManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashSet;
import java.util.List;

/**
 * Created by Mcp on 2016/3/25.
 */
public class VChatMemberAdapter extends BaseAdapter {
    private List<UserInfo> memberList;
    private Context context;
    private HashSet<UserInfo> inviteList;
    private HashSet<String> oldList = new HashSet<>();
//    private LayoutInflater mInflater;

    public VChatMemberAdapter(Context context, List<UserInfo> memberList, List<String> oldMember) {
        this.memberList = memberList;
        this.context = context;
        inviteList = new HashSet<>();
        setOldMember(oldMember);
//        mInflater=LayoutInflater.from(context);
    }

    public void setOldMember(List<String> oldMember) {
        if (oldMember == null) return;
        oldList.clear();
        oldList.addAll(oldMember);
        notifyDataSetChanged();
    }

    public HashSet<UserInfo> getInviteList() {
        return inviteList;
    }

    @Override
    public int getCount() {
        return memberList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(context, convertView, parent, R.layout.vchat_member_item, position);
        convertView = holder.getConvertView();
        RadioButton rb = holder.getView(R.id.btn_radio);
        final UserInfo user = memberList.get(position);
        ImageLoader.getInstance().displayImage(user.pic, (ImageView) holder.getView(R.id.img), ImUtils.getNormalImageOptions());
        holder.setText(R.id.title, user.name);
        if (ImUtils.getLoginUserId().equals(user.id) || oldList.contains(user.id)) {
            rb.setBackgroundResource(R.drawable.pay_disable);
            convertView.setOnClickListener(null);
        } else {
            rb.setBackgroundResource(R.drawable.selector_btn_radio);
            rb.setChecked(inviteList.contains(user));
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inviteList.contains(user)) {
                        inviteList.remove(user);
                    } else {
                        List<UserInfo> allUserList = VChatManager.getInstance().allUserList;
                        int count = 0;//已经存在的成员
                        if (allUserList != null) {
                            count = allUserList.size();
                        }
                        if (inviteList.size() + 1 + count > 8) {
                            ToastUtil.showToast(context, "您最多可选择9名成员");
                            return;
                        }

                        inviteList.add(user);
                    }
                    notifyDataSetChanged();
                }
            });
        }
        return convertView;
    }
}
