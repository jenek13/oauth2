package com.ten.security;
import com.ten.config.AuthProvider;
import com.ten.config.CustomUserInfoTokenServices;
import com.ten.repositories.UserRepo;
import com.ten.service.RoleService;
import com.ten.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import com.ten.security.handlers.CustomAuthenticationFailureHandler;
import com.ten.security.handlers.CustomAuthenticationSuccessHandler;
import com.ten.security.service.UserDetailsServiceImpl;

import javax.servlet.Filter;

//@EnableOAuth2Sso
@Configuration
@ComponentScan(basePackages = "com.ten")
@EnableWebSecurity
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthProvider authProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Qualifier("oauth2ClientContext")
    @Autowired
    private OAuth2ClientContext oauth2ClientContext;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    @ConfigurationProperties("google.client")
    public AuthorizationCodeResourceDetails google()
    {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("google.resource")
    public ResourceServerProperties googleResource()
    {
        return new ResourceServerProperties();
    }

    @Bean
    public FilterRegistrationBean oAuth2ClientFilterRegistration(OAuth2ClientContextFilter oAuth2ClientContextFilter)
    {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(oAuth2ClientContextFilter);
        registration.setOrder(-100);
        return registration;
    }

    private Filter ssoFilter()
    {
        OAuth2ClientAuthenticationProcessingFilter googleFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/google");
        OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(google(), oauth2ClientContext);
        googleFilter.setRestTemplate(googleTemplate);
        CustomUserInfoTokenServices tokenServices = new CustomUserInfoTokenServices(googleResource().getUserInfoUri(), google().getClientId(), userService, roleService);
        tokenServices.setRestTemplate(googleTemplate);
        googleFilter.setTokenServices(tokenServices);
        tokenServices.setUserRepo(userRepo);
        tokenServices.setPasswordEncoder(passwordEncoder);
        return googleFilter;
    }

    private UserDetailsServiceImpl authenticationService;//чекает юзера в бд по его логину есть или нет
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;//положиельный хендлер для залогиненных
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;//для не залогиненных

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl authenticationService,
                          CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
                          CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
        this.authenticationService = authenticationService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
    }

    @Override
    protected void configure(HttpSecurity http) {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);//Set whether the configured encoding of this filter is supposed to override existing request and response encodings.
        try {
            http.csrf().disable().addFilterBefore(filter, CsrfFilter.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            http.authorizeRequests()
                    .antMatchers("/user/**")//указываю для какого юрла хочу настроить доступ
                    .permitAll()
                    //.hasAnyAuthority("ROLE_USER")//юзер с ролью юзер может войти на юрл выше
                    .antMatchers("/admin/**")
                    //.permitAll()
                    .hasAnyAuthority("ROLE_ADMIN")
                    .and()
                    //.oauth2Login()
                    .formLogin()//регистрируем страницу с формой логина
                    .loginPage("/login")//имя джспи страницы куда направить по дефолту при запуске
                    .loginProcessingUrl("/loginpr")//сабмитит наши креденшиалы
                    //указываем action с формы логина/login-processing-url — задает значение action у form при котором Spring Security понимает,
                    // что нужно проверять пользователя согласно настройкам.
                    //сообщает Spring Security обрабатывать предоставленные учетные данные при отправке указанного пути
                    // и, по умолчанию, перенаправляет пользователя обратно пользователю страницы
                                .successHandler(customAuthenticationSuccessHandler)//вот это не тригеритс после того как loadUserByUsername отработал
                    .failureHandler(customAuthenticationFailureHandler)
                    .usernameParameter("login")//принимает логин введенный в форму вот в кавычках login это парметр на форме login.html
                    .passwordParameter("password")
                    .and().exceptionHandling().accessDeniedPage("/error");

            http
                    .addFilterBefore(ssoFilter(), UsernamePasswordAuthenticationFilter.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception
//    {
//        http
//                .authorizeRequests()
//                .antMatchers("/", "/login**").permitAll()
//                .anyRequest().authenticated()
//                .and().formLogin().loginPage("/login")
//                .defaultSuccessUrl("/admin").failureUrl("/login?error").permitAll()
//                .and().logout().logoutSuccessUrl("/").permitAll();
//
//        http
//                .addFilterBefore(ssoFilter(), UsernamePasswordAuthenticationFilter.class);
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
    {
        auth.authenticationProvider(authProvider);
    }




//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Bean
//    public DaoAuthenticationProvider authProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(encoder());
//        return authProvider;
//    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) {
//        try {
//            auth.
//                    userDetailsService(authenticationService).passwordEncoder(encoder());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}