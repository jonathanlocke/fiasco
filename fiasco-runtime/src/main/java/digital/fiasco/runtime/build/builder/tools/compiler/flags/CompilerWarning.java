package digital.fiasco.runtime.build.builder.tools.compiler.flags;

public enum CompilerWarning
{
    ALL("all"),
    NONE("none"),
    UNNECESSARY_CAST("cast"),
    CLASSFILE_CONTENTS("classfile"),
    DEPRECATIONS("deprecation"),
    DIVISION_BY_ZERO("divzero"),
    EMPTY_STATEMENTS_AFTER_IF("empty"),
    SWITCH_FALLTHROUGH("fallthrough"),
    INCOMPLETABLE_FINALLY_CLAUSE("finally"),
    COMMAND_LINE_OPTIONS("options"),
    METHOD_OVERRIDES("overrides"),
    INVALID_PATH_ELEMENTS("path"),
    RAW_TYPES("rawtypes"),
    SERIALIZATION_IDS("serial"),
    STATIC_QUALIFICATIONS("static"),
    TRY_WITH_RESOURCES("try"),
    UNCHECKED_CONVERSIONS("unchecked"),
    UNSAFE_VARARGS("varargs");

    private final String flag;

    CompilerWarning(String flag)
    {
        this.flag = flag;
    }

    public String flag()
    {
        return flag;
    }
}
