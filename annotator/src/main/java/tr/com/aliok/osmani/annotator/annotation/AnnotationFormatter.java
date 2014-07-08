package tr.com.aliok.osmani.annotator.annotation;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import tr.com.aliok.osmani.annotator.model.Annotation;
import tr.com.aliok.osmani.annotator.model.AnnotationBuilder;

import java.io.Serializable;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
public class AnnotationFormatter implements Serializable {

    private static final String FORMAT = "| %36s  %04d  %08.02f  %08.02f  %08.02f  %08.02f  %-50s  %-50s  %-50s  %-200s |";
    private static final int FORMAT_LENGTH = 444;

    private static final String COMMENT_FORMAT = "# %36s  %4s  %8s  %8s  %8s  %8s  %-50s  %-50s  %-50s  %-200s |";

    private static final Pair<Integer, Integer> ANNOTATION_ID_INDICES = Pair.of(2, 2 + 36);
    private static final Pair<Integer, Integer> PAGE_NUMBER_INDICES = Pair.of(ANNOTATION_ID_INDICES.getRight() + 2, ANNOTATION_ID_INDICES.getRight() + 2 + 4);
    private static final Pair<Integer, Integer> X_INDICES = Pair.of(PAGE_NUMBER_INDICES.getRight() + 2, PAGE_NUMBER_INDICES.getRight() + 2 + 8);
    private static final Pair<Integer, Integer> Y_INDICES = Pair.of(X_INDICES.getRight() + 2, X_INDICES.getRight() + 2 + 8);
    private static final Pair<Integer, Integer> W_INDICES = Pair.of(Y_INDICES.getRight() + 2, Y_INDICES.getRight() + 2 + 8);
    private static final Pair<Integer, Integer> H_INDICES = Pair.of(W_INDICES.getRight() + 2, W_INDICES.getRight() + 2 + 8);
    private static final Pair<Integer, Integer> TR_ARABIC_INDICES = Pair.of(H_INDICES.getRight() + 2, H_INDICES.getRight() + 2 + 50);
    private static final Pair<Integer, Integer> TR_LATIN_INDICES = Pair.of(TR_ARABIC_INDICES.getRight() + 2, TR_ARABIC_INDICES.getRight() + 2 + 50);
    private static final Pair<Integer, Integer> TR_LATIN2_INDICES = Pair.of(TR_LATIN_INDICES.getRight() + 2, TR_LATIN_INDICES.getRight() + 2 + 50);
    private static final Pair<Integer, Integer> DESCRIPTION_INDICES = Pair.of(TR_LATIN2_INDICES.getRight() + 2, TR_LATIN2_INDICES.getRight() + 2 + 200);

    public String getFormatCommentLine() {
        return String.format(COMMENT_FORMAT,
                "Annotation Id",
                "#Pg",
                "X",
                "Y",
                "W",
                "H",
                "TR ARABIC",
                "TR LATIN",
                "TR LATIN 2",
                "DESCRIPTION"
        );
    }

    public String formatAnnotation(Annotation annotation) {
        return String.format(FORMAT,
                annotation.getAnnotationId(),
                annotation.getPageNumber(),
                annotation.getX(),
                annotation.getY(),
                annotation.getW(),
                annotation.getH(),
                annotation.getTr_arabic(),
                annotation.getTr_latin(),
                annotation.getTr_latin2(),
                annotation.getDescription()
        );
    }

    public Annotation parseLine(String fileId, String line) {
        Validate.isTrue(line.length() == FORMAT_LENGTH, "Expected line length:" + FORMAT_LENGTH + " retrieved : " + line.length() + "\n, line : \n" + line);

        final String annotationId = getStringFroLine(line, ANNOTATION_ID_INDICES);
        final int pageNumber = getIntegerFromLine(line, PAGE_NUMBER_INDICES);
        final double x = getDoubleFromLine(line, X_INDICES);
        final double y = getDoubleFromLine(line, Y_INDICES);
        final double w = getDoubleFromLine(line, W_INDICES);
        final double h = getDoubleFromLine(line, H_INDICES);
        final String tr_arabic = getStringFroLine(line, TR_ARABIC_INDICES);
        final String tr_latin = getStringFroLine(line, TR_LATIN_INDICES);
        final String tr_latin2 = getStringFroLine(line, TR_LATIN2_INDICES);
        final String description = getStringFroLine(line, DESCRIPTION_INDICES);


        final Annotation annotation = new AnnotationBuilder()
                .fileId(fileId)
                .annotationId(annotationId)
                .pageNumber(pageNumber)
                .x(x)
                .y(y)
                .w(w)
                .h(h)
                .tr_arabic(tr_arabic)
                .tr_latin(tr_latin)
                .tr_latin2(tr_latin2)
                .description(description)
                .createAnnotation();

        return annotation;
    }

    private String getStringFroLine(String line, Pair<Integer, Integer> indices) {
        final String substring = line.substring(indices.getLeft(), indices.getRight());
        return substring.trim();
    }

    private int getIntegerFromLine(String line, Pair<Integer, Integer> indices) {
        final String string = getStringFroLine(line, indices);
        return Integer.parseInt(string);
    }

    private double getDoubleFromLine(String line, Pair<Integer, Integer> indices) {
        final String string = getStringFroLine(line, indices);
        return Double.parseDouble(string);
    }


}
