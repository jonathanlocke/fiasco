package digital.fiasco.runtime.build;

/**
 * Options for builds
 *
 * @author Jonathan Locke
 */
public enum BuildOption
{
    /** Show what would be built without building it */
    DESCRIBE("describe the build rather than running it"),

    /** Build with minimal output */
    QUIET("build with minimal output"),

    /** Build with debug trace output */
    DEBUG("build with debug trace output"),

    /** Show help */
    HELP("show help");

    private final String help;

    BuildOption(String help)
    {

        this.help = help;
    }

    public String help()
    {
        return help;
    }
}
