package org.apache.myfaces.cactus;

import java.util.ArrayList;
import java.util.List;

public class ArticleThreadMBean {
    private String title;
    private String replyTitle;
    private String result;
    private ArticleThreadTableMBean parent;

    void setParent(ArticleThreadTableMBean parent) {
        this.parent = parent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        replyTitle = "Re: " + title;
    }

    public void setReplyTitle(String replyTitle) {
        this.replyTitle = replyTitle;
    }

    public String getReplyTitle() {
        return replyTitle;
    }

    public String reply() {
        parent.setResult(replyTitle);
        return "";
    }
}
