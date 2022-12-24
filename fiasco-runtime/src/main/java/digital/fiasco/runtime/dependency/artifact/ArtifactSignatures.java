package digital.fiasco.runtime.dependency.artifact;

/**
 * Holds PGP signature and MD5 and SHA-1 hashes for a stored artifact.
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
