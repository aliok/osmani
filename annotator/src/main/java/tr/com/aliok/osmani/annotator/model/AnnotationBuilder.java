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
    private String annotationId;

    public AnnotationBuilder setFileId(String fileId) {
        this.fileId = fileId;
        return this;
    }

    public AnnotationBuilder setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public AnnotationBuilder setX(double x) {
        this.x = x;
        return this;
    }

    public AnnotationBuilder setY(double y) {
        this.y = y;
        return this;
    }

    public AnnotationBuilder setW(double w) {
        this.w = w;
        return this;
    }

    public AnnotationBuilder setH(double h) {
        this.h = h;
        return this;
    }

    public AnnotationBuilder setTr_latin(String tr_latin) {
        this.tr_latin = tr_latin;
        return this;
    }

    public AnnotationBuilder setTr_arabic(String tr_arabic) {
        this.tr_arabic = tr_arabic;
        return this;
    }

    public AnnotationBuilder setAnnotationId(String annotationId) {
        this.annotationId = annotationId;
        return this;
    }

    public Annotation createAnnotation() {
        return new Annotation(fileId, pageNumber, x, y, w, h, tr_latin, tr_arabic, annotationId);
    }
}