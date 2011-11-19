package de.measite.gitserve.sshd.git.command

import org.apache.sshd.server.Command
import org.apache.sshd.server.Environment

class ErrorCommand(val error : String) extends BaseCommand {

    override def start(env : Environment) = {
        System.out.println("error: " + error)
        err.write({error + "\n"}.getBytes)
        err.flush
        callback.onExit(1, error)
    }

}