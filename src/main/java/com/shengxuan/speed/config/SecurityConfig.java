package com.shengxuan.speed.config;

import com.shengxuan.speed.entity.Role;
import com.shengxuan.speed.mapper.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.nio.file.AccessDeniedException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserMapper userMapper;

    public SecurityConfig(UserMapper userMapper) {
        this.userMapper = userMapper;
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
                .antMatchers("/socket/**").hasAnyRole( "ADMIN","SUPPER")    /*有USER或ADMIN的用户可以访问/admin/开头的路径*/
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
                .logoutSuccessUrl("/login")
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

    @Bean
    protected UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                try {
                    com.shengxuan.speed.entity.User user = userMapper.findByUsername(username);
                    if (user != null) {
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
