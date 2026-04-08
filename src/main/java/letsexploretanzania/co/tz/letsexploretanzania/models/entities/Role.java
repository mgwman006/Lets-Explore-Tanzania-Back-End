package letsexploretanzania.co.tz.letsexploretanzania.models.entities;

import jakarta.persistence.*;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.RoleNameEnum;

import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToMany(mappedBy = "roles")
  private Set<User> users;

  @Enumerated(EnumType.STRING)
  @Column(unique = true, nullable = false)
  private RoleNameEnum roleNameEnum;

  public Role() {}

  public Role(RoleNameEnum roleNameEnum)
  {
    this.roleNameEnum = roleNameEnum;
  }

  public Long getId() {
    return id;
  }

  public String getRoleName() {
    return roleNameEnum.name();
  }

  public void setRoleName(RoleNameEnum name) {
    this.roleNameEnum = name;
  }

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }
}
