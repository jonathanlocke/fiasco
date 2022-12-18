package digital.fiasco.runtime.dependency.oss.logging;

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
