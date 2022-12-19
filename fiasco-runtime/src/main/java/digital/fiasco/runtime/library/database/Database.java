package digital.fiasco.runtime.library.database;

public interface Database extends
        H2,
        Hibernate,
        Hikari,
        Jooq,
        MySql
{
}
