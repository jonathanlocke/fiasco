//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.dependency.artifact;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.registry.RegistryTrait;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.DependencyList;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;
import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.artifactDescriptor;
import static digital.fiasco.runtime.dependency.artifact.ArtifactType.LIBRARY;

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
 *     <li>{@link #withJavadoc(ArtifactContentMetadata)} - Returns a copy of this library with the given Javadoc content attached</li>
 *     <li>{@link #withSource(ArtifactContentMetadata)} - Returns a copy of this library with the given Javadoc source content attached</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public class Library extends BaseArtifact<Library> implements
        Dependency<Library>,
        RegistryTrait
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
        return new Library(artifactDescriptor(artifactDescriptor));
    }

    /**
     * Creates a {@link Library} with the given artifact descriptor
     *
     * @param artifactDescriptor The artifact descriptor
     * @return The library
     */
    public static Library library(ArtifactDescriptor artifactDescriptor)
    {
        return new Library(artifactDescriptor).withType(LIBRARY);
    }

    /**
     * Creates a library for the given artifact
     *
     * @param artifact The library artifact
     * @return The library
     */
    public static Library library(Artifact<?> artifact)
    {
        return new Library(artifact.descriptor());
    }

    /** The Javadoc content for this library */
    private ArtifactContentMetadata javadoc;

    /** The source code for this library */
    private ArtifactContentMetadata source;

    protected Library(ArtifactDescriptor descriptor)
    {
        super(descriptor);
    }

    protected Library(Library that)
    {
        super(that);
        this.javadoc = that.javadoc;
        this.source = that.source;
    }

    @Override
    public ObjectList<ArtifactContentMetadata> attachments()
    {
        return list(jar(), javadoc(), source());
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
    public ArtifactContentMetadata javadoc()
    {
        return javadoc;
    }

    /**
     * Returns the source attachment for this library,
     *
     * @return The source code JAR
     */
    public ArtifactContentMetadata source()
    {
        return source;
    }

    /**
     * Returns a copy of this library with the given Javadoc attachment
     *
     * @param javadoc The Javadoc content
     * @return The new library
     */
    public Library withJavadoc(ArtifactContentMetadata javadoc)
    {
        var copy = copy();
        copy.javadoc = javadoc;
        return copy;
    }

    /**
     * Returns a copy of this library with the given source attachment
     *
     * @param source The source code JAR attachment
     * @return The new library
     */
    public Library withSource(ArtifactContentMetadata source)
    {
        var copy = copy();
        copy.source = source;
        return copy;
    }
}
