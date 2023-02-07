package digital.fiasco.runtime.build.settings;

import digital.fiasco.runtime.build.Build;

/**
 * Options for builds. See {@link Build} for details.
 *
 * @author Jonathan Locke
 * @see Build
 * @see BuildSettings
 */
public enum BuildOption
{
    /** Show what would be built without building it */
    DESCRIBE("describe the build rather than running it"),

    /** Describe operations as well as executing them */
    VERBOSE("describe the build in detail as it runs"),

    /** Build with minimal output */
    QUIET("build with minimal output"),

    /** Build with debug trace output */
    DEBUG("build with debug trace output"),

    /** Show help */
    HELP("show help");

    /** Help text for this option */
    private final String help;

    BuildOption(String help)
    {

        this.help = help;
    }

    /**
     * Returns the help for this build option
     */
    public String help()
    {
        return help;
    }
}
