package com.anriku.imcheck.MainInterface.Presenter.GroupSet;

import android.content.Context;
import android.widget.Toast;

import com.anriku.imcheck.MainInterface.Interface.GroupSet.INoticeModifyAct;
import com.anriku.imcheck.MainInterface.Interface.GroupSet.INoticeModifyPre;
import com.anriku.imcheck.databinding.ActivityNoticeModifyBinding;
import com.hyphenate.chat.EMClient;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anriku on 2017/8/23.
 */

public class NoticeModifyPresenter implements INoticeModifyPre {

    private INoticeModifyAct iNoticeModifyAct;

    public NoticeModifyPresenter(INoticeModifyAct iNoticeModifyAct) {
        this.iNoticeModifyAct = iNoticeModifyAct;
    }

    @Override
    public void finishWrite(final Context context, final ActivityNoticeModifyBinding binding, final String obj) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                EMClient.getInstance().groupManager().updateGroupAnnouncement(obj, binding.acNoticeEt.getText().toString());
                e.onNext("更新成功");
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
