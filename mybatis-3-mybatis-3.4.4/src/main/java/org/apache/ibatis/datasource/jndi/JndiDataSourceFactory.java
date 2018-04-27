/**
 *    Copyright 2009-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.datasource.jndi;

import java.util.Map.Entry;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.ibatis.datasource.DataSourceException;
import org.apache.ibatis.datasource.DataSourceFactory;

/**
 * @author Clinton Begin
 */
public class JndiDataSourceFactory implements DataSourceFactory {
 
  //服务器的JNDI目录，不同的服务器该值不同，因此需要在mybatis-config的配置文件中传入该值
  public static final String INITIAL_CONTEXT = "initial_context";
  
  //data_source：对应着META-INF/context.xml中注册的服务名称（name属性值），即键值对中的键值
  public static final String DATA_SOURCE = "data_source";
  
  //判断环境变量前缀
  public static final String ENV_PREFIX = "env.";

  //数据源对象
  private DataSource dataSource;

  /**
   * 函数功能描述:读取配置信息,并从中获取得到相关的信息用来初始化航下文和从上下文中获取得到数据源对象
   * 即该方法在初始化Mybatis的时候被调用，会将mybatis-config.xml中配置的属性注入进来
   * 要注入的是initial_context和data_source的值
   */
  @Override
  public void setProperties(Properties properties) {
    try {
      // 第一步:InitialContext此类是执行命名操作的初始上下文,即声明JAVA命名和目录接口的上下文类
      InitialContext initCtx;
      
      // properties在SqlSessionFactoryBuilder创建SqlSessionFactory的过程中收集mybatis-config.xml配置文件中的<dataSource>标签下属性建立
      // env不为null的条件是在mybatis-config.xml中配置的JNDI属性以"env."开头
      Properties env = getEnvProperties(properties);// 获取配置文件中key以 env. 开头的配置信息
      if (env == null) {
    	// 进入到这个流程，默认使用SqlSessionFactoryBuilder流程中的properties
        initCtx = new InitialContext();
      } else {
    	// 如果配置文件中配置的JNDI属性以"env."开头，则进入这个步骤
        initCtx = new InitialContext(env);
      }

      /**
       * mybatis-config.xml中有两种方式可以进行JNDI数据源的配置
       * 1. 第一种方式需要配置initial_context和data_source的值，本例中
       *      initial_context="java:/comp/env"
       *      data_source="jdbc/mybatis-jndi"
       * 2. 第二种方式只需要配置data_source的值
       *      data_source="java:/comp/env/jdbc/mybatis-jndi"
       * 结论：其实是一样的，请结合context.xml配置文件内容查看此处
       */
      if (properties.containsKey(INITIAL_CONTEXT) && properties.containsKey(DATA_SOURCE)) {
    	//第一种方式
        Context ctx = (Context) initCtx.lookup(properties.getProperty(INITIAL_CONTEXT));
        dataSource = (DataSource) ctx.lookup(properties.getProperty(DATA_SOURCE));
      } else if (properties.containsKey(DATA_SOURCE)) {
    	//第二种方式
        dataSource = (DataSource) initCtx.lookup(properties.getProperty(DATA_SOURCE));
      }

    } catch (NamingException e) {
      throw new DataSourceException("There was an error configuring JndiDataSourceTransactionPool. Cause: " + e, e);
    }
  }

  /**
   * 函数功能描述:获取一个数据源对象
   * 因为数据源交由服务器托管，因此mybatis不需要再像pooled类型那样自己实现连接池并通过动态代理增强java.sql.Connection
   */
  @Override
  public DataSource getDataSource() {
    return dataSource;
  }

  /**
   * 函数功能描述:获取配置文件当中以 env. 开头的配置信息
   * @param allProps
   * @return
   */
  private static Properties getEnvProperties(Properties allProps) {
    final String PREFIX = ENV_PREFIX;
    Properties contextProperties = null;
    for (Entry<Object, Object> entry : allProps.entrySet()) {
      String key = (String) entry.getKey();
      String value = (String) entry.getValue();
      //首先判断每次循环得到的key是够以 env. 为开头,如果是,则将此信息放在contextProperties中.
      if (key.startsWith(PREFIX)) {
        if (contextProperties == null) {
          contextProperties = new Properties();
        }
        contextProperties.put(key.substring(PREFIX.length()), value);
      }
    }
    return contextProperties;
  }
  
}
