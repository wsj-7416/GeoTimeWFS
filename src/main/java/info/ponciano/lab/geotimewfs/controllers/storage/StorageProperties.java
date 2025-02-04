/*
 * Copyright (C) 2021 Dr. Jean-Jacques Ponciano.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package info.ponciano.lab.geotimewfs.controllers.storage;

import info.ponciano.lab.geotimewfs.models.semantic.KB;
import info.ponciano.lab.pitools.files.PiFile;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private  String location = KB.STORAGE_DIR;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        new PiFile(this.location).mkdir();

    }

    public StorageProperties() {
        new PiFile(this.location).mkdir();
    }

}
