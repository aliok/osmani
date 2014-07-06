package tr.com.aliok.osmani.annotator.annotation;

import org.junit.Test;
import tr.com.aliok.osmani.annotator.model.Annotation;
import tr.com.aliok.osmani.annotator.model.AnnotationBuilder;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class AnnotationFormatterTest {

    @Test
    public void shouldFormatAndThenParse() {
        final Annotation annotation = new AnnotationBuilder()
                .setAnnotationId(UUID.randomUUID().toString())
                .setPageNumber(10)
                .setX(10)
                .setY(200)
                .setW(250)
                .setH(300)
                .setTr_arabic("test_arabic")
                .setTr_latin("test_latin")
                .setFileId("testFile.pdf")
                .createAnnotation();

        final AnnotationFormatter formatter = new AnnotationFormatter();
        final String formatted = formatter.formatAnnotation(annotation);
        final Annotation parsed = formatter.parseLine("testFile.pdf", formatted);

        assertThat(parsed.getFileId(), equalTo(parsed.getFileId()));
        assertThat(annotation.getAnnotationId(), equalTo(parsed.getAnnotationId()));
        assertThat(annotation.getPageNumber(), equalTo(parsed.getPageNumber()));
        assertThat(annotation.getX(), equalTo(parsed.getX()));
        assertThat(annotation.getY(), equalTo(parsed.getY()));
        assertThat(annotation.getW(), equalTo(parsed.getW()));
        assertThat(annotation.getH(), equalTo(parsed.getH()));
        assertThat(annotation.getTr_arabic(), equalTo(parsed.getTr_arabic()));
        assertThat(annotation.getTr_latin(), equalTo(parsed.getTr_latin()));

    }

}