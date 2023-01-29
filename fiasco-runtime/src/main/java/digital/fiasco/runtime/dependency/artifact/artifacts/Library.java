//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.dependency.artifact.artifacts;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.registry.RegistryTrait;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactContent;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.string.Strings.ensureStartsWith;
import static digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachment.attachment;
import static digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachmentType.JAVADOC_ATTACHMENT;
import static digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachmentType.SOURCES_ATTACHMENT;

/**
 * A library is an artifact with source code and Javadoc JAR attachments.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #library(String)} - Returns a library with the given artifact descriptor</li>
 *     <li>{@link #library(ArtifactDescriptor)} - Returns a library with the given artifact descriptor</li>
 *     <li>{@link #library(Artifact) - Returns a library with the given artifact's descriptor}</li>
 * </ul>
 *
 * <p><b>Attached Content</b></p>
 *
 * <ul>
 *     <li>{@link #javadoc()} - The Javadoc attachment for this library</li>
 *     <li>{@link #sources()} - The source attachment for this library</li>
 *     <li>{@link #withJavadoc(ArtifactContent)} - Returns a copy of this library with the given Javadoc content attached</li>
 *     <li>{@link #withSources(ArtifactContent)} - Returns a copy of this library with the given Javadoc source content attached</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
@SuppressWarnings("unused")
public class Library extends BaseArtifact<Library> implements RegistryTrait
{
    /**
     * Creates a {@link Library} with the given artifact descriptor
     *
     * @param descriptor The artifact descriptor
     * @return The library
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static Library library(String descriptor)
    {
        return new Library(ArtifactDescriptor.descriptor(ensureStartsWith(descriptor, "library:")));
    }

    /**
     * Creates a {@link Library} with the given artifact descriptor
     *
     * @param descriptor The artifact descriptor
     * @return The library
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static Library library(ArtifactDescriptor descriptor)
    {
        return new Library(descriptor);
    }

    /**
     * Creates a {@link Library} with the given artifact's descriptor
     *
     * @param artifact The artifact
     * @return The library
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static Library library(Artifact<?> artifact)
    {
        return library(artifact.descriptor());
    }

    public Library(ArtifactDescriptor descriptor)
    {
        super(descriptor);
    }

    public Library()
    {
        super((ArtifactDescriptor) null);
    }

    protected Library(Library that)
    {
        super(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public Library copy()
    {
        return new Library(this);
    }

    /**
     * Returns the Javadoc attachment for this library,
     *
     * @return The Javadoc content
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactContent javadoc()
    {
        return attachmentOfType(JAVADOC_ATTACHMENT).content();
    }

    /**
     * Returns the source attachment for this library,
     *
     * @return The source code JAR
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public ArtifactContent sources()
    {
        return attachmentOfType(SOURCES_ATTACHMENT).content();
    }

    /**
     * Returns a copy of this library with the given Javadoc attachment
     *
     * @param javadoc The Javadoc content
     * @return The new library
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public Library withJavadoc(ArtifactContent javadoc)
    {
        return copy().withAttachment(attachment(JAVADOC_ATTACHMENT, javadoc));
    }

    /**
     * Returns a copy of this library with the given source attachment
     *
     * @param sources The source code JAR attachment
     * @return The new library
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public Library withSources(ArtifactContent sources)
    {
        return copy().withAttachment(attachment(SOURCES_ATTACHMENT, sources));
    }
}
