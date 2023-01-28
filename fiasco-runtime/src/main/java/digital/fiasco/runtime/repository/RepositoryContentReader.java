package digital.fiasco.runtime.repository;

import com.telenav.kivakit.core.value.count.Bytes;

import java.io.InputStream;

/**
 * Called when resolving artifacts from a repository to allow the caller to read the artifact content
 *
 * @author Jonathan Locke
 */
public interface RepositoryContentReader
{
    static RepositoryContentReader nullContentReader()
    {
        return (in, length) ->
        {
        };
    }

    /**
     * Callback method to read the input stream of the given length
     *
     * @param in The input stream
     * @param length The length of the stream
     */
    void read(InputStream in, Bytes length);
}
