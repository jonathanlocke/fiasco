package digital.fiasco.libraries.data.formats;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface ApachePoi
{
    Library apache_poi = library("org.apache.poi:poi");
    Library apache_poi_microsoft_excel = library("org.apache.poi:poi-excelant");
    Library apache_poi_microsoft_scratchpad = library("org.apache.poi:poi-scratchpad");
    Library apache_poi_microsoft_xml = library("org.apache.poi:poi-ooxml");
    Library apache_poi_microsoft_xml_lite = library("org.apache.poi:poi-ooxml-lite");
    Library apache_poi_microsoft_xml_schemas = library("org.apache.poi:poi-ooxml-schemas");
}
