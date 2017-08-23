package com.anriku.imcheck.MainInterface.Presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.anriku.imcheck.Adapter.FriendsRecAdapter;
import com.anriku.imcheck.MainInterface.Interface.IBlackFrg;
import com.anriku.imcheck.MainInterface.Interface.IBlackPre;
import com.anriku.imcheck.databinding.FragmentBlackBinding;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anriku on 2017/8/22.
 */

public class BlackPresenter implements IBlackPre {

    private IBlackFrg iBlackFrg;

    public BlackPresenter(IBlackFrg iBlackFrg) {
        this.iBlackFrg = iBlackFrg;
    }

    @Override
    public void getBlackList(final Context context, final FragmentBlackBinding binding) {
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<String>> e) throws Exception {
                try {
                    List<String> names = EMClient.getInstance().contactManager().getBlackListFromServer();
                    e.onNext(names);
                } catch (HyphenateException ex) {
                    ex.printStackTrace();
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        LinearLayoutManager manager = new LinearLayoutManager(context);
                        FriendsRecAdapter adapter = new FriendsRecAdapter(context, strings,FriendsRecAdapter.BLACK);
                        binding.frgBlackRv.setLayoutManager(manager);
                        binding.frgBlackRv.setAdapter(adapter);
                    }
                });
    }
}
