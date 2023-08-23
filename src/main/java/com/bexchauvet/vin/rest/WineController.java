package com.bexchauvet.vin.rest;

import com.bexchauvet.vin.error.dto.ErrorDTO;
import com.bexchauvet.vin.rest.dto.WineDTO;
import com.bexchauvet.vin.service.WineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
