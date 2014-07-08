package tr.com.aliok.osmani.annotator.annotation;

import org.junit.Test;
import tr.com.aliok.osmani.annotator.model.Annotation;
import tr.com.aliok.osmani.annotator.model.AnnotationBuilder;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class AnnotationFormatterTest {

    @Test
    public void shouldFormatAndThenParse() {
        final Annotation annotation = new AnnotationBuilder()
                .annotationId(UUID.randomUUID().toString())
                .pageNumber(10)
                .x(10)
                .y(200)
                .w(250)
                .h(300)
                .tr_arabic("test_arabic")
                .tr_latin("test_latin")
                .tr_latin2("test_latin2")
                .description("description(")
                .fileId("testFile.pdf")
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
        assertThat(annotation.getTr_latin2(), equalTo(parsed.getTr_latin2()));
        assertThat(annotation.getDescription(), equalTo(parsed.getDescription()));

    }

}