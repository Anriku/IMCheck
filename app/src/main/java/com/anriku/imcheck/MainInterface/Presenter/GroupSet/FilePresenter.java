package com.anriku.imcheck.MainInterface.Presenter.GroupSet;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.anriku.imcheck.Adapter.FileRecAdapter;
import com.anriku.imcheck.MainInterface.Interface.GroupSet.IFileAct;
import com.anriku.imcheck.MainInterface.Interface.GroupSet.IFilePre;
import com.anriku.imcheck.databinding.ActivityFileBinding;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMucSharedFile;

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
 * Created by Anriku on 2017/8/23.
 */

public class FilePresenter implements IFilePre {

    private IFileAct iFileAct;
    private FileRecAdapter adapter;
    private List<EMMucSharedFile> sharedFiles = new ArrayList<>();

    public FilePresenter(IFileAct iFileAct) {
        this.iFileAct = iFileAct;
    }

    @Override
    public void getFiles(Context context, ActivityFileBinding binding, final String obj) {

        LinearLayoutManager manager = new LinearLayoutManager(context);
        adapter = new FileRecAdapter(context, sharedFiles);
        binding.acFileRv.setLayoutManager(manager);
        binding.acFileRv.setAdapter(adapter);

        Observable.create(new ObservableOnSubscribe<List<EMMucSharedFile>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<EMMucSharedFile>> e) throws Exception {
                List<EMMucSharedFile> files = EMClient.getInstance().groupManager().fetchGroupSharedFileList(obj, 0, 5);
                e.onNext(files);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<EMMucSharedFile>>() {
                    @Override
                    public void accept(List<EMMucSharedFile> emMucSharedFiles) throws Exception {
                        sharedFiles.addAll(emMucSharedFiles);
                        adapter.notifyDataSetChanged();
                    }
                });
    }


}
