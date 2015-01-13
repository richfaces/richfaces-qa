/**
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
package org.richfaces.tests.metamer.bean.issues;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.google.common.collect.Lists;

@ManagedBean(name = "rf13927")
@SessionScoped
public class RF13927 implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Level> level0;

    public List<Level> getLevel0() {
        return level0;
    }

    @PostConstruct
    public void init() {
        Level l3a = new Level("Level 3 a");
        l3a.setData(Lists.newArrayList("item 1", "item 2", "item 3", "item 4"));
        Level l3b1 = new Level("Level 3 b 1");
        l3b1.setData(Lists.newArrayList("item 1", "item 2"));
        Level l3b2 = new Level("Level 3 b 2");
        l3b2.setData(Lists.newArrayList("item 1", "item 2", "item 3"));

        List<Level> level2a = Lists.newArrayList(new Level("Level 2 a", l3a));
        List<Level> level2b = Lists.newArrayList(new Level("Level 2 b 1", l3b1), new Level("Level 2 b 2", l3b2));

        List<Level> level1 = Lists.newArrayList(new Level("Level 1 a", level2a), new Level("Level 1 b", level2b));
        level0 = Lists.newArrayList(level1);

    }

    public void setLevel0(List<Level> level0) {
        this.level0 = level0;
    }

    public static class Level implements Serializable {

        private static final long serialVersionUID = 1L;

        private List<String> data;
        private List<Level> levels;
        private String name;

        public Level(String name, Level... subLevels) {
            this.name = name;
            levels = Lists.newArrayList(subLevels);
        }

        public Level(String name, Iterable<Level> subLevels) {
            this.name = name;
            levels = Lists.newArrayList(subLevels);
        }

        public List<String> getData() {
            return data;
        }

        public List<Level> getLevels() {
            return levels;
        }

        public String getName() {
            return name;
        }

        public void setData(List<String> data) {
            this.data = data;
        }

        public void setLevels(List<Level> levels) {
            this.levels = levels;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
