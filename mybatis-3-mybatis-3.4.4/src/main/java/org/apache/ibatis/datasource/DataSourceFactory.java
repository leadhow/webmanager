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
package org.apache.ibatis.datasource;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.parsing.XNode;

/**
 * @author Clinton Begin
 */
public interface DataSourceFactory {

  /**
	* 数据源工厂接口中定义了两个方法，一个是设置属性的方法，与事务模型中的设置属性方法类似，也是需要在构建Configuration配置类时由XMLConfigBuilder来进行调用的，
	* 调用的目的是为了将配置文件中配置的数据源属性信息填充到DataSource中（然后在填充到Environment中，在填充到Configuration中，这是后话）。
	*/
	
	
  /**
   * 函数功能描述:读取形参当中的配置信息,并从中获取得到相关的信息用来初始化下文和从上下文中获取得到数据源等其他信息
   * @param props 配置文件信息被放在了Properties当中
   */
  void setProperties(Properties props);//此方法会在XMLConfigBuilder类中的private DataSourceFactory dataSourceElement(XNode context) throws Exception{}中被调用

  /**
   * 函数功能描述:获取dataSource数据源对象
   * @return
   */
  DataSource getDataSource();

}
