/**
 *    Copyright 2009-2015 the original author or authors.
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

import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.datasource.DataSourceException;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

public class JndiDataSourceFactoryTest extends BaseDataTest {

  private static final String TEST_INITIAL_CONTEXT_FACTORY = MockContextFactory.class.getName();//结果为:org.apache.ibatis.datasource.jndi.JndiDataSourceFactoryTest$MockContextFactory,可以参照内部类去了解为什么是这样
  private static final String TEST_INITIAL_CONTEXT = "/mypath/path/";
  private static final String TEST_DATA_SOURCE = "myDataSource";
  private UnpooledDataSource expectedDataSource;

  @Before
  public void setup() throws Exception {
	//从org/apache/ibatis/databases/blog/blog-derby.properties路径中取出配置文件的相关信息,创建UnpooledDataSource数据源
    expectedDataSource = createUnpooledDataSource(BLOG_PROPERTIES);
  }

  
  /**
   * 函数功能描述:测试JndiDataSourceFactory创建的数据源和预期已经创建好的数据源是否相等
   * @throws Exception
   */
  @Test
  public void shouldRetrieveDataSourceFromJNDI() throws Exception {
	  
    createJndiDataSource();
    
    JndiDataSourceFactory factory = new JndiDataSourceFactory();
    factory.setProperties(new Properties() {
      {
    	//env.java.naming.factory.initial=org.apache.ibatis.datasource.jndi.JndiDataSourceFactoryTest$MockContextFactory
        setProperty(JndiDataSourceFactory.ENV_PREFIX + Context.INITIAL_CONTEXT_FACTORY, TEST_INITIAL_CONTEXT_FACTORY);
        //initial_context=/mypath/path/
        setProperty(JndiDataSourceFactory.INITIAL_CONTEXT, TEST_INITIAL_CONTEXT);
        //data_source=myDataSource
        setProperty(JndiDataSourceFactory.DATA_SOURCE, TEST_DATA_SOURCE);
      }
    });
    DataSource actualDataSource = factory.getDataSource();
    
    assertEquals(expectedDataSource, actualDataSource);
  }

  /**
   * 函数功能描述:为创建jndi数据源设置一些前置条件(环境等)
   * @throws Exception
   */
  private void createJndiDataSource() throws Exception {
    try {
      /*
       * 设置环境参数信息
       */
      Hashtable<String, String> env = new Hashtable<String, String>();
      //Context.INITIAL_CONTEXT_FACTORY结果为:java.naming.factory.initial
      env.put(Context.INITIAL_CONTEXT_FACTORY, TEST_INITIAL_CONTEXT_FACTORY);
      
      /*
       * 创建执行命名操作的初始上下文
       */
      MockContext ctx = new MockContext(false);
      ctx.bind(TEST_DATA_SOURCE, expectedDataSource);

      /*
       * 使用所提供的环境构造一个初始上下文.并在刚刚构造的上下文环境中绑定执行命名操作的初始上下文
       */
      InitialContext initCtx = new InitialContext(env);
      initCtx.bind(TEST_INITIAL_CONTEXT, ctx);
    } catch (NamingException e) {
      throw new DataSourceException("There was an error configuring JndiDataSourceTransactionPool. Cause: " + e, e);
    }
  }

  /**
   * 功能描述:创建初始上下文的工厂(静态内部类)
   * 其中InitialContextFactory此接口表示创建初始上下文的工厂。 关于InitialContextFactory接口介绍可以参照jdk文档.
   * @author guoyangyang
   *
   */
  public static class MockContextFactory implements InitialContextFactory {
	
	/**
	 * 函数功能描述:实现InitialContextFactory接口getInitialContext(Hashtable<?,?> environment) 方法,用来创建一个执行命名操作的初始上下文
	 */
    @Override
    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
      return new MockContext(false);
    }
  }

  /**
   * 功能描述:执行命名操作的初始上下文(静态内部类)
   * 其中InitialContext此类是执行命名操作的初始上下文。 
   * @author guoyangyang
   *
   */
  public static class MockContext extends InitialContext {
	  
    private static Map<String,Object> bindings = new HashMap<String,Object>();//bingings相当于一个临时缓冲变量.

    /**
     * 函数功能描述:调用父类的构造方法创建一个MockContext对象
     * @param lazy
     * @throws NamingException
     */
    public MockContext(boolean lazy) throws NamingException {
      /**
       * 根据java当中构造函数链来说,调用子类MockContext会调用父类InitialContext的构造方法InitialContext(boolean lazy)方法.
       * 而父类的InitialContext(boolean lazy)方法在jdk文档中是下面这样描述的:
       * 构造一个带有不进行初始化选项的初始上下文。如果子类在调用 InitialContext类的构造方法时仍然未知环境属性的值，则可以在其中使用父类的InitialContext(boolean lazy)构造方法.
       * 子类的构造方法将调用父类的InitialContext(boolean lazy)构造方法，计算环境的值，然后在返回之前调用 init(). 
       * 其中init()方法jdk文档介绍为:init(Hashtable<?,?> environment):使用所提供的环境(environment)初始化初始上下文.这些环境属性已在类描述中讨论。 此方法将修改 environment 并保存一个对它的引用。调用者可能不再修改它。 
       * 
       * 其中父类的InitialContext(boolean lazy)方法中的lazy值true/false分别执行如下情形:
       * lazy - 为 true 表示不初始化该初始上下文；为 false 等效于调用 new InitialContext() 
       */
      super(lazy);
    }

    /**
     * 函数功能描述:重载InitialContext类中的lookup(String name)方法
     */
    @Override
    public Object lookup(String name) throws NamingException {
      return bindings.get(name);
    }

    /**
     * 函数功能描述:重在InitialContext类中的bind(String name, Object obj)方法
     */
    @Override
    public void bind(String name, Object obj) throws NamingException {
      bindings.put(name, obj);
    }
  }


}
