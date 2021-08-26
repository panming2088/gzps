package org.geowebcache.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;


public interface Resource {

    /**
     * The size of the resource in bytes.
     * @return
     */
    long getSize();

    /**
     * Writes the resource to a channel
     * @param channel the channel to write too
     * @return The number of bytes written
     * @throws IOException
     */
    long transferTo(WritableByteChannel channel) throws IOException;

    /**
     * Overwrites the resource with bytes read from a channel. 
     * @param channel the channel to read from
     * @return The number of bytes read
     * @throws IOException
     */
    long transferFrom(ReadableByteChannel channel) throws IOException;
    
    /**
     * An InputStream backed by the resource.
     * @return
     * @throws IOException
     */
    InputStream getInputStream() throws IOException;

    /**
     * An OutputStream backed by the resource.  Writes are appended to the resource.
     * @return
     * @throws IOException
     */
    OutputStream getOutputStream() throws IOException;

    /**
     * The time the resource was last modified.
     * 
     * @see System#currentTimeMillis
     * 
     * @return
     */
    long getLastModified();
}
