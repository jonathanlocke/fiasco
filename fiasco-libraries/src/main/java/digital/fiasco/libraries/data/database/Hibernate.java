package digital.fiasco.libraries.data.database;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface Hibernate extends LibraryGroups
{
    Library hibernate                     = hibernate_group.library("hibernate");
    Library hibernate_annotations         = hibernate_group.library("hibernate-annotations");
    Library hibernate_c3p0                = hibernate_group.library("hibernate-c3p0");
    Library hibernate_common              = hibernate_group.library("hibernate-common");
    Library hibernate_commons_annotations = hibernate_group.library("hibernate-commons-annotations");
    Library hibernate_core                = hibernate_group.library("hibernate-core");
    Library hibernate_eh_cache            = hibernate_group.library("hibernate-ehcache");
    Library hibernate_ejb3_persistence    = hibernate_group.library("hibernate-ejb3-persistence");
    Library hibernate_entity_manager      = hibernate_group.library("hibernate-entity-manager");
    Library hibernate_envers              = hibernate_group.library("hibernate-envers");
    Library hibernate_hikari              = hibernate_group.library("hibernate-hikaricp");
    Library hibernate_javax               = hibernate_group.library("hibernate-javax");
    Library hibernate_jpa_model_gen       = hibernate_group.library("hibernate-jpamodelgen");
    Library hibernate_orm                 = hibernate_group.library("hibernate-orm");
    Library hibernate_search              = hibernate_group.library("hibernate-search");
    Library hibernate_search_orm          = hibernate_group.library("hibernate-search-orm");
    Library hibernate_spatial             = hibernate_group.library("hibernate-spatial");
    Library hibernate_validator           = hibernate_group.library("hibernate-validator");
}
