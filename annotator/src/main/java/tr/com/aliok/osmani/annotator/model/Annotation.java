package tr.com.aliok.osmani.annotator.model;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.io.Serializable;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
public class Annotation implements Serializable, Comparable {

    private String fileId;
    private int pageNumber;
    private double x;
    private double y;
    private double w;
    private double h;
    private String tr_latin;
    private String tr_arabic;
    private String annotationId;

    Annotation(String fileId, int pageNumber, double x, double y, double w, double h, String tr_latin, String tr_arabic, String annotationId) {
        this.fileId = fileId;
        this.pageNumber = pageNumber;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.tr_latin = tr_latin;
        this.tr_arabic = tr_arabic;
        this.annotationId = annotationId;
    }

    public String getFileId() {
        return fileId;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getW() {
        return w;
    }

    public double getH() {
        return h;
    }

    public String getTr_latin() {
        return tr_latin;
    }

    public String getTr_arabic() {
        return tr_arabic;
    }

    public String getAnnotationId() {
        return annotationId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setW(double w) {
        this.w = w;
    }

    public void setH(double h) {
        this.h = h;
    }

    public void setTr_latin(String tr_latin) {
        this.tr_latin = tr_latin;
    }

    public void setTr_arabic(String tr_arabic) {
        this.tr_arabic = tr_arabic;
    }

    public void setAnnotationId(String annotationId) {
        this.annotationId = annotationId;
    }

    @Override
    public String toString() {
        return "Annotation{" +
                "fileId='" + fileId + '\'' +
                ", pageNumber=" + pageNumber +
                ", x=" + x +
                ", y=" + y +
                ", w=" + w +
                ", h=" + h +
                ", tr_latin='" + tr_latin + '\'' +
                ", tr_arabic='" + tr_arabic + '\'' +
                ", annotationId='" + annotationId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Annotation)) return false;

        Annotation that = (Annotation) o;

        if (!annotationId.equals(that.annotationId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return annotationId.hashCode();
    }

    @Override
    public int compareTo(Object o) {
        Validate.isInstanceOf(Annotation.class, o);
        final Annotation other = (Annotation) o;

        return new CompareToBuilder()
                .append(this.fileId, other.fileId)
                .append(this.pageNumber, other.pageNumber)
                .append(this.x, other.x)
                .append(this.y, other.y)
                .append(this.w, other.w)
                .append(this.h, other.h)
                .append(this.annotationId, other.annotationId)
                .toComparison();
    }
}
