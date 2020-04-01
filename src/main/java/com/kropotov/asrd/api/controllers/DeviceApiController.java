package com.kropotov.asrd.api.controllers;

import com.kropotov.asrd.entities.DeviceComponent;
import com.kropotov.asrd.entities.DeviceComponentTitle;
import com.kropotov.asrd.entities.DeviceTitle;
import com.kropotov.asrd.entities.SystemTitle;
import com.kropotov.asrd.services.DeviceTitleService;
import com.kropotov.asrd.services.SystemTitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceApiController {

    private final DeviceTitleService deviceTitleService;
    private final SystemTitleService systemTitleService;


    @GetMapping("/titles")
    public Iterable<DeviceTitle> getDeviceTitlesBySystemTitleId(@RequestParam(value = "systemTitleId", required = false) Long systemTitleId) {
        if (systemTitleId == null) {
            return deviceTitleService.getAll();
        }
        SystemTitle systemTitle = systemTitleService.getById(systemTitleId);
        return systemTitle.getDeviceTitles();
    }

    @GetMapping("/components")
    public Iterable<DeviceComponent> getDeviceComponentsByDeviceTitleId(@RequestParam("deviceTitleId") Long deviceTitleId) {
        List<DeviceComponentTitle> componentTitles = deviceTitleService.getById(deviceTitleId).getComponentTitles();
        List<DeviceComponent> components = new ArrayList<>();
        for (DeviceComponentTitle d: componentTitles) {
            DeviceComponent deviceComponent = new DeviceComponent();
            deviceComponent.setTitle(d);
            components.add(deviceComponent);
        }
        return components;
    }
}