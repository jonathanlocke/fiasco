//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package fiasco.plugins.librarian;

import com.telenav.kivakit.interfaces.comparison.Filter;
import fiasco.Library;
import fiasco.Module;
import fiasco.Repository;
import fiasco.plugins.Plugin;
import fiasco.repository.LibraryResolver;
import fiasco.repository.RemoteMavenRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * Copies selected files from one folder to another.
 *
 * @author shibo
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class Librarian extends Plugin implements LibraryResolver
{
    private final List<Repository> repositories = new ArrayList<>();

    private Repository deploymentRepository;

    public Librarian(Module module)
    {
        super(module);
    }

    public Librarian deploy(Library library)
    {
        deploymentRepository.install(library);
        return this;
    }

    public Librarian install(Library library)
    {
        RemoteMavenRepository.local().install(library);
        return this;
    }

    public Librarian lookIn(Repository repository)
    {
        repositories.add(repository);
        return this;
    }

    public List<Repository> repositories()
    {
        return Collections.unmodifiableList(repositories);
    }

    @Override
    public List<Library> resolve(Library library, Filter<Library> exclusions)
    {
        for (var repository : repositories)
        {
            var libraries = repository.dependencies(library);
            if (libraries != null)
            {
                return libraries;
            }
        }
        return Collections.emptyList();
    }

    public Librarian withDeploymentRepository(Repository deploymentRepository)
    {
        this.deploymentRepository = deploymentRepository;
        return this;
    }

    @Override
    protected void onRun()
    {
        unsupported();
    }
}
