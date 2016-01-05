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
package org.richfaces.tests.metamer.bean.issues;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ManagedBean(name = "rf14205")
@SessionScoped
public class RF14205 implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Records> records;

    public List<Records> getRecords() {
        return records;
    }

    @PostConstruct
    public void init() {
        records = Lists.newArrayList();

        List<Record> aRecords = Lists.newArrayList(
            new Record("2015-10-25", 12.312862139711692),
            new Record("2015-10-26", 14.901776611224083),
            new Record("2015-10-27", 14.755423671399239),
            new Record("2015-10-28", 14.602430828520129),
            new Record("2015-10-29", 13.628360161065546),
            new Record("2015-11-02", 14.201179678754213),
            new Record("2015-11-03", 14.200689281784712),
            new Record("2015-11-05", 11.18364331759435),
            new Record("2015-11-10", 13.025021735577573),
            new Record("2015-11-20", 0.6931471805599453)
        );
        List<Record> bRecords = Lists.newArrayList(
            new Record("2015-10-23", 9.490544554572004),
            new Record("2015-10-25", 12.12345678901234),
            new Record("2015-10-26", 14.902114028547388),
            new Record("2015-10-27", 15.079203010035226),
            new Record("2015-10-28", 14.627664025703298),
            new Record("2015-10-29", 13.58524042276822),
            new Record("2015-10-31", 11.146546445569266),
            new Record("2015-11-02", 14.201180358749468),
            new Record("2015-11-03", 14.200690642441854),
            new Record("2015-11-05", 11.18364331759435),
            new Record("2015-11-09", 2.1972245773362196),
            new Record("2015-11-10", 13.025021735577573),
            new Record("2015-11-11", 0.6931471805599453),
            new Record("2015-11-15", 2.4849066497880004),
            new Record("2015-11-16", 1.3862943611198906),
            new Record("2015-11-18", 0.6931471805599453),
            new Record("2015-11-20", 2.4849066497880004)
        );
        List<Record> cRecords = Lists.newArrayList(
            new Record("2015-10-25", 12.318137139226447),
            new Record("2015-10-26", 15.602702110126636),
            new Record("2015-10-27", 15.121923969160472),
            new Record("2015-10-28", 15.317144973089409),
            new Record("2015-10-29", 11.36276514638457),
            new Record("2015-11-02", 14.739717870855348),
            new Record("2015-11-03", 14.279789708602518),
            new Record("2015-11-05", 11.494486507453624),
            new Record("2015-11-10", 13.096019402064726)
        );

        Records a = new Records("line a", aRecords);
        Records b = new Records("line b", bRecords);
        Records c = new Records("line c", cRecords);

        records.add(b);
        records.add(a);
        records.add(c);
    }

    public static class Record {

        private final String date;
        private final double value;

        public Record(String date, double value) {
            this.date = date;
            this.value = value;
        }

        public String getDate() {
            return date;
        }

        public double getValue() {
            return value;
        }
    }

    public static class Records {

        private final List<Record> data;
        private final String name;

        public Records(String name, List<Record> data) {
            this.name = name;
            this.data = data;
        }

        public List<Record> getData() {
            return data;
        }

        public String getName() {
            return name;
        }
    }
}
