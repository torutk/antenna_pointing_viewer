package antenna.direction;

import antenna.direction.boundary.AntennaDirectionBoundary;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;

class AntennaDirectionBoundaryTest {

    AntennaDirectionBoundary fixture = AntennaDirectionBoundary.INSTANCE;



    @Test
    void toStringBuffer() {
        byte[] data = { 0x06, 0x45, 'r', 'e', 0x7f, 0x03 };
        ByteBuffer buf = ByteBuffer.wrap(data);
        //assertEquals("06 45 72 65 7f 03", AntennaDirectionBoundary.toStringBuffer(buf));
    }
}