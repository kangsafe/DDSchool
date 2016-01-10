package com.ddschool.bean;

/**
 * Created by Administrator on 2016/1/9.
 */
public class NoticeDetail extends BwResult {
private NoticeDetailData data;

    public NoticeDetailData getData() {
        return data;
    }

    public void setData(NoticeDetailData data) {
        this.data = data;
    }

    public class NoticeDetailData{
        private String nid;
        private String title;
        private String date;
        private String depname;
        private String content;
        private String tname;
        private String tid;
        private String tphone;
        private String[] files;

        public String getNid() {
            return nid;
        }

        public void setNid(String nid) {
            this.nid = nid;
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

        public String getDepname() {
            return depname;
        }

        public void setDepname(String depname) {
            this.depname = depname;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getTphone() {
            return tphone;
        }

        public void setTphone(String tphone) {
            this.tphone = tphone;
        }
    }
}
