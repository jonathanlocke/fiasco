//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.core.registry.RegistryTrait;
import digital.fiasco.runtime.dependency.DependencyList;

import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;
import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.artifactDescriptor;
import static digital.fiasco.runtime.dependency.artifact.ArtifactType.ASSET;

/**
 * An asset is an artifact having only a single JAR containing resource content
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #asset(String)} - Returns an asset with the given artifact descriptor</li>
 *     <li>{@link #asset(Artifact)} - Returns an asset with the given artifact descriptor</li>
 *     <li>{@link #asset(ArtifactDescriptor)} - Returns an asset with the given artifact descriptor</li>
 *     <li>{@link #assets(Asset...)} - Returns a {@link DependencyList} with the given assets</li>
 *     <li>{@link #assets(String...)} - Returns a {@link DependencyList} with assets for each of the given artifact descriptors</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public class Asset extends BaseArtifact<Asset> implements RegistryTrait
{
    /**
     * Creates a {@link Asset} with the given artifact descriptor
     *
     * @param artifactDescriptor The artifact descriptor
     * @return The asset
     */
    public static Asset asset(String artifactDescriptor)
    {
        return asset(artifactDescriptor(artifactDescriptor));
    }

    /**
     * Creates a {@link Asset} with the given artifact descriptor
     *
     * @param artifactDescriptor The artifact descriptor
     * @return The asset
     */
    public static Asset asset(ArtifactDescriptor artifactDescriptor)
    {
        return new Asset(artifactDescriptor).withType(ASSET);
    }

    /**
     * Creates an asset for the given artifact
     *
     * @param artifact The asset artifact
     * @return The asset
     */
    public static Asset asset(Artifact<?> artifact)
    {
        return asset(artifact.descriptor());
    }

    /**
     * Creates a list of libraries from the given variable-argument list of descriptors
     *
     * @param descriptors The artifact descriptors
     * @return The asset dependency list
     */
    public static DependencyList assets(String... descriptors)
    {
        var assets = stringList(descriptors).map(Asset::asset);
        return dependencyList(assets.asArray(Asset.class));
    }

    /**
     * Creates a list of assets from the given variable-argument list of libraries
     *
     * @param assets The assets
     * @return The asset dependency list
     */
    public static DependencyList assets(Asset... assets)
    {
        return dependencyList(assets);
    }

    protected Asset(ArtifactDescriptor descriptor)
    {
        super(descriptor);
    }

    protected Asset(Asset that)
    {
        super(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Asset copy()
    {
        return new Asset(this);
    }
}
