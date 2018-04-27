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
package org.apache.ibatis.session;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;

/**
 * Builds {@link SqlSession} instances.
 *
 * @author Clinton Begin
 */
public class SqlSessionFactoryBuilder {

  /**
   * 函数功能描述:根据默认的environment创建SqlSessionFactory	
   * @param reader
   * @return
   */
  public SqlSessionFactory build(Reader reader) {
    return build(reader, null, null);
  }

  /**
   * 函数功能描述:指定environment进行创建SqlSessionFactory
   * @param reader
   * @param environment
   * @return
   */
  public SqlSessionFactory build(Reader reader, String environment) {
    return build(reader, environment, null);
  }

  /**
   * 函数功能描述:传入的Properties内容将会被覆盖<Properties>标签的内容
   * @param reader
   * @param properties
   * @return
   */
  public SqlSessionFactory build(Reader reader, Properties properties) {
    return build(reader, null, properties);
  }

  /**
   * 函数功能描述:被上述三个方法调用,调用真正去解析mybatis的配置文件的方法
   * 即解析mybatis-config.xml文件,得到Configuration对象
   * @param reader
   * @param environment
   * @param properties
   * @return
   */
  public SqlSessionFactory build(Reader reader, String environment, Properties properties) {
    try {
      XMLConfigBuilder parser = new XMLConfigBuilder(reader, environment, properties);
      return build(parser.parse());
    } catch (Exception e) {
      throw ExceptionFactory.wrapException("Error building SqlSession.", e);
    } finally {
      ErrorContext.instance().reset();
      try {
        reader.close();
      } catch (IOException e) {
        // Intentionally ignore. Prefer previous error.
      }
    }
  }

  /**
   * 函数功能描述:根据默认的environment创建SqlSessionFactory
   * @param inputStream
   * @return
   */
  public SqlSessionFactory build(InputStream inputStream) {
    return build(inputStream, null, null);
  }

  /**
   * 函数功能描述:指定environment进行创建SqlSessionFactory
   * @param inputStream
   * @param environment
   * @return
   */
  public SqlSessionFactory build(InputStream inputStream, String environment) {
    return build(inputStream, environment, null);
  }

  /**
   * 函数功能描述:传入的Properties将会覆盖<properties>标签中同名的value
   * @param inputStream
   * @param properties
   * @return
   */
  public SqlSessionFactory build(InputStream inputStream, Properties properties) {
    return build(inputStream, null, properties);
  }

  /**
   * 函数功能描述:被上述三个方法调用,调用真正去解析mybatis的配置文件的方法
   * 即解析mybatis-config.xml文件,得到Configuration对象
   * @param inputStream
   * @param environment
   * @param properties
   * @return
   */
  public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
    try {
      // mybatis-config.xml文件的解析对象  
      // 在XMLConfigBuilder中封装了Configuration对象  
      // 此时还未真正发生解析，但是将解析的必备条件都准备好了  
      XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);//非常重要的一句代码
      // parser.parse()的调用标志着解析的开始  
      // mybatis-config.xml中的配置将会被解析成运行时对象封装到Configuration中  
      return build(parser.parse());
    } catch (Exception e) {
      throw ExceptionFactory.wrapException("Error building SqlSession.", e);
    } finally {
      ErrorContext.instance().reset();
      try {
        inputStream.close();
      } catch (IOException e) {
        // Intentionally ignore. Prefer previous error.
      }
    }
  }
    
  /**
   * 函数功能描述:依据Configuration内容生成SqlSessionFactory
   * @param config
   * @return
   */
  public SqlSessionFactory build(Configuration config) {
    return new DefaultSqlSessionFactory(config);
  }

}
