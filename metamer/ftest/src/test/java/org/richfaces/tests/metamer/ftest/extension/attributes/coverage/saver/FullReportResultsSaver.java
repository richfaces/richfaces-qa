/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.extension.attributes.coverage.saver;

import java.io.BufferedWriter;
import java.io.IOException;

import org.apache.commons.lang3.math.Fraction;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.result.CoverageResult;

/**
 * Saves covered, not covered and ignored attributes of all results.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class FullReportResultsSaver extends AbstractResultsSaver {

    protected void write(BufferedWriter bw) throws IOException {
        try {
            int covered = 0, notCovered = 0;
            for (CoverageResult result : getResults()) {
                bw.append(result.getReport());
                bw.newLine();
                bw.flush();
                covered += result.getCovered().size() + result.getIgnored().size();
                notCovered += result.getNotCovered().size();
            }
            Fraction f = Fraction.getFraction(covered, covered + notCovered);
            bw.append("===========================================================");
            bw.newLine();
            bw.append("total coverage: ").append(f.toString()).append(" (").append(String.valueOf((int) (f.doubleValue() * 100))).append("%)");
            bw.newLine();
            bw.flush();
        } finally {
            if (bw != null) {
                bw.close();
            }
        }
    }
}
