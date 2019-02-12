/*
 *  Copyright © 2019 TAKAHASHI,Toru
 */
package antenna.direction.boundary;

import antenna.direction.AntennaDirectionViewModel;
import javafx.application.Platform;

import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 *
 * @author TAKAHASHI,Toru
 */
public enum AntennaDirectionBoundary {
    INSTANCE;

    public static final Logger log = Logger.getLogger(AntennaDirectionBoundary.class.getName());

    private SerialIo serial = new SerialIo("COM3");
    Executor executor = Executors.newSingleThreadExecutor();
    private AntennaDirectionViewModel model = AntennaDirectionViewModel.INSTANCE;

    private AntennaDirectionBoundary() {
    }

    /**
     * Execute on serial IO thread, command read elevation to STC-110, then receive response from STC-110.
     */
    public void getElevation() {
        executor.execute(() -> {
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
        executor.execute(() -> {
            log.fine("3 Axes Basic Feedback:Azimuth");
            var response = new AzimuthBasicFeedbackResponse(sendAndReceive(Message.READ_AZIMUTH_COMMAND_MESSAGE));
            double angle;
            boolean isAbsolute;
            if (response.isNak()) {
                log.warning("3 Axes Basic Feedback:Azimuth respond NAK:" + response.getNakStatus());
                angle = Double.NaN;
                isAbsolute = false;
            } else {
                log.fine("3 Axes Basic Feedback:Azimuth respond ACK:");
                angle = response.getAzimuth();
                isAbsolute = response.isAbsolute();
            }
            Platform.runLater(() -> {
                model.setAzimuth(angle);
                model.setAbsolute(isAbsolute);
            });
        });
    }

    /**
     * Execute on serial IO thread, command read polarization to STC-110, then receive response from STC-110.
     */
    public void getPolarizatoin() {
        executor.execute(() -> {
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
            Platform.runLater(() -> {
                model.setPolarization(angle);
            });
        });
    }

    Message sendAndReceive(Message command) {
        serial.sendCommand(command.getData());
        byte[] received = serial.receiveResponse();
        return Message.of(received);
    }

    static String toStringBuffer(ByteBuffer buffer) {
        buffer.flip();
        StringBuffer sb = new StringBuffer();
        while (buffer.hasRemaining()) {
            sb.append(String.format("%02x ", buffer.get()));
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.lastIndexOf(" "));
        }
        return sb.toString();
    }

}
