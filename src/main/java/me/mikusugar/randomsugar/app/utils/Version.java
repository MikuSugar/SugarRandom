package me.mikusugar.randomsugar.app.utils;

/**
 * @author mikusugar
 */
public class Version {

    public static String getVersion() {
        final String version = Version.class.getPackage().getImplementationVersion();
        return version == null ? "" : version;
    }
}
