package tr.com.aliok.osmani.annotator.model;

public class AnnotationBuilder {
    private String fileId;
    private int pageNumber;
    private double x;
    private double y;
    private double w;
    private double h;
    private String tr_latin;
    private String tr_arabic;
    private String tr_latin2;
    private String description;
    private String annotationId;

    public AnnotationBuilder fileId(String fileId) {
        this.fileId = fileId;
        return this;
    }

    public AnnotationBuilder pageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public AnnotationBuilder x(double x) {
        this.x = x;
        return this;
    }

    public AnnotationBuilder y(double y) {
        this.y = y;
        return this;
    }

    public AnnotationBuilder w(double w) {
        this.w = w;
        return this;
    }

    public AnnotationBuilder h(double h) {
        this.h = h;
        return this;
    }

    public AnnotationBuilder tr_latin(String tr_latin) {
        this.tr_latin = tr_latin;
        return this;
    }

    public AnnotationBuilder tr_arabic(String tr_arabic) {
        this.tr_arabic = tr_arabic;
        return this;
    }

    public AnnotationBuilder tr_latin2(String tr_latin2) {
        this.tr_latin2 = tr_latin2;
        return this;
    }

    public AnnotationBuilder description(String description) {
        this.description = description;
        return this;
    }

    public AnnotationBuilder annotationId(String annotationId) {
        this.annotationId = annotationId;
        return this;
    }

    public Annotation createAnnotation() {
        return new Annotation(fileId, pageNumber, x, y, w, h, tr_latin, tr_arabic, tr_latin2, description, annotationId);
    }
}