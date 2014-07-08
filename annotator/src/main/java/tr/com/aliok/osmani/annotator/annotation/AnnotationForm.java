package tr.com.aliok.osmani.annotator.annotation;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.context.RequestContext;
import tr.com.aliok.osmani.annotator.model.Annotation;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
@ManagedBean(name = "annotationForm")
@ViewScoped
public class AnnotationForm implements Serializable {
    protected Log log = LogFactory.getLog(getClass());

    @ManagedProperty("#{annotatorData}")
    private AnnotatorData annotatorData;

    @ManagedProperty("#{annotationDataController}")
    private AnnotationDataController annotationDataController;

    public String initialize() {
        final TreeSet<Annotation> annotations = annotationDataController.getAnnotations(annotatorData.getCurrentFileId(), annotatorData.getCurrentPageNumber());
        if (annotations != null && !annotations.isEmpty())
            this.annotatorData.setCurrent(annotations.iterator().next());
        return "";
    }

    public void selectAnnotation() {
        try {
            final Map<String, String> requestParamMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            String annotationId = requestParamMap.get("annotationId");
            Validate.notBlank(annotationId);

            final TreeSet<Annotation> annotations = annotationDataController.getAnnotations(annotatorData.getCurrentFileId(), annotatorData.getCurrentPageNumber());
            for (Annotation annotation : annotations) {
                if (annotation.getAnnotationId().equals(annotationId)) {
                    annotatorData.setCurrent(annotation);
                    break;
                }
            }

            Validate.notNull(annotatorData.getCurrent());
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    public void deleteGivenAnnotation() {
        try {
            final Map<String, String> requestParamMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            String annotationId = requestParamMap.get("annotationId");
            Validate.notBlank(annotationId);

            final TreeSet<Annotation> annotations = annotationDataController.getAnnotations(annotatorData.getCurrentFileId(), annotatorData.getCurrentPageNumber());
            for (Iterator<Annotation> iterator = annotations.iterator(); iterator.hasNext(); ) {
                Annotation annotation = iterator.next();
                if (annotation.getAnnotationId().equals(annotationId)) {
                    iterator.remove();
                    return;
                }
            }
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

    public String save() {
        // stuff is directly set on the object anyway
        annotationDataController.increaseCounter();
        annotationDataController.flushIfNecessary();
        annotatorData.setCurrent(null);
        return null;
    }

    public String saveAndNext() {
        String previousAnnotationId = annotatorData.getCurrent().getAnnotationId();
        this.save();
        annotatorData.setCurrent(findNextEmpty(previousAnnotationId));
        RequestContext.getCurrentInstance().addCallbackParam("annotationId", annotatorData.getCurrent().getAnnotationId());
        return null;
    }

    public boolean isHasNextAnnotation() {
        final TreeSet<Annotation> annotations = annotationDataController.getAnnotations(annotatorData.getCurrentFileId(), annotatorData.getCurrentPageNumber());
        return annotations != null && annotations.iterator().hasNext();
    }

    private Annotation findNextEmpty(String previousAnnotationId) {
        final TreeSet<Annotation> annotations = annotationDataController.getAnnotations(annotatorData.getCurrentFileId(), annotatorData.getCurrentPageNumber());
        if (annotations == null)
            return null;

        //find current first
        Iterator<Annotation> iterator = annotations.iterator();
        for (; iterator.hasNext(); ) {
            Annotation annotation = iterator.next();
            if (annotation.getAnnotationId().equals(previousAnnotationId))
                break;
        }

        for (; iterator.hasNext(); ) {
            Annotation annotation = iterator.next();
            final boolean arabicEmpty = StringUtils.isBlank(annotation.getTr_arabic());
            final boolean latinEmpty = StringUtils.isBlank(annotation.getTr_latin());
            if (arabicEmpty)
                return annotation;
            if (latinEmpty && !annotatorData.isIgnoreEmptyLatin()) {
                return annotation;
            }
        }

        return annotations.iterator().next();
    }

    public String delete() {
        Validate.notNull(annotatorData.getCurrent());
        final Annotation nextEmpty = findNextEmpty(annotatorData.getCurrent().getAnnotationId());

        annotationDataController.delete(annotatorData.getCurrent());

        annotationDataController.increaseCounter();
        annotationDataController.flushIfNecessary();

        annotatorData.setCurrent(nextEmpty);
        RequestContext.getCurrentInstance().addCallbackParam("annotationId", annotatorData.getCurrent().getAnnotationId());
        return null;
    }

    public void setAnnotationDataController(AnnotationDataController annotationDataController) {
        this.annotationDataController = annotationDataController;
    }

    public void setAnnotatorData(AnnotatorData annotatorData) {
        this.annotatorData = annotatorData;
    }
}
