package tr.com.aliok.osmani.annotator.validation;

import tr.com.aliok.osmani.annotator.model.Annotation;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
public interface AnnotationValidationRule {
    public void validate(Annotation annotation, AnnotationContext annotationContext);
}
