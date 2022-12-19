package digital.fiasco.runtime.library.data.database;

public interface Database extends
        ApacheCouchDb,
        H2,
        Hibernate,
        Hikari,
        Jooq,
        MyBatis,
        MySql
{
}
