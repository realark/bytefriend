package ark.agent;

import java.lang.instrument.Instrumentation;

public class Agent {
    public static void premain(final String agentArgs, final Instrumentation inst) throws Exception {
        System.out.println("Enter premain");
    }
}
