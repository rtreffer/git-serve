package de.measite.gitserve.sshd.git.command

import org.apache.sshd.server.Environment
import org.eclipse.jgit.storage.file.FileRepository
import org.eclipse.jgit.transport.UploadPack

class GitUploadPack(repository : FileRepository) extends BaseCommand {

    override def start(env : Environment) : Unit = {
        // upload task
        val upload = new UploadPack(repository)
        val thread = new Thread("checkout/clone"){
            override def run() {
                try {
                    upload.upload(in, out, err)
                    callback.onExit(0)
                } catch {
                    case _ => {callback.onExit(1)}
                }
            }
        }
        thread.start
    }

}