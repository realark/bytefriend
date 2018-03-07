package ark.agent;

public class App {
  public static final String[] CLASSNAMES = {
    "akka.dispatch.Mailbox",
    "akka.dispatch.forkjoin.ForkJoinTask",
    "akka.dispatch.Mailboxes$$anon$1",
    "akka.dispatch.forkjoin.ForkJoinTask$AdaptedRunnableAction",
    "akka.dispatch.forkjoin.ForkJoinTask$AdaptedCallable",
    "akka.dispatch.forkjoin.ForkJoinTask$AdaptedRunnable",
    "akka.dispatch.Dispatcher$$anon$1",
    "akka.dispatch.ForkJoinExecutorConfigurator$AkkaForkJoinTask"
  };

  public static void main(String[] args) throws Exception {
    System.out.println("Enter Main");
    for (int i = 0; i < CLASSNAMES.length; ++i) {
      System.out.println("Load class " + Class.forName(CLASSNAMES[i]));
    }
  }
}
