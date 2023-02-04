package digital.fiasco.runtime.dependency.collections;

import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.dependency.Dependency;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.types.Asset;
import digital.fiasco.runtime.dependency.artifact.types.Library;

/**
 * Dependency visitor
 */
public interface DependencyTreeVisitor
{
    default void onArtifact(Artifact<?> artifact)
    {
    }

    default void onAsset(Asset asset)
    {
    }

    default void onBuilder(Builder builder)
    {
    }

    default void onDependency(Dependency dependency)
    {
    }

    default void onLibrary(Library library)
    {
    }
}
