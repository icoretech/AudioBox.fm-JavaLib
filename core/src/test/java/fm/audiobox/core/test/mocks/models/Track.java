package fm.audiobox.core.test.mocks.models;

public class Track extends fm.audiobox.core.models.Track {
    private String test;

    public Track() {
        test = "TEST";
    }
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
