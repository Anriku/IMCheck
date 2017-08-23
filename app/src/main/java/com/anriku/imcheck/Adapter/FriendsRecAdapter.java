package com.anriku.imcheck.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anriku.imcheck.MainInterface.View.MessageActivity;
import com.anriku.imcheck.R;
import com.anriku.imcheck.Utils.PopupWindowUtil;
import com.anriku.imcheck.databinding.FriendsRecItemBinding;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anriku on 2017/8/17.
 */

public class FriendsRecAdapter extends RecyclerView.Adapter<FriendsRecAdapter.ViewHolder> {

    private Context context;
    private List<String> names;
    private String[] invites;
    private List<String> listInvites;
    private String groupId;
    public static final int FRIEND = 0;
    public static final int BLACK = 1;
    public static final int ADMIN = 3;
    public static final int MEMBER = 4;
    public static final int INVITE = 5;
    private int type;

    public FriendsRecAdapter(Context context, List<String> names, int type) {
        this.context = context;
        this.names = names;
        this.type = type;
        listInvites = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        FriendsRecItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.friends_rec_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.binding.friendsRecItemTv.setText(names.get(position));


        if (type == FRIEND || type == BLACK) {
            //进行对话界面的跳转
            holder.binding.friendsRecItemTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MessageActivity.class);
                    intent.putExtra("obj", names.get(position));
                    intent.putExtra("is_group", false);
                    context.startActivity(intent);
                }
            });
            setLongClick(holder, position);
        } else if (type == ADMIN) {
            holder.binding.friendsRecItemDelete.setVisibility(View.VISIBLE);
            holder.binding.friendsRecItemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Observable.create(new ObservableOnSubscribe<String>() {
                        @Override
                        public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                            EMClient.getInstance().groupManager().removeGroupAdmin(groupId, names.get(position));
                        }
                    })
                            .subscribeOn(Schedulers.io())
                            .subscribeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    Toast.makeText(context, "取消管理员成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        } else if (type == MEMBER) {
            holder.binding.friendsRecItemDelete.setVisibility(View.GONE);
            holder.binding.friendsRecItemAdd.setVisibility(View.VISIBLE);

            holder.binding.friendsRecItemAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Observable.create(new ObservableOnSubscribe<String>() {
                        @Override
                        public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                            EMClient.getInstance().groupManager().addGroupAdmin(groupId, names.get(position));
                            e.onNext("添加成功");
                        }
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        } else if (type == INVITE) {
            holder.binding.friendsRecItemDelete.setVisibility(View.GONE);
            holder.binding.friendsRecItemAdd.setVisibility(View.GONE);

            holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listInvites.contains(names.get(position))){
                        invites = new String[names.size()];
                        invites[position] = null;
                        listInvites.remove(names.get(position));
                        holder.binding.getRoot().setBackgroundColor(Color.parseColor("#ffffff"));
                    }else {
                        invites = new String[names.size()];
                        invites[position] = names.get(position);
                        listInvites.add(names.get(position));
                        holder.binding.getRoot().setBackgroundColor(Color.parseColor("#333333"));
                    }
                }
            });
        }
    }

    private void setLongClick(final ViewHolder holder, final int position) {
        holder.binding.friendsRecItemTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final PopupWindowUtil popupWindowUtil = PopupWindowUtil.getInstance(R.layout.friends_manager,
                        R.style.Popupwindow, LayoutInflater.from(context), ((Activity) context).getWindow(),
                        PopupWindowUtil.BOTTOM);

                FrameLayout frameLayout = popupWindowUtil.getFrameLayout();
                TextView firstTv = frameLayout.findViewById(R.id.friends_manager_first_tv);
                TextView secondTv = frameLayout.findViewById(R.id.friends_manager_second_tv);
                popupWindowUtil.bottomWindow(holder.binding.friendsRecItemTv);

                if (type == FRIEND) {
                    //联系人部分的逻辑操作
                    firstTv.setVisibility(View.VISIBLE);
                    secondTv.setVisibility(View.VISIBLE);
                    firstTv.setText("删除好友");
                    secondTv.setText("加入黑名单");


                    firstTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                EMClient.getInstance().contactManager().deleteContact(names.get(position));
                                popupWindowUtil.getPopupWindow().dismiss();

                                Toast.makeText(context, "删除好友成功", Toast.LENGTH_SHORT).show();
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "删除好友失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    secondTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                EMClient.getInstance().contactManager().addUserToBlackList(names.get(position), true);
                                popupWindowUtil.getPopupWindow().dismiss();
                                Toast.makeText(context, "加入黑名单成功", Toast.LENGTH_SHORT).show();
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "加入黑名单失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else if (type == BLACK) {
                    //黑名单部分的逻辑操作
                    firstTv.setVisibility(View.VISIBLE);
                    secondTv.setVisibility(View.GONE);

                    firstTv.setText("移回好友");


                    firstTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                EMClient.getInstance().contactManager().removeUserFromBlackList(names.get(position));
                                Toast.makeText(context, "移回成功", Toast.LENGTH_SHORT).show();
                                names.remove(position);
                                notifyDataSetChanged();
                                popupWindowUtil.getPopupWindow().dismiss();
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "移回失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                return true;
            }
        });
    }

    public String[] getInvites() {
        Log.e("getInvites: ", String.valueOf(invites.length));
        return invites;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private FriendsRecItemBinding binding;

        public ViewHolder(FriendsRecItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
