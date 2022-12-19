package digital.fiasco.libraries.logging;

@SuppressWarnings({ "unused" })
public interface Logging extends
        ApacheCommonsLogging,
        ApacheLog4j,
        CommonsLogging,
        Log4j,
        Logback,
        Slf4j
{
}
