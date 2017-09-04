package io.lance.common.core.log4j2;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Author Lance.
 * Date: 2017-08-29 10:23
 * Desc: 扩展log4j2日志输出ip
 */
@Plugin(name = "IPatternConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({"ip"})
public class IPatternConverter extends LogEventPatternConverter {

    private static class IPatternConverterClassesLoader {
        static final IPatternConverter INSTANCE = new IPatternConverter();
    }

    private IPatternConverter() {
        super("ip", "ip");
    }

    public static IPatternConverter newInstance() {
        return IPatternConverterClassesLoader.INSTANCE;
    }


    @Override
    public void format(LogEvent logEvent, StringBuilder builder) {
        builder.append(getHostIp());
    }

    private static String getHostIp() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
