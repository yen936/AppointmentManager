package AppointmentManager.Model;

public class Noun {

    private int Id;
    private String name;

    /** Constructor for Noun
     *
     * @param id int
     * @param name String
     */
    Noun(int id, String name) {
        this.Id = id;
        this.name = name;
    }

    /** Getter for ID
     *
     * @return int
     */
    public int getID() {
        return Id;
    }

    /** Setter for ID
     *
     * @param ID int
     */
    public void setID(int ID) {
        this.Id = ID;
    }

    /** Getter for Name
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /** Setter for Name
     *
     * @param name String
     */
    public void setUserName(String name) {
        this.name = name;
    }


}
