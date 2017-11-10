package fr.aeris.actris.validation.rest.services.data;
public class FtpConfig {

    private String login;
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getRootFolder() {
        return rootFolder;
    }
    public void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
    }
    private String password;
    private String address;
    private String rootFolder;
    
}