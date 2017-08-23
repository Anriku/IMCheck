package com.anriku.imcheck.MainInterface.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Anriku on 2017/8/23.
 */

public class GroupApply implements Serializable {
    private List<String> groupIds;
    private List<String> groupNames;
    private List<String> applyers;
    private List<String> reasons;

    public GroupApply(List<String> groupIds, List<String> groupNames, List<String> applyers, List<String> reasons) {
        this.groupIds = groupIds;
        this.groupNames = groupNames;
        this.applyers = applyers;
        this.reasons = reasons;
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<String> groupIds) {
        this.groupIds = groupIds;
    }

    public List<String> getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(List<String> groupNames) {
        this.groupNames = groupNames;
    }

    public List<String> getApplyers() {
        return applyers;
    }

    public void setApplyers(List<String> applyers) {
        this.applyers = applyers;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }
}
