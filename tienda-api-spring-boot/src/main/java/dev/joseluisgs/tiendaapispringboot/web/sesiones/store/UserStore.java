package dev.joseluisgs.tiendaapispringboot.web.sesiones.store;

import org.springframework.stereotype.Component;

@Component
public class UserStore {
    private int loginCount;

    public int getLoginCount() {
        return loginCount;
    }

    public void incrementLoginCount() {
        loginCount++;
    }
}