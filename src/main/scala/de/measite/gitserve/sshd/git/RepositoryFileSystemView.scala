package de.measite.gitserve.sshd.git

import org.apache.sshd.server.FileSystemView
import org.apache.sshd.common.Session
import org.apache.sshd.server.SshFile

class RepositoryFileSystemView(session : Session) extends FileSystemView {

    override def getFile(file : String) = {
        System.out.println(file)
        null
    }

    override def getFile(base : SshFile, file : String) = {
        System.out.println(base + " :: " + file)
        null
    }
}