package tr.com.aliok.osmani.annotator.annotation;

import org.apache.commons.lang3.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tr.com.aliok.osmani.annotator.model.Annotation;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeSet;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
@ManagedBean(name = "annotationForm")
@ViewScoped
public class AnnotationForm implements Serializable {
    protected Log log = LogFactory.getLog(getClass());

    private Annotation current;

    @ManagedProperty("#{annotatorData}")
    private AnnotatorData annotatorData;

    @ManagedProperty("#{annotationDataController}")
    private AnnotationDataController annotationDataController;

    public void selectAnnotation() {
        try {
            final Map<String, String> requestParamMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            String annotationId = requestParamMap.get("annotationId");
            Validate.notBlank(annotationId);

            final TreeSet<Annotation> annotations = annotationDataController.getAnnotations(annotatorData.getCurrentFileId(), annotatorData.getCurrentPageNumber());
            for (Annotation annotation : annotations) {
                if (annotation.getAnnotationId().equals(annotationId)) {
                    this.current = annotation;
                    break;
                }
            }

            Validate.notNull(current);
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    public String save() {
        // stuff is directly set on the object anyway
        annotationDataController.increaseCounter();
        annotationDataController.flushIfNecessary();
        this.current = null;
        return null;
    }

    public String delete() {
        Validate.notNull(current);

        annotationDataController.delete(current);

        annotationDataController.increaseCounter();
        annotationDataController.flushIfNecessary();

        this.current = null;
        return null;
    }

    public Annotation getCurrent() {
        return current;
    }

    public void setAnnotationDataController(AnnotationDataController annotationDataController) {
        this.annotationDataController = annotationDataController;
    }

    public void setAnnotatorData(AnnotatorData annotatorData) {
        this.annotatorData = annotatorData;
    }
}
