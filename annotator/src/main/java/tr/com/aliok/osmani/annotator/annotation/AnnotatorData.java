package tr.com.aliok.osmani.annotator.annotation;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
@ManagedBean(name = "annotatorData")
@ViewScoped
public class AnnotatorData implements Serializable {

    private String currentFileId;
    private int currentPageNumber;

    public String getCurrentFileId() {
        return currentFileId;
    }

    public void setCurrentFileId(String currentFileId) {
        this.currentFileId = currentFileId;
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    public void setCurrentPageNumber(int currentPageNumber) {
        this.currentPageNumber = currentPageNumber;
    }
}
