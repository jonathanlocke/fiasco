package digital.fiasco.runtime.build.builder.tools.documenter.flags;

public enum JavadocWarning
{
    ALL("all"),
    NONE("none"),
    ACCESSIBILITY("accessibility"),
    INVALID_SYNTAX("syntax"),
    BROKEN_REFERENCE("reference"),
    INVALID_HTML("html"),
    MISSING("missing");

    private final String flag;

    JavadocWarning(String flag)
    {
        this.flag = flag;
    }

    public String flag()
    {
        return flag;
    }
}
