package edu.uoc.orimarkettfm.clases;

public class user {

    private String pnombre;
    private String papellido;
    private String email;
    private String password;
    private String telefono;
    private String tipo;

    public user(String pnombre, String papellido, String email, String password, String telefono, String tipo) {
        this.pnombre = pnombre;
        this.papellido = papellido;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.tipo = tipo;
    }

    public String getPnombre() {
        return pnombre;
    }

    public void setPnombre(String pnombre) {
        this.pnombre = pnombre;
    }

    public String getPapellido() {
        return papellido;
    }

    public void setPapellido(String papellido) {
        this.papellido = papellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
