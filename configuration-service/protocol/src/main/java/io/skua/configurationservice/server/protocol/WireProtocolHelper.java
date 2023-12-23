package io.skua.configurationservice.server.protocol;

import org.agrona.DirectBuffer;

public final class WireProtocolHelper
{
    /**
     * private constructor.
     */
    private WireProtocolHelper()
    {
        //unused;
    }

    /**
     * Reads the Wire Protocol Id from the buffer at the offset provided.
     */
    public static short getWireProtocolId(final DirectBuffer buffer, final int offset)
    {
        return buffer.getShort(offset, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Reads the Wire Protocol Version from the buffer at the offset provided.
     */
    public static short getWireProtocolVersion(final DirectBuffer buffer, final int offset)
    {
        return buffer.getShort(offset + 2, java.nio.ByteOrder.LITTLE_ENDIAN);
    }
}
