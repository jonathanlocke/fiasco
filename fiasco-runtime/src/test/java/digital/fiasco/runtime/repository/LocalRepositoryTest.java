package digital.fiasco.runtime.repository;

import com.telenav.kivakit.filesystem.Folder;
import digital.fiasco.runtime.FiascoTest;
import digital.fiasco.runtime.dependency.artifact.Asset;
import digital.fiasco.runtime.dependency.artifact.Library;
import digital.fiasco.runtime.repository.local.LocalRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.filesystem.Folders.currentFolder;
import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.descriptor;

public class LocalRepositoryTest extends FiascoTest
{
    @Test
    public void testInstall()
    {
        var core = kivakitCore()
            .withContent(packageContent());

        var local = local();
        local.installArtifact(core);
        var resolved = local.resolveArtifacts(list(core.descriptor()));
        ensure(resolved.size() == 1);
        ensure(resolved.first().equals(core));
    }

    @Test
    public void testName()
    {
        ensure(local().name().equals("test"));
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

        var local = local();
        local.installArtifact(core);
        local.installArtifact(icons);
        local.installArtifact(logos);

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

        var local = local();
        local.installArtifact(core);
        local.installArtifact(icons);
        local.installArtifact(logos);

        testRepository(local, core, icons, logos);
    }

    @Test
    public void testUri()
    {
        ensureEqual(local().uri(), root().asUri());
    }

    @NotNull
    private static LocalRepository local()
    {
        return new LocalRepository("test", root().mkdirs().clearAll());
    }

    private static Folder root()
    {
        return currentFolder().folder("target/.fiasco/test").absolute();
    }

    private void testRepository(LocalRepository local, Library core, Asset icons, Asset logos)
    {
        {
            var resolved = local.resolveArtifacts(list(core.descriptor()));
            ensure(resolved.size() == 1);
            ensure(resolved.first().equals(core));
        }
        {
            var resolved = local.resolveArtifacts(list(icons.descriptor()));
            ensure(resolved.size() == 1);
            ensure(resolved.first().equals(icons));
        }
        {
            var resolved = local.resolveArtifacts(list(descriptor("com.telenav.kivakit::")));
            ensure(resolved.size() == 3);
            ensure(resolved.first().equals(core));
            ensure(resolved.get(1).equals(icons));
            ensure(resolved.get(2).equals(logos));
        }
        {
            var resolved = local.resolveArtifacts(list(descriptor("com.telenav.x::")));
            ensure(resolved.size() == 0);
        }
    }
}