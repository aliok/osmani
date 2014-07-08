package tr.com.aliok.osmani.annotator.annotation;

import tr.com.aliok.osmani.annotator.model.Annotation;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
public interface AnnotationFormatter {

    public String getFormatCommentLine();

    public String formatAnnotation(Annotation annotation);

    public Annotation parseLine(String fileId, String line);

}
