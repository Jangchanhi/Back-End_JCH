package com.example.plugissue.station.controller;

import com.example.plugissue.exceptionhandler.ErrorResponse;
import com.example.plugissue.station.controller.dto.StationStatusDistDto;
import com.example.plugissue.station.controller.dto.StationStatusDto;
import com.example.plugissue.station.service.StationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Api(tags = "Station Api")
@Slf4j
@RestController
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;
    @ResponseBody
    @GetMapping("/station")
    @ApiOperation(value = "범위 별 주변 충전소 조회", notes = "범위 별(km)로 주변에 있는 전기차 충전소를 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = StationStatusDto.class),
            @ApiResponse(code = 400, message = "BAD REQUEST", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)
    })
    public ResponseEntity<List<StationStatusDto>> getStationList(
            @RequestParam("lat")Double lat,
            @RequestParam("lng")Double lng,
            @RequestParam("range") Double range)
    {

        List<StationStatusDto> stationStatusDtoList= stationService.findStationsStatusByLoc(lat, lng, range);
        return ResponseEntity.ok(stationStatusDtoList);
    }

    @ResponseBody
    @GetMapping("/station/{sId}")
    @ApiOperation(value = "특정 충전소 정보 조회", notes = "사용자가 선택한 충전소의 자세한 정보를 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = StationStatusDto.class),
            @ApiResponse(code = 400, message = "BAD REQUEST", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)
    })
    public ResponseEntity<List<StationStatusDto>>getStationDetails(
            @PathVariable("sId") Long stationId
    ){
        List<StationStatusDto> stationStatusDto = stationService.findById(stationId);
        return ResponseEntity.ok(stationStatusDto);
    }

    @ResponseBody
    @GetMapping("/nav/near")
    @ApiOperation(value = "목적지 주변 충전소 목록 조회", notes = "사용자의 목적지 기준 2km 안에 있는 충전소 중 최적의 충전소를 조회한다.")
    public ResponseEntity<List<StationStatusDistDto>>getNearStations(
            @RequestParam("lat") Double lat,
            @RequestParam("lng") Double lng
    ){
        List<StationStatusDistDto> stationStatusDtoList = stationService.findNearStations(lat,lng);

        return ResponseEntity.ok(stationStatusDtoList);
    }
}
