package antenna.direction;

import antenna.direction.AntennaDirectionViewModel.AngleMode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Log records of antenna pointing.
 *
 * Record the elements of datetime, antenna number, Az, El, Pol.
 *
 */
public class AntennaDirectionLogger {
    private static final Logger logger = Logger.getLogger(AntennaDirectionLogger.class.getName());
    private static final String HEADER = "timestamp, antenna no., azimuth, elevation, polarization, mode";

    private static class Record {
        LocalDateTime timestamp;
        int number;
        double azimuth;
        double elevation;
        double polarization;
        char mode;

        public String toString() {
            return String.format("%tF %tT, %d, %f, %f, %f, %c",
                    timestamp, timestamp, number, azimuth, elevation, polarization, mode
            );
        }
    }

    private List<Record> records = new ArrayList<>();

    public void add(int number, double azimuth, double elevation, double polarization, AngleMode mode) {
        add(LocalDateTime.now(), number, azimuth, elevation, polarization, mode);
    }

    public void add(LocalDateTime time, int number, double azimuth, double elevation, double polarization, AngleMode mode) {
        var record = new Record();
        record.timestamp = time;
        record.number = number;
        record.azimuth = azimuth;
        record.elevation = elevation;
        record.polarization = polarization;
        record.mode = mode == null ? '?' : mode.name().charAt(0);
        records.add(record);
    }

    public void save(Path path) {
        try (var writer = Files.newBufferedWriter(path)) {
            writer.write(HEADER);
            writer.newLine();
            for (Record record : records) {
                writer.write(record.toString());
                writer.newLine();
            }
        } catch (IOException ex) {
            logger.warning("Cannot be saved to file " + path);
        }
    }
}
