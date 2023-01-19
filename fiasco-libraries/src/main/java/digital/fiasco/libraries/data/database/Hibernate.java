package digital.fiasco.libraries.data.database;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface Hibernate extends LibraryGroups
{
    Library hibernate                     = hibernate_group.library("hibernate").asLibrary();
    Library hibernate_annotations         = hibernate_group.library("hibernate-annotations").asLibrary();
    Library hibernate_c3p0                = hibernate_group.library("hibernate-c3p0").asLibrary();
    Library hibernate_common              = hibernate_group.library("hibernate-common").asLibrary();
    Library hibernate_commons_annotations = hibernate_group.library("hibernate-commons-annotations").asLibrary();
    Library hibernate_core                = hibernate_group.library("hibernate-core").asLibrary();
    Library hibernate_eh_cache            = hibernate_group.library("hibernate-ehcache").asLibrary();
    Library hibernate_ejb3_persistence    = hibernate_group.library("hibernate-ejb3-persistence").asLibrary();
    Library hibernate_entity_manager      = hibernate_group.library("hibernate-entity-manager").asLibrary();
    Library hibernate_envers              = hibernate_group.library("hibernate-envers").asLibrary();
    Library hibernate_hikari              = hibernate_group.library("hibernate-hikaricp").asLibrary();
    Library hibernate_javax               = hibernate_group.library("hibernate-javax").asLibrary();
    Library hibernate_jpa_model_gen       = hibernate_group.library("hibernate-jpamodelgen").asLibrary();
    Library hibernate_orm                 = hibernate_group.library("hibernate-orm").asLibrary();
    Library hibernate_search              = hibernate_group.library("hibernate-search").asLibrary();
    Library hibernate_search_orm          = hibernate_group.library("hibernate-search-orm").asLibrary();
    Library hibernate_spatial             = hibernate_group.library("hibernate-spatial").asLibrary();
    Library hibernate_validator           = hibernate_group.library("hibernate-validator").asLibrary();
}
