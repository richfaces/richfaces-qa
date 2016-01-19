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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;

@ManagedBean(name = "rf13127")
@SessionScoped
public class RF13127 implements Serializable {

    private static final long serialVersionUID = -1L;

    private List<Car> cars;

    public List<Car> getCars() {
        return cars;
    }

    @PostConstruct
    public void init() {
        cars = new ArrayList<Car>(10);
        for (int i = 0; i < 10; i++) {
            Car car = new Car();
            car.setVin("1324567890abcdef" + i);
            car.setMileage(BigDecimal.valueOf(i * 1234));
            cars.add(car);
        }
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public static class Car implements Serializable {

        private static final long serialVersionUID = -1L;

        @DecimalMin(value = "0.0", message = "Mileage must be greater than 0")
        @DecimalMax(value = "300000.0", message = "Mileage must be lesser than 300000")
        private BigDecimal mileage = null;
        @Size(min = 17, max = 17, message = "VIN must be alphanumeric string with length 17")
        private String vin = null;

        public BigDecimal getMileage() {
            return mileage;
        }

        public String getVin() {
            return vin;
        }

        public void setMileage(BigDecimal mileage) {
            this.mileage = mileage;
        }

        public void setVin(String vin) {
            this.vin = vin;
        }
    }
}
