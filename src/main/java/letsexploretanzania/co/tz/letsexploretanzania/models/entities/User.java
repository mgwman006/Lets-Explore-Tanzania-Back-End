package letsexploretanzania.co.tz.letsexploretanzania.models.entities;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User implements UserDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String userName;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "tpurist_id")
    private Tourist tourist;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private TourOperator tourOperator;

    public User()
    {
    }

    public User(String userName, String password)
    {
        this.userName = userName;
        this.password = password;
    }

    public Long getId()
    {
        return id;
    }

    public void setUserName(String email)
    {
        this.userName = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Tourist getTourist()
    {
        return tourist;
    }

    public void setTourist(Tourist tourist)
    {
        this.tourist = tourist;
        if (tourist.getUser() != this)
        {
            tourist.setUser(this);
        }
    }

    public TourOperator getTourOperator()
    {
        return tourOperator;
    }

    public void setTourOperator(TourOperator tourOperator)
    {
        this.tourOperator = tourOperator;
        if (tourOperator.getUser() != this)
        {
            tourOperator.setUser(this);
        }
    }

    public Set<Role> getRoles()
    {
        return roles;
    }

    public void setRoles(Set<Role> roles)
    {
        this.roles = roles;
    }

    public void addRole(Role role)
    {
        this.roles.add(role);
    }

    // -------------------- UserDetails Methods --------------------
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return roles.stream()
          .map(role -> (GrantedAuthority) role::getRoleName)
          .collect(Collectors.toSet());
    }

    @Override
    public String getUsername()
    {
      return userName;
    }

}
