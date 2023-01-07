package digital.fiasco.runtime.build.metadata;

/**
 * Roles that can be assumed by {@link Contributor}s to a project. Contributors can have multiple roles on a project.
 *
 * @author Jonathan Locke
 */
public enum ProjectRole
{
    ORIGINATOR,
    ARCHITECT,
    LEAD_DEVELOPER,
    DEVELOPER,
    TECHNICAL_WRITER,
    TESTER,
    DESIGNER,
    EVANGELIST
}
