package digital.fiasco.runtime.dependency;

import java.util.concurrent.Callable;
import java.util.function.Function;

public abstract class DependencyTask implements Callable<DependencyProcessingResult>, Function<Dependency, DependencyProcessingResult>
{
    private final Dependency dependency;

    public DependencyTask(Dependency dependency)
    {
        this.dependency = dependency;
    }

    @Override
    public DependencyProcessingResult call()
    {
        return apply(dependency);
    }
}
