package com.fasterxml.jackson.dataformat.php.cfg;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.Versioned;
import com.fasterxml.jackson.core.util.VersionUtil;

/**
 * Automatically generated from PackageVersion.java during
 * packageVersion-generate execution of maven-template-plugin in
 * pom.xml.
 */
public final class PackageVersion implements com.fasterxml.jackson.core.Versioned {
    public final static Version VERSION = VersionUtil.parseVersion(
            "${project.version}", "${project.groupId}", "${project.artifactId}");

    @Override
    public Version version() {
        return VERSION;
    }
}
