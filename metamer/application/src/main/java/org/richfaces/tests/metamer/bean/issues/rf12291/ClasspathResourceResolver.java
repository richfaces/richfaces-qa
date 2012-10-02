package org.richfaces.tests.metamer.bean.issues.rf12291;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.faces.facelets.impl.DefaultResourceResolver;

/**
 * Resolves Facelets pages based on classpath
 */
public class ClasspathResourceResolver extends DefaultResourceResolver {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static String SLASH = "/";
    private static String BASE_PATH_PAGES = "/WEB-INF/classes/";

    @Override
    public URL resolveUrl(final String url) {

        logger.info("url: " + url);

        URL resolvedUrl = super.resolveUrl(url);

        if (resolvedUrl == null) {// if1
            String urlPath = toModulePagePath(url);
            logger.info("urlPath: " + urlPath);
            resolvedUrl = super.resolveUrl(BASE_PATH_PAGES + urlPath);
            logger.info("resolverUrl: " + resolvedUrl);
        }// if1

        if (resolvedUrl == null) {
            logger.info("Page url: " + url + " not found. Showing wrongUrl page...");
            String wrongPageUrl = BASE_PATH_PAGES + "org/richfaces/tests/metamer/bean/issues/rf12291/wrongUrl.xhtml";
            resolvedUrl = super.resolveUrl(wrongPageUrl);
        }

        if (resolvedUrl == null) {
            throw new IllegalStateException("Unexpected error: wrongUrl.xhtml page not found");
        }

        logger.info("resolvedUrl: " + resolvedUrl.toString());

        return resolvedUrl;
    }

    private String toModulePagePath(String url) {
        String normalizedUrl = normalizeUrl(url);

        int moduleAcronymStartIdx = normalizedUrl.lastIndexOf("/module/") + "/module/".length();
        int moduleAcronymEndIdx = moduleAcronymStartIdx + normalizedUrl.substring(moduleAcronymStartIdx).indexOf(SLASH);

        String moduleAcronymPath = normalizedUrl.substring(0, moduleAcronymEndIdx);
        logger.info("moduleAcronymPath: " + moduleAcronymPath);
        String pageRelativePath = normalizedUrl.substring(moduleAcronymEndIdx + 1);
        logger.info("pageRelativePath: " + pageRelativePath);
        return "org/richfaces/tests/metamer/bean/issues/rf12291/" + pageRelativePath;
    }

    private String normalizeUrl(String url) {
        logger.info("Unnormalized Url: " + url);
        if (url == null || url.length() == 0) {
            throw new IllegalStateException("Received a null or empty url String");
        }
        String normalizedUrl = url;
        if (!normalizedUrl.startsWith(SLASH)) {
            normalizedUrl = SLASH + normalizedUrl;
        }
        if (!normalizedUrl.toLowerCase().startsWith("/module")) {
            normalizedUrl = "/module" + normalizedUrl;
        }
        logger.info("Normalized Url: " + normalizedUrl);
        return normalizedUrl;
    }

}
