package digital.fiasco.runtime.repository.artifact;

/**
 * Holds PGP signature and md5 and sha1 hashes for a stored artifact.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public record ArtifactSignature
        (
                String pgp, // PGP signature text
                String md5, // MD5 hash
                String sha1 // SHA-1 hash
        )
{
}
