package fiasco;

import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.object.Lazy;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.FileList;
import com.telenav.kivakit.filesystem.Folder;
import fiasco.dependency.DependencyList;
import fiasco.plugins.FilePattern;
import fiasco.plugins.archiver.Archiver;
import fiasco.plugins.builder.Builder;
import fiasco.plugins.compiler.Compiler;
import fiasco.plugins.copier.Copier;
import fiasco.plugins.librarian.Librarian;
import fiasco.plugins.shader.Shader;
import fiasco.plugins.tester.Tester;
import fiasco.repository.artifact.Artifact;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.object.Lazy.lazy;
import static com.telenav.kivakit.core.os.OperatingSystem.operatingSystem;

/**
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public class Module extends BaseRepeater implements Dependency<Module>
{
    private final DependencyList<Module> dependencies = new DependencyList<>();

    private final DependencyList<Library> libraries = new DependencyList<>();

    /** The project that this module belongs to */
    private FiascoBuild project;

    private Artifact artifact;

    /** The folder where this module exists */
    private final Folder folder;

    private final Lazy<Archiver> archiver = lazy(() -> new Archiver(this));

    private final Lazy<Builder> builder = lazy(() -> new Builder(this));

    private final Lazy<Compiler> compiler = lazy(() -> new Compiler(this));

    private final Lazy<Copier> copier = lazy(() -> new Copier(this));

    private final Lazy<Librarian> librarian = lazy(() -> new Librarian(this));

    private final Lazy<Shader> shader = lazy(() -> new Shader(this));

    private final Lazy<Tester> tester = lazy(() -> new Tester(this));

    public Module(final FiascoBuild project, final String relativePath)
    {
        this(project, Folder.parseFolder(relativePath));
    }

    public Module(final FiascoBuild project, final Folder relativeFolder)
    {
        this.project = project;

        ensure(relativeFolder.path().isRelative());
        folder = project().folder().folder(relativeFolder);
    }

    public Archiver archiver()
    {
        return archiver.get();
    }

    public Builder builder()
    {
        return builder.get();
    }

    public Folder classesFolder()
    {
        return outputFolder().folder("classes");
    }

    public Compiler compiler()
    {
        return compiler.get();
    }

    public Copier copier()
    {
        return copier.get();
    }

    @Override
    public DependencyList<Module> dependencies()
    {
        return dependencies;
    }

    public Folder folder()
    {
        return folder;
    }

    public Folder folder(final String path)
    {
        return folder.folder(path);
    }

    public Librarian librarian()
    {
        return librarian.get();
    }

    public DependencyList<Library> libraries()
    {
        return libraries;
    }

    public FilePattern matching(final String pattern)
    {
        return FilePattern.parse(pattern);
    }

    public Folder outputFolder()
    {
        return folder("output");
    }

    public FiascoBuild project()
    {
        return project;
    }

    public Module requires(final Library library)
    {
        libraries.add(library);
        return this;
    }

    public void requires(final Module module)
    {
        dependencies.add(module);
    }

    public Folder resourceFolder()
    {
        return folder("src/main/resources");
    }

    public Shader shader()
    {
        return shader.get();
    }

    public Folder sourceFolder()
    {
        return folder("src/main/java");
    }

    public FileList sources()
    {
        return sourceFolder().nestedFiles(matching("**/*.java"));
    }

    public Folder testResourceFolder()
    {
        return folder("src/test/resources");
    }

    public Folder testSourceFolder()
    {
        return folder("src/test/java");
    }

    public Tester tester()
    {
        return tester.get();
    }

    public Version version()
    {
        return artifact.version();
    }

    protected void artifact(final String descriptor)
    {
        artifact(Artifact.parse(descriptor));
    }

    protected void artifact(final Artifact artifact)
    {
        this.artifact = artifact;
    }

    protected boolean isMac()
    {
        return operatingSystem().isMac();
    }

    protected boolean isUnix()
    {
        return operatingSystem().isUnix();
    }

    protected boolean isWindows()
    {
        return operatingSystem().isWindows();
    }

    protected void project(final FiascoBuild project)
    {
        this.project = project;
    }
}
