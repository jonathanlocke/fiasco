package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;

/**
 * Holds PGP signature and MD5 and SHA-1 hashes for a stored artifact.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public record ArtifactContentSignatures(
    @FormatProperty String pgp,  // PGP signature text
    @FormatProperty String md5,  // MD5 hash
    @FormatProperty String sha1) // SHA-1 hash
{
    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }
}
