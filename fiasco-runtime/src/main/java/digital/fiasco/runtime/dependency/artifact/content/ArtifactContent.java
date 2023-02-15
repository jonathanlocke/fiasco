package digital.fiasco.runtime.dependency.artifact.content;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.data.formats.yaml.model.YamlBlock;
import com.telenav.kivakit.interfaces.object.Copyable;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceIdentifier;

import java.util.Objects;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.language.Objects.areEqualPairs;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.time.Duration.ONE_SECOND;
import static com.telenav.kivakit.core.time.KivaKitTimeFormats.KIVAKIT_DATE_TIME_SECONDS;
import static com.telenav.kivakit.core.time.LocalTime.localTime;
import static com.telenav.kivakit.core.value.count.Bytes.bytes;
import static com.telenav.kivakit.data.formats.yaml.model.YamlBlock.yamlBlock;
import static com.telenav.kivakit.data.formats.yaml.model.YamlScalar.yamlScalar;

/**
 * Holds the content for a single artifact attachment, for example the main JAR, Javadoc, or source code.
 *
 * <p><b>YAML</b></p>
 *
 * <ul>
 *     <li>{@link #toYaml()}</li>
 *     <li>{@link #content(YamlBlock)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
public class ArtifactContent implements Copyable<ArtifactContent>
{
    public static ArtifactContent content()
    {
        return new ArtifactContent(null, null, null, -1, null, null);
    }

    public static ArtifactContent content(Resource resource)
    {
        return content().withResource(resource);
    }

    public static ArtifactContent content(YamlBlock block)
    {
        return new ArtifactContent(block);
    }

    @FormatProperty
    @Expose
    private String name;

    @FormatProperty
    @Expose
    private ArtifactContentSignatures signatures;

    @FormatProperty
    @Expose
    private final ResourceIdentifier resourceIdentifier;

    @FormatProperty
    @Expose
    private long offset;

    @FormatProperty
    @Expose
    private LocalTime lastModified;

    @FormatProperty
    @Expose
    private Bytes size;

    protected ArtifactContent(YamlBlock block)
    {
        name = block.scalar("name").string();
        size = bytes(block.scalar("size").number().intValue());
        lastModified = localTime(KIVAKIT_DATE_TIME_SECONDS, block.scalar("lastModified").string()).roundDown(ONE_SECOND);
        offset = block.scalar("offset").number().longValue();
        resourceIdentifier = new ResourceIdentifier(block.scalar("resourceIdentifier").string());
        if (block.has("signatures"))
        {
            signatures = ArtifactContentSignatures.signatures(block.block("signatures"));
        }
    }

    protected ArtifactContent(ArtifactContent that)
    {
        this.name = that.name;
        this.signatures = that.signatures;
        this.resourceIdentifier = that.resourceIdentifier;
        this.offset = that.offset;
        this.lastModified = that.lastModified.roundDown(ONE_SECOND);
        this.size = that.size;
    }

    /**
     * @param name The name of the artifact resource
     * @param signatures The signatures for this content
     * @param resourceIdentifier The resource containing the content
     * @param offset The offset of this content in a cache file (if any)
     * @param lastModified The time of last modification of the resource
     * @param size The size of the content
     */
    protected ArtifactContent(
        String name,
        ArtifactContentSignatures signatures,
        ResourceIdentifier resourceIdentifier,
        long offset,
        LocalTime lastModified,
        Bytes size
    )
    {
        this.name = name;
        this.signatures = signatures;
        this.resourceIdentifier = resourceIdentifier;
        this.offset = offset;
        this.lastModified = lastModified.roundDown(ONE_SECOND);
        this.size = size;
    }

    @Override
    public ArtifactContent copy()
    {
        return new ArtifactContent(this);
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof ArtifactContent that)
        {
            return areEqualPairs(this.name, that.name,
                this.signatures, that.signatures,
                this.resourceIdentifier, that.resourceIdentifier,
                this.offset, that.offset,
                this.lastModified, that.lastModified,
                this.size, that.size);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, signatures, resourceIdentifier, offset, lastModified, size);
    }

    @FormatProperty
    public Time lastModified()
    {
        return lastModified;
    }

    @FormatProperty
    public String name()
    {
        return name;
    }

    @FormatProperty
    public long offset()
    {
        return offset;
    }

    public Resource resource()
    {
        return resourceIdentifier.resolve(throwingListener());
    }

    @FormatProperty
    public ResourceIdentifier resourceIdentifier()
    {
        return resourceIdentifier;
    }

    @FormatProperty
    public ArtifactContentSignatures signatures()
    {
        return signatures;
    }

    @FormatProperty
    public Bytes size()
    {
        return size;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    public YamlBlock toYaml()
    {
        var yaml = yamlBlock()
            .with(yamlScalar("name", name()))
            .with(yamlScalar("offset", offset()))
            .with(yamlScalar("size", size.asBytes()))
            .with(yamlScalar("lastModified", lastModified.asLocalTime().roundDown(ONE_SECOND).asDateTimeSecondsString()))
            .with(yamlScalar("resourceIdentifier", resourceIdentifier.identifier()));

        if (signatures != null)
        {
            yaml = yaml.with(signatures.toYaml());
        }

        return yaml;
    }

    /**
     * Returns a copy of this artifact content with the given last modified time
     *
     * @param lastModified The new last modified time
     * @return The new artifact content
     */
    public ArtifactContent withLastModified(LocalTime lastModified)
    {
        return mutated(it -> it.lastModified = lastModified.roundDown(ONE_SECOND));
    }

    /**
     * Returns a copy of this artifact content with the given offset
     *
     * @param name The content's name
     * @return The new artifact content
     */
    public ArtifactContent withName(String name)
    {
        return mutated(it -> it.name = name);
    }

    /**
     * Returns a copy of this artifact content with the given offset
     *
     * @param offset The new content offset
     * @return The new artifact content
     */
    public ArtifactContent withOffset(long offset)
    {
        return mutated(it -> it.offset = offset);
    }

    /**
     * Returns a copy of this artifact content with the given offset
     *
     * @param resource The content
     * @return The new artifact content
     */
    public ArtifactContent withResource(Resource resource)
    {
        var data = resource.reader().readBytes();
        var content = new ArtifactContent(name, signatures, resource == null
            ? null
            : resource.identifier(), offset, lastModified, size);

        return content
            .withSize(resource.sizeInBytes())
            .withOffset(0)
            .withName(resource.fileName().name())
            .withLastModified(resource.lastModified().asLocalTime());
    }

    /**
     * Returns a copy of this artifact content with the given offset
     *
     * @param signatures The content's hash signatures
     * @return The new artifact content
     */
    public ArtifactContent withSignatures(ArtifactContentSignatures signatures)
    {
        return mutated(it -> it.signatures = signatures);
    }

    /**
     * Returns a copy of this artifact content with the given size
     *
     * @param size The new size
     * @return The new artifact content
     */
    public ArtifactContent withSize(Bytes size)
    {
        return mutated(it -> it.size = size);
    }
}
