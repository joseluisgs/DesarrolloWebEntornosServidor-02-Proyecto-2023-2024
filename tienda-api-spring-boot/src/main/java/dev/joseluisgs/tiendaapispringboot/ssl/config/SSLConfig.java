package dev.joseluisgs.tiendaapispringboot.ssl.config;

// Por defecto la conexion es con SSL, por lo que vamos a decirle que use el puerto 3080
// para la conexión sin SSL
// Comentar si no usamos SSL para que no de error
/*
@Configuration
public class SSLConfig {
    // (User-defined Property)
    @Value("${server.http.port}")
    private int httpPort;

    // Creamos un bean que nos permita configurar el puerto de conexión sin SSL
    @Bean
    ServletWebServerFactory servletContainer() {
        var connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setPort(httpPort);
        var tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(connector);
        return tomcat;
    }
}*/
