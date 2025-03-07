package asia.circles.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Component
public class RequestMatcherBuilder {

	private final HandlerMappingIntrospector introspector;
	private final String servletPath;

	@Autowired
	public RequestMatcherBuilder(HandlerMappingIntrospector introspector) {
		this(introspector, null);
	}

	private RequestMatcherBuilder(HandlerMappingIntrospector introspector, String servletPath) {
		this.introspector = introspector;
		this.servletPath = servletPath;
	}

	public MvcRequestMatcher[] matchers(String... patterns) {
		MvcRequestMatcher[] matchers = new MvcRequestMatcher[patterns.length];
		for (int index = 0; index < patterns.length; index++) {
			matchers[index] = new MvcRequestMatcher(this.introspector, patterns[index]);
			if (this.servletPath != null) {
				matchers[index].setServletPath(this.servletPath);
			}
		}
		return matchers;
	}

	public RequestMatcherBuilder servletPath(String path) {
		return new RequestMatcherBuilder(this.introspector, path);
	}

	public List<RequestMatcherBuilder> servletPath(String[] path) {
		List<RequestMatcherBuilder> list = new ArrayList<RequestMatcherBuilder>();
		for (int i = 0; i < path.length; i++) {
			list.add(new RequestMatcherBuilder(this.introspector, path[i]));
		}
		return list;
		// new RequestMatcherBuilder(this.introspector, path);
	}
}