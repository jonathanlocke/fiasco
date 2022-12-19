package digital.fiasco.runtime.library.languages.java.networking;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface Netty
{
    Library netty = library("io.netty:netty");
    Library netty_all = library("io.netty:netty-all");
    Library netty_buffer = library("io.netty:netty-buffer");
    Library netty_codec = library("io.netty:netty-codec");
    Library netty_codec_http = library("io.netty:netty-codec-http");
    Library netty_codec_http2 = library("io.netty:netty-codec-http2");
    Library netty_codec_socks = library("io.netty:netty-codec-socks");
    Library netty_codec_transport_native_epoll = library("io.netty:netty-transport-native-epoll");
    Library netty_common = library("io.netty:netty-common");
    Library netty_handler = library("io.netty:netty-handler");
    Library netty_handler_proxy = library("io.netty:netty-handler-proxy");
    Library netty_native_kqueue = library("io.netty:netty-native-kqueue");
    Library netty_resolver = library("io.netty:netty-resolver");
    Library netty_resolver_dns = library("io.netty:netty-resolver-dns");
    Library netty_transport = library("io.netty:netty-transport");
    Library netty_transport_native_unix_common = library("io.netty:netty-transport-native-unix-common");
}
