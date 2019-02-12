package antenna.direction.boundary;

enum ReceiveState {

    WAIT_STX {
        @Override
        ReceiveState read(byte datum) {
            return datum == Message.ACK || datum == Message.NAK ? WAIT_ETX : this;
        }
    },
    WAIT_ETX {
        @Override
        ReceiveState read(byte datum) {
            if (datum == Message.ETX) {
                return WAIT_CHECKSUM;
            } else if (isControlCharacter(datum)) {
                AntennaDirectionBoundary.log.warning(String.format("control character %x is unexpected in WAIT_ETX state.", datum));
                return ABORTED;
            }
            return this;
        }
    },
    WAIT_CHECKSUM {
        @Override
        ReceiveState read(byte datum) {
            return COMPLETED;
        }
    },
    COMPLETED {
        @Override
        ReceiveState read(byte datum) {
            return WAIT_STX;
        }
    },
    ABORTED {
        @Override
        ReceiveState read(byte datum) {
            return WAIT_STX;
        }
    };

    abstract ReceiveState read(byte data);

    static boolean isControlCharacter(byte b) {
        return 0 <= b && b <= 0x1f;
    }

}
