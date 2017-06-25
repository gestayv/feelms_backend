package json;

/**
 * Created by Arturo on 25-06-2017.
 */
public class NewSubscription {

    private String first_name;

    private String last_name;

    private String mail;

    private  NewSubscription() {

    }

    public NewSubscription(String first_name, String last_name, String mail) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.mail = mail;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
