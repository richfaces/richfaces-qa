/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.extension.attributes.coverage;

import java.io.File;
import java.io.IOException;

import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.collect.AttributesCoverageCollector;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.collect.SimpleAttributesCoverageCollector;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.result.CoverageResult;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.result.CoverageResultsCreator;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.result.ResultsCreator;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.saver.NotCoveredNotEmptyResultsSaver;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.saver.ResultsSaver;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.saver.ResultsSaver.SaveTo;

/**
 * Checks all RF's components attributes coverage.
 *
 * Searches in all Metamer ftest classes for methods with annotation @CoversAttributes. Saves the result to file
 * target/coverage.txt.
 *
 * To perform the attributes coverage scan just run this file.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class CoverageCollectorTool {

    private static final String PACKAGE_TO_SCAN_FROM = "org.richfaces.tests.metamer.ftest";
    private static final AttributesCoverageCollector attributesCoverageCollector = new SimpleAttributesCoverageCollector();
    private static final ResultsCreator<? extends CoverageResult> resultsCreator = new CoverageResultsCreator();
//    private static final ResultsSaver resultsSaver = new NotCoveredResultsSaver();
    private static final ResultsSaver resultsSaver = new NotCoveredNotEmptyResultsSaver();
//    private static final ResultsSaver resultsSaver = new FullReportResultsSaver();

    public static void main(String[] args) throws IOException {
        SaveTo save = resultsSaver.save(
            resultsCreator.createResultsFrom(
                attributesCoverageCollector.collect(PACKAGE_TO_SCAN_FROM)
            )
        );
        save.to(System.out);
        save.to(new File("target/coverage.txt"));
    }
}
