/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.sun.management.OperatingSystemMXBean;
import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.netty.NettyHandler;
import systems.reformcloud.netty.channel.ChannelHandler;
import systems.reformcloud.netty.channel.ChannelReader;
import systems.reformcloud.netty.handler.Decoder;
import systems.reformcloud.netty.handler.Encoder;
import systems.reformcloud.utility.StringUtil;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import systems.reformcloud.utility.checkable.Checkable;

import java.lang.management.ManagementFactory;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

public final class ReformCloudLibraryService {
    static {
        Thread.currentThread().setName("ReformCloud-Main");
    }

    public static final Gson GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().disableHtmlEscaping().create();
    public static final JsonParser PARSER = new JsonParser();

    public static final boolean EPOLL = Epoll.isAvailable();

    public static final ThreadLocalRandom THREAD_LOCAL_RANDOM = ThreadLocalRandom.current();

    public static <K, V> ConcurrentHashMap<K, V> concurrentHashMap() {
        return new ConcurrentHashMap<>(0);
    }

    public static void sendHeader() {
        System.out.println(" ");
        System.out.println(
                        "         ______ _______ _______  _____   ______ _______ _______         _____  _     _ ______ \n" +
                        "        |_____/ |______ |______ |     | |_____/ |  |  | |       |      |     | |     | |     \\\n" +
                        "        |    \\_ |______ |       |_____| |    \\_ |  |  | |_____  |_____ |_____| |_____| |_____/\n" +
                        "                                                                                              \n" +
                        "                                     The official CloudSystem                               \n" +
                        "      __________________________________________________________________________________________ \n\n" +
                        "                            Support Discord: https://discord.gg/fwe2CHD      \n"
        );
    }

    public static void sendHeader(final LoggerProvider loggerProvider) {
        System.out.println(" ");
        loggerProvider.coloured(
                        "§3" +
                        "         ______ _______ _______  _____   ______ _______ _______         _____  _     _ ______ \n" +
                        "        |_____/ |______ |______ |     | |_____/ |  |  | |       |      |     | |     | |     \\\n" +
                        "        |    \\_ |______ |       |_____| |    \\_ |  |  | |_____  |_____ |_____| |_____| |_____/\n" +
                        "                                                                                              \n" +
                        "                                     §rThe official CloudSystem                               \n" +
                        "      __________________________________________________________________________________________ \n\n" +
                        "                            Support Discord: https://discord.gg/fwe2CHD      \n"
        );
    }

    public static String newKey() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            UUID uuid = UUID.randomUUID();
            stringBuilder.append(uuid.toString().replace("-", ""));
        }

        return stringBuilder.substring(0);
    }

    /**
     * Prepares the given Channel with all utilities
     *
     * @param channel               The given channel where all Handlers should be added
     * @param channelHandler        The pre-initialized ChannelHandler where all channels are
     *                              registered and handled
     * @see Channel#pipeline()
     * @see LengthFieldBasedFrameDecoder
     * @see LengthFieldPrepender
     */
    public static Channel prepareChannel(Channel channel, ChannelHandler channelHandler) {
        channel.pipeline().addLast(
                new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4),
                new LengthFieldPrepender(4),
                new Encoder(),
                new Decoder(),
                new ChannelReader(channelHandler));

        return channel;
    }

    /**
     * New EventLoopGroup
     *
     * @return new EpollEventLoopGroup if {@see Epoll} is available or a new NioEventLoopGroup
     * @see EpollEventLoopGroup
     * @see NioEventLoopGroup
     */
    public static EventLoopGroup eventLoopGroup() {
        return EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();
    }

    /**
     * New EventLoopGroup
     *
     * @return new EpollEventLoopGroup with the given threads if {@see Epoll} is available or a new NioEventLoopGroup with the given threads
     * @see EpollEventLoopGroup
     * @see NioEventLoopGroup
     */
    public static EventLoopGroup eventLoopGroup(int threads) {
        return EPOLL ? new EpollEventLoopGroup(threads) : new NioEventLoopGroup(threads);
    }

    /**
     * New ServerSocketChannel
     *
     * @return EpollServerSocketChannel-Class if {@see Epoll} is available or a new NioServerSocketChannel-Class
     * @see ServerSocketChannel
     */
    public static Class<? extends ServerSocketChannel> serverSocketChanel() {
        return EPOLL ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
    }

    /**
     * New SocketChannel
     *
     * @return EpollSocketChannel-Class if {@see Epoll} is available or a new NioSocketChannel-Class
     * @see SocketChannel
     */
    public static Class<? extends SocketChannel> clientSocketChannel() {
        return EPOLL ? EpollSocketChannel.class : NioSocketChannel.class;
    }

    /**
     * Let the main-thread sleep the given time
     *
     * @param time
     */
    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (final InterruptedException ignored) {
        }
    }

    /**
     * Let the given thread sleep the given time
     *
     * @param thread
     * @param time
     */
    public static void sleep(Thread thread, long time) {
        try {
            Thread.sleep(time);
        } catch (final InterruptedException ignored) {
        }
    }

    /**
     * Check if the given String is a {@link Integer}
     *
     * @param key
     * @return {@code true} if the {@link String} is a {@link Integer}
     * or {@code false} if the {@link String} is not a {@link Integer}
     * @see Integer#parseInt(String)
     */
    public static boolean checkIsInteger(String key) {
        try {
            Integer.parseInt(key);
            return true;
        } catch (final Throwable ignored) {
            return false;
        }
    }

    /**
     * Returns the cpu usage of the system
     *
     * @return the cpu usage of the system
     */
    public static double cpuUsage() {
        return ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getSystemCpuLoad() * 100;
    }

    /**
     * Returns the cpu usage of the internal jar
     *
     * @return the cpu usage of the internal jar
     */
    public static double internalCpuUsage() {
        return ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getProcessCpuLoad() * 100;
    }

    public static long usedMemorySystem() {
        return maxMemorySystem() - ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getFreePhysicalMemorySize();
    }

    public static long maxMemorySystem() {
        return ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
    }

    public static long bytesToMB(final long b) {
        return b / 1024 / 1024;
    }

    public static boolean check(Checkable<Object> checkable, final Object toCheck) {
        return toCheck != null && checkable != null && checkable.isChecked(toCheck);
    }
}
