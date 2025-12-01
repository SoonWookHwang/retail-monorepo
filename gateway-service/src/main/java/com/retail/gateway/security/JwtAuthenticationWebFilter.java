import com.retail.gateway.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationWebFilter implements WebFilter {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String token = resolveToken(exchange);

    if (token != null && jwtTokenProvider.validateToken(token)) {

      Claims claims = jwtTokenProvider.getClaims(token);

      String email = claims.getSubject();
      String role = claims.get("role", String.class); // 있으면 가져옴

      UserDetails principal = User.withUsername(email)
          .password("")
          .roles(role != null ? role.replace("ROLE_", "") : "USER")
          .build();

      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(
              principal, token, principal.getAuthorities()
          );

      return chain.filter(exchange)
          .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(
              Mono.just(new SecurityContextImpl(authentication))
          ));
    }

    return chain.filter(exchange);
  }

  private String resolveToken(ServerWebExchange exchange) {
    String bearer = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (bearer != null && bearer.startsWith("Bearer ")) {
      return bearer.substring(7);
    }
    return null;
  }
}
