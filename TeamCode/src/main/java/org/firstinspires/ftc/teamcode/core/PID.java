package org.firstinspires.ftc.teamcode.core;

import android.util.Range;

import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

public class PID {
    private PIDFCoefficients coefficients;
    private double lastError = 0, errorSum = 0, target = 0;
    private long lastTime = 0;
    private Range<Double> outputRange = new Range<>(-1.0, 1.0), iRange = new Range<>(-1.0, 1.0);
    private final ElapsedTime timer;

    public PID(PIDFCoefficients coefficients) {
        this.coefficients = coefficients;
        timer = new ElapsedTime();
    }

    public PID setCoefficients(PIDFCoefficients coefficients) {
        this.coefficients = coefficients;
        return this;
    }

    public PID setOutputRange(Range<Double> outputRange) {
        this.outputRange = outputRange;
        return this;
    }

    public PID setIRange(Range<Double> iRange) {
        this.iRange = iRange;
        return this;
    }

    public PID reset() {
        errorSum = 0;
        lastError = 0;
        lastTime = 0;
        timer.reset();
        return this;
    }

    public PID setTarget(double target) {
        this.target = target;
        return this;
    }

    public double calculate(double current) {
        double error = target - current;
        double p = coefficients.p * error;
        long time = timer.nanoseconds();
        errorSum += error;
        long deltaTime = time - lastTime;
        double i = iRange.clamp(coefficients.i * errorSum * deltaTime);
        double d = coefficients.d * (error - lastError) / deltaTime;
        double f = target * coefficients.f;
        lastError = error;
        lastTime = time;
        return outputRange.clamp(p + i + d + f);
    }
    public double calculate(double current, double target){
        setTarget(target);
        return calculate(current);
    }
}
