package org.apache.myfaces.cactus;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class ArticleThreadTableMBean {
    private String result;

    public List getArticleThreadTable() {
        List ret = new ArrayList();
        ArticleThreadMBean thread = new ArticleThreadMBean();
        thread.setTitle("First Title");
        thread.setParent(this);
        ret.add(thread);
        thread = new ArticleThreadMBean();
        thread.setTitle("Second Title");
        thread.setParent(this);
        ret.add(thread);
        return Collections.unmodifiableList(ret);
    }

    void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
