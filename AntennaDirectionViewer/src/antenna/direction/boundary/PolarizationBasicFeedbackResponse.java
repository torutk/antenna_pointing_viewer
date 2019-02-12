package antenna.direction.boundary;

import java.util.Arrays;

public class PolarizationBasicFeedbackResponse {
    private Message message;

    PolarizationBasicFeedbackResponse(Message message) {
        assert message.isNak() || message.isAck();
        assert message.getData()[2] == 'r' && message.getData()[3] == 'p';
        this.message = message;
    }

    double getPolarization() {
        assert message.isAck();
        byte[] data = message.getData();
        byte[] polarization = Arrays.copyOfRange(data, 4, 12);
        String polarizationString = new String(polarization);
        return Double.parseDouble(polarizationString);
    }

    boolean isNak() {
        return message.isNak();
    }

    byte getNakStatus() {
        return message.getNakStatus();
    }
}
