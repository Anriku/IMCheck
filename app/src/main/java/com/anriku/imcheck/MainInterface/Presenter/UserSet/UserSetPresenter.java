package com.anriku.imcheck.MainInterface.Presenter.UserSet;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anriku.imcheck.MainInterface.Interface.UserSet.IUserSetAct;
import com.anriku.imcheck.MainInterface.Interface.UserSet.IUserSetPre;
import com.anriku.imcheck.R;
import com.anriku.imcheck.Utils.PopupWindowUtil;
import com.anriku.imcheck.databinding.ActivityUserSetBinding;
import com.hyphenate.chat.EMClient;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anriku on 2017/8/25.
 */

public class UserSetPresenter implements IUserSetPre {

    private IUserSetAct iUserSetAct;

    public UserSetPresenter(IUserSetAct iUserSetAct) {
        this.iUserSetAct = iUserSetAct;
    }

    @Override
    public void changNickName(final Context context, final Window window, ActivityUserSetBinding binding) {
        binding.acUserSetNickNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                Window dialogWindow = dialog.getWindow();
                WindowManager.LayoutParams params = dialogWindow.getAttributes();
                params.gravity = Gravity.CENTER;
                dialogWindow.setAttributes(params);
                dialog.setContentView(R.layout.nick_name_dialog_window);
                final EditText et = dialog.findViewById(R.id.nick_name_dialog_window_et);
                Button bt = dialog.findViewById(R.id.nick_name_dialog_window_bt);
                dialog.show();

                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                                EMClient.getInstance().pushManager().updatePushNickname(et.getText().toString());
                                e.onNext("修改成功");
                            }
                        })
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        dialog.dismiss();
                                        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }
        });
    }
}
