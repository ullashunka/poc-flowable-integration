package asia.circles.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class FlowSecurityConfig {

	private @Autowired PasswordEncoder passwordEncoder;

	private @Autowired UserDetailsService userDetailsService;

	private @Autowired RequestMatcherBuilder mvc;

	@Bean
	public AuthenticationManager authManager() {
		var authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(authProvider);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
			throws Exception {

		return http.csrf(csrf -> {
			csrf.disable();
		}).cors(cors -> cors.disable()).authorizeHttpRequests(auth -> {
			// auth.requestMatchers(mvc.matchers("/error/**")).permitAll();
			auth.requestMatchers(mvc.matchers("/**")).permitAll();
			// auth.requestMatchers(mvc.matchers("/home")).permitAll();
			// auth.requestMatchers(mvc.matchers("/")).permitAll();
			// auth.requestMatchers(mvc.matchers("/learner")).permitAll();
			// auth.requestMatchers(mvc.matchers("/webjars/**")).permitAll();
			// auth.requestMatchers(mvc.matchers("/resources/**")).permitAll();
			auth.anyRequest().authenticated();
		}).build();
	}

//	@Bean
//	public JwtDecoder jwtDecoder() {
//		return NimbusJwtDecoder.withPublicKey(rsaKeyConfigProperties.publicKey()).build();
//	}
//
//	@Bean
//	JwtEncoder jwtEncoder() {
//		JWK jwk = new RSAKey.Builder(rsaKeyConfigProperties.publicKey()).privateKey(rsaKeyConfigProperties.privateKey())
//				.build();
//
//		JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
//		return new NimbusJwtEncoder(jwks);
//	}

//	@Bean
//	public CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration configuration = new CorsConfiguration();
//		String[] xmet = { "http://localhost:8080", "http://localhost:3001", "https://scorizons.dronaedms.com",
//				"https://auth.dronaedms.com", "https://dronaedms.com" };
//		configuration.setAllowedOrigins(Arrays.asList(xmet));
//		configuration.setAllowedHeaders(List.of("*"));
//		String[] met = { "GET", "POST" };
//		List<String> allowed = Arrays.asList(met);
//		configuration.setAllowedMethods(allowed);
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", configuration);
//		return source;
//	}
	/*
	 * @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder();
	 * }
	 */
}
