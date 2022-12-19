package digital.fiasco.runtime.build.tools.compiler;

import com.telenav.kivakit.core.string.Formatter;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.FileList;
import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.tools.BaseTool;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;
import java.nio.charset.Charset;
import java.util.Locale;

import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.tools.ToolProvider.getSystemJavaCompiler;

@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class Compiler extends BaseTool
{
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

    private Version sourceVersion;

    private Version targetVersion;

    private FileList sources;

    private Charset charset = UTF_8;

    private Locale locale = Locale.getDefault();

    public Compiler(Build build)
    {
        super(build);
    }

    public Compiler charSet(Charset charset)
    {
        this.charset = charset;
        return this;
    }

    public Compiler locale(Locale locale)
    {
        this.locale = locale;
        return this;
    }

    public Compiler sourceVersion(Version version)
    {
        sourceVersion = version;
        return this;
    }

    public Compiler sources(FileList sources)
    {
        this.sources = sources;
        return this;
    }

    public Compiler targetVersion(Version version)
    {
        targetVersion = version;
        return this;
    }

    @Override
    protected String description()
    {
        return Formatter.format("""
                Compiler
                  sources: $
                  source version: $
                  target version: $
                  charset: $
                """, sources, sourceVersion, targetVersion, charset);
    }

    @Override
    protected void onRun()
    {
        compile(associatedBuild().javaSources());
    }

    private boolean compile(FileList sources)
    {
        information("Compiling");

        var compiler = getSystemJavaCompiler();
        var fileManager = compiler.getStandardFileManager(new ProblemListener(), locale, charset);
        var files = fileManager.getJavaFileObjectsFromFiles(sources.asJavaFiles());
        return compiler.getTask(null, fileManager, new ProblemListener(), null, null, files).call();
    }
}
