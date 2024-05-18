package io.github.xezzon.geom.domain;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.xezzon.tao.trait.Into;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xezzon
 */
@Getter
@Setter
@ToString
public class JwtDTO implements Into<JWTCreator.Builder> {

  public static final String USERNAME_CLAIM = "preferred_username";
  public static final String NICKNAME_CLAIM = "nickname";
  public static final String ROLES_CLAIM = "roles";
  public static final String GROUPS_CLAIM = "groups";
  public static final String PERMISSION_CLAIM = "entitlements";

  private String subject;
  private String username;
  private String nickname;
  private List<String> roles;
  private List<String> groups;
  private List<String> permissions;

  public JWTCreator.Builder into() {
    return JWT.create()
        .withSubject(this.subject)
        .withClaim(USERNAME_CLAIM, this.username)
        .withClaim(NICKNAME_CLAIM, this.nickname)
        .withClaim(ROLES_CLAIM, this.roles)
        .withClaim(GROUPS_CLAIM, this.groups)
        .withClaim(PERMISSION_CLAIM, this.permissions);
  }

  public static JwtDTO from(DecodedJWT jwt) {
    JwtDTO dto = new JwtDTO();
    dto.setSubject(jwt.getSubject());
    dto.setUsername(jwt.getClaim(USERNAME_CLAIM).asString());
    dto.setNickname(jwt.getClaim(NICKNAME_CLAIM).asString());
    dto.setRoles(jwt.getClaim(ROLES_CLAIM).asList(String.class));
    dto.setGroups(jwt.getClaim(GROUPS_CLAIM).asList(String.class));
    dto.setPermissions(jwt.getClaim(PERMISSION_CLAIM).asList(String.class));
    return dto;
  }
}
