package digital.fiasco.runtime.build.builder.tools.compile.java.flags;

public enum DebugInformation
{
    NONE("none"),
    SOURCE_FILES("source"),
    LINES("lines"),
    VARIABLES("vars"),
    ALL("all");

    private final String flag;

    DebugInformation(String flag)
    {
        this.flag = flag;
    }

    public String flag()
    {
        return flag;
    }
}
