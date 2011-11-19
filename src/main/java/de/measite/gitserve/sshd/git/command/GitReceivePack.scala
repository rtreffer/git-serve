package de.measite.gitserve.sshd.git.command

import org.apache.sshd.server.Environment
import org.eclipse.jgit.storage.file.FileRepository
import org.eclipse.jgit.transport.ReceivePack

class GitReceivePack(repository : FileRepository)
extends BaseCommand {

    override def start(env : Environment) : Unit = {
        val receive = new ReceivePack(repository)
        val thread = new Thread("receive/push"){
            override def run() {
                try {
                    receive.receive(in, out, err)
                    callback.onExit(0)
                } catch {
                    case _ => {callback.onExit(1)}
                }
            }
        }
        thread.start
    }

}