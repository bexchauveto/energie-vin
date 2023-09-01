package com.bexchauvet.winescrapper.rest;


import com.bexchauvet.lib.mq.WineMQ;
import com.bexchauvet.winescrapper.mq.WineSender;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/wines")
@Tag(name = "Wines", description = "The wines API.")
@AllArgsConstructor
public class WineController {

    private final WineSender wineSender;

    @Operation(summary = "Create a new Wine to be inserted into the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valid wine bottle to be inserted",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = String.class)))})})
    @PostMapping(value = "")
    public String createNewWine(@RequestBody WineMQ wineMQ) {
        wineSender.send(wineMQ);
        return "Message sent to the RabbitMQ Queue Successfully";
    }

}
