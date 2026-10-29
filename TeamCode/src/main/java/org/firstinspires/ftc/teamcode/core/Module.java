package org.firstinspires.ftc.teamcode.core;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class Module {
    protected HardwareMap hardwareMap;
    protected Telemetry telemetry;
    public Module(HardwareMap hardwareMap, Telemetry telemetry){
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
    }
    public abstract void start();
    public abstract void stop();
}
