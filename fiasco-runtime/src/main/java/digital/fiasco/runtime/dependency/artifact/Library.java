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
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.JAVADOC_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType.SOURCES_ATTACHMENT;

/**
 * A library is an artifact with source code and Javadoc JAR attachments.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #library(String)} - Returns a library with the given artifact descriptor</li>
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
public class Library extends BaseArtifact<Library> implements RegistryTrait
{
    /**
     * Creates a list of libraries from the given variable-argument list of descriptors
     *
     * @param descriptors The library descriptors
     * @return The library dependency list
     */
    public static DependencyList<Library> libraries(String... descriptors)
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
    public static DependencyList<Library> libraries(Library... libraries)
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
        return new Library(ArtifactDescriptor.descriptor(artifactDescriptor));
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
        return attachment(JAVADOC_ATTACHMENT).content();
    }

    /**
     * Returns the source attachment for this library,
     *
     * @return The source code JAR
     */
    public ArtifactContent source()
    {
        return attachment(SOURCES_ATTACHMENT).content();
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
        copy.withAttachment(new ArtifactAttachment(this, JAVADOC_ATTACHMENT, javadoc));
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
        copy.withAttachment(new ArtifactAttachment(this, JAVADOC_ATTACHMENT, source));
        return copy;
    }
}
