package bot.Mathematics.Functions;

import bot.Mathematics.LinearAlgebra.Vector;

public interface ODEFunction {

    Vector evaluate(double t, Vector y);

}
