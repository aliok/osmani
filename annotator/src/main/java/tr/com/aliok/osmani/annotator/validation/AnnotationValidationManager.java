package tr.com.aliok.osmani.annotator.validation;

import tr.com.aliok.osmani.annotator.model.Annotation;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
public class AnnotationValidationManager {

    private List<? extends AnnotationValidationRule> rules = Arrays.asList(
            new AnnotationArabicToLatinConsistencyValidationRule()
    );

    public void validateAll(AnnotationContext annotationContext) {
        final Iterable<Annotation> allAnnotations = annotationContext.getAllAnnotations();

        int i = 0;
        for (Annotation annotation : allAnnotations) {
            try {
                validateSingle(annotation, annotationContext);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                i++;
            }
        }

        System.err.println("Found " + i + " problems");
    }

    public void validateSingle(Annotation annotation, AnnotationContext annotationContext) {
        for (AnnotationValidationRule rule : rules) {
            rule.validate(annotation, annotationContext);
        }
    }
}
