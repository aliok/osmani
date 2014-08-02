package tr.com.aliok.osmani.annotator.commons.model;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
public class Letter {

    public final char value;

    private Letter(char value) {
        this.value = value;
    }

    public static Letter of(char value) {
        return new Letter(value);
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Letter)) return false;

        Letter letter = (Letter) o;

        if (value != letter.value) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) value;
    }
}
