package tr.com.aliok.osmani.annotator.commons;

import com.google.common.collect.ImmutableList;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.Enumeration;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
public class LoggingSettings {

    public static void turnOnLogger(Piece piece) {
        setLoggerLevel(piece, Level.INFO);
    }

    public static void turnOffLogger(Piece piece) {
        setLoggerLevel(piece, Level.OFF);
    }

    public static void setLoggerLevel(Piece piece, Level level) {
        for (String relatedLogger : piece.relatedLoggers) {
            final Enumeration currentLoggers = Logger.getLogger(relatedLogger).getLoggerRepository().getCurrentLoggers();
            while (currentLoggers.hasMoreElements()) {
                final Logger logger = (Logger) currentLoggers.nextElement();
                logger.setLevel(level);
            }
        }
    }

    public enum Piece {
        EVERYTHING("tr.com.aliok.osmani");

        private ImmutableList<String> relatedLoggers;

        private Piece(String... relatedLoggers) {
            this.relatedLoggers = ImmutableList.copyOf(relatedLoggers);
        }
    }

}
