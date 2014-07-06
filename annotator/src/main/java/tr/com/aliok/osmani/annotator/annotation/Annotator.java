package tr.com.aliok.osmani.annotator.annotation;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;
import org.primefaces.context.RequestContext;
import tr.com.aliok.osmani.annotator.model.Annotation;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
@ManagedBean(name = "annotator")
@ViewScoped
public class Annotator implements Serializable {

    @ManagedProperty("#{annotatorData}")
    private AnnotatorData annotatorData;

    @ManagedProperty("#{annotationDataController}")
    private AnnotationDataController annotationDataController;

    private final AnnotationJSONFormatter annotationJSONFormatter = new AnnotationJSONFormatter();

    @PostConstruct
    public void initialize() {
        annotatorData.setCurrentFileId(annotationDataController.getFileIds().iterator().next());
    }

    public ArrayList<String> getAllFileIds() {
        return Lists.newArrayList(annotationDataController.getFileIds());
    }

    public void selectFile(String fileId) throws IOException {
        annotatorData.setCurrentFileId(fileId);
        annotatorData.setCurrentPageNumber(0);
    }

    public String selectPage(int i) {
        annotatorData.setCurrentPageNumber(i);
        return null;
    }

    public String nextPage() {
        Validate.isTrue(isNextPageAvailable());
        return selectPage(annotatorData.getCurrentPageNumber() + 1);
    }

    public String previousPage() {
        Validate.isTrue(isPreviousPageAvailable());
        return selectPage(annotatorData.getCurrentPageNumber() - 1);
    }

    public boolean isNextPageAvailable() {
        final int numberOfPagesForCurrentFile = numberOfPagesForCurrentFile();
        return numberOfPagesForCurrentFile - 1 > annotatorData.getCurrentPageNumber();
    }

    public boolean isPreviousPageAvailable() {
        return annotatorData.getCurrentPageNumber() > 0;
    }

    public int numberOfPagesForCurrentFile() {
        return annotationDataController.getNumberOfPagesForCurrentFile(annotatorData.getCurrentFileId());
    }

    public String getAnnotationsJSONForCurrentFileAndPage() {
        final TreeSet<Annotation> annotations = this.annotationDataController.getAnnotations(annotatorData.getCurrentFileId(), annotatorData.getCurrentPageNumber());
        if(annotations!=null)
            return this.annotationJSONFormatter.getJSON(annotations);
        else
            return "[]";
    }

    public void onFileSelect() throws IOException {
        selectFile(annotatorData.getCurrentFileId());
    }

    public void onPageSelect() {
        int page = annotatorData.getCurrentPageNumber();
        if (page < 0)
            page = 0;
        if (page >= numberOfPagesForCurrentFile())
            page = numberOfPagesForCurrentFile() - 1;

        selectPage(page);
    }

    public void forceFlush() throws IOException {
        this.annotationDataController.doFlush();
    }

    public void createNewAnnotation() {
        Map<String, String> requestParamMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        int x = getRequiredIntegerRequestParam(requestParamMap, "x");
        int y = getRequiredIntegerRequestParam(requestParamMap, "y");
        int w = getRequiredIntegerRequestParam(requestParamMap, "w");
        int h = getRequiredIntegerRequestParam(requestParamMap, "h");
        final String tr_latin = getStringRequestParam(requestParamMap, "tr_latin");
        final String tr_arabic = getStringRequestParam(requestParamMap, "tr_arabic");

        // tr_latin is optional
        Validate.notNull(tr_arabic);

        // negative is also ok if selection direction is right to left
        Validate.isTrue(w != 0);
        Validate.isTrue(h != 0);

        // make sure direction of the annotation is left to right and top to bottom
        if (w < 0) {
            x = x + w;
            w = -w;
        }

        if (h < 0) {
            y = y + h;
            h = -h;
        }


        final String annotationId = UUID.randomUUID().toString();
        RequestContext.getCurrentInstance().addCallbackParam("annotationId", annotationId);
        final Annotation annotation = annotationDataController.addNew(annotatorData.getCurrentFileId(), annotatorData.getCurrentPageNumber(), x, y, w, h, tr_latin, tr_arabic, annotationId);
        System.out.printf("Created : " + annotation.toString() + "\n");
    }

    private String getStringRequestParam(Map<String, String> requestParamMap, String key) {
        final String value = requestParamMap.get(key);
        if (value == null)
            return null;
        return value.trim();
    }

    private int getRequiredIntegerRequestParam(Map<String, String> requestParamMap, String key) {
        final String strVal = requestParamMap.get(key);
        Validate.notNull(strVal);
        Validate.isTrue(NumberUtils.isNumber(strVal));
        return Integer.parseInt(strVal);
    }

    public void setAnnotatorData(AnnotatorData annotatorData) {
        this.annotatorData = annotatorData;
    }

    public void setAnnotationDataController(AnnotationDataController annotationDataController) {
        this.annotationDataController = annotationDataController;
    }
}
