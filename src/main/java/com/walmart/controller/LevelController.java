package com.walmart.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.walmart.entities.LevelDetail;
import com.walmart.exception.InvalidRequestException;
import com.walmart.service.LeveLService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = LevelController.REQUEST_MAPPING, produces = MediaType.APPLICATION_JSON_VALUE)
// RestController for creating/storing and retrieving level
public class LevelController {

    public static final String REQUEST_MAPPING = "/assignment/level";

    @Autowired
    private LeveLService levelService;

    private String generateLevelResourceUri(Integer integer) {
        return REQUEST_MAPPING + "/" + integer;
    }

    @ApiOperation(nickname = "Create levels", value = "Create level", notes = "")
    @ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = LevelDetail.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")}) 
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LevelDetail> createLevel(@RequestBody @Valid LevelDetail levelDetail,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException("Invalid create request", bindingResult);
        }
        levelDetail = levelService.createLevel(levelDetail);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", generateLevelResourceUri(levelDetail.getId()));
        return new ResponseEntity<LevelDetail>(levelDetail, httpHeaders, HttpStatus.CREATED);
    }

    
    @ApiOperation(nickname = "Retrieves level detail by levelId", value = "Retrieves level detail", notes = "")
    @ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = LevelDetail.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")}) 
	@RequestMapping(value = "/{levelId}", method = RequestMethod.GET)
    public LevelDetail getLevelById(@PathVariable Integer levelId) {
        return levelService.findLeveLById(levelId);
    }

}