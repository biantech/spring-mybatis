package com.biantech.ssmd.es;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author BIANJIANQUAN223
 * @date 2018/11/13
 */

public class ESClientFactoryBean implements
        FactoryBean<TransportClient>, InitializingBean, DisposableBean {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private TransportClient client;

    private Resource configureResource;

    public void setConfigureResource(Resource configureResource) {
        this.configureResource = configureResource;
    }

    public ESClientFactoryBean() {

    }

    @Override
    public void destroy() throws Exception {
        try {
            if (client != null) {
                client.close();
            }
        } catch (Throwable ex) {
            logger.error(
                    "Close ElasticSearch-Client error:"
                            + ex.getMessage(), ex);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (configureResource == null) {
            throw new FatalBeanException("configure be null.");
        }
        Properties props = null;
        try {
            InputStream input = configureResource.getInputStream();
            Throwable localThrowable2 = null;
            try {
                props = new Properties();
                props.load(input);
                create(props);
            } catch (Throwable localThrowable1) {
                localThrowable2 = localThrowable1;
                throw localThrowable1;
            } finally {

                if (input != null)
                    if (localThrowable2 != null)
                        try {
                            input.close();
                        } catch (Throwable x2) {
                            localThrowable2.addSuppressed(x2);
                        }
                    else {
                        input.close();
                    }
            }
        } catch (Throwable e) {
            throw new FatalBeanException(
                    "Create ElasticSearch-Client error by config:"
                            + props, e);
        }
    }

    @Override
    public TransportClient getObject() throws Exception {
        return client;
    }

    @Override
    public Class<?> getObjectType() {
        return TransportClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * 创建client
     *
     * @param props
     * @return
     * @throws NumberFormatException
     * @throws UnknownHostException
     */
    private TransportClient create(Properties props) throws NumberFormatException, UnknownHostException {
        String clusterName = props.getProperty("clusterName");
        String remotes = props.getProperty("remotes");
        if (StringUtils.isBlank(clusterName)) {
            throw new FatalBeanException(
                    "Create ElasticSearch-Client error by clusterName is null:");
        }
        if (StringUtils.isBlank(remotes)) {
            throw new FatalBeanException(
                    "Create ElasticSearch-Client error by remotes is null:");
        }
        List<String> serverUris = StringUtils.isBlank(remotes) ? new ArrayList() : Arrays.asList(remotes.split(";"));
        if (serverUris.size() == 0) {
            throw new FatalBeanException(
                    "Create ElasticSearch-Client error by remotes is null:");
        }
        Settings esSettings = Settings.builder()
                .put("cluster.name", clusterName) //设置ES实例的名称
                .put("client.transport.sniff", true) //自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
                .put("network.tcp.connect_timeout", 2, TimeUnit.SECONDS)//tcp连接超时
                .put("transport.tcp.connect_timeout", 2, TimeUnit.SECONDS)//tcp连接超时
                .put("search.default_search_timeout", 2, TimeUnit.SECONDS)//search连接超时
                .build();
        client = new PreBuiltTransportClient(esSettings);
        for (String remote : serverUris) {
            String ip = remote.substring(0, remote.indexOf(":"));
            String port = remote.substring(remote.indexOf(":") + 1, remote.length());
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), Integer.valueOf(port)));
        }
        return client;
    }


}

