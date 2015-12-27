package com.ddschool.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/12/26.
 */
public class NoticeList extends BwResult{
    private ArrayList<NoticeListItem> data;

    public ArrayList<NoticeListItem> getData() {
        return data;
    }

    public void setData(ArrayList<NoticeListItem> data) {
        this.data = data;
    }

    public class NoticeListItem{
        private String nid;
        private int type;
        private String depname;
        private String title;
        private String date;
        private int isread;

        public String getNid() {
            return nid;
        }

        public void setNid(String nid) {
            this.nid = nid;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getDepname() {
            return depname;
        }

        public void setDepname(String depname) {
            this.depname = depname;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getIsread() {
            return isread;
        }

        public void setIsread(int isread) {
            this.isread = isread;
        }
    }
}
