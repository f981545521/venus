package cn.acyou.base.config;


import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author youfang
 * @date 2017-12-07 17:37
 **/
@Configuration
@EnableTransactionManagement
public class MyBatisConfigurer implements TransactionManagementConfigurer {

    @Autowired
    private DataSource dataSource;//注入数据源

    @Bean(name = "sqlSessionFactory")
    protected SqlSessionFactoryBean getSqlSessionFactoryBean(){
        SqlSessionFactoryBean sqlsession = new SqlSessionFactoryBean();
        sqlsession.setDataSource(dataSource);
        // typeAliasesPackage：它一般对应我们的实体类所在的包，这个时候会自动取对应包中不包括包名的简单类名作为包括包名的别名。多个package之间可以用逗号或者分号等来进行分隔。(value的值一定要是包的全名)
        sqlsession.setTypeAliasesPackage("cn.acyou.iblogdata.entity");//扫描entity包 使用别名
        org.apache.ibatis.session.Configuration configuration=new org.apache.ibatis.session.Configuration();
        configuration.setUseGeneratedKeys(true);//使用jdbc的getGeneratedKeys获取数据库自增主键值
        configuration.setUseColumnLabel(true);//使用列别名替换列名 select user as User
        configuration.setMapUnderscoreToCamelCase(true);//-自动使用驼峰命名属性映射字段   userId    user_id
        sqlsession.setConfiguration(configuration);
        sqlsession.setFailFast(true);

        //配置分页插件，详情请查阅官方文档
        PageHelper pageHelper = new CustomPageHelper();
        Properties properties = new Properties();
        properties.setProperty("pageSizeZero", "true");
        //分页尺寸为0时查询所有纪录不再执行分页
        properties.setProperty("reasonable", "true");
        //页码<=0 查询第一页，页码>=总页数查询最后一页
        properties.setProperty("supportMethodsArguments", "false");
        //支持通过 Mapper 接口参数来传递分页参数
        pageHelper.setProperties(properties);
        //添加插件
        sqlsession.setPlugins(new Interceptor[]{ pageHelper});

        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            // mapperLocations：它表示我们的Mapper文件存放的位置，当我们的Mapper文件跟对应的Mapper接口处于同一位置的时候可以不用指定该属性的值。
            sqlsession.setMapperLocations(resolver.getResources("classpath*:mapper/*.xml"));
            return sqlsession;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


    @Bean
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }



    //@Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = getMapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("cn.acyou.base.mapper");
        return mapperScannerConfigurer;
    }



    private MapperScannerConfigurer getMapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        //配置通用Mapper，详情请查阅官方文档
        Properties properties = new Properties();
        properties.setProperty("mappers", "tk.mybatis.mapper.common.Mapper");
        properties.setProperty("notEmpty", "false");
        //insert、update是否判断字符串类型!='' 即 test="str != null"表达式内是否追加 and str != ''
        properties.setProperty("IDENTITY", "MYSQL");
        mapperScannerConfigurer.setProperties(properties);
        return mapperScannerConfigurer;
    }
}
