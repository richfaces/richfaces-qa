/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.tests.metamer.bean;

import com.google.common.collect.Lists;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ManagedBean
@ViewScoped
public class KeepSavedBean {

    private List<Data> dataList = Lists.newArrayList(new Data(1), new Data(2), new Data(3), new Data(4));
    private List<String> singleStringList = Lists.newArrayList("1");
    private boolean keepSaved = false;

    public boolean isKeepSaved() {
        return keepSaved;
    }

    public void setKeepSaved(boolean keepSaved) {
        this.keepSaved = keepSaved;
    }

    public List<String> getSingleStringList() {
        return singleStringList;
    }

    /**
     * @return the data
     */
    public List<Data> getDataList() {
        return dataList;
    }

    public static class Data {

        private Integer data;

        public Data() {
        }

        public Data(Integer data) {
            this.data = data;
        }

        public Integer getData() {
            return data;
        }

        public void setData(Integer data) {
            this.data = data;
        }
    }
}
