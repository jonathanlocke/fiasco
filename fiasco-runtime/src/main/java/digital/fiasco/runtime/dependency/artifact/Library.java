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
import static com.telenav.kivakit.core.version.Version.parseVersion;
import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachment.CONTENT_SUFFIX;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachment.JAVADOC_SUFFIX;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachment.SOURCES_SUFFIX;

/**
 * A library is an artifact with source code and Javadoc JAR attachments.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #library(String)} - Returns a library with the given artifact descriptor</li>
 *     <li>{@link #library(Artifact)} - Returns a library with the given artifact descriptor</li>
 *     <li>{@link #library(ArtifactDescriptor)} - Returns a library with the given artifact descriptor</li>
 *     <li>{@link #libraries(Library...)} - Returns a {@link DependencyList} with the given libraries</li>
 *     <li>{@link #libraries(String...)} - Returns a {@link DependencyList} with libraries for each of the given artifact descriptors</li>
 * </ul>
 *
 * <p><b>Attached Content</b></p>
 *
 * <ul>
 *     <li>{@link #javadoc()} - The Javadoc attachment for this library</li>
 *     <li>{@link #source()} - The source attachment for this library</li>
 *     <li>{@link #withJavadoc(ArtifactContent)} - Returns a copy of this library with the given Javadoc content attached</li>
 *     <li>{@link #withSource(ArtifactContent)} - Returns a copy of this library with the given Javadoc source content attached</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public class Library extends BaseArtifact implements RegistryTrait
{
    /**
     * Creates a list of libraries from the given variable-argument list of descriptors
     *
     * @param descriptors The library descriptors
     * @return The library dependency list
     */
    public static DependencyList libraries(String... descriptors)
    {
        var libraries = stringList(descriptors).map(Library::library);
        return libraries(libraries.asArray(Library.class));
    }

    /**
     * Creates a list of libraries from the given variable-argument list of libraries
     *
     * @param libraries The libraries
     * @return The library dependency list
     */
    public static DependencyList libraries(Library... libraries)
    {
        return dependencyList(libraries);
    }

    /**
     * Creates a {@link Library} with the given artifact descriptor
     *
     * @param artifactDescriptor The artifact descriptor
     * @return The library
     */
    public static Library library(String artifactDescriptor)
    {
        return new Library(ArtifactDescriptor.artifactDescriptor(artifactDescriptor));
    }

    /**
     * Creates a {@link Library} with the given artifact descriptor
     *
     * @param artifactDescriptor The artifact descriptor
     * @return The library
     */
    public static Library library(ArtifactDescriptor artifactDescriptor)
    {
        return new Library(artifactDescriptor);
    }

    /**
     * Creates a library for the given artifact
     *
     * @param artifact The library artifact
     * @return The library
     */
    public static Library library(Artifact artifact)
    {
        return new Library(artifact.artifactDescriptor());
    }

    protected Library(ArtifactDescriptor descriptor)
    {
        super(descriptor);
    }

    protected Library(Library that)
    {
        super(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Library copy()
    {
        return new Library(this);
    }

    /**
     * Returns the Javadoc attachment for this library,
     *
     * @return The Javadoc content
     */
    public ArtifactContent javadoc()
    {
        return attachment(JAVADOC_SUFFIX).content();
    }

    /**
     * Returns the source attachment for this library,
     *
     * @return The source code JAR
     */
    public ArtifactContent source()
    {
        return attachment(SOURCES_SUFFIX).content();
    }

    /**
     * Convenience method for {@link #withVersion(Version)}
     */
    @Override
    public Artifact version(Version version)
    {
        return withVersion(version);
    }

    /**
     * Convenience method for {@link #withVersion(Version)}
     */
    @Override
    public Artifact version(String version)
    {
        return withVersion(parseVersion(version));
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
        return withDescriptor(artifactDescriptor().withArtifactIdentifier(artifact));
    }

    /**
     * Attaches the resource for the given artifact suffix, such as <i>.jar</i>
     *
     * @param attachment The content to attach
     */
    @Override
    public Library withAttachment(ArtifactAttachment attachment)
    {
        return (Library) super.withAttachment(attachment);
    }

    /**
     * Returns primary content attachment for this asset
     *
     * @return The content
     */
    @Override
    public Library withContent(ArtifactContent content)
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
    public Library withDependencies(DependencyList dependencies)
    {
        return (Library) super.withDependencies(dependencies);
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
     * Returns a copy of this library with the given Javadoc attachment
     *
     * @param javadoc The Javadoc content
     * @return The new library
     */
    public Library withJavadoc(ArtifactContent javadoc)
    {
        var copy = copy();
        copy.withAttachment(new ArtifactAttachment(this, JAVADOC_SUFFIX, javadoc));
        return copy;
    }

    /**
     * Returns a copy of this library with the given source attachment
     *
     * @param source The source code JAR attachment
     * @return The new library
     */
    public Library withSource(ArtifactContent source)
    {
        var copy = copy();
        copy.withAttachment(new ArtifactAttachment(this, JAVADOC_SUFFIX, source));
        return copy;
    }

    /**
     * Returns a copy of this artifact with the given version
     *
     * @param version The new version
     * @return The new artifact
     */
    @Override
    public Library withVersion(Version version)
    {
        return withDescriptor(artifactDescriptor().withVersion(version));
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
