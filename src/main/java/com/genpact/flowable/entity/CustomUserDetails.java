package com.genpact.flowable.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author sxia
 */
public class CustomUserDetails extends User implements UserDetails {

    private static final long serialVersionUID = 1702923242319850756L;

    private final boolean enabled;
    private final boolean accountNonExpired;
    private final boolean credentialsNonExpired;
    private final boolean accountNonLocked;
    private final Set<GrantedAuthority> authorities;

    public CustomUserDetails(User user, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        if (user != null
                && !StringUtils.isBlank(user.getName())
                && !StringUtils.isBlank(user.getPassword())) {
            setName(user.getName());
            setPassword(user.getPassword());
            this.enabled = enabled;
            this.accountNonExpired = accountNonExpired;
            this.credentialsNonExpired = credentialsNonExpired;
            this.accountNonLocked = accountNonLocked;
            this.authorities = Collections.unmodifiableSet(new HashSet<>(CollectionUtils.emptyIfNull(authorities)));
        } else {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
        }
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		String[] roles = new String[] {"ADMIN_ROLE"};
		return Stream.of(roles).map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
	}

	@Override
	public String getUsername() {
		return getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

}
