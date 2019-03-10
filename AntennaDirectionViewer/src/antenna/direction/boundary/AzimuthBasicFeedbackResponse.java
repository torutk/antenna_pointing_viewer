package antenna.direction.boundary;

import antenna.direction.AntennaDirectionViewModel.AngleMode;

import java.util.Arrays;

public class AzimuthBasicFeedbackResponse {
    private Message message;

    AzimuthBasicFeedbackResponse(Message message) {
        assert message.isNak() || message.isAck();
        assert message.getData()[2] == 'r' && message.getData()[3] == 'a';
        this.message = message;
    }

    AngleMode getAngleMode() {
        assert message.isAck();
        byte[] data = message.getData();
        byte relativeOrAbsolute = data[4];
        assert relativeOrAbsolute == 'a' || relativeOrAbsolute == 'r';
        return relativeOrAbsolute == 'a' ? AngleMode.ABSOLUTE : AngleMode.RELATIVE;
    }

    double getAzimuth() {
        assert message.isAck();
        byte[] data = message.getData();
        byte[] azimuth = Arrays.copyOfRange(data, 6, 14);
        String azimuthString = new String(azimuth);
        return Double.parseDouble(azimuthString);
    }

    boolean isNak() {
        return message.isNak();
    }

    byte getNakStatus() {
        return message.getNakStatus();
    }
}
