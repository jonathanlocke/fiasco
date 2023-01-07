package digital.fiasco.libraries.data.formats;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface ApachePoi extends LibraryGroups
{
    Library apache_poi                       = apache_poi_group.library("poi");
    Library apache_poi_microsoft_excel       = apache_poi_group.library("poi-excelant");
    Library apache_poi_microsoft_scratchpad  = apache_poi_group.library("poi-scratchpad");
    Library apache_poi_microsoft_xml         = apache_poi_group.library("poi-ooxml");
    Library apache_poi_microsoft_xml_lite    = apache_poi_group.library("poi-ooxml-lite");
    Library apache_poi_microsoft_xml_schemas = apache_poi_group.library("poi-ooxml-schemas");
}
