package digital.fiasco.runtime.repository.local;

import com.telenav.kivakit.filesystem.Folder;
import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.dependency.artifact.Asset;
import digital.fiasco.runtime.dependency.artifact.Library;
import digital.fiasco.runtime.repository.Repository;
import digital.fiasco.runtime.repository.local.LocalRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.filesystem.Folders.currentFolder;
import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.descriptors;

public class LocalRepositoryTest extends FiascoTest
{
    @Test
    public void testCreate()
    {
        var core = kivakitCore()
            .withContent(packageContent());

        var funky = new LocalRepository("funky").clear();
        funky.installArtifact(core);

        var resolved = funky.resolveArtifacts(descriptors(":com.telenav.kivakit::"));
        ensure(resolved.size() == 1);
        ensureEqual(resolved.first(), core);
    }

    @Test
    public void testEquals()
    {
        ensureEqual(repository(), repository());
        ensureNotEqual(repository(), 5);
    }

    @Test
    public void testInstall()
    {
        var core = kivakitCore()
            .withContent(packageContent());

        var repository = repository();
        repository.installArtifact(core);

        var resolved = repository.resolveArtifacts(list(core.descriptor()));
        ensure(resolved.size() == 1);
        ensure(resolved.first().equals(core));
    }

    @Test
    public void testIsRemote()
    {
        ensure(!repository().isRemote());
    }

    @Test
    public void testName()
    {
        ensure(repository().name().equals("test"));
    }

    @Test
    public void testReload()
    {
        var core = kivakitCore()
            .withContent(packageContent());
        var icons = kivakitIcons()
            .withContent(packageContent());
        var logos = kivakitLogos()
            .withContent(packageContent());

        var repository = repository();
        repository.installArtifact(core);
        repository.installArtifact(icons);
        repository.installArtifact(logos);

        var reloaded = new LocalRepository("test", root());
        testRepository(reloaded, core, icons, logos);
    }

    @Test
    public void testResolve()
    {
        var core = kivakitCore()
            .withContent(packageContent());
        var icons = kivakitIcons()
            .withContent(packageContent());
        var logos = kivakitLogos()
            .withContent(packageContent());

        var repository = repository();
        repository.installArtifact(core);
        repository.installArtifact(icons);
        repository.installArtifact(logos);

        testRepository(repository, core, icons, logos);
    }

    @Test
    public void testUri()
    {
        ensureEqual(repository().uri(), root().asUri());
    }

    @NotNull
    private static Repository repository()
    {
        return new LocalRepository("test", root().mkdirs().clearAll());
    }

    private static Folder root()
    {
        return currentFolder().folder("target/.fiasco/test").absolute();
    }

    private void testRepository(Repository repository, Library core, Asset icons, Asset logos)
    {
        {
            var resolved = repository.resolveArtifacts(list(core.descriptor()));
            ensure(resolved.size() == 1);
            ensure(resolved.first().equals(core));
        }
        {
            var resolved = repository.resolveArtifacts(list(icons.descriptor()));
            ensure(resolved.size() == 1);
            ensure(resolved.first().equals(icons));
        }
        {
            var resolved = repository.resolveArtifacts(descriptors(":com.telenav.kivakit::")).sorted();
            ensure(resolved.size() == 3);
            ensure(resolved.get(0).equals(icons));
            ensure(resolved.get(1).equals(logos));
            ensure(resolved.get(2).equals(core));
        }
        {
            var resolved = repository.resolveArtifacts(descriptors(":com.telenav.x::"));
            ensure(resolved.size() == 0);
        }
    }
}