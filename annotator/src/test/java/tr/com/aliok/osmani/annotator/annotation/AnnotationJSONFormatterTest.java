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
        final Annotation annotation = new AnnotationBuilder()
                .setAnnotationId("12345678-1234-1234-1234-1234567890ab")
                .setPageNumber(10)
                .setX(10)
                .setY(200)
                .setW(250)
                .setH(300)
                .setTr_arabic("test_arabic")
                .setTr_latin("test_latin")
                .setFileId("test.pdf")
                .createAnnotation();

        final String expected = "[{\"id\":\"12345678-1234-1234-1234-1234567890ab\",\"x\":10.0,\"y\":200.0,\"w\":250.0,\"h\":300.0,\"textData\":{\"tr_arabic\":\"test_arabic\",\"tr_latin\":\"test_latin\"}}]";

        final String json = formatter.getJSON(new TreeSet<Annotation>(Arrays.asList(annotation)));
        assertThat(json, equalTo(expected));
    }

}