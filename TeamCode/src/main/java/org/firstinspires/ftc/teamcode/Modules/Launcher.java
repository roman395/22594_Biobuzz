package org.firstinspires.ftc.teamcode.Modules;

import android.util.Range;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.configurables.annotations.GenericValue;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.behaviors.InterruptedBehavior;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.core.Module;
import org.firstinspires.ftc.teamcode.core.PID;

public class Launcher extends Module {
    private final DcMotorEx master, slave;
    private final Rev2mDistanceSensor sensor;
    private final PID pid = new PID(LauncherConfig.chargingCoefficients).setIRange(LauncherConfig.iRange);

    public Launcher(HardwareMap map, Telemetry telemetry) {
        super(map, telemetry);
        master = hardwareMap.get(DcMotorEx.class, LauncherConfig.leftMotorName);
        slave = hardwareMap.get(DcMotorEx.class, LauncherConfig.rightMotorName);
        sensor = hardwareMap.get(Rev2mDistanceSensor.class, LauncherConfig.distanceSensorName);
    }

    @Override
    public void start() {
        pid.reset();
    }

    @Override
    public void stop() {

    }
    private double getFilteredDistance(){
        return 1;
    }
    public Command holding(){
        return Command.build()
                .setStart(() -> {
                    pid.setTarget(LauncherConfig.chargeDist);
                })
                .setExecute(() -> {
                    if(master.getCurrent(CurrentUnit.MILLIAMPS) > LauncherConfig.chargeAmps)
                        pid.setCoefficients(LauncherConfig.holdingCoefficients);
                    double power = pid.calculate(getFilteredDistance());
                    master.setPower(power);
                    slave.setPower(power);
                })
                .setDone(() -> false)
                .requiring(master, slave)
                .setInterruptedBehavior(InterruptedBehavior.SUSPEND)
                .setPriority(0);
    }
    public Command shoot(){
        return Command.build()
                .setStart(()->{
                    pid.setCoefficients(LauncherConfig.chargingCoefficients);
                    pid.reset();
                })
                .setExecute(()->{
                    master.setPower(LauncherConfig.shootPower);
                    slave.setPower(LauncherConfig.shootPower);
                })
                .setDone(()-> getFilteredDistance() > LauncherConfig.chargeDist + LauncherConfig.chargeDistOffset)
                .requiring(master, slave)
                .setPriority(1);
    }
}

@Configurable
class LauncherConfig {
    public static String leftMotorName = "", rightMotorName = "", distanceSensorName = "";
    public static PIDFCoefficients chargingCoefficients = new PIDFCoefficients(-0.03, -0.0001, 0, 0);
    public static PIDFCoefficients holdingCoefficients = new PIDFCoefficients(-0.03, -0.0001, 0, 0);
    @GenericValue(tParam = Double.class)
    public static Range<Double> iRange = new Range<>(-0.6, 0.6);
    public static double chargeDist = 95;
    public static double chargeDistOffset = 50;
    public static double chargeAmps = 1000;
    public static double shootPower = 1;
}