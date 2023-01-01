//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import digital.fiasco.runtime.dependency.DependencyList;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.language.Arrays.arrayContains;
import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachment.CONTENT_SUFFIX;
import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.artifactDescriptor;

/**
 * An asset is an artifact having only a single content attachment
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
public class Asset extends BaseArtifact implements RegistryTrait
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
        return new Asset(artifactDescriptor);
    }

    /**
     * Creates an asset for the given artifact
     *
     * @param artifact The asset artifact
     * @return The asset
     */
    public static Asset asset(Artifact artifact)
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

    /**
     * Returns a copy of this artifact with the given identifier
     *
     * @param artifact The new artifact identifier
     * @return The new artifact
     */
    @Override
    public Artifact withArtifactIdentifier(String artifact)
    {
        return withDescriptor(descriptor().withArtifactIdentifier(artifact));
    }

    /**
     * Attaches the resource for the given artifact suffix, such as <i>.jar</i>
     *
     * @param attachment The content to attach
     */
    @Override
    public Asset withAttachment(ArtifactAttachment attachment)
    {
        return (Asset) super.withAttachment(attachment);
    }

    /**
     * Returns primary content attachment for this asset
     *
     * @return The content
     */
    @Override
    public Asset withContent(ArtifactContent content)
    {
        var copy = copy();
        copy.withAttachment(new ArtifactAttachment(this, CONTENT_SUFFIX, content));
        return copy;
    }

    /**
     * Returns a copy of this artifact with the given dependencies
     *
     * @param dependencies The new dependencies
     * @return The new artifact
     */
    @Override
    public Asset withDependencies(DependencyList dependencies)
    {
        return (Asset) super.withDependencies(dependencies);
    }

    /**
     * Returns a copy of this artifact with the given descriptor
     *
     * @param descriptor The new descriptor
     * @return The new artifact
     */
    @Override
    public Library withDescriptor(ArtifactDescriptor descriptor)
    {
        return (Library) super.withDescriptor(descriptor);
    }

    /**
     * Returns a copy of this artifact with the given version
     *
     * @param version The new version
     * @return The new artifact
     */
    @Override
    public Asset withVersion(Version version)
    {
        return (Asset) super.withVersion(version);
    }

    /**
     * Returns a copy of this artifact that excludes the given descriptors from its dependencies
     *
     * @param exclude The descriptors to exclude
     * @return The new artifact
     */
    @Override
    public Library withoutDependencies(ArtifactDescriptor... exclude)
    {
        return withoutDependencies(library -> arrayContains(exclude, library));
    }

    /**
     * Returns a copy of this artifact that excludes the given descriptors from its dependencies
     *
     * @param exclude The descriptors to exclude
     * @return The new artifact
     */
    @Override
    public Library withoutDependencies(String... exclude)
    {
        var descriptors = list(exclude).map(ArtifactDescriptor::artifactDescriptor);
        return withoutDependencies(descriptors::contains);
    }

    /**
     * Returns a copy of this artifact that excludes all descriptors matching the given pattern from its dependencies
     *
     * @param pattern The pattern to exclude
     * @return The new artifact
     */
    @Override
    public Library withoutDependencies(Matcher<ArtifactDescriptor> pattern)
    {
        return (Library) super.withoutDependencies(pattern);
    }
}
