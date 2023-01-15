package digital.fiasco.runtime.dependency.artifact;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_PUBLIC;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Holds PGP signature and MD5 and SHA-1 hashes for a stored artifact.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
@TypeQuality
    (
        documentation = DOCUMENTED,
        testing = TESTING_NOT_NEEDED,
        stability = STABLE
    )
public record ArtifactContentSignatures
    (
        @FormatProperty @Expose String pgp,  // PGP signature text
        @FormatProperty @Expose String md5,  // MD5 hash
        @FormatProperty @Expose String sha1  // SHA-1 hash
    )
{
    @MethodQuality
        (
            documentation = DOCUMENTATION_NOT_NEEDED,
            testing = TESTED
        )
    public static ArtifactContentSignatures signatures()
    {
        return new ArtifactContentSignatures(null, null, null);
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    @MethodQuality
        (
            documentation = DOCUMENTATION_NOT_NEEDED,
            testing = TESTED
        )
    public ArtifactContentSignatures withMd5(String md5)
    {
        return new ArtifactContentSignatures(pgp, md5, sha1);
    }

    @MethodQuality
        (
            documentation = DOCUMENTATION_NOT_NEEDED,
            testing = TESTED
        )
    public ArtifactContentSignatures withPgp(String pgp)
    {
        return new ArtifactContentSignatures(pgp, md5, sha1);
    }

    @MethodQuality
        (
            documentation = DOCUMENTATION_NOT_NEEDED,
            testing = TESTED
        )
    public ArtifactContentSignatures withSha1(String sha1)
    {
        return new ArtifactContentSignatures(pgp, md5, sha1);
    }
}
