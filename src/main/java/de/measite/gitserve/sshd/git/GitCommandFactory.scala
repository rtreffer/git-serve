package de.measite.gitserve.sshd.git

import org.apache.sshd.server.CommandFactory
import org.apache.sshd.server.Command
import org.apache.sshd.server.command.UnknownCommand
import org.apache.sshd.server.shell.ProcessShellFactory
import org.apache.sshd.server.shell.InvertedShellWrapper
import org.eclipse.jgit.storage.file.FileRepository
import command._
import java.io.File

class GitCommandFactory extends CommandFactory {

    val repoPath = new File("./.git").getCanonicalFile

    override def createCommand(command: String) : Command = {
        System.out.println(command)
        if (command.startsWith("git-upload-pack ")) {
            val repository = new FileRepository(repoPath)
            return new GitUploadPack(repository)
        }
        if (command.startsWith("git-receive-pack ")) {
            val repository = new FileRepository(repoPath)
            return new GitReceivePack(repository)
        }
        System.out.println("error command")
        return new ErrorCommand("Unknown command: " + command)
    }

}