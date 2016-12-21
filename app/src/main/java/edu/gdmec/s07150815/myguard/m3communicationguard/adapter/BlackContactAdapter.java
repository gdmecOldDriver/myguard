package edu.gdmec.s07150815.myguard.m3communicationguard.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.gdmec.s07150815.myguard.R;
import edu.gdmec.s07150815.myguard.m3communicationguard.db.dao.BlackNumberDao;
import edu.gdmec.s07150815.myguard.m3communicationguard.entity.BlackContactInfo;

/**
 * Created by 马达 on 2016/12/20.
 */
public class BlackContactAdapter extends BaseAdapter{
   private List<BlackContactInfo> contactInfos;
    private Context context;
    private BlackNumberDao dao;
    private BlackContactCallBack callBack;
    public void setCallBack(BlackContactCallBack callBack){
        this.callBack=callBack;
    }
    public BlackContactAdapter(List<BlackContactInfo> systemContacts,Context context){
        super();
        this.contactInfos=systemContacts;
        this.context=context;
        dao=new BlackNumberDao(context);
    }
    @Override
    public int getCount() {
        return contactInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return contactInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            convertView=View.inflate(context, R.layout.item_list_blackcontact,null);
            holder=new ViewHolder();
            holder.mNameTV= (TextView) convertView.findViewById(R.id.tv_black_name);
            holder.mModeTV= (TextView) convertView.findViewById(R.id.tv_black_mode);
            holder.mContactImgv=convertView.findViewById(R.id.view_black_icon);
            holder.mDeleteView=convertView.findViewById(R.id.view_black_delete);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.mNameTV.setText(contactInfos.get(position).contactName+"("+contactInfos.get(position).phoneNumber+")");
        holder.mModeTV.setText(contactInfos.get(position).getModeString(contactInfos.get(position).mode));
        holder.mNameTV.setTextColor(context.getResources().getColor(R.color.bright_purple));
        holder.mModeTV.setTextColor(context.getResources().getColor(R.color.bright_purple));
        holder.mContactImgv.setBackgroundResource(R.drawable.brightpurple_contact_icon);
        holder.mDeleteView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                boolean datele=dao.delete(contactInfos.get(position));
                if (datele){
                    contactInfos.remove(contactInfos.get(position));
                    BlackContactAdapter.this.notifyDataSetChanged();
                    //如果数据库中没有数据，则执行回调函数
                    if (dao.getTotalNumber()==0){
                        callBack.DataSizeChange();
                    }
                }else {
                    Toast.makeText(context,"删除失败!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return convertView;
    }
    class ViewHolder{
        TextView mNameTV;
        TextView mModeTV;
        View mContactImgv;
        View mDeleteView;
    }
    public interface BlackContactCallBack{
        void DataSizeChange();
    }
}
