package com.express.subao.adaptera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.express.subao.R;
import com.express.subao.box.ShoppingCarObj;
import com.express.subao.box.StoreItemObj;
import com.express.subao.box.StoreObj;
import com.express.subao.box.handlers.ShoppingCarHandler;
import com.express.subao.dao.DBHandler;
import com.express.subao.dialogs.MessageDialog;
import com.express.subao.download.DownloadImageLoader;
import com.express.subao.tool.WinTool;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * *
 * * ┏┓      ┏┓
 * *┏┛┻━━━━━━┛┻┓
 * *┃          ┃
 * *┃          ┃
 * *┃ ┳┛   ┗┳  ┃
 * *┃          ┃
 * *┃    ┻     ┃
 * *┃          ┃
 * *┗━┓      ┏━┛
 * *  ┃      ┃
 * *  ┃      ┃
 * *  ┃      ┗━━━┓
 * *  ┃          ┣┓
 * *  ┃         ┏┛
 * *  ┗┓┓┏━━━┳┓┏┛
 * *   ┃┫┫   ┃┫┫
 * *   ┗┻┛   ┗┻┛
 * Created by Hua on 16/7/5.
 */
public class ShoppingCarAdapter extends BaseAdapter {

    private final static int TYPE_COUNT = 2;

    private Context context;
    private List<ShoppingCarObj> itemList;
    private LayoutInflater inflater;

    private List<ShoppingCarObj> choiceList;
    private boolean isSetting;

    private NotifyListener mNotifyListener;

    public ShoppingCarAdapter(Context context, List<ShoppingCarObj> itemList, NotifyListener mNotifyListener) {
        initAdapter(context);
        this.itemList = itemList;
        this.choiceList = new ArrayList<>();
        this.mNotifyListener = mNotifyListener;
    }

    private void initAdapter(Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.isSetting = false;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (mNotifyListener != null) {
            mNotifyListener.onNotify(getChoiseItemList(), isSetting(), isChoiceAll());
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        StoreHolder storeHolder;
        StoreItemHolder storeItemHolder;

        ShoppingCarObj obj = (ShoppingCarObj) getItem(position);

        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case ShoppingCarObj.STORE:
                    convertView = inflater.inflate(R.layout.layout_shoppinacar_store_item, null);
                    storeHolder = new StoreHolder(convertView);
                    setStoreLayoutMessage(storeHolder, obj, position);
                    convertView.setTag(storeHolder);
                    break;
                case ShoppingCarObj.ITEM:
                    convertView = inflater.inflate(R.layout.layout_shoppinacar_items_item, null);
                    storeItemHolder = new StoreItemHolder(convertView);
                    setItemLayoutMessage(storeItemHolder, obj, position);
                    convertView.setTag(storeItemHolder);
                    break;
            }
        } else {
            switch (type) {
                case ShoppingCarObj.STORE:
                    storeHolder = (StoreHolder) convertView.getTag();
                    setStoreLayoutMessage(storeHolder, obj, position);
                    break;
                case ShoppingCarObj.ITEM:
                    storeItemHolder = (StoreItemHolder) convertView.getTag();
                    setItemLayoutMessage(storeItemHolder, obj, position);
                    break;
            }
        }

        return convertView;
    }

    private void setStoreLayoutMessage(StoreHolder holder, ShoppingCarObj obj, int p) {
        holder.storeName.setText(obj.getStoreObj().getTitle());
        holder.status.setVisibility(View.GONE);
//        holder.delivery.setVisibility(View.GONE);

        holder.allChoice.setImageResource(R.drawable.choice_off_icon);
        if (isAllStoreItemChoice(obj.getStoreObj().getObjectId())) {
            holder.allChoice.setImageResource(R.drawable.choice_on_icon);
        }

        setOnStoreChoice(holder.allChoice, obj);
    }

    private boolean isAllStoreItemChoice(String id) {
        List<ShoppingCarObj> list = getAllStoreItemList(id);
        for (ShoppingCarObj obj : list) {
            if (!isChoice(obj)) {
                return false;
            }
        }
        return true;
    }

    private void setOnStoreChoice(ImageView view, final ShoppingCarObj obj) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ShoppingCarObj> list = getAllStoreItemList(obj.getStoreObj().getObjectId());
                if (isChoice(obj)) {
                    removeChoiceList(obj);
                    removeChoiceList(list);
                } else {
                    addChoiceList(obj);
                    addChoiceList(list);
                }
                notifyDataSetChanged();
            }
        });
    }

    private List<ShoppingCarObj> getAllStoreItemList(String id) {
        List<ShoppingCarObj> list = new ArrayList<>();
        for (ShoppingCarObj obj : itemList) {
            if (obj.isItem()) {
                StoreItemObj item = obj.getStoreItemObj();
                if (item.isStore(id)) {
                    list.add(obj);
                }
            }
        }
        return list;
    }

    private void setItemLayoutMessage(StoreItemHolder holder, ShoppingCarObj obj, int p) {
        holder.title.setText(obj.getStoreItemObj().getTitle());
        holder.sell.setText("已售 " + obj.getStoreItemObj().getSell());
        holder.price.setText("MOB " + obj.getStoreItemObj().getPrice());
        holder.sum.setText("x " + obj.getStoreItemObj().getSum());
        holder.settingSum.setText(String.valueOf(obj.getStoreItemObj().getSum()));

        int w = WinTool.getWinWidth(context) / 4;
        holder.image.setLayoutParams(new RelativeLayout.LayoutParams(w, w));
        DownloadImageLoader.loadImage(holder.image, obj.getStoreItemObj().getCover());

        holder.warnLayout.setVisibility(View.GONE);

        holder.aboveLine.setVisibility(View.INVISIBLE);
        if (getCount() > p + 1) {
            if (((ShoppingCarObj) getItem(p + 1)).isItem()) {
                holder.aboveLine.setVisibility(View.VISIBLE);
            }
        }

        holder.choice.setImageResource(R.drawable.choice_off_icon);
        if (isChoice(obj)) {
            holder.choice.setImageResource(R.drawable.choice_on_icon);
        }

        if (isSetting) {
            holder.toolLayout.setVisibility(View.VISIBLE);
            holder.messageLayout.setVisibility(View.GONE);
        } else {
            holder.toolLayout.setVisibility(View.GONE);
            holder.messageLayout.setVisibility(View.VISIBLE);
        }

        setOnItemChoice(holder.choice, obj);
        setOnSettingSub(holder.settingSub, obj);
        setOnSettingAdd(holder.settingAdd, obj);
    }

    private void setOnSettingAdd(TextView view, final ShoppingCarObj obj) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sum = obj.getStoreItemObj().getSum();
                sum += 1;
                obj.getStoreItemObj().setSum(sum);
                notifyDataSetChanged();
            }
        });
    }

    private void setOnSettingSub(TextView view, final ShoppingCarObj obj) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sum = obj.getStoreItemObj().getSum();
                if (sum - 1 == 0) {
                    showDeleteDialog(obj);
                } else {
                    sum -= 1;
                    obj.getStoreItemObj().setSum(sum);
                    notifyDataSetChanged();
                }

            }
        });
    }

    private void showDeleteDialog(final ShoppingCarObj obj) {
        MessageDialog dialog = new MessageDialog(context);
        dialog.setMessage("是否刪除該商品？");
        dialog.setCommitStyle("確定");
        dialog.setCommitListener(new MessageDialog.CallBackListener() {
            @Override
            public void callback() {
                deleteItem(obj, true);
            }
        });
        dialog.setCancelStyle("取消");
        dialog.setCancelListener(null);
    }

    private void deleteItem(ShoppingCarObj obj, boolean b) {
        itemList.remove(obj);
        deleteItem(obj.getStoreItemObj());
        if (b) {
            notifyDataSetChanged();
        }
    }

    private void deleteItem(StoreItemObj obj) {
        ShoppingCarHandler.deleteItem(context, obj);
        List<ShoppingCarObj> list = getAllStoreItemList(obj.getStoreId());
        if (list.isEmpty()) {
            itemList.remove(getStoreForId(obj.getStoreId()));
        }
    }

    private void setOnItemChoice(ImageView view, final ShoppingCarObj obj) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChoice(obj)) {
                    removeChoiceList(obj);
                } else {
                    addChoiceList(obj);
                }

                ShoppingCarObj store = getStoreForId(obj.getStoreItemObj().getStoreId());
                if (store != null) {
                    if (isAllStoreItemChoice(obj.getStoreItemObj().getStoreId())) {
                        addChoiceList(store);
                    } else {
                        removeChoiceList(store);
                    }
                }

                notifyDataSetChanged();
            }
        });
    }

    private void removeChoiceList(ShoppingCarObj obj) {
        if (isChoice(obj)) {
            choiceList.remove(obj);
        }
    }

    private void removeChoiceList(List<ShoppingCarObj> list) {
        for (ShoppingCarObj obj : list) {
            removeChoiceList(obj);
        }
    }

    private void addChoiceList(ShoppingCarObj obj) {
        if (!isChoice(obj)) {
            choiceList.add(obj);
        }
    }

    private void addChoiceList(List<ShoppingCarObj> list) {
        for (ShoppingCarObj obj : list) {
            addChoiceList(obj);
        }
    }

    private ShoppingCarObj getStoreForId(String id) {
        for (ShoppingCarObj obj : itemList) {
            if (obj.isStore()) {
                if (obj.getStoreObj().equals(id)) {
                    return obj;
                }
            }
        }
        return null;
    }

    public boolean isChoice(ShoppingCarObj obj) {
        return choiceList.contains(obj);
    }

    public boolean isSetting() {
        return isSetting;
    }

    public void setSetting(boolean b) {
        isSetting = b;
        notifyDataSetChanged();
    }

    public void saveAll() {
        ShoppingCarHandler.updateShoppingCar(context, getAllItemList());
    }

    public List<StoreItemObj> getAllItemList() {
        List<StoreItemObj> list = new ArrayList<>();
        for (ShoppingCarObj obj : itemList) {
            if (obj.isItem()) {
                list.add(obj.getStoreItemObj());
            }
        }
        return list;
    }

    public List<StoreItemObj> getChoiseItemList() {
        List<StoreItemObj> list = new ArrayList<>();
        for (ShoppingCarObj obj : choiceList) {
            if (obj.isItem()) {
                list.add(obj.getStoreItemObj());
            }
        }
        return list;
    }

    public void choiceAll() {
        removeAllChoice(false);
        choiceList.addAll(itemList);
        notifyDataSetChanged();
    }

    public void removeAllChoice() {
        removeAllChoice(true);
    }

    private void removeAllChoice(boolean b) {
        choiceList.removeAll(choiceList);
        if (b) {
            notifyDataSetChanged();
        }
    }

    public boolean isChoiceAll() {
        for (ShoppingCarObj obj : itemList) {
            if (obj.isStore()) {
                if (!isAllStoreItemChoice(obj.getStoreObj().getObjectId())) {
                    return false;
                }
            }
        }
        return true;
    }

    public void deleteChoice() {
        MessageDialog dialog = new MessageDialog(context);
        dialog.setMessage("是否刪除選擇商品？");
        dialog.setCommitStyle("確定");
        dialog.setCommitListener(new MessageDialog.CallBackListener() {
            @Override
            public void callback() {
                for (ShoppingCarObj obj : choiceList) {
                    if (obj.isItem()) {
                        deleteItem(obj, false);
                    }
                }
                removeAllChoice();
            }
        });
        dialog.setCancelStyle("取消");
        dialog.setCancelListener(null);
    }

    public Map<String, List<StoreItemObj>> getChoiseItemMap() {
        Map<String, List<StoreItemObj>> map = new HashMap<>();
        for (ShoppingCarObj obj : itemList) {
            if (obj.isStore()) {
                List<StoreItemObj> list = getChoiseItemForStoreList(obj.getStoreObj().getObjectId());
                if (!list.isEmpty()) {
                    map.put(obj.getStoreObj().getObjectId(), list);
                }
            }
        }
        return map;
    }

    private List<StoreItemObj> getChoiseItemForStoreList(String id) {
        List<StoreItemObj> list = new ArrayList<>();
        for (ShoppingCarObj obj : choiceList) {
            if (obj.isItem()) {
                StoreItemObj item = obj.getStoreItemObj();
                if (item.isStore(id)) {
                    list.add(item);
                }
            }
        }
        return list;
    }

    class StoreHolder {
        ImageView allChoice;
        TextView storeName;
        TextView status;
        TextView delivery;

        StoreHolder(View view) {
            allChoice = (ImageView) view.findViewById(R.id.sCar_item_allChoice);
            storeName = (TextView) view.findViewById(R.id.sCar_item_shopName);
            status = (TextView) view.findViewById(R.id.sCar_item_status);
            delivery = (TextView) view.findViewById(R.id.sCar_item_delivery);
        }
    }

    class StoreItemHolder {
        ImageView image;
        ImageView choice;
        TextView noStock;
        TextView title;
        TextView sell;
        TextView price;
        TextView sum;
        TextView settingSum;
        TextView settingSub;
        TextView settingAdd;
        TextView warnMessage;
        LinearLayout messageLayout;
        LinearLayout toolLayout;
        LinearLayout warnLayout;
        View aboveLine;

        StoreItemHolder(View view) {
            image = (ImageView) view.findViewById(R.id.sCar_item_image);
            choice = (ImageView) view.findViewById(R.id.sCar_item_choice);
            noStock = (TextView) view.findViewById(R.id.sCar_item_noStock);
            title = (TextView) view.findViewById(R.id.sCar_item_title);
            sell = (TextView) view.findViewById(R.id.sCar_item_sell);
            price = (TextView) view.findViewById(R.id.sCar_item_price);
            sum = (TextView) view.findViewById(R.id.sCar_item_sum);
            settingSum = (TextView) view.findViewById(R.id.sCar_item_num);
            settingSub = (TextView) view.findViewById(R.id.sCar_item_sub);
            settingAdd = (TextView) view.findViewById(R.id.sCar_item_add);
            warnMessage = (TextView) view.findViewById(R.id.sCar_item_warnMessage);
            messageLayout = (LinearLayout) view.findViewById(R.id.sCar_item_messageLayout);
            toolLayout = (LinearLayout) view.findViewById(R.id.sCar_item_toolLayout);
            warnLayout = (LinearLayout) view.findViewById(R.id.sCar_item_warn);
            aboveLine = view.findViewById(R.id.sCar_item_aboveLine);
        }
    }

    public interface NotifyListener {
        public void onNotify(List<StoreItemObj> list, boolean isSetting, boolean isChoiceAll);
    }


}
