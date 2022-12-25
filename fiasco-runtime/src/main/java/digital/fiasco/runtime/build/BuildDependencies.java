package digital.fiasco.runtime.build;

import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.dependency.artifact.Artifact;

@SuppressWarnings("UnusedReturnValue")
public interface BuildDependencies
{
    DependencyList dependencies();

    Build pinVersion(Artifact<?> artifact, String version);

    Build requires(Artifact<?> first, Artifact<?>... rest);

    Build requires(DependencyList dependencies);

    Build withDependencies(Artifact<?>... dependencies);

    Build withDependencies(DependencyList dependencies);
}
