//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.registry.RegistryTrait;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.string.Strings.ensureStartsWith;

/**
 * An asset is an artifact having only a single content attachment
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #asset(String)}</li>
 *     <li>{@link #asset(ArtifactDescriptor)}</li>
 *     <li>{@link #asset(Artifact)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
public class Asset extends BaseArtifact<Asset> implements RegistryTrait
{
    /**
     * Creates a {@link Asset} with the given artifact descriptor
     *
     * @param descriptor The artifact descriptor
     * @return The asset
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static Asset asset(String descriptor)
    {
        return asset(ArtifactDescriptor.descriptor(ensureStartsWith(descriptor, "asset:")));
    }

    /**
     * Creates a {@link Asset} with the given artifact descriptor
     *
     * @param descriptor The artifact descriptor
     * @return The asset
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static Asset asset(ArtifactDescriptor descriptor)
    {
        return new Asset(descriptor);
    }

    /**
     * Creates an asset for the given artifact
     *
     * @param artifact The asset artifact
     * @return The asset
     */
    @MethodQuality(documentation = DOCUMENTED, testing = UNTESTED)
    public static Asset asset(Artifact<?> artifact)
    {
        return asset(artifact.descriptor());
    }

    @MethodQuality
        (
            audience = AUDIENCE_INTERNAL,
            documentation = DOCUMENTATION_NOT_NEEDED,
            testing = TESTED
        )
    public Asset(ArtifactDescriptor descriptor)
    {
        super(descriptor);
    }

    public Asset()
    {
        super((ArtifactDescriptor) null);
    }

    @MethodQuality
        (
            audience = AUDIENCE_INTERNAL,
            documentation = DOCUMENTATION_NOT_NEEDED,
            testing = TESTED
        )
    protected Asset(Asset that)
    {
        super(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public Asset copy()
    {
        return new Asset(this);
    }
}
