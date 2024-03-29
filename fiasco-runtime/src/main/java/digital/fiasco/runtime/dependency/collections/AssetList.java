package digital.fiasco.runtime.dependency.collections;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.types.Asset;

import java.util.Collection;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;

/**
 * A list of {@link Asset}s.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #assets(ArtifactDescriptor...)}</li>
 *     <li>{@link #assets(Asset...)}</li>
 *     <li>{@link #assets(Collection)}</li>
 *     <li>{@link #assets(Iterable)}</li>
 *     <li>{@link #assets(String...)}</li>
 * </ul>
 *
 * <p><b>Matching</b></p>
 *
 * <ul>
 *     <li>{@link #asAssetList()}</li>
 *     <li>{@link #asAssetList()}</li>
 *     <li>{@link #matching(Matcher)}</li>
 * </ul>
 *
 * <p><b>List Operations</b></p>
 *
 * <ul>
 *     <li>{@link #count()}</li>
 *     <li>{@link #size()}</li>
 *     <li>{@link #first()}</li>
 *     <li>{@link #get(int)}</li>
 *     <li>{@link #iterator()}</li>
 *     <li>{@link #sorted()}</li>
 *     <li>{@link #join(String)}</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asDescriptors()}</li>
 *     <li>{@link #asStringList()}</li>
 *     <li>{@link #asMutableList()}</li>
 *     <li>{@link #asMutableSet()}</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #copy()}</li>
 *     <li>{@link BaseDependencyList#with(Dependency)}</li>
 *     <li>{@link BaseDependencyList#with(Dependency, Dependency[])}</li>
 *     <li>{@link BaseDependencyList#with(Dependency[])}</li>
 *     <li>{@link BaseDependencyList#without(Matcher)}</li>
 *     <li>{@link BaseDependencyList#without(Collection)}</li>
 *     <li>{@link BaseDependencyList#without(Dependency)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 * @see Artifact
 * @see Asset
 * @see Asset
 * @see BaseDependencyList
 */
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
public class AssetList extends BaseDependencyList<Asset, AssetList>
{
    /**
     * Creates a list of assets
     *
     * @param assets The assets to add
     * @return The list of assets
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static AssetList assets(Collection<Asset> assets)
    {
        return new AssetList(assets);
    }

    /**
     * Creates a list of assets
     *
     * @param assets The assets to add
     * @return The list of assets
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static AssetList assets(Asset... assets)
    {
        return assets(list(assets));
    }

    /**
     * Creates a list of assets
     *
     * @param assets The assets to add
     * @return The list of assets
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static AssetList assets(ArtifactDescriptor... assets)
    {
        return new AssetList(list(assets).map(Asset::asset));
    }

    /**
     * Creates a list of assets
     *
     * @param assets The assets to add
     * @return The list of assets
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static AssetList assets(Iterable<ArtifactDescriptor> assets)
    {
        return new AssetList(list(assets).map(Asset::asset));
    }

    /**
     * Creates a list of assets from the given variable-argument list of descriptors
     *
     * @param descriptors The asset descriptors
     * @return The list of assets
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static AssetList assets(String... descriptors)
    {
        var assets = stringList(descriptors).map(Asset::asset);
        return assets(assets.asArray(Asset.class));
    }

    public AssetList()
    {
    }

    protected AssetList(AssetList that)
    {
        super(that);
    }

    protected AssetList(Collection<Asset> assets)
    {
        super(assets);
    }

    @Override
    protected AssetList newList()
    {
        return new AssetList();
    }

    @Override
    protected AssetList newList(AssetList that)
    {
        return new AssetList(that);
    }
}
