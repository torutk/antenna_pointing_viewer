package antenna.direction.boundary;

import com.fazecast.jSerialComm.SerialPort;

import java.nio.ByteBuffer;
import java.util.logging.Logger;


public class SerialIo {
    private static final Logger logger = Logger.getLogger(SerialIo.class.getName());
    private SerialPort port;

    public SerialIo(String identifier) {
        port = SerialPort.getCommPort(identifier);
        if (! port.openPort()) {
            logger.severe("SerialPort of " + identifier + " cannot open.");
        }
    }

    void sendCommand(byte[] command) {
        Message.writeChecksum(command);
        port.writeBytes(command, command.length);
    }

    byte[] receiveResponse() {
        byte[] buffer = new byte[256];
        ByteBuffer responseBuffer = ByteBuffer.allocate(256);
        ReceiveState state = ReceiveState.WAIT_STX;

        read_loop:
        while (true) {
            int numRead = port.readBytes(buffer, buffer.length);
            for (int i = 0; i < numRead; i++) {
                state = state.read(buffer[i]);
                if (state == ReceiveState.COMPLETED) {
                    responseBuffer.put(buffer[i]);
                    break read_loop;
                } else if (state == ReceiveState.ABORTED) {
                    logger.warning(String.format("response abort with %s", Message.hexString(responseBuffer)));
                    responseBuffer.put(buffer[i]);
                    return new byte[0];
                }
                responseBuffer.put(buffer[i]);
            }
        }
        logger.info(String.format("%s", Message.hexString(responseBuffer)));
        responseBuffer.flip();
        byte[] received = new byte[responseBuffer.limit()];
        responseBuffer.get(received);
        return received;
    }
}
