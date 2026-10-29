package org.firstinspires.ftc.teamcode.opModes.test;

import com.bylazar.gamepad.PanelsGamepad;
import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Modules.Launcher;

@TeleOp(name = "launcher test", group = "test")
public class LauncherTest extends LinearOpMode {
    Launcher launcher;

    @Override
    public void runOpMode() throws InterruptedException {
        launcher = new Launcher(hardwareMap, PanelsTelemetry.INSTANCE.getFtcTelemetry());
        waitForStart();
        launcher.start();
        Scheduler.schedule(launcher.holding());
        Command shootCommand = launcher.shoot();
        while (opModeIsActive()) {
            if(PanelsGamepad.INSTANCE.getFirstManager().getCircle() && !Scheduler.isRunning(shootCommand))
                Scheduler.schedule(shootCommand);
            launcher.addTelemetry();
            PanelsTelemetry.INSTANCE.getFtcTelemetry().update();
            Scheduler.execute();
        }
    }
}
