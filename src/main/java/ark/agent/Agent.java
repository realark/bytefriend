package ark.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.Future;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class Agent {
  public static void premain(final String agentArgs, final Instrumentation inst) throws Exception {
    System.out.println("Enter premain");

    AgentBuilder agentBuilder =
      new AgentBuilder.Default()
      .disableClassFormatChanges()
      .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
      .ignore(nameStartsWith("ark.agent.Agent"))
      .or(nameStartsWith("net.bytebuddy"))
      .with(new LoggingListener());

    // java.util.concurrent.Future instrumentation
    agentBuilder =
      agentBuilder.type(not(isInterface()).and(hasSuperType(named(Future.class.getName()))))
      .transform(new AgentBuilder.Transformer.ForAdvice()
                 .advice(named("cancel").and(returns(boolean.class)),
                         FutureAdvice.class.getName()))
      .asDecorator();

    agentBuilder.installOn(inst);
  }

  public static class LoggingListener implements AgentBuilder.Listener {
    @Override
    public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
      // System.out.println("Discovered: " + typeName);
    }
    @Override
    public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
      System.out.println("Transformed: " + typeDescription + " :: " + classLoader);
    }
    @Override
    public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {
      // System.out.println("Ignored: " + typeDescription);
    }
    @Override
    public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable t) {
      t.printStackTrace();
    }
    @Override
    public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
    }
  }

  public static class FutureAdvice {
    @Advice.OnMethodEnter(suppress = Throwable.class)
    public static void beforeCancel(@Advice.This Future<?> future) {
      System.out.println("Before cancel: " + future);
    }

    @Advice.OnMethodExit(suppress = Throwable.class)
    public static void afterCancel(@Advice.Return boolean canceled) {
        System.out.println("After future cancel: " + canceled);
    }
  }
}
