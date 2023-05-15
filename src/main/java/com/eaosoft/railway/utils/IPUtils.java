package com.eaosoft.railway.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * IP address
 *
 * @author ZhouWenTao
 */
@Slf4j
public class IPUtils {
	private static Logger logger = LoggerFactory.getLogger(IPUtils.class);

	/**
	 *  IP address
	 * 
	 * With reverse proxy software such as Nginx, the IP address cannot be passed through request.getRemoteAddr().
	 * If a multi-level reverse proxy is used, the value of X-Forwarded-For is not just one, but a string of IP addresses,
     * and the first valid IP string in X-Forwarded-For that is not unknown is the real IP address
	 */
	public static String getIpAddr(HttpServletRequest request) {
	    String ip = null;

        try {
            ip = request.getHeader("X-Real-IP");
            if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("x-forwarded-for");
            }
            if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isBlank(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
        	logger.error("ipIPUtils ERROR ", e);
        }
        
        String[] split = ip.split(",");
        StringBuffer sb=new StringBuffer();
        if (split.length>0) {
            for (String s : Arrays.asList(split)) {
                boolean contains = s.contains("192.168.");
                if(contains) continue;
                ip = s;
            }
        }
        return ip;
    }
	
}
