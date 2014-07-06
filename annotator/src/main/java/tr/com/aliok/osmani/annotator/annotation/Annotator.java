package tr.com.aliok.osmani.annotator.annotation;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;
import org.primefaces.context.RequestContext;
import tr.com.aliok.osmani.annotator.model.Annotation;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
@ManagedBean(name = "annotator")
@ViewScoped
public class Annotator implements Serializable {

    private String currentFileId;
    private int currentPageNumber;

    @ManagedProperty("annotationDataController")
    private AnnotationDataController annotationDataController;

    public void selectFile(String fileId) throws IOException {
        this.currentFileId = fileId;
        this.currentPageNumber = 0;

    }

    public void selectPage(int i) {
        this.currentPageNumber = i;
    }

    public void nextPage() {
        Validate.isTrue(isNextPageAvailable());
        currentPageNumber++;
    }

    public void previousPage() {
        Validate.isTrue(isPreviousPageAvailable());
        currentPageNumber--;
    }

    public boolean isNextPageAvailable() {
        final int numberOfPagesForCurrentFile = numberOfPagesForCurrentFile();
        return numberOfPagesForCurrentFile - 1 > currentPageNumber;
    }

    public boolean isPreviousPageAvailable() {
        return currentPageNumber > 0;
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    public String getCurrentFileId() {
        return currentFileId;
    }

    public int numberOfPagesForCurrentFile() {
        return annotationDataController.getNumberOfPagesForCurrentFile(currentFileId);
    }

    public void getAnnotationsJSONForCurrentFileAndPage() {
        //TODO
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
        final Annotation annotation = annotationDataController.addNew(currentFileId, currentPageNumber, x, y, w, h, tr_latin, tr_arabic, annotationId);
        System.out.printf("Created : " + annotation.toString());
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
        Validate.isTrue(NumberUtils.isDigits(strVal));
        return Integer.parseInt(strVal);
    }

}
