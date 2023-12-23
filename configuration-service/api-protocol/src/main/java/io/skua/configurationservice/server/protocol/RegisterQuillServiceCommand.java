package io.skua.configurationservice.server.protocol;

import org.agrona.DirectBuffer;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;

@SuppressWarnings("unused")
public class RegisterQuillServiceCommand
{
    /**
     * The wire protocol id for this type. Useful in switch statements to detect type in first 16bits.
     */
    public static final short WIRE_PROTOCOL_ID = 100;
    /**
     * Indicates if this flyweight holds a fixed length object.
     */
    public static final boolean FIXED_LENGTH = false;
    /**
     * The wire protocol version for this type.
     */
    public static final short WIRE_PROTOCOL_VERSION = 1;
    /**
     * The offset for the message length within the buffer.
     */
    private static final int MESSAGE_LENGTH_OFFSET = 0;
    /**
     * The offset for the encoding type within the buffer.
     */
    private static final int EIDER_WIRE_ENCODING_TYPE_OFFSET = 4;
    /**
     * The offset for the WIRE_PROTOCOL_ID within the buffer.
     */
    private static final int PROTOCOL_ID_OFFSET = 6;
    /**
     * The offset for the WIRE_PROTOCOL_VERSION within the buffer.
     */
    private static final int HEADER_VERSION_OFFSET = 8;
    /**
     * The byte offset in the byte array for this LONG. Byte length is 8.
     */
    private static final int CORRELATIONID_OFFSET = 10;
    /**
     * The byte offset in the byte array for this INT. Byte length is 4.
     */
    private static final int QUILLHOSTCONNECTION_COUNT_OFFSET = 18;
    /**
     * The byte offset in the byte array to start writing QuillHostConnection.
     */
    private static final int QUILLHOSTCONNECTION_RECORD_START_OFFSET = 22;
    /**
     * The total bytes required to store the core data, excluding any repeating record data.
     * Use precomputeBufferLength to compute buffer length this object.
     */
    private static final int BUFFER_LENGTH = 22;
    /**
     * The internal DirectBuffer.
     */
    private DirectBuffer buffer = null;

    /**
     * The internal DirectBuffer used for mutatation opertions. Valid only if a mutable buffer was provided.
     */
    private MutableDirectBuffer mutableBuffer = null;

    /**
     * The internal UnsafeBuffer. Valid only if an unsafe buffer was provided.
     */
    private UnsafeBuffer unsafeBuffer = null;

    /**
     * The starting offset for reading and writing.
     */
    private int initialOffset;

    /**
     * Flag indicating if the buffer is mutable.
     */
    private boolean isMutable = false;

    /**
     * Flag indicating if the buffer is an UnsafeBuffer.
     */
    private boolean isUnsafe = false;

    /**
     * The max number of items allocated for this record. Use resize() to alter.
     */
    @SuppressWarnings("checkstyle:MemberName")
    private int QUILLHOSTCONNECTION_COMMITTED_SIZE = 0;

    /**
     * The flyweight for the QuillHostConnection record.
     */
    @SuppressWarnings("checkstyle:MemberName")
    private QuillHostConnection QUILLHOSTCONNECTION_FLYWEIGHT = new QuillHostConnection();

    /**
     * Uses the provided {@link DirectBuffer} from the given offset.
     *
     * @param buffer - buffer to read from and write to.
     * @param offset - offset to begin reading from/writing to in the buffer.
     */
    public void setUnderlyingBuffer(final DirectBuffer buffer, final int offset)
    {
        this.initialOffset = offset;
        this.buffer = buffer;
        if (buffer instanceof UnsafeBuffer)
        {
            unsafeBuffer = (UnsafeBuffer)buffer;
            mutableBuffer = (MutableDirectBuffer)buffer;
            isUnsafe = true;
            isMutable = true;
        }
        else if (buffer instanceof MutableDirectBuffer)
        {
            mutableBuffer = (MutableDirectBuffer)buffer;
            isUnsafe = false;
            isMutable = true;
        }
        else
        {
            isUnsafe = false;
            isMutable = false;
        }
        buffer.checkLimit(initialOffset + BUFFER_LENGTH);
    }

    /**
     * Returns the wire protocol id.
     *
     * @return WIRE_PROTOCOL_ID.
     */
    public short wireProtocolId()
    {
        return WIRE_PROTOCOL_ID;
    }

    /**
     * Writes the header data to the buffer.
     */
    public void writeHeader()
    {
        if (!isMutable)
        {
            throw new RuntimeException("cannot write to immutable buffer");
        }
        mutableBuffer.putInt(initialOffset + MESSAGE_LENGTH_OFFSET, BUFFER_LENGTH, java.nio.ByteOrder.LITTLE_ENDIAN);
        mutableBuffer.putShort(initialOffset + EIDER_WIRE_ENCODING_TYPE_OFFSET, (short)43,
            java.nio.ByteOrder.LITTLE_ENDIAN);
        mutableBuffer.putShort(initialOffset + PROTOCOL_ID_OFFSET, WIRE_PROTOCOL_ID, java.nio.ByteOrder.LITTLE_ENDIAN);
        mutableBuffer.putShort(initialOffset + HEADER_VERSION_OFFSET, WIRE_PROTOCOL_VERSION,
            java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Validates the length and wireProtocolId in the header against the expected values. False if invalid.
     */
    public boolean validateHeader()
    {
        final int bufferLength = buffer.getInt(initialOffset + MESSAGE_LENGTH_OFFSET, java.nio.ByteOrder.LITTLE_ENDIAN);
        final int encodingType =
            buffer.getShort(initialOffset + EIDER_WIRE_ENCODING_TYPE_OFFSET, java.nio.ByteOrder.LITTLE_ENDIAN);
        final short wireProtocolId =
            buffer.getShort(initialOffset + PROTOCOL_ID_OFFSET, java.nio.ByteOrder.LITTLE_ENDIAN);
        final short wireProtocolVersion =
            buffer.getShort(initialOffset + HEADER_VERSION_OFFSET, java.nio.ByteOrder.LITTLE_ENDIAN);
        if (encodingType != EIDER_WIRE_ENCODING_TYPE_OFFSET)
        {
            return false;
        }
        if (wireProtocolId != WIRE_PROTOCOL_ID)
        {
            return false;
        }
        if (wireProtocolVersion != WIRE_PROTOCOL_VERSION)
        {
            return false;
        }
        return bufferLength == BUFFER_LENGTH;
    }

    /**
     * Reads correlationId as stored in the buffer.
     */
    public long readCorrelationId()
    {
        return buffer.getLong(initialOffset + CORRELATIONID_OFFSET, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Writes correlationId to the buffer. Returns true if success, false if not.
     *
     * @param value Value for the correlationId to write to buffer.
     */
    public boolean writeCorrelationId(final long value)
    {
        if (!isMutable)
        {
            throw new RuntimeException("Cannot write to immutable buffer");
        }
        mutableBuffer.putLong(initialOffset + CORRELATIONID_OFFSET, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return true;
    }

    /**
     * Uses the provided {@link DirectBuffer} from the given offset.
     *
     * @param buffer - buffer to read from and write to.
     * @param offset - offset to begin reading from/writing to in the buffer.
     */
    public void setBufferWriteHeader(final DirectBuffer buffer, final int offset)
    {
        setUnderlyingBuffer(buffer, offset);
        writeHeader();
    }

    /**
     * Precomputes the required buffer length with the given record sizes
     */
    public int precomputeBufferLength(final int quillHostConnectionCount)
    {
        return BUFFER_LENGTH + (quillHostConnectionCount * QuillHostConnection.BUFFER_LENGTH);
    }

    /**
     * The required buffer size given current max record counts
     */
    public int committedBufferLength()
    {
        return BUFFER_LENGTH + (QUILLHOSTCONNECTION_COMMITTED_SIZE * QuillHostConnection.BUFFER_LENGTH);
    }

    /**
     * Sets the amount of QuillHostConnection items that can be written to the buffer
     */
    public void resetQuillHostConnectionSize(final int quillHostConnectionCommittedSize)
    {
        QUILLHOSTCONNECTION_COMMITTED_SIZE = quillHostConnectionCommittedSize;
        buffer.checkLimit(committedBufferLength());
        mutableBuffer.putInt(QUILLHOSTCONNECTION_COUNT_OFFSET + initialOffset, quillHostConnectionCommittedSize,
            java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Returns & internally sets the amount of QuillHostConnection items that the buffer potentially contains
     */
    public int readQuillHostConnectionSize()
    {
        QUILLHOSTCONNECTION_COMMITTED_SIZE = mutableBuffer.getInt(QUILLHOSTCONNECTION_COUNT_OFFSET);
        return QUILLHOSTCONNECTION_COMMITTED_SIZE;
    }

    /**
     * Gets the QuillHostConnection flyweight at the given index
     */
    public QuillHostConnection getQuillHostConnection(final int offset)
    {
        if (QUILLHOSTCONNECTION_COMMITTED_SIZE < offset)
        {
            throw new RuntimeException("cannot access record beyond committed size");
        }
        QUILLHOSTCONNECTION_FLYWEIGHT.setUnderlyingBuffer(this.buffer,
            QUILLHOSTCONNECTION_RECORD_START_OFFSET + initialOffset + (offset * QuillHostConnection.BUFFER_LENGTH));
        return QUILLHOSTCONNECTION_FLYWEIGHT;
    }
}
