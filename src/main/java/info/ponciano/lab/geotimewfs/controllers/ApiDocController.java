/*
 * Copyright (C) 2020 claireprudhomme.
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
package info.ponciano.lab.geotimewfs.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author claireprudhomme
 */

@Controller
public class ApiDocController {
    
    @GetMapping("/api")
    public String postMdChangeAction(@RequestParam(name = "format", required = false, defaultValue = "HTML") String format) {
        String rtn="";
        if (format.equals("YAML")){
            rtn = "redirect:/v3/api-docs.yaml";
        }
        else if (format.equals("JSON")){
                rtn = "redirect:/v3/api-docs/";
            }
        else rtn = "redirect:/swagger-ui.html";
        return rtn;
    }
}
