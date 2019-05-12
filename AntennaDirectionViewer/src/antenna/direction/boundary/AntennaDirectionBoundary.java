/*
 *  Copyright © 2019 TAKAHASHI,Toru
 */
package antenna.direction.boundary;

import antenna.direction.AntennaDirectionViewModel;
import antenna.direction.AntennaDirectionViewModel.AngleMode;
import javafx.application.Platform;

import java.nio.ByteBuffer;
import java.time.LocalTime;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 *
 * @author TAKAHASHI,Toru
 */
public enum AntennaDirectionBoundary {
    INSTANCE;

    public static final Logger log = Logger.getLogger(AntennaDirectionBoundary.class.getName());
    private ThreadFactory daemonThreadFactory = r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    };

    private SerialIo serial = new SerialIo("COM3");
    private Executor requestExecutor = Executors.newSingleThreadExecutor(daemonThreadFactory);

    private ScheduledExecutorService periodicExecutor = Executors.newSingleThreadScheduledExecutor(daemonThreadFactory);
    private ScheduledFuture<?> future;
    private AntennaDirectionViewModel model = AntennaDirectionViewModel.INSTANCE;
    private Runnable task;

    AntennaDirectionBoundary() {
        task = () -> {
            getAzimuth();
            getElevation();
            getPolarization();
            model.setUpdateTime(LocalTime.now());
        };
    }

    /**
     * Execute on serial IO thread, command read elevation to STC-110, then receive response from STC-110.
     */
    public void getElevation() {
        requestExecutor.execute(() -> {
            log.fine("3 Axes Basic Feedback:Elevation");
            var response = new ElevationBasicFeedbackResponse(sendAndReceive(Message.READ_ELEVATION_COMMAND_MESSAGE));
            double angle;
            if (response.isNak()) {
                log.warning("3 Axes Basic Feedback:Elevation respond NAK:" + response.getNakStatus());
                angle = Double.NaN;
            } else {
                log.fine("3 Axes Basic Feedback:Elevation respond ACK:");
                angle = response.getElevation();
            }
            Platform.runLater(() -> model.setElevation(angle));
        });
    }

    /**
     * Execute on serial IO thread, command read azimuth to STC-110, then receive response from STC-110.
     */
    public void getAzimuth() {
        requestExecutor.execute(() -> {
            log.fine("3 Axes Basic Feedback:Azimuth");
            var response = new AzimuthBasicFeedbackResponse(sendAndReceive(Message.READ_AZIMUTH_COMMAND_MESSAGE));
            double angle;
            AngleMode angleMode;
            if (response.isNak()) {
                log.warning("3 Axes Basic Feedback:Azimuth respond NAK:" + response.getNakStatus());
                angle = Double.NaN;
                angleMode = AntennaDirectionViewModel.AngleMode.RELATIVE;
            } else {
                log.fine("3 Axes Basic Feedback:Azimuth respond ACK:");
                angle = response.getAzimuth();
                angleMode = response.getAngleMode();
            }
            Platform.runLater(() -> {
                model.setAzimuth(angle);
                model.setAngleMode(angleMode);
            });
        });
    }

    /**
     * Execute on serial IO thread, command read polarization to STC-110, then receive response from STC-110.
     */
    public void getPolarization() {
        requestExecutor.execute(() -> {
            log.fine("3 Axes Basic Feedback:Polarization");
            var response = new PolarizationBasicFeedbackResponse(sendAndReceive(Message.READ_POLARIZATION_COMMAND_MESSAGE));
            double angle;
            if (response.isNak()) {
                log.warning("3 Axes Basic Feedback:Polarization respond NAK:" + response.getNakStatus());
                angle = Double.NaN;
            } else {
                log.fine("3 Axes Basic Feedback:Polarization respond ACK:");
                angle = response.getPolarization();
            }
            Platform.runLater(() -> model.setPolarization(angle));
        });
    }

    Message sendAndReceive(Message command) {
        serial.sendCommand(command.getData());
        byte[] received = serial.receiveResponse();
        return Message.of(received);
    }

    static String toStringBuffer(ByteBuffer buffer) {
        buffer.flip();
        StringBuilder sb = new StringBuilder();
        while (buffer.hasRemaining()) {
            sb.append(String.format("%02x ", buffer.get()));
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.lastIndexOf(" "));
        }
        return sb.toString();
    }

    public void startPeriodic(int interval) {
        future = periodicExecutor.scheduleWithFixedDelay(task, 0, interval, TimeUnit.SECONDS);
    }

    public void stopPeriodic() {
        if (future != null) {
            future.cancel(true);
        }
    }

    public void shutdown() {
        periodicExecutor.shutdownNow();
    }
}
