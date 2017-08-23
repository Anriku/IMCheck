package com.anriku.imcheck.MainInterface.View;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.anriku.imcheck.MainInterface.Interface.IMainInterfaceAct;
import com.anriku.imcheck.MainInterface.Interface.IMainInterfacePre;
import com.anriku.imcheck.MainInterface.Presenter.MainInterfacePresenter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ActivityMainInterfaceBinding;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

public class MainInterfaceActivity extends AppCompatActivity implements IMainInterfaceAct{

    private IMainInterfacePre iMainInterfacePre;
    private ActivityMainInterfaceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main_interface);
        iMainInterfacePre = new MainInterfacePresenter(this);

        setSupportActionBar(binding.acMainInterfaceTb);

        iMainInterfacePre.setFragments(getSupportFragmentManager(),binding);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mian_interface_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_friend:{
                startActivity(new Intent(this,SearchFriendActivity.class));
                break;
            }
            case R.id.add_group:{
                startActivity(new Intent(this,SelectGroupActivity.class));
                break;
            }
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        //在退出主界面时登出
        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.d("MainInterface","退出成功");
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(int code, String message) {
            }
        });
        super.onDestroy();
    }
}
