package digital.fiasco.libraries.utilities.networking;

import digital.fiasco.runtime.dependency.artifact.ArtifactGroup;
import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.ArtifactGroup.group;

@SuppressWarnings("unused")
public interface Netty
{
    ArtifactGroup nettyGroup = group("io.netty");

    Library netty                              = nettyGroup.library("netty");
    Library netty_all                          = nettyGroup.library("netty-all");
    Library netty_buffer                       = nettyGroup.library("netty-buffer");
    Library netty_codec                        = nettyGroup.library("netty-codec");
    Library netty_codec_http                   = nettyGroup.library("netty-codec-http");
    Library netty_codec_http2                  = nettyGroup.library("netty-codec-http2");
    Library netty_codec_socks                  = nettyGroup.library("netty-codec-socks");
    Library netty_codec_transport_native_epoll = nettyGroup.library("netty-transport-native-epoll");
    Library netty_common                       = nettyGroup.library("netty-common");
    Library netty_handler                      = nettyGroup.library("netty-handler");
    Library netty_handler_proxy                = nettyGroup.library("netty-handler-proxy");
    Library netty_native_kqueue                = nettyGroup.library("netty-native-kqueue");
    Library netty_resolver                     = nettyGroup.library("netty-resolver");
    Library netty_resolver_dns                 = nettyGroup.library("netty-resolver-dns");
    Library netty_transport                    = nettyGroup.library("netty-transport");
    Library netty_transport_native_unix_common = nettyGroup.library("netty-transport-native-unix-common");
}
