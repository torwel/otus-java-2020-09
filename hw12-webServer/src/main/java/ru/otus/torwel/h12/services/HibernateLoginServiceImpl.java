package ru.otus.torwel.h12.services;

import org.eclipse.jetty.security.AbstractLoginService;
import org.eclipse.jetty.security.RolePrincipal;
import org.eclipse.jetty.security.UserPrincipal;
import org.eclipse.jetty.util.security.Password;
import ru.otus.torwel.h12.core.model.User;
import ru.otus.torwel.h12.core.service.DBServiceUser;

import java.util.List;
import java.util.Optional;

public class HibernateLoginServiceImpl extends AbstractLoginService  {

    private final DBServiceUser dbServiceUser;

    public HibernateLoginServiceImpl(String name, DBServiceUser dbServiceUser) {
        setName(name);
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    protected List<RolePrincipal> loadRoleInfo(UserPrincipal user) {
        return List.of(new RolePrincipal("user"));
    }

    @Override
    protected UserPrincipal loadUserInfo(String login) {
        System.out.println(String.format("HibernateLoginServiceImpl#loadUserInfo(%s)", login));
        Optional<User> dbUser = dbServiceUser.getUser(login);
        return dbUser.map(u -> new UserPrincipal(u.getLogin(), new Password(u.getPassword()))).orElse(null);
    }
}
