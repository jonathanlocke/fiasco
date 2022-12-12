package digital.fiasco.runtime.dependency;

import digital.fiasco.runtime.repository.Library;

/**
 * @author jonathan
 */
@SuppressWarnings("unused")
public interface DependencyResolver
{
    DependencyList<Library> dependencies(Library library);
}
