package digital.fiasco.runtime.dependency.oss;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public interface Aws
{
    Library aws_jmespath = library("com.amazonaws:jmespath-java");
    Library aws_sdk_core = library("com.amazonaws:aws-java-sdk-core");
    Library aws_sdk_test_utils = library("com.amazonaws:aws-java-sdk-test-utils");
    Library software_aws_sdk_annotations = library("software.amazon.awssdk:annotations");
    Library software_aws_sdk_apache_client = library("software.amazon.awssdk:apache-client");
    Library software_aws_sdk_auth = library("software.amazon.awssdk:auth");
    Library software_aws_sdk_core = library("software.amazon.awssdk:core");
    Library software_aws_sdk_http_client_spi = library("software.amazon.awssdk:http-client-spi");
    Library software_aws_sdk_json_protocol = library("software.amazon.awssdk:aws-json-protocol");
    Library software_aws_sdk_netty_nio_client = library("software.amazon.awssdk:netty-nio-client");
    Library software_aws_sdk_protocol_core = library("software.amazon.awssdk:protocol-core");
    Library software_aws_sdk_regions = library("software.amazon.awssdk:regions");
    Library software_aws_sdk_service_test_utils = library("software.amazon.awssdk:service-test-utils");
    Library software_aws_sdk_utils = library("software.amazon.awssdk:utils");
}
