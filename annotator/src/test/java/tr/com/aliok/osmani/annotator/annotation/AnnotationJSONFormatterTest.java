package tr.com.aliok.osmani.annotator.annotation;

import org.junit.Test;
import tr.com.aliok.osmani.annotator.model.Annotation;
import tr.com.aliok.osmani.annotator.model.AnnotationBuilder;

import java.util.Arrays;
import java.util.TreeSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class AnnotationJSONFormatterTest {

    AnnotationJSONFormatter formatter = new AnnotationJSONFormatter();

    @Test
    public void shouldGetJson() {
        final Annotation annotation1 = new AnnotationBuilder()
                .annotationId("12345678-1234-1234-1234-1234567890ab")
                .pageNumber(10)
                .x(10)
                .y(200)
                .w(250)
                .h(300)
                .tr_arabic("test_arabic")
                .tr_latin("test_latin")
                .tr_latin2("test_latin2")
                .description("description")
                .fileId("test.pdf")
                .createAnnotation();

        final Annotation annotation2 = new AnnotationBuilder()
                .annotationId("12345678-1234-1234-1234-1234567890ac")
                .pageNumber(11)
                .x(10)
                .y(200)
                .w(250)
                .h(300)
                .tr_arabic("test_arabic")
                .tr_latin("test_latin")
                .tr_latin2("test_latin2")
                .description("description")
                .fileId("test.pdf")
                .createAnnotation();

        final String expected = "[{\"id\":\"12345678-1234-1234-1234-1234567890ab\",\"x\":10.0,\"y\":200.0,\"w\":250.0,\"h\":300.0,\"textData\":{\"tr_arabic\":\"test_arabic\",\"tr_latin\":\"test_latin\"},\"selected\":true},{\"id\":\"12345678-1234-1234-1234-1234567890ac\",\"x\":10.0,\"y\":200.0,\"w\":250.0,\"h\":300.0,\"textData\":{\"tr_arabic\":\"test_arabic\",\"tr_latin\":\"test_latin\"},\"selected\":false}]";

        final String json = formatter.getJSON(new TreeSet<>(Arrays.asList(annotation1, annotation2)), annotation1);
        assertThat(json, equalTo(expected));
    }

}