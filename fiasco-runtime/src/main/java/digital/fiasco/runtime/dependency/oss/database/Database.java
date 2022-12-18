package digital.fiasco.runtime.dependency.oss.database;

public interface Database extends
        H2,
        Hibernate,
        Hikari,
        Jooq,
        MySql
{
}
