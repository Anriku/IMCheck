package com.anriku.imcheck.MainInterface.Presenter.GroupSet;

import android.content.Context;

import com.anriku.imcheck.MainInterface.Interface.GroupSet.INoticeAct;
import com.anriku.imcheck.MainInterface.Interface.GroupSet.INoticePre;
import com.anriku.imcheck.databinding.ActivityNoticeBinding;
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

public class NoticePresenter implements INoticePre {

    private INoticeAct iNoticeAct;

    public NoticePresenter(INoticeAct iNoticeAct) {
        this.iNoticeAct = iNoticeAct;
    }

    @Override
    public void getNotice(Context context, final ActivityNoticeBinding binding, final String obj) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                String notice = EMClient.getInstance().groupManager().fetchGroupAnnouncement(obj);
                e.onNext(notice);
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        binding.acNoticeTv.setText(s);
                    }
                });
    }
}
