package tr.com.aliok.osmani.annotator.annotation;

import tr.com.aliok.osmani.annotator.model.Annotation;

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

    private Annotation current;

    private boolean batchMode = true;
    private boolean ignoreEmptyLatin = true;
    private boolean areaSelectionMode = false;

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

    public boolean isBatchMode() {
        return batchMode;
    }

    public void setBatchMode(boolean batchMode) {
        this.batchMode = batchMode;
    }

    public boolean isIgnoreEmptyLatin() {
        return ignoreEmptyLatin;
    }

    public void setIgnoreEmptyLatin(boolean ignoreEmptyLatin) {
        this.ignoreEmptyLatin = ignoreEmptyLatin;
    }

    public boolean isAreaSelectionMode() {
        return areaSelectionMode;
    }

    public void setAreaSelectionMode(boolean areaSelectionMode) {
        this.areaSelectionMode = areaSelectionMode;
    }

    public Annotation getCurrent() {
        return current;
    }

    public void setCurrent(Annotation current) {
        this.current = current;
    }
}
