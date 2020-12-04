package AppointmentManager.Model;

public class Contact extends Noun{

    private String email;

    /** Constructor for Contact
     * @param id int
     * @param name String
     * @param email String
     */
    Contact(int id, String name, String email) {
        super(id, name);
        this.email = email;
    }

    /** Getter for email
     * @return String
     */
    public String getEmail() {
        return email;
    }

    /** Setter for email
     *
     * @param email String
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
