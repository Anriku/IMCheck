package com.anriku.imcheck.MainInterface.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Anriku on 2017/8/23.
 */

public class FriendApply implements Serializable{

    private List<String> names;

    private List<String> reasons;

    public FriendApply(List<String> names, List<String> reasons) {
        this.names = names;
        this.reasons = reasons;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }
}
