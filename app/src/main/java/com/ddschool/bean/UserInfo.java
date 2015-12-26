package com.ddschool.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/12/26.
 */
public class UserInfo extends BwResult {
    private UserInfoData data;

    public UserInfoData getData() {
        return data;
    }

    public void setData(UserInfoData data) {
        this.data = data;
    }

    private class UserInfoData extends BaseUserInfo{

        private List<UserRole> roles;
        private List<UserClass> classes;
        private List<UserChild> children;

        public List<UserChild> getChildren() {
            return children;
        }

        public void setChildren(List<UserChild> children) {
            this.children = children;
        }

        public List<UserClass> getClasses() {
            return classes;
        }

        public void setClasses(List<UserClass> classes) {
            this.classes = classes;
        }

        public List<UserRole> getRoles() {
            return roles;
        }

        public void setRoles(List<UserRole> roles) {
            this.roles = roles;
        }

    }
}
