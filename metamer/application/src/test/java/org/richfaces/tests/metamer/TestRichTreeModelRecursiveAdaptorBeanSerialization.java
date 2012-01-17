/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.richfaces.tests.metamer.bean.rich.RichTreeModelRecursiveAdaptorBean;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.io.NullOutputStream;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22848 $
 */
public class TestRichTreeModelRecursiveAdaptorBeanSerialization {

    PrintStream sysout;
    
    private Set<Integer> notEquals = new TreeSet<Integer>();

    @BeforeClass
    public void redirectOut() {
        sysout = System.out;
        System.setOut(new PrintStream(new NullOutputStream()));
    }

    @AfterClass(alwaysRun = true)
    public void returnOut() {
        System.setOut(sysout);
    }

    //@Test
    public void testBeanSerialization() throws IOException, ClassNotFoundException, IllegalArgumentException,
        IllegalAccessException {

        RichTreeModelRecursiveAdaptorBean bean = new RichTreeModelRecursiveAdaptorBean();
        bean.init();

        Object obj1 = bean;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
        outputStream.writeObject(obj1);

        byte[] bytes = byteArrayOutputStream.toByteArray();

        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Object obj2 = inputStream.readObject();

        verifyEqualsDeeply(obj1, obj2);
    }

    public void verifyEqualsDeeply(Object obj1, Object obj2) throws IllegalArgumentException, IllegalAccessException {

        if (obj1 == null) {
            if (obj2 == null) {
                return;
            }
            throw new AssertionError("obj1 is null, but obj2 is not null");
        }

        Class<?> classO = obj1.getClass();
        assertEquals(obj2.getClass(), classO, "Both objects should be instances of the same class");

        if (obj1.equals(obj2)) {
            return;
        }

        int hash = cohash(obj1, obj2);
        if (notEquals.contains(hash)) {
            return;
        }

        notEquals.add(hash);

        if (Collection.class.isAssignableFrom(classO)) {
            Collection<?> col1 = (Collection<?>) obj1;
            Collection<?> col2 = (Collection<?>) obj2;
            assertEquals(col1.toArray(new Object[col1.size()]), col2.toArray(new Object[col2.size()]));
        } else if (Map.class.isAssignableFrom(classO)) {
            Map map1 = (Map) obj1;
            Map map2 = (Map) obj2;

            Object[] array1 = map1.keySet().toArray(new Object[map1.size()]);
            Arrays.sort(array1, new HashCodeComparator());

            Object[] array2 = map2.keySet().toArray(new Object[map2.size()]);
            Arrays.sort(array2, new HashCodeComparator());

            assertEquals(array1, array2);

            array1 = map1.values().toArray(new Object[map1.size()]);
            Arrays.sort(array1, new HashCodeComparator());

            array2 = map2.values().toArray(new Object[map2.size()]);
            Arrays.sort(array2, new HashCodeComparator());

            assertEquals(array1, array2);
        } else {
            for (Field field : classO.getDeclaredFields()) {

                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                if (Modifier.isTransient(field.getModifiers())) {
                    continue;
                }

                boolean accessible = field.isAccessible();
                if (!accessible) {
                    field.setAccessible(true);
                }
                Object nObj1 = field.get(obj1);
                Object nObj2 = field.get(obj2);
                if (!accessible) {
                    field.setAccessible(false);
                }
                verifyEqualsDeeply(nObj1, nObj2);
            }
        }
    }

    public int cohash(Object obj1, Object obj2) {
        return 17 * obj1.hashCode() * obj2.hashCode() + obj2.hashCode() + 11;
    }

    public class HashCodeComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            return o1.hashCode() - o2.hashCode();
        }
    }
}
