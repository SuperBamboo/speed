package com.shengxuan.speed.config;

import com.shengxuan.speed.entity.Logger;
import com.shengxuan.speed.mapper.LoggerMapper;
import com.shengxuan.speed.mapper.UserMapper;
import com.shengxuan.speed.util.DateFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserMapper userMapper;

    private final LoggerMapper loggerMapper;

    // 定义 Session 属性标识的常量
    //private static final String LOGOUT_MARKER = "LOGOUT_MARKER";

    public SecurityConfig(UserMapper userMapper, LoggerMapper loggerMapper) {
        this.userMapper = userMapper;
        this.loggerMapper = loggerMapper;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /**
         * 基于内存的方式，创建两个用户admin/123456，user/123456
         * */
        auth.inMemoryAuthentication()
                .withUser("supper")//用户名
                .password(passwordEncoder().encode("supper"))//密码
                .roles("SUPPER");//角色
        auth.inMemoryAuthentication()
                .withUser("adminSXDZ")//用户名
                .password(passwordEncoder().encode("adminSXDZ"))//密码
                .roles("ADMIN");//角色
        auth.inMemoryAuthentication()
                .withUser("user")//用户名
                .password(passwordEncoder().encode("user"))//密码
                .roles("USER");//角色

        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //配置静态文件不需要认证
        web.ignoring().antMatchers("/static/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/user/**").hasRole("SUPPER")  /*只有SUPPER的用户才能访问/user/开头的路径*/
                .antMatchers("/server/**").hasRole("SUPPER")  /*只有SUPPER的用户才能访问/server/开头的路径*/
                .antMatchers("/socket/**").hasAnyRole( "ADMIN","SUPPER","USER")    /*有USER或ADMIN的用户可以访问/admin/开头的路径*/
                .antMatchers("/admin/**").hasAnyRole("USER", "ADMIN","SUPPER")    /*有USER或ADMIN的用户可以访问/admin/开头的路径*/
                .antMatchers("/login", "/config/**","/css/**","/images/**","/js/**","/plugins/**","/doLogin","/403").permitAll()  /*允许所有用户访问主页 和 /home路径*/
                .anyRequest().authenticated()   /*表示除了前面的路径其他所有的路径都需要用户登陆后才能访问*/
                .and()
                .formLogin()
                .loginPage("/login")    /*指定重定向到那个页面*/
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/doLogin")
                .defaultSuccessUrl("/index",true)
                .permitAll()    /*表示未登录的用户也可以访问login*/
                .and()
                .sessionManagement()
                .invalidSessionUrl("/login?session=expired")
                .and()
                .csrf().disable()   /*禁用CSRF 防御功能*/
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(new LogoutSuccessHandler() {  // 添加自定义登出处理器
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                        // 获取用户名
                        String username = null;
                        if (authentication != null) {
                            username = authentication.getName();
                        }

                        // 【关键】在 Session 中设置主动登出的标识
                        //request.getSession().setAttribute(LOGOUT_MARKER, true);

                        Logger logger = new Logger();
                        //1.设置用户名
                        logger.setUsername(username);
                        //设置日期
                        String dateNotHaveTime = DateFormat.getDateNotHaveTime();
                        logger.setDate(dateNotHaveTime);
                        //获取详细时间
                        String date = DateFormat.dateNowFormat();
                        logger.setTime(date);
                        //构建描述文字
                        StringBuffer sb = new StringBuffer();
                        sb.append(" 主动退出系统 ");

                        logger.setDesc1(sb.toString());
                        loggerMapper.add(logger);

                        // 重定向到登录页
                        response.sendRedirect("/login");
                    }
                })
                //.logoutSuccessUrl("/login")
                .permitAll()  /*允许所有用户访问注销操作，即使未登录的用户也可以访问注销操作*/
                .and()
                        .exceptionHandling()
                                .accessDeniedHandler(accessDeniedHandler());   //权限不足处理器

        http.headers().frameOptions().sameOrigin();

    }

    public AccessDeniedHandler accessDeniedHandler(){
        return ((request, response, accessDeniedException) -> {
            response.sendRedirect("/403");
        });
    }

    /**
     * Session 监听器，用于记录 Session 超时/销毁
     */
    @Bean
    public HttpSessionListener httpSessionListener() {
        return new HttpSessionListener() {
            @Override
            public void sessionCreated(HttpSessionEvent se) {

            }

            @Override
            public void sessionDestroyed(HttpSessionEvent se) {
                /*// Session 销毁时（包括超时、主动登出、服务器关闭等）
                try {
                    // 尝试从 Session 中获取用户信息
                    String username = null;
                    String sessionId = se.getSession().getId();
                    boolean isLogout = false;

                    // 方法1：从 Session 属性中获取（需要在登录时保存）
                    Object usernameAttr = se.getSession().getAttribute("username");
                    if (usernameAttr != null) {
                        username = usernameAttr.toString();
                    }

                    // 方法2：从 Spring Security 的 SecurityContext 获取
                    if (username == null) {
                        SecurityContext securityContext = (SecurityContext) se.getSession()
                                .getAttribute("SPRING_SECURITY_CONTEXT");
                        if (securityContext != null && securityContext.getAuthentication() != null) {
                            username = securityContext.getAuthentication().getName();
                        }
                    }

                    // 判断是主动登出还是超时
                    // 主动登出时，Session 会被立即销毁，但此时会先触发 LogoutSuccessHandler
                    // 可以通过检查 Session 中是否有某个标记来区分，这里简单记录
                    // 【关键】检查是否是主动登出（有标识符）
                    Object logoutMarker = se.getSession().getAttribute(LOGOUT_MARKER);
                    if (logoutMarker != null && (Boolean) logoutMarker) {
                        isLogout = true;
                    }

                    // 根据标识判断销毁原因
                    if (isLogout) {
                        return;
                    }

                    Logger logger = new Logger();
                    if (username != null) {
                        logger.setUsername(username);

                    } else {
                        logger.setUsername("匿名用户 ");
                    }
                    //设置日期
                    String dateNotHaveTime = DateFormat.getDateNotHaveTime();
                    logger.setDate(dateNotHaveTime);
                    //获取详细时间
                    String date = DateFormat.dateNowFormat();
                    logger.setTime(date);
                    //构建描述文字
                    StringBuffer sb = new StringBuffer();
                    sb.append(" 超时退出系统 ");

                    logger.setDesc1(sb.toString());
                    loggerMapper.add(logger);

                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        };
    }



    @Bean
    protected UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                try {
                    com.shengxuan.speed.entity.User user = userMapper.findByUsername(username);
                    if (user != null) {

                        Logger logger = new Logger();
                        //1.设置用户名
                        logger.setUsername(user.getUsername());
                        //设置日期
                        String dateNotHaveTime = DateFormat.getDateNotHaveTime();
                        logger.setDate(dateNotHaveTime);
                        //获取详细时间
                        String date = DateFormat.dateNowFormat();
                        logger.setTime(date);
                        //构建描述文字
                        StringBuffer sb = new StringBuffer();
                        sb.append(" 登录系统 ");

                        logger.setDesc1(sb.toString());
                        loggerMapper.add(logger);
                        return User
                                .withUsername(user.getUsername())
                                .password(user.getPassword())
                                .roles(user.getRole())
                                .build();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                throw new UsernameNotFoundException("User not found " + username);
            }
        };

    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
