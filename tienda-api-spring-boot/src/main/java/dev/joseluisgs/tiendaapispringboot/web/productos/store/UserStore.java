package dev.joseluisgs.tiendaapispringboot.web.productos.store;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

// Componente como Singleton
@Component("userSession")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UserStore {
    @Getter
    private int loginCount;

    @Getter
    @Setter
    private Date lastLogin;

    public void incrementLoginCount() {
        loginCount++;
    }

}