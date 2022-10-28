package com.example.demo;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class SSHTasklet implements Tasklet {
  @Override
  public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {

    SSHClient ssh = new SSHClient();
    ssh.addHostKeyVerifier(new PromiscuousVerifier());
    ssh.connect("localhost", 22022);
    
    ssh.authPassword("root", "mypassword");
    Session session = ssh.startSession();
    session.exec("mkdir /home/test");
    session.close();
    ssh.disconnect();

    return RepeatStatus.FINISHED;
  }
}
