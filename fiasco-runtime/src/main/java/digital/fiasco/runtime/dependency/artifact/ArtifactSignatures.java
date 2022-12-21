package digital.fiasco.runtime.dependency.artifact;

/**
 * Holds PGP signature and md5 and sha1 hashes for a stored artifact.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public record ArtifactSignatures(
        String pgp,  // PGP signature text
        String md5,  // MD5 hash
        String sha1) // SHA-1 hash
{
}
