package digital.fiasco.runtime.build.builder.tools.compiler;

import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.FileList;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.FolderList;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.tools.BaseTool;
import digital.fiasco.runtime.build.builder.tools.compiler.flags.CompilerWarning;
import digital.fiasco.runtime.build.builder.tools.compiler.flags.DebugInformation;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;
import java.nio.charset.Charset;
import java.util.Locale;

import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.string.Formatter.format;
import static digital.fiasco.runtime.build.builder.tools.compiler.flags.DebugInformation.ALL;
import static digital.fiasco.runtime.build.builder.tools.compiler.flags.DebugInformation.NONE;
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
 *     <li>{@link #withSourceEncoding(Charset)}</li>
 *     <li>{@link #withSourceLocale(Locale)}</li>
 *     <li>{@link #withSourceVersion(Version)}</li>
 *     <li>{@link #withSources(FileList)}</li>
 *     <li>{@link #withTargetVersion(Version)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class Compiler extends BaseTool<Compiler>
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

    /** The JVM release being targeted */
    private Version releaseVersion;

    /** The source resources to compile */
    private FileList sources;

    /** The class path to give to the compiler */
    private FolderList classpath;

    /** The source path to look for source code */
    private FolderList sourcepath;

    /** Folder to write class files to */
    private Folder targetFolder;

    /** The source file encoding */
    private Charset sourceEncoding = UTF_8;

    /** The kinds of debug information to include in compiled classes */
    private ObjectSet<DebugInformation> debugInformation = set();

    /** The compiler warnings that are enabled */
    private ObjectSet<CompilerWarning> enabledCompilerWarnings = set();

    /** The source file locale */
    private Locale sourceLocale = Locale.getDefault();

    /**
     * Create a new Java compiler associated with the given builder
     *
     * @param builder The builder
     */
    public Compiler(Builder builder)
    {
        super(builder);
    }

    /**
     * Creates a copy of the given compiler
     *
     * @param that The compiler to copy
     */
    public Compiler(Compiler that)
    {
        super(that.associatedBuilder());
        this.classpath = that.classpath.copy();
        this.debugInformation = that.debugInformation;
        this.enabledCompilerWarnings = that.enabledCompilerWarnings.copy();
        this.releaseVersion = that.releaseVersion;
        this.sourceEncoding = that.sourceEncoding;
        this.sourceLocale = that.sourceLocale;
        this.sourceVersion = that.sourceVersion;
        this.sourcepath = that.sourcepath.copy();
        this.sources = that.sources.copy();
        this.targetFolder = that.targetFolder;
        this.targetVersion = that.targetVersion;
    }

    @Override
    public void checkConsistency()
    {
        ensure(debugInformation.equals(set(NONE))
                || debugInformation.equals(set(ALL))
                || !debugInformation.isEmpty(),
            "Debug information can be NONE, ALL or any combination of SOURCE_FILES, LINES, and VARIABLES");
    }

    /**
     * Returns a copy of this compiler tool
     */
    @Override
    public Compiler copy()
    {
        return new Compiler(this);
    }

    /**
     * Returns the set of debug information that will be included in compiled class files
     *
     * @return The set of {@link DebugInformation} enum values
     */
    public ObjectSet<DebugInformation> debugInformation()
    {
        return debugInformation;
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
     * Returns the set of enabled compiler warnings
     *
     * @return The set
     */
    public ObjectSet<CompilerWarning> enabledCompilerWarnings()
    {
        return enabledCompilerWarnings;
    }

    /**
     * Returns the source code encoding
     */
    public Charset encoding()
    {
        return sourceEncoding;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRun()
    {
        compile(sourceMainJavaSources());
    }

    /**
     * Returns the targeted Java release
     */
    public Version releaseVersion()
    {
        return releaseVersion;
    }

    /**
     * Returns the source code encoding
     */
    public Charset sourceEncoding()
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
     * Returns the target folder this compiler will use for class file output
     *
     * @return The folder
     */
    @Override
    public Folder targetFolder()
    {
        return targetFolder;
    }

    /**
     * Returns the targeted VM version
     */
    public Version targetVersion()
    {
        return targetVersion;
    }

    /**
     * Returns a copy of this compiler tool with the given debug information included in class files
     *
     * @param information The debug information to include
     * @return The new copy of this compiler tool
     */
    public Compiler withDebugInformation(DebugInformation... information)
    {
        return mutatedCopy(it -> it.debugInformation = set(information));
    }

    /**
     * Returns a copy of this compiler tool with the given compiler warnings disabled
     *
     * @param warnings The compiler warnings
     * @return The new copy of this compiler tool
     */
    public Compiler withDisabled(CompilerWarning... warnings)
    {
        return mutatedCopy(it -> it.enabledCompilerWarnings.without(warnings));
    }

    /**
     * Returns a copy of this compiler tool with the given compiler warnings enabled
     *
     * @param warnings The compiler warnings
     * @return The new copy of this compiler tool
     */
    public Compiler withEnabled(CompilerWarning... warnings)
    {
        return mutatedCopy(it -> it.enabledCompilerWarnings.with(warnings));
    }

    /**
     * Returns a copy of this compiler tool with the given target Java release
     *
     * @param version The Java release
     * @return The new copy of this compiler tool
     */
    public Compiler withReleaseVersion(Version version)
    {
        return mutatedCopy(it -> it.releaseVersion = version);
    }

    /**
     * Returns a copy of this compiler tool with the given source encoding
     *
     * @param sourceEncoding The new encoding
     * @return The new copy of this compiler tool
     */
    public Compiler withSourceEncoding(Charset sourceEncoding)
    {
        return mutatedCopy(it -> it.sourceEncoding = sourceEncoding);
    }

    /**
     * Returns a copy of this compiler tool with the given source locale
     *
     * @param sourceLocale The new locale
     * @return The new copy of this compiler tool
     */
    public Compiler withSourceLocale(Locale sourceLocale)
    {
        return mutatedCopy(it -> it.sourceLocale = sourceLocale);
    }

    /**
     * Returns a copy of this compiler tool with the given source code version
     *
     * @param version The new source version
     * @return The new copy of this compiler tool
     */
    public Compiler withSourceVersion(Version version)
    {
        return mutatedCopy(it -> it.sourceVersion = version);
    }

    /**
     * Returns a copy of this compiler tool with the given source files
     *
     * @param sources The sources to compile
     * @return The new copy of this compiler tool
     */
    public Compiler withSources(FileList sources)
    {
        return mutatedCopy(it -> it.sources = sources.copy());
    }

    /**
     * Returns a copy of this compiler tool with the given target folder for writing class files to
     *
     * @param folder The target folder
     * @return The new copy of this compiler tool
     */
    public Compiler withTargetFolder(Folder folder)
    {
        return mutatedCopy(it -> it.targetFolder = folder);
    }

    /**
     * Returns a copy of this compiler tool with the given target JVM version
     *
     * @param version The new JVM version
     * @return The new copy of this compiler tool
     */
    public Compiler withTargetVersion(Version version)
    {
        return mutatedCopy(it -> it.targetVersion = version);
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
