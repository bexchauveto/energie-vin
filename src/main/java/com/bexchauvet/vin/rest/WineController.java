package com.bexchauvet.vin.rest;

import com.bexchauvet.vin.error.dto.ErrorDTO;
import com.bexchauvet.vin.rest.dto.SearchWineDTO;
import com.bexchauvet.vin.rest.dto.WineDTO;
import com.bexchauvet.vin.service.WineService;
import com.bexchauvet.vin.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/wines")
@RestController
@Tag(name = "Wines", description = "The wines API.")
@AllArgsConstructor
public class WineController {

    private final WineService wineService;

    @Operation(summary = "Get the list of wines bottles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all the wines bottles",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = WineDTO.class)))}),
            @ApiResponse(responseCode = "401", description = "Invalid authentication token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))})
    @GetMapping("")
    public List<WineDTO> getAll() {
        return this.wineService.getAllWines();
    }

    @Operation(summary = "Get a list of wines bottles corresponding to the criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all the wines bottles corresponding to the criteria",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = WineDTO.class)))}),
            @ApiResponse(responseCode = "401", description = "Invalid authentication token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))})
    @PostMapping("/filter")
    public List<WineDTO> getAllByCriteria(@Valid @RequestBody SearchWineDTO searchWineDTO) {
        return this.wineService.getWinesByCriteria(searchWineDTO);
    }

    @Operation(summary = "Get a map of all the available criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found a map of all the available criteria",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = String.class)))}),
            @ApiResponse(responseCode = "401", description = "Invalid authentication token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))})
    @GetMapping("/criteria")
    public Map<String, Object> getCriteria() {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("colors", Constants.VIN_COLORS);
        criteria.put("countries", this.wineService.getCountries());
        criteria.put("vintages", List.of(1900, LocalDate.now().getYear()));
        criteria.put("min_score", 0.0);
        criteria.put("max_score", 100.0);
        criteria.put("min_price", 0.0);
        criteria.put("max_price", this.wineService.currentMaxPrice() != null ? this.wineService.currentMaxPrice() :
                1.0);
        return criteria;
    }


    @Secured({Constants.ROLE_USER, Constants.ROLE_EXPERT})
    @Operation(summary = "Get a wine bottle by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the wine bottle corresponding",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = WineDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Invalid authentication token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Wine bottle not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))})
    @GetMapping("/{id}")
    public WineDTO getId(@PathVariable("id") String id) {
        return this.wineService.getWineById(id);
    }
}
