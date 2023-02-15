package digital.fiasco.runtime.build.builder.tools.compile.java;

import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.FileList;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.FolderList;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.tools.BaseTool;
import digital.fiasco.runtime.build.builder.tools.Tool;
import digital.fiasco.runtime.build.builder.tools.compile.java.flags.CompilerWarning;
import digital.fiasco.runtime.build.builder.tools.compile.java.flags.DebugInformation;
import digital.fiasco.runtime.utility.CommandLineComposer;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;
import java.nio.charset.Charset;
import java.util.Locale;

import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.filesystem.Folders.currentFolder;
import static digital.fiasco.runtime.build.builder.tools.compile.java.flags.DebugInformation.ALL;
import static digital.fiasco.runtime.build.builder.tools.compile.java.flags.DebugInformation.NONE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.tools.ToolProvider.getSystemJavaCompiler;

/**
 * Compiles one or more files containing Java code.
 *
 * <p><b>Settings</b></p>
 *
 * <ul>
 *     <li>{@link #checkConsistency()}</li>
 *     <li>{@link #compilerWarnings()}</li>
 *     <li>{@link #debugInformation()}</li>
 *     <li>{@link #withDebugInformation(DebugInformation...)}</li>
 *     <li>{@link #withReleaseVersion(Version)}</li>
 *     <li>{@link #withWarningDisabled(CompilerWarning...)}</li>
 *     <li>{@link #withWarningEnabled(CompilerWarning...)}</li>
 * </ul>
 *
 * <p><b>Sources</b></p>
 *
 * <ul>
 *     <li>{@link #sourceEncoding()}</li>
 *     <li>{@link #sourceLocale()}</li>
 *     <li>{@link #sourceVersion()}</li>
 *     <li>{@link #sources()}</li>
 *     <li>{@link #withSourceEncoding(Charset)}</li>
 *     <li>{@link #withSourceLocale(Locale)}</li>
 *     <li>{@link #withSourceVersion(Version)}</li>
 *     <li>{@link #withSources(FileList)}</li>
 * </ul>
 *
 * <p><b>Targets</b></p>
 *
 * <ul>
 *     <li>{@link #targetFolder()}</li>
 *     <li>{@link #targetVersion()}</li>
 *     <li>{@link #withTargetFolder(Folder)}</li>
 *     <li>{@link #withTargetVersion(Version)}</li>
 * </ul>
 *
 * @author Jonathan Locke
 * @see Tool
 * @see BaseTool
 * @see CompilerWarning
 * @see Version
 * @see Folder
 * @see FileList
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class JavaCompiler extends BaseTool<JavaCompiler, Void>
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
    private FileList sources = new FileList();

    /** The class path to give to the compiler */
    private FolderList classpath = new FolderList();

    /** The source path to look for source code */
    private FolderList sourcepath = new FolderList();

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
    public JavaCompiler(Builder builder)
    {
        super(builder);

        this.targetFolder = builder.targetFolder();
    }

    /**
     * Creates a copy of the given compiler
     *
     * @param that The compiler to copy
     */
    public JavaCompiler(JavaCompiler that)
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
        ensure(debugInformation.equals(ObjectSet.set(NONE))
                || debugInformation.equals(ObjectSet.set(ALL))
                || !debugInformation.isEmpty(),
            "Debug information can be NONE, ALL or any combination of SOURCE_FILES, LINES, and VARIABLES");
    }

    /**
     * Returns the set of enabled compiler warnings
     *
     * @return The set
     */
    public ObjectSet<CompilerWarning> compilerWarnings()
    {
        return enabledCompilerWarnings;
    }

    /**
     * Returns a copy of this compiler tool
     */
    @Override
    public JavaCompiler copy()
    {
        return new JavaCompiler(this);
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
     * {@inheritDoc}
     */
    @Override
    public Void onRun()
    {
        compile(sources);

        return null;
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
    public JavaCompiler withDebugInformation(DebugInformation... information)
    {
        return mutated(it -> it.debugInformation = set(information));
    }

    /**
     * Returns a copy of this compiler tool with the given target Java release
     *
     * @param version The Java release
     * @return The new copy of this compiler tool
     */
    public JavaCompiler withReleaseVersion(Version version)
    {
        return mutated(it -> it.releaseVersion = version);
    }

    /**
     * Returns a copy of this compiler tool with the given source encoding
     *
     * @param sourceEncoding The new encoding
     * @return The new copy of this compiler tool
     */
    public JavaCompiler withSourceEncoding(Charset sourceEncoding)
    {
        return mutated(it -> it.sourceEncoding = sourceEncoding);
    }

    /**
     * Returns a copy of this compiler tool with the given source locale
     *
     * @param sourceLocale The new locale
     * @return The new copy of this compiler tool
     */
    public JavaCompiler withSourceLocale(Locale sourceLocale)
    {
        return mutated(it -> it.sourceLocale = sourceLocale);
    }

    /**
     * Returns a copy of this compiler tool with the given source code version
     *
     * @param version The new source version
     * @return The new copy of this compiler tool
     */
    public JavaCompiler withSourceVersion(Version version)
    {
        return mutated(it -> it.sourceVersion = version);
    }

    /**
     * Returns a copy of this compiler tool with the given source files
     *
     * @param sources The sources to compile
     * @return The new copy of this compiler tool
     */
    public JavaCompiler withSources(FileList sources)
    {
        return mutated(it -> it.sources = sources.copy());
    }

    /**
     * Returns a copy of this compiler tool with the given target folder for writing class files to
     *
     * @param folder The target folder
     * @return The new copy of this compiler tool
     */
    public JavaCompiler withTargetFolder(Folder folder)
    {
        return mutated(it -> it.targetFolder = folder);
    }

    /**
     * Returns a copy of this compiler tool with the given target JVM version
     *
     * @param version The new JVM version
     * @return The new copy of this compiler tool
     */
    public JavaCompiler withTargetVersion(Version version)
    {
        return mutated(it -> it.targetVersion = version);
    }

    /**
     * Returns a copy of this compiler tool with the given compiler warnings disabled
     *
     * @param warnings The compiler warnings
     * @return The new copy of this compiler tool
     */
    public JavaCompiler withWarningDisabled(CompilerWarning... warnings)
    {
        return mutated(it -> it.enabledCompilerWarnings.without(warnings));
    }

    /**
     * Returns a copy of this compiler tool with the given compiler warnings enabled
     *
     * @param warnings The compiler warnings
     * @return The new copy of this compiler tool
     */
    public JavaCompiler withWarningEnabled(CompilerWarning... warnings)
    {
        return mutated(it -> it.enabledCompilerWarnings.with(warnings));
    }

    /**
     * Compiles the given source files
     *
     * @param sources The sources to compile
     */
    private boolean compile(FileList sources)
    {
        if (sources.isNonEmpty())
        {
            announce("Compiling $ files in $", sources.count(), sources.parent().relativeTo(currentFolder()));
            trace("Compiling $", sources.relativeTo(currentFolder()));

            var compiler = getSystemJavaCompiler();
            var fileManager = compiler.getStandardFileManager(new ProblemListener(), sourceLocale, sourceEncoding);
            var files = fileManager.getJavaFileObjectsFromFiles(sources.asJavaFiles());
            var task = compiler.getTask(null, fileManager, new ProblemListener(), options(), null, files);
            return task.call();
        }
        return true;
    }

    private CommandLineComposer debugFlags(CommandLineComposer composer)
    {
        if (debugInformation.contains(ALL))
        {
            ensure(debugInformation.size() == 1, "ALL cannot be specified with other debug flags");
            return composer.withBooleanFlag("-g");
        }
        if (debugInformation.contains(NONE))
        {
            ensure(debugInformation.size() == 1, "NONE cannot be specified with other debug flags");
            return composer.withBooleanFlag("-g");
        }
        return composer.withOneArgumentSwitch("-g", debugInformation.map(DebugInformation::flag), ",", ":");
    }

    private StringList options()
    {
        return debugFlags(new CommandLineComposer())
            .withTwoArgumentSwitch("-classpath", classpath, ";")
            .withTwoArgumentSwitch("-d", targetFolder)
            .withTwoArgumentSwitch("-encoding", sourceEncoding)
            .withTwoArgumentSwitch("-release", releaseVersion)
            .withTwoArgumentSwitch("-source", sourceVersion)
            .withTwoArgumentSwitch("-target", targetVersion)
            .asStringList();
    }
}
