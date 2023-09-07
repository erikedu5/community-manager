package com.meztlisoft.communitymanager.controller;

import com.meztlisoft.communitymanager.dto.ActionStatusResponse;
import com.meztlisoft.communitymanager.dto.MeetingAttendanceDto;
import com.meztlisoft.communitymanager.dto.MeetingDto;
import com.meztlisoft.communitymanager.dto.MeetingResponse;
import com.meztlisoft.communitymanager.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/meeting")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @GetMapping("/{id}")
    public ResponseEntity<MeetingResponse> getById(@PathVariable(name = "id") final long id) {
        return ResponseEntity.ok(meetingService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ActionStatusResponse> save(@RequestBody MeetingDto meeting,
                                                      @RequestHeader(name = "Authorization") String token,
                                                      @RequestHeader(name = "retinueId") Long retinueId) {
        return ResponseEntity.ok(meetingService.save(meeting, token, retinueId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActionStatusResponse> saveAttendance(@PathVariable(name = "id") final Long id,
                                                                @RequestBody MeetingAttendanceDto meetingAttendanceDto) {
        return ResponseEntity.ok(meetingService.check(id, meetingAttendanceDto));
    }

    @PostMapping("/all")
    public ResponseEntity<List<MeetingResponse>> getAll(@RequestHeader(name = "retinueId") Long retinueId) {
        return ResponseEntity.ok(meetingService.getAll(retinueId));
    }

}
