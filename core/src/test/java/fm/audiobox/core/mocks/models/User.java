package fm.audiobox.core.mocks.models;

public class User extends fm.audiobox.core.models.User {
    
    private String test = "test";

    /**
     * @param test the test to set
     */
    public void setTest(String test) {
        this.test = test;
    }

    /**
     * @return the test
     */
    public String getTest() {
        return test;
    }
}
