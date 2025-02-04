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

import info.ponciano.lab.geotimewfs.models.Catalog;
import info.ponciano.lab.geotimewfs.models.Catalogs;
import info.ponciano.lab.geotimewfs.models.semantic.OntoManagementException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author claireprudhomme
 */
@Controller
@RequestMapping("/geotimeWFS")
public class OGCapiRecordsHTMLController {

    /**
     * The landing page provides links to the API definition, links to the
     * conformance statement, links to catalogues metadata and links to other
     * resources offered by the service.
     *
     * @param f A MIME type indicating the representation of the resources to be
     * presented. Available values : json, xml, html
     * @param model
     * @return
     */
    @GetMapping("/")
    public String getLandingPage(@RequestParam(name = "f", required = false, defaultValue = "html") String f, Model model) {
        return "geotimeWFS";
    }

    @GetMapping("/error")
    public String errorManagement(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("message", name);
        return "errorView";

    }

    /**
     * The conformance provides the conformance statement
     *
     * @param f A MIME type indicating the representation of the resources to be
     * presented. Available values : json, xml, html
     * @param model
     * @return
     */
    @GetMapping("/conformance")
    public String getConformance(@RequestParam(name = "f", required = false, defaultValue = "html") String f, Model model) {

        String rtn = "conformanceView";

        switch (f) {
            case "json":
                rtn = "redirect:/api/geotimeWFS/conformance?f=json";
                break;
            case "xml":
                rtn = "redirect:/api/geotimeWFS/conformance?f=xml";
                break;
            case "html":
                rtn="conformanceView";
            break;
        }

        return rtn;
    }

    /**
     * A catalogue is a collection of records that describe a set of things. A
     * catalogue end point may offer a single collection of records (the usual
     * case) but may offer more that one collection of records each describing
     * different things (e.g. a catalogue of imagery and a catalogue of vector
     * data). The /collections endpoint provides metadata about the list of
     * available record collections.
     *
     * @param f A MIME type indicating the representation of the resources to be
     * presented. Available values : json, xml, html
     * @param model
     * @return
     */
    @Operation(summary = "The set of catalogues offered at this endpoint.",
            description = "A catalogue is a collection of records that describe a set of things. A catalogue end point may may offer a single collection of records (the usual case) but may offer more that one collection of records each describing different things (e.g. a catalogue of imagery and a catalogue of vector data). The /collections endpoint provides metadata about the list of available record collections.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OGCapiRecordsController.class))}),
        @ApiResponse(responseCode = "default",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OGCapiRecordsController.class))})})
    @GetMapping("/collections")
    public String getCatalogues(@RequestParam(name = "f", required = false, defaultValue = "html") String f, Model model) {

        String rtn = "cataloguesView";
        switch (f) {
            case "json":
                rtn = "redirect:/api/geotimeWFS/collections?f=json";
                break;
            case "xml":
                rtn = "redirect:/api/geotimeWFS/collections?f=xml";
                break;
            case "html":
                try {
                    //create a collection of catalogs containing all catalogs in the ontology
                    Catalogs c = new Catalogs();
                    //provide it to the model to dispaly it in the HTML page "cataloguesView"
                    model.addAttribute("catalogues", c.catalogues);
                } catch (OntoManagementException ex) {
                    Logger.getLogger(OGCapiRecordsHTMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
        }

        return rtn;
    }

    /**
     * Provides metadata about a specific collection of records. Same output as
     * generated for /collections but specific to the indicated catalogueId
     *
     * @param catalogueId Identifier of a catalogue offered by the service.
     * Available values : ogcCore, ebRIM
     * @param f A MIME type indicating the representation of the resources to be
     * presented (e.g. application/xml). Available values : html, xml, json
     * @param model
     * @return
     */
    @Operation(description = "Provides metadata about a specific collection of records. Same output as generated for /collections but specific to the indicated catalogueId")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the book",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OGCapiRecordsController.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Book not found",
                content = @Content)})
    @GetMapping("/collections/{catalogueId}")
    public String getCatalogue(@PathVariable(name = "catalogueId", required = true) String catalogueId, @RequestParam(name = "f", required = false, defaultValue = "html") String f, Model model) {
        String rtn = "";
        try {
            Catalog c;
            System.out.println(catalogueId);
            c = new Catalog(catalogueId);
            model.addAttribute("message", c.getJo().toString());
            rtn = "view";
        } catch (OntoManagementException ex) {
            Logger.getLogger(OGCapiRecordsController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rtn;
    }

    @Operation(summary = "Get the list of queryables for this catalogue")
    @GetMapping("/collections/{catalogueId}/queryables")
    public String getQueryables(@PathVariable(name = "catalogueId", required = true) String catalogueId, @RequestParam(name = "f", required = false, defaultValue = "html") String f, Model model) {
        try {
            Catalog c =new Catalog(catalogueId);
            model.addAttribute("title", c.title);
            model.addAttribute("id", c.catalogId);
            model.addAttribute("queryables", c.queryables);
        } catch (OntoManagementException ex) {
            Logger.getLogger(OGCapiRecordsHTMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "queryableView";
    }

    @GetMapping("/collections/{catalogueId}/items")
    public String getRecords(
            @PathVariable(name = "catalogueId", required = true) String catalogueId,
            @RequestParam(name = "f", required = false, defaultValue = "html") String f,
            @RequestParam(name = "crs", required = false, defaultValue = "") String crs,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            // @RequestParam(name = "q", required = false, defaultValue = "") String q,
            @RequestParam(name = "bbox", required = false, defaultValue = "") String bbox,
            //@RequestParam(name = "geometry", required = false, defaultValue = "") String geometry,
            @RequestParam(name = "geometry_crs", required = false, defaultValue = "") String geometry_crs,
            //@RequestParam(name = "gRelation", required = false, defaultValue = "") String gRelation,
            //@RequestParam(name = "lat", required = false, defaultValue = "0.0") double lat,
            // @RequestParam(name = "lon", required = false, defaultValue = "0.0") double lon,
            //@RequestParam(name = "radius", required = false, defaultValue = "0.0") double radius,
            @RequestParam(name = "time", required = false, defaultValue = "") String time,
            //@RequestParam(name = "tRelation", required = false, defaultValue = "") String tRelation,
            @RequestParam(name = "filter", required = false, defaultValue = "") String filter,
            @RequestParam(name = "filter_language", required = false, defaultValue = "") String filter_language,
            Model model) {
        //TODO search items from the Semantic WFS corresponding to the provided parameters.
        //SemanticWFSRequest sr = new SemanticWFSRequest();
        //String collectionid = "";
        //retrieve features from the SemanticWFS corresponding to the provided parameters.
        //String jsfeatures = sr.getCollectionItems(collectionid, "json", limit, offset, bbox, null, crs, geometry_crs, filter, filter_language, time);

        //First version without other parameters than the output format
        String rtn = "";
        switch (f) {
            case "json":
                rtn = "redirect:/api/geotimeWFS/collections/{catalogueId}/items?f=json";
                break;
            case "xml":
                rtn = "redirect:/api/geotimeWFS/collections/{catalogueId}/items/{recordId}?f=xml";
                break;
            case "html":
                rtn = "redirect:/metadata";
                break;
        }
        return rtn;
    }

    @GetMapping("/collections/{catalogueId}/items/{recordId}")
    public String getRecord(@PathVariable(name = "catalogueId", required = true) String catalogueId, @PathVariable(name = "recordId", required = true) String recordId, @RequestParam(name = "f", required = false, defaultValue = "html") String f, Model model) {
        String rtn = "";
        switch (f) {
            case "json":
                rtn = "redirect:/api/geotimeWFS/collections/{catalogueId}/items/{recordId}?f=json";
                break;
            case "xml":
                rtn = "redirect:/api/geotimeWFS/collections/{catalogueId}/items/{recordId}?f=xml";
                break;
            case "html":
                rtn = "redirect:/metadata/selected?md=" + recordId;
                break;
        }
        return rtn;
    }
}
