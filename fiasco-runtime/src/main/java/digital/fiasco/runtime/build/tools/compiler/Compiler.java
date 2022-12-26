package digital.fiasco.runtime.build.tools.compiler;

import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.FileList;
import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.tools.BaseTool;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;
import java.nio.charset.Charset;
import java.util.Locale;

import static com.telenav.kivakit.core.string.Formatter.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.tools.ToolProvider.getSystemJavaCompiler;

/**
 * Compiles one or more files containing Java code.
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #encoding()}</li>
 *     <li>{@link #sourceLocale()}</li>
 *     <li>{@link #sourceVersion()}</li>
 *     <li>{@link #sources()}</li>
 *     <li>{@link #targetVersion()}</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #withEncoding(Charset)}</li>
 *     <li>{@link #withLocale(Locale)}</li>
 *     <li>{@link #withSourceVersion(Version)}</li>
 *     <li>{@link #withSources(FileList)}</li>
 *     <li>{@link #withTargetVersion(Version)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class Compiler extends BaseTool
{
    /**
     * Broadcasts compilation errors
     */
    private class ProblemListener implements DiagnosticListener<JavaFileObject>
    {
        @Override
        public void report(Diagnostic<? extends JavaFileObject> diagnostic)
        {
            problem("$ $ ($:$): $",
                diagnostic.getKind(),
                diagnostic.getCode(),
                diagnostic.getSource().getName(),
                diagnostic.getLineNumber(),
                diagnostic.getMessage(Locale.getDefault()));
        }
    }

    /** The Java source code compatibility version */
    private Version sourceVersion;

    /** The Java virtual machine bytecode target version */
    private Version targetVersion;

    /** The source resources to compile */
    private FileList sources;

    /** The source file encoding */
    private Charset sourceEncoding = UTF_8;

    /** The source file locale */
    private Locale sourceLocale = Locale.getDefault();

    /**
     * Create a new Java compiler associated with the given build
     *
     * @param build The build
     */
    public Compiler(Build build)
    {
        super(build);
    }

    /**
     * Creates a copy of the given compiler
     *
     * @param that The compiler to copy
     */
    public Compiler(Compiler that)
    {
        super(that.associatedBuild());
        this.sourceVersion = that.sourceVersion;
        this.targetVersion = that.targetVersion;
        this.sources = that.sources.copy();
        this.sourceEncoding = that.sourceEncoding;
        this.sourceLocale = that.sourceLocale;
    }

    /**
     * Returns a copy of this compiler tool
     */
    public Compiler copy()
    {
        return new Compiler(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String description()
    {
        return format("""
            Compiler
              sources: $
              source version: $
              target version: $
              source encoding: $
            """, sources, sourceVersion, targetVersion, sourceEncoding);
    }

    /**
     * Returns the source code encoding
     */
    public Charset encoding()
    {
        return sourceEncoding;
    }

    /**
     * Returns the source code locale
     */
    public Locale sourceLocale()
    {
        return sourceLocale;
    }

    /**
     * Returns the source code version
     */
    public Version sourceVersion()
    {
        return sourceVersion;
    }

    /**
     * Returns the source files to compile
     */
    public FileList sources()
    {
        return sources;
    }

    /**
     * Returns the targeted VM version
     */
    public Version targetVersion()
    {
        return targetVersion;
    }

    /**
     * Returns a copy of this compiler tool with the given source encoding
     *
     * @param sourceEncoding The new encoding
     * @return The new copy of this compiler tool
     */
    public Compiler withEncoding(Charset sourceEncoding)
    {
        var copy = copy();
        copy.sourceEncoding = sourceEncoding;
        return this;
    }

    /**
     * Returns a copy of this compiler tool with the given source locale
     *
     * @param sourceLocale The new locale
     * @return The new copy of this compiler tool
     */
    public Compiler withLocale(Locale sourceLocale)
    {
        var copy = copy();
        copy.sourceLocale = sourceLocale;
        return copy;
    }

    /**
     * Returns a copy of this compiler tool with the given source code version
     *
     * @param version The new source version
     * @return The new copy of this compiler tool
     */
    public Compiler withSourceVersion(Version version)
    {
        var copy = copy();
        copy.sourceVersion = version;
        return copy;
    }

    /**
     * Returns a copy of this compiler tool with the given source files
     *
     * @param sources The sources to compile
     * @return The new copy of this compiler tool
     */
    public Compiler withSources(FileList sources)
    {
        var copy = copy();
        copy.sources = sources.copy();
        return copy;
    }

    /**
     * Returns a copy of this compiler tool with the given target JVM version
     *
     * @param version The new JVM version
     * @return The new copy of this compiler tool
     */
    public Compiler withTargetVersion(Version version)
    {
        var copy = copy();
        copy.targetVersion = version;
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRun()
    {
        compile(sourceMainJavaSources());
    }

    /**
     * Compiles the given source files
     *
     * @param sources The sources to compile
     */
    private boolean compile(FileList sources)
    {
        information("Compiling");

        var compiler = getSystemJavaCompiler();
        var fileManager = compiler.getStandardFileManager(new ProblemListener(), sourceLocale, sourceEncoding);
        var files = fileManager.getJavaFileObjectsFromFiles(sources.asJavaFiles());
        return compiler.getTask(null, fileManager, new ProblemListener(), null, null, files).call();
    }
}
