package ge.unknown.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageConfiguration {

    /**
     * Folder location for storing files
     */
    private String location = "uploadFiles";
    private String root = "/";

    public String getLocation() {
        return location;
    }

    public String getRoot() {
        return root;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}