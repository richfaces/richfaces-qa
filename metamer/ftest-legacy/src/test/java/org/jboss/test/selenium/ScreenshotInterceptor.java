/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.test.selenium;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.jboss.arquillian.ajocado.browser.BrowserType;
import org.jboss.arquillian.ajocado.command.CommandContext;
import org.jboss.arquillian.ajocado.command.CommandInterceptor;
import org.jboss.arquillian.ajocado.command.CommandInterceptorException;
import org.jboss.arquillian.ajocado.framework.GrapheneConfiguration;
import org.jboss.arquillian.ajocado.framework.GrapheneConfigurationContext;
import org.jboss.arquillian.ajocado.framework.GrapheneSelenium;
import org.jboss.arquillian.ajocado.framework.GrapheneSeleniumContext;
import org.jboss.arquillian.ajocado.guard.GuardedCommands;
import org.richfaces.tests.metamer.ftest.MetamerTestInfo;

/**
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M1
 */
public class ScreenshotInterceptor implements CommandInterceptor {

    /**
     * Proxy to local selenium instance
     */
    private GrapheneSelenium selenium = GrapheneSeleniumContext.getProxy();
    private GrapheneConfiguration configuration = GrapheneConfigurationContext.getProxy();
    protected File mavenProjectBuildDirectory = new File(System.getProperty("maven.project.build.directory",
        "./target/"));
    protected File screenshotsOutputDir = new File(mavenProjectBuildDirectory, "screenshots");
    private static int counter = 0;
    private Method method;
    private NumberFormat numberFormat;
    private MessageDigest messageDigest;
    private String lastImageHash;

    public ScreenshotInterceptor() {
        numberFormat = NumberFormat.getIntegerInstance();
        numberFormat.setMinimumIntegerDigits(3);

        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e1) {
            System.out.println("***** Algorithm SHA-1 is not available.");
        }

        try {
            FileUtils.forceMkdir(screenshotsOutputDir);
            // FIXME it should clean directory only if it is the first test suite
            // FileUtils.cleanDirectory(failuresOutputDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
        lastImageHash = null;
        counter = 0;
    }

    @Override
    public void intercept(CommandContext ctx) throws CommandInterceptorException {
        final String command = ctx.getCommand();

        ctx.invoke();

        if (GuardedCommands.INTERACTIVE_COMMANDS.contains(command)) {
            takeScreenshot(command);
        }
    }

    public void takeScreenshot(String command) {
        BrowserType browser = configuration.getBrowser().getType();
        BufferedImage screenshot = null;
        File imageOutputFile = new File(screenshotsOutputDir, MetamerTestInfo.getAssociatedFilename(method) + "/"
            + getFileName(command));

        if (browser == BrowserType.FIREFOX) {
            screenshot = selenium.captureEntirePageScreenshot();
            File directory = imageOutputFile.getParentFile();

            String newHash = calculateHash(screenshot);
            if (lastImageHash == null || !lastImageHash.equals(newHash)) {

                try {
                    FileUtils.forceMkdir(directory);
                    calculateHash(screenshot);
                    ImageIO.write(screenshot, "PNG", imageOutputFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                lastImageHash = newHash;
            }
        }
    }

    public String getFileName(String command) {
        StringBuilder filename = new StringBuilder(numberFormat.format(counter++));
        filename.append("_");
        filename.append(command);
        filename.append(".png");
        return filename.toString();
    }

    public String calculateHash(BufferedImage image) {
        WritableRaster raster = image.getRaster();
        DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
        messageDigest.update(buffer.getData());
        return new BigInteger(1, messageDigest.digest()).toString(16);
    }

}
